package com.polytech.mtonairserver.stationshandling;

import com.polytech.mtonairserver.model.entities.StationEntity;
import com.polytech.mtonairserver.service.implementation.StationService;
import com.polytech.mtonairserver.utils.string.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class that, as its name tells, handle some specific cases that are not handled by the data reader class itself
 */
public class DataReaderParticularCaseHandler
{
    private List<StationEntity> stationEntities;

    /**
     * A list of some particular cases.
     */
    private static final String[] PARTICULAR_CASES = new String[]
    {
        "newyork",
        "gtradio-sonda",
        "curacao",
        "sorel",
        "japan",
        "koto",
        "ota",
        "turkey"
    };


    public DataReaderParticularCaseHandler(List<StationEntity> _stationEntities)
    {
        this.stationEntities = _stationEntities;
    }

    /**
     * Method that executes all the StationEntities cleaning methods : handles some errors, miss spellings, etc.
     */
    public void executeAllCleaningMethods()
    {
        this.cleanSpecificSpellings();
        this.cleanSubdivisionsAndStationsNames();
    }


    /**
     * Some Countries / regions / cities are wrongly spelled in the aqicn urls / or contains dashes cause there
     * are countries/regions/cities with composed words.
     * As a result, to proceed to good joins in the DataReader, the countries/regions/cities have been modified
     * to respect the aqicn spelling, which is either wrong / contains dashes.
     *
     * This method reverts back those changes, that were made to the csv file.
     * The concerned words :
     * cleanup some station names :
     *  - Los Angeles (instead of losangeles),
     *  - Czechia ( instead of czechrepublic),
     *  - El Salvador (instead of el-salavor)
     *  - Henan instead of (cnhenan) (china district/region)
     *  - Afghanistan instead of afganistan
     *  - United Arab Emirates instead of uae
     *  - Hong Kong instead of hongkong
     *  - Sankt Gallen instead of saint-gallen
     *  - Massachusetts instead of Massachusett
     */
    private void cleanSpecificSpellings()
    {
        final String georgiaCapital = "Tbilisi";
        final String usa = "United States";

        for(StationEntity station : this.stationEntities)
        {
            switch(station.getCountry().toLowerCase())
            {
                case "afganistan":
                    station.setCountry("Afghanistan");
                    break;
                case "el-salavor":
                    station.setCountry("El Salvador");
                    break;
                case "uae":
                    station.setCountry("United Arab Emirates");
                    break;
                case  "hongkong":
                    station.setCountry("Hong Kong");
                    break;
                case "korea":
                    station.setCountry("South Korea");
                    break;
                case "ivory-coast":
                    station.setCountry("Côte D'Ivoire");
                    break;
                case "macau":
                    station.setCountry("Macao");
                    break;
                case "maynmar":
                    station.setCountry("Myanmar");
                    break;
                case "netherland":
                    station.setCountry("Netherlands");
                    break;
                case "czechrepublic":
                    station.setCountry("Czech Republic");
                    break;
                case "bosnia-herzegovina":
                    station.setCountry("Bosnia And Herzegovina");
                    break;
                /*
                 *      * Georgia is a country, in Europe, but also a state in the USA.
                 *      * There are only few stations in Georgia, Europe, which are located in the
                 *      * Tbilissi town. So, the logic is simple, if the stationEntity URL contains "Tbilissi",
                 *      * we consider it's in Georgia, in Europe.
                 *      * Otherwise, it's in the USA.
                 */
                case "georgia":
                    if(!station.getUrl().toLowerCase().contains(georgiaCapital.toLowerCase()))
                    {
                        station.setCountry(usa);
                        station.setIso2("US");
                    }
                    break;
            }

            if(station.getSubdivision1() != null)
            {
                switch(station.getSubdivision1().toLowerCase())
                {
                    case "cnhenan":
                        station.setSubdivision1("Henan");
                        break;
                    case "massachusett":
                        station.setSubdivision1("Massachusetts");
                        break;
                }
            }

            if(station.getStationName() != null)
            {
                switch(station.getStationName().toLowerCase())
                {
                    case "tel-aviv":
                        station.setStationName("Tel Aviv Yafo");
                        break;
                    case "saint-gallen":
                        station.setStationName("Sankt Gallen");
                        break;
                    case "losangeles":
                        station.setStationName("Los Angeles");
                        break;

                }
            }

            station.setIso2(station.getIso2().trim());
            station.setCountry(station.getCountry().trim());
        }
    }

    /**
     * As we retrieve stations names and subdivions names from
     * URLs, they contains dashes ("-") and are probably lower case.
     * We want to display these names in a user friendly way (With an uppercase first letter and without dashes,
     * even if they were meant to be in the name, most of the time they are not.)
     * Examples :
     * Tel Aviv-Yafo instead of tel-aviv (city)
     *      - Pardes Hanna Karkur instead of pardes-hanna-karkur (city)
     *      - Sri Lanka instead of sri-lanka
     *      - Puerto rico (puerto-rico),
     *      - Kuala Lumpur instead of kuala-lumpur (city in malaysia)
     *      - British Columbia (instead of british-comlumbia, region in canada)
     *      - United Kingdom, Trinidad And Tobago, Ivory Coast, Saudi Arabia
     *      - South Africa,
     *      - New Zealand,
     *      - New Caledonia,
     *      - Bosnia And Herzegovina
     *      - and many more.
     */
    private void cleanSubdivisionsAndStationsNames()
    {
        for(StationEntity se : this.stationEntities)
        {
            if(se.getStationName() == null)
            {
                se.setStationName(se.getCountry());
            }

            if(se.getCountry() != null)
            {
                se.setCountry(StringUtils.upperRemoveDashAndSlashes(se.getCountry()));
            }

            if(se.getSubdivision1() != null)
            {
                se.setSubdivision1(StringUtils.upperRemoveDashAndSlashes(se.getSubdivision1()));
            }

            if(se.getSubdivision2() != null)
            {
                se.setSubdivision2(StringUtils.upperRemoveDashAndSlashes(se.getSubdivision2()));
            }

            if(se.getSubdivision3() != null)
            {
                se.setSubdivision3(StringUtils.upperRemoveDashAndSlashes(se.getSubdivision3()));
            }

            if(se.getStationName() != null)
            {
                se.setStationName(StringUtils.upperRemoveDashAndSlashes(se.getStationName()));
            }
        }
    }



    /**
     * Gets all the elements that has to be removed in some html href links.
     * @param links the links in which we want to get elements to remove.
     * @return the elements to delete
     */
    public static List<Element> removeParticularCases(Elements links)
    {
        List<Element> elementsToRemove = new ArrayList<Element>();
        for(Element e : links)
        {
            String url = e.attr("href");
            String endpoint = url.toString().replace(StationService.getHostLinkRealTimeAQI(), "");
            String potentialParticularCase = endpoint.split("/")[0];
            for(String particularCase : DataReaderParticularCaseHandler.PARTICULAR_CASES)
            {
                if(potentialParticularCase.toLowerCase().equals(particularCase.toLowerCase()))
                {
                    elementsToRemove.add(e);
                }
            }
            if(endpoint.contains("?city="))
            {
                // removes the following urls :
                // url='http://aqicn.org/?city=Japan/%E9%AB%98%E5%B2%A1%E9%83%A1%E6%AA%AE%E5%8E%9F%E7%94%BA/%E5%9B%BD%E8%A8%AD%E6%AA%AE%E5%8E%9F%E9%85%B8%E6%80%A7%E9%9B%A8'
                // url='http://aqicn.org/?city=Turkey/Eski%C5%9Fehir/Odunpazar%C4%B1'
                elementsToRemove.add(e);
            }
        }
        return elementsToRemove;
    }
}
