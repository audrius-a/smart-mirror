package com.development.audrius.smartmirror;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

/**
 * Created by Audri on 06/11/2017.
 */

public class TodayView extends DayView {

    public TodayView(Context context) {
        super(context);
    }

    public TodayView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TodayView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void SetLayout() {
        LayoutInflater.from(getContext()).inflate(R.layout.today_view, this);
    }
}
