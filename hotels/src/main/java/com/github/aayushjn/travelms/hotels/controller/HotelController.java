package com.github.aayushjn.travelms.hotels.controller;

import com.github.aayushjn.travelms.common.exceptions.AlreadyExistsException;
import com.github.aayushjn.travelms.common.exceptions.DoesNotExistException;
import com.github.aayushjn.travelms.hotels.model.Hotel;
import com.github.aayushjn.travelms.hotels.model.request.HotelAddResponse;
import com.github.aayushjn.travelms.hotels.model.request.HotelUpdateRequest;
import com.github.aayushjn.travelms.hotels.service.HotelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("hotels")
@CrossOrigin(origins = "*")
public class HotelController {
    private final HotelService service;

    @Autowired
    public HotelController(HotelService service) {
        this.service = service;
    }

    @GetMapping("")
    public ResponseEntity<List<Hotel>> getAllHotels() {
        return new ResponseEntity<>(service.getAllHotels(), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<HotelAddResponse> addHotel(@RequestBody @Valid Hotel hotel) throws AlreadyExistsException {
        return new ResponseEntity<>(new HotelAddResponse(service.addHotel(hotel)), HttpStatus.CREATED);
    }

    @DeleteMapping("/{hotelId}")
    public ResponseEntity<Void> deleteHotel(@PathVariable int hotelId) throws DoesNotExistException {
        service.deleteHotel(hotelId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{hotelId}")
    public ResponseEntity<Void> updateHotel(@PathVariable int hotelId,
                                            @RequestBody @Valid HotelUpdateRequest hotel) throws DoesNotExistException {
        service.updateHotel(hotelId, hotel);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
