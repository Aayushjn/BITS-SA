package com.github.aayushjn.travelms.hotels.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record HotelUpdateRequest(@NotNull @NotEmpty @NotBlank String description,
                                 @NotNull @NotEmpty @NotBlank String address,
                                 @NotNull BigDecimal rent) {
}
