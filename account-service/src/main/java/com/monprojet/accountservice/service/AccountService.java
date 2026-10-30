package com.monprojet.accountservice.service;

import com.monprojet.accountservice.domain.Account;

import java.util.UUID;

public interface AccountService {
    Account createAccount(String customerReference, String currency, double initialBalance);
    Account getAccount(UUID id);
    double getBalance(UUID id);
    void debit(UUID id, double amount);
    void credit(UUID id, double amount);
    boolean isAccountActive(UUID id);
}