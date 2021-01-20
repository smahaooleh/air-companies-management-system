package com.osmaha.aircompaniesmanagementsystem.payload.request.flight;

import javax.validation.constraints.NotBlank;

public class FilteringByAirCompanyNameAndFlightStatusRequest {

    @NotBlank
    private String airCompanyName;

    @NotBlank
    private String flightStatus;

    public String getAirCompanyName() {
        return airCompanyName;
    }

    public void setAirCompanyName(String airCompanyName) {
        this.airCompanyName = airCompanyName;
    }

    public String getFlightStatus() {
        return flightStatus;
    }

    public void setFlightStatus(String flightStatus) {
        this.flightStatus = flightStatus;
    }
}
