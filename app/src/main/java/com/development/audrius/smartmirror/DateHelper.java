package com.development.audrius.smartmirror;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.text.format.DateUtils;

/**
 * Created by Audrius on 11/10/2017.
 */

public class DateHelper {
    public static String ToDateString(Date date, String format) {
        SimpleDateFormat secondsDateFormat = new SimpleDateFormat(format);
        return secondsDateFormat.format(date);
    }

    public static String GetDayOfWeekText(Calendar calendar) {
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return DaysOfWeek.values()[dayOfWeek - 1].toString();
    }

    public static Boolean IsMidnight(Date date) {
        String time = DateHelper.ToDateString(date, "HHmmss");
        return time.equalsIgnoreCase("000000");
    }

    public static Boolean MinutesElapsed(Date date, Integer interval) {
        Integer minutes = Integer.parseInt(DateHelper.ToDateString(date, "mm"));
        Integer seconds = Integer.parseInt(DateHelper.ToDateString(date, "ss"));
        return minutes % interval == 0 && seconds == 0;
    }

    public static String GetDayOfWeekAcronym(Calendar calendar) {
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return DaysOfWeekShort.values()[dayOfWeek - 1].toString();
    }

    public static String GetDayOfMonthOrdinal(Calendar calendar) {
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        return dayOfMonth + GetOrdinalCharacter(dayOfMonth);
    }

    public static String Ago(Date date) {
        long now = System.currentTimeMillis();
        return DateUtils.getRelativeTimeSpanString(date.getTime(), now, DateUtils.MINUTE_IN_MILLIS).toString();
    }

    private static String GetOrdinalCharacter(int number) {
        if (number >= 11 && number <= 13) {
            return "th";
        }
        switch (number % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }
}
