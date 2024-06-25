package app.habitrac.com;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
//import androidx.navigation.NavController;
//import androidx.navigation.Navigation;
//import androidx.navigation.ui.AppBarConfiguration;
//import androidx.navigation.ui.NavigationUI;
//import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.habitrac.com.databinding.ActivityMain2Binding;

public class MainActivity2 extends AppCompatActivity {
    SharedPreferences prefs ;
    private boolean isFirstLaunch;
    //private AppBarConfiguration mAppBarConfiguration;
    private ActivityMain2Binding binding;
    private RadioGroup question1RadioGroup;
    private EditText question2EditText;
    private SeekBar question3SeekBar;
    private Button submit_buttont;
    public TextView name,lastname,email,tvName;
    private RadioGroup question5RadioGroup;
    private ImageView img;
    private DatabaseReference rfUser = FirebaseDatabase.getInstance().getReference().child("usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    /*    binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;

        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        getUserImageUrl(uid);
        loadUserData();

        // Check for first launch after initialization
        isFirstLaunch = prefs.getBoolean("first_launch", true);


        if (isFirstLaunch) {
            // Show survey layout
            setContentView(R.layout.cuestion_layout); // Replace with your actual survey layout ID

            // Initialize survey UI elements
            question1RadioGroup = findViewById(R.id.question1_radio_group);
           // question2EditText = findViewById(R.id.question2_edit_text);
            question3SeekBar = findViewById(R.id.question3_seek_bar);



            // Handle submit button click
            findViewById(R.id.submit_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Save survey responses
                    //saveSurveyResponses();
                    prefs.edit().putBoolean("first_launch", false).apply();
                    // Start dashboard activity
                    Intent intent = new Intent(MainActivity2.this, SplashScreen.class); // Replace with your actual dashboard activity class
                    startActivity(intent);

                    // Update first launch flag

                }
            });
        } else {
           // Intent intent = new Intent(MainActivity.this, MainActivity.class); // Replace with your actual dashboard activity class
           // startActivity(intent);

        }*/
    }

    private void loadUserData() {

        rfUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                //  name.setText("" + dataSnapshot.child("nombre").getValue().toString());
                //   email.setText("" + dataSnapshot.child("email").getValue().toString());

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getUserImageUrl(String uid) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        databaseRef.child("usuariosimg").child(uid).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ImageUser imageUser = dataSnapshot.getValue(ImageUser.class);
                    String imageUrl = imageUser.getAvatar();
                    // Now you have the image URL, use it to display the image
                    displayImage(imageUrl);
                } else {
                    // Handle the case where no image is found for the user
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle any errors during data retrieval
            }
        });
    }

    private void displayImage(String imageUrl) {
     //   Glide.with(this)
       //         .load(imageUrl)
        //        .transform(new CircleCrop())
         //       .into(img); // Replace profileImageView with your actual ImageView
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        name = (TextView) findViewById(R.id.namedraw);
        email = (TextView) findViewById(R.id.emaildraw);
        img = (ImageView) findViewById(R.id.imagert);
        return true;
    }

   /* @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }*/


    public Context getFragmentContext() {
        return this;
    }

}