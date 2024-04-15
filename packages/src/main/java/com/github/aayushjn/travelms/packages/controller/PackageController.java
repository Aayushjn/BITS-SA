package com.github.aayushjn.travelms.packages.controller;

import com.github.aayushjn.travelms.common.exceptions.DoesNotExistException;
import com.github.aayushjn.travelms.packages.model.Package;
import com.github.aayushjn.travelms.packages.model.request.PackageAddResponse;
import com.github.aayushjn.travelms.packages.model.request.PackageUpdateRequest;
import com.github.aayushjn.travelms.packages.service.PackageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("packages")
@CrossOrigin(origins = "*")
public class PackageController {
    private final PackageService service;

    @Autowired
    public PackageController(PackageService service) {
        this.service = service;
    }

    @GetMapping("")
    public ResponseEntity<List<Package>> getAllPackages() {
        return new ResponseEntity<>(service.getAllPackages(), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<PackageAddResponse> addPackage(@RequestBody @Valid Package pkg) throws Throwable {
        return new ResponseEntity<>(new PackageAddResponse(service.addPackage(pkg)), HttpStatus.CREATED);
    }

    @DeleteMapping("/{packageId}")
    public ResponseEntity<Void> deletePackage(@PathVariable int packageId) throws DoesNotExistException {
        service.deletePackage(packageId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{packageId}")
    public ResponseEntity<Void> updatePackage(@PathVariable int packageId,
                                              @RequestBody @Valid PackageUpdateRequest pkg) throws Throwable {
        service.updatePackage(packageId, pkg);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
