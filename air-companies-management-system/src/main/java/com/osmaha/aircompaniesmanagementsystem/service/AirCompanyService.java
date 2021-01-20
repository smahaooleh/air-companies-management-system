package com.osmaha.aircompaniesmanagementsystem.service;

import com.osmaha.aircompaniesmanagementsystem.domain.AirCompany;
import com.osmaha.aircompaniesmanagementsystem.payload.request.aircompany.AirCompanyCreationRequest;
import com.osmaha.aircompaniesmanagementsystem.payload.request.aircompany.AirCompanyUpdateRequest;
import com.osmaha.aircompaniesmanagementsystem.service.exception.RegistrationException;
import com.osmaha.aircompaniesmanagementsystem.service.exception.ResourceNotFoundException;
import com.osmaha.aircompaniesmanagementsystem.service.exception.UpdateException;

import java.util.List;

public interface AirCompanyService {

    List<AirCompany> findAll();

    AirCompany findById(Long id) throws ResourceNotFoundException;

    AirCompany findByName(String name) throws ResourceNotFoundException;

    AirCompany createAirCompany(AirCompanyCreationRequest airCompanyDTO) throws ResourceNotFoundException, RegistrationException;

    AirCompany updateAirCompany(Long id, AirCompanyUpdateRequest updateRequest) throws ResourceNotFoundException;

    void deleteAirCompany(Long id) throws ResourceNotFoundException;
}
