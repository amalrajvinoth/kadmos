package io.kadmos.savings.repository;

import io.kadmos.savings.entity.AccountEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountEntity, String> {
    // NO-OP
}
