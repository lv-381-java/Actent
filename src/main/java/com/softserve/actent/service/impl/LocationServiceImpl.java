package com.softserve.actent.service.impl;

import com.softserve.actent.constant.ExceptionMessages;
import com.softserve.actent.constant.UrlConstants;
import com.softserve.actent.exceptions.DataNotFoundException;
import com.softserve.actent.exceptions.codes.ExceptionCode;
import com.softserve.actent.model.entity.Location;
import com.softserve.actent.repository.LocationRepository;
import com.softserve.actent.service.LocationService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {
    private static final String INPUT = "?input=";
    private static final String ADDRESS = "?address=";
    private static final String KEY = "&key=";
    private static final String API_KEY = "AIzaSyCKjqC6ENzXDMbAOIkpbU24N1ULYFEhA9o";

    private final LocationRepository locationRepository;

    @Autowired
    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Transactional
    @Override
    public Location add(String address) {
        Location location = new Location();
        location.setAddress(address);
        StringBuilder jsonResults = new StringBuilder();
        getUrl(createGeocodeApiUrl(address), jsonResults);
        try {
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("results");
            location.setLatitude(predsJsonArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
            location.setLongtitude(predsJsonArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng"));

        } catch (JSONException e) {
            System.out.println("Error processing JSON results");
        }
        return locationRepository.save(location);
    }

    @Override
    public Location get(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(
                        ExceptionMessages.LOCATION_NOT_FOUND,
                        ExceptionCode.NOT_FOUND));
    }

    @Override
    public Location getByAddress(String address) {
        if (locationRepository.existsByAddress(address)) {
            return locationRepository.findByAddress(address);
        } else {
            throw new DataNotFoundException(
                    ExceptionMessages.LOCATION_ADDRESS_NOT_FOUND,
                    ExceptionCode.NOT_FOUND);
        }

    }


    @Override
    public List<Location> getAllAutocomplete(String address) {
        ArrayList<Location> locations = null;
        StringBuilder jsonResults = new StringBuilder();
        getUrl(createPlaceApiUrl(address), jsonResults);

        try {
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");
            locations = new ArrayList(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                Location location = new Location();
                location.setAddress(predsJsonArray.getJSONObject(i).getString("description"));
                locations.add(location);
            }
        } catch (JSONException e) {
            System.out.println("Error processing JSON results");
        }

        return locations;
    }

    private String createPlaceApiUrl(String address) {
        StringBuilder placeUrl = new StringBuilder(UrlConstants.PLACES_API);
        placeUrl.append(UrlConstants.TYPE_AUTOCOMPLETE);
        placeUrl.append(UrlConstants.OUT_JSON);
        try {
            placeUrl.append(INPUT + URLEncoder.encode(address, "utf8"));
        } catch (UnsupportedEncodingException e) {
            System.out.println("UnsupportedEncodingException");
        }
        placeUrl.append(KEY + API_KEY);
        return placeUrl.toString();
    }

    private String createGeocodeApiUrl(String address) {
        StringBuilder geocodeUrl = new StringBuilder(UrlConstants.GEOCODE_API);
        geocodeUrl.append(UrlConstants.OUT_JSON);
        try {
            geocodeUrl.append(ADDRESS + URLEncoder.encode(address, "utf8"));
        } catch (UnsupportedEncodingException e) {
            System.out.println("UnsupportedEncodingException");
        }
        geocodeUrl.append(KEY + API_KEY);
        return geocodeUrl.toString();
    }

    private void getUrl(String address, StringBuilder jsonResults) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(address);
            System.out.println(url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            System.out.println("Error processing API URL");
        } catch (IOException e) {
            System.out.println("Error connecting to API");
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

}
