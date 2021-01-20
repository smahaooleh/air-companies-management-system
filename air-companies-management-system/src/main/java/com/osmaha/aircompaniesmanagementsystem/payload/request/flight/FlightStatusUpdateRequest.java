package com.osmaha.aircompaniesmanagementsystem.payload.request.flight;

import com.osmaha.aircompaniesmanagementsystem.domain.enums.FlightStatusE;

import javax.validation.constraints.NotBlank;

public class FlightStatusUpdateRequest {

    @NotBlank
    private FlightStatusE status;

    public FlightStatusE getStatus() {
        return status;
    }

    public void setStatus(FlightStatusE status) {
        this.status = status;
    }
}
