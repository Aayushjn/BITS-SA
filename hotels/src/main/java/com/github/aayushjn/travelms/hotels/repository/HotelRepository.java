package com.github.aayushjn.travelms.hotels.repository;

import com.github.aayushjn.travelms.hotels.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Integer> {

    @Query("SELECT h FROM Hotel h WHERE h.name = ?1")
    Optional<Hotel> findByName(String name);
}
