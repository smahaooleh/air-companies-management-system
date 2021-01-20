package com.osmaha.aircompaniesmanagementsystem.repository.util;

import com.osmaha.aircompaniesmanagementsystem.domain.CompanyType;
import com.osmaha.aircompaniesmanagementsystem.domain.enums.CompanyTypeE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyTypeRepository extends JpaRepository<CompanyType, Long> {

    Optional<CompanyType> findByName(CompanyTypeE companyTypeE);
}
