package com.osmaha.aircompaniesmanagementsystem.controller;

import com.osmaha.aircompaniesmanagementsystem.domain.AirCompany;
import com.osmaha.aircompaniesmanagementsystem.payload.request.aircompany.AirCompanyCreationRequest;
import com.osmaha.aircompaniesmanagementsystem.payload.request.aircompany.AirCompanyUpdateRequest;
import com.osmaha.aircompaniesmanagementsystem.service.AirCompanyService;
import com.osmaha.aircompaniesmanagementsystem.service.exception.RegistrationException;
import com.osmaha.aircompaniesmanagementsystem.service.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/air-companies")
public class AirCompanyController {

    @Autowired
    private AirCompanyService airCompanyService;

    @GetMapping
    public ResponseEntity<?> getAirCompanies() {

        List<AirCompany> airCompanies = airCompanyService.findAll();
        return new ResponseEntity<>(airCompanies, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAirCompanyById(@PathVariable("id") Long id) {

        AirCompany airCompany;
        try {
            airCompany = airCompanyService.findById(id);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(airCompany, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createAirCompany(@RequestBody AirCompanyCreationRequest airCompanyDTO) {

        AirCompany airCompany;
        try {
            airCompany = airCompanyService.createAirCompany(airCompanyDTO);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RegistrationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(airCompany, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAirCompanyName(@PathVariable Long id, @RequestBody AirCompanyUpdateRequest updateRequest) {

        AirCompany airCompany;
        try {
            airCompany = airCompanyService.updateAirCompany(id, updateRequest);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(airCompany, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAirCompany(@PathVariable Long id) {

        try {
            airCompanyService.deleteAirCompany(id);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
