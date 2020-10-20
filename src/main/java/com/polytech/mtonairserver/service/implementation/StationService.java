package com.polytech.mtonairserver.service.implementation;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;
import com.polytech.mtonairserver.model.entities.StationEntity;
import com.polytech.mtonairserver.model.entities.UserEntity;
import com.polytech.mtonairserver.model.external.CountryCity;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// todo : documentation

/**
 * Service implementation for the station service.
 */
@Service
public class StationService implements IStationService {
    private StationRepository stationRepository;

    @Autowired
    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Value(value = "classpath:/data/stations.html")
    private Resource stations;


    @Value(value = "classpath:/data/countries_cities.csv")
    private Resource csvCountriesCities;

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
        Document document = Jsoup.parse(htmlFile, "UTF-8");

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
                case 0:
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

                case 5:
                    stationEntity.setCountry(geo[0]);
                    stationEntity.setRegion(geo[1]);
                    stationEntity.setCity(geo[2] + " " + geo[3] + " " + geo[4]);
                    break;


            }


            if (!href.equals("") & !stationName.contains("http")) {
                listStationEntity.add(stationEntity);
                count++;
            }

            // for the test
            if (stationEntity.getCountry() == null & !href.equals("") & !stationName.contains("http")) {
                listCountryNull.add(stationEntity);
            }


        }
        return listStationEntity;
    }

    //todo : réfléchir à comment rendre ça + propre
    public List<CountryCity> getCsvCountriesCities() throws IOException {

      /*  FileReader fileReader = new FileReader(this.csvCountriesCities.getFile());
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        //CSVParser csvParser = new CSVParser(',', '"');
        List<String[]> listRes = new ArrayList<String[]>();
        List<CountryCity> countriesCitiesList = new ArrayList<>();

        String line = "";
        //remove the first line
        bufferedReader.readLine();
        while ((line = bufferedReader.readLine()) != null) {

            line.replaceFirst("\"", "");
            String[] res = line.split(",");
            listRes.add(res);

            CountryCity countryCity = new CountryCity(res[4], res[0], res [6]);
            countriesCitiesList.add(countryCity);
        }

        return countriesCitiesList; */


//listStationEntity.stream().anyMatch(s -> s.getUrl().toLowerCase().contains("rome"))
        //listStationEntity.stream().filter(s -> s.getUrl().toLowerCase().contains("rome")).toArray()

        List<String> records = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(this.csvCountriesCities.getFile()))) {
            String[] values;
            csvReader.readNext();
            while ((values = csvReader.readNext()) != null) {
                records.add(Arrays.toString(values));
                String[] res = Arrays.toString(values).split(",");
            }
            System.out.println();
        }
        return null;

    }

}