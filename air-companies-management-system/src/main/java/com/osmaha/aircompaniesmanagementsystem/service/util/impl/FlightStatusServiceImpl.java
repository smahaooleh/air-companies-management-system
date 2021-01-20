package com.osmaha.aircompaniesmanagementsystem.service.util.impl;

import com.osmaha.aircompaniesmanagementsystem.domain.FlightStatus;
import com.osmaha.aircompaniesmanagementsystem.domain.enums.FlightStatusE;
import com.osmaha.aircompaniesmanagementsystem.repository.util.FlightStatusRepository;
import com.osmaha.aircompaniesmanagementsystem.service.exception.ResourceNotFoundException;
import com.osmaha.aircompaniesmanagementsystem.service.util.FlightStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FlightStatusServiceImpl implements FlightStatusService {

    @Autowired
    private FlightStatusRepository flightStatusRepository;

    @Transactional(readOnly = true)
    @Override
    public FlightStatus findByName(String name) throws ResourceNotFoundException {
        FlightStatusE statusE;
        try {
            statusE = FlightStatusE.valueOf(name);
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("No flight status available");
        }
        return flightStatusRepository.findByName(statusE).orElseThrow(() -> new ResourceNotFoundException("No flight status available"));
    }
}
