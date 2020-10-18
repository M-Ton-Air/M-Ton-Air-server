package com.polytech.mtonairserver.service.interfaces;

import com.polytech.mtonairserver.model.entities.StationEntity;

import java.io.IOException;
import java.util.List;

public interface IStationService
{
    List<StationEntity> getAllStationsName() throws IOException;
}
