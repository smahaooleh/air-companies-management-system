package com.osmaha.aircompaniesmanagementsystem.controller;

import com.osmaha.aircompaniesmanagementsystem.domain.*;
import com.osmaha.aircompaniesmanagementsystem.domain.enums.AirplaneTypeE;
import com.osmaha.aircompaniesmanagementsystem.domain.enums.CompanyTypeE;
import com.osmaha.aircompaniesmanagementsystem.domain.enums.FlightStatusE;
import com.osmaha.aircompaniesmanagementsystem.payload.request.flight.FlightCreationRequest;
import com.osmaha.aircompaniesmanagementsystem.payload.request.flight.FlightStatusUpdateRequest;
import com.osmaha.aircompaniesmanagementsystem.payload.request.flight.FlightUpdateRequest;
import com.osmaha.aircompaniesmanagementsystem.service.FlightService;
import com.osmaha.aircompaniesmanagementsystem.service.exception.*;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.Duration;
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
@WebMvcTest(FlightController.class)
class FlightControllerTest {

    @MockBean
    private FlightService flightService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getFlights_Status200() throws Exception {

        FlightStatus flightStatusPending = new FlightStatus(FlightStatusE.PENDING);
        FlightStatus flightStatusDelayed = new FlightStatus(FlightStatusE.DELAYED);

        CompanyType passenger = new CompanyType(CompanyTypeE.PASSENGER);
        CompanyType cargo = new CompanyType(CompanyTypeE.CARGO);

        AirCompany airCompanyPassenger = new AirCompany(1L, "Jet", passenger, LocalDateTime.now());
        AirCompany airCompanyCargo = new AirCompany(2L, "WizzAir", cargo, LocalDateTime.now());

        AirplaneType smallAirplaneType = new AirplaneType(AirplaneTypeE.SMALL);
        AirplaneType mediumAirplaneType = new AirplaneType(AirplaneTypeE.MEDIUM);

        Airplane airplane737 = new Airplane("Boeing 737", "KB1234", airCompanyPassenger, 0, 0, 300, smallAirplaneType);
        Airplane airplane707 = new Airplane("Boeing 707", "TR1234", airCompanyCargo, 1, 1, 500, mediumAirplaneType);

        Country countryUkraine = new Country("Ukraine");
        Country countryPoland = new Country("Poland");

        Flight flightUkrainePoland = new Flight(1L, flightStatusPending, airCompanyPassenger, airplane737, countryUkraine, countryPoland,
                650, Duration.ofDays(3), LocalDateTime.now(), null, null, null);

        Flight flightPolandUkraine = new Flight(1L, flightStatusDelayed, airCompanyCargo, airplane707, countryPoland, countryUkraine,
                650, Duration.ofDays(2), LocalDateTime.now().minusHours(1), LocalDateTime.now(), null, null);

        List<Flight> expectedList = new ArrayList<>(Arrays.asList(flightUkrainePoland, flightPolandUkraine));

        given(flightService.findAll()).willReturn(expectedList);

        mockMvc.perform(get("/api/flights").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect((jsonPath("$.[0].id").value(1)))
                .andExpect((jsonPath("$.[0].flightStatus.name").value("PENDING")))
                .andExpect((jsonPath("$.[0].airCompany.id").value(1)))
                .andExpect((jsonPath("$.[0].airCompany.name").value("Jet")))
                .andExpect((jsonPath("$.[0].airCompany.companyType.name").value("PASSENGER")))
                .andExpect((jsonPath("$.[0].airplane.name").value("Boeing 737")))
                .andExpect((jsonPath("$.[0].airplane.factorySerialNumber").value("KB1234")))
                .andExpect((jsonPath("$.[0].airplane.airplaneType.name").value("SMALL")))
                .andExpect((jsonPath("$.[0].departureCountry.name").value("Ukraine")))
                .andExpect((jsonPath("$.[0].destinationCountry.name").value("Poland")))
                .andExpect((jsonPath("$.[0].estimatedFlightTime").value("PT72H")))
                .andExpect((jsonPath("$.[1].id").value(1)))
                .andExpect((jsonPath("$.[1].flightStatus.name").value("DELAYED")))
                .andExpect((jsonPath("$.[1].airCompany.id").value(2)))
                .andExpect((jsonPath("$.[1].airCompany.name").value("WizzAir")))
                .andExpect((jsonPath("$.[1].airCompany.companyType.name").value("CARGO")))
                .andExpect((jsonPath("$.[1].airplane.name").value("Boeing 707")))
                .andExpect((jsonPath("$.[1].airplane.factorySerialNumber").value("TR1234")))
                .andExpect((jsonPath("$.[1].airplane.airplaneType.name").value("MEDIUM")))
                .andExpect((jsonPath("$.[1].departureCountry.name").value("Poland")))
                .andExpect((jsonPath("$.[1].destinationCountry.name").value("Ukraine")))
                .andExpect((jsonPath("$.[1].estimatedFlightTime").value("PT48H")));
    }

    @Test
    void getFlightById_FlightIdRequestGiven_checkFlightServiceMethodArguments() throws Exception {

        Long flightId = 11L;

        doAnswer(invocationOnMock -> {
            Long argId = invocationOnMock.getArgument(0);

            assertEquals(flightId, argId);

            return null;
        }).when(flightService).findById(any(Long.class));

        mockMvc.perform(get("/api/flights/11").contentType(MediaType.APPLICATION_JSON));

    }


    @Test
    void getFlightById() throws Exception {
        FlightStatus flightStatusPending = new FlightStatus(FlightStatusE.PENDING);

        CompanyType passenger = new CompanyType(CompanyTypeE.PASSENGER);

        AirCompany airCompanyPassenger = new AirCompany(1L, "Jet", passenger, LocalDateTime.now());

        AirplaneType smallAirplaneType = new AirplaneType(AirplaneTypeE.SMALL);

        Airplane airplane737 = new Airplane("Boeing 737", "KB1234", airCompanyPassenger, 0, 0, 300, smallAirplaneType);

        Country countryUkraine = new Country("Ukraine");
        Country countryPoland = new Country("Poland");

        Flight flightUkrainePoland = new Flight(1L, flightStatusPending, airCompanyPassenger, airplane737, countryUkraine, countryPoland,
                650, Duration.ofDays(3), LocalDateTime.now(), null, null, null);

        given(flightService.findById(any(Long.class))).willReturn(flightUkrainePoland);

        mockMvc.perform(get("/api/flights/4").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect((jsonPath("$.id").value(1)))
                .andExpect((jsonPath("$.flightStatus.name").value("PENDING")))
                .andExpect((jsonPath("$.airCompany.id").value(1)))
                .andExpect((jsonPath("$.airCompany.name").value("Jet")))
                .andExpect((jsonPath("$.airCompany.companyType.name").value("PASSENGER")))
                .andExpect((jsonPath("$.airplane.name").value("Boeing 737")))
                .andExpect((jsonPath("$.airplane.factorySerialNumber").value("KB1234")))
                .andExpect((jsonPath("$.airplane.airplaneType.name").value("SMALL")))
                .andExpect((jsonPath("$.departureCountry.name").value("Ukraine")))
                .andExpect((jsonPath("$.destinationCountry.name").value("Poland")))
                .andExpect((jsonPath("$.estimatedFlightTime").value("PT72H")));
    }

    @Test
    void getFlightById_Status404() throws Exception {
        given(flightService.findById(any())).willThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(get("/api/flights/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect((jsonPath("$").value("Not found")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void createFlight_FlightCreationRequestGiven_checkFlightServiceMethodArguments() throws Exception {

        String creationRequest = "{\"airCompanyId\": 1,\"airplaneId\": 2"
                + ",\"departureCountry\": \"Ukraine\",\"destinationCountry\": \"Poland\""
                + ",\"distance\": 654,\"estimatedFlightTime\": \"01:12\"}";

        Long airCompanyId = 1L;
        Long airplaneId = 2L;
        String departureCountry = "Ukraine";
        String destinationCountry = "Poland";
        int distance = 654;
        String estimatedFlightTime = "01:12";

        doAnswer(invocationOnMock -> {
            FlightCreationRequest arg = invocationOnMock.getArgument(0);

            assertEquals(airCompanyId, arg.getAirCompanyId());
            assertEquals(airplaneId, arg.getAirplaneId());
            assertEquals(departureCountry, arg.getDepartureCountry());
            assertEquals(destinationCountry, arg.getDestinationCountry());
            assertEquals(distance, arg.getDistance());
            assertEquals(estimatedFlightTime, arg.getEstimatedFlightTime());

            return null;
        }).when(flightService).createFlight(any(FlightCreationRequest.class));

        mockMvc.perform(post("/api/flights").contentType(MediaType.APPLICATION_JSON).content(creationRequest));
    }

    @Test
    void createFlight_FlightCreationRequestGiven_Status200() throws Exception {
        String creationRequest = "{\"airCompanyId\": 1,\"airplaneId\": 2"
                + ",\"departureCountry\": \"Ukraine\",\"destinationCountry\": \"Poland\""
                + ",\"distance\": 654,\"estimatedFlightTime\": \"01:12\"}";

        FlightStatus flightStatusPending = new FlightStatus(FlightStatusE.PENDING);

        CompanyType passenger = new CompanyType(CompanyTypeE.PASSENGER);

        AirCompany airCompanyPassenger = new AirCompany(1L, "Jet", passenger, LocalDateTime.now());

        AirplaneType smallAirplaneType = new AirplaneType(AirplaneTypeE.SMALL);

        Airplane airplane737 = new Airplane("Boeing 737", "KB1234", airCompanyPassenger, 0, 0, 300, smallAirplaneType);

        Country countryUkraine = new Country("Ukraine");
        Country countryPoland = new Country("Poland");

        Flight flightUkrainePoland = new Flight(1L, flightStatusPending, airCompanyPassenger, airplane737, countryUkraine, countryPoland,
                650, Duration.ofDays(3), LocalDateTime.now(), null, null, null);

        doAnswer(invocationOnMock -> flightUkrainePoland).when(flightService).createFlight(any(FlightCreationRequest.class));

        mockMvc.perform(post("/api/flights").contentType(MediaType.APPLICATION_JSON).content(creationRequest))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect((jsonPath("$.id").value(1)))
                .andExpect((jsonPath("$.flightStatus.name").value("PENDING")))
                .andExpect((jsonPath("$.airCompany.id").value(1)))
                .andExpect((jsonPath("$.airCompany.name").value("Jet")))
                .andExpect((jsonPath("$.airCompany.companyType.name").value("PASSENGER")))
                .andExpect((jsonPath("$.airplane.name").value("Boeing 737")))
                .andExpect((jsonPath("$.airplane.factorySerialNumber").value("KB1234")))
                .andExpect((jsonPath("$.airplane.airplaneType.name").value("SMALL")))
                .andExpect((jsonPath("$.departureCountry.name").value("Ukraine")))
                .andExpect((jsonPath("$.destinationCountry.name").value("Poland")))
                .andExpect((jsonPath("$.estimatedFlightTime").value("PT72H")));
    }

    @Test
    void createFlight_FlightCreationRequestGiven_Status400() throws Exception {
        String creationRequest = "{\"airCompanyId\": 1,\"airplaneId\": 2"
                + ",\"departureCountry\": \"Ukraine\",\"destinationCountry\": \"Poland\""
                + ",\"distance\": 654,\"estimatedFlightTime\": \"01:12\"}";


        given(flightService.createFlight(any())).willThrow(new RegistrationException("Registration error"));

        mockMvc.perform(post("/api/flights").contentType(MediaType.APPLICATION_JSON).content(creationRequest))
                .andExpect(status().isBadRequest())
                .andExpect((jsonPath("$").value("Registration error")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void createFlight_FlightCreationRequestGiven_Status404() throws Exception {
        String creationRequest = "{\"airCompanyId\": 1,\"airplaneId\": 2"
                + ",\"departureCountry\": \"Ukraine\",\"destinationCountry\": \"Poland\""
                + ",\"distance\": 654,\"estimatedFlightTime\": \"01:12\"}";


        given(flightService.createFlight(any())).willThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(post("/api/flights").contentType(MediaType.APPLICATION_JSON).content(creationRequest))
                .andExpect(status().isNotFound())
                .andExpect((jsonPath("$").value("Not found")))
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    void updateFlight_FlightUpdateRequestGiven_checkFlightServiceMethodArguments() throws Exception {

        String updateRequest = "{\"airCompanyId\": 1,\"airplaneId\": 2"
                + ",\"departureCountry\": \"Ukraine\",\"destinationCountry\": \"Poland\""
                + ",\"distance\": 654,\"estimatedFlightTime\": \"01:12\"}";

        Long airCompanyId = 1L;
        Long airplaneId = 2L;
        String departureCountry = "Ukraine";
        String destinationCountry = "Poland";
        int distance = 654;
        String estimatedFlightTime = "01:12";

        doAnswer(invocationOnMock -> {
            Long argId = invocationOnMock.getArgument(0);
            FlightUpdateRequest arg = invocationOnMock.getArgument(1);

            assertEquals(12, argId);
            assertEquals(airCompanyId, arg.getAirCompanyId());
            assertEquals(airplaneId, arg.getAirplaneId());
            assertEquals(departureCountry, arg.getDepartureCountry());
            assertEquals(destinationCountry, arg.getDestinationCountry());
            assertEquals(distance, arg.getDistance());
            assertEquals(estimatedFlightTime, arg.getEstimatedFlightTime());

            return null;
        }).when(flightService).updateFlight(any(Long.class), any(FlightUpdateRequest.class));

        mockMvc.perform(put("/api/flights/12").contentType(MediaType.APPLICATION_JSON).content(updateRequest));
    }

    @Test
    void updateFlight_FlightUpdateRequestGiven_Status200() throws Exception {

        String updateRequest = "{\"airCompanyId\": 1,\"airplaneId\": 2"
                + ",\"departureCountry\": \"Ukraine\",\"destinationCountry\": \"Poland\""
                + ",\"distance\": 654,\"estimatedFlightTime\": \"01:12\"}";

        FlightStatus flightStatusPending = new FlightStatus(FlightStatusE.PENDING);

        CompanyType passenger = new CompanyType(CompanyTypeE.PASSENGER);

        AirCompany airCompanyPassenger = new AirCompany(1L, "Jet", passenger, LocalDateTime.now());

        AirplaneType smallAirplaneType = new AirplaneType(AirplaneTypeE.SMALL);

        Airplane airplane737 = new Airplane("Boeing 737", "KB1234", airCompanyPassenger, 0, 0, 300, smallAirplaneType);

        Country countryUkraine = new Country("Ukraine");
        Country countryPoland = new Country("Poland");

        Flight flightUkrainePoland = new Flight(1L, flightStatusPending, airCompanyPassenger, airplane737, countryUkraine, countryPoland,
                650, Duration.ofDays(3), LocalDateTime.now(), null, null, null);

        doAnswer(invocationOnMock -> flightUkrainePoland).when(flightService).updateFlight(any(Long.class), any(FlightUpdateRequest.class));

        mockMvc.perform(put("/api/flights/1").contentType(MediaType.APPLICATION_JSON).content(updateRequest))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect((jsonPath("$.id").value(1)))
                .andExpect((jsonPath("$.flightStatus.name").value("PENDING")))
                .andExpect((jsonPath("$.airCompany.id").value(1)))
                .andExpect((jsonPath("$.airCompany.name").value("Jet")))
                .andExpect((jsonPath("$.airCompany.companyType.name").value("PASSENGER")))
                .andExpect((jsonPath("$.airplane.name").value("Boeing 737")))
                .andExpect((jsonPath("$.airplane.factorySerialNumber").value("KB1234")))
                .andExpect((jsonPath("$.airplane.airplaneType.name").value("SMALL")))
                .andExpect((jsonPath("$.departureCountry.name").value("Ukraine")))
                .andExpect((jsonPath("$.destinationCountry.name").value("Poland")))
                .andExpect((jsonPath("$.estimatedFlightTime").value("PT72H")));
    }

    @Test
    void updateFlight_FlightUpdateRequestGiven_Status400() throws Exception {

        String updateRequest = "{\"airCompanyId\": 1,\"airplaneId\": 2"
                + ",\"departureCountry\": \"Ukraine\",\"destinationCountry\": \"Poland\""
                + ",\"distance\": 654,\"estimatedFlightTime\": \"01:12\"}";


        given(flightService.updateFlight(any(), any())).willThrow(new UpdateException("Update error"));

        mockMvc.perform(put("/api/flights/12").contentType(MediaType.APPLICATION_JSON).content(updateRequest))
                .andExpect(status().isBadRequest())
                .andExpect((jsonPath("$").value("Update error")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void updateFlight_FlightUpdateRequestGiven_Status404() throws Exception {

        String updateRequest = "{\"airCompanyId\": 1,\"airplaneId\": 2"
                + ",\"departureCountry\": \"Ukraine\",\"destinationCountry\": \"Poland\""
                + ",\"distance\": 654,\"estimatedFlightTime\": \"01:12\"}";


        given(flightService.updateFlight(any(), any())).willThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(put("/api/flights/12").contentType(MediaType.APPLICATION_JSON).content(updateRequest))
                .andExpect(status().isNotFound())
                .andExpect((jsonPath("$").value("Not found")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void deleteFlight_RequestGiven_checkFlightServiceMethodArguments() throws Exception {

        doAnswer(invocationOnMock -> {
            Long argId = invocationOnMock.getArgument(0);

            assertEquals(4, argId);
            return null;
        }).when(flightService).deleteFlight(any(Long.class));

        mockMvc.perform(delete("/api/flights/4").contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void deleteFlight_RequestGiven_Status202() throws Exception {

        doAnswer(invocationOnMock -> {
            Long argId = invocationOnMock.getArgument(0);

            assertEquals(4, argId);
            return null;
        }).when(flightService).deleteFlight(any(Long.class));

        mockMvc.perform(delete("/api/flights/4").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
    }

    @Test
    void deleteFlight_RequestGiven_Status400() throws Exception {

        doAnswer(invocationOnMock -> {
            throw new DeleteException("Delete error");
        }).when(flightService).deleteFlight(any(Long.class));

        mockMvc.perform(delete("/api/flights/4").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect((jsonPath("$").value("Delete error")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void deleteFlight_RequestGiven_Status404() throws Exception {

        doAnswer(invocationOnMock -> {
            throw new ResourceNotFoundException("Not found");
        }).when(flightService).deleteFlight(any(Long.class));

        mockMvc.perform(delete("/api/flights/4").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect((jsonPath("$").value("Not found")))
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    void patchFlightStatus_FlightStatusUpdateRequestGiven_checkFlightServiceMethodArguments() throws Exception {
        String updateStatusRequest = "{\"status\":\"ACTIVE\"}";

        FlightStatusE status = FlightStatusE.ACTIVE;

        doAnswer(invocationOnMock -> {
            Long argId = invocationOnMock.getArgument(0);
            FlightStatusUpdateRequest arg = invocationOnMock.getArgument(1);

            assertEquals(4, argId);
            assertEquals(status, arg.getStatus());

            return null;
        }).when(flightService).setFlightStatus(any(Long.class), any(FlightStatusUpdateRequest.class));

        mockMvc.perform(patch("/api/flights/4/status").contentType(MediaType.APPLICATION_JSON).content(updateStatusRequest));
    }

    @Test
    void patchFlightStatus_RequestGiven_Status202() throws Exception {

        String updateStatusRequest = "{\"status\":\"ACTIVE\"}";

        FlightStatusE status = FlightStatusE.ACTIVE;

        doAnswer(invocationOnMock -> {
            Long argId = invocationOnMock.getArgument(0);
            FlightStatusUpdateRequest arg = invocationOnMock.getArgument(1);

            assertEquals(4, argId);
            assertEquals(status, arg.getStatus());

            return null;
        }).when(flightService).setFlightStatus(any(Long.class), any(FlightStatusUpdateRequest.class));

        mockMvc.perform(patch("/api/flights/4/status").contentType(MediaType.APPLICATION_JSON).content(updateStatusRequest))
                .andExpect(status().isAccepted());
    }

    @Test
    void patchFlightStatus_RequestGiven_Status400() throws Exception {

        String updateStatusRequest = "{\"status\":\"ACTIVE\"}";

        doAnswer(invocationOnMock -> {
            throw new UpdateException("Update error");
        }).when(flightService).setFlightStatus(any(), any());

        mockMvc.perform(patch("/api/flights/12/status").contentType(MediaType.APPLICATION_JSON).content(updateStatusRequest))
                .andExpect(status().isBadRequest())
                .andExpect((jsonPath("$").value("Update error")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void patchFlightStatus_RequestGiven_Status404() throws Exception {

        String updateStatusRequest = "{\"status\":\"ACTIVE\"}";

        doAnswer(invocationOnMock -> {
            throw new ResourceNotFoundException("Not found");
        }).when(flightService).setFlightStatus(any(), any());

        mockMvc.perform(patch("/api/flights/12/status").contentType(MediaType.APPLICATION_JSON).content(updateStatusRequest))
                .andExpect(status().isNotFound())
                .andExpect((jsonPath("$").value("Not found")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getFlightsByAirCompanyNameAndFlightStatus_RequestGiven_checkFlightServiceMethodArguments() throws Exception {
        String filterRequest = "{\"airCompanyName\":\"Jet\", \"flightStatus\":\"ACTIVE\"}";

        String airCompanyName = "Jet";
        String status = "ACTIVE";

        doAnswer(invocationOnMock -> {
            String arg1 = invocationOnMock.getArgument(0);
            String arg2 = invocationOnMock.getArgument(1);

            assertEquals(airCompanyName, arg1);
            assertEquals(status, arg2);

            return null;
        }).when(flightService).findAllByAirCompanyNameAndFlightStatus(any(String.class), any(String.class));

        mockMvc.perform(get("/api/flights/specific-filter/1").contentType(MediaType.APPLICATION_JSON).content(filterRequest));
    }

    @Test
    void getFlightsByAirCompanyNameAndFlightStatus_RequestGiven_Status200() throws Exception {
        String filterRequest = "{\"airCompanyName\":\"Jet\", \"flightStatus\":\"ACTIVE\"}";

        FlightStatus flightStatusPending = new FlightStatus(FlightStatusE.PENDING);
        FlightStatus flightStatusDelayed = new FlightStatus(FlightStatusE.DELAYED);

        CompanyType passenger = new CompanyType(CompanyTypeE.PASSENGER);
        CompanyType cargo = new CompanyType(CompanyTypeE.CARGO);

        AirCompany airCompanyPassenger = new AirCompany(1L, "Jet", passenger, LocalDateTime.now());
        AirCompany airCompanyCargo = new AirCompany(2L, "WizzAir", cargo, LocalDateTime.now());

        AirplaneType smallAirplaneType = new AirplaneType(AirplaneTypeE.SMALL);
        AirplaneType mediumAirplaneType = new AirplaneType(AirplaneTypeE.MEDIUM);

        Airplane airplane737 = new Airplane("Boeing 737", "KB1234", airCompanyPassenger, 0, 0, 300, smallAirplaneType);
        Airplane airplane707 = new Airplane("Boeing 707", "TR1234", airCompanyCargo, 1, 1, 500, mediumAirplaneType);

        Country countryUkraine = new Country("Ukraine");
        Country countryPoland = new Country("Poland");

        Flight flightUkrainePoland = new Flight(1L, flightStatusPending, airCompanyPassenger, airplane737, countryUkraine, countryPoland,
                650, Duration.ofDays(3), LocalDateTime.now(), null, null, null);

        Flight flightPolandUkraine = new Flight(1L, flightStatusDelayed, airCompanyCargo, airplane707, countryPoland, countryUkraine,
                650, Duration.ofDays(2), LocalDateTime.now().minusHours(1), LocalDateTime.now(), null, null);

        List<Flight> expectedList = new ArrayList<>(Arrays.asList(flightUkrainePoland, flightPolandUkraine));

        given(flightService.findAllByAirCompanyNameAndFlightStatus(any(), any())).willReturn(expectedList);

        mockMvc.perform(get("/api/flights/specific-filter/1").contentType(MediaType.APPLICATION_JSON).content(filterRequest))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect((jsonPath("$.[0].id").value(1)))
                .andExpect((jsonPath("$.[0].flightStatus.name").value("PENDING")))
                .andExpect((jsonPath("$.[0].airCompany.id").value(1)))
                .andExpect((jsonPath("$.[0].airCompany.name").value("Jet")))
                .andExpect((jsonPath("$.[0].airCompany.companyType.name").value("PASSENGER")))
                .andExpect((jsonPath("$.[0].airplane.name").value("Boeing 737")))
                .andExpect((jsonPath("$.[0].airplane.factorySerialNumber").value("KB1234")))
                .andExpect((jsonPath("$.[0].airplane.airplaneType.name").value("SMALL")))
                .andExpect((jsonPath("$.[0].departureCountry.name").value("Ukraine")))
                .andExpect((jsonPath("$.[0].destinationCountry.name").value("Poland")))
                .andExpect((jsonPath("$.[0].estimatedFlightTime").value("PT72H")))
                .andExpect((jsonPath("$.[1].id").value(1)))
                .andExpect((jsonPath("$.[1].flightStatus.name").value("DELAYED")))
                .andExpect((jsonPath("$.[1].airCompany.id").value(2)))
                .andExpect((jsonPath("$.[1].airCompany.name").value("WizzAir")))
                .andExpect((jsonPath("$.[1].airCompany.companyType.name").value("CARGO")))
                .andExpect((jsonPath("$.[1].airplane.name").value("Boeing 707")))
                .andExpect((jsonPath("$.[1].airplane.factorySerialNumber").value("TR1234")))
                .andExpect((jsonPath("$.[1].airplane.airplaneType.name").value("MEDIUM")))
                .andExpect((jsonPath("$.[1].departureCountry.name").value("Poland")))
                .andExpect((jsonPath("$.[1].destinationCountry.name").value("Ukraine")))
                .andExpect((jsonPath("$.[1].estimatedFlightTime").value("PT48H")));
    }


    @Test
    void getFlightsByAirCompanyNameAndFlightStatus_RequestGiven_Status404() throws Exception {
        String filterRequest = "{\"airCompanyName\":\"Jet\", \"flightStatus\":\"ACTIVE\"}";

        doAnswer(invocationOnMock -> {
            throw new ResourceNotFoundException("Not found");
        }).when(flightService).findAllByAirCompanyNameAndFlightStatus(any(), any());

        mockMvc.perform(get("/api/flights/specific-filter/1").contentType(MediaType.APPLICATION_JSON).content(filterRequest))
                .andExpect(status().isNotFound())
                .andExpect((jsonPath("$").value("Not found")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getFlightsByActiveStatusAndStartedHoursAgo_RequestGiven_checkFlightServiceMethodArguments() throws Exception {
        String filterRequest = "{\"flightStatus\":\"ACTIVE\", \"hoursAgo\": 24}";

        String status = "ACTIVE";
        int hoursAgo = 24;

        doAnswer(invocationOnMock -> {
            String arg1 = invocationOnMock.getArgument(0);
            int arg2 = invocationOnMock.getArgument(1);

            assertEquals(status, arg1);
            assertEquals(hoursAgo, arg2);

            return null;
        }).when(flightService).findAllByFlightStatusAndStartedAtMoreThanHoursAgo(any(String.class), any(Integer.class));

        mockMvc.perform(get("/api/flights/specific-filter/2").contentType(MediaType.APPLICATION_JSON).content(filterRequest));
    }

    @Test
    void getFlightsByActiveStatusAndStartedHoursAgo_RequestGiven_Status200() throws Exception {
        String filterRequest = "{\"flightStatus\":\"ACTIVE\", \"hoursAgo\": 24}";

        FlightStatus flightStatusPending = new FlightStatus(FlightStatusE.PENDING);
        FlightStatus flightStatusDelayed = new FlightStatus(FlightStatusE.DELAYED);

        CompanyType passenger = new CompanyType(CompanyTypeE.PASSENGER);
        CompanyType cargo = new CompanyType(CompanyTypeE.CARGO);

        AirCompany airCompanyPassenger = new AirCompany(1L, "Jet", passenger, LocalDateTime.now());
        AirCompany airCompanyCargo = new AirCompany(2L, "WizzAir", cargo, LocalDateTime.now());

        AirplaneType smallAirplaneType = new AirplaneType(AirplaneTypeE.SMALL);
        AirplaneType mediumAirplaneType = new AirplaneType(AirplaneTypeE.MEDIUM);

        Airplane airplane737 = new Airplane("Boeing 737", "KB1234", airCompanyPassenger, 0, 0, 300, smallAirplaneType);
        Airplane airplane707 = new Airplane("Boeing 707", "TR1234", airCompanyCargo, 1, 1, 500, mediumAirplaneType);

        Country countryUkraine = new Country("Ukraine");
        Country countryPoland = new Country("Poland");

        Flight flightUkrainePoland = new Flight(1L, flightStatusPending, airCompanyPassenger, airplane737, countryUkraine, countryPoland,
                650, Duration.ofDays(3), LocalDateTime.now(), null, null, null);

        Flight flightPolandUkraine = new Flight(1L, flightStatusDelayed, airCompanyCargo, airplane707, countryPoland, countryUkraine,
                650, Duration.ofDays(2), LocalDateTime.now().minusHours(1), LocalDateTime.now(), null, null);

        List<Flight> expectedList = new ArrayList<>(Arrays.asList(flightUkrainePoland, flightPolandUkraine));

        given(flightService.findAllByFlightStatusAndStartedAtMoreThanHoursAgo(any(String.class), any(Integer.class))).willReturn(expectedList);

        mockMvc.perform(get("/api/flights/specific-filter/2").contentType(MediaType.APPLICATION_JSON).content(filterRequest))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect((jsonPath("$.[0].id").value(1)))
                .andExpect((jsonPath("$.[0].flightStatus.name").value("PENDING")))
                .andExpect((jsonPath("$.[0].airCompany.id").value(1)))
                .andExpect((jsonPath("$.[0].airCompany.name").value("Jet")))
                .andExpect((jsonPath("$.[0].airCompany.companyType.name").value("PASSENGER")))
                .andExpect((jsonPath("$.[0].airplane.name").value("Boeing 737")))
                .andExpect((jsonPath("$.[0].airplane.factorySerialNumber").value("KB1234")))
                .andExpect((jsonPath("$.[0].airplane.airplaneType.name").value("SMALL")))
                .andExpect((jsonPath("$.[0].departureCountry.name").value("Ukraine")))
                .andExpect((jsonPath("$.[0].destinationCountry.name").value("Poland")))
                .andExpect((jsonPath("$.[0].estimatedFlightTime").value("PT72H")))
                .andExpect((jsonPath("$.[1].id").value(1)))
                .andExpect((jsonPath("$.[1].flightStatus.name").value("DELAYED")))
                .andExpect((jsonPath("$.[1].airCompany.id").value(2)))
                .andExpect((jsonPath("$.[1].airCompany.name").value("WizzAir")))
                .andExpect((jsonPath("$.[1].airCompany.companyType.name").value("CARGO")))
                .andExpect((jsonPath("$.[1].airplane.name").value("Boeing 707")))
                .andExpect((jsonPath("$.[1].airplane.factorySerialNumber").value("TR1234")))
                .andExpect((jsonPath("$.[1].airplane.airplaneType.name").value("MEDIUM")))
                .andExpect((jsonPath("$.[1].departureCountry.name").value("Poland")))
                .andExpect((jsonPath("$.[1].destinationCountry.name").value("Ukraine")))
                .andExpect((jsonPath("$.[1].estimatedFlightTime").value("PT48H")));
    }

    @Test
    void getFlightsByActiveStatusAndStartedHoursAgo_RequestGiven_Status400() throws Exception {
        String filterRequest = "{\"flightStatus\":\"ACTIVE\", \"hoursAgo\": 24}";

        doAnswer(invocationOnMock -> {
            throw new ConvertingParameterException("Convert error");
        }).when(flightService).findAllByFlightStatusAndStartedAtMoreThanHoursAgo(any(String.class), any(Integer.class));

        mockMvc.perform(get("/api/flights/specific-filter/2").contentType(MediaType.APPLICATION_JSON).content(filterRequest))
                .andExpect(status().isBadRequest())
                .andExpect((jsonPath("$").value("Convert error")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getFlightsByActiveStatusAndStartedHoursAgo_RequestGiven_Status404() throws Exception {
        String filterRequest = "{\"flightStatus\":\"ACTIVE\", \"hoursAgo\": 24}";

        doAnswer(invocationOnMock -> {
            throw new ResourceNotFoundException("Not found");
        }).when(flightService).findAllByFlightStatusAndStartedAtMoreThanHoursAgo(any(String.class), any(Integer.class));

        mockMvc.perform(get("/api/flights/specific-filter/2").contentType(MediaType.APPLICATION_JSON).content(filterRequest))
                .andExpect(status().isNotFound())
                .andExpect((jsonPath("$").value("Not found")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getCompletedFlightsWithActualFlightTimeBiggerThanEstimated_RequestGiven_Status200() throws Exception {
        FlightStatus flightStatusPending = new FlightStatus(FlightStatusE.PENDING);
        FlightStatus flightStatusDelayed = new FlightStatus(FlightStatusE.DELAYED);

        CompanyType passenger = new CompanyType(CompanyTypeE.PASSENGER);
        CompanyType cargo = new CompanyType(CompanyTypeE.CARGO);

        AirCompany airCompanyPassenger = new AirCompany(1L, "Jet", passenger, LocalDateTime.now());
        AirCompany airCompanyCargo = new AirCompany(2L, "WizzAir", cargo, LocalDateTime.now());

        AirplaneType smallAirplaneType = new AirplaneType(AirplaneTypeE.SMALL);
        AirplaneType mediumAirplaneType = new AirplaneType(AirplaneTypeE.MEDIUM);

        Airplane airplane737 = new Airplane("Boeing 737", "KB1234", airCompanyPassenger, 0, 0, 300, smallAirplaneType);
        Airplane airplane707 = new Airplane("Boeing 707", "TR1234", airCompanyCargo, 1, 1, 500, mediumAirplaneType);

        Country countryUkraine = new Country("Ukraine");
        Country countryPoland = new Country("Poland");

        Flight flightUkrainePoland = new Flight(1L, flightStatusPending, airCompanyPassenger, airplane737, countryUkraine, countryPoland,
                650, Duration.ofDays(3), LocalDateTime.now(), null, null, null);

        Flight flightPolandUkraine = new Flight(1L, flightStatusDelayed, airCompanyCargo, airplane707, countryPoland, countryUkraine,
                650, Duration.ofDays(2), LocalDateTime.now().minusHours(1), LocalDateTime.now(), null, null);

        List<Flight> expectedList = new ArrayList<>(Arrays.asList(flightUkrainePoland, flightPolandUkraine));

        given(flightService.findAllCompletedFlightsWithActualTimeBiggerThanEstimated()).willReturn(expectedList);

        mockMvc.perform(get("/api/flights/specific-filter/3").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect((jsonPath("$.[0].id").value(1)))
                .andExpect((jsonPath("$.[0].flightStatus.name").value("PENDING")))
                .andExpect((jsonPath("$.[0].airCompany.id").value(1)))
                .andExpect((jsonPath("$.[0].airCompany.name").value("Jet")))
                .andExpect((jsonPath("$.[0].airCompany.companyType.name").value("PASSENGER")))
                .andExpect((jsonPath("$.[0].airplane.name").value("Boeing 737")))
                .andExpect((jsonPath("$.[0].airplane.factorySerialNumber").value("KB1234")))
                .andExpect((jsonPath("$.[0].airplane.airplaneType.name").value("SMALL")))
                .andExpect((jsonPath("$.[0].departureCountry.name").value("Ukraine")))
                .andExpect((jsonPath("$.[0].destinationCountry.name").value("Poland")))
                .andExpect((jsonPath("$.[0].estimatedFlightTime").value("PT72H")))
                .andExpect((jsonPath("$.[1].id").value(1)))
                .andExpect((jsonPath("$.[1].flightStatus.name").value("DELAYED")))
                .andExpect((jsonPath("$.[1].airCompany.id").value(2)))
                .andExpect((jsonPath("$.[1].airCompany.name").value("WizzAir")))
                .andExpect((jsonPath("$.[1].airCompany.companyType.name").value("CARGO")))
                .andExpect((jsonPath("$.[1].airplane.name").value("Boeing 707")))
                .andExpect((jsonPath("$.[1].airplane.factorySerialNumber").value("TR1234")))
                .andExpect((jsonPath("$.[1].airplane.airplaneType.name").value("MEDIUM")))
                .andExpect((jsonPath("$.[1].departureCountry.name").value("Poland")))
                .andExpect((jsonPath("$.[1].destinationCountry.name").value("Ukraine")))
                .andExpect((jsonPath("$.[1].estimatedFlightTime").value("PT48H")));
    }

    @Test
    void getCompletedFlightsWithActualFlightTimeBiggerThanEstimated_RequestGiven_Status404() throws Exception {
        doAnswer(invocationOnMock -> {
            throw new ResourceNotFoundException("Not found");
        }).when(flightService).findAllCompletedFlightsWithActualTimeBiggerThanEstimated();

        mockMvc.perform(get("/api/flights/specific-filter/3").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect((jsonPath("$").value("Not found")))
                .andDo(MockMvcResultHandlers.print());
    }
}