package com.monprojet.accountservice;

import com.monprojet.accountservice.domain.Account;
import com.monprojet.accountservice.repository.AccountRepository;
import com.monprojet.accountservice.status.AccountStatus;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AccountServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(AccountRepository accountRepository) {
        return args -> {
            accountRepository.save(Account.builder()
                    .customerReference("CUST-001")
                    .currency("MAD")
                    .status(AccountStatus.ACTIVE)
                    .balance(1500.00)
                    .build());

            accountRepository.save(Account.builder()
                    .customerReference("CUST-002")
                    .currency("EUR")
                    .status(AccountStatus.ACTIVE)
                    .balance(320.50)
                    .build());

            accountRepository.save(Account.builder()
                    .customerReference("CUST-003")
                    .currency("USD")
                    .status(AccountStatus.BLOCKED)
                    .balance(0.00)
                    .build());

            accountRepository.findAll().forEach(a -> {
                System.out.println("======================");
                System.out.println(a.getId());
                System.out.println(a.getCustomerReference());
                System.out.println(a.getCurrency());
                System.out.println(a.getStatus());
                System.out.println(a.getBalance());
                System.out.println("======================");
            });
        };
    }
}