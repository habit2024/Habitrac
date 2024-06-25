package app.habitrac.com;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.PurchaseInfo;
import com.anjlab.android.iab.v3.SkuDetails;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.fxn.stash.Stash;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import app.habitrac.com.databinding.ActivityMainBinding;
import app.habitrac.com.fragments.HomeFragment;
import app.habitrac.com.fragments.InsightsFragment;
import app.habitrac.com.activities.AddActivity;
import app.habitrac.com.activities.SettingActivity;
import app.habitrac.com.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler {
    ActivityMainBinding binding;
    BillingProcessor bp;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        int theme = Stash.getInt(Constants.THEME);
        setTheme(theme);
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;

        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_home, R.id.nav_close,R.id.addn,R.id.calen,
                R.id.logros )
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_close:
                        singOut();
                        break;
                    case R.id.addn:
                        startActivity(new Intent(MainActivity.this, AddActivity.class));
                        finish();
                        break;
                    case R.id.nav_slideshow:
                        // Use Navigation action to navigate to FragmentSlideshow
                        NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_content_main);
                        navController.navigate(R.id.nav_slideshow); // Replace with your slideshow fragment action ID
                        drawer.close();
                        break;
                    case R.id.nav_gallery:
                        // Use Navigation action to navigate to FragmentSlideshow
                        NavController navControllerg = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_content_main);
                        navControllerg.navigate(R.id.nav_gallery); // Replace with your slideshow fragment action ID
                        drawer.close();
                        break;
                    case R.id.calen:
                        // Use Navigation action to navigate to FragmentSlideshow
                        NavController navControllerc = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_content_main);
                        navControllerc.navigate(R.id.calen); // Replace with your slideshow fragment action ID
                        drawer.close();
                        break;
                    case R.id.logros:
                        // Use Navigation action to navigate to FragmentSlideshow
                        NavController navControllerl = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_content_main);
                        navControllerl.navigate(R.id.logros); // Replace with your slideshow fragment action ID
                        drawer.close();
                        break;
                    default:
                        // Handle other navigation if needed
                        break;
                }

                return false;
            }
        });


        Constants.changeTheme(this);
        Constants.checkApp(this);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        if (Stash.getBoolean(Constants.LANGUAGE, true)){
            Constants.setLocale(getBaseContext(), Constants.EN);
        } else {
            Constants.setLocale(getBaseContext(), Constants.ES);
        }


        bp = BillingProcessor.newBillingProcessor(this, Constants.LICENSE_KEY, this);
        bp.initialize();

        ArrayList<String> ids = new ArrayList<>();
        ids.add(Constants.VIP_MONTH);
        ids.add(Constants.VIP_LIFE);
        bp.getSubscriptionsListingDetailsAsync(ids, new BillingProcessor.ISkuDetailsResponseListener() {
            @Override
            public void onSkuDetailsResponse(@Nullable List<SkuDetails> products) {
                Log.d("PURSS", "Size : " + products.size());
                for (int i = 0; i < products.size(); i++){
                    boolean isSub = products.get(i).isSubscription;
                    Stash.put(Constants.IS_VIP, isSub);
                }
            }

            @Override
            public void onSkuDetailsError(String error) {

            }
        });
        Log.d("PURSS", "init : " + bp.isInitialized());

        if (bp.isSubscribed(Constants.VIP_MONTH) || bp.isSubscribed(Constants.VIP_LIFE)){
            Stash.put(Constants.IS_VIP, true);
          //  binding.addLayout.setVisibility(View.GONE);
        } else {
            Stash.put(Constants.IS_VIP, false);
            //showAd();
        }



     // binding.home.setOnClickListener(v -> getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new HomeFragment()).commit());
      //  binding.insights.setOnClickListener(v -> getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new InsightsFragment()).commit());

      /* binding.settings.setOnClickListener(v -> {
            startActivity(new Intent(this, SettingActivity.class));
            finish();
        });*/
     //  binding.add.setOnClickListener(v -> {
      //    startActivity(new Intent(this, AddActivity.class));
     //   finish();
      //  });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS);
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
        }

    }

    private void singOut() {
        Constants.auth().signOut();
        startActivity(new Intent(this, SplashScreenActivity.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
     //   binding.add.setCardBackgroundColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));

    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable PurchaseInfo details) {

    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {

    }

    @Override
    public void onBillingInitialized() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       DatabaseReference rfUser = FirebaseDatabase.getInstance().getReference().child("Habitrac").child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        TextView name = (TextView) findViewById(R.id.namedraw);
        TextView  email = (TextView) findViewById(R.id.emaildraw);
       ImageView img = (ImageView) findViewById(R.id.imagert);
        rfUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



                   name.setText("" + dataSnapshot.child("name").getValue().toString());
                    email.setText("" + dataSnapshot.child("email").getValue().toString());
                   email.setText("" + dataSnapshot.child("email").getValue().toString());



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();


        String uid = currentUser.getUid();



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

       // Replace profileImageView with your actual ImageView
        return true;
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
        ImageView img = (ImageView) findViewById(R.id.imagert);
        Glide.with(this)
                .load(imageUrl)
                .transform(new CircleCrop())
                .into(img); // Replace profileImageView with your actual ImageView
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    public Context getFragmentContext() {
        return this;
    }
}