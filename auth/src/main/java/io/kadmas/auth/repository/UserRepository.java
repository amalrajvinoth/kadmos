package io.kadmas.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import io.kadmas.auth.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByUserName(String username);
}
