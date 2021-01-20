package com.osmaha.aircompaniesmanagementsystem.payload.request.flight;

import javax.validation.constraints.NotBlank;

public class FlightCreationRequest {

    @NotBlank
    private Long airCompanyId;

    @NotBlank
    private Long airplaneId;

    @NotBlank
    private String departureCountry;

    @NotBlank
    private String destinationCountry;

    @NotBlank
    private int distance;

    @NotBlank
    private String estimatedFlightTime;

    public Long getAirCompanyId() {
        return airCompanyId;
    }

    public void setAirCompanyId(Long airCompanyId) {
        this.airCompanyId = airCompanyId;
    }

    public Long getAirplaneId() {
        return airplaneId;
    }

    public void setAirplaneId(Long airplaneId) {
        this.airplaneId = airplaneId;
    }

    public String getDepartureCountry() {
        return departureCountry;
    }

    public void setDepartureCountry(String departureCountry) {
        this.departureCountry = departureCountry;
    }

    public String getDestinationCountry() {
        return destinationCountry;
    }

    public void setDestinationCountry(String destinationCountry) {
        this.destinationCountry = destinationCountry;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getEstimatedFlightTime() {
        return estimatedFlightTime;
    }

    public void setEstimatedFlightTime(String estimatedFlightTime) {
        this.estimatedFlightTime = estimatedFlightTime;
    }
}
