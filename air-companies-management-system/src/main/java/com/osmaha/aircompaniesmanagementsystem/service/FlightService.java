package com.osmaha.aircompaniesmanagementsystem.service;

import com.osmaha.aircompaniesmanagementsystem.domain.Airplane;
import com.osmaha.aircompaniesmanagementsystem.domain.Flight;
import com.osmaha.aircompaniesmanagementsystem.payload.request.flight.FlightCreationRequest;
import com.osmaha.aircompaniesmanagementsystem.payload.request.flight.FlightStatusUpdateRequest;
import com.osmaha.aircompaniesmanagementsystem.payload.request.flight.FlightUpdateRequest;
import com.osmaha.aircompaniesmanagementsystem.service.exception.*;

import java.util.List;

public interface FlightService {

    List<Flight> findAll();

    List<Flight> findAllByAirplane(Airplane airplane);

    List<Flight> findAllByAirCompanyNameAndFlightStatus(String airCompanyName, String flightStatus) throws ResourceNotFoundException;

    List<Flight> findAllByFlightStatusAndStartedAtMoreThanHoursAgo(String flightStatus, int hoursAgo) throws ResourceNotFoundException, ConvertingParameterException;

    List<Flight> findAllCompletedFlightsWithActualTimeBiggerThanEstimated() throws ResourceNotFoundException;

    Flight findById(Long id) throws ResourceNotFoundException;

    Flight createFlight(FlightCreationRequest flightDTO) throws ResourceNotFoundException, RegistrationException, ConvertingParameterException;

    Flight updateFlight(Long id, FlightUpdateRequest updateRequest) throws ResourceNotFoundException, UpdateException, ConvertingParameterException;

    void deleteFlight(Long id) throws ResourceNotFoundException, DeleteException;

    void setFlightStatus(Long id, FlightStatusUpdateRequest updateRequest) throws ResourceNotFoundException, UpdateException;

}
