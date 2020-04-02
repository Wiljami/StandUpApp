package fi.skd.standup;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class DataManager {
    public static boolean[] getDayToggles(Context context) {
        SharedPreferences prefs = getSharedPreferences(context, "schedule");
        boolean[] days = new boolean[7];
        days[0] = prefs.getBoolean("monday", true);
        days[1] = prefs.getBoolean("tuesday", true);
        days[2] = prefs.getBoolean("wednesday", true);
        days[3] = prefs.getBoolean("thursday", true);
        days[4] = prefs.getBoolean("friday", true);
        days[5] = prefs.getBoolean("saturday", false);
        days[6] = prefs.getBoolean("sunday", false);
        return days;
    }

    public static void toggleDay(Context context, int id, boolean newValue) {
        String day = null;
        switch(id) {
            case(R.id.monday_toggle): day = "monday"; break;
            case(R.id.tuesday_toggle): day = "tuesday"; break;
            case(R.id.wednesday_toggle): day = "wednesday"; break;
            case(R.id.thursday_toggle): day = "thursday"; break;
            case(R.id.friday_toggle): day = "friday"; break;
            case(R.id.saturday_toggle): day = "saturday"; break;
            case(R.id.sunday_toggle): day = "sunday"; break;
        }
        if (day != null) {
            SharedPreferences prefs = getSharedPreferences(context, "schedule");
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(day, newValue);
            editor.apply();
        }
    }

    private static SharedPreferences getSharedPreferences(Context context, String prefKey) {
        return context.getSharedPreferences(prefKey, MODE_PRIVATE);
    }
}
