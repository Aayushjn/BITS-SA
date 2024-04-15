package com.github.aayushjn.travelms.buses.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "buses")
public class Bus {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Integer busId;

    @NotBlank
    @NotEmpty
    @Column(nullable = false, unique = true)
    private String name;

    @NotBlank
    @NotEmpty
    @Column(nullable = false)
    private String company;

    @Column(precision = 7, scale = 2)
    private BigDecimal cost;

    @Enumerated(EnumType.STRING)
    private BusType busType = BusType.AC;
}
