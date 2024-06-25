package app.habitrac.com.ui.calen;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;

import app.habitrac.com.R;
import app.habitrac.com.activities.AddActivity;
import app.habitrac.com.fragments.HomeFragment;
import app.habitrac.com.fragments.InsightsFragment;
import app.habitrac.com.fragments.InsightsFragmentup;

public class CalenFragment extends Fragment implements CalendarView.OnDateChangeListener {

    private FrameLayout container; // Reference to the FrameLayout
    private MaterialCardView add;
    private ImageView change,hom;
    private CalendarView calendarView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calen, container, false);
        //this.container = view.findViewById(R.id.framelayout); // Get the FrameLayout reference
        calendarView = view.findViewById(R.id.calendar_view);
        calendarView.setOnDateChangeListener(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




        Fragment childFragment = new HomeFragment();  // Replace with your actual fragment class

        // Add the child fragment to the FrameLayout
        getChildFragmentManager().beginTransaction() // Use getChildFragmentManager()
                .add(R.id.framelayout, childFragment)
                .commit();






    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
        String selectedDate = String.format("%d-%d-%d", i, i1 + 1, i2);
        Toast.makeText(getActivity(), "Día Seleccionado: " + selectedDate, Toast.LENGTH_SHORT).show();
    }
}
