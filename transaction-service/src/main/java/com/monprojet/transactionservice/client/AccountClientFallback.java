package com.monprojet.transactionservice.client;


import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.UUID;

@Component
public class AccountClientFallback implements AccountClient {

    @Override
    public BigDecimal getBalance(UUID id) {
        throw new RuntimeException("Service de compte indisponible");
    }

    @Override
    public void debit(UUID id, BigDecimal amount) {
        throw new RuntimeException("Service de compte indisponible");
    }

    @Override
    public void credit(UUID id, BigDecimal amount) {
        throw new RuntimeException("Service de compte indisponible");
    }
}