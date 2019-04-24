package com.softserve.actent.service.impl;

import com.softserve.actent.constant.ExceptionMessages;
import com.softserve.actent.constant.UrlConstants;
import com.softserve.actent.exceptions.DataNotFoundException;
import com.softserve.actent.exceptions.codes.ExceptionCode;
import com.softserve.actent.model.entity.Location;
import com.softserve.actent.repository.LocationRepository;
import com.softserve.actent.service.LocationService;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class LocationServiceImpl implements LocationService {
    private static final String INPUT = "?input=";
    private static final String ADDRESS = "?address=";
    private static final String RESULT = "results";
    private static final String GEOMETRY = "geometry";
    private static final String LOCATION = "location";
    private static final String LAT = "lat";
    private static final String LNG = "lng";
    private static final String PREDICTIONS = "predictions";
    private static final String DESCRIPTION = "description";

    private final LocationRepository locationRepository;

    @Autowired
    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public Location get(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.LOCATION_NOT_FOUND, ExceptionCode.NOT_FOUND));
    }

    @Override
    public Location getByAddress(String address) {
        if (!locationRepository.existsByAddress(address)) {
            return addNew(address);
        } else {
            return locationRepository.findByAddress(address);
        }
    }

    @Override
    public Location getByCoordinates(String address) {
        return locationRepository.findByAddress(address);
    }

    @Override
    public List<Location> getAllAutocomplete(String address) {
        ArrayList<Location> locations = null;
        StringBuilder jsonResults = new StringBuilder();
        getUrl(createPlaceApiUrl(address), jsonResults);

        try {
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray(PREDICTIONS);
            locations = new ArrayList(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                Location location = new Location();
                location.setAddress(predsJsonArray.getJSONObject(i).getString(DESCRIPTION));
                locations.add(location);
            }
        } catch (JSONException e) {
            log.error(ExceptionMessages.JSON_ERROR);
        }
        return locations;
    }

    private Location addNew(String address) {
        Location location = new Location();
        location.setAddress(address);
        StringBuilder jsonResults = new StringBuilder();
        getUrl(createGeocodeApiUrl(address), jsonResults);

        try {
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray(RESULT);
            location.setLatitude(predsJsonArray.getJSONObject(0).getJSONObject(GEOMETRY).getJSONObject(LOCATION).getDouble(LAT));
            location.setLongtitude(predsJsonArray.getJSONObject(0).getJSONObject(GEOMETRY).getJSONObject(LOCATION).getDouble(LNG));
        } catch (JSONException e) {
            log.error(ExceptionMessages.JSON_ERROR);
        }
        return locationRepository.save(location);

    }

    private String createPlaceApiUrl(String address) {
        StringBuilder placeUrl = new StringBuilder(UrlConstants.PLACES_API);
        placeUrl.append(UrlConstants.TYPE_AUTOCOMPLETE);
        placeUrl.append(UrlConstants.OUT_JSON);

        try {
            placeUrl.append(INPUT + URLEncoder.encode(address, "utf8"));
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
        }
        placeUrl.append(UrlConstants.API_KEY);
        return placeUrl.toString();
    }

    private String createGeocodeApiUrl(String address) {
        StringBuilder geocodeUrl = new StringBuilder(UrlConstants.GEOCODE_API);
        geocodeUrl.append(UrlConstants.OUT_JSON);

        try {
            geocodeUrl.append(ADDRESS + URLEncoder.encode(address, "utf8"));
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
        }
        geocodeUrl.append(UrlConstants.API_KEY);
        return geocodeUrl.toString();
    }

    private void getUrl(String address, StringBuilder jsonResults) {
        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(address);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream());

            int read;
            char[] buff = new char[1024];
            while ((read = inputStreamReader.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            log.error(ExceptionMessages.API_URL_ERROR);
        } catch (IOException e) {
            log.error(ExceptionMessages.CONNECTION_ERROR);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

}
