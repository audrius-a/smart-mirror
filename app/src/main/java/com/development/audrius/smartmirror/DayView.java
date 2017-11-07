package com.development.audrius.smartmirror;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * TODO: document your custom view class.
 */
public class DayView extends ConstraintLayout {

    private TextView name;
    private TextView maxTemperature;
    private TextView minTemperature;
    private ImageView typeIcon;

    public DayView(Context context) {
        super(context);
        SetLayout();
        init();
    }

    public DayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        SetLayout();
        init();
    }

    public DayView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        SetLayout();
        init();
    }

    protected void SetLayout(){
        LayoutInflater.from(getContext()).inflate(R.layout.day_view, this);
    }

    private void init() {
        name = findViewById(R.id.name);
        maxTemperature = findViewById(R.id.maxTemp);
        minTemperature = findViewById(R.id.minTemp);
        typeIcon = findViewById(R.id.typeIcon);
    }

    public void Update(Day day){
        name.setText(day.Name);
        maxTemperature.setText(day.DayTemperature + "°C");
        minTemperature.setText(day.NightTemperature + "°C");
        typeIcon.setImageResource(getWeatherIcon(day.Type));
    }

    private int getWeatherIcon(int type){
        return getResources().getIdentifier("ic_weather_" + type, "drawable", getContext().getPackageName());
    }
}
