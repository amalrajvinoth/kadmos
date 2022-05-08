package io.kadmas.auth.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "user_role")
public class UserEntity {
    @Id
    private String id;
    private String userName;
    private String password;
    private String roles;
}
