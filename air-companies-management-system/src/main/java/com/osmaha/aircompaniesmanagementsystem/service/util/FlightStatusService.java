package com.osmaha.aircompaniesmanagementsystem.service.util;

import com.osmaha.aircompaniesmanagementsystem.domain.FlightStatus;
import com.osmaha.aircompaniesmanagementsystem.service.exception.ResourceNotFoundException;

public interface FlightStatusService {

    FlightStatus findByName(String name) throws ResourceNotFoundException;
}
