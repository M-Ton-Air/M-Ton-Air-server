package com.polytech.mtonairserver.service.interfaces;

import com.polytech.mtonairserver.model.entities.DailyAqicnDataEntity;

import java.util.List;

public interface IDailyAqicnDataService {

    List<DailyAqicnDataEntity> findAll();
}
