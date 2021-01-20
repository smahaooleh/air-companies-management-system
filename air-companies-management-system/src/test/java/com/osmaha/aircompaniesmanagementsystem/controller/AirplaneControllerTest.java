package com.osmaha.aircompaniesmanagementsystem.controller;

import com.osmaha.aircompaniesmanagementsystem.domain.AirCompany;
import com.osmaha.aircompaniesmanagementsystem.domain.Airplane;
import com.osmaha.aircompaniesmanagementsystem.domain.AirplaneType;
import com.osmaha.aircompaniesmanagementsystem.domain.CompanyType;
import com.osmaha.aircompaniesmanagementsystem.domain.enums.AirplaneTypeE;
import com.osmaha.aircompaniesmanagementsystem.domain.enums.CompanyTypeE;
import com.osmaha.aircompaniesmanagementsystem.payload.request.aircompany.AirCompanyCreationRequest;
import com.osmaha.aircompaniesmanagementsystem.payload.request.airplane.AirplaneCreationRequest;
import com.osmaha.aircompaniesmanagementsystem.payload.request.airplane.AirplaneUpdateRequest;
import com.osmaha.aircompaniesmanagementsystem.service.AirplaneService;
import com.osmaha.aircompaniesmanagementsystem.service.exception.DeleteException;
import com.osmaha.aircompaniesmanagementsystem.service.exception.RegistrationException;
import com.osmaha.aircompaniesmanagementsystem.service.exception.ResourceNotFoundException;
import com.osmaha.aircompaniesmanagementsystem.service.exception.UpdateException;
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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AirplaneController.class)
class AirplaneControllerTest {

    @MockBean
    private AirplaneService airplaneService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAirplanes_Status200() throws Exception {

        AirplaneType smallAirplaneType = new AirplaneType(AirplaneTypeE.SMALL);
        AirplaneType mediumAirplaneType = new AirplaneType(AirplaneTypeE.MEDIUM);
        AirplaneType largeAirplaneType = new AirplaneType(AirplaneTypeE.LARGE);

        AirCompany airCompany = new AirCompany("Jet", new CompanyType(CompanyTypeE.CARGO), LocalDateTime.now());

        List<Airplane> expectedList = new ArrayList<>(Arrays.asList(
                new Airplane("Boeing 737", "KB1234", airCompany, 0, 0, 300, smallAirplaneType),
                new Airplane("Boeing 707", "TR1234", airCompany, 1, 1, 500, mediumAirplaneType),
                new Airplane("Boeing 800", "TK1234", airCompany, 3, 3, 700, largeAirplaneType)
        ));

        given(airplaneService.findAll()).willReturn(expectedList);

        mockMvc.perform(get("/api/airplanes").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect((jsonPath("$.[0].name").value("Boeing 737")))
                .andExpect((jsonPath("$.[0].factorySerialNumber").value("KB1234")))
                .andExpect((jsonPath("$.[0].airCompany.name").value("Jet")))
                .andExpect((jsonPath("$.[0].numberOfFlights").value(0)))
                .andExpect((jsonPath("$.[0].flightDistance").value(0)))
                .andExpect((jsonPath("$.[0].fuelCapacity").value(300)))
                .andExpect((jsonPath("$.[0].airplaneType.name").value("SMALL")))

                .andExpect((jsonPath("$.[1].name").value("Boeing 707")))
                .andExpect((jsonPath("$.[1].factorySerialNumber").value("TR1234")))
                .andExpect((jsonPath("$.[1].airCompany.name").value("Jet")))
                .andExpect((jsonPath("$.[1].numberOfFlights").value(1)))
                .andExpect((jsonPath("$.[1].flightDistance").value(1)))
                .andExpect((jsonPath("$.[1].fuelCapacity").value(500)))
                .andExpect((jsonPath("$.[1].airplaneType.name").value("MEDIUM")))

                .andExpect((jsonPath("$.[2].name").value("Boeing 800")))
                .andExpect((jsonPath("$.[2].factorySerialNumber").value("TK1234")))
                .andExpect((jsonPath("$.[2].airCompany.name").value("Jet")))
                .andExpect((jsonPath("$.[2].numberOfFlights").value(3)))
                .andExpect((jsonPath("$.[2].flightDistance").value(3)))
                .andExpect((jsonPath("$.[2].fuelCapacity").value(700)))
                .andExpect((jsonPath("$.[2].airplaneType.name").value("LARGE")));

    }

    @Test
    void getAirplaneById_AirplaneIdRequestGiven_checkAirplaneServiceMethodArguments() throws Exception {
        Long airplaneId = 13L;

        doAnswer(invocationOnMock -> {
            Long argId = invocationOnMock.getArgument(0);

            assertEquals(airplaneId, argId);

            return null;
        }).when(airplaneService).findById(any(Long.class));

        mockMvc.perform(get("/api/airplane/13").contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getAirplaneById_Status200() throws Exception {
        AirplaneType smallAirplaneType = new AirplaneType(AirplaneTypeE.SMALL);
        AirCompany airCompany = new AirCompany("Jet", new CompanyType(CompanyTypeE.CARGO), LocalDateTime.now());
        Airplane expectedAirplane = new Airplane("Boeing 737", "KB1234", airCompany, 0, 0, 300, smallAirplaneType);

        given(airplaneService.findById(any())).willReturn(expectedAirplane);

        mockMvc.perform(get("/api/airplanes/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect((jsonPath("$.name").value("Boeing 737")))
                .andExpect((jsonPath("$.factorySerialNumber").value("KB1234")))
                .andExpect((jsonPath("$.airCompany.name").value("Jet")))
                .andExpect((jsonPath("$.numberOfFlights").value(0)))
                .andExpect((jsonPath("$.flightDistance").value(0)))
                .andExpect((jsonPath("$.fuelCapacity").value(300)))
                .andExpect((jsonPath("$.airplaneType.name").value("SMALL")));
    }

    @Test
    void getAirplaneById_Status404() throws Exception {

        given(airplaneService.findById(any())).willThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(get("/api/airplanes/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect((jsonPath("$").value("Not found")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void createAirplane_AirplaneCreationRequestGiven_checkAirplaneServiceMethodArguments() throws Exception {

        String creationRequest = "{\"name\": \"Boeing 737\",\"factorySerialNumber\":\"B15463521\""
                                + ",\"airCompanyId\": 12,\"numberOfFlights\": 53,\"flightDistance\": 654.56"
                                + ",\"fuelCapacity\": 158,\"type\": \"MEDIUM\"}";

        String name = "Boeing 737";
        String factorySerialNumber = "B15463521";
        Long airCompanyId = 12L;
        int numberOfFlights = 53;
        double flightDistance = 654.56;
        double fuelCapacity = 158;
        String type = "MEDIUM";

        doAnswer(invocationOnMock -> {
            AirplaneCreationRequest arg = invocationOnMock.getArgument(0);

            assertEquals(name, arg.getName());
            assertEquals(factorySerialNumber, arg.getFactorySerialNumber());
            assertEquals(airCompanyId, arg.getAirCompanyId());
            assertEquals(numberOfFlights, arg.getNumberOfFlights());
            assertEquals(flightDistance, arg.getFlightDistance());
            assertEquals(fuelCapacity, arg.getFuelCapacity());
            assertEquals(type, arg.getType());

            return null;
        }).when(airplaneService).createAirplane(any(AirplaneCreationRequest.class));

        mockMvc.perform(post("/api/airplanes").contentType(MediaType.APPLICATION_JSON).content(creationRequest));
    }

    @Test
    void createAirplane_AirplaneCreationRequestWithAirCompanyNullGiven_checkAirplaneServiceMethodArguments() throws Exception {

        String creationRequest = "{\"name\": \"Boeing 737\",\"factorySerialNumber\":\"B15463521\""
                + ",\"airCompanyId\": null,\"numberOfFlights\": 53,\"flightDistance\": 654.56"
                + ",\"fuelCapacity\": 158,\"type\": \"MEDIUM\"}";

        String name = "Boeing 737";
        String factorySerialNumber = "B15463521";
        Long airCompanyId = 12L;
        int numberOfFlights = 53;
        double flightDistance = 654.56;
        double fuelCapacity = 158;
        String type = "MEDIUM";



        doAnswer(invocationOnMock -> {
            AirplaneCreationRequest arg = invocationOnMock.getArgument(0);

            assertEquals(name, arg.getName());
            assertEquals(factorySerialNumber, arg.getFactorySerialNumber());
            assertNull(arg.getAirCompanyId());
            assertEquals(numberOfFlights, arg.getNumberOfFlights());
            assertEquals(flightDistance, arg.getFlightDistance());
            assertEquals(fuelCapacity, arg.getFuelCapacity());
            assertEquals(type, arg.getType());

            return null;
        }).when(airplaneService).createAirplane(any(AirplaneCreationRequest.class));

        mockMvc.perform(post("/api/airplanes").contentType(MediaType.APPLICATION_JSON).content(creationRequest));

    }

    @Test
    void createAirplane_AirplaneCreationRequestGiven_Status200() throws Exception {

        String creationRequest = "{\"name\": \"Boeing 737\",\"factorySerialNumber\":\"B15463521\""
                + ",\"airCompanyId\": 12,\"numberOfFlights\": 53,\"flightDistance\": 654.56"
                + ",\"fuelCapacity\": 158,\"type\": \"MEDIUM\"}";

        AirplaneType smallAirplaneType = new AirplaneType(AirplaneTypeE.SMALL);
        AirCompany airCompany = new AirCompany("Jet", new CompanyType(CompanyTypeE.CARGO), LocalDateTime.now());

        doAnswer(invocationOnMock -> {
            return new Airplane(1L,"Boeing 737", "KB1234", airCompany, 0, 0, 300, smallAirplaneType);
        }).when(airplaneService).createAirplane(any(AirplaneCreationRequest.class));

        mockMvc.perform(post("/api/airplanes").contentType(MediaType.APPLICATION_JSON).content(creationRequest))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect((jsonPath("$.name").value("Boeing 737")))
                .andExpect((jsonPath("$.factorySerialNumber").value("KB1234")))
                .andExpect((jsonPath("$.airCompany.name").value("Jet")))
                .andExpect((jsonPath("$.numberOfFlights").value(0)))
                .andExpect((jsonPath("$.flightDistance").value(0)))
                .andExpect((jsonPath("$.fuelCapacity").value(300)))
                .andExpect((jsonPath("$.airplaneType.name").value("SMALL")));
    }

    @Test
    void createAirplane_AirplaneCreationRequestGiven_Status400() throws Exception {

        String creationRequest = "{\"name\": \"Boeing 737\",\"factorySerialNumber\":\"B15463521\""
                + ",\"airCompanyId\": 12,\"numberOfFlights\": 53,\"flightDistance\": 654.56"
                + ",\"fuelCapacity\": 158,\"type\": \"MEDIUM\"}";

        given(airplaneService.createAirplane(any())).willThrow(new RegistrationException("Registration error"));

        mockMvc.perform(post("/api/airplanes").contentType(MediaType.APPLICATION_JSON).content(creationRequest))
                .andExpect(status().isBadRequest())
                .andExpect((jsonPath("$").value("Registration error")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void createAirplane_AirplaneCreationRequestGiven_Status404() throws Exception {
        String creationRequest = "{\"name\": \"Boeing 737\",\"factorySerialNumber\":\"B15463521\""
                + ",\"airCompanyId\": 12,\"numberOfFlights\": 53,\"flightDistance\": 654.56"
                + ",\"fuelCapacity\": 158,\"type\": \"MEDIUM\"}";

        given(airplaneService.createAirplane(any())).willThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(post("/api/airplanes").contentType(MediaType.APPLICATION_JSON).content(creationRequest))
                .andExpect(status().isNotFound())
                .andExpect((jsonPath("$").value("Not found")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void updateAirplane_AirplaneUpdateRequestGiven_checkAirplaneServiceMethodArguments() throws Exception {
        String updateRequest = "{\"name\": \"Boeing 737\""
                + ",\"numberOfFlights\": 53,\"flightDistance\": 654.56"
                + ",\"fuelCapacity\": 158,\"type\": \"MEDIUM\"}";

        String name = "Boeing 737";
        int numberOfFlights = 53;
        double flightDistance = 654.56;
        double fuelCapacity = 158;
        String type = "MEDIUM";

        doAnswer(invocationOnMock -> {
            Long argId = invocationOnMock.getArgument(0);
            AirplaneUpdateRequest arg = invocationOnMock.getArgument(1);

            assertEquals(3, argId);
            assertEquals(name, arg.getName());
            assertEquals(numberOfFlights, arg.getNumberOfFlights());
            assertEquals(flightDistance, arg.getFlightDistance());
            assertEquals(fuelCapacity, arg.getFuelCapacity());
            assertEquals(type, arg.getType());

            return null;
        }).when(airplaneService).updateAirplane(any(Long.class),any(AirplaneUpdateRequest.class));

        mockMvc.perform(put("/api/airplanes/3").contentType(MediaType.APPLICATION_JSON).content(updateRequest));
    }

    @Test
    void updateAirplane_AirplaneUpdateRequestGiven_Status200() throws Exception{

        String updateRequest = "{\"name\": \"Boeing 737\",\"factorySerialNumber\":\"B15463521\""
                + ",\"airCompanyId\": 12,\"numberOfFlights\": 53,\"flightDistance\": 654.56"
                + ",\"fuelCapacity\": 158,\"type\": \"MEDIUM\"}";

        AirplaneType mediumAirplaneType = new AirplaneType(AirplaneTypeE.MEDIUM);
        AirCompany airCompany = new AirCompany("Jet", new CompanyType(CompanyTypeE.CARGO), LocalDateTime.now());

        doAnswer(invocationOnMock -> {
            return new Airplane(1L,"Boeing 737", "KB1234", airCompany, 0, 654.56, 158, mediumAirplaneType);
        }).when(airplaneService).updateAirplane(any(Long.class), any(AirplaneUpdateRequest.class));

        mockMvc.perform(put("/api/airplanes/4").contentType(MediaType.APPLICATION_JSON).content(updateRequest))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect((jsonPath("$.name").value("Boeing 737")))
                .andExpect((jsonPath("$.factorySerialNumber").value("KB1234")))
                .andExpect((jsonPath("$.airCompany.name").value("Jet")))
                .andExpect((jsonPath("$.numberOfFlights").value(0)))
                .andExpect((jsonPath("$.flightDistance").value(654.56)))
                .andExpect((jsonPath("$.fuelCapacity").value(158)))
                .andExpect((jsonPath("$.airplaneType.name").value("MEDIUM")));
    }

    @Test
    void updateAirplane_AirplaneUpdateRequestGiven_Status400() throws Exception {

        String updateRequest = "{\"name\": \"Boeing 737\",\"factorySerialNumber\":\"B15463521\""
                + ",\"airCompanyId\": 12,\"numberOfFlights\": 53,\"flightDistance\": 654.56"
                + ",\"fuelCapacity\": 158,\"type\": \"MEDIUM\"}";

        given(airplaneService.updateAirplane(any(), any())).willThrow(new UpdateException("Update error"));

        mockMvc.perform(put("/api/airplanes/4").contentType(MediaType.APPLICATION_JSON).content(updateRequest))
                .andExpect(status().isBadRequest())
                .andExpect((jsonPath("$").value("Update error")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void updateAirplane_AirplaneUpdateRequestGiven_Status404() throws Exception {

        String updateRequest = "{\"name\": \"Boeing 737\",\"factorySerialNumber\":\"B15463521\""
                + ",\"airCompanyId\": 12,\"numberOfFlights\": 53,\"flightDistance\": 654.56"
                + ",\"fuelCapacity\": 158,\"type\": \"MEDIUM\"}";

        given(airplaneService.updateAirplane(any(), any())).willThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(put("/api/airplanes/4").contentType(MediaType.APPLICATION_JSON).content(updateRequest))
                .andExpect(status().isNotFound())
                .andExpect((jsonPath("$").value("Not found")))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    void deleteAirplane_RequestGiven_checkAirplaneServiceMethodArguments() throws Exception {

        doAnswer(invocationOnMock -> {
            Long argId = invocationOnMock.getArgument(0);

            assertEquals(4, argId);
            return null;
        }).when(airplaneService).deleteAirplane(any(Long.class));

        mockMvc.perform(delete("/api/airplanes/4").contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void deleteAirplane_RequestGiven_Status202() throws Exception {

        doAnswer(invocationOnMock -> {
            Long argId = invocationOnMock.getArgument(0);

            assertEquals(4, argId);
            return null;
        }).when(airplaneService).deleteAirplane(any(Long.class));

        mockMvc.perform(delete("/api/airplanes/4").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
    }

    @Test
    void deleteAirplane_RequestGiven_Status400() throws Exception {

        doAnswer(invocationOnMock -> {
            throw new DeleteException("Delete error");
        }).when(airplaneService).deleteAirplane(any(Long.class));

        mockMvc.perform(delete("/api/airplanes/4").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect((jsonPath("$").value("Delete error")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void deleteAirplane_RequestGiven_Status404() throws Exception {

        doAnswer(invocationOnMock -> {
            throw new ResourceNotFoundException("Not found");
        }).when(airplaneService).deleteAirplane(any(Long.class));

        mockMvc.perform(delete("/api/airplanes/4").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect((jsonPath("$").value("Not found")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void reassignAirplaneToAnotherCompany_RequestGiven_checkAirplaneServiceMethodArguments() throws Exception {

        doAnswer(invocationOnMock -> {
            Long argAirplaneId = invocationOnMock.getArgument(0);
            Long argAirCompanyId = invocationOnMock.getArgument(1);

            assertEquals(4, argAirplaneId);
            assertEquals(5, argAirCompanyId);
            return null;
        }).when(airplaneService).reassignAirplaneToAnotherCompany(any(Long.class),any(Long.class));

        mockMvc.perform(patch("/api/airplanes/4/reassign").param("airCompanyId", "5").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
    }

    @Test
    void reassignAirplaneToAnotherCompany_RequestGiven_Status202() throws Exception {
        doAnswer(invocationOnMock -> {
            return null;
        }).when(airplaneService).reassignAirplaneToAnotherCompany(any(Long.class),any(Long.class));

        mockMvc.perform(patch("/api/airplanes/4/reassign").param("airCompanyId", "5").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
    }

    @Test
    void reassignAirplaneToAnotherCompany_RequestGiven_Status400() throws Exception {
        doAnswer(invocationOnMock -> {
            throw new UpdateException("Update error");
        }).when(airplaneService).reassignAirplaneToAnotherCompany(any(Long.class),any(Long.class));

        mockMvc.perform(patch("/api/airplanes/4/reassign").param("airCompanyId", "5").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect((jsonPath("$").value("Update error")))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    void reassignAirplaneToAnotherCompany_RequestGiven_Status404() throws Exception {

        doAnswer(invocationOnMock -> {
            throw new ResourceNotFoundException("Not found");
        }).when(airplaneService).reassignAirplaneToAnotherCompany(any(Long.class),any(Long.class));

        mockMvc.perform(patch("/api/airplanes/4/reassign").param("airCompanyId", "5").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect((jsonPath("$").value("Not found")))
                .andDo(MockMvcResultHandlers.print());
    }

}