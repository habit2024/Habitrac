package app.habitrac.com.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fxn.stash.Stash;
import app.habitrac.com.MainActivity;
import app.habitrac.com.R;
import app.habitrac.com.databinding.ActivityLanguageBinding;
import app.habitrac.com.utils.Constants;

public class LanguageActivity extends AppCompatActivity {
    ActivityLanguageBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLanguageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Constants.changeTheme(this);


        if (Stash.getBoolean(Constants.LANGUAGE, true)){
            Constants.setLocale(getBaseContext(), Constants.EN);
        } else {
            Constants.setLocale(getBaseContext(), Constants.ES);
        }

        binding.toolbar.tittle.setText(getString(R.string.languages));
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        if (!Stash.getBoolean("backShow")) {
            binding.toolbar.back.setVisibility(View.GONE);
            if (!Stash.getBoolean(Constants.IS_VIP)){
                Stash.put(Constants.IS_VIP, false);
            }
        }

        if (Stash.getBoolean(Constants.LANGUAGE, true)) {
            binding.english.setChecked(true);
        } else {
            binding.spanish.setChecked(true);
        }

        binding.next.setOnClickListener(v -> {
            boolean lang = binding.english.isChecked();
            Stash.put(Constants.LANGUAGE, lang);
            Stash.put("backShow", true);
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.pickOne.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.next.setBackgroundColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, SettingActivity.class));
        finish();
    }
}