package com.monprojet.transactionservice.kafka;

import com.monprojet.transactionservice.domain.TransactionCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionEventProducer {

    private static final String TOPIC = "bank.transactions.created";
    private final KafkaTemplate<String, TransactionCreatedEvent> kafkaTemplate;

    public void publish(TransactionCreatedEvent event) {
        kafkaTemplate.send(TOPIC, event.getAccountId().toString(), event);
    }
}