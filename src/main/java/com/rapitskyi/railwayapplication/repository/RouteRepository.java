package com.rapitskyi.railwayapplication.repository;

import com.rapitskyi.railwayapplication.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends JpaRepository<Route, Integer> {
}