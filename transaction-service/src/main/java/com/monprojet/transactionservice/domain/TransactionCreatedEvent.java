package com.monprojet.transactionservice.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TransactionCreatedEvent {
    private UUID eventId;
    private String eventType;
    private UUID transactionId;
    private UUID accountId;
    private BigDecimal amount;
    private String currency;
    private Instant occurredAt;
}