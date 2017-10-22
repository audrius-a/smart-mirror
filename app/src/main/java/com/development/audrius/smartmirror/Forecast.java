package com.development.audrius.smartmirror;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.StringTokenizer;

import static android.content.ContentValues.TAG;

/**
 * Created by Audrius on 18/10/2017.
 */

public class Forecast {
    public ArrayList<Day> Days;

    public static Forecast ParseJson(String jsonString) {
        Forecast forecast = new Forecast();
        forecast.Days = new ArrayList<Day>();
        Day dayOne = new Day();
        dayOne.DayTemperature = "30";

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject siteRep = jsonObject.getJSONObject("SiteRep");
            JSONObject dayValues = siteRep.getJSONObject("DV");
            JSONObject location = dayValues.getJSONObject("Location");
            JSONArray periods = location.getJSONArray("Period");
            for (int i = 0; i < periods.length(); i++) {
                JSONObject period = periods.getJSONObject(i);
                JSONArray reps = period.getJSONArray("Rep");
                for (int j = 0; j < reps.length(); j++) {
                    JSONObject rep = reps.getJSONObject(j);

                    //Day day = new Day();
                    dayOne.DayTemperature = rep.getString("D");
                }
            }

            String type = dayValues.getString("type");
            //TODO: implement parsing
        } catch (final JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
            dayOne.DayTemperature = e.getMessage();
        }

        forecast.Days.add(dayOne);
        return forecast;
    }
}
