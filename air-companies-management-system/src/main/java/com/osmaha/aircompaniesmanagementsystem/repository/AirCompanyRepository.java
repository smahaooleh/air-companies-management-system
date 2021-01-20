package com.osmaha.aircompaniesmanagementsystem.repository;

import com.osmaha.aircompaniesmanagementsystem.domain.AirCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AirCompanyRepository extends JpaRepository<AirCompany, Long> {

    Optional<AirCompany> findById(Long id);

    Optional<AirCompany> findByName(String name);

}
