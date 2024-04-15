package com.github.aayushjn.travelms.packages.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "packages")
public class Package {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Integer packageId;

    @NotBlank
    @NotEmpty
    @Column(nullable = false, unique = true)
    private String name;

    @NotBlank
    @NotEmpty
    @Column(nullable = false)
    private String description;

    @Column(precision = 7, scale = 2)
    private BigDecimal cost;

    @Enumerated(EnumType.STRING)
    private PackageType packageType;

    @Column(nullable = true)
    private Integer hotelId;

    @Column(nullable = true)
    private Integer busId;
}
