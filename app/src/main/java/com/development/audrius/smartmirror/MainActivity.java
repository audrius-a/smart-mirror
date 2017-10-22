package com.development.audrius.smartmirror;

import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends Activity {
    private Handler timerHandler = new Handler();
    private MetService metService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SetKioskMode();
        setContentView(R.layout.activity_main);

        // Use this line to save unique MetOffice API key
        // Remove the key once saved to avoid checking into source code
        //SaveSetting("MetOfficeApiKey", "xxxxxxxxxxxxxxxxxxx");

        String apiKey = ReadSetting("MetOfficeApiKey");

        metService = new MetService(apiKey);

        UpdateWeather();
        UpdateDate();

        timerHandler.postDelayed(timerRunnable, 1000);
    }

    private String ReadSetting(String key) {
        SharedPreferences settings = getPreferences(0);
        return settings.getString(key, null);
    }

    private void SaveSetting(String key, String value) {
        SharedPreferences settings = getPreferences(0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
    }

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            UpdateTime();
            timerHandler.postDelayed(timerRunnable, 1000);
        }
    };

    private void UpdateTime() {
        Date now = new Date();

        TextView timeView = findViewById(R.id.timeView);
        String time = DateHelper.ToDateString(now, "HH:mm");
        timeView.setText(time);

        TextView secondsView = findViewById(R.id.secondsView);
        String seconds = DateHelper.ToDateString(now, ":ss");
        secondsView.setText(seconds);

        if (time == "00:00" && seconds == ":00") {
            UpdateDate();
            UpdateWeather();
        }
    }

    private void UpdateDate() {
        Calendar calendar = Calendar.getInstance();
        TextView dayName = findViewById(R.id.dayName);
        dayName.setText(DateHelper.GetDayOfWeekText(calendar) + " " + DateHelper.GetDayOfMonthOrdinal(calendar));
    }

    private void UpdateWeather() {
        new UpdateWeatherTask().execute();
    }

    private void SetKioskMode() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            ActionBar actionBar = getActionBar();
            actionBar.hide();
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private class UpdateWeatherTask extends AsyncTask<Void, Void, Forecast> {
        @Override
        protected Forecast doInBackground(Void... voids) {
            return metService.GetWeather();
        }

        @Override
        protected void onPostExecute(Forecast result) {
            super.onPostExecute(result);

            TextView day1MaxTemp = (TextView) findViewById(R.id.Day1MaxTemp);
            day1MaxTemp.setText(result.getDays().get(0).DayTemperature + "°C");

            TextView day1MinTemp = (TextView) findViewById(R.id.Day1MinTemp);
            day1MinTemp.setText(result.getDays().get(0).NightTemperature+"°C");
        }
    }
}