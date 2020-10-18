package com.polytech.mtonairserver.repository;

import com.polytech.mtonairserver.model.entities.StationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StationRepository extends JpaRepository<StationEntity, Integer>
{
}
