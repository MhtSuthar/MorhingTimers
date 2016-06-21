package com.timerdemo.stopwach;

import java.util.Calendar;

/**
 * Created by Android-132 on 28-Oct-15.
 */
public class StopWatchModel {

    private String mTimeString="";
    private Calendar mCurrentAlarmCalendar;

    public Calendar getCurrentAlarmCalendar() {
        if (mCurrentAlarmCalendar == null) {
            return Calendar.getInstance();
        } else {
            return mCurrentAlarmCalendar;
        }
    }

    public void setCurrentAlarmCalendar(Calendar cal) {
        mCurrentAlarmCalendar = cal;
    }

    public void startTimer(long milliseconds) {
        mCurrentAlarmCalendar = Calendar.getInstance();
        mCurrentAlarmCalendar.setTimeInMillis(milliseconds);
    }
}
