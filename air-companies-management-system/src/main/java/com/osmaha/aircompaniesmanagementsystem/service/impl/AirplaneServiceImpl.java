package com.osmaha.aircompaniesmanagementsystem.service.impl;

import com.osmaha.aircompaniesmanagementsystem.domain.AirCompany;
import com.osmaha.aircompaniesmanagementsystem.domain.Airplane;
import com.osmaha.aircompaniesmanagementsystem.domain.AirplaneType;
import com.osmaha.aircompaniesmanagementsystem.domain.Flight;
import com.osmaha.aircompaniesmanagementsystem.domain.enums.FlightStatusE;
import com.osmaha.aircompaniesmanagementsystem.payload.request.airplane.AirplaneCreationRequest;
import com.osmaha.aircompaniesmanagementsystem.payload.request.airplane.AirplaneUpdateRequest;
import com.osmaha.aircompaniesmanagementsystem.repository.AirplaneRepository;
import com.osmaha.aircompaniesmanagementsystem.service.AirCompanyService;
import com.osmaha.aircompaniesmanagementsystem.service.AirplaneService;
import com.osmaha.aircompaniesmanagementsystem.service.FlightService;
import com.osmaha.aircompaniesmanagementsystem.service.exception.DeleteException;
import com.osmaha.aircompaniesmanagementsystem.service.exception.RegistrationException;
import com.osmaha.aircompaniesmanagementsystem.service.exception.ResourceNotFoundException;
import com.osmaha.aircompaniesmanagementsystem.service.exception.UpdateException;
import com.osmaha.aircompaniesmanagementsystem.service.util.AirplaneTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.module.ResolutionException;
import java.util.List;
import java.util.Optional;

@Service
public class AirplaneServiceImpl implements AirplaneService {

    @Autowired
    private AirplaneRepository airplaneRepository;

    @Autowired
    private AirplaneTypeService airplaneTypeService;

    @Autowired
    private AirCompanyService airCompanyService;

    @Autowired
    private FlightService flightService;

    @Transactional(readOnly = true)
    @Override
    public List<Airplane> findAll() {
        return airplaneRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Airplane findById(Long id) throws ResourceNotFoundException {
        return airplaneRepository.findById(id).orElseThrow(() -> new ResolutionException("No airplane available"));
    }

    @Transactional(rollbackFor = {ResourceNotFoundException.class, RegistrationException.class})
    @Override
    public Airplane createAirplane(AirplaneCreationRequest airplaneDTO) throws ResourceNotFoundException, RegistrationException {

        Optional<Airplane> airplaneOptional = airplaneRepository.findByFactorySerialNumber(airplaneDTO.getFactorySerialNumber());
        if (airplaneOptional.isPresent()) throw new RegistrationException("Such airplane already exists");

        AirplaneType airplaneType = airplaneTypeService.findByName(airplaneDTO.getType());

        Airplane airplane;

        if (airplaneDTO.getAirCompanyId() == -1) {
            airplane = new Airplane(airplaneDTO.getName(),
                    airplaneDTO.getFactorySerialNumber(),
                    airplaneDTO.getNumberOfFlights(),
                    airplaneDTO.getFlightDistance(),
                    airplaneDTO.getFuelCapacity(),
                    airplaneType);
        } else {
            AirCompany existingAirCompany = airCompanyService.findById(airplaneDTO.getAirCompanyId());

            airplane = new Airplane(airplaneDTO.getName(),
                    airplaneDTO.getFactorySerialNumber(),
                    existingAirCompany,
                    airplaneDTO.getNumberOfFlights(),
                    airplaneDTO.getFlightDistance(),
                    airplaneDTO.getFuelCapacity(),
                    airplaneType);
        }

        return airplaneRepository.save(airplane);
    }

    @Transactional(rollbackFor = {ResourceNotFoundException.class, UpdateException.class})
    @Override
    public Airplane updateAirplane(Long id, AirplaneUpdateRequest updateRequest) throws ResourceNotFoundException, UpdateException {

        AirplaneType airplaneType = airplaneTypeService.findByName(updateRequest.getType());

        Airplane existingAirplane = airplaneRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No airplane available"));

        existingAirplane.setName(updateRequest.getName());
        existingAirplane.setNumberOfFlights(updateRequest.getNumberOfFlights());
        existingAirplane.setFlightDistance(updateRequest.getFlightDistance());
        existingAirplane.setFuelCapacity(updateRequest.getFuelCapacity());
        existingAirplane.setAirplaneType(airplaneType);

        return airplaneRepository.save(existingAirplane);
    }

    @Transactional(rollbackFor = {ResourceNotFoundException.class, DeleteException.class})
    @Override
    public void deleteAirplane(Long id) throws ResourceNotFoundException, DeleteException {

        Airplane existingAirplane = airplaneRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No airplane available"));
        if (hasActiveFlight(existingAirplane)) throw new DeleteException("Airplane has flight with status ACTIVE");

        airplaneRepository.delete(existingAirplane);
    }

    @Transactional(rollbackFor = {ResourceNotFoundException.class, UpdateException.class})
    @Override
    public void reassignAirplaneToAnotherCompany(Long airplaneId, Long airCompanyId) throws ResourceNotFoundException, UpdateException {

        Airplane existingAirplane = airplaneRepository.findById(airplaneId).orElseThrow(() -> new ResourceNotFoundException("No airplane available"));
        if (hasActiveFlight(existingAirplane)) throw new UpdateException("Airplane can not be reassigned if it has flight with status ACTIVE");

        AirCompany existingAirCompany = airCompanyService.findById(airCompanyId);

        existingAirplane.setAirCompany(existingAirCompany);

        airplaneRepository.save(existingAirplane);

    }

    private boolean hasActiveFlight(Airplane airplane) {

        List<Flight> flights = flightService.findAllByAirplane(airplane);
        if (flights.size() == 0) return false;
        return flights.stream().map(flight -> flight.getFlightStatus().getName()).anyMatch(flightStatus -> flightStatus == FlightStatusE.ACTIVE);
    }
}
