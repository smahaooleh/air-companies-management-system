package com.osmaha.aircompaniesmanagementsystem.service.impl;

import com.osmaha.aircompaniesmanagementsystem.domain.AirCompany;
import com.osmaha.aircompaniesmanagementsystem.domain.CompanyType;
import com.osmaha.aircompaniesmanagementsystem.payload.request.aircompany.AirCompanyCreationRequest;
import com.osmaha.aircompaniesmanagementsystem.payload.request.aircompany.AirCompanyUpdateRequest;
import com.osmaha.aircompaniesmanagementsystem.repository.AirCompanyRepository;
import com.osmaha.aircompaniesmanagementsystem.service.AirCompanyService;
import com.osmaha.aircompaniesmanagementsystem.service.exception.RegistrationException;
import com.osmaha.aircompaniesmanagementsystem.service.exception.ResourceNotFoundException;
import com.osmaha.aircompaniesmanagementsystem.service.exception.UpdateException;
import com.osmaha.aircompaniesmanagementsystem.service.util.CompanyTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AirCompanyServiceImpl implements AirCompanyService {

    @Autowired
    private AirCompanyRepository airCompanyRepository;

    @Autowired
    private CompanyTypeService companyTypeService;

    @Transactional(readOnly = true)
    @Override
    public List<AirCompany> findAll() {
        return airCompanyRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public AirCompany findById(Long id) throws ResourceNotFoundException {
        return airCompanyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No air company available"));
    }

    @Transactional(readOnly = true)
    @Override
    public AirCompany findByName(String name) throws ResourceNotFoundException {
        return airCompanyRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("No air company available"));
    }

    @Transactional(rollbackFor = {ResourceNotFoundException.class, RegistrationException.class})
    @Override
    public AirCompany createAirCompany(AirCompanyCreationRequest airCompanyDTO) throws ResourceNotFoundException, RegistrationException {

        Optional<AirCompany> airCompanyOptional = airCompanyRepository.findByName(airCompanyDTO.getName());
        if (airCompanyOptional.isPresent()) throw new RegistrationException("Such air company already exists");

        CompanyType companyType = companyTypeService.findByName(airCompanyDTO.getCompanyType());
        LocalDateTime foundedAt = LocalDateTime.now();

        AirCompany airCompany = new AirCompany(airCompanyDTO.getName(), companyType, foundedAt);

        return airCompanyRepository.save(airCompany);
    }

    @Transactional(rollbackFor = ResourceNotFoundException.class)
    @Override
    public AirCompany updateAirCompany(Long id, AirCompanyUpdateRequest updateRequest) throws ResourceNotFoundException {

        CompanyType companyType = companyTypeService.findByName(updateRequest.getCompanyType());

        AirCompany existingAirCompany = airCompanyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No air company available"));

        existingAirCompany.setName(updateRequest.getName());
        existingAirCompany.setCompanyType(companyType);

        return airCompanyRepository.save(existingAirCompany);
    }

    @Transactional(rollbackFor = ResourceNotFoundException.class)
    @Override
    public void deleteAirCompany(Long id) throws ResourceNotFoundException {

        AirCompany existingAirCompany = airCompanyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No air company available"));

        airCompanyRepository.delete(existingAirCompany);
    }
}
