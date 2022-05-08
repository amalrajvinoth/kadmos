package io.kadmos.savings.model;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BalanceRequest {
    @NotBlank
    private OperationType operationType;
    @NotBlank
    private BigDecimal balance;

    public BalanceRequest(OperationType operationType, BigDecimal balance) {
        this.operationType = operationType;
        this.balance = balance;
    }
}
