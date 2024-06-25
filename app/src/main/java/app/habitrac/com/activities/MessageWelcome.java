package app.habitrac.com.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import app.habitrac.com.MainActivity;
import app.habitrac.com.R;
import app.habitrac.com.SplashScreenActivity;
import app.habitrac.com.WelcomeActivity;
import app.habitrac.com.utils.Constants;

public class MessageWelcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_welcome);

        new Handler().postDelayed(() -> {

                startActivity(new Intent(MessageWelcome.this, FisrtWelcomeQ.class));
                finish();

        }, 4000);

    }
}