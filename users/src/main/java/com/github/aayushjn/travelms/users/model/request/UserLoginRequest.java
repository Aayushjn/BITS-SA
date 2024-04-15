package com.github.aayushjn.travelms.users.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UserLoginRequest(@NotNull @NotEmpty @NotBlank String email,
                               @NotNull @NotEmpty @NotBlank String password) {
}
