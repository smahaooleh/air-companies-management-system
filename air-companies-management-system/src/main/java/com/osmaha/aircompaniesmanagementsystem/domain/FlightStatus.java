package com.osmaha.aircompaniesmanagementsystem.domain;

import com.osmaha.aircompaniesmanagementsystem.domain.enums.FlightStatusE;

import javax.persistence.*;

@Entity
@Table(name = "flight_status")
public class FlightStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, unique = true)
    private FlightStatusE name;

    public FlightStatus() {
    }

    public FlightStatus(FlightStatusE name) {
        this.name = name;
    }

    public FlightStatus(Long id, FlightStatusE name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FlightStatusE getName() {
        return name;
    }

    public void setName(FlightStatusE name) {
        this.name = name;
    }
}
