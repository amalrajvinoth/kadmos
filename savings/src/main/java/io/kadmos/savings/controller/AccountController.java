package io.kadmos.savings.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.kadmos.savings.model.BalanceRequest;
import io.kadmos.savings.model.BalanceResponse;
import io.kadmos.savings.service.AccountService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/accounts/{accountId}/balance")
    public BalanceResponse updateBalance(@PathVariable String accountId, @RequestBody BalanceRequest updateBalance) {
        return accountService.updateBalance(accountId, updateBalance);
    }

    @GetMapping("/accounts/{accountId}/balance")
    public BalanceResponse getBalance(@PathVariable String accountId) {
        return accountService.getBalance(accountId);
    }
}
