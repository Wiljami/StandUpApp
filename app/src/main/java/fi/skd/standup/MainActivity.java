package fi.skd.standup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupBroadcastReceiver();
        startTimer();
    }

    private void setupBroadcastReceiver() {
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        Bundle bundle = intent.getExtras();
                        if (bundle != null) {
                            updateTimer(bundle.getLong("timeLeftInMillis"));
                        }
                    }
                },
                new IntentFilter("TimerBroadcast"));
    }

    private void startTimer() {
        Intent intent = new Intent(this, TimerService.class);
        long time = 10000;
        intent.putExtra("timer", time);
        startService(intent);
    }

    private void updateTimer(long timeLeftInMillis) {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timerString = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        TextView timer = findViewById(R.id.text_view_timer);

        timer.setText(timerString);
    }
}
