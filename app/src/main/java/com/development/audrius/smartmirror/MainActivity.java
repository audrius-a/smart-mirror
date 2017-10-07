package com.development.audrius.smartmirror;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EnableKioskMode();
        setContentView(R.layout.activity_main);


        Calendar calendar= Calendar.getInstance();
        //calendar.set(2017, 8, 12);
        TextView dayName = findViewById(R.id.dayName);
        int test=calendar.get(Calendar.DAY_OF_WEEK);
        dayName.setText(DaysOfWeek.values()[test-1].toString());
        //dayName.setText(new Integer(test).toString());

        SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
        String time = localDateFormat.format(new Date());
        TextView timeView=findViewById(R.id.timeView);
        timeView.setText(time);

       String currentDateTimeString= new Date().toString();
        //String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

// textView is the TextView view that should display it
        TextView textView = findViewById(R.id.dateText);
        textView.setText(currentDateTimeString);
        //dateText.setText(currentDateTimeString);

    }

    private void EnableKioskMode() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD){
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
            ActionBar actionBar = getActionBar();
            actionBar.hide();
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public enum DaysOfWeek {
        Sunday,
        Monday,
        Tuesday,
        Wednesday,
        Thursday,
        Friday,
        Saturday
    }
}
