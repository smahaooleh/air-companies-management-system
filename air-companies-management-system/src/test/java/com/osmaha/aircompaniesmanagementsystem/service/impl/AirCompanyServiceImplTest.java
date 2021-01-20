package com.osmaha.aircompaniesmanagementsystem.service.impl;

import com.osmaha.aircompaniesmanagementsystem.domain.AirCompany;
import com.osmaha.aircompaniesmanagementsystem.domain.CompanyType;
import com.osmaha.aircompaniesmanagementsystem.domain.enums.CompanyTypeE;
import com.osmaha.aircompaniesmanagementsystem.payload.request.aircompany.AirCompanyCreationRequest;
import com.osmaha.aircompaniesmanagementsystem.payload.request.aircompany.AirCompanyUpdateRequest;
import com.osmaha.aircompaniesmanagementsystem.repository.AirCompanyRepository;
import com.osmaha.aircompaniesmanagementsystem.service.AirCompanyService;
import com.osmaha.aircompaniesmanagementsystem.service.exception.RegistrationException;
import com.osmaha.aircompaniesmanagementsystem.service.exception.ResourceNotFoundException;
import com.osmaha.aircompaniesmanagementsystem.service.util.CompanyTypeService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class AirCompanyServiceImplTest {

    @Autowired
    private AirCompanyRepository airCompanyRepositoryReal;

    @Autowired
    private CompanyTypeService companyTypeServiceReal;

    @MockBean
    private AirCompanyRepository airCompanyRepository;

    @MockBean
    private CompanyTypeService companyTypeService;

    @Autowired
    private AirCompanyService airCompanyService;

    @Test
    void findAll_ShouldCallAirCompanyRepositoryAndReturnList() {
        CompanyType passenger = new CompanyType(CompanyTypeE.PASSENGER);
        CompanyType cargo = new CompanyType(CompanyTypeE.CARGO);

        List<AirCompany> expectedList = new ArrayList<>(Arrays.asList(
                new AirCompany(1L, "Jet", passenger, LocalDateTime.now()),
                new AirCompany(2L, "WizzAir", cargo, LocalDateTime.now())
        ));

        given(airCompanyRepository.findAll()).willReturn(expectedList);

        List<AirCompany> receivedList = airCompanyService.findAll();

        for (int i = 0; i < expectedList.size(); i++) {
            assertEquals(expectedList.get(i).getId(),receivedList.get(i).getId());
        }

        verify(airCompanyRepository, times(1)).findAll();
    }

    @Test
    void findById_ShouldCallAirCompanyRepositoryAndReturnAirCompany() throws Exception {
        CompanyType passenger = new CompanyType(CompanyTypeE.PASSENGER);

        AirCompany expectedAirCompany = new AirCompany(1L, "Jet", passenger, LocalDateTime.now());

        given(airCompanyRepository.findById(any(Long.class))).willReturn(Optional.of(expectedAirCompany));

        AirCompany receivedAirCompany = airCompanyService.findById(1L);


        assertEquals(expectedAirCompany.getId(),receivedAirCompany.getId());


        verify(airCompanyRepository, times(1)).findById(any(Long.class));
    }

    @Test
    void findById_ShouldThrowResourceNotFoundException() throws Exception {

        given(airCompanyRepository.findById(any(Long.class))).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {airCompanyService.findById(1L);});

        verify(airCompanyRepository, times(1)).findById(any(Long.class));
    }

    @Test
    void findByName_ShouldCallAirCompanyRepositoryAndReturnAirCompany() throws Exception {
        CompanyType passenger = new CompanyType(CompanyTypeE.PASSENGER);

        AirCompany expectedAirCompany = new AirCompany(1L, "Jet", passenger, LocalDateTime.now());

        given(airCompanyRepository.findByName(any(String.class))).willReturn(Optional.of(expectedAirCompany));

        AirCompany receivedAirCompany = airCompanyService.findByName("Jet");

        assertEquals(expectedAirCompany.getId(),receivedAirCompany.getId());

        verify(airCompanyRepository, times(1)).findByName(any(String.class));
    }

    @Test
    void findByName_ShouldThrowResourceNotFoundException() throws Exception {
        given(airCompanyRepository.findByName(any(String.class))).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {airCompanyService.findByName("Jet");});

        verify(airCompanyRepository, times(1)).findByName(any(String.class));
    }

    @Test
    void createAirCompany_ShouldCallAirCompanyRepositoryAndReturnAirCompany() throws Exception {

        AirCompanyCreationRequest airCompanyCreationRequest = new AirCompanyCreationRequest("Jet", "PASSENGER");
        CompanyType passenger = new CompanyType(CompanyTypeE.PASSENGER);

        AirCompany expectedAirCompany = new AirCompany(1L, "Jet", passenger, LocalDateTime.now());

        given(airCompanyRepository.save(any(AirCompany.class))).willReturn(expectedAirCompany);
        given(companyTypeService.findByName(any(String.class))).willReturn(passenger);

        AirCompany receivedAirCompany = airCompanyService.createAirCompany(airCompanyCreationRequest);

        assertEquals(expectedAirCompany.getId(),receivedAirCompany.getId());

        verify(airCompanyRepository, times(1)).save(any(AirCompany.class));
    }

    @Test
    void createAirCompany_CreatingAirCompanyObject() throws Exception {

        AirCompanyCreationRequest airCompanyCreationRequest = new AirCompanyCreationRequest("Jet", "PASSENGER");
        CompanyType passenger = new CompanyType(CompanyTypeE.PASSENGER);

        AirCompany expectedAirCompany = new AirCompany(1L, "Jet", passenger, LocalDateTime.now());

        given(airCompanyRepository.save(any(AirCompany.class))).willReturn(expectedAirCompany);
        given(companyTypeService.findByName(any(String.class))).willReturn(passenger);

        doAnswer(invocationOnMock -> {
            AirCompany arg = invocationOnMock.getArgument(0);

            assertEquals("Jet", arg.getName());
            assertEquals(CompanyTypeE.PASSENGER, arg.getCompanyType().getName());

            return null;
        }).when(airCompanyRepository).save(any(AirCompany.class));

        AirCompany airCompany = airCompanyService.createAirCompany(airCompanyCreationRequest);
    }

    @Test
    void createAirCompany_IncorrectCompanyType_ShouldThrowResourceNotFoundException() throws Exception {

        AirCompanyCreationRequest airCompanyCreationRequest = new AirCompanyCreationRequest("Jet", "PASSENGER");
        CompanyType passenger = new CompanyType(CompanyTypeE.PASSENGER);

        given(airCompanyRepository.findByName(any(String.class))).willReturn(Optional.empty());

        given(companyTypeService.findByName(any(String.class))).willThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> {airCompanyService.createAirCompany(airCompanyCreationRequest);});
    }

    @Test
    void createAirCompany_AirCompanyExists_ShouldThrowRegistrationFoundException() throws Exception {
        AirCompanyCreationRequest airCompanyCreationRequest = new AirCompanyCreationRequest("Jet", "PASSENGER");
        CompanyType passenger = new CompanyType(CompanyTypeE.PASSENGER);

        AirCompany existsAirCompany = new AirCompany(1L, "Jet", passenger, LocalDateTime.now());

        given(airCompanyRepository.findByName(any(String.class))).willReturn(Optional.of(existsAirCompany));

        assertThrows(RegistrationException.class, () -> {airCompanyService.createAirCompany(airCompanyCreationRequest);});
    }

    @Test
    void updateAirCompany_ShouldCallAirCompanyRepositoryAndReturnUpdatedAirCompany() throws Exception {

        AirCompanyUpdateRequest airCompanyUpdateRequest = new AirCompanyUpdateRequest("WizzAir", "PASSENGER");
        CompanyType passenger = new CompanyType(CompanyTypeE.PASSENGER);

        AirCompany expectedAirCompany = new AirCompany(1L, "Jet", passenger, LocalDateTime.now());

        String updatedName = "WizzAir";

        given(airCompanyRepository.findById(any(Long.class))).willReturn(Optional.of(expectedAirCompany));

        doAnswer(invocationOnMock -> {
            AirCompany arg = invocationOnMock.getArgument(0);

            assertEquals(updatedName, arg.getName());

            return null;
        }).when(airCompanyRepository).save(any(AirCompany.class));

        AirCompany airCompany = airCompanyService.updateAirCompany(1L, airCompanyUpdateRequest);
    }

    @Test
    void updateAirCompany_IncorrectCompanyId_ShouldThrowResourceNotFoundException() throws Exception {

        AirCompanyUpdateRequest airCompanyUpdateRequest = new AirCompanyUpdateRequest("WizzAir", "PASSENGER");
        CompanyType passenger = new CompanyType(CompanyTypeE.PASSENGER);

        given(airCompanyRepository.findByName(any(String.class))).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {airCompanyService.updateAirCompany(1L, airCompanyUpdateRequest);});
    }


    @Test
    void deleteAirCompany_ShouldCallAirCompanyRepository() throws Exception {
        CompanyType passenger = new CompanyType(CompanyTypeE.PASSENGER);

        AirCompany expectedAirCompany = new AirCompany(1L, "Jet", passenger, LocalDateTime.now());

        given(airCompanyRepository.findById(any(Long.class))).willReturn(Optional.of(expectedAirCompany));

        airCompanyService.deleteAirCompany(1L);

        verify(airCompanyRepository, times(1)).delete(any(AirCompany.class));
    }

    @Test
    void deleteAirCompany_IncorrectCompanyId_ShouldThrowResourceNotFoundException() {

        given(airCompanyRepository.findById(any(Long.class))).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {airCompanyService.deleteAirCompany(1L);});
    }


}