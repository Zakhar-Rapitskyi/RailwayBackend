package com.rapitskyi.railwayapplication.repository;

import com.rapitskyi.railwayapplication.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StationRepository extends JpaRepository<Station, Integer> {
    Optional<Station> findByName(String name);
}