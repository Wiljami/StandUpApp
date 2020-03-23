package fi.skd.standup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private String TAG = "MainActivity";
    private MediaPlayer mediaPlayer;
    private boolean sound = true;
    private boolean vibration = true;
    private boolean timerIsRunning = false;

    private long activity;
    private long time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupSharedPreferences();
        setupBroadcastReceiver();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupSharedPreferences() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        pref.registerOnSharedPreferenceChangeListener(this);
        pref.edit().clear().commit();
        sound = pref.getBoolean(getString(R.string.pref_audio_key), true);
        vibration = pref.getBoolean(getString(R.string.pref_vibration_key), true);
        setupTimer(pref.getString(getString(R.string.pref_frequency_key), "60"));
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
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(new BroadcastReceiver() {
                                      @Override
                                      public void onReceive(Context context, Intent intent) {
                                          finishTimer(intent);
                                      }
                                  },
                        new IntentFilter("TimerFinish"));
    }

    private void setupTimer(String minutes) {
        int min = Integer.parseInt(minutes);
        time = 1000 * 60 * min;
        setupTimer(time);
    }

    private void setupTimer(long millis) {
        updateTimer(time);
    }

    private void startActivityTimer() {
        setupTimer(activity);
        Intent intent = new Intent(this, TimerService.class);
        intent.putExtra("timer", time);
        intent.putExtra("alarm", false);
        startService(intent);
    }

    private void startAlarmTimer() {
        Intent intent = new Intent(this, TimerService.class);
        intent.putExtra("timer", time);
        intent.putExtra("alarm", true);
        startService(intent);
    }

    private void finishTimer(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            boolean alarm = bundle.getBoolean("alarm");
            if (alarm) alarmUser();
            else {
                TextView textView = findViewById(R.id.alarm_text);
                textView.setText("");
                startAlarmTimer();
            }
        }
    }

    private void alarmUser() {
        TextView alarmText = findViewById(R.id.alarm_text);
        alarmText.setText("STAND UP LIKE YOUR LIFE DEPENDS ON IT!");
        if (vibration) {
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(2000, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                v.vibrate(2000);
            }
        }

        if (sound) {
            mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
            mediaPlayer.start();
        }
        startActivityTimer();
    }

    private void updateTimer(long timeLeftInMillis) {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timerString = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        TextView timer = findViewById(R.id.text_view_timer);

        timer.setText(timerString);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_audio_key)))
            sound = sharedPreferences.getBoolean(key, true);
        if (key.equals(getString(R.string.pref_vibration_key)))
            vibration = sharedPreferences.getBoolean(key, true);
        if (key.equals(getString(R.string.pref_frequency_key))) {
            setupTimer(sharedPreferences.getString(key, "60"));
        }
        if (key.equals(getString(R.string.pref_activity_key))) {
            setActivity(sharedPreferences.getString(key, "5"));
        }
    }

    public void startAlarmTimer(View view) {
        if(!timerIsRunning) {
            timerIsRunning = true;
            startAlarmTimer();
        }
    }

    private void setActivity(String minutes) {
        int min = Integer.parseInt(minutes);
        activity = 1000 * 60 * min;
    }
}