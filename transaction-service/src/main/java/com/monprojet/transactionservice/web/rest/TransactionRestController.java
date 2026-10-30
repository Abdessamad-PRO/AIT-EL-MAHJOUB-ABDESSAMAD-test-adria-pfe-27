package com.monprojet.transactionservice.web.rest;


import com.monprojet.transactionservice.domain.Transaction;
import com.monprojet.transactionservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionRestController {

    private final TransactionService transactionService;

    @PostMapping
    public Transaction create(@RequestParam UUID accountId,
                              @RequestParam BigDecimal amount,
                              @RequestParam String currency,
                              @RequestParam String merchantCategory,
                              @RequestParam String country) {
        return transactionService.createTransaction(accountId, amount, currency, merchantCategory, country);
    }

    @GetMapping
    public List<Transaction> list(@RequestParam UUID accountId) {
        return transactionService.listByAccount(accountId);
    }

    @PostMapping("/transfer")
    public void transfer(@RequestParam UUID sourceAccountId,
                         @RequestParam UUID targetAccountId,
                         @RequestParam BigDecimal amount,
                         @RequestParam String currency) {
        transactionService.transfer(sourceAccountId, targetAccountId, amount, currency);
    }
}