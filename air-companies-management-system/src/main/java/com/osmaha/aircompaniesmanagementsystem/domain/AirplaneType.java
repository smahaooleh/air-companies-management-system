package com.osmaha.aircompaniesmanagementsystem.domain;

import com.osmaha.aircompaniesmanagementsystem.domain.enums.AirplaneTypeE;

import javax.persistence.*;

@Entity
@Table(name = "airplane_types")
public class AirplaneType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, unique = true)
    private AirplaneTypeE name;

    public AirplaneType() {
    }

    public AirplaneType(AirplaneTypeE name) {
        this.name = name;
    }

    public AirplaneType(Long id, AirplaneTypeE name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AirplaneTypeE getName() {
        return name;
    }

    public void setName(AirplaneTypeE name) {
        this.name = name;
    }
}
