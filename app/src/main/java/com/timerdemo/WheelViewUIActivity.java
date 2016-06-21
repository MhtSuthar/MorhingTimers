package com.timerdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.timerdemo.wheelpicker.WheelView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android-132 on 02-Nov-15.
 */
public class WheelViewUIActivity extends AppCompatActivity{

    WheelView wvHour, wvMinute, wvSecond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheel_view_ui);
        init();
        wvMinute.setItems(getMin());
        wvSecond.setItems(getMin());
    }

    private void init() {
       // wvHour= (WheelView) findViewById(R.id.hour);
        wvMinute= (WheelView) findViewById(R.id.mins);
        wvSecond= (WheelView) findViewById(R.id.sec);
    }

    private List<String> getMin() {
        List<String> hours=new ArrayList<>();
        for (int i=0; i< 60; i++)
        {
            hours.add(String.format("%02d", i));
        }
        return hours;
    }
}
