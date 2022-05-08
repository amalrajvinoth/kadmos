package io.kadmos.savings.service;

import io.kadmos.savings.model.BalanceRequest;
import io.kadmos.savings.model.BalanceResponse;

public interface IAccountService {

    BalanceResponse updateBalance(String accountId, BalanceRequest balanceRequest);

    BalanceResponse getBalance(String accountId);
}
