package fi.skd.standup;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SchedulerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduler);
        readAndSetPrefs();
    }

    private void readAndSetPrefs() {
        boolean[] days = DataManager.getDayToggles(this);
        CheckBox dayCheckBox = findViewById(R.id.monday_toggle);
        dayCheckBox.setChecked(days[0]);
        dayCheckBox = findViewById(R.id.tuesday_toggle);
        dayCheckBox.setChecked(days[1]);
        dayCheckBox = findViewById(R.id.wednesday_toggle);
        dayCheckBox.setChecked(days[2]);
        dayCheckBox = findViewById(R.id.thursday_toggle);
        dayCheckBox.setChecked(days[3]);
        dayCheckBox = findViewById(R.id.friday_toggle);
        dayCheckBox.setChecked(days[4]);
        dayCheckBox = findViewById(R.id.saturday_toggle);
        dayCheckBox.setChecked(days[5]);
        dayCheckBox = findViewById(R.id.sunday_toggle);
        dayCheckBox.setChecked(days[6]);
    }

    public void onWeekDayCheckBoxClicked(View view) {
        CheckBox checkBox = (CheckBox) view;
        DataManager.toggleDay(this, view.getId(), checkBox.isChecked());
    }

}