package com.monprojet.transactionservice.service;


import com.monprojet.transactionservice.domain.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface TransactionService {
    Transaction createTransaction(UUID accountId, BigDecimal amount, String currency,
                                  String merchantCategory, String country);
    List<Transaction> listByAccount(UUID accountId);
    void transfer(UUID sourceAccountId, UUID targetAccountId, BigDecimal amount, String currency);
}