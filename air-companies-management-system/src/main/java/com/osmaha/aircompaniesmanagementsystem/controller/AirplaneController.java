package com.osmaha.aircompaniesmanagementsystem.controller;


import com.osmaha.aircompaniesmanagementsystem.domain.Airplane;
import com.osmaha.aircompaniesmanagementsystem.payload.request.airplane.AirplaneCreationRequest;
import com.osmaha.aircompaniesmanagementsystem.payload.request.airplane.AirplaneUpdateRequest;
import com.osmaha.aircompaniesmanagementsystem.service.AirplaneService;
import com.osmaha.aircompaniesmanagementsystem.service.exception.DeleteException;
import com.osmaha.aircompaniesmanagementsystem.service.exception.RegistrationException;
import com.osmaha.aircompaniesmanagementsystem.service.exception.ResourceNotFoundException;
import com.osmaha.aircompaniesmanagementsystem.service.exception.UpdateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/airplanes")
public class AirplaneController {

    @Autowired
    private AirplaneService airplaneService;

    @GetMapping
    public ResponseEntity<?> getAirplanes() {

        List<Airplane> airplanes = airplaneService.findAll();
        return new ResponseEntity<>(airplanes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAirplaneById(@PathVariable("id") Long id) {

        Airplane airplane;
        try {
            airplane = airplaneService.findById(id);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(airplane, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createAirplane(@RequestBody AirplaneCreationRequest airplaneDTO) {

        Airplane airplane;
        try {
            airplane = airplaneService.createAirplane(airplaneDTO);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RegistrationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(airplane, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAirplane(@PathVariable Long id, @RequestBody AirplaneUpdateRequest updateRequest) {

        Airplane airplane;
        try {
            airplane = airplaneService.updateAirplane(id, updateRequest);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (UpdateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(airplane, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAirplane(@PathVariable Long id) {

        try {
            airplaneService.deleteAirplane(id);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (DeleteException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PatchMapping("/{id}/reassign")
    public ResponseEntity<?> reassignAirplaneToAnotherCompany(@PathVariable(name = "id") Long airplaneId, @RequestParam(name = "airCompanyId") Long airCompanyId) {

        try {
            airplaneService.reassignAirplaneToAnotherCompany(airplaneId, airCompanyId);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (UpdateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
