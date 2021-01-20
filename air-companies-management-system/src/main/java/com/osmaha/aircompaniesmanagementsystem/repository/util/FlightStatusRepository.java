package com.osmaha.aircompaniesmanagementsystem.repository.util;

import com.osmaha.aircompaniesmanagementsystem.domain.FlightStatus;
import com.osmaha.aircompaniesmanagementsystem.domain.enums.FlightStatusE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlightStatusRepository extends JpaRepository<FlightStatus, Long> {

    Optional<FlightStatus> findByName(FlightStatusE flightStatusE);
}
