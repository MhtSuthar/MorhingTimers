package com.timerdemo.stopwach;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.timerdemo.R;
import com.timerdemo.TimeConversionUtil;

import java.util.Calendar;

/**
 * Created by Android-132 on 28-Oct-15.
 */
public class DemoStop extends AppCompatActivity {

    Button btnStart;
    TextView txtTimer;
    private long startTime = 0L;
    private Handler customHandler = new Handler();
    long updatedTime = 0L;
    StopWatchModel stopWatchModel;
    Long timeMill=100L;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo);
        stopWatchModel=new StopWatchModel();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(getSecond() != 0 ){
            btnStart.setText("Pause");
            restoreText();
            setUpdateTimeMills();
            customHandler.postDelayed(updateTimerThread, 100);
        }
    }

    private void setUpdateTimeMills() {
        Calendar c = stopWatchModel.getCurrentAlarmCalendar();
        if (c != null && c.getTimeInMillis() != 0) {
            long alarmTime = c.getTimeInMillis();
            long currentTime = Calendar.getInstance().getTimeInMillis();
            long timeDifference = currentTime - alarmTime;
            if (timeDifference > 0) {
                timeMill=getSecond()+timeDifference;
            }
        }
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeMill=timeMill+100;
            updateTime(timeMill);
            customHandler.postDelayed(this, 100);
        }
    };

    /*private Runnable updateTimerThreadUpdate = new Runnable() {
        public void run() {
            //Log.e("Update call", ""+timeMill);
            timeMill=timeMill+100;
            updateTime(timeMill);
            customHandler.postDelayed(this, 100);
        }
    };*/

    private void init() {
        btnStart= (Button) findViewById(R.id.btnStart);
        txtTimer= (TextView) findViewById(R.id.timerTextView);
    }

    public void onClickStart(View view) {
        if(btnStart.getText().toString().equalsIgnoreCase("Start")){
            btnStart.setText("Pause");
            startTime = SystemClock.uptimeMillis();
            //stopWatchModel.startTimer(System.currentTimeMillis());
            customHandler.postDelayed(updateTimerThread, 100);
        }else{
            btnStart.setText("Start");
            customHandler.removeCallbacks(updateTimerThread);
        }
    }

    public void onClickClear(View view){
        clearStopWatch();
      //  startActivity(new Intent(this, StopwatchActivity.class));
    }

    public void onClickLap(View view){
        Toast.makeText(this, ""+ TimeConversionUtil.getTimeStringFromMilliseconds(timeMill), Toast.LENGTH_SHORT).show();
    }

    private void clearStopWatch() {
        updatedTime = 0L;
        btnStart.setText("Start");
        customHandler.removeCallbacks(updateTimerThread);
        txtTimer.setText("00:00:00");
        timeMill=100L;
        SharedPreferences settings = getPreferences(0);
        SharedPreferences.Editor editor = settings.edit();
        // editor.putLong(TimeConversionUtil.STOP_WATCH_TIME, stopWatchModel.getCurrentAlarmCalendar().getTimeInMillis());
        editor.putLong("time", 0);//time to stop
        editor.commit();
    }

    private void updateTime(long updatedTime) {
        txtTimer.setText(TimeConversionUtil
                .getTimeStringFromMilliseconds1(updatedTime));
    }

    private Long getSecond(){
        SharedPreferences settings = getPreferences(0);
        return settings.getLong("time", 0);
    }

    private void restoreText() {
        SharedPreferences settings = getPreferences(0);
        if (settings != null) {
            long milliseconds = settings.getLong(TimeConversionUtil.STOP_WATCH_TIME, 0);
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(milliseconds);
            stopWatchModel.setCurrentAlarmCalendar(c);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        customHandler.removeCallbacks(updateTimerThread);
        Log.e("stop",""+timeMill);
        if(timeMill != 100) {
            stopWatchModel.startTimer(System.currentTimeMillis());
            SharedPreferences settings = getPreferences(0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putLong(TimeConversionUtil.STOP_WATCH_TIME, stopWatchModel.getCurrentAlarmCalendar().getTimeInMillis());
            editor.putLong("time", timeMill);//time to stop
            editor.commit();
        }
    }
}
