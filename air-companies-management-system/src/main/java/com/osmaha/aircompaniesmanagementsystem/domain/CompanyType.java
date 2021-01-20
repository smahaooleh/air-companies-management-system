package com.osmaha.aircompaniesmanagementsystem.domain;

import com.osmaha.aircompaniesmanagementsystem.domain.enums.CompanyTypeE;

import javax.persistence.*;

@Entity
@Table(name = "company_types")
public class CompanyType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, unique = true)
    private CompanyTypeE name;

    public CompanyType() {
    }

    public CompanyType(CompanyTypeE name) {
        this.name = name;
    }

    public CompanyType(Long id, CompanyTypeE name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CompanyTypeE getName() {
        return name;
    }

    public void setName(CompanyTypeE name) {
        this.name = name;
    }
}
