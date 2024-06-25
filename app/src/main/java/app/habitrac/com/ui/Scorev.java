package app.habitrac.com.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TabHost;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;

import app.habitrac.com.R;

public class Scorev extends Fragment  {

    private FrameLayout container,container2; // Reference to the FrameLayout
    private MaterialCardView add;
    private TabHost tabHost;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_score, container, false);
  // Get the FrameLayout reference
        this.container = view.findViewById(R.id.tab_one_container);
        this.container2 = view.findViewById(R.id.tab_one_container);
        tabHost = (TabHost) view.findViewById(R.id.tab_host);
        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("Mis logros");
        tabSpec.setContent(R.id.tab_one_container);
        tabSpec.setIndicator("Mis logros");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("Mis recompecabezas");
        tabSpec.setContent(R.id.tab_two_container);
        tabSpec.setIndicator("Mis recompecabezas");
        tabHost.addTab(tabSpec);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                switch (s) {
                    case "Mis logros":
                        // Load content for Tab 1
                        break;

                    case "Mis recompecabezas":
                        // Load content for Tab 2
                        break;
                }
            }
        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Fragment childFragment = new Logrosfragment();  // Replace with your actual fragment class

        // Add the child fragment to the FrameLayout
        getChildFragmentManager().beginTransaction() // Use getChildFragmentManager()
                .add(R.id.tab_one_container, childFragment)
                .commit();

        Fragment childFragmentr = new rompeC_fragment();  // Replace with your actual fragment class

        // Add the child fragment to the FrameLayout
        getChildFragmentManager().beginTransaction() // Use getChildFragmentManager()
                .add(R.id.tab_two_container, childFragmentr)
                .commit();






    }

}

