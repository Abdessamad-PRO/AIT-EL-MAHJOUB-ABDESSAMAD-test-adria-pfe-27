package com.monprojet.accountservice.service;

import com.monprojet.accountservice.domain.Account;
import com.monprojet.accountservice.exception.BusinessException;
import com.monprojet.accountservice.exception.ResourceNotFoundException;
import com.monprojet.accountservice.repository.AccountRepository;
import com.monprojet.accountservice.status.AccountStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public Account createAccount(String customerReference, String currency, double initialBalance) {
        if (customerReference == null || customerReference.isBlank()) {
            throw new BusinessException("La référence client est obligatoire");
        }
        if (currency == null || currency.isBlank()) {
            throw new BusinessException("La devise est obligatoire");
        }
        if (!Double.isFinite(initialBalance) || initialBalance < 0) {
            throw new BusinessException("Le solde initial doit être positif ou nul");
        }

        Account account = Account.builder()
                .customerReference(customerReference)
                .currency(currency.toUpperCase())
                .status(AccountStatus.ACTIVE)
                .balance(initialBalance)
                .build();
        return accountRepository.save(account);
    }

    @Override
    @Transactional(readOnly = true)
    public Account getAccount(UUID id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compte introuvable : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public double getBalance(UUID id) {
        return getAccount(id).getBalance();
    }

    @Override
    @Transactional
    public void debit(UUID id, double amount) {
        checkPositiveAmount(amount);
        Account account = getActiveAccount(id);

        double nouveauSolde = account.getBalance() - amount;
        if (nouveauSolde < 0) {
            throw new BusinessException("Solde insuffisant : "
                    + account.getBalance() + " " + account.getCurrency());
        }

        account.setBalance(nouveauSolde);
        accountRepository.save(account);
    }

    @Override
    @Transactional
    public void credit(UUID id, double amount) {
        checkPositiveAmount(amount);
        Account account = getActiveAccount(id);

        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isAccountActive(UUID id) {
        return getAccount(id).getStatus() == AccountStatus.ACTIVE;
    }

    private Account getActiveAccount(UUID id) {
        Account account = getAccount(id);
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new BusinessException("Le compte " + id + " est bloqué");
        }
        return account;
    }

    private void checkPositiveAmount(double amount) {
        if (!Double.isFinite(amount) || amount <= 0) {
            throw new BusinessException("Le montant doit être strictement positif");
        }
    }
}