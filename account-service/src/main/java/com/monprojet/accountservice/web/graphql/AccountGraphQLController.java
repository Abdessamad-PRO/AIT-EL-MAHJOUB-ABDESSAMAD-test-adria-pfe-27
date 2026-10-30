package com.monprojet.accountservice.web.graphql;

import com.monprojet.accountservice.domain.Account;
import com.monprojet.accountservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class AccountGraphQLController {

    private final AccountService accountService;

    @QueryMapping
    public Account account(@Argument String id) {
        return accountService.getAccount(UUID.fromString(id));
    }

    @QueryMapping
    public double balance(@Argument String id) {
        return accountService.getBalance(UUID.fromString(id));
    }
}