package app.habitrac.com.bottomsheets;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fxn.stash.Stash;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import app.habitrac.com.MainActivity;
import app.habitrac.com.R;
import app.habitrac.com.activities.AddCustomStepsActivity;
import app.habitrac.com.activities.CustomRoutineActivity;
import app.habitrac.com.adapters.AddStepsParentAdapter;
import app.habitrac.com.listners.BottomSheetDismissListener;
import app.habitrac.com.listners.StepClickListner;
import app.habitrac.com.models.AddStepsChildModel;
import app.habitrac.com.models.AddStepsModel;
import app.habitrac.com.utils.Constants;

import java.util.ArrayList;

public class AddStepsFragment extends BottomSheetDialogFragment {

    RecyclerView recyler;
    Context context;
    AddStepsParentAdapter adapter;
    ArrayList<AddStepsModel> list;
    private BottomSheetDismissListener listener;
    private CustomRoutineActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Get a reference to the CustomRoutineActivity
        if (context instanceof CustomRoutineActivity) {
            activity = (CustomRoutineActivity) context;
        } else {
            throw new ClassCastException("Must be attached to a CustomRoutineActivity");
        }
    }

    public void checkButtonPress() {
        if (activity != null) {
            SharedPreferences prefs = activity.getSharedPreferences("mis_preferencias", MODE_PRIVATE);
            boolean botonPresionado = prefs.getBoolean("mis_preferencias", true);

            Toast.makeText(context, "dqqq11111", Toast.LENGTH_SHORT).show();
            if (botonPresionado) {
                Toast.makeText(context, "ddddss", Toast.LENGTH_SHORT).show();
                prefs.edit().putBoolean("mis_preferencias", false).apply();
                Stash.put(Constants.STEPS_LIST, "MORNING");
                Intent intent = new Intent(getActivity(), CustomRoutineActivity.class);
                startActivity(intent);
                showAd();
            }
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_steps_fragment, container, false);
        View toolbar = view.findViewById(R.id.toolbar);

        Button custom = view.findViewById(R.id.custom);
        Button savex = view.findViewById(R.id.savex);
        TextView title = toolbar.findViewById(R.id.tittle);
        ImageView back = toolbar.findViewById(R.id.back);

        context = view.getContext();

        back.setImageResource(R.drawable.round_close_24);
        title.setText(getString(R.string.add_steps));
        back.setOnClickListener(v -> {
            dismiss();
        });

        savex.setOnClickListener(v -> {

            showAd();

        });
        this.dismiss();

        custom.setOnClickListener(v -> {
            startActivity(new Intent(view.getContext(), AddCustomStepsActivity.class));
            dismiss();
        });

        custom.setBackgroundColor(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text)));
        custom.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));

        recyler = view.findViewById(R.id.recyler);
        recyler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyler.setHasFixedSize(false);

        list = new ArrayList<>();

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

        adapter = new AddStepsParentAdapter(view.getContext(), list, listner);
        recyler.setAdapter(adapter);

        return view;
    }

    private void morningSteps() {
        ArrayList<AddStepsChildModel> child1 = new ArrayList<>();
        child1.add(new AddStepsChildModel(getString(R.string.make_breakfast), "20 min"));
        child1.add(new AddStepsChildModel(getString(R.string.brush_teeth), "5 min"));
        child1.add(new AddStepsChildModel(getString(R.string.make_bed), "5 min"));
        child1.add(new AddStepsChildModel(getString(R.string.get_sunlight), "10 min"));
        child1.add(new AddStepsChildModel(getString(R.string.stretch), "15 min"));
        child1.add(new AddStepsChildModel(getString(R.string.write_down_goals), "15 min"));
        child1.add(new AddStepsChildModel(getString(R.string.wash_face), "3 min"));
        child1.add(new AddStepsChildModel(getString(R.string.take_shower), "20 min"));
        child1.add(new AddStepsChildModel(getString(R.string.meditate), "45 min"));
        child1.add(new AddStepsChildModel(getString(R.string.drink_water), "1 min"));
        child1.add(new AddStepsChildModel(getString(R.string.make_coffee), "10 min"));
        child1.add(new AddStepsChildModel(getString(R.string.write_grateful), "5 min"));
        list.add(new AddStepsModel(getString(R.string.morning_routine), child1));

    }

    private void eveningSteps() {
        ArrayList<AddStepsChildModel> child2 = new ArrayList<>();
        child2.add(new AddStepsChildModel(getString(R.string.wash_face_2), "1 min"));
        child2.add(new AddStepsChildModel(getString(R.string.write_grateful_2), "5 min"));
        child2.add(new AddStepsChildModel(getString(R.string.reflect_on_day), "10 min"));
        child2.add(new AddStepsChildModel(getString(R.string.read_fiction), "40 min"));
        child2.add(new AddStepsChildModel(getString(R.string.talk_to_family), "30 min"));
        child2.add(new AddStepsChildModel(getString(R.string.meditate_2), "15 min"));
        child2.add(new AddStepsChildModel(getString(R.string.write_down_goals_2), "15 min"));
        child2.add(new AddStepsChildModel(getString(R.string.take_shower_2), "20 min"));
        child2.add(new AddStepsChildModel(getString(R.string.prepare_outfit), "15 min"));
        child2.add(new AddStepsChildModel(getString(R.string.turn_off_electronics), "8 min"));
        child2.add(new AddStepsChildModel(getString(R.string.brush_teeth_2), "5 min"));

        list.add(new AddStepsModel(getString(R.string.evening_routine), child2));
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

        list.add(new AddStepsModel(getString(R.string.ready_for_work_routine), child3));
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

        list.add(new AddStepsModel(getString(R.string.selfcare_routine), child4));
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

        list.add(new AddStepsModel(getString(R.string.study_routine), child5));
    }

    private void addData() {
        morningSteps();
        eveningSteps();
        workSteps();
        selfcareSteps();
        studySteps();

    }

    @Override
    public void onStart() {
        super.onStart();
        // Set BottomSheet behavior to go full screen
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            BottomSheetBehavior.from((View) getView().getParent()).setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (listener != null) {
            listener.onBottomSheetDismissed();

        }
    }

    public void setListener(BottomSheetDismissListener listener) {
        this.listener = listener;
    }
    StepClickListner listner = model -> {
        ArrayList<AddStepsChildModel> list = Stash.getArrayList(Constants.Steps, AddStepsChildModel.class);
        list.add(model);
        Stash.put(Constants.Steps, list);
        // this.dismiss();
    };
    public void showAd() {

        activity.showAdInter(); // Call showAdInter() from the activity

    }


}
