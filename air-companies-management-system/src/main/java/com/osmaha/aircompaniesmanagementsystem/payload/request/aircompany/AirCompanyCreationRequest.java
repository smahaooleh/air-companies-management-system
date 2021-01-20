package com.osmaha.aircompaniesmanagementsystem.payload.request.aircompany;

import javax.validation.constraints.NotBlank;

public class AirCompanyCreationRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String companyType;

    public AirCompanyCreationRequest(@NotBlank String name, @NotBlank String companyType) {
        this.name = name;
        this.companyType = companyType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

}
