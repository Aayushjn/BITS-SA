package com.github.aayushjn.travelms.hotels.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "hotels")
public class Hotel {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Integer hotelId;

    @NotBlank
    @NotEmpty
    @Column(nullable = false, unique = true)
    private String name;

    @NotBlank
    @NotEmpty
    @Column(nullable = false)
    private String description;

    @NotBlank
    @NotEmpty
    @Column(nullable = false)
    private String address;

    @Column(precision = 7, scale = 2)
    private BigDecimal rent;

    @Enumerated(EnumType.STRING)
    private HotelType hotelType = HotelType.AC;
}
