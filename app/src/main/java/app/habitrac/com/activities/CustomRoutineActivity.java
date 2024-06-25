package app.habitrac.com.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import com.fxn.stash.Stash;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import app.habitrac.com.MainActivity;
import app.habitrac.com.R;
import app.habitrac.com.SplashScreenActivity;
import app.habitrac.com.WelcomeActivity;
import app.habitrac.com.adapters.AddStepsChildAdapter;
import app.habitrac.com.adapters.AddStepsParentAdapter;
import app.habitrac.com.adapters.RoutineAdapter;
import app.habitrac.com.bottomsheets.AddStepsFragment;
import app.habitrac.com.databinding.ActivityCustomRoutineBinding;
import app.habitrac.com.listners.BottomSheetDismissListener;
import app.habitrac.com.listners.StepClickListner;
import app.habitrac.com.models.AddStepsChildModel;
import app.habitrac.com.models.AddStepsModel;
import app.habitrac.com.models.CompletedDaysModel;
import app.habitrac.com.models.RoutineModel;
import app.habitrac.com.models.StepsLocalModel;
import app.habitrac.com.models.UserModel;
import app.habitrac.com.notification.NotificationHelper;
import app.habitrac.com.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class CustomRoutineActivity extends AppCompatActivity implements BottomSheetDismissListener {
    ActivityCustomRoutineBinding binding;
    private static final String TAG = "CustomRoutineActivity";
    List<String> context;
    ArrayAdapter<String> partiesAdapter;
    ArrayList<AddStepsChildModel> list;
    AddStepsChildAdapter adapter;
    ArrayList<AddStepsModel> listx;
    RecyclerView recyler;
    AddStepsParentAdapter adapterx;
    StepsLocalModel stepsModel;
    RoutineModel model;
    int count = 0;
    ArrayList<StepsLocalModel> stepsList;
    int minute = 0;
    long reminder = 0;
    private InterstitialAd mInterstitialAd;
    SharedPreferences prefs;
    private int currentQuestion = 1;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomRoutineBinding.inflate(getLayoutInflater());
        int theme = Stash.getInt(Constants.THEME);
        setTheme(theme);
        Constants.changeTheme(this);
        setContentView(binding.getRoot());

        binding.toolbar.tittle.setText(getResources().getString(R.string.add_routine));
        Constants.initDialog(this);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,getString(R.string.AD_Interstitial_ID), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });
        if (!Stash.getBoolean(Constants.IS_VIP)){
            Stash.put(Constants.IS_VIP, false);

        }

        if (Stash.getBoolean(Constants.LANGUAGE, true)){
            Constants.setLocale(getBaseContext(), Constants.EN);
        } else {
            Constants.setLocale(getBaseContext(), Constants.ES);
        }

        LinearLayout[] linearLayouts = new LinearLayout[]{
                findViewById(R.id.v1),
                findViewById(R.id.v2)

        };
        model = (RoutineModel) Stash.getObject(Constants.ROUTINE_LIST, RoutineModel.class);
        if (model != null) {
            String id = model.getID();
            ArrayList<StepsLocalModel> modelList = Stash.getArrayList(id, StepsLocalModel.class);
            stepsList = new ArrayList<>();
            for (StepsLocalModel model : modelList) {
                if (!model.isCompleted()) {
                    stepsList.add(model);
                }
            }
            if (count >= 0 && count < stepsList.size()) {
                stepsModel = stepsList.get(count);
            } else {
                // Handle invalid index (e.g., log an error)
            }
        } else {
            // Handle the case where model is null (e.g., log an error or provide a default value)
        }
      //  ArrayList<StepsLocalModel> modelList = Stash.getArrayList(model.getID(), StepsLocalModel.class);

     // stepsModel = stepsList.get(count);
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        context = new ArrayList<>();
        String[] stringArray = getResources().getStringArray(R.array.my_context_array);
        context.addAll(Arrays.asList(stringArray));
        prefs = getSharedPreferences("mis_preferencias", MODE_PRIVATE);
        binding.stepsRC.setLayoutManager(new LinearLayoutManager(this));
        binding.stepsRC.setHasFixedSize(false);
        recyler = findViewById(R.id.recyler);
        recyler.setLayoutManager(new LinearLayoutManager(this));
        recyler.setHasFixedSize(false);

        listx = new ArrayList<>();
        if (Stash.getString(Constants.STEPS_LIST, "ALL").equals("ALL")) {
            binding.name.getEditText().setText("");
        } else if (Stash.getString(Constants.STEPS_LIST, "ALL").equals("MORNING")) {
            binding.name.getEditText().setText(getString(R.string.morning_routine));
        } else if (Stash.getString(Constants.STEPS_LIST, "ALL").equals("EVENING")) {
            binding.name.getEditText().setText(getString(R.string.evening_routine));
        } else if (Stash.getString(Constants.STEPS_LIST, "ALL").equals("WORK")) {
            binding.name.getEditText().setText(getString(R.string.ready_for_work_routine));
        } else if (Stash.getString(Constants.STEPS_LIST, "ALL").equals("SELFCARE")) {
            binding.name.getEditText().setText(getString(R.string.selfcare_routine));
        } else if (Stash.getString(Constants.STEPS_LIST, "ALL").equals("STUDY")) {
            binding.name.getEditText().setText(getString(R.string.study_routine));
        }
        if (Stash.getString(Constants.STEPS_LIST, "ALL").equals("ALL")) {
            addData();
        } else if (Stash.getString(Constants.STEPS_LIST, "ALL").equals("MORNING")) {
            morningSteps();
        } else if (Stash.getString(Constants.STEPS_LIST, "ALL").equals("EVENING")) {
            eveningSteps();
        } else if (Stash.getString(Constants.STEPS_LIST, "ALL").equals("WORK")) {
            workSteps();
        } else if (Stash.getString(Constants.STEPS_LIST, "ALL").equals("SELFCARE")) {
            selfcareSteps();
        } else if (Stash.getString(Constants.STEPS_LIST, "ALL").equals("STUDY")) {
            studySteps();
        }

        adapterx = new AddStepsParentAdapter(CustomRoutineActivity.this, listx, listner);
        recyler.setAdapter(adapterx);

        partiesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, context);
        binding.addContext.setAdapter(partiesAdapter);
       // getSupportFragmentManager().beginTransaction().replace(R.id.framelayoutop, new AddStepsFragment()).commit();

        binding.addSteps.setOnClickListener(v -> {
            AddStepsFragment bottomSheetFragment = new AddStepsFragment();
            bottomSheetFragment.setListener(this);
           bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
        });

        binding.addReminder.setOnClickListener(v -> {
            openTimePicker();
        });

        ItemTouchHelper.Callback ithCallback = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
                        ItemTouchHelper.DOWN | ItemTouchHelper.UP);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                Collections.swap(list, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                adapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                Stash.put(Constants.Steps, list);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        };
        ItemTouchHelper ith = new ItemTouchHelper(ithCallback);
        ith.attachToRecyclerView(binding.stepsRC);

        binding.save.setOnClickListener(v -> {


            if (currentQuestion < 1 || currentQuestion > 6) {
                // Handle invalid currentQuestion value
                return;
            }

            if (currentQuestion == 1) {
                // Check if a radio button is selected in question 1 (optional)

            }

            switch (currentQuestion) {
                case 1:
                    linearLayouts[0].setVisibility(View.GONE);
                    currentQuestion = 2;
                    break;
                case 2:
                    linearLayouts[0].setVisibility(View.GONE);
                    AddStepsFragment bottomSheetFragment = new AddStepsFragment();
                    bottomSheetFragment.setListener(this);
                    bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
                    new Handler().postDelayed(() -> {
                        if (valid()){
                            Constants.showDialog();
                            String ID = UUID.randomUUID().toString();
                            ArrayList<String> days = getDays();
                            CompletedDaysModel daysCompleted = new CompletedDaysModel(false,false,false,false,false,false,false);
                            RoutineModel model = new RoutineModel(
                                    ID, binding.name.getEditText().getText().toString(), binding.context.getEditText().getText().toString(),
                                    minute, days, daysCompleted, list, reminder
                            );
                            Constants.databaseReference().child(Constants.Routines).child(Constants.auth().getCurrentUser().getUid())
                                    .child(ID).setValue(model).addOnSuccessListener(unused -> {
                                        Constants.dismissDialog();
                                        ArrayList<StepsLocalModel> localList = new ArrayList<>();
                                        for(AddStepsChildModel childModel : list){
                                            localList.add(new StepsLocalModel(ID, childModel.getName(), childModel.getTime(), false));
                                        }
                                        Stash.put(ID, localList);
                                        Stash.clear(Constants.Steps);
                                        String steps = "";
                                        for (int i = 0; i < model.getSteps().size(); i++) {
                                            String st = model.getSteps().get(i).getName();
                                            steps +=  st;
                                            if (i == 2){
                                                steps += "...";
                                                break;
                                            }
                                        }


                                        Constants.databaseReference().child(Constants.Routines).child(Constants.auth().getCurrentUser().getUid())
                                                .child(ID).child("name").setValue(steps);
                                        Toast.makeText(this, getString(R.string.routine_created), Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(CustomRoutineActivity.this, MainActivity.class));
                                        finish();
                                        if (!Stash.getBoolean(Constants.IS_VIP)){
                                            // showAdInter();
                                        } else {
                                            Log.d("TAG", "User account is premium");
                                            startActivity(new Intent(CustomRoutineActivity.this, MainActivity.class));
                                            finish();
                                        }

                                    }).addOnFailureListener(e -> {
                                        Constants.dismissDialog();
                                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    }, 2000);
                    Runnable showNotificationRunnable = new Runnable() {
                        @Override
                        public void run() {
                            // Enviar la notificación
                            NotificationHelper helper = new NotificationHelper(CustomRoutineActivity.this);
                            String body = stepsModel.getName() + " " + stepsModel.getTime() + " " + getString(R.string.left);
                            helper.sendHighPriorityNotification(getString(R.string.start_routine), body, TimerActivity.class);
                        }
                    };
                    Calendar now = Calendar.getInstance();
                    int targetHour = 18;
                    int targetMinute = 41;

                    if (now.get(Calendar.HOUR_OF_DAY) < targetHour ||
                            (now.get(Calendar.HOUR_OF_DAY) == targetHour && now.get(Calendar.MINUTE) < targetMinute)) {
                        // Calculate delay (ensure target time is in the future)
                        now.set(Calendar.HOUR_OF_DAY, targetHour);
                        now.set(Calendar.MINUTE, targetMinute);
                        now.set(Calendar.SECOND, 0);
                        long timeInMillis = now.getTimeInMillis() - System.currentTimeMillis();
                        handler.postDelayed(showNotificationRunnable, timeInMillis);
                    } else {

                    }

                    break;


            }

            updateLayoutVisibility(linearLayouts,  currentQuestion);





























        });

    }

    private void updateLayoutVisibility(LinearLayout[] linearLayouts, int currentQuestion) {
        for (int i = 0; i < linearLayouts.length; i++) {
            linearLayouts[i].setVisibility(i == (currentQuestion - 1) ? View.VISIBLE : View.GONE);
        }

    }
    private void morningSteps() {
        ArrayList<AddStepsChildModel> child1 = new ArrayList<>();
        child1.add(new AddStepsChildModel(getString(R.string.goodA),"20 min"));
        child1.add(new AddStepsChildModel(getString(R.string.study), "20 min"));
        child1.add(new AddStepsChildModel(getString(R.string.exercise), "5 min"));
        child1.add(new AddStepsChildModel(getString(R.string.meditate_4), "5 min"));
        child1.add(new AddStepsChildModel(getString(R.string.org), "10 min"));
        child1.add(new AddStepsChildModel(getString(R.string.Lect), "15 min"));
        child1.add(new AddStepsChildModel(getString(R.string.Sleep), "15 min"));
        child1.add(new AddStepsChildModel(getString(R.string.other), "3 min"));

        listx.add(new AddStepsModel(getString(R.string.morning_routine), child1));


    }

    private void eveningSteps() {
        ArrayList<AddStepsChildModel> child2 = new ArrayList<>();
        child2.add(new AddStepsChildModel(getString(R.string.creative), "1 min"));
        child2.add(new AddStepsChildModel(getString(R.string.small), "5 min"));
        child2.add(new AddStepsChildModel(getString(R.string.elog), "10 min"));
        child2.add(new AddStepsChildModel(getString(R.string.dance), "40 min"));
        child2.add(new AddStepsChildModel(getString(R.string.Pic), "30 min"));


        listx.add(new AddStepsModel(getString(R.string.evening_routine), child2));
    }

    private void workSteps() {
        ArrayList<AddStepsChildModel> child3 = new ArrayList<>();
        child3.add(new AddStepsChildModel(getString(R.string.answer_emails), "25 min"));
        child3.add(new AddStepsChildModel(getString(R.string.short_break), "15 min"));
        child3.add(new AddStepsChildModel(getString(R.string.deep_work), "50 min"));
        child3.add(new AddStepsChildModel(getString(R.string.write_down_priorities), "10 min"));
        child3.add(new AddStepsChildModel(getString(R.string.prepare_meetings), "25 min"));
        child3.add(new AddStepsChildModel(getString(R.string.breathing_exercise), "5 min"));
        child3.add(new AddStepsChildModel(getString(R.string.make_coffee_3), "10 min"));
        child3.add(new AddStepsChildModel(getString(R.string.long_break), "40 min"));
        child3.add(new AddStepsChildModel(getString(R.string.pomodoro), "20 min"));

        listx.add(new AddStepsModel(getString(R.string.ready_for_work_routine), child3));
    }

    private void selfcareSteps() {
        ArrayList<AddStepsChildModel> child4 = new ArrayList<>();
        child4.add(new AddStepsChildModel(getString(R.string.write_todo_list), "5 min"));
        child4.add(new AddStepsChildModel(getString(R.string.exercise), "30 min"));
        child4.add(new AddStepsChildModel(getString(R.string.write_grateful_4), "20 min"));
        child4.add(new AddStepsChildModel(getString(R.string.take_warm_bath), "40 min"));
        child4.add(new AddStepsChildModel(getString(R.string.take_loved_ones), "45 min"));
        child4.add(new AddStepsChildModel(getString(R.string.meditate_4), "20 min"));
        child4.add(new AddStepsChildModel(getString(R.string.visualize_success), "10 min"));

        listx.add(new AddStepsModel(getString(R.string.selfcare_routine), child4));
    }

    private void studySteps() {
        ArrayList<AddStepsChildModel> child5 = new ArrayList<>();
        child5.add(new AddStepsChildModel(getString(R.string.practice_problems), "30 min"));
        child5.add(new AddStepsChildModel(getString(R.string.study), "45 min"));
        child5.add(new AddStepsChildModel(getString(R.string.read_subject), "30 min"));
        child5.add(new AddStepsChildModel(getString(R.string.breathing_exercise_5), "10 min"));
        child5.add(new AddStepsChildModel(getString(R.string.figure_out), "20 min"));
        child5.add(new AddStepsChildModel(getString(R.string.write_task), "5 min"));
        child5.add(new AddStepsChildModel(getString(R.string.deep_work_5), "45 min"));
        child5.add(new AddStepsChildModel(getString(R.string.prepare_desk), "5 min"));

        listx.add(new AddStepsModel(getString(R.string.study_routine), child5));
    }

    private void addData() {
        morningSteps();
        eveningSteps();
        workSteps();
        selfcareSteps();
        studySteps();

    }



    StepClickListner listner = model -> {
        ArrayList<AddStepsChildModel> list = Stash.getArrayList(Constants.Steps, AddStepsChildModel.class);
        list.add(model);
        Stash.put(Constants.Steps, list);
        // this.dismiss();
    };
    public void showAdInter() {


                Constants.showDialog();
                String ID = UUID.randomUUID().toString();
                ArrayList<String> days = getDays();
                CompletedDaysModel daysCompleted = new CompletedDaysModel(false,false,false,false,false,false,false);
                RoutineModel model = new RoutineModel(
                        ID, binding.name.getEditText().getText().toString(), binding.context.getEditText().getText().toString(),
                        minute, days, daysCompleted, list, reminder
                );
                Constants.databaseReference().child(Constants.Routines).child(Constants.auth().getCurrentUser().getUid())
                        .child(ID).setValue(model).addOnSuccessListener(unused -> {
                            Constants.dismissDialog();
                            ArrayList<StepsLocalModel> localList = new ArrayList<>();
                            for(AddStepsChildModel childModel : list){
                                localList.add(new StepsLocalModel(ID, childModel.getName(), childModel.getTime(), false));
                            }
                            Stash.put(ID, localList);
                            Stash.clear(Constants.Steps);
                            Toast.makeText(this, getString(R.string.routine_created), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(CustomRoutineActivity.this, MainActivity.class));
                            finish();
                            if (!Stash.getBoolean(Constants.IS_VIP)){
                                // showAdInter();
                            } else {
                                Log.d("TAG", "User account is premium");
                                startActivity(new Intent(CustomRoutineActivity.this, MainActivity.class));
                                finish();
                            }

                        }).addOnFailureListener(e -> {
                            Constants.dismissDialog();
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        });




    }



    private void openTimePicker() {
        int format = Stash.getBoolean(Constants.SHOW_24) ? TimeFormat.CLOCK_24H : TimeFormat.CLOCK_12H;
        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(format)
                .setHour(12)
                .setMinute(0)
                .setTitleText(getString(R.string.select_time))
                .setPositiveButtonText(getString(R.string.add))
                .setNegativeButtonText(getString(R.string.no_reminder))
                .build();


        timePicker.addOnNegativeButtonClickListener(view -> {
            binding.timeText.setText(getString(R.string.no_reminder));
        });

        timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);

                reminder = calendar.getTimeInMillis();
                String form = Stash.getBoolean(Constants.SHOW_24) ? "HH:mm" : "hh:mm";
                String formattedTime = new SimpleDateFormat(form, Locale.getDefault()).format(reminder);
                binding.timeText.setText(formattedTime);
            }
        });

        timePicker.show(getSupportFragmentManager(), "timePicker");
    }

    private boolean valid() {
        ArrayList<String> days = getDays();
        if (binding.name.getEditText().getText().toString().isEmpty()){
            Toast.makeText(this, getString(R.string.please_add_name_for_the_routine), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (days.size() == 0){
            Toast.makeText(this, getString(R.string.please_add_at_least_one_day), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (list.size() == 0){
            Toast.makeText(this, getString(R.string.please_add_at_least_one_step), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private ArrayList<String> getDays() {
        ArrayList<String> day = new ArrayList<>();
        for (int i = 0; i < binding.days.getChildCount(); i++) {
            Chip chip = (Chip) binding.days.getChildAt(i);
            if (chip.isChecked()){
                day.add(chip.getText().toString());
            }
        }
        return day;
    }

    @Override
    public void onBottomSheetDismissed() {
        getData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
        updateViews();
    }

    private void updateViews() {
        binding.save.setBackgroundColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.addReminder.setStrokeColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.addReminder.setCardBackgroundColor(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text)));
        binding.addSteps.setStrokeColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.addSteps.setCardBackgroundColor(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text)));
        binding.timeText.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.totalTime.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.name.getEditText().setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.context.getEditText().setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.addIco.setImageTintList(ColorStateList.valueOf(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light))));

        binding.monday.setCheckedIconTint(ColorStateList.valueOf(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light))));
        binding.monday.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.monday.setChipBackgroundColor(ColorStateList.valueOf(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text))));

        binding.tuesday.setCheckedIconTint(ColorStateList.valueOf(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light))));
        binding.tuesday.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.tuesday.setChipBackgroundColor(ColorStateList.valueOf(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text))));

        binding.wednesday.setCheckedIconTint(ColorStateList.valueOf(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light))));
        binding.wednesday.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.wednesday.setChipBackgroundColor(ColorStateList.valueOf(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text))));

        binding.thursday.setCheckedIconTint(ColorStateList.valueOf(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light))));
        binding.thursday.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.thursday.setChipBackgroundColor(ColorStateList.valueOf(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text))));

        binding.friday.setCheckedIconTint(ColorStateList.valueOf(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light))));
        binding.friday.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.friday.setChipBackgroundColor(ColorStateList.valueOf(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text))));

        binding.sat.setCheckedIconTint(ColorStateList.valueOf(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light))));
        binding.sat.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.sat.setChipBackgroundColor(ColorStateList.valueOf(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text))));

        binding.sun.setCheckedIconTint(ColorStateList.valueOf(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light))));
        binding.sun.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.sun.setChipBackgroundColor(ColorStateList.valueOf(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text))));
    }

    private void getData() {
        list = Stash.getArrayList(Constants.Steps, AddStepsChildModel.class);
        if (list.size() >= 1){
            binding.stepsRC.setVisibility(View.VISIBLE);
            binding.totalTime.setVisibility(View.VISIBLE);
            int min = 0;
            List<Integer> timeValues = Constants.extractTimeValues(list);
            for (int value : timeValues) {
                min += value;
                minute = min;
            }
            String formattedTime =  getString(R.string.total_time) + " " + Constants.convertMinutesToHHMM(min) + "h";
            binding.totalTime.setText(formattedTime);
        }

        adapter = new AddStepsChildAdapter(CustomRoutineActivity.this, list, null);
        binding.stepsRC.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {
        Stash.clear(Constants.Steps);
        startActivity(new Intent(this, AddActivity.class));
        finish();
    }
}