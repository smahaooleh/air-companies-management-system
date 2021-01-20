package com.osmaha.aircompaniesmanagementsystem.domain;

import com.osmaha.aircompaniesmanagementsystem.domain.converter.DurationToStringConverter;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "flights")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private FlightStatus flightStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "air_company_id", referencedColumnName = "id")
    private AirCompany airCompany;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "airplane_id", referencedColumnName = "id")
    private Airplane airplane;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "departure_country_id", referencedColumnName = "id")
    private Country departureCountry;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "destination_country_id", referencedColumnName = "id")
    private Country destinationCountry;

    @Column
    private int distance;

    @Column(name = "estimated_flight_time")
    @Convert(converter = DurationToStringConverter.class)
    private Duration estimatedFlightTime;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "delay_started_at")
    private LocalDateTime delayStartedAt;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    public Flight() {
    }

    public Flight(FlightStatus flightStatus, AirCompany airCompany, Airplane airplane, Country departureCountry, Country destinationCountry, int distance, Duration estimatedFlightTime, LocalDateTime createdAt) {
        this.flightStatus = flightStatus;
        this.airCompany = airCompany;
        this.airplane = airplane;
        this.departureCountry = departureCountry;
        this.destinationCountry = destinationCountry;
        this.distance = distance;
        this.estimatedFlightTime = estimatedFlightTime;
        this.createdAt = createdAt;
    }

    public Flight(FlightStatus flightStatus, AirCompany airCompany, Airplane airplane, Country departureCountry, Country destinationCountry, int distance, Duration estimatedFlightTime, LocalDateTime createdAt, LocalDateTime delayStartedAt, LocalDateTime startedAt, LocalDateTime endedAt) {
        this.flightStatus = flightStatus;
        this.airCompany = airCompany;
        this.airplane = airplane;
        this.departureCountry = departureCountry;
        this.destinationCountry = destinationCountry;
        this.distance = distance;
        this.estimatedFlightTime = estimatedFlightTime;
        this.createdAt = createdAt;
        this.delayStartedAt = delayStartedAt;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }

    public Flight(Long id, FlightStatus flightStatus, AirCompany airCompany, Airplane airplane, Country departureCountry, Country destinationCountry, int distance, Duration estimatedFlightTime, LocalDateTime createdAt, LocalDateTime delayStartedAt, LocalDateTime startedAt, LocalDateTime endedAt) {
        this.id = id;
        this.flightStatus = flightStatus;
        this.airCompany = airCompany;
        this.airplane = airplane;
        this.departureCountry = departureCountry;
        this.destinationCountry = destinationCountry;
        this.distance = distance;
        this.estimatedFlightTime = estimatedFlightTime;
        this.createdAt = createdAt;
        this.delayStartedAt = delayStartedAt;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FlightStatus getFlightStatus() {
        return flightStatus;
    }

    public void setFlightStatus(FlightStatus flightStatus) {
        this.flightStatus = flightStatus;
    }

    public AirCompany getAirCompany() {
        return airCompany;
    }

    public void setAirCompany(AirCompany airCompany) {
        this.airCompany = airCompany;
    }

    public Airplane getAirplane() {
        return airplane;
    }

    public void setAirplane(Airplane airplane) {
        this.airplane = airplane;
    }

    public Country getDepartureCountry() {
        return departureCountry;
    }

    public void setDepartureCountry(Country departureCountry) {
        this.departureCountry = departureCountry;
    }

    public Country getDestinationCountry() {
        return destinationCountry;
    }

    public void setDestinationCountry(Country destinationCountry) {
        this.destinationCountry = destinationCountry;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Duration getEstimatedFlightTime() {
        return estimatedFlightTime;
    }

    public void setEstimatedFlightTime(Duration estimatedFlightTime) {
        this.estimatedFlightTime = estimatedFlightTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getDelayStartedAt() {
        return delayStartedAt;
    }

    public void setDelayStartedAt(LocalDateTime delayStartedAt) {
        this.delayStartedAt = delayStartedAt;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(LocalDateTime endedAt) {
        this.endedAt = endedAt;
    }
}
