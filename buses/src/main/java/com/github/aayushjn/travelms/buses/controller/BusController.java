package com.github.aayushjn.travelms.buses.controller;

import com.github.aayushjn.travelms.buses.model.Bus;
import com.github.aayushjn.travelms.buses.model.request.BusAddResponse;
import com.github.aayushjn.travelms.buses.model.request.BusUpdateRequest;
import com.github.aayushjn.travelms.buses.service.BusService;
import com.github.aayushjn.travelms.common.exceptions.AlreadyExistsException;
import com.github.aayushjn.travelms.common.exceptions.DoesNotExistException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("buses")
@CrossOrigin(origins = "*")
public class BusController {
    private final BusService service;

    @Autowired
    public BusController(BusService service) {
        this.service = service;
    }

    @GetMapping("")
    public ResponseEntity<List<Bus>> getAllBuses() {
        return new ResponseEntity<>(service.getAllBuses(), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<BusAddResponse> addBus(@RequestBody @Valid Bus bus) throws AlreadyExistsException {
        return new ResponseEntity<>(new BusAddResponse(service.addBus(bus)), HttpStatus.CREATED);
    }

    @DeleteMapping("/{busId}")
    public ResponseEntity<Void> deleteBus(@PathVariable int busId) throws DoesNotExistException {
        service.deleteBus(busId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{busId}")
    public ResponseEntity<Void> updateBus(@PathVariable int busId,
                                          @RequestBody @Valid BusUpdateRequest bus) throws DoesNotExistException {
        service.updateBus(busId, bus);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
