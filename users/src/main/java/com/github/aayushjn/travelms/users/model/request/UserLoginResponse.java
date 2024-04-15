package com.github.aayushjn.travelms.users.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UserLoginResponse(@NotNull @NotEmpty @NotBlank String authKey) {
}
