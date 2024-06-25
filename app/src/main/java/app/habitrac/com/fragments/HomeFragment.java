package app.habitrac.com.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.google.common.eventbus.Subscribe;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import app.habitrac.com.HabitMapper;
import app.habitrac.com.MainActivity;
import app.habitrac.com.R;
import app.habitrac.com.RoutineHabitMapper;
import app.habitrac.com.SplashScreenActivity;
import app.habitrac.com.activities.CuestionsDaily;
import app.habitrac.com.activities.CustomRoutineActivity;
import app.habitrac.com.activities.FisrtWelcomeQ;
import app.habitrac.com.activities.TimerActivity;
import app.habitrac.com.adapters.RoutineAdapter;
import app.habitrac.com.databinding.FragmentHomeBinding;
import app.habitrac.com.models.AddStepsChildModel;
import app.habitrac.com.models.CompletedDaysModel;
import app.habitrac.com.models.RoutineModel;
import app.habitrac.com.models.StepsLocalModel;
import app.habitrac.com.notification.NotificationHelper;
import app.habitrac.com.ui.gallery.HabitDisplayListener;
import app.habitrac.com.ui.gallery.HabitEvent;
import app.habitrac.com.utils.Constants;

import java.util.ArrayList;
import java.util.Calendar;

public class HomeFragment extends Fragment implements HabitDisplayListener {

    FragmentHomeBinding binding;
    ArrayList<RoutineModel> list;
    RoutineAdapter adapter;
    Context context;
    Handler handler = new Handler();
    public HomeFragment() {
        // Required empty public constructor
    }



    @Subscribe // Annotation for the event bus library
    public void onHabitReceived(HabitEvent event) {
        // Update UI to display the habit (e.g., using a TextView)

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(getLayoutInflater(), container, false);
        context = requireContext();
        Constants.initDialog(context);

        String today = Constants.getToday();
        updateCalender(today);

        list = new ArrayList<>();

        binding.monday.setOnClickListener(v -> updateClick("Mon"));
        binding.tuesday.setOnClickListener(v -> updateClick("Tue"));
        binding.wednessday.setOnClickListener(v -> updateClick("Wed"));
        binding.thursday.setOnClickListener(v -> updateClick("Thu"));
        binding.friday.setOnClickListener(v -> updateClick("Fri"));
        binding.saturday.setOnClickListener(v -> updateClick("Sat"));
        binding.sunday.setOnClickListener(v -> updateClick("Sun"));

        binding.routineRC.setLayoutManager(new LinearLayoutManager(context));
        binding.routineRC.setHasFixedSize(false);

        binding.inCompleted.setOnClickListener(v -> {
            boolean show = binding.routineRC.getVisibility() == View.VISIBLE;

            if (show) {
                binding.routineRC.setVisibility(View.GONE);
                binding.inCompleted.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.upload, 0);
            } else {
                binding.routineRC.setVisibility(View.VISIBLE);
                binding.inCompleted.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.download, 0);
            }

        });

        binding.completed.setOnClickListener(v -> {
            boolean show = binding.routineRC.getVisibility() == View.VISIBLE;

            if (show) {
                binding.routineRCCompleted.setVisibility(View.GONE);
                binding.completed.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.upload, 0);
            } else {
                binding.routineRCCompleted.setVisibility(View.VISIBLE);
                binding.completed.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.download, 0);
            }
        });


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (Stash.getBoolean(Constants.LANGUAGE, true)) {
            Constants.setLocale(context, Constants.EN);
        } else {
            Constants.setLocale(context, Constants.ES);
        }
    }

    ArrayList<Long> times = new ArrayList<>();

    @Override
    public void onResume() {
        super.onResume();
        updateViews();
        if (Constants.isInternetConnected(context)) {
            getData();
        } else {
            Toast.makeText(context, "Internet no conectado", Toast.LENGTH_SHORT).show();
        }

    }

    private void updateViews() {
        binding.monText.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.tueText.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.wedText.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.thuText.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.friText.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.satText.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.sunText.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
    }

    private void getData() {
        Constants.showDialog();
        Constants.databaseReference().child(Constants.Routines).child(Constants.auth().getCurrentUser().getUid())
                .get().addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        list.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                            RoutineModel model = dataSnapshot.getValue(RoutineModel.class);

                            RoutineModel model = new RoutineModel();
                            String ID = dataSnapshot.child("id").getValue(String.class);
                            String name = dataSnapshot.child("name").getValue(String.class);
                            String context = dataSnapshot.child("context").getValue(String.class);
                            int minutes = dataSnapshot.child("minutes").getValue(Integer.class);
                            long reminder = dataSnapshot.child("reminder").getValue(Long.class);

                            ArrayList<String> days = new ArrayList<>();
                            ArrayList<AddStepsChildModel> steps = new ArrayList<>();

                            if (reminder != 0) {
                                times.add(reminder);
                            }

                            for (DataSnapshot day : dataSnapshot.child("days").getChildren()) {
                                String d = day.getValue(String.class);
                                days.add(d);
                            }

                            for (DataSnapshot step : dataSnapshot.child("steps").getChildren()) {
                                AddStepsChildModel d = step.getValue(AddStepsChildModel.class);
                                steps.add(d);
                            }

                            CompletedDaysModel completedDays = dataSnapshot.child("daysCompleted").getValue(CompletedDaysModel.class);

                            model.setID(ID);
                            model.setName(name);
                            model.setContext(context);
                            model.setMinutes(minutes);
                            model.setDays(days);
                            model.setSteps(steps);
                            model.setDaysCompleted(completedDays);
                            model.setReminder(reminder);

                            list.add(model);
                        }


                        if (list.size() > 0) {
                            for (int i = 0; i < list.size(); i++) {
                                RoutineModel model = list.get(i);
                                String ID = model.getID();

                                ArrayList<StepsLocalModel> localList = Stash.getArrayList(ID, StepsLocalModel.class);
                                if (localList.size() == 0) {
                                    for (AddStepsChildModel childModel : model.getSteps()) {
                                        localList.add(new StepsLocalModel(ID, childModel.getName(), childModel.getTime(), false));
                                    }
                                    Stash.put(ID, localList);
                                }
                            }
                        }
                        Runnable showAdapterRunnable = new Runnable() {
                            @Override
                            public void run() {
                                NotificationHelper helper = new NotificationHelper(getActivity());
                                String body =  "Responde tus preguntas diarias" ;
                                helper.sendHighPriorityNotification(getString(R.string.startx), body, CuestionsDaily.class);

                                startActivity(new Intent(getActivity(), CuestionsDaily.class));
                            }
                        };

                        // Program the execution (assuming current time is before 12 PM)
                        Calendar now = Calendar.getInstance();
                        int targetHour = 00;
                        int targetMinute = 00;

                        if (now.get(Calendar.HOUR_OF_DAY) < targetHour ||
                                (now.get(Calendar.HOUR_OF_DAY) == targetHour && now.get(Calendar.MINUTE) < targetMinute)) {
                            // Calculate delay (ensure target time is in the future)
                            now.set(Calendar.HOUR_OF_DAY, targetHour);
                            now.set(Calendar.MINUTE, targetMinute);
                            now.set(Calendar.SECOND, 0);
                            long timeInMillis = now.getTimeInMillis() - System.currentTimeMillis();
                            handler.postDelayed(showAdapterRunnable, timeInMillis);
                        } else {

                        }



                        HabitMapper habitMapper = new RoutineHabitMapper(getActivity());
                        // Mostrar el adaptador
                        adapter = new RoutineAdapter(binding.getRoot().getContext(), getActivity(), list,habitMapper);

                        binding.routineRC.setAdapter(adapter);

                       // adapter = new RoutineAdapter(binding.getRoot().getContext(), getActivity(), list);
                      //  binding.routineRC.setAdapter(adapter);

                        String today = Constants.getToday();
                        if (today.equalsIgnoreCase("Sun")) {
                            today = getString(R.string.sunday);
                        }
                        if (today.equalsIgnoreCase("Mon")) {
                            today = getString(R.string.monday);
                        }
                        if (today.equalsIgnoreCase("Tue")) {
                            today = getString(R.string.tuesday);
                        }
                        if (today.equalsIgnoreCase("Wed")) {
                            today = getString(R.string.wednesday);
                        }
                        if (today.equalsIgnoreCase("Thu")) {
                            today = getString(R.string.thursday);
                        }
                        if (today.equalsIgnoreCase("Fri")) {
                            today = getString(R.string.friday);
                        }
                        if (today.equalsIgnoreCase("Sat")) {
                            today = getString(R.string.saturday);
                        }

                     //   adapter.getFilter().filter(today);
                        Stash.put(Constants.TIME_LIST, times);
                    }
                    if (getActivity() != null) {
                        Constants.dismissDialog();
                    }
                }).addOnFailureListener(e -> {
                    if (getActivity() != null) {
                        Constants.dismissDialog();
                    }
                    Toast.makeText(binding.getRoot().getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void updateClick(String clicked) {

        String today = Constants.getToday();
        if (today.equals(clicked)) {
            binding.tittle.setText(getResources().getString(R.string.today_routines));
        } else {
            if (clicked.equalsIgnoreCase("Sun")) {
                binding.tittle.setText(getResources().getString(R.string.sunday_routines));
            }
            if (clicked.equalsIgnoreCase("Mon")) {
                binding.tittle.setText(getResources().getString(R.string.monday_routines));
            }
            if (clicked.equalsIgnoreCase("Tue")) {
                binding.tittle.setText(getResources().getString(R.string.tuesday_routines));
            }
            if (clicked.equalsIgnoreCase("Wed")) {
                binding.tittle.setText(getResources().getString(R.string.wednesday_routines));
            }
            if (clicked.equalsIgnoreCase("Thu")) {
                binding.tittle.setText(getResources().getString(R.string.thursday_routines));
            }
            if (clicked.equalsIgnoreCase("Fri")) {
                binding.tittle.setText(getResources().getString(R.string.friday_routines));
            }
            if (clicked.equalsIgnoreCase("Sat")) {
                binding.tittle.setText(getResources().getString(R.string.saturday_routines));
            }
        }

        if (clicked.equalsIgnoreCase("Sun")) {
            binding.sunday.setCardBackgroundColor(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text)));
            if (list.size() > 0) {
                adapter.getFilter().filter(getResources().getString(R.string.sunday));
            }
            binding.monday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.tuesday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.wednessday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.thursday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.friday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.saturday.setCardBackgroundColor(getResources().getColor(R.color.white));

        }
        if (clicked.equalsIgnoreCase("Mon")) {
            binding.monday.setCardBackgroundColor(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text)));
            if (list.size() > 0) {
                adapter.getFilter().filter(getResources().getString(R.string.monday));
            }
            binding.sunday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.tuesday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.wednessday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.thursday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.friday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.saturday.setCardBackgroundColor(getResources().getColor(R.color.white));
        }
        if (clicked.equalsIgnoreCase("Tue")) {
            binding.tuesday.setCardBackgroundColor(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text)));
            if (list.size() > 0) {
                adapter.getFilter().filter(getResources().getString(R.string.tuesday));
            }
            binding.monday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.sunday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.wednessday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.thursday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.friday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.saturday.setCardBackgroundColor(getResources().getColor(R.color.white));
        }
        if (clicked.equalsIgnoreCase("Wed")) {
            binding.wednessday.setCardBackgroundColor(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text)));

            if (list.size() > 0) {
                adapter.getFilter().filter(getResources().getString(R.string.wednesday));
            }
            binding.monday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.tuesday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.sunday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.thursday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.friday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.saturday.setCardBackgroundColor(getResources().getColor(R.color.white));
        }
        if (clicked.equalsIgnoreCase("Thu")) {
            binding.thursday.setCardBackgroundColor(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text)));

            if (list.size() > 0) {
                adapter.getFilter().filter(getResources().getString(R.string.thursday));
            }
            binding.monday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.tuesday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.wednessday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.sunday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.friday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.saturday.setCardBackgroundColor(getResources().getColor(R.color.white));
        }
        if (clicked.equalsIgnoreCase("Fri")) {
            binding.friday.setCardBackgroundColor(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text)));

            if (list.size() > 0) {
                adapter.getFilter().filter(getResources().getString(R.string.friday));
            }
            binding.monday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.tuesday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.wednessday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.thursday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.sunday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.saturday.setCardBackgroundColor(getResources().getColor(R.color.white));
        }
        if (clicked.equalsIgnoreCase("Sat")) {
            binding.saturday.setCardBackgroundColor(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text)));

            if (list.size() > 0) {
                adapter.getFilter().filter(getResources().getString(R.string.saturday));
            }
            binding.monday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.tuesday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.wednessday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.thursday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.friday.setCardBackgroundColor(getResources().getColor(R.color.white));
            binding.sunday.setCardBackgroundColor(getResources().getColor(R.color.white));
        }
    }

    private void updateCalender(@NonNull String today) {
        binding.tittle.setText(getString(R.string.today_routines));
        if (today.equalsIgnoreCase("Sun")) {
            binding.sunday.setCardBackgroundColor(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text)));
            binding.sunday.setStrokeColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        }
        if (today.equalsIgnoreCase("Mon")) {
            binding.monday.setCardBackgroundColor(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text)));
            binding.monday.setStrokeColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        }
        if (today.equalsIgnoreCase("Tue")) {
            binding.tuesday.setCardBackgroundColor(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text)));
            binding.tuesday.setStrokeColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        }
        if (today.equalsIgnoreCase("Wed")) {
            binding.wednessday.setCardBackgroundColor(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text)));
            binding.wednessday.setStrokeColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        }
        if (today.equalsIgnoreCase("Thu")) {
            binding.thursday.setCardBackgroundColor(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text)));
            binding.thursday.setStrokeColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        }
        if (today.equalsIgnoreCase("Fri")) {
            binding.friday.setCardBackgroundColor(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text)));
            binding.friday.setStrokeColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        }
        if (today.equalsIgnoreCase("Sat")) {
            binding.saturday.setCardBackgroundColor(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text)));
            binding.saturday.setStrokeColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        }
    }

    @Override
    public void showHabit(String habit) {
        // Update UI to display the habit (e.g., using a TextView)

    }
}