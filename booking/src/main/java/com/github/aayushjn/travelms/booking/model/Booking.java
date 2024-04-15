package com.github.aayushjn.travelms.booking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "bookings")
public class Booking {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Integer bookingId;

    @Enumerated(EnumType.STRING)
    private BookingType bookingType;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus = BookingStatus.PAYMENT_PENDING;

    private Integer userId;

    @NotNull
    private Integer packageId;
}
