package com.monprojet.transactionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.monprojet.transactionservice.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.UUID;

@SpringBootApplication
@Slf4j
@EnableFeignClients
public class TransactionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransactionServiceApplication.class, args);
    }
    @Bean
    CommandLineRunner seedTransactions(
            TransactionService transactionService,
            @Value("${app.seed.enabled:true}") boolean seedEnabled,
            @Value("${app.seed.account-id:}") String accountIdStr
    ) {
        return args -> {
            if (!seedEnabled) {
                log.info("[SEED] Désactivé (app.seed.enabled=false)");
                return;
            }

            if (accountIdStr == null || accountIdStr.isBlank()) {
                log.warn("[SEED] Aucun accountId fourni (app.seed.account-id). "
                        + "Ajoute-le dans application.yml pour activer le seeding.");
                return;
            }
            UUID accountId;
            try {
                accountId = UUID.fromString(accountIdStr);
            } catch (IllegalArgumentException e) {
                log.error("[SEED] account-id invalide : {} ({})", accountIdStr, e.getMessage());
                return;
            }

            log.info("[SEED] Démarrage du seeding de transactions pour le compte {}", accountId);

            seedOne(transactionService, accountId,
                    new BigDecimal("50.00"), "EUR", "GROCERY", "FR");

            seedOne(transactionService, accountId,
                    new BigDecimal("120.50"), "EUR", "RESTAURANT", "FR");

            seedOne(transactionService, accountId,
                    new BigDecimal("300.00"), "EUR", "ELECTRONICS", "MA");

            seedOne(transactionService, accountId,
                    new BigDecimal("999.99"), "EUR", "LUXURY", "MC");

            log.info("[SEED] Seeding terminé.");
        };
    }

    private void seedOne(TransactionService transactionService,
                         UUID accountId,
                         BigDecimal amount,
                         String currency,
                         String merchantCategory,
                         String country) {
        try {
            var tx = transactionService.createTransaction(
                    accountId, amount, currency, merchantCategory, country
            );
            log.info("[SEED] ✅ Transaction créée : id={}, amount={} {}, merchant={}",
                    tx.getId(), tx.getAmount(), tx.getCurrency(), tx.getMerchantCategory());
        } catch (Exception e) {
            log.warn("[SEED] ❌ Transaction échouée (amount={} {} merchant={}) : {}",
                    amount, currency, merchantCategory, e.getMessage());
        }
    }
}