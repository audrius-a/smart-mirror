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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends Activity {
    private Handler timerHandler = new Handler();
    private MetService metService;


    private ArrayList<DayLayout> dayLayouts;
    TextView timeView;
    TextView secondsView;
    TextView dayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SetKioskMode();
        setContentView(R.layout.activity_main);
        initialiseLayout();

        // Use this line to save unique MetOffice API key
        // Remove the key once saved to avoid checking into source code
        //SaveSetting("MetOfficeApiKey", "xxxxxxxxxxxxxxxxxxx");

        String apiKey = ReadSetting("MetOfficeApiKey");
        metService = new MetService(apiKey);

        UpdateWeather();
        UpdateDate();

        timerHandler.postDelayed(timerRunnable, 1000);
    }

    private void initialiseLayout() {
        dayLayouts = new ArrayList<>();

        DayLayout dayOne = new DayLayout();
        dayOne.MaxTemperature = findViewById(R.id.Day1MaxTemp);
        dayOne.MinTemperature = findViewById(R.id.Day1MinTemp);
        //ImageView favorite = (ImageView) view.findViewById(R.id.favourite_mark_icon);
        dayLayouts.add(dayOne);

        DayLayout dayTwo = new DayLayout();
        dayTwo.MaxTemperature = findViewById(R.id.Day2MaxTemp);
        dayTwo.MinTemperature = findViewById(R.id.Day2MinTemp);
        dayLayouts.add(dayTwo);

        DayLayout dayThree = new DayLayout();
        dayThree.MaxTemperature = findViewById(R.id.Day3MaxTemp);
        dayThree.MinTemperature = findViewById(R.id.Day3MinTemp);
        dayLayouts.add(dayThree);

        DayLayout dayFour = new DayLayout();
        dayFour.MaxTemperature = findViewById(R.id.Day4MaxTemp);
        dayFour.MinTemperature = findViewById(R.id.Day4MinTemp);
        dayLayouts.add(dayFour);

        DayLayout dayFive = new DayLayout();
        dayFive.MaxTemperature = findViewById(R.id.Day5MaxTemp);
        dayFive.MinTemperature = findViewById(R.id.Day5MinTemp);
        dayLayouts.add(dayFive);

        timeView = findViewById(R.id.timeView);
        secondsView = findViewById(R.id.secondsView);
        dayName = findViewById(R.id.dayName);
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

        String time = DateHelper.ToDateString(now, "HH:mm");
        timeView.setText(time);

        String seconds = DateHelper.ToDateString(now, ":ss");
        secondsView.setText(seconds);

        if (time == "00:00" && seconds == ":00") {
            UpdateDate();
            UpdateWeather();
        }
    }

    private void UpdateDate() {
        Calendar calendar = Calendar.getInstance();
        dayName.setText(DateHelper.GetDayOfWeekText(calendar) + " " + DateHelper.GetDayOfMonthOrdinal(calendar));
    }

    private void UpdateWeather() {
        new UpdateWeatherTask().execute();
    }

    private class UpdateWeatherTask extends AsyncTask<Void, Void, Forecast> {
        @Override
        protected Forecast doInBackground(Void... voids) {
            return metService.GetWeather();
        }

        @Override
        protected void onPostExecute(Forecast result) {
            super.onPostExecute(result);

            ArrayList<Day> days = result.getDays();
            for (int i = 0; i < days.size(); i++) {
                dayLayouts.get(i).MaxTemperature.setText(days.get(i).DayTemperature + "°C");
                dayLayouts.get(i).MinTemperature.setText(days.get(i).NightTemperature + "°C");
            }
        }
    }

}