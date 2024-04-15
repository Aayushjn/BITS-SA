package com.github.aayushjn.travelms.packages.repository;

import com.github.aayushjn.travelms.packages.model.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PackageRepository extends JpaRepository<Package, Integer> {
    @Query("SELECT p FROM Package p WHERE p.name = ?1")
    Optional<Package> findByName(String name);
}
