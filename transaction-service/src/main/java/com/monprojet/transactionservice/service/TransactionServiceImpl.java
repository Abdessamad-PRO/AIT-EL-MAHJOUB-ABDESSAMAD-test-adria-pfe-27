package com.monprojet.transactionservice.service;

import com.monprojet.transactionservice.client.AccountClient;
import com.monprojet.transactionservice.domain.Transaction;
import com.monprojet.transactionservice.domain.TransactionCreatedEvent;
import com.monprojet.transactionservice.repository.TransactionRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountClient accountClient;
    private final TransactionEventProducer eventProducer;

    @Value("${bank.transaction.max-amount:1000}")
    private BigDecimal maxAmount;

    @Override
    @Transactional
    public Transaction createTransaction(UUID accountId, BigDecimal amount, String currency,
                                         String merchantCategory, String country) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant doit être strictement positif");
        }
        if (amount.compareTo(maxAmount) > 0) {
            throw new IllegalArgumentException("Le montant dépasse le plafond autorisé");
        }

        BigDecimal balance = accountClient.getBalance(accountId);
        if (balance.compareTo(amount) < 0) {
            throw new IllegalStateException("Solde insuffisant");
        }

        Transaction transaction = Transaction.builder()
                .accountId(accountId)
                .amount(amount)
                .currency(currency)
                .merchantCategory(merchantCategory)
                .country(country)
                .createdAt(Instant.now())
                .build();
        transaction = transactionRepository.save(transaction);

        accountClient.debit(accountId, amount);

        TransactionCreatedEvent event = TransactionCreatedEvent.builder()
                .eventId(UUID.randomUUID())
                .eventType("TransactionCreated")
                .transactionId(transaction.getId())
                .accountId(accountId)
                .amount(amount)
                .currency(currency)
                .occurredAt(transaction.getCreatedAt())
                .build();
        eventProducer.publish(event);

        return transaction;
    }

    @Override
    public List<Transaction> listByAccount(UUID accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

    @Override
    @Transactional
    @CircuitBreaker(name = "transfer", fallbackMethod = "transferFallback")
    public void transfer(UUID sourceAccountId, UUID targetAccountId, BigDecimal amount, String currency) {
        accountClient.debit(sourceAccountId, amount);
        accountClient.credit(targetAccountId, amount);

        // Créer deux transactions pour l'historique
        createTransaction(sourceAccountId, amount, currency, "TRANSFER", "INTERNAL");
        createTransaction(targetAccountId, amount, currency, "TRANSFER", "INTERNAL");
    }

    private void transferFallback(UUID source, UUID target, BigDecimal amount, String currency, Throwable t) {
        throw new RuntimeException("Échec du virement, veuillez réessayer plus tard", t);
    }
}