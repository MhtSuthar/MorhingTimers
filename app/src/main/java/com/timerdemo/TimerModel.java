package com.timerdemo;

import java.util.Calendar;

/**
 * Created by Android-132 on 28-Oct-15.
 */
public class TimerModel {

    private String mTimeString="";
    private Calendar mCurrentAlarmCalendar;

    public String getTimeString() {
        return mTimeString;
    }

    public void setTimeString(String mTimeString) {
        this.mTimeString = mTimeString;
    }

    public void appendToTimeString(String append) {
        this.mTimeString = this.mTimeString.concat(append);
    }

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
        mCurrentAlarmCalendar.add(Calendar.SECOND, (int) (milliseconds / 1000));
    }
}
