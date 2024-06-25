package app.habitrac.com;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;

import androidx.appcompat.app.AppCompatActivity;

import android.view.Window;
import android.view.WindowManager;



public class SplashScreen extends AppCompatActivity {

    DatabaseReference dbUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen2);
        Intent intent = new Intent(SplashScreen.this, MainActivity.class); // Replace with your actual dashboard activity class
        startActivity(intent);

    }


}
