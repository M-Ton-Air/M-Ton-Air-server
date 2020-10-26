package com.polytech.mtonairserver.utils.io;

import com.polytech.mtonairserver.model.entities.StationEntity;

import java.util.List;
import java.util.stream.Collectors;

public class StationsParticularCaseHandler
{
    private List<StationEntity> stationEntities;

    public  StationsParticularCaseHandler(List<StationEntity> _stationEntities)
    {
        this.stationEntities = _stationEntities;
    }

    public List<StationEntity> executeAllCleaningMethods()
    {
        return this.stationEntities;
    }


    //todo : unit test
    /**
     * Georgia is a country, in Europe, but also a state in the USA.
     * There are only few stations in Georgia, Europe, which are located in the
     * Tbilissi town. So, the logic is simple, if the stationEntity URL contains "Tbilissi",
     * we consider it's in Georgia, in Europe.
     * Otherwise, it's in the USA.
     */
    private void cleanGeorgiaStationEntities()
    {
        final String georgiaCapital = "Tbilissi";
        final String usa = "United States";
        List<StationEntity> georgiaStationEntities = this.stationEntities.stream().filter(se -> se.getCountry().equals("Georgia")).collect(Collectors.toList());
        for(StationEntity se : georgiaStationEntities)
        {
            if(!se.getUrl().contains(georgiaCapital.toLowerCase()))
            {
                se.setCountry(usa);
            }
        }
    }

    private void 
}
