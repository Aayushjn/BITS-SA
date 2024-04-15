package com.github.aayushjn.travelms.users.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Integer userId;

    @NotBlank
    @NotEmpty
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false)
    private String mobile;

    @NotBlank
    @NotEmpty
    @Column(nullable = false)
    private String address;

    @Email(message = "Invalid Email address.")
    @Column(unique = true, nullable = false)
    private String email;

    @NotNull
    @NotBlank
    @NotEmpty
    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private UserType userType = UserType.GENERAL;
}
