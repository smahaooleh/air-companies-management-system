package com.osmaha.aircompaniesmanagementsystem.service.util.impl;

import com.osmaha.aircompaniesmanagementsystem.domain.AirplaneType;
import com.osmaha.aircompaniesmanagementsystem.domain.enums.AirplaneTypeE;
import com.osmaha.aircompaniesmanagementsystem.domain.enums.CompanyTypeE;
import com.osmaha.aircompaniesmanagementsystem.repository.util.AirplaneTypeRepository;
import com.osmaha.aircompaniesmanagementsystem.service.exception.ResourceNotFoundException;
import com.osmaha.aircompaniesmanagementsystem.service.util.AirplaneTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AirplaneTypeServiceImpl implements AirplaneTypeService {

    @Autowired
    private AirplaneTypeRepository airplaneTypeRepository;

    @Transactional(readOnly = true)
    @Override
    public AirplaneType findByName(String name) throws ResourceNotFoundException {
        AirplaneTypeE airplaneTypeE;
        try {
            airplaneTypeE = AirplaneTypeE.valueOf(name);
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("No airplane type available");
        }
        return airplaneTypeRepository.findByName(airplaneTypeE).orElseThrow(() -> new ResourceNotFoundException("No airplane type available"));
    }
}
