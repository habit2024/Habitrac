package app.habitrac.com.ui.gallery;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.card.MaterialCardView;

import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;

import app.habitrac.com.LoginActivity;
import app.habitrac.com.R;
import app.habitrac.com.activities.AddActivity;
import app.habitrac.com.activities.FisrtWelcomeQ;
import app.habitrac.com.activities.MessageWelcome;
import app.habitrac.com.activities.SignUpActivity;
import app.habitrac.com.databinding.FragmentGalleryBinding;
import app.habitrac.com.fragments.HomeFragment;
import app.habitrac.com.fragments.InsightsFragment;
import app.habitrac.com.fragments.InsightsFragmentup;

public class GalleryFragment extends Fragment {

    private FrameLayout container; // Reference to the FrameLayout
    private MaterialCardView add;
    private ImageView change,hom;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        this.container = view.findViewById(R.id.framelayout); // Get the FrameLayout reference
        add= view.findViewById(R.id.add);
        change= view.findViewById(R.id.insights);
        hom= view.findViewById(R.id.home);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        // Determine how to display content within the FrameLayout:

        // Option 1: Predefined Fragment to Display (Static Inflation)

        add.setOnClickListener(v -> {
              startActivity(new Intent(getContext(), AddActivity.class));
            });
        Fragment childFragment = new HomeFragment();  // Replace with your actual fragment class

        // Add the child fragment to the FrameLayout
        getChildFragmentManager().beginTransaction() // Use getChildFragmentManager()
                .add(R.id.framelayout, childFragment)
                .commit();

        Fragment childFragment2 = new InsightsFragmentup();  // Replace with your actual fragment class

        // Add the child fragment to the FrameLayout
        getChildFragmentManager().beginTransaction() // Use getChildFragmentManager()
                .add(R.id.framelayout2, childFragment2)
                .commit();
       /* hom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), FisrtWelcomeQ.class));

            }
        });*/
        hom.setOnClickListener(v -> getChildFragmentManager().beginTransaction().replace(R.id.framelayout, new HomeFragment()).commit());
        change.setOnClickListener(v -> getChildFragmentManager().beginTransaction().replace(R.id.framelayout, new InsightsFragment()).commit());
        // Option 2: Dynamic Fragment Inflation Based on User Interaction or Logic

        // ... (Implement your specific logic here)
        // ... (Use getChildFragmentManager() and transaction methods to add fragments as needed)

        // Option 3: Custom View Inflation Within the FrameLayout (Potentially for Static Content)


    }




}