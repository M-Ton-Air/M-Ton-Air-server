package com.polytech.mtonairserver.repository;

import com.polytech.mtonairserver.model.entities.MeasureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasureRepository extends JpaRepository<MeasureEntity, Integer> {
}
