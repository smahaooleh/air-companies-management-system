package com.osmaha.aircompaniesmanagementsystem.repository.util;

import com.osmaha.aircompaniesmanagementsystem.domain.AirplaneType;
import com.osmaha.aircompaniesmanagementsystem.domain.enums.AirplaneTypeE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AirplaneTypeRepository extends JpaRepository<AirplaneType, Long> {

    Optional<AirplaneType> findByName(AirplaneTypeE airplaneTypeE);
}
