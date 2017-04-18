package com.rockspin.bargainbits.watch_list.job;

import android.text.format.DateUtils;

import java.util.Calendar;
import java.util.Date;

public class JobScheduleHelper {

    public static class ExecutionWindow {
        public final long startMS;
        public final long endMS;

        public ExecutionWindow(long startMS, long endMS) {
            this.startMS = startMS;
            this.endMS = endMS;
        }
    }

    public static ExecutionWindow getBestExecutionWindowForDate(final Date date) {
        final Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(date);

        final Calendar executionCalendar = Calendar.getInstance();
        executionCalendar.set(Calendar.HOUR_OF_DAY, 12);
        executionCalendar.set(Calendar.MINUTE, dateCalendar.get(Calendar.MINUTE));
        executionCalendar.set(Calendar.SECOND, dateCalendar.get(Calendar.SECOND));
        executionCalendar.set(Calendar.MILLISECOND, dateCalendar.get(Calendar.MILLISECOND));

        // if we've already passed noon for this day, schedule for next day
        if (dateCalendar.get(Calendar.HOUR_OF_DAY) >= 12) {
            executionCalendar.add(Calendar.DATE, 1);
        }

        final long startMS = executionCalendar.getTimeInMillis() - dateCalendar.getTimeInMillis();
        final long endMS = startMS + DateUtils.HOUR_IN_MILLIS;

        return new ExecutionWindow(startMS, endMS);
    }
}
