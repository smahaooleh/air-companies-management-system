package com.osmaha.aircompaniesmanagementsystem.payload.request.flight;

import javax.validation.constraints.NotBlank;

public class FilteringByActiveFlightStatusAndStartedHoursAgoRequest {

    @NotBlank
    private String flightStatus;

    @NotBlank
    private int hoursAgo;

    public String getFlightStatus() {
        return flightStatus;
    }

    public void setFlightStatus(String flightStatus) {
        this.flightStatus = flightStatus;
    }

    public int getHoursAgo() {
        return hoursAgo;
    }

    public void setHoursAgo(int hoursAgo) {
        this.hoursAgo = hoursAgo;
    }
}
