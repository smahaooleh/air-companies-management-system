package com.osmaha.aircompaniesmanagementsystem.service;

import com.osmaha.aircompaniesmanagementsystem.domain.Airplane;
import com.osmaha.aircompaniesmanagementsystem.payload.request.airplane.AirplaneCreationRequest;
import com.osmaha.aircompaniesmanagementsystem.payload.request.airplane.AirplaneUpdateRequest;
import com.osmaha.aircompaniesmanagementsystem.service.exception.DeleteException;
import com.osmaha.aircompaniesmanagementsystem.service.exception.RegistrationException;
import com.osmaha.aircompaniesmanagementsystem.service.exception.ResourceNotFoundException;
import com.osmaha.aircompaniesmanagementsystem.service.exception.UpdateException;

import java.util.List;

public interface AirplaneService {

    List<Airplane> findAll();

    Airplane findById(Long id) throws ResourceNotFoundException;

    Airplane createAirplane(AirplaneCreationRequest airplaneDTO) throws ResourceNotFoundException, RegistrationException;

    Airplane updateAirplane(Long id, AirplaneUpdateRequest updateRequest) throws ResourceNotFoundException, UpdateException;

    void deleteAirplane(Long id) throws ResourceNotFoundException, DeleteException;

    void reassignAirplaneToAnotherCompany(Long airplaneId, Long airCompanyId) throws ResourceNotFoundException, UpdateException;
}
