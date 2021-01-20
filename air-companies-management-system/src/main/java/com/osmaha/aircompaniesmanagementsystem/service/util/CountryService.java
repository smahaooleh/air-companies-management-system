package com.osmaha.aircompaniesmanagementsystem.service.util;

import com.osmaha.aircompaniesmanagementsystem.domain.Country;
import com.osmaha.aircompaniesmanagementsystem.service.exception.ResourceNotFoundException;

public interface CountryService {

    Country findByName(String name) throws ResourceNotFoundException;
}
