package com.timerdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.timerdemo.morphing.DFont;
import com.timerdemo.morphing.MorphingView;
import com.timerdemo.stopwach.DemoStop;
import com.timerdemo.wheelpicker.WheelView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnStart;
    EditText edtValue;
    MorphingView txtTimer, morphingMillSec;
    TimerModel timerModel;
    WheelView wvHour, wvMinute, wvSecond;
    //private boolean mCountingDown;
    private CountDownTimer mCountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timerModel = new TimerModel();
        init();
        // mCountingDown = false;
        restoreText();
        setupWheelView();
    }

    private void setupWheelView() {
        wvHour.setOffset(1);
        wvHour.setItems(getHours());
        wvHour.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                Log.e("TAG", "selectedIndex: " + selectedIndex + ", item: " + item);
                // strItem = item;
            }
        });

        wvMinute.setItems(getMin());
        wvSecond.setItems(getMin());
    }

    private List<String> getHours() {
        List<String> hours = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            hours.add(String.format("%02d", i));
        }
        return hours;
    }

    private List<String> getMin() {
        List<String> hours = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            hours.add(String.format("%02d", i));
        }
        return hours;
    }


    private void stopWatchActivity() {
        startActivity(new Intent(this, DemoStop.class));
        finish();
    }

    public void onClickAnother(View view) {
        startActivity(new Intent(this, WheelViewUIActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTimeView();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        startTextCountdown();
    }

    private void init() {
        btnStart = (Button) findViewById(R.id.btnStart);
        edtValue = (EditText) findViewById(R.id.edtValue);


        wvHour = (WheelView) findViewById(R.id.hour);
        wvMinute = (WheelView) findViewById(R.id.mins);
        wvSecond = (WheelView) findViewById(R.id.sec);
        txtTimer = (MorphingView) findViewById(R.id.timerTextView);
        txtTimer.setMorphingDuration(500);
        txtTimer.setFont(new DFont(80, 10));
        morphingMillSec = (MorphingView) findViewById(R.id.morphingMillSec);
        morphingMillSec.setMorphingDuration(150);
        morphingMillSec.setFont(new DFont(40, 5));
    }

    public void onClickStart(View view) {
        if (btnStart.getText().toString().equalsIgnoreCase("Start")) {
            if (edtValue.getText().toString().length() < 6) {
                btnStart.setText("Stop");
                timerModel.appendToTimeString(edtValue.getText().toString());
                updateTimeView();
                timerModel.startTimer(TimeConversionUtil
                        .convertStringToMilliseconds(timerModel
                                .getTimeString()));
                stopTextCountdown();
                startTextCountdown();
                timerModel.setTimeString("");
                //mCountingDown = true;
            } else {
                Toast.makeText(getApplicationContext(),
                        "Cant ",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            stopTextCountdown();
            timerModel.setTimeString("");
            //mCountingDown = false;
            morphingMillSec.setTime("00");
            txtTimer.setTime("00:00:00");
            btnStart.setText("Start");
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(0);
            timerModel.setCurrentAlarmCalendar(c);
        }
    }

    private void updateTimeView() {
        Integer hours = TimeConversionUtil.getHoursFromTimeString(timerModel
                .getTimeString());
        Integer minutes = TimeConversionUtil.getMinutesFromTimeString(timerModel
                .getTimeString());
        Integer seconds = TimeConversionUtil.getSecondsFromTimeString(timerModel
                .getTimeString());
        txtTimer.setTime(String.format("%02d", hours) + ":"
                + String.format("%02d", minutes) + ":"
                + String.format("%02d", seconds));
    }

    private void startTextCountdown() {
        Calendar c = timerModel.getCurrentAlarmCalendar();
        if (c != null && c.getTimeInMillis() != 0) {
            long alarmTime = c.getTimeInMillis();
            long currentTime = Calendar.getInstance().getTimeInMillis();
            long timeDifference = alarmTime - currentTime;
            //Log.e("timeDifference", ""+timeDifference);
            if (timeDifference > 0) {
                // mCountingDown = true;
                btnStart.setText("Stop");
            } else {
                // mCountingDown = false;
            }
            mCountDownTimer = new CountDownTimer(timeDifference, 150) {
                @Override
                public void onTick(long millisUntilFinished) {
                    //Log.e("onTick", ""+millisUntilFinished);
                    txtTimer
                            .setTime(TimeConversionUtil
                                    .getTimeStringFromMilliseconds(millisUntilFinished));
                    morphingMillSec.setTime(TimeConversionUtil
                            .getTimeMillSec(millisUntilFinished));
                }

                @Override
                public void onFinish() {
                    txtTimer.setTime("00:00:00");
                    morphingMillSec.setTime("00");
                    //mCountingDown = false;
                    btnStart.setText("Start");
                }
            };
            mCountDownTimer.start();
        } else {
            // mCountingDown = false;
        }
    }

    private void stopTextCountdown() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    private void restoreText() {
        SharedPreferences settings = getPreferences(0);
        if (settings != null) {
            long milliseconds = settings.getLong(TimeConversionUtil.ALARM_TIME, 0);
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(milliseconds);
            timerModel.setCurrentAlarmCalendar(c);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences settings = getPreferences(0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(TimeConversionUtil.ALARM_TIME, timerModel.getCurrentAlarmCalendar()
                .getTimeInMillis());
        editor.commit();
    }


    //Start StopWatch
    public void onClickStopwatch(View view) {

    }


}
