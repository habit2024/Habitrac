package app.habitrac.com.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.fxn.stash.Stash;
import app.habitrac.com.MainActivity;
import app.habitrac.com.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = "Start Your Routine Now";
        String description = "";

        NotificationHelper notificationHelper = new NotificationHelper(context);


        Calendar[] notificationTimes = getNotificationTimes();

        for (Calendar calendar : notificationTimes) {
            notificationHelper.sendHighPriorityNotification(title, description, MainActivity.class);
            NotificationScheduler.scheduleNotification(context, calendar);
        }

    }

    private Calendar[] getNotificationTimes() {
        ArrayList<Long> times = Stash.getArrayList(Constants.TIME_LIST, Long.class);
        Calendar[] notificationTimes = new Calendar[times.size()]; // Replace 5 with the number of times

        for (int i = 0; i < notificationTimes.length; i++) {
            String H = new SimpleDateFormat("hh", Locale.getDefault()).format(times.get(0));
            String M = new SimpleDateFormat("mm", Locale.getDefault()).format(times.get(0));

            int hour = Integer.parseInt(H);
            int minute = Integer.parseInt(M);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            notificationTimes[i] = calendar;
        }
        return notificationTimes;
    }

}

