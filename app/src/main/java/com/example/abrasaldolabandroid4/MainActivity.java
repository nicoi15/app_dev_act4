package com.example.abrasaldolabandroid4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private TextView textViewTimer;
    private TextView textViewStart;
    private TextView textViewEnd;
    private TextView textViewElapsed;

    private Button buttonStart;
    private Button buttonStop;

    private long startTime,millis,buffer, updatedTime;
    private Date startDate = new Date();
    Handler myHandler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewTimer = findViewById(R.id.textviewTimer);
        textViewStart = findViewById(R.id.textviewStart);
        textViewEnd = findViewById(R.id.textviewEnd);
        textViewElapsed = findViewById(R.id.textviewElapsed);

        buttonStart = findViewById(R.id.start);
        buttonStop = findViewById(R.id.stop);
        buttonStop.setEnabled(false);

        Thread myThread = null;
        Runnable runnable = new MoveTime();
        myThread= new Thread(runnable);

        final Thread finalMyThread = myThread;
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTime = SystemClock.uptimeMillis();
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
                String dateTime = sdf.format(cal.getTime());
                startDate = cal.getTime();
                textViewStart.setText(dateTime);
                buttonStart.setEnabled(false);
                buttonStop.setEnabled(true);
                textViewEnd.setText("00:00:00");
                finalMyThread.start();
            }
        });
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buffer += millis;
                buttonStart.setEnabled(true);
                buttonStop.setEnabled(false);
                textViewEnd.setText(textViewTimer.getText().toString());
                textViewTimer.setText("00:00:00");
                finalMyThread.interrupt();
            }
        });
    }
    public void forTime() {
        runOnUiThread(new Runnable() {
            public void run() {
                try{
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
                    String dateTime = sdf.format(cal.getTime());
                    timeDifference(startDate, cal.getTime());
                    textViewTimer.setText(dateTime);
                }catch (Exception e) {}
            }
        });
    }
    private void timeDifference(Date startDate, Date endDate) {
        long difference = endDate.getTime() - startDate.getTime();
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;

        long elapsedHours = difference / hoursInMilli;
        difference = difference % hoursInMilli;

        long elapsedMinutes = difference / minutesInMilli;
        difference = difference % minutesInMilli;

        long elapsedSeconds = difference / secondsInMilli;

        textViewElapsed.setText(String.format("%02d", elapsedHours)
                + ":" + String.format("%02d", elapsedMinutes)
                + ":" + String.format("%02d", elapsedSeconds));
    }
    class MoveTime implements Runnable{
        public void run() {
            while(!Thread.currentThread().isInterrupted()){
                try {
                    forTime();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }catch(Exception e){
                }
            }
        }
    }
}