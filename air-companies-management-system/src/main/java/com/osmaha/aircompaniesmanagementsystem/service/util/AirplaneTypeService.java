package com.osmaha.aircompaniesmanagementsystem.service.util;

import com.osmaha.aircompaniesmanagementsystem.domain.AirplaneType;
import com.osmaha.aircompaniesmanagementsystem.service.exception.ResourceNotFoundException;

public interface AirplaneTypeService {

    AirplaneType findByName(String name) throws ResourceNotFoundException;
}
