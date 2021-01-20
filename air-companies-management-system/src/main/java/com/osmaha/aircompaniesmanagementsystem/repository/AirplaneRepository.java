package com.osmaha.aircompaniesmanagementsystem.repository;

import com.osmaha.aircompaniesmanagementsystem.domain.Airplane;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AirplaneRepository extends JpaRepository<Airplane, Long> {

    Optional<Airplane> findByFactorySerialNumber(String factorySerialNumber);
}
