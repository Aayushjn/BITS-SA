package com.github.aayushjn.travelms.users.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UserUpdateRequest(@NotNull @NotEmpty @NotBlank String name,
                                @NotNull @NotEmpty @NotBlank @Pattern(regexp = "^[789]\\d{9}", message = "Mobile No. should be 10 digits") String mobile,
                                @NotNull @NotEmpty @NotBlank String address) {
}
