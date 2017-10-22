package com.development.audrius.smartmirror;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Audrius on 18/10/2017.
 */

public class Forecast {
    private ArrayList<Day> Days;

    public ArrayList<Day> getDays() {
        return Days;
    }

    public static Forecast ParseJson(String jsonString) {
        Forecast forecast = new Forecast();
        forecast.Days = new ArrayList<Day>();

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject siteRep = jsonObject.getJSONObject("SiteRep");
            JSONObject dv = siteRep.getJSONObject("DV");
            JSONObject location = dv.getJSONObject("Location");
            JSONArray periods = location.getJSONArray("Period");
            for (int i = 0; i < periods.length(); i++) {
                JSONObject period = periods.getJSONObject(i);
                JSONArray reps = period.getJSONArray("Rep");

                Day day = new Day();

                JSONObject dayRep = reps.getJSONObject(0);
                day.DayTemperature = dayRep.getString("Dm");

                JSONObject nightRep = reps.getJSONObject(1);
                day.NightTemperature = nightRep.getString("Nm");

                forecast.getDays().add(day);
            }
        } catch (final JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
        }

        return forecast;
    }
}
