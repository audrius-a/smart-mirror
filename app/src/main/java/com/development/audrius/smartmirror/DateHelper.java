package com.development.audrius.smartmirror;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    public static String GetDayOfMonthOrdinal(Calendar calendar) {
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        return dayOfMonth + GetOrdinalCharacter(dayOfMonth);
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
