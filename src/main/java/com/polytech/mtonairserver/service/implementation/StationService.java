package com.polytech.mtonairserver.service.implementation;

import com.polytech.mtonairserver.model.entities.StationEntity;
import com.polytech.mtonairserver.model.entities.UserEntity;
import com.polytech.mtonairserver.repository.StationRepository;
import com.polytech.mtonairserver.service.interfaces.IStationService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// todo : documentation

/**
 * Service implementation for the station service.
 */
@Service
public class StationService implements IStationService
{
    private StationRepository stationRepository;

    @Autowired
    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Value(value = "classpath:/data/stations.html")
    private Resource stations;

    private static String hostLink = "http://aqicn.org/city/";
    final String SPLITTER = "/";

    public static String getHostLink() {
        return hostLink;
    }

    public static void setHostLink(String hostLink) {
        StationService.hostLink = hostLink;
    }

    public List<StationEntity> getAllStationsName() throws IOException {

        File htmlFile = this.stations.getFile();
        Document document =  Jsoup.parse(htmlFile, "UTF-8");

        int count = 0;

        List<String> listHref = new ArrayList<>();
        List<StationEntity> listStationEntity = new ArrayList<>();

        List<StationEntity> listCountryNull = new ArrayList<>();

        Elements newsHealines = document.select("a");
        for (Element headline : newsHealines) {
            String href = headline.attr("href");
            String stationName = href.replace(hostLink, "");
            String geo[] = stationName.split(SPLITTER);

            StationEntity stationEntity = new StationEntity();
            stationEntity.setIdStation(count);
            stationEntity.setUrl(href);
            stationEntity.setStationName(stationName);

            switch (geo.length) {
                case 0 :
                    break;
                case 1:
                    stationEntity.setCity(geo[0]);
                    break;

                case 2:
                    stationEntity.setCountry(geo[0]);
                    stationEntity.setCity(geo[1]);
                    break;

                case 3:
                    stationEntity.setCountry(geo[0]);
                    stationEntity.setRegion(geo[1]);
                    stationEntity.setCity(geo[2]);
                    break;

                case 4:
                    stationEntity.setCountry(geo[0]);
                    stationEntity.setRegion(geo[1]);
                    stationEntity.setCity(geo[2] + " " + geo[3]);
                    break;

                case 5 :
                    stationEntity.setCountry(geo[0]);
                    stationEntity.setRegion(geo[1]);
                    stationEntity.setCity(geo[2] + " " + geo[3] + " " + geo[4]);
                    break;


            }



            if (!href.equals("")  & !stationName.contains("http")) {
                listStationEntity.add(stationEntity);
                count++;
            }

            // for the test
            if(stationEntity.getCountry() == null & !href.equals("") &  !stationName.contains("http")) {
                listCountryNull.add(stationEntity);
            }

        }
        return listStationEntity;
    }

}
