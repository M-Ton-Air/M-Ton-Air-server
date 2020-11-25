package com.polytech.mtonairserver.stationshandling.io;

import com.google.gson.*;
import com.opencsv.CSVReader;
import com.polytech.mtonairserver.customexceptions.datareader.NoProperLocationFoundException;
import com.polytech.mtonairserver.customexceptions.datareader.UnsupportedFindOperationOnLocationException;
import com.polytech.mtonairserver.customexceptions.requestaqicnexception.UnknownStationException;
import com.polytech.mtonairserver.model.entities.StationEntity;
import com.polytech.mtonairserver.model.external.CountryCityRegion;
import com.polytech.mtonairserver.service.implementation.StationService;
import com.polytech.mtonairserver.stationshandling.LocationType;
import com.polytech.mtonairserver.stationshandling.DataReaderParticularCaseHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.stream.Collectors;


/**
 * Class that handles all the reading operations with data files.
 */
@Component
public class StationsDataReader
{
    /**
     * The stations.html files. It contains all the aqicn stations
     * https://aqicn.org/city/all
     */
    private Resource stations;

    /**
     * A list of cities + their region and countries, from all around the world.
     */
    private Resource csvCountriesCities;

    /**
     * A list of all the japanese cities and their administrative region.
     */
    private Resource csvJapan;

    /**
     * A json file containing the longitude + latitude for every station contained in the stations.html file.
     *
     */
    private Resource jsonCoordinates;


    private final String UNKNOWN_ENDPOINT = "%2A";

    private final List<CountryCityRegion> countriesCitiesRegions;

    /**
     * Beware. The station entities of this stationsCoordinates list only contain the following fields :
     * - url
     * - longitude
     * - latitude
     *
     * All ofther fields are null.
     */
    private final List<StationEntity> stationsCoordinates;

    @Autowired
    public StationsDataReader(@Value(value = "classpath:/data/cities_in_japan_2019.csv") Resource _csvJapan,
                              @Value(value = "classpath:/data/countries_cities.csv")     Resource _csvCountriesCities,
                              @Value(value = "classpath:/data/stations.html")            Resource _stations,
                              @Value(value = "classpath:/data/stations_geo.json")        Resource _jsonCoordinates) throws IOException, ExecutionException, InterruptedException
    {
        this.csvJapan = _csvJapan;
        this.csvCountriesCities = _csvCountriesCities;
        this.stations = _stations;
        this.jsonCoordinates = _jsonCoordinates;
        this.countriesCitiesRegions = this.retrieveCountriesCities();
        this.stationsCoordinates = this.retrieveStationsCoordinates();
    }

    /**
     * Reads all the cities in the world and their associate countries, from csv data.
     * https://simplemaps.com/data/world-cities
     * @return a list of CountryCityRegion.
     * @see CountryCityRegion
     * @throws IOException file reading exception.
     */
    public List<CountryCityRegion> retrieveCountriesCities() throws IOException
    {
        List<CountryCityRegion> countryCities = new ArrayList<CountryCityRegion>();
        countryCities.addAll(this.retrieveWordlwideCountriesCities());
        countryCities.addAll(this.retrieveJapaneseCities());
        return countryCities;
    }

    private List<CountryCityRegion> retrieveJapaneseCities() throws IOException
    {
        List<CountryCityRegion> countryCities = new ArrayList<CountryCityRegion>();

        String[] japaneseCityRegionRecord;
        CSVReader japanReader = new CSVReader(new FileReader(this.csvJapan.getFile()), ',', '"', 1);
        while ( (japaneseCityRegionRecord = japanReader.readNext()) != null)
        {
            String line = japaneseCityRegionRecord[0];
            String[] japaneseCityAndRegion = line.split(",");
            // see the cities_in_japan_2019 csv to understand this behavior.
            if(japaneseCityAndRegion.length > 1)
            {
                CountryCityRegion ccr = new CountryCityRegion("Japan", japaneseCityAndRegion[0], "JP", "JAP", japaneseCityAndRegion[1]);
                countryCities.add(ccr);
            }
            // ignore the other cities (if we don't have the region).
        }
        return countryCities;
    }

    private List<CountryCityRegion> retrieveWordlwideCountriesCities() throws IOException
    {
        List<CountryCityRegion> countryCities = new ArrayList<CountryCityRegion>();
        CSVReader reader = new CSVReader(new FileReader(this.csvCountriesCities.getFile()), ',', '"', 1);
        String[] line;
        while( (line = reader.readNext()) != null)
        {
            String[] lineValues= line[0].replace("\"", "").split(",");
            // [4] = country, [1] = city, [6] = ISO3 country code (3chars code), [7] region / administration_name
            CountryCityRegion countryCityRegion = new CountryCityRegion(lineValues[4], lineValues[1], lineValues[5], lineValues[6]  ,lineValues[7]);
            countryCities.add(countryCityRegion);
        }
        return countryCities;
    }

    public List<StationEntity> retrieveStationsCoordinates() throws IOException, ExecutionException, InterruptedException
    {
        List<StationEntity> allStationCoordinates = Collections.synchronizedList(new ArrayList<>());

        JsonParser parser = new JsonParser();
        Reader reader = new FileReader(this.jsonCoordinates.getFile());
        JsonArray allCoordinates = (JsonArray) parser.parse(reader);

        ExecutorService executor = Executors.newWorkStealingPool();
        List<Future<?>> futures = new ArrayList<Future<?>>();

        for(JsonElement jElement : allCoordinates)
        {
            // todo : can this be factorized ? cause it is used many times in the code (threading)
            futures.add
            (
                executor.submit( () ->
                {
                    StationEntity currentStationCoordinates = new StationEntity();
                    JsonObject currentObject = jElement.getAsJsonObject();
                    // first element is longitude, second element is latitude.
                    JsonArray longitudeLatitude = currentObject.getAsJsonArray("geo");
                    String url = currentObject.get("url").getAsString();
                    String endpoint = StationService.getEndpointFromUrl(url);

                    currentStationCoordinates.setLongitude(longitudeLatitude.get(0).getAsDouble());
                    currentStationCoordinates.setLatitude(longitudeLatitude.get(1).getAsDouble());
                    currentStationCoordinates.setUrl(endpoint);

                    allStationCoordinates.add(currentStationCoordinates);
                })
            );
        }

        //blocking : waits for all threads to be completed
        for(Future<?> future : futures)
        {
            future.get();
        }

        boolean allDone = true;
        for(Future<?> future : futures)
        {
            allDone &= future.isDone();
        }
        executor.shutdown();

        System.out.println();
        return allStationCoordinates;
    }


    /**
     * Retrieves all the station names by making joins with the aqicn url links (a href)
     * and the csv file (csvCountriesCities).
     * @throws IOException in case of file reading exception.
     */
    public List<StationEntity> initializeAllStationsFromResourcesFiles() throws IOException, NoProperLocationFoundException, UnsupportedFindOperationOnLocationException, ExecutionException, InterruptedException
    {
        List<StationEntity> initializedStations = Collections.synchronizedList(new ArrayList<>());
        File htmlFile = this.stations.getFile();
        Document document = Jsoup.parse(htmlFile, "UTF-8");
        //only keeping a[href] links cause they also are "a" items that looks like this : <a id="United States"></a>
        Elements htmlLinks = document.select("a[href]");
        // handling particular cases : sometimes the url is "https://aqicn.org?city=%2A". We remove those.
        htmlLinks.removeAll(htmlLinks.stream().filter(element -> element.toString().contains(this.UNKNOWN_ENDPOINT)).collect(Collectors.toList()));
        // removes some unused links / wrong links.
        htmlLinks.removeAll(DataReaderParticularCaseHandler.removeParticularCases(htmlLinks));


        // todo : can this be factorized ? cause it is used many times in the code (threading)
        ExecutorService executor = Executors.newWorkStealingPool();

        List<Future<?>> futures = new ArrayList<Future<?>>();
        for(Element htmlHrefElement : htmlLinks)
        {
            futures.add
            (
                executor.submit( () ->
                {
                    try
                    {
                        StationEntity newSta = this.initializeStation(htmlHrefElement);
                        this.initializeStationCoordinates(newSta);
                        if(newSta.getLatitude() != null)
                        {
                            initializedStations.add(newSta);
                        }
                    }
                    catch (UnsupportedFindOperationOnLocationException | NoProperLocationFoundException ex)
                    {
                        ex.printStackTrace();
                    }
                    catch(UnknownStationException e)
                    {
                        // do nothing. If we don't get coordinates for a given station, it's because that
                        // station is not used anymore. Then, its longitude and latitude will be null,
                        // and it will not be added to the initializedStations list.
                    }
                })
            );
        }


        //blocking : waits for all threads to be completed
        for(Future<?> future : futures)
        {
            future.get();
        }

        boolean allDone = true;
        for(Future<?> future : futures)
        {
            allDone &= future.isDone();
        }
        executor.shutdown();

        //initializeStation(initializedStations, htmlLinks);
        DataReaderParticularCaseHandler cleaner = new DataReaderParticularCaseHandler(initializedStations);
        cleaner.executeAllCleaningMethods();
        return initializedStations;
    }

    private StationEntity initializeStation(Element html_a_HrefLink) throws UnsupportedFindOperationOnLocationException, NoProperLocationFoundException
    {
        StationEntity station = new StationEntity();

       // only retrieves the href links.
        String url = html_a_HrefLink.attr("href");

        // gets the endpoint (url) without the https://aqicn.org/city stuff.
        String endpoint = StationService.getEndpointFromUrl(url);

        // locations ared separated by slashes in the URL.
        String[] locations = endpoint.split("/");
        // a location can be, in that approximate specific order :
        // A country -> a region -> a city -> a specific place (Street, district, etc.)

        // We will analyze the first element of the locations array
        // And we will retrieve the corresponding countries/region/cities thanks to the csv file
        // And we'll store the rest as subdivisions.

        // country is always (if present) at the first place.
        // otherwise, it can be sometimes be a region or a city.
        final String potentialCountry = locations[0];
        // we'll get that potential country and check if it is really one thanks to the countries_cities.csv file.
        Optional<CountryCityRegion> realCountryObject = this.findByCountryRegionCity(potentialCountry, LocationType.COUNTRY);

        station.setUrl(endpoint);
        // if we got a country at the first place of the endpoint, we initialize it
        if(realCountryObject.isPresent())
        {
            CountryCityRegion realCountry = realCountryObject.get();
            station.setCountry(realCountry.getCountry());
            station.setIso2(realCountry.getIso2());
        }
        // else : it may be a region or a city
        else
        {
            //The first place can also be a region,
            //so we'll get that potentialRegion and see if it is really a region thanks to the csv file.
            final String potentialRegion = locations[0];
            Optional<CountryCityRegion> realRegionObject = this.findByCountryRegionCity(potentialRegion, LocationType.REGION);

            // if it is a region, then we got the country and the first subdivision
            if(realRegionObject.isPresent())
            {
                CountryCityRegion countryCityRegion = realRegionObject.get();
                // but sometimes, the region name is a city name. So in that case, we initialize the stationName instead.
                // that case occurs when the length is 1 --> we directly have the city name in the url.
                if(locations.length == 1)
                {
                    station.setStationName(countryCityRegion.getRegion());
                    station.setCountry(countryCityRegion.getCountry());
                    station.setIso2(countryCityRegion.getIso2());
                }
                // else, it's very prolly a subdivision / region.
                else
                {
                    station.setSubdivision1(countryCityRegion.getRegion());
                    station.setCountry(countryCityRegion.getCountry());
                    station.setIso2(countryCityRegion.getIso2());
                }

            }
            // When the first string is a city, then we have nothing left to do. We got the city,
            // subdivision 1 and country
            else
            {
                final String potentialCity = locations[0];
                //and if it is a city, we make another filter.
                Optional<CountryCityRegion> realCityObject = this.findByCountryRegionCity(potentialCity, LocationType.CITY);
                if(realCityObject.isPresent())
                {
                    CountryCityRegion countryCityRegion = realCityObject.get();
                    station.setStationName(countryCityRegion.getCity());
                    station.setSubdivision1(countryCityRegion.getRegion());
                    station.setCountry(countryCityRegion.getCountry());
                    station.setIso2(countryCityRegion.getIso2());
                }
                // as of 25/10/2020, this exception isn't raised and will not be unless the resources
                // do change (stations.html, countries_cities.csv, cities_in_japan_2019.csv).
                else
                {
                    final String error = "Could not found a suitable location. " +
                            "Please check the unknown location and fix the error.";
                    throw new NoProperLocationFoundException(error, StationsDataReader.class, potentialCity);
                }
            }
        }
        // SECOND STEP : INITIALIZE THE SUBDIVISIONS / STATION NAME
        this.initializeSubdivisionsAndStationsNames(locations, station);
        return station;
    }


    private void initializeSubdivisionsAndStationsNames(String[] locations, StationEntity station)
    {
        for(int i = 1; i < locations.length; i++)
        {
            // if we're in presence of the last element,
            if(i == locations.length - 1)
            {
                // then we initialize the station name
                if(station.getStationName() == null)
                {
                    station.setStationName(locations[i]);
                }
            }
            else
            {
                //otherwise we initialize the subdivisions.
                switch(i)
                {
                    case 1:
                        if(station.getSubdivision1() == null)
                        {
                            station.setSubdivision1(locations[i]);
                        }
                        break;
                    case 2:
                        station.setSubdivision2(locations[i]);
                        break;
                    case 3:
                        station.setSubdivision3(locations[i]);
                        break;
                }
            }
        }
    }



    /**
     * Finds a Country/Region/City that has a given name (countryRegionCityName). That is way much fast this way than
     * with java8 stream queries like so :
     * countriesCitiesRegions.stream().filter(ccr -> ccr.getCity().toLowerCase().equals(potentialCity.toLowerCase())).findFirst();
     * but that is less easy to maintain. We chose to have a faster algorithm, time consumption was insane with the streams
     * (like 1+min for 3 calls).
     * @param countryRegionCityName The name of the country / region / city that has to be find in the list of CountriesCitiesRegions.
     * @param locationType the type of location : Country, Region or City that was given in parameter.
     * @return an Optional containing the found value or nothing.
     * @throws UnsupportedFindOperationOnLocationException if the location type is o not handled for any reason.
     */
    private Optional<CountryCityRegion> findByCountryRegionCity(String countryRegionCityName, LocationType locationType) throws UnsupportedFindOperationOnLocationException
    {
        for(CountryCityRegion ccr : this.countriesCitiesRegions)
        {
            switch(locationType)
            {
                case COUNTRY:
                    // sometimes, the country is set as an ISO code in the aqicn urls (USA for United States). So we'll check both
                    // the country and the iso3 fields.
                    if (this.isSameCountry(ccr, countryRegionCityName))
                    {
                        return Optional.of(ccr);
                    }
                    break;
                case REGION:
                    // we use equals, its not a problem if it is not an exact match (cause regions that contains many words
                    // may not match), because only 3k stations are concerned by this problem (not specifying the country) :
                    // these are particular cases.
                    if(ccr.getRegion().toLowerCase().equals(countryRegionCityName))
                    {
                        return Optional.of(ccr);
                    }
                    break;
                case CITY:
                    if(ccr.getCity().toLowerCase().equals(countryRegionCityName))
                    {
                        return Optional.of(ccr);
                    }
                    break;
                default:
                    throw new UnsupportedFindOperationOnLocationException("The given LocationType is not handled.", StationsDataReader.class);
            }
        }
        return Optional.empty();
    }

    private boolean isSameCountry(CountryCityRegion current, String expectedCountryRegionCityName)
    {
        return current.getCountry().toLowerCase().equals(expectedCountryRegionCityName.toLowerCase())
                || current.getIso3().toLowerCase().   equals(expectedCountryRegionCityName.toLowerCase());
    }

    private StationEntity initializeStationCoordinates(StationEntity station) throws UnknownStationException
    {
        for(StationEntity coordinates : this.stationsCoordinates)
        {
            if(coordinates.getUrl().equals(station.getUrl()))
            {
                station.setLongitude(coordinates.getLongitude());
                station.setLatitude(coordinates.getLatitude());
                return station;
            }
        }
        throw new UnknownStationException("Station coordinates could not be found", StationsDataReader.class);
    }
}
