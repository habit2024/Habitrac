 package app.habitrac.com.utils;

import com.fxn.stash.Stash;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import app.habitrac.com.R;
import app.habitrac.com.models.AddStepsChildModel;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Window;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Constants {

    static Dialog dialog;
    public static final String DATEFORMATE = "dd/MM/yyyy";
    public static final String DATEFORMATEHISTORY = "ddMMyyyy";
    public static String CURRENTTIME = "hh:mm a";
    public static final String USER = "users";
    public static final String Steps = "Steps";
    public static final String THEME = "THEME";
    public static final String Routines = "Routines";
    public static final String HISTORY = "History";
    public static final String LANGUAGE = "LANGUAGE";
    public static final String ROUTINE_LIST = "ROUTINE_LIST";
    public static final String TIME_LIST = "TIME_LIST";
    public static final String DAY = "DAY";
    public static final String EN = "en";
    public static final String ES = "es";
    public static final String MODEL = "MODEL";
    public static final String STEPS_LIST = "STEPS_LIST";
    public static final String SHOW_24 = "SHOW_24";
    public static final String DARK_MODE = "DARK_MODE";
    public static final String COLOR = "COLOR";
    public static final String COLOR_TEXT = "COLOR_TEXT";
    public static final String LICENSE_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgJMiq7qhNcmNRDFMzPWGLx9MOfmFV9/myZ/Boi7+GZPvftT/6jzOa0baSiJ5HSX8bsBwvWRvIpz2y4GJyVEHhcOYoPR0dRZMUQ5LvsxTAdXWAZk/Arr+jhivV0ttJsqpgsr6BJDNtVky7gtK/4aN3PLbyhQz16He/rR/TA5XnqOkNqo6R2uN34mLR0eVaxY52p9MaaOY4z2G1th2KJB9B2TgQGq8FVLu9KJddZwfRPX1zwCR8XKyQcs0OYeWCXKqY4JmB/Wpoflg2UnWNO7IldndlqUjZmy5AM3KbPztPBHI9kIZ27qCLHWju7Fph6ZTl9YciX3o9Y2Y8di6NwdVtQIDAQAB";
    public static final  String VIP_MONTH = "vip.month.com.moutamid.routineapp";
    public static final  String VIP_LIFE = "vip.lifetime.com.moutamid.routineapp";
    public static final  String IS_VIP = "IS_VIP";

    public static String getFormattedDate(long date){
        return new SimpleDateFormat(DATEFORMATE, Locale.getDefault()).format(date);
    }
    public static String getFormattedDate(){
        return new SimpleDateFormat(DATEFORMATEHISTORY, Locale.getDefault()).format(new Date());
    }

    public static String getCurrentTime(){
        if (Stash.getBoolean(Constants.SHOW_24, false)){
            CURRENTTIME = "HH:mm";
        }
        return new SimpleDateFormat(CURRENTTIME, Locale.getDefault()).format(new Date());
    }
    public static FirebaseAuth auth() {
        return FirebaseAuth.getInstance();
    }

    public static DatabaseReference databaseReference() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Habitrac");
        db.keepSynced(true);
        return db;
    }

    public static float historyDay(long timestamp) {
        return Float.parseFloat(new SimpleDateFormat("dd", Locale.getDefault()).format(timestamp));
    }

    // en - English
    // es - Spanish
    public static void setLocale(Context context, String lng) {
        Locale locale = new Locale(lng);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
    }

    public static boolean isInternetConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static void initDialog(Context context){
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
    }

    public static void changeTheme(Context context){
        if (Stash.getBoolean(Constants.DARK_MODE, false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public static String getToday() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        String dayOfWeekString;
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                dayOfWeekString = "Sun";
                break;
            case Calendar.MONDAY:
                dayOfWeekString = "Mon";
                break;
            case Calendar.TUESDAY:
                dayOfWeekString = "Tue";
                break;
            case Calendar.WEDNESDAY:
                dayOfWeekString = "Wed";
                break;
            case Calendar.THURSDAY:
                dayOfWeekString = "Thu";
                break;
            case Calendar.FRIDAY:
                dayOfWeekString = "Fri";
                break;
            case Calendar.SATURDAY:
                dayOfWeekString = "Sat";
                break;
            default:
                dayOfWeekString = "";
                break;
        }
        return dayOfWeekString;
    }

    public static String finishTime(String currentTime, int additionalMinutes) {
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        try {
            Date currentTimeDate = sdf.parse(currentTime);
            if (currentTimeDate == null) {
                return "";
            }

            calendar.setTime(currentTimeDate);

            long currentTimeMillis = currentTimeDate.getTime();
            long endTimeMillis = currentTimeMillis + additionalMinutes * 60000;
            calendar.setTimeInMillis(endTimeMillis);

            String startTime = sdf.format(new Date(currentTimeMillis));
            String endTime = sdf.format(calendar.getTime());

            return endTime;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String calculateTimeRange(String currentTime, int additionalMinutes) {
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        try {
            Date currentTimeDate = sdf.parse(currentTime);
            if (currentTimeDate == null) {
                return "";
            }

            calendar.setTime(currentTimeDate);

            long currentTimeMillis = currentTimeDate.getTime();
            long endTimeMillis = currentTimeMillis + additionalMinutes * 60000;
            calendar.setTimeInMillis(endTimeMillis);

            String startTime = sdf.format(new Date(currentTimeMillis));
            String endTime = sdf.format(calendar.getTime());

            return startTime + " to " + endTime;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static int extractSingleTimeValue(String timeString){
        int time = 0;
        Pattern pattern = Pattern.compile("(\\d+) min");
        Matcher matcher = pattern.matcher(timeString);
        if (matcher.find()) {
            time = Integer.parseInt(matcher.group(1));
        }
        return time;
    }

    public static List<Integer> extractTimeValues(ArrayList<AddStepsChildModel> timeStrings) {
        List<Integer> timeValues = new ArrayList<>();
        Pattern pattern = Pattern.compile("(\\d+) min");

        for (AddStepsChildModel timeString : timeStrings) {
            Matcher matcher = pattern.matcher(timeString.getTime());
            if (matcher.find()) {
                int value = Integer.parseInt(matcher.group(1));
                timeValues.add(value);
            }
        }

        return timeValues;
    }

    public static String convertMinutesToHHMM(int minutes) {
        int hours = minutes / 60;
        int remainingMinutes = minutes % 60;

        return String.format("%02d:%02d", hours, remainingMinutes);
    }


    public static void showDialog(){
        dialog.show();
    }

    public static void dismissDialog(){
        dialog.dismiss();
    }

    public static void checkApp(Activity activity) {
        String appName = "routineApp";

        new Thread(() -> {
            URL google = null;
            try {
                google = new URL("https://raw.githubusercontent.com/Moutamid/Moutamid/main/apps.txt");
            } catch (final MalformedURLException e) {
                e.printStackTrace();
            }
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(google != null ? google.openStream() : null));
            } catch (final IOException e) {
                e.printStackTrace();
            }
            String input = null;
            StringBuffer stringBuffer = new StringBuffer();
            while (true) {
                try {
                    if ((input = in != null ? in.readLine() : null) == null) break;
                } catch (final IOException e) {
                    e.printStackTrace();
                }
                stringBuffer.append(input);
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
            String htmlData = stringBuffer.toString();

            try {
                JSONObject myAppObject = new JSONObject(htmlData).getJSONObject(appName);

                boolean value = myAppObject.getBoolean("value");
                String msg = myAppObject.getString("msg");

                if (value) {
                    activity.runOnUiThread(() -> {
                        new AlertDialog.Builder(activity)
                                .setMessage(msg)
                                .setCancelable(false)
                                .show();
                    });
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }).start();
    }
}
