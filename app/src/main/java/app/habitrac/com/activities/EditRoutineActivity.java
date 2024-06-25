package app.habitrac.com.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

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
import app.habitrac.com.adapters.AddStepsChildAdapter;
import app.habitrac.com.bottomsheets.AddStepsFragment;
import app.habitrac.com.databinding.ActivityEditRoutineBinding;
import app.habitrac.com.listners.BottomSheetDismissListener;
import app.habitrac.com.models.AddStepsChildModel;
import app.habitrac.com.models.CompletedDaysModel;
import app.habitrac.com.models.RoutineModel;
import app.habitrac.com.models.StepsLocalModel;
import app.habitrac.com.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class EditRoutineActivity extends AppCompatActivity implements BottomSheetDismissListener {
    ActivityEditRoutineBinding binding;
    List<String> context;
    ArrayAdapter<String> partiesAdapter;
    ArrayList<AddStepsChildModel> list;
    AddStepsChildAdapter adapter;
    int minute = 0;
    long reminder = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditRoutineBinding.inflate(getLayoutInflater());
        int theme = Stash.getInt(Constants.THEME);
        setTheme(theme);
        Constants.changeTheme(this);
        setContentView(binding.getRoot());
        binding.toolbar.tittle.setText(getString(R.string.edit_routine));
        Constants.initDialog(this);

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        binding.toolbar.delete.setVisibility(View.VISIBLE);

        RoutineModel model = (RoutineModel) Stash.getObject(Constants.MODEL, RoutineModel.class);


        if (Stash.getBoolean(Constants.LANGUAGE, true)){
            Constants.setLocale(getBaseContext(), Constants.EN);
        } else {
            Constants.setLocale(getBaseContext(), Constants.ES);
        }

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        if (!Stash.getBoolean(Constants.IS_VIP)){
            Stash.put(Constants.IS_VIP, false);

        }

        binding.toolbar.delete.setOnClickListener(v -> {
            new AlertDialog.Builder(this).setTitle(getString(R.string.delete_routine))
                    .setMessage(getString(R.string.do_you_really_want_to_delete_this_routine))
                    .setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        Constants.showDialog();

                        Constants.databaseReference().child(Constants.Routines).child(Constants.auth().getCurrentUser().getUid())
                                .child(model.getID()).removeValue().addOnSuccessListener(unused -> {
                                    Constants.dismissDialog();
                                    Toast.makeText(this, getString(R.string.routine_deleted), Toast.LENGTH_SHORT).show();
                                    finish();
                                }).addOnFailureListener(e -> {
                                    Constants.dismissDialog();
                                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                });

                    }).setNegativeButton(getString(R.string.no), (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    }).show();
        });

        binding.name.getEditText().setText(model.getName());
        binding.context.getEditText().setText(model.getContext());

        context = new ArrayList<>();
        String[] stringArray = getResources().getStringArray(R.array.my_context_array);
        context.addAll(Arrays.asList(stringArray));

        binding.stepsRC.setLayoutManager(new LinearLayoutManager(this));
        binding.stepsRC.setHasFixedSize(false);

        partiesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, context);
        binding.addContext.setAdapter(partiesAdapter);

        list = new ArrayList<>();
        list.addAll(model.getSteps());

        Stash.put(Constants.Steps, list);

        if (list.size() > 1){
            binding.stepsRC.setVisibility(View.VISIBLE);
            binding.totalTime.setVisibility(View.VISIBLE);
            int min = 0;
            List<Integer> timeValues = Constants.extractTimeValues(list);
            for (int value : timeValues) {
                min += value;
                minute = min;
            }
            String formattedTime = getString(R.string.total_time) + " " + Constants.convertMinutesToHHMM(min) + "h";
            binding.totalTime.setText(formattedTime);
        }

        adapter = new AddStepsChildAdapter(EditRoutineActivity.this, list, m -> {

        });
        binding.stepsRC.setAdapter(adapter);

        for (int i = 0; i < model.getDays().size(); i++) {
            for (int j = 0; j < binding.days.getChildCount(); j++) {
                String name = model.getDays().get(i);
                Chip chip = (Chip) binding.days.getChildAt(j);
                if (name.equals(chip.getText().toString())){
                    chip.setChecked(true);
                }
            }
        }

        binding.addSteps.setOnClickListener(v -> {
            AddStepsFragment bottomSheetFragment = new AddStepsFragment();
            bottomSheetFragment.setListener(this);
            bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
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

        binding.addReminder.setOnClickListener(v -> {
            openTimePicker();
        });

        binding.save.setOnClickListener(v -> {
            if (valid()){
                Constants.showDialog();
                String ID = model.getID();
                ArrayList<String> days = getDays();
                CompletedDaysModel daysCompleted = model.getDaysCompleted();
                RoutineModel updateModel = new RoutineModel(
                        ID, binding.name.getEditText().getText().toString(), binding.context.getEditText().getText().toString(),
                        minute, days, daysCompleted, list, reminder
                );
                Constants.databaseReference().child(Constants.Routines).child(Constants.auth().getCurrentUser().getUid())
                        .child(ID).setValue(updateModel).addOnSuccessListener(unused -> {
                            Constants.dismissDialog();
                            ArrayList<StepsLocalModel> localList = new ArrayList<>();
                            for(AddStepsChildModel childModel : list){
                                localList.add(new StepsLocalModel(ID, childModel.getName(), childModel.getTime(), false));
                            }
                            Stash.put(ID, localList);
                            Stash.clear(Constants.Steps);
                            Toast.makeText(this, getString(R.string.routine_updated), Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }).addOnFailureListener(e -> {
                            Constants.dismissDialog();
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
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

    private void getData() {
        list.clear();
        list = Stash.getArrayList(Constants.Steps, AddStepsChildModel.class);
        if (list.size() > 1){
            binding.stepsRC.setVisibility(View.VISIBLE);
            binding.totalTime.setVisibility(View.VISIBLE);
            int min = 0;
            List<Integer> timeValues = Constants.extractTimeValues(list);
            for (int value : timeValues) {
                min += value;
                minute = min;
            }
            String formattedTime = getString(R.string.total_time) + " " + Constants.convertMinutesToHHMM(min) + "h";
            binding.totalTime.setText(formattedTime);
        }

        adapter = new AddStepsChildAdapter(EditRoutineActivity.this, list, model -> {

        });
        binding.stepsRC.setAdapter(adapter);
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

    @Override
    public void onBottomSheetDismissed() {
        getData();
    }

    @Override
    public void onBackPressed() {
        Stash.clear(Constants.Steps);
        startActivity(new Intent(this, RoutineStartActivity.class));
        finish();
    }
}