package app.habitrac.com.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import app.habitrac.com.ImageDisplay;
import app.habitrac.com.R;
import app.habitrac.com.ui.DoneListener;
import app.habitrac.com.ui.Logrosfragment;

public class LogrosActivity extends AppCompatActivity  {
    private ImageView imagenc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logros); // Replace with your layout resource ID
        imagenc = findViewById(R.id.imagenc);
        String imagePath = "ruta/a/la/imagen.jpg"; // Reemplaza con la ruta real de la imagen

        // Inyecta la dependencia de la interfaz ImageDisplay
        ImageDisplay imageDisplay = (ImageDisplay) this;
        imageDisplay.showImage(imagePath);

    }


    public void onDoneClicked(Context context) {
        Toast.makeText(this, "AQUIIIII", Toast.LENGTH_SHORT).show();
        // Ensure imagen is not null before calling setVisibility
        if (imagenc != null) {
            imagenc.setVisibility(View.VISIBLE);
        } else {
            // Handle the case where imagen is null (optional)
            // You can log an error or display a message to the user
            Log.e("LogrosActivity", "ImageView (imagen) not found!");
        }
    }
}