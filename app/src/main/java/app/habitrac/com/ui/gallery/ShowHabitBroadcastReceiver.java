package app.habitrac.com.ui.gallery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class ShowHabitBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
            // Check if it's noon (12 PM)
            Calendar calendar = Calendar.getInstance();
            if (calendar.get(Calendar.HOUR_OF_DAY) == 12 && calendar.get(Calendar.MINUTE) == 0) {
                // Show the habit in HomeFragment (logic will be implemented there)
                showHabitInHomeFragment(context);
            }
        }
    }

    private void showHabitInHomeFragment(Context context) {
        // Get the habit data (implementation depends on your app's logic)
        String habit = getHabit();

        // Send the habit data to HomeFragment using an interface or event bus (explained later)
        // ...
    }

    // Implement a method to get the habit data based on your app's logic
    private String getHabit() {
        // ... (replace with your logic to retrieve habit data)
        return "";
    }
}