package com.example.stopwatch;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView timerTextView;
    private Button startButton, pauseButton, resetButton;
    private Handler handler = new Handler();
    private long startTime = 0L, timeInMillis = 0L, timeSwapBuff = 0L, updateTime = 0L;
    private boolean isRunning = false;

    private Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {
            timeInMillis = System.currentTimeMillis() - startTime;
            updateTime = timeSwapBuff + timeInMillis;
            int secs = (int) (updateTime / 1000);
            int mins = secs / 60;
            int hours = mins / 60;
            secs = secs % 60;
            int milliseconds = (int) (updateTime % 1000);
            timerTextView.setText(String.format("%02d:%02d:%02d:%03d", hours, mins, secs, milliseconds));
            handler.postDelayed(this, 10);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Fix image loading
        ImageView clockImage = findViewById(R.id.clockImage);
        clockImage.setImageResource(R.drawable.alarm_clock); // ✅ Fixed

        timerTextView = findViewById(R.id.timerTextView);
        startButton = findViewById(R.id.startButton);
        pauseButton = findViewById(R.id.pauseButton);
        resetButton = findViewById(R.id.resetButton);

        startButton.setOnClickListener(v -> {
            if (!isRunning) {
                startTime = System.currentTimeMillis();
                handler.postDelayed(updateTimerThread, 0);
                isRunning = true;
            }
        });

        pauseButton.setOnClickListener(v -> {
            if (isRunning) {
                timeSwapBuff += timeInMillis;
                handler.removeCallbacks(updateTimerThread);
                isRunning = false;
            }
        });

        resetButton.setOnClickListener(v -> {
            timeInMillis = 0L;
            timeSwapBuff = 0L;
            updateTime = 0L;
            isRunning = false;
            handler.removeCallbacks(updateTimerThread);
            timerTextView.setText("00:00:00:000");
        });
    }
}
