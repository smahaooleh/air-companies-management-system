package com.osmaha.aircompaniesmanagementsystem.service.util.impl;

import com.osmaha.aircompaniesmanagementsystem.domain.Country;
import com.osmaha.aircompaniesmanagementsystem.repository.util.CountryRepository;
import com.osmaha.aircompaniesmanagementsystem.service.exception.ResourceNotFoundException;
import com.osmaha.aircompaniesmanagementsystem.service.util.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.module.ResolutionException;

@Service
public class CountryServiceImpl implements CountryService {

    @Autowired
    private CountryRepository countryRepository;

    @Transactional(readOnly = true)
    @Override
    public Country findByName(String name) throws ResourceNotFoundException {
        return countryRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("No country available"));
    }
}
