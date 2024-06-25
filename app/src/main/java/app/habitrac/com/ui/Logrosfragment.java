package app.habitrac.com.ui;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import app.habitrac.com.R;


public class Logrosfragment extends Fragment implements DoneListener{


    private ImageView imagenLogro;



    @Override
    public void onDoneClicked() {
        Toast.makeText(getContext(), "SSSSSSSSSSSssssaaaSaaaaaaaaaaaaaaaaaaaaa", Toast.LENGTH_SHORT).show();
        Log.d("ActivityB", "Done button clicked!");
        imagenLogro.setVisibility(View.VISIBLE);
        mostrarImagenLogro();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logros_fragment, container, false);
        imagenLogro = view.findViewById(R.id.imagen_logro);
       // imagenLogro.setVisibility(View.GONE);
        // Inflate the layout for this fragment
        return view;
    }
    public void mostrarImagenLogro() {
        // Load or generate the image and display it
        imagenLogro.setVisibility(View.GONE); // Make the image visible
        Toast.makeText(getContext(), "SSSSSSSSSSSSaaaaaaaaaaaaaaaaaaaaa", Toast.LENGTH_SHORT).show();
    }
}