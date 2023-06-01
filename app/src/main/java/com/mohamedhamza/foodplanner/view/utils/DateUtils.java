package com.mohamedhamza.foodplanner.view.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by Mohamed Hamza on 5/31/2023.
 */


public class DateUtils {
    public static List<Date> getDatesForCurrentWeek() {
        List<Date> dates = new ArrayList<>();

        // Get the current date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        // Adjust the calendar to the previous Saturday
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        calendar.add(Calendar.DATE, -1);

        // Add the dates for the week (Saturday to Friday)
        for (int i = 0; i < 7; i++) {
            calendar.add(Calendar.DATE, 1);
            dates.add(calendar.getTime());
        }

        return dates;
    }

    public static Date getDateForDayName(String dayName) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);

        DateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        for (int i = 0; i < 7; i++) {
            String currentDayName = dateFormat.format(calendar.getTime());
            if (currentDayName.equalsIgnoreCase(dayName)) {
                return calendar.getTime();
            }
            calendar.add(Calendar.DATE, 1);
        }

        return null;
    }
}