package app.habitrac.com;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.fxn.stash.Stash;
import app.habitrac.com.databinding.ActivitySplashScreenBinding;
import app.habitrac.com.utils.Constants;

public class SplashScreenActivity extends AppCompatActivity {
    ActivitySplashScreenBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        int theme = Stash.getInt(Constants.THEME);
        setTheme(theme);
        setContentView(binding.getRoot());

        Constants.changeTheme(this);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.bg.setBackgroundColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));

        new Handler().postDelayed(() -> {
            if (Constants.auth().getCurrentUser() == null){
                startActivity(new Intent(SplashScreenActivity.this, WelcomeActivity.class));
                finish();
            } else {
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                finish();
            }
        }, 2000);

    }
}