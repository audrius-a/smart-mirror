package com.development.audrius.smartmirror;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StreamCorruptedException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Audrius on 17/10/2017.
 */

public class MetService {

    private String apiKey;

    public MetService(String apiKey) {
        this.apiKey = apiKey;
    }

    public Forecast GetWeather() {
        HttpClient client = new HttpClient("http://datapoint.metoffice.gov.uk/public/data/");
        String result = client.GetString("val/wxfcs/all/json/352688?res=daily&key=" + apiKey);
        return Forecast.ParseJson(result);
    }

}

