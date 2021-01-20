package com.osmaha.aircompaniesmanagementsystem.repository.util;

import com.osmaha.aircompaniesmanagementsystem.domain.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    Optional<Country> findByName(String name);
}
