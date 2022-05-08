package io.kadmos.savings.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import io.kadmos.savings.entity.AccountEntity;
import io.kadmos.savings.exception.AccountException;
import io.kadmos.savings.exception.AccountNotFoundException;
import io.kadmos.savings.model.BalanceRequest;
import io.kadmos.savings.model.BalanceResponse;
import io.kadmos.savings.model.OperationType;
import io.kadmos.savings.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService implements IAccountService {
    private static final String ACCOUNT_LOCK = "account_lock_%s";
    private static final Long LOCK_LEASE_TIME = 0L;
    private static final Long LOCK_WAIT_TIME = 3000L;

    private final AccountRepository repository;
    private final SemaphoreBasedDistributedLocker distributedLocker;

    @Override
    @Transactional
    public BalanceResponse updateBalance(String accountId, BalanceRequest balanceRequest) {
        try {
            String lockingKey = String.format(ACCOUNT_LOCK, accountId);
            try {
                log.info("Acquiring lock with key: {}", lockingKey);
                distributedLocker.acquire(lockingKey, LOCK_LEASE_TIME, LOCK_WAIT_TIME);
                Optional<AccountEntity> accountEntity = repository.findById(accountId);
                accountEntity.orElseThrow(() -> new AccountNotFoundException("account not found with id= " + accountId));

                AtomicReference<BigDecimal> newBalance = new AtomicReference<>();
                accountEntity.ifPresent(a -> {
                    newBalance.set(a.getBalance());
                    if (Objects.equals(balanceRequest.getOperationType(), OperationType.DEPOSIT)) {
                        newBalance.set(a.getBalance().add(balanceRequest.getBalance()));
                    } else if (Objects.equals(balanceRequest.getOperationType(), OperationType.WITHDRAW)) {
                        newBalance.set(a.getBalance().subtract(balanceRequest.getBalance()));
                    }
                    a.setBalance(newBalance.get());
                    repository.save(a);
                });

                return new BalanceResponse(newBalance.get());
            } finally {
                log.info("Releasing lock for key: {} ", lockingKey);
                distributedLocker.release(lockingKey);
            }
        } catch (Exception exception) {
            log.error("Failed to perform {} operation on account:{}, reason: {}",
                    balanceRequest.getOperationType(),
                    accountId,
                    exception);
            throw new AccountException("Failed to update account balance", exception);
        }
    }

    @Override
    public BalanceResponse getBalance(String accountId) {
        BigDecimal balance = repository
                .findById(accountId)
                .map(AccountEntity::getBalance)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
        return new BalanceResponse(balance);
    }
}
