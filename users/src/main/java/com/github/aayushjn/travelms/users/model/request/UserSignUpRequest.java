package com.github.aayushjn.travelms.users.model.request;

import com.github.aayushjn.travelms.users.constraints.ValidPassword;
import jakarta.validation.constraints.*;

public record UserSignUpRequest(@NotNull @NotEmpty @NotBlank String name,
                                @NotNull @NotEmpty @NotBlank @Email String email,
                                @NotNull @NotEmpty @NotBlank @Pattern(regexp = "^[789]\\d{9}", message = "Mobile No. should be 10 digits") String mobile,
                                @NotNull @NotEmpty @NotBlank String address,
                                @ValidPassword @NotNull @NotEmpty @NotBlank String password) {
}
