package com.osmaha.aircompaniesmanagementsystem.repository;

import com.osmaha.aircompaniesmanagementsystem.domain.AirCompany;
import com.osmaha.aircompaniesmanagementsystem.domain.Airplane;
import com.osmaha.aircompaniesmanagementsystem.domain.Flight;
import com.osmaha.aircompaniesmanagementsystem.domain.FlightStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    List<Flight> findAllByAirplane(Airplane airplane);

    List<Flight> findAllByFlightStatus(FlightStatus flightStatus);

    List<Flight> findAllByAirCompanyAndFlightStatus(AirCompany airCompany, FlightStatus flightStatus);

    @Query("select f from Flight f where f.flightStatus = :flightStatus and f.startedAt < :beforeDateTime")
    List<Flight> findAllByFlightStatusAndStartedAtMoreThanHoursAgo(@Param("flightStatus") FlightStatus flightStatus,
                                                                   @Param("beforeDateTime") LocalDateTime beforeDateTime);

}
