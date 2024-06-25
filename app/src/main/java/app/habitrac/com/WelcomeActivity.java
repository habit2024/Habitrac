package app.habitrac.com;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.fxn.stash.Stash;
import app.habitrac.com.databinding.ActivityWelcomeBinding;
import app.habitrac.com.activities.LoginActivity;
import app.habitrac.com.utils.Constants;

public class WelcomeActivity extends AppCompatActivity {
    ActivityWelcomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        int theme = Stash.getInt(Constants.THEME);
        setTheme(theme);
        setContentView(binding.getRoot());
        Constants.checkApp(this);

        if (Stash.getBoolean(Constants.LANGUAGE, true)){
            Constants.setLocale(getBaseContext(), Constants.EN);
        } else {
            Constants.setLocale(getBaseContext(), Constants.ES);
        }

        Constants.changeTheme(this);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.green));
        window.setStatusBarColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.green)));
        binding.bg.setBackgroundColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.green)));



        binding.continueBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}