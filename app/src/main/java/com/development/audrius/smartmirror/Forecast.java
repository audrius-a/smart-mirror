package com.development.audrius.smartmirror;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

                day.Name = GetDayName(period.getString("value"));


                JSONObject dayRep = reps.getJSONObject(0);
                day.DayTemperature = dayRep.getString("Dm");
                day.Type = dayRep.getInt("W");

                JSONObject nightRep = reps.getJSONObject(1);
                day.NightTemperature = nightRep.getString("Nm");

                forecast.getDays().add(day);
            }
        } catch (final JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
        }

        return forecast;
    }

    private static String GetDayName(String dateValue) {
        Calendar date = ParseDate(dateValue);
        if (date == null)
            return "ERR";

        return DateHelper.GetDayOfWeekAcronym(date);
    }

    private static Calendar ParseDate(String value) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            dateFormat.parse(value);
            return dateFormat.getCalendar();
        } catch (ParseException ex) {
            return null;
        }
    }
}
