package com.monprojet.transactionservice.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.math.BigDecimal;
import java.util.UUID;

@FeignClient(name = "account-service", fallback = AccountClientFallback.class)
public interface AccountClient {

    @GetMapping("/api/accounts/{id}/balance")
    BigDecimal getBalance(@PathVariable UUID id);

    @PatchMapping("/api/accounts/{id}/debit")
    void debit(@PathVariable UUID id, @RequestParam BigDecimal amount);

    @PatchMapping("/api/accounts/{id}/credit")
    void credit(@PathVariable UUID id, @RequestParam BigDecimal amount);
}