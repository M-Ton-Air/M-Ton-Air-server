package com.polytech.mtonairserver.service.implementation;

import com.polytech.mtonairserver.model.entities.DailyAqicnDataEntity;
import com.polytech.mtonairserver.repository.DailyAqicnDataRepository;
import com.polytech.mtonairserver.service.interfaces.IDailyAqicnDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for the daily AQICN data.
 */
@Service
public class DailyAqicnDataService implements IDailyAqicnDataService {

    private DailyAqicnDataRepository dailyAqicnDataRepository;

    @Autowired
    public DailyAqicnDataService(DailyAqicnDataRepository dailyAqicnDataRepository) {
        this.dailyAqicnDataRepository = dailyAqicnDataRepository;
    }

    /**
     * Gets all the AQICN data
     * @return all the AQICN data
     */
    @Override
    public List<DailyAqicnDataEntity> findAll() {
        List<DailyAqicnDataEntity> dailyAqicnDataList = this.dailyAqicnDataRepository.findAll();
        return dailyAqicnDataList;
    }
}
