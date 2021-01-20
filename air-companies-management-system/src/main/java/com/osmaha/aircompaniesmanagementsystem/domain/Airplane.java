package com.osmaha.aircompaniesmanagementsystem.domain;

import javax.persistence.*;

@Entity
@Table(name = "airplanes")
public class Airplane {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "factory_serial_number", nullable = false, unique = true)
    private String factorySerialNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "air_company_id", referencedColumnName = "id", nullable = true)
    private AirCompany airCompany;

    @Column(name = "number_of_flights")
    private int numberOfFlights;

    @Column(name = "flight_distance")
    private double flightDistance;

    @Column(name = "fuel_capacity")
    private double fuelCapacity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "airplane_type_id", referencedColumnName = "id")
    private AirplaneType airplaneType;

    public Airplane() {
    }

    public Airplane(String name, String factorySerialNumber, int numberOfFlights, double flightDistance, double fuelCapacity, AirplaneType airplaneType) {
        this.name = name;
        this.factorySerialNumber = factorySerialNumber;
        this.numberOfFlights = numberOfFlights;
        this.flightDistance = flightDistance;
        this.fuelCapacity = fuelCapacity;
        this.airplaneType = airplaneType;
    }

    public Airplane(String name, String factorySerialNumber, AirCompany airCompany, int numberOfFlights, double flightDistance, double fuelCapacity, AirplaneType airplaneType) {
        this.name = name;
        this.factorySerialNumber = factorySerialNumber;
        this.airCompany = airCompany;
        this.numberOfFlights = numberOfFlights;
        this.flightDistance = flightDistance;
        this.fuelCapacity = fuelCapacity;
        this.airplaneType = airplaneType;
    }

    public Airplane(Long id, String name, String factorySerialNumber, AirCompany airCompany, int numberOfFlights, double flightDistance, double fuelCapacity, AirplaneType airplaneType) {
        this.id = id;
        this.name = name;
        this.factorySerialNumber = factorySerialNumber;
        this.airCompany = airCompany;
        this.numberOfFlights = numberOfFlights;
        this.flightDistance = flightDistance;
        this.fuelCapacity = fuelCapacity;
        this.airplaneType = airplaneType;
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

    public String getFactorySerialNumber() {
        return factorySerialNumber;
    }

    public void setFactorySerialNumber(String factorySerialNumber) {
        this.factorySerialNumber = factorySerialNumber;
    }

    public AirCompany getAirCompany() {
        return airCompany;
    }

    public void setAirCompany(AirCompany airCompany) {
        this.airCompany = airCompany;
    }

    public int getNumberOfFlights() {
        return numberOfFlights;
    }

    public void setNumberOfFlights(int numberOfFlights) {
        this.numberOfFlights = numberOfFlights;
    }

    public double getFlightDistance() {
        return flightDistance;
    }

    public void setFlightDistance(double flightDistance) {
        this.flightDistance = flightDistance;
    }

    public double getFuelCapacity() {
        return fuelCapacity;
    }

    public void setFuelCapacity(double fuelCapacity) {
        this.fuelCapacity = fuelCapacity;
    }

    public AirplaneType getAirplaneType() {
        return airplaneType;
    }

    public void setAirplaneType(AirplaneType airplaneType) {
        this.airplaneType = airplaneType;
    }

}
