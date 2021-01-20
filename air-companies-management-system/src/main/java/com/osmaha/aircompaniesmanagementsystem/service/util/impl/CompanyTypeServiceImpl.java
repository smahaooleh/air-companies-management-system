package com.osmaha.aircompaniesmanagementsystem.service.util.impl;

import com.osmaha.aircompaniesmanagementsystem.domain.CompanyType;
import com.osmaha.aircompaniesmanagementsystem.domain.enums.CompanyTypeE;
import com.osmaha.aircompaniesmanagementsystem.domain.enums.FlightStatusE;
import com.osmaha.aircompaniesmanagementsystem.repository.util.CompanyTypeRepository;
import com.osmaha.aircompaniesmanagementsystem.service.exception.ResourceNotFoundException;
import com.osmaha.aircompaniesmanagementsystem.service.util.CompanyTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.module.ResolutionException;

@Service
public class CompanyTypeServiceImpl implements CompanyTypeService {

    @Autowired
    private CompanyTypeRepository companyTypeRepository;

    @Transactional(readOnly = true)
    @Override
    public CompanyType findByName(String name) throws ResourceNotFoundException {
        CompanyTypeE companyTypeE;
        try {
            companyTypeE = CompanyTypeE.valueOf(name);
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("No company type available");
        }
        return companyTypeRepository.findByName(companyTypeE).orElseThrow(() -> new ResourceNotFoundException("No company type available"));
    }
}
