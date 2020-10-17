package com.polytech.mtonairserver.controller;


import com.polytech.mtonairserver.model.entities.UserEntity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/stations")
public class StationController {

    @Value(value = "classpath:/data/stations.html")
    private Resource stations;

    private static String hostLink = "http://aqicn.org/city/";
    final String SPLITTER = "/";

    public static String getHostLink() {
        return hostLink;
    }

    public static void setHostLink(String hostLink) {
        StationController.hostLink = hostLink;
    }

    @RequestMapping(value = "/stationsName", method = RequestMethod.GET)
    public UserEntity getAllStationsName() throws IOException {

        File htmlFile = this.stations.getFile();
        Document document =  Jsoup.parse(htmlFile, "UTF-8");

        List<String> listHref = new ArrayList<>();
        List<String> listPays = new ArrayList<>();
        List<String> listRegion = new ArrayList<>();
        List<String> listAutre = new ArrayList<>();

        Elements newsHealines = document.select("a");
        for (Element headline : newsHealines) {
            String href = headline.attr("href");
            String stationName = href.replace(hostLink, "");
            listHref.add(stationName);
        }

        for (String lien : listHref) {
            if (StringUtils.countOccurrencesOf(lien, "/") == 3) {

                String geo[] = lien.split(SPLITTER);
                String pays = geo[0];
                String region = geo[1];
                String autre = geo[2];
                listPays.add(pays);
                listRegion.add(region);
                listAutre.add(autre);
            }
        }
        System.out.println();
        return null;
    }
}
