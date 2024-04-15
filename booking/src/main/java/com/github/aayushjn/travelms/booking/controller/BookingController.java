package com.github.aayushjn.travelms.booking.controller;

import com.github.aayushjn.travelms.booking.model.Booking;
import com.github.aayushjn.travelms.booking.model.request.MakeBookingResponse;
import com.github.aayushjn.travelms.booking.model.request.UpdateBookingRequest;
import com.github.aayushjn.travelms.booking.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@CrossOrigin(origins = "*")
public class BookingController {
    private final BookingService service;

    @Autowired
    public BookingController(BookingService service) {
        this.service = service;
    }

    @PostMapping("")
    public ResponseEntity<MakeBookingResponse> makeBooking(@RequestHeader("X-Auth-Key") String authKey,
                                                           @Valid @RequestBody Booking booking) throws Throwable {
        return new ResponseEntity<>(new MakeBookingResponse(service.makeBooking(authKey, booking)), HttpStatus.OK);
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<Void> cancelBooking(@RequestHeader("X-Auth-Key") String authKey,
                                              @PathVariable Integer bookingId) throws Throwable {
        service.cancelBooking(authKey, bookingId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Booking> getBooking(@RequestHeader("X-Auth-Key") String authKey,
                                              @PathVariable Integer bookingId) throws Throwable {
        return new ResponseEntity<>(service.getBooking(authKey, bookingId), HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<Booking>> getAllBookings(@RequestHeader("X-Auth-Key") String authKey) throws Throwable {
        return new ResponseEntity<>(service.getAllBookings(authKey), HttpStatus.OK);
    }

    @PutMapping("/{bookingId}")
    public ResponseEntity<Void> makeBooking(@RequestHeader("X-Auth-Key") String authKey,
                                            @PathVariable Integer bookingId,
                                            @Valid @RequestBody UpdateBookingRequest updateBookingRequest) throws Throwable {
        service.updateBooking(authKey, bookingId, updateBookingRequest);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
