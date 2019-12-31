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

    TextView timeView;
    TextView secondsView;
    TextView dayName;
    TextView status;
    Date lastUpdateDate;
    boolean hasError;
    private ArrayList<DayView> dayViews;

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
        dayViews = new ArrayList<>();

        DayView dayOne = findViewById(R.id.day1View);
        dayViews.add(dayOne);

        DayView dayTwo = findViewById(R.id.day2View);
        dayViews.add(dayTwo);

        DayView dayThree = findViewById(R.id.day3View);
        dayViews.add(dayThree);

        DayView dayFour = findViewById(R.id.day4View);
        dayViews.add(dayFour);

        DayView dayFive = findViewById(R.id.day5View);
        dayViews.add(dayFive);

        timeView = findViewById(R.id.timeView);
        secondsView = findViewById(R.id.secondsView);
        dayName = findViewById(R.id.dayName);
        status = findViewById(R.id.status);
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

        if (!hasError) {
            status.setText("Updated " + DateHelper.Ago(lastUpdateDate));
        }

        if (DateHelper.IsMidnight(now)) {
            UpdateDate();
        }

        int interval = hasError ? 1 : 10;
        if (DateHelper.MinutesElapsed(now, interval)) {
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

    private class UpdateWeatherTask extends AsyncTask<Void, Void, AsyncTaskResult<Forecast>> {
        @Override
        protected AsyncTaskResult<Forecast> doInBackground(Void... voids) {
            try {
                return new AsyncTaskResult(metService.GetWeather());
            } catch (Exception ex) {
                return new AsyncTaskResult(ex);
            }
        }

        @Override
        protected void onPostExecute(AsyncTaskResult<Forecast> result) {
            super.onPostExecute(result);

            Exception error = result.getError();
            if (error == null) {
                status.setText("");
                ArrayList<Day> days = result.getResult().getDays();
                for (int i = 0; i < days.size(); i++) {
                    dayViews.get(i).Update(days.get(i));
                }
                lastUpdateDate = new Date();
                hasError = false;
            } else {
                hasError = true;
                status.setText("Error: " + error.getMessage());
            }
        }
    }
}