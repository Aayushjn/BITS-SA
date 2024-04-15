package com.github.aayushjn.travelms.buses.repository;

import com.github.aayushjn.travelms.buses.model.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusRepository extends JpaRepository<Bus, Integer> {

    @Query("SELECT b FROM Bus b WHERE b.name = ?1")
    Optional<Bus> findByName(String name);
}
