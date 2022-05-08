package io.kadmos.savings.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Account {
    private String id;
    @NotBlank
    private String userName;
    @NotBlank
    private String password;
    private String roles;
}
