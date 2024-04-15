package com.github.aayushjn.travelms.packages.model.request;

import com.github.aayushjn.travelms.packages.model.PackageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PackageUpdateRequest(@NotNull @NotEmpty @NotBlank String description,
                                   BigDecimal cost,
                                   @NotNull PackageType packageType,
                                   Integer hotelId,
                                   Integer busId) {
}
