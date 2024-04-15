package com.github.aayushjn.travelms.buses.model.request;

import com.github.aayushjn.travelms.buses.model.BusType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record BusUpdateRequest(@NotNull @NotEmpty @NotBlank String company,
                               @NotNull BigDecimal cost,
                               @NotNull BusType busType) {
}
