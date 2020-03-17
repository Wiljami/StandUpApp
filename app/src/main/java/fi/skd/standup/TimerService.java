package fi.skd.standup;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class TimerService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            startTimer(bundle.getLong("timer"));
        }
        return START_STICKY;
    }

    private void startTimer(long timeInMillis) {
        new CountDownTimer(timeInMillis, 1000) {
            @Override
            public void onTick(long timeLeftInMillis) {
                broadcastTick(timeLeftInMillis);
            }

            @Override
            public void onFinish() {
            }
        }.start();
    }

    private void broadcastTick(long timeLeftInMillis) {
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        Intent intent = new Intent("TimerBroadcast");
        intent.putExtra("timeLeftInMillis", timeLeftInMillis);
        manager.sendBroadcast(intent);
    }
}
