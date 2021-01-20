package com.osmaha.aircompaniesmanagementsystem.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "air_companies")
public class AirCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_type_id", referencedColumnName = "id")
    private CompanyType companyType;

    @Column(name = "founded_at")
    private LocalDateTime foundedAt;

    public AirCompany() {
    }

    public AirCompany(String name, CompanyType companyType, LocalDateTime foundedAt) {
        this.name = name;
        this.companyType = companyType;
        this.foundedAt = foundedAt;
    }

    public AirCompany(Long id, String name, CompanyType companyType) {
        this.id = id;
        this.name = name;
        this.companyType = companyType;
    }

    public AirCompany(Long id, String name, CompanyType companyType, LocalDateTime foundedAt) {
        this.id = id;
        this.name = name;
        this.companyType = companyType;
        this.foundedAt = foundedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CompanyType getCompanyType() {
        return companyType;
    }

    public void setCompanyType(CompanyType companyType) {
        this.companyType = companyType;
    }

    public LocalDateTime getFoundedAt() {
        return foundedAt;
    }

    public void setFoundedAt(LocalDateTime foundedAt) {
        this.foundedAt = foundedAt;
    }
}
