package com.osmaha.aircompaniesmanagementsystem.controller;

import com.osmaha.aircompaniesmanagementsystem.domain.AirCompany;
import com.osmaha.aircompaniesmanagementsystem.domain.CompanyType;
import com.osmaha.aircompaniesmanagementsystem.domain.enums.CompanyTypeE;
import com.osmaha.aircompaniesmanagementsystem.payload.request.aircompany.AirCompanyCreationRequest;
import com.osmaha.aircompaniesmanagementsystem.payload.request.aircompany.AirCompanyUpdateRequest;
import com.osmaha.aircompaniesmanagementsystem.service.AirCompanyService;
import com.osmaha.aircompaniesmanagementsystem.service.exception.RegistrationException;
import com.osmaha.aircompaniesmanagementsystem.service.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AirCompanyController.class)
class AirCompanyControllerTest {

    @MockBean
    private AirCompanyService airCompanyService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAirCompanies_Status200() throws Exception {

        CompanyType passenger = new CompanyType(CompanyTypeE.PASSENGER);
        CompanyType cargo = new CompanyType(CompanyTypeE.CARGO);

        List<AirCompany> expectedList = new ArrayList<>(Arrays.asList(
                new AirCompany(1L, "Jet", passenger, LocalDateTime.now()),
                new AirCompany(2L, "WizzAir", cargo, LocalDateTime.now())
        ));
        given(airCompanyService.findAll()).willReturn(expectedList);

        mockMvc.perform(get("/api/air-companies").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect((jsonPath("$.[0].id").value(1)))
                .andExpect((jsonPath("$.[0].name").value("Jet")))
                .andExpect((jsonPath("$.[0].companyType.name").value("PASSENGER")))
                .andExpect((jsonPath("$.[1].id").value(2)))
                .andExpect((jsonPath("$.[1].name").value("WizzAir")))
                .andExpect((jsonPath("$.[1].companyType.name").value("CARGO")));

    }

    @Test
    void getAirCompanyById_Status200() throws Exception {

        CompanyType passenger = new CompanyType(CompanyTypeE.PASSENGER);

        AirCompany expectedAirCompany = new AirCompany(1L, "Jet", passenger, LocalDateTime.now());

        given(airCompanyService.findById(any())).willReturn(expectedAirCompany);

        mockMvc.perform(get("/api/air-companies/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.id").value(1)))
                .andExpect((jsonPath("$.name").value("Jet")))
                .andExpect((jsonPath("$.companyType.name").value("PASSENGER")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getAirCompanyById_Status404() throws Exception {

        given(airCompanyService.findById(any())).willThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(get("/api/air-companies/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect((jsonPath("$").value("Not found")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void createAirCompany_AirCompanyCreationRequestGiven_checkAirCompanyServiceMethodArguments() throws Exception {

        String creationRequest = "{\"name\":\"Jet\",\"companyType\":\"CARGO\"}";

        String airCompanyName = "Jet";
        String airCompanyType = "CARGO";

        doAnswer(invocationOnMock -> {
            AirCompanyCreationRequest arg = invocationOnMock.getArgument(0);

            assertEquals(airCompanyName, arg.getName());
            assertEquals(airCompanyType, arg.getCompanyType());

            return null;
        }).when(airCompanyService).createAirCompany(any(AirCompanyCreationRequest.class));

        mockMvc.perform(post("/api/air-companies").contentType(MediaType.APPLICATION_JSON).content(creationRequest));
    }

    @Test
    void createAirCompany_AirCompanyCreationRequestGiven_Status200() throws Exception {

        String creationRequest = "{\"name\":\"String\",\"companyType\":\"CARGO\"}";

        doAnswer(invocationOnMock -> {
            AirCompany airCompany = new AirCompany(7L, "Jet", new CompanyType(CompanyTypeE.CARGO));
            return airCompany;
        }).when(airCompanyService).createAirCompany(any(AirCompanyCreationRequest.class));

        mockMvc.perform(post("/api/air-companies").contentType(MediaType.APPLICATION_JSON).content(creationRequest))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.id").value(7)))
                .andExpect((jsonPath("$.name").value("Jet")))
                .andExpect((jsonPath("$.companyType.name").value("CARGO")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void createAirCompany_AirCompanyServiceExceptionThrows_Status400() throws Exception {

        String creationRequest = "{\"name\":\"Jet\",\"companyType\":\"CARGO\"}";

        given(airCompanyService.createAirCompany(any())).willThrow(new RegistrationException("Registration error"));

        mockMvc.perform(post("/api/air-companies").contentType(MediaType.APPLICATION_JSON).content(creationRequest))
                .andExpect(status().isBadRequest())
                .andExpect((jsonPath("$").value("Registration error")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void createAirCompany_AirCompanyServiceExceptionThrows_Status404() throws Exception {

        String creationRequest = "{\"name\":\"String\",\"companyType\":\"CARGO\"}";

        given(airCompanyService.createAirCompany(any())).willThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(post("/api/air-companies").contentType(MediaType.APPLICATION_JSON).content(creationRequest))
                .andExpect(status().isNotFound())
                .andExpect((jsonPath("$").value("Not found")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void updateAirCompanyName_AirCompanyUpdateRequestGiven_checkAirCompanyServiceMethodArguments() throws Exception {

        String updateRequest = "{\"name\":\"WizzAir\",\"companyType\":\"PASSENGER\"}";

        String airCompanyName = "WizzAir";
        String airCompanyType = "PASSENGER";

        doAnswer(invocationOnMock -> {
            Long argId = invocationOnMock.getArgument(0);
            AirCompanyUpdateRequest argUpdate = invocationOnMock.getArgument(1);

            assertEquals(3, argId);
            assertEquals(airCompanyName, argUpdate.getName());
            assertEquals(airCompanyType, argUpdate.getCompanyType());

            return null;
        }).when(airCompanyService).updateAirCompany(any(Long.class), any(AirCompanyUpdateRequest.class));

        mockMvc.perform(put("/api/air-companies/3").contentType(MediaType.APPLICATION_JSON).content(updateRequest));
    }

    @Test
    void updateAirCompanyName_AirCompanyUpdateRequestGiven_Status200() throws Exception {

        String creationRequest = "{\"name\":\"String\",\"companyType\":\"CARGO\"}";

        doAnswer(invocationOnMock -> {
            AirCompany airCompany = new AirCompany(4L, "Jet", new CompanyType(CompanyTypeE.CARGO));
            return airCompany;
        }).when(airCompanyService).updateAirCompany(any(Long.class), any(AirCompanyUpdateRequest.class));

        mockMvc.perform(put("/api/air-companies/4").contentType(MediaType.APPLICATION_JSON).content(creationRequest))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.id").value(4)))
                .andExpect((jsonPath("$.name").value("Jet")))
                .andExpect((jsonPath("$.companyType.name").value("CARGO")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void updateAirCompanyName_AirCompanyUpdateRequestGiven_Status400() throws Exception {

        String creationRequest = "{\"name\":\"String\",\"companyType\":\"CARGO\"}";

        given(airCompanyService.updateAirCompany(any(), any())).willThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(put("/api/air-companies/4").contentType(MediaType.APPLICATION_JSON).content(creationRequest))
                .andExpect(status().isNotFound())
                .andExpect((jsonPath("$").value("Not found")))
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    void deleteAirCompany_RequestGiven_checkAirCompanyServiceMethodArguments() throws Exception {

        doAnswer(invocationOnMock -> {
            Long argId = invocationOnMock.getArgument(0);

            assertEquals(4, argId);
            return null;
        }).when(airCompanyService).deleteAirCompany(any(Long.class));

        mockMvc.perform(delete("/api/air-companies/4").contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void deleteAirCompany_RequestGiven_Status202() throws Exception {

        doAnswer(invocationOnMock -> {
            Long argId = invocationOnMock.getArgument(0);

            assertEquals(4, argId);
            return null;
        }).when(airCompanyService).deleteAirCompany(any(Long.class));

        mockMvc.perform(delete("/api/air-companies/4").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
    }

    @Test
    void deleteAirCompany_RequestGiven_Status404() throws Exception {

        doAnswer(invocationOnMock -> {
            throw new ResourceNotFoundException("Not found");
        }).when(airCompanyService).deleteAirCompany(any(Long.class));

        mockMvc.perform(delete("/api/air-companies/4").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect((jsonPath("$").value("Not found")))
                .andDo(MockMvcResultHandlers.print());
    }
}