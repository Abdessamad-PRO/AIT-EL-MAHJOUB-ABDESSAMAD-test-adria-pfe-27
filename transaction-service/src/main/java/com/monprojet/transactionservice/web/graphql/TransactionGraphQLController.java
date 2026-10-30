package com.monprojet.transactionservice.web.graphql;

import com.monprojet.transactionservice.domain.Transaction;
import com.monprojet.transactionservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class TransactionGraphQLController {

    private final TransactionService transactionService;

    @QueryMapping
    public List<Transaction> transactions(@Argument String accountId) {
        return transactionService.listByAccount(UUID.fromString(accountId));
    }
}