package com.monprojet.accountservice.web.rest;

import com.monprojet.accountservice.domain.Account;
import com.monprojet.accountservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountRestController {

    private final AccountService accountService;

    @PostMapping
    public Account create(@RequestParam String customerReference,
                          @RequestParam String currency,
                          @RequestParam double initialBalance) {
        return accountService.createAccount(customerReference, currency, initialBalance);
    }

    @GetMapping("/{id}")
    public Account get(@PathVariable UUID id) {
        return accountService.getAccount(id);
    }

    @GetMapping("/{id}/balance")
    public double getBalance(@PathVariable UUID id) {
        return accountService.getBalance(id);
    }

    @PatchMapping("/{id}/debit")
    public void debit(@PathVariable UUID id, @RequestParam double amount) {
        accountService.debit(id, amount);
    }

    @PatchMapping("/{id}/credit")
    public void credit(@PathVariable UUID id, @RequestParam double amount) {
        accountService.credit(id, amount);
    }
}