package app.habitrac.com.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import app.habitrac.com.MainActivity;
import app.habitrac.com.R;
import app.habitrac.com.SplashScreenActivity;
import app.habitrac.com.databinding.ActivitySettingBinding;
import app.habitrac.com.utils.Constants;

public class SettingActivity extends AppCompatActivity {
    ActivitySettingBinding binding;
    int currentTheme;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        int theme = Stash.getInt(Constants.THEME);
        Log.d("COLOR123", theme+"");
        setTheme(theme);
        setContentView(binding.getRoot());
        
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

        Constants.changeTheme(this);

        currentTheme = theme;

        binding.toolbar.tittle.setText(getString(R.string.settings));
        Constants.initDialog(this);

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        binding.colorPicker.setSelectedColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));

        binding.time24Switch.setOn(Stash.getBoolean(Constants.SHOW_24, false));
        binding.darkSwitch.setOn(Stash.getBoolean(Constants.DARK_MODE, false));

        binding.time24Switch.setOnToggledListener((toggleableView, isOn) -> Stash.put(Constants.SHOW_24, isOn));
        binding.darkSwitch.setOnToggledListener((toggleableView, isOn) -> {
            Stash.put(Constants.DARK_MODE, isOn);
            onBackPressed();
        });

        if (Stash.getBoolean(Constants.LANGUAGE, true)){
            binding.defaultLanguage.setText(getString(R.string.english));
        } else {
            binding.defaultLanguage.setText(getString(R.string.spanish));
        }

        binding.language.setOnClickListener(v -> {
            startActivity(new Intent(this, LanguageActivity.class));
            finish();
        });

        binding.pro.setOnClickListener(v -> {
            startActivity(new Intent(this, PaymentActivity.class));
        });

        binding.contactInstagram.setOnClickListener(v -> {
            String instagramUsername = "ikervitoriapsicologo";
            if ( isInstagramAppInstalled()) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://instagram.com/_u/" + instagramUsername));
                intent.setPackage("com.instagram.android");
                startActivity(intent);
            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://instagram.com/" + instagramUsername));
                startActivity(intent);
            }
        });

        binding.contactEmail.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"contacto@ikervitoria.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "");
            startActivity(Intent.createChooser(intent, ""));
        });

        binding.colorPicker.setOnColorSelectedListener(color -> {
            switch (color) {
                case -9409538:
                    currentTheme = R.style.Theme_RoutineApp;
                    Stash.put(Constants.COLOR, getResources().getColor(R.color.light));
                    Stash.put(Constants.COLOR_TEXT, getResources().getColor(R.color.text));
                    Log.d("COLOR123", currentTheme+"Light");
                    break;
                case -34401:
                    currentTheme = R.style.Theme_Pink;
                    Log.d("COLOR123", currentTheme+"PINK");
                    Stash.put(Constants.COLOR, getResources().getColor(R.color.pink));
                    Stash.put(Constants.COLOR_TEXT, getResources().getColor(R.color.text_pink));
                    break;
                case -14297152:
                    currentTheme = R.style.Theme_SeaGreen;
                    Log.d("COLOR123", currentTheme+"");
                    Stash.put(Constants.COLOR, getResources().getColor(R.color.seeGreen));
                    Stash.put(Constants.COLOR_TEXT, getResources().getColor(R.color.text_seagreen));
                    break;
                case -11561105:
                    currentTheme = R.style.Theme_Green;
                    Stash.put(Constants.COLOR, getResources().getColor(R.color.green));
                    Stash.put(Constants.COLOR_TEXT, getResources().getColor(R.color.text_green));
                    Log.d("COLOR123", currentTheme+"");
                    break;
                case -7785801:
                    currentTheme = R.style.Theme_Purple;
                    Log.d("COLOR123", currentTheme+"");
                    Stash.put(Constants.COLOR, getResources().getColor(R.color.purple));
                    Stash.put(Constants.COLOR_TEXT, getResources().getColor(R.color.text_purple));
                    break;
                case -16571060:
                    currentTheme = R.style.Theme_Blue;
                    Log.d("COLOR123", currentTheme+"");
                    Stash.put(Constants.COLOR, getResources().getColor(R.color.blue));
                    Stash.put(Constants.COLOR_TEXT, getResources().getColor(R.color.text_blue));
                    break;
            }

            Log.d("COLOR123", color+"\t\tcolor");
            Stash.put(Constants.THEME, currentTheme);
            recreate();
        });

        binding.faq1.setOnClickListener(v -> {
            int vis = binding.faq1Text.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
            binding.faq1Text.setVisibility(vis);
        });

        binding.faq2.setOnClickListener(v -> {
            int vis = binding.faq2Text.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
            binding.faq2Text.setVisibility(vis);
        });

        binding.faq3.setOnClickListener(v -> {
            int vis = binding.faq3Text.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
            binding.faq3Text.setVisibility(vis);
        });

        binding.faq4.setOnClickListener(v -> {
            int vis = binding.faq4Text.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
            binding.faq4Text.setVisibility(vis);
        });

        binding.faq5.setOnClickListener(v -> {
            int vis = binding.faq5Text.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
            binding.faq5Text.setVisibility(vis);
        });

        binding.faq6.setOnClickListener(v -> {
            int vis = binding.faq6Text.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
            binding.faq6Text.setVisibility(vis);
        });

        binding.logout.setOnClickListener(v ->{
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.logout))
                    .setMessage(getString(R.string.do_you_really_want_to_logout))
                    .setNegativeButton(getString(R.string.no), ((dialogInterface, i) -> dialogInterface.dismiss()))
                    .setPositiveButton(getString(R.string.yes), ((dialogInterface, i) -> {
                        Constants.auth().signOut();
                        startActivity(new Intent(this, SplashScreenActivity.class));
                        finish();
                    }))
                    .show();
        });

        binding.deleteAll.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.delete_all_data_irreversible))
                    .setMessage(getString(R.string.do_you_really_want_to_delete))
                    .setNegativeButton(getString(R.string.no), ((dialogInterface, i) -> dialogInterface.dismiss()))
                    .setPositiveButton(getString(R.string.yes), ((dialogInterface, i) -> {
                        deleteData();
                    }))
                    .show();
        });

    }

    private void deleteData() {
        Constants.showDialog();
        Constants.databaseReference().child(Constants.Routines).child(Constants.auth().getCurrentUser().getUid()).removeValue()
                .addOnSuccessListener(unused -> {
                    Constants.databaseReference().child(Constants.HISTORY).child(Constants.auth().getCurrentUser().getUid()).removeValue()
                            .addOnSuccessListener(unused1 -> {
                                Constants.dismissDialog();
                                Toast.makeText(this, getString(R.string.all_data_deleted_successfully), Toast.LENGTH_SHORT).show();
                            }).addOnFailureListener(e -> {
                                Constants.dismissDialog();
                                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateViews();
    }

    private void updateViews() {
        binding.text1.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.text2.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.text3.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.text4.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.text5.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.text6.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.text7.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.text8.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.text9.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.text10.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.text11.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.text12.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.languageTile.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.logout.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.logout.setBackgroundColor(Stash.getInt(Constants.COLOR_TEXT, getResources().getColor(R.color.text)));

        binding.time24Switch.setColorBorder(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.time24Switch.setColorOn(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.darkSwitch.setColorBorder(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.darkSwitch.setColorOn(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
    }

    private boolean isInstagramAppInstalled() {
        try {
            getPackageManager().getPackageInfo("com.instagram.android", 0);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}