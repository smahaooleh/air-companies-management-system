package com.osmaha.aircompaniesmanagementsystem.service.util;

import com.osmaha.aircompaniesmanagementsystem.domain.CompanyType;
import com.osmaha.aircompaniesmanagementsystem.service.exception.ResourceNotFoundException;

public interface CompanyTypeService {

    CompanyType findByName(String name) throws ResourceNotFoundException;
}
