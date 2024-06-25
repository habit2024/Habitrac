package app.habitrac.com.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.fxn.stash.Stash;
import app.habitrac.com.MainActivity;
import app.habitrac.com.R;
import app.habitrac.com.adapters.RoutineStartAdapter;
import app.habitrac.com.databinding.ActivityRoutineStartBinding;
import app.habitrac.com.databinding.UpdateDaysBottomsheetBinding;
import app.habitrac.com.models.CompletedDaysModel;
import app.habitrac.com.models.RoutineModel;
import app.habitrac.com.models.StepsLocalModel;
import app.habitrac.com.utils.Constants;

import java.util.ArrayList;

public class RoutineStartActivity extends AppCompatActivity {
    ActivityRoutineStartBinding binding;
    RoutineModel model;
    boolean monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    boolean allDone = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRoutineStartBinding.inflate(getLayoutInflater());
        int theme = Stash.getInt(Constants.THEME);
        setTheme(theme);
        Constants.changeTheme(this);
        setContentView(binding.getRoot());
        binding.toolbar.tittle.setText(getString(R.string.start_routine));
        Constants.initDialog(this);


        if (Stash.getBoolean(Constants.LANGUAGE, true)){
            Constants.setLocale(getBaseContext(), Constants.EN);
        } else {
            Constants.setLocale(getBaseContext(), Constants.ES);
        }

        if (!Stash.getBoolean(Constants.IS_VIP)){
            Stash.put(Constants.IS_VIP, false);
        }

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        binding.edit.setOnClickListener(v -> {
            startActivity(new Intent(this, EditRoutineActivity.class));
            finish();
        });

        model = (RoutineModel) Stash.getObject(Constants.MODEL, RoutineModel.class);

        String startNow = Constants.calculateTimeRange(Constants.getCurrentTime(), model.getMinutes());

        binding.name.setText(model.getName());
        binding.time.setText(startNow);
        String s = model.getSteps().size() > 1 ? getString(R.string.steps) : getString(R.string.step);
        binding.totalSteps.setText(model.getSteps().size() + s);

        binding.totalTime.setText(getString(R.string.total_time) + " " + Constants.convertMinutesToHHMM(model.getMinutes()) + " h" );

        if (model.getDaysCompleted().isMonday()){
            binding.mondayCheck.setVisibility(View.VISIBLE);
        }
        if (model.getDaysCompleted().isTuesday()){
            binding.teusdayCheck.setVisibility(View.VISIBLE);
        }
        if (model.getDaysCompleted().isWednesday()){
            binding.wednessCheck.setVisibility(View.VISIBLE);
        }
        if (model.getDaysCompleted().isThursday()){
            binding.thursdayCheck.setVisibility(View.VISIBLE);
        }
        if (model.getDaysCompleted().isFriday()){
            binding.fridayCheck.setVisibility(View.VISIBLE);
        }
        if (model.getDaysCompleted().isSaturday()){
            binding.saturdayCheck.setVisibility(View.VISIBLE);
        }
        if (model.getDaysCompleted().isSunday()){
            binding.sundayCheck.setVisibility(View.VISIBLE);
        }

        binding.routineRC.setLayoutManager(new LinearLayoutManager(this));
        binding.routineRC.setHasFixedSize(false);
        ArrayList<StepsLocalModel> list = Stash.getArrayList(model.getID(), StepsLocalModel.class);
        RoutineStartAdapter adapter = new RoutineStartAdapter(this, list);
        binding.routineRC.setAdapter(adapter);

        binding.daysCheckLayout.setOnClickListener(v -> showDialog());

        binding.start.setOnClickListener(v -> {
            if (getAllDone()) {
                Stash.put(Constants.ROUTINE_LIST, model);
                Stash.put(Constants.DAY, model);
                startActivity(new Intent(this, TimerActivity.class));
                finish();
            } else {
                Toast.makeText(this, getString(R.string.you_already_finish_all_task), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean getAllDone() {
        ArrayList<StepsLocalModel> list = Stash.getArrayList(model.getID(), StepsLocalModel.class);
        for (StepsLocalModel model : list) {
            if (!model.isCompleted()) {
                allDone = true;
            }
        }
        return allDone;
    }


    private void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        UpdateDaysBottomsheetBinding bottomsheetBinding = UpdateDaysBottomsheetBinding.inflate(getLayoutInflater());
        dialog.setContentView(bottomsheetBinding.getRoot());
        String today = Constants.getToday();

        bottomsheetBinding.sunday.setCardBackgroundColor(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text)));
        bottomsheetBinding.monday.setCardBackgroundColor(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text)));
        bottomsheetBinding.tuesday.setCardBackgroundColor(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text)));
        bottomsheetBinding.wednesday.setCardBackgroundColor(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text)));
        bottomsheetBinding.thursday.setCardBackgroundColor(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text)));
        bottomsheetBinding.friday.setCardBackgroundColor(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text)));
        bottomsheetBinding.saturday.setCardBackgroundColor(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text)));

        bottomsheetBinding.sundayCheck.setImageTintList(ColorStateList.valueOf(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light))));
        bottomsheetBinding.mondayCheck.setImageTintList(ColorStateList.valueOf(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light))));
        bottomsheetBinding.teusdayCheck.setImageTintList(ColorStateList.valueOf(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light))));
        bottomsheetBinding.wednessCheck.setImageTintList(ColorStateList.valueOf(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light))));
        bottomsheetBinding.thursdayCheck.setImageTintList(ColorStateList.valueOf(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light))));
        bottomsheetBinding.fridayCheck.setImageTintList(ColorStateList.valueOf(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light))));
        bottomsheetBinding.saturdayCheck.setImageTintList(ColorStateList.valueOf(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light))));

        if (today.equalsIgnoreCase("Sun")) {
            bottomsheetBinding.sunday.setStrokeColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        }
        if (today.equalsIgnoreCase("Mon")) {
            bottomsheetBinding.monday.setStrokeColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        }
        if (today.equalsIgnoreCase("Tue")) {
            bottomsheetBinding.tuesday.setStrokeColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        }
        if (today.equalsIgnoreCase("Wed")) {
            bottomsheetBinding.wednesday.setStrokeColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        }
        if (today.equalsIgnoreCase("Thu")) {
            bottomsheetBinding.thursday.setStrokeColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        }
        if (today.equalsIgnoreCase("Fri")) {
            bottomsheetBinding.friday.setStrokeColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        }
        if (today.equalsIgnoreCase("Sat")) {
            bottomsheetBinding.saturday.setStrokeColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        }

        if (model.getDaysCompleted().isMonday()){
            bottomsheetBinding.mondayCheck.setVisibility(View.VISIBLE);
        }
        if (model.getDaysCompleted().isTuesday()){
            bottomsheetBinding.teusdayCheck.setVisibility(View.VISIBLE);
        }
        if (model.getDaysCompleted().isWednesday()){
            bottomsheetBinding.wednessCheck.setVisibility(View.VISIBLE);
        }
        if (model.getDaysCompleted().isThursday()){
            bottomsheetBinding.thursdayCheck.setVisibility(View.VISIBLE);
        }
        if (model.getDaysCompleted().isFriday()){
            bottomsheetBinding.fridayCheck.setVisibility(View.VISIBLE);
        }
        if (model.getDaysCompleted().isSaturday()){
            bottomsheetBinding.saturdayCheck.setVisibility(View.VISIBLE);
        }
        if (model.getDaysCompleted().isSunday()){
            bottomsheetBinding.sundayCheck.setVisibility(View.VISIBLE);
        }


        bottomsheetBinding.monday.setOnClickListener(v -> {
            int vis = bottomsheetBinding.mondayCheck.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
            bottomsheetBinding.mondayCheck.setVisibility(vis);
        });

        bottomsheetBinding.tuesday.setOnClickListener(v -> {
            int vis = bottomsheetBinding.teusdayCheck.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
            bottomsheetBinding.teusdayCheck.setVisibility(vis);
        });

        bottomsheetBinding.wednesday.setOnClickListener(v -> {
            int vis = bottomsheetBinding.wednessCheck.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
            bottomsheetBinding.wednessCheck.setVisibility(vis);
        });

        bottomsheetBinding.thursday.setOnClickListener(v -> {
            int vis = bottomsheetBinding.thursdayCheck.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
            bottomsheetBinding.thursdayCheck.setVisibility(vis);
        });

        bottomsheetBinding.friday.setOnClickListener(v -> {
            int vis = bottomsheetBinding.fridayCheck.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
            bottomsheetBinding.fridayCheck.setVisibility(vis);
        });

        bottomsheetBinding.saturday.setOnClickListener(v -> {
            int vis = bottomsheetBinding.saturdayCheck.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
            bottomsheetBinding.saturdayCheck.setVisibility(vis);
        });

        bottomsheetBinding.sunday.setOnClickListener(v -> {
            int vis = bottomsheetBinding.sundayCheck.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
            bottomsheetBinding.sundayCheck.setVisibility(vis);
        });

        bottomsheetBinding.update.setOnClickListener(v -> {
            dialog.dismiss();
            monday = bottomsheetBinding.mondayCheck.getVisibility() == View.VISIBLE;
            tuesday = bottomsheetBinding.teusdayCheck.getVisibility() == View.VISIBLE;
            wednesday = bottomsheetBinding.wednessCheck.getVisibility() == View.VISIBLE;
            thursday = bottomsheetBinding.thursdayCheck.getVisibility() == View.VISIBLE;
            friday = bottomsheetBinding.fridayCheck.getVisibility() == View.VISIBLE;
            saturday = bottomsheetBinding.saturdayCheck.getVisibility() == View.VISIBLE;
            sunday = bottomsheetBinding.sundayCheck.getVisibility() == View.VISIBLE;
            Constants.showDialog();
            CompletedDaysModel completedDays = new CompletedDaysModel(monday, tuesday, wednesday, thursday, friday, saturday, sunday);
            Constants.databaseReference().child(Constants.Routines).child(Constants.auth().getCurrentUser().getUid()).child(model.getID())
                    .child("daysCompleted").setValue(completedDays).addOnSuccessListener(unused -> {
                        Constants.dismissDialog();
                        int mon = monday ? View.VISIBLE : View.GONE;
                        int tue = tuesday ? View.VISIBLE : View.GONE;
                        int wed = wednesday ? View.VISIBLE : View.GONE;
                        int thu = thursday ? View.VISIBLE : View.GONE;
                        int fri = friday ? View.VISIBLE : View.GONE;
                        int sat = saturday ? View.VISIBLE : View.GONE;
                        int sun = sunday ? View.VISIBLE : View.GONE;

                        model.setDaysCompleted(completedDays);

                        binding.mondayCheck.setVisibility(mon);
                        binding.teusdayCheck.setVisibility(tue);
                        binding.wednessCheck.setVisibility(wed);
                        binding.thursdayCheck.setVisibility(thu);
                        binding.fridayCheck.setVisibility(fri);
                        binding.saturdayCheck.setVisibility(sat);
                        binding.sundayCheck.setVisibility(sun);

                    }).addOnFailureListener(e-> {
                        Constants.dismissDialog();
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.BottomSheetAnim;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateViews();
    }

    private void updateViews() {
        binding.sunday.setCardBackgroundColor(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text)));
        binding.monday.setCardBackgroundColor(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text)));
        binding.tuesday.setCardBackgroundColor(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text)));
        binding.wednesday.setCardBackgroundColor(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text)));
        binding.thursday.setCardBackgroundColor(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text)));
        binding.friday.setCardBackgroundColor(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text)));
        binding.saturday.setCardBackgroundColor(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text)));

        binding.sundayCheck.setImageTintList(ColorStateList.valueOf(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light))));
        binding.mondayCheck.setImageTintList(ColorStateList.valueOf(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light))));
        binding.teusdayCheck.setImageTintList(ColorStateList.valueOf(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light))));
        binding.wednessCheck.setImageTintList(ColorStateList.valueOf(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light))));
        binding.thursdayCheck.setImageTintList(ColorStateList.valueOf(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light))));
        binding.fridayCheck.setImageTintList(ColorStateList.valueOf(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light))));
        binding.saturdayCheck.setImageTintList(ColorStateList.valueOf(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light))));

        binding.totalTime.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.time.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.edit.setBackgroundColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.start.setBackgroundColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}