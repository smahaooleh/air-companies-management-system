package com.osmaha.aircompaniesmanagementsystem.controller;

import com.osmaha.aircompaniesmanagementsystem.domain.Flight;
import com.osmaha.aircompaniesmanagementsystem.payload.request.flight.*;
import com.osmaha.aircompaniesmanagementsystem.service.FlightService;
import com.osmaha.aircompaniesmanagementsystem.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/flights")
public class FlightController {

    @Autowired
    private FlightService flightService;

    @GetMapping
    public ResponseEntity<?> getFlights() {

        List<Flight> flights = flightService.findAll();
        return new ResponseEntity<>(flights, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFlightById(@PathVariable("id") Long id) {

        Flight flight;
        try {
            flight = flightService.findById(id);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(flight, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createFlight(@RequestBody FlightCreationRequest flightDTO) {

        Flight flight;
        try {
            flight = flightService.createFlight(flightDTO);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RegistrationException | ConvertingParameterException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(flight, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFlight(@PathVariable Long id, @RequestBody FlightUpdateRequest updateRequest) {

        Flight flight;
        try {
            flight = flightService.updateFlight(id, updateRequest);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (UpdateException | ConvertingParameterException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(flight, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFlight(@PathVariable Long id) {

        try {
            flightService.deleteFlight(id);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (DeleteException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> patchFlightStatus(@PathVariable(name = "id") Long id, @RequestBody FlightStatusUpdateRequest flightStatus) {

        try {
            flightService.setFlightStatus(id, flightStatus);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (UpdateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("/specific-filter/1")
    public ResponseEntity<?> getFlightsByAirCompanyNameAndFlightStatus(@RequestBody FilteringByAirCompanyNameAndFlightStatusRequest filter) {

        List<Flight> flights;
        try {
            flights = flightService.findAllByAirCompanyNameAndFlightStatus(filter.getAirCompanyName(), filter.getFlightStatus());
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(flights, HttpStatus.OK);
    }

    @GetMapping("/specific-filter/2")
    public ResponseEntity<?> getFlightsByActiveStatusAndStartedHoursAgo(@RequestBody FilteringByActiveFlightStatusAndStartedHoursAgoRequest filter) {

        List<Flight> flights;
        try {
            flights = flightService.findAllByFlightStatusAndStartedAtMoreThanHoursAgo(filter.getFlightStatus(), filter.getHoursAgo());
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ConvertingParameterException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(flights, HttpStatus.OK);
    }

    @GetMapping("/specific-filter/3")
    public ResponseEntity<?> getCompletedFlightsWithActualFlightTimeBiggerThanEstimated() {

        List<Flight> flights;
        try {
            flights = flightService.findAllCompletedFlightsWithActualTimeBiggerThanEstimated();
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(flights, HttpStatus.OK);
    }
}
