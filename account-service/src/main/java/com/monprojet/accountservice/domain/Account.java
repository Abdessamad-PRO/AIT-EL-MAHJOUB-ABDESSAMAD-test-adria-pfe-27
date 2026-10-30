package com.monprojet.accountservice.domain;

import com.monprojet.accountservice.status.AccountStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity @Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String customerReference;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status = AccountStatus.ACTIVE;
    @Column(nullable = false, length = 3)
    private String currency;
    @Column(nullable = false)
    private double balance;
}
