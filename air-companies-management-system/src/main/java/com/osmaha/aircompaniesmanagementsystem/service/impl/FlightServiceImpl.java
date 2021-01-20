package com.osmaha.aircompaniesmanagementsystem.service.impl;

import com.osmaha.aircompaniesmanagementsystem.domain.*;
import com.osmaha.aircompaniesmanagementsystem.domain.enums.FlightStatusE;
import com.osmaha.aircompaniesmanagementsystem.payload.request.flight.FlightCreationRequest;
import com.osmaha.aircompaniesmanagementsystem.payload.request.flight.FlightStatusUpdateRequest;
import com.osmaha.aircompaniesmanagementsystem.payload.request.flight.FlightUpdateRequest;
import com.osmaha.aircompaniesmanagementsystem.repository.FlightRepository;
import com.osmaha.aircompaniesmanagementsystem.service.AirCompanyService;
import com.osmaha.aircompaniesmanagementsystem.service.AirplaneService;
import com.osmaha.aircompaniesmanagementsystem.service.FlightService;
import com.osmaha.aircompaniesmanagementsystem.service.exception.*;
import com.osmaha.aircompaniesmanagementsystem.service.util.CountryService;
import com.osmaha.aircompaniesmanagementsystem.service.util.FlightStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FlightServiceImpl implements FlightService {

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private FlightStatusService flightStatusService;

    @Autowired
    private AirCompanyService airCompanyService;

    @Autowired
    private AirplaneService airplaneService;

    @Autowired
    private CountryService countryService;

    @Transactional(readOnly = true)
    @Override
    public List<Flight> findAll() {
        return flightRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Flight> findAllByAirplane(Airplane airplane) {
        return flightRepository.findAllByAirplane(airplane);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Flight> findAllByAirCompanyNameAndFlightStatus(String airCompanyName, String flightStatus) throws ResourceNotFoundException {

        AirCompany existingAirCompany = airCompanyService.findByName(airCompanyName);

        FlightStatus searchedFlightStatus = flightStatusService.findByName(flightStatus);

        return flightRepository.findAllByAirCompanyAndFlightStatus(existingAirCompany, searchedFlightStatus);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Flight> findAllByFlightStatusAndStartedAtMoreThanHoursAgo(String flightStatus, int hoursAgo) throws ResourceNotFoundException, ConvertingParameterException {

        if (hoursAgo <= 0) throw new ConvertingParameterException("Parameter 'hourseAgo' can not be zero or negative");
        LocalDateTime beforeDateTime = LocalDateTime.now().minusHours(hoursAgo);

        FlightStatus searchedFlightStatus = flightStatusService.findByName(flightStatus);

        return flightRepository.findAllByFlightStatusAndStartedAtMoreThanHoursAgo(searchedFlightStatus, beforeDateTime);
    }

    @Override
    public List<Flight> findAllCompletedFlightsWithActualTimeBiggerThanEstimated() throws ResourceNotFoundException {

        FlightStatus statusCompleted = flightStatusService.findByName(FlightStatusE.COMPLETED.name());

        List<Flight> allCompletedFlights = flightRepository.findAllByFlightStatus(statusCompleted);

        return allCompletedFlights.stream().filter(flight -> {
            Duration actualFlightTime = Duration.between(flight.getStartedAt(), flight.getEndedAt());

            int result = actualFlightTime.compareTo(flight.getEstimatedFlightTime()); //returns positive if actualFlightTime is bigger
            return result > 0;
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public Flight findById(Long id) throws ResourceNotFoundException {
        return flightRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No flight available"));
    }

    @Transactional(rollbackFor = {ResourceNotFoundException.class, RegistrationException.class, ConvertingParameterException.class})
    @Override
    public Flight createFlight(FlightCreationRequest flightDTO) throws ResourceNotFoundException, RegistrationException, ConvertingParameterException {

        if (flightDTO.getDistance() <= 0)
            throw new RegistrationException("Flight distance (kilometers) can not be zero or negative");

        Duration estimatedFlightTime = convertStringToDuration(flightDTO.getEstimatedFlightTime());

        AirCompany existingAirCompany = airCompanyService.findById(flightDTO.getAirCompanyId());

        Airplane existingAirplane = airplaneService.findById(flightDTO.getAirplaneId());
        if (existingAirplane.getAirCompany() == null)
            throw new RegistrationException("Unassigned airplane can not be add to flight");

        Country departureCountry = countryService.findByName(flightDTO.getDepartureCountry());
        Country destinationCountry = countryService.findByName(flightDTO.getDestinationCountry());

        FlightStatus flightStatus = flightStatusService.findByName(FlightStatusE.PENDING.name());

        LocalDateTime cratedAt = LocalDateTime.now();

        Flight flight = new Flight(flightStatus,
                existingAirCompany,
                existingAirplane,
                departureCountry,
                destinationCountry,
                flightDTO.getDistance(),
                estimatedFlightTime,
                cratedAt);

        return flightRepository.save(flight);
    }

    @Transactional(rollbackFor = {ResourceNotFoundException.class, UpdateException.class, ConvertingParameterException.class})
    @Override
    public Flight updateFlight(Long id, FlightUpdateRequest updateRequest) throws ResourceNotFoundException, UpdateException, ConvertingParameterException {

        Optional<Flight> flightOptional = flightRepository.findById(id);
        Flight existingFlight = flightOptional.orElseThrow(() -> new ResourceNotFoundException("No flight available"));

        if (updateRequest.getDistance() <= 0)
            throw new UpdateException("Flight distance (kilometers) can not be zero or negative");

        Duration estimatedFlightTime = convertStringToDuration(updateRequest.getEstimatedFlightTime());

        AirCompany existingAirCompany = airCompanyService.findById(updateRequest.getAirCompanyId());

        Airplane existingAirplane = airplaneService.findById(updateRequest.getAirplaneId());
        if (existingAirplane.getAirCompany() == null)
            throw new UpdateException("Unassigned airplane can not be add to flight");

        Country departureCountry = countryService.findByName(updateRequest.getDepartureCountry());
        Country destinationCountry = countryService.findByName(updateRequest.getDestinationCountry());

        existingFlight.setAirCompany(existingAirCompany);
        existingFlight.setAirplane(existingAirplane);
        existingFlight.setDepartureCountry(departureCountry);
        existingFlight.setDestinationCountry(destinationCountry);
        existingFlight.setDistance(updateRequest.getDistance());
        existingFlight.setEstimatedFlightTime(estimatedFlightTime);

        return flightRepository.save(existingFlight);
    }

    @Transactional(rollbackFor = {ResourceNotFoundException.class, DeleteException.class})
    @Override
    public void deleteFlight(Long id) throws ResourceNotFoundException, DeleteException {

        Flight existingFlight = flightRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No flight available"));

        if (existingFlight.getFlightStatus().getName() == FlightStatusE.ACTIVE)
            throw new DeleteException("Flight with status ACTIVE can not be deleted");

        flightRepository.delete(existingFlight);
    }

    @Transactional(rollbackFor = {ResourceNotFoundException.class, UpdateException.class})
    @Override
    public void setFlightStatus(Long id, FlightStatusUpdateRequest updateRequest) throws ResourceNotFoundException, UpdateException {

        Flight existingFlight = flightRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No flight available"));
        FlightStatus flightStatus = flightStatusService.findByName(updateRequest.getStatus().name());

        setFlightStatus(existingFlight, flightStatus);
    }

    public void setFlightStatus(Flight flight, FlightStatus status) throws UpdateException {
        if (flight.getFlightStatus().getName() == FlightStatusE.COMPLETED)
            throw new UpdateException("Completed flight status can not be updated");

        if (flight.getFlightStatus().getName() == status.getName())
            throw new UpdateException("Such flight status is already set");

        if (status.getName() == FlightStatusE.PENDING)
            throw new UpdateException("PENDING flight status can not be set");

        if (status.getName() == FlightStatusE.DELAYED) updateFlightForDelayedStatusUpdate(flight, status);
        if (status.getName() == FlightStatusE.ACTIVE) updateFlightForActiveStatusUpdate(flight, status);
        if (status.getName() == FlightStatusE.COMPLETED) updateFlightForCompletedStatusUpdate(flight, status);

        flightRepository.save(flight);
    }

    private void updateFlightForDelayedStatusUpdate(Flight flight, FlightStatus status) throws UpdateException {
        FlightStatusE flightStatus = flight.getFlightStatus().getName();
        if (flightStatus == FlightStatusE.ACTIVE)
            throw new UpdateException("Such flight status can not be set");

        flight.setFlightStatus(status);
        flight.setDelayStartedAt(LocalDateTime.now());
    }

    private void updateFlightForActiveStatusUpdate(Flight flight, FlightStatus status) {
        flight.setFlightStatus(status);
        flight.setStartedAt(LocalDateTime.now());
    }

    private void updateFlightForCompletedStatusUpdate(Flight flight, FlightStatus status) throws UpdateException {
        FlightStatusE flightStatus = flight.getFlightStatus().getName();
        if (flightStatus != FlightStatusE.ACTIVE)
            throw new UpdateException("Such flight status can not be set");

        flight.setFlightStatus(status);
        flight.setEndedAt(LocalDateTime.now());
    }

    private Duration convertStringToDuration(String durationStr) throws ConvertingParameterException {

        String[] splitDurationStr = durationStr.split(":");
        if (splitDurationStr.length == 1 || splitDurationStr.length > 2)
            throw new ConvertingParameterException("String duration format is incorrect (should be 'HH:MM')");

        int hours;
        int minutes;
        try {
            hours = Integer.parseInt(splitDurationStr[0]);
            minutes = Integer.parseInt(splitDurationStr[1]);
        } catch (NumberFormatException e) {
            throw new ConvertingParameterException("Error with parsing string parameter to integer");
        }

        if (hours < 0 | minutes < 0) throw new ConvertingParameterException("Hours and minutes can not be negative");

        Duration duration = Duration.ZERO;
        duration = duration.plusHours(hours).plusMinutes(minutes);

        return duration;
    }
}
