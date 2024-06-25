package app.habitrac.com.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.fxn.stash.Stash;

import org.greenrobot.eventbus.EventBus;

import app.habitrac.com.MainActivity;
import app.habitrac.com.R;
import app.habitrac.com.databinding.ActivityCompletedBinding;
import app.habitrac.com.models.HistoryModel;
import app.habitrac.com.models.RoutineModel;
import app.habitrac.com.models.StepsLocalModel;
import app.habitrac.com.ui.DoneEvent;
import app.habitrac.com.ui.DoneListener;
import app.habitrac.com.ui.Logrosfragment;
import app.habitrac.com.utils.Constants;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CompletedActivity extends AppCompatActivity  {
    ActivityCompletedBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCompletedBinding.inflate(getLayoutInflater());
        int theme = Stash.getInt(Constants.THEME);
        setTheme(theme);
        Constants.changeTheme(this);
        setContentView(binding.getRoot());

        binding.bg.setBackgroundColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));


        if (Stash.getBoolean(Constants.LANGUAGE, true)) {
            Constants.setLocale(getBaseContext(), Constants.EN);
        } else {
            Constants.setLocale(getBaseContext(), Constants.ES);
        }

        if (!Stash.getBoolean(Constants.IS_VIP)) {
            Stash.put(Constants.IS_VIP, false);
        }

        Constants.initDialog(this);

        RoutineModel model = (RoutineModel) Stash.getObject("CONGRATS", RoutineModel.class);
        ArrayList<StepsLocalModel> mainList = Stash.getArrayList(model.getID(), StepsLocalModel.class);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));

        binding.steps.setText(mainList.size() + "");
        int min = 0;
        for (StepsLocalModel list : mainList) {
            min += Constants.extractSingleTimeValue(list.getTime());
        }
        String formattedTime = Constants.convertMinutesToHHMM(min) + "h";
        binding.time.setText(formattedTime);



        binding.done.setOnClickListener(v -> {
           // Toast.makeText(this, "ssssssssssssssssssssaa", Toast.LENGTH_SHORT).show();
         Constants.showDialog();
            Map<String, Object> completedDays = new HashMap<>();
            String today = getToday(Constants.getToday());
            String date = Constants.getFormattedDate();
            completedDays.put(today, true);
            Constants.databaseReference().child(Constants.Routines).child(Constants.auth().getCurrentUser().getUid()).child(model.getID())
                    .child("daysCompleted").updateChildren(completedDays).addOnSuccessListener(unused -> {
                        Constants.databaseReference().child(Constants.HISTORY).child(Constants.auth().getCurrentUser().getUid())
                                .child(date).get().addOnSuccessListener(dataSnapshot -> {
                                    if (dataSnapshot.exists()) {
                                        HistoryModel historyModel = dataSnapshot.getValue(HistoryModel.class);
                                        updateHistory(date, historyModel);
                                    } else {
                                        addHistory(date);
                                    }
                                }).addOnFailureListener(e -> {
                                    Constants.dismissDialog();
                                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                });

                    }).addOnFailureListener(e -> {
                        Constants.dismissDialog();
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

    }



    private void addHistory(String date) {
        HistoryModel model = new HistoryModel(new Date().getTime(), date, 1);
        Constants.databaseReference().child(Constants.HISTORY).child(Constants.auth().getCurrentUser().getUid())
                .child(date).setValue(model).addOnSuccessListener(unused -> {
                    Constants.dismissDialog();
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void updateHistory(String date, HistoryModel historyModel) {
        historyModel.setCount(historyModel.getCount() + 1);
        Constants.databaseReference().child(Constants.HISTORY).child(Constants.auth().getCurrentUser().getUid())
                .child(date).setValue(historyModel).addOnSuccessListener(unused -> {
                    Constants.dismissDialog();
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private String getToday(String today) {

        if (today.equalsIgnoreCase("Sun")) {
            return "sunday";
        }
        if (today.equalsIgnoreCase("Mon")) {
            return "monday";
        }
        if (today.equalsIgnoreCase("Tue")) {
            return "tuesday";
        }
        if (today.equalsIgnoreCase("Wed")) {
            return "wednesday";
        }
        if (today.equalsIgnoreCase("Thu")) {
            return "thursday";
        }
        if (today.equalsIgnoreCase("Fri")) {
            return "friday";
        }
        if (today.equalsIgnoreCase("Sat")) {
            return "saturday";
        }
        return "";
    }




}