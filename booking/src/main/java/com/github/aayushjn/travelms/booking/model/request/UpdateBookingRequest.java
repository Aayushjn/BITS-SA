package com.github.aayushjn.travelms.booking.model.request;

import com.github.aayushjn.travelms.booking.model.BookingStatus;

public record UpdateBookingRequest(BookingStatus bookingStatus) {
}
