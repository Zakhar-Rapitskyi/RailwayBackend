package com.rapitskyi.railwayapplication.repository;

import com.rapitskyi.railwayapplication.entity.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainRepository extends JpaRepository<Train, Integer> {
}