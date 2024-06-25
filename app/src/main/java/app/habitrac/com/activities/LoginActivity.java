package app.habitrac.com.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import com.fxn.stash.Stash;
import app.habitrac.com.MainActivity;
import app.habitrac.com.R;
import app.habitrac.com.databinding.ActivityLoginBinding;
import app.habitrac.com.utils.Constants;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        int theme = Stash.getInt(Constants.THEME);
        setTheme(theme);
        Constants.changeTheme(this);
        setContentView(binding.getRoot());




        if (Stash.getBoolean(Constants.LANGUAGE, true)){
            Constants.setLocale(getBaseContext(), Constants.EN);
        } else {
            Constants.setLocale(getBaseContext(), Constants.ES);
        }



        binding.toolbar.tittle.setText(getString(R.string.continue_with_e_mail));

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        binding.forgot.setOnClickListener(v -> startActivity(new Intent(this, ForgotActivity.class)));
        binding.create.setOnClickListener(v -> startActivity(new Intent(this, SignUpActivity.class)));

        binding.login.setOnClickListener(v -> {
            if (valid()){
                Constants.showDialog();
                Constants.auth().signInWithEmailAndPassword(
                        binding.email.getEditText().getText().toString(),
                        binding.password.getEditText().getText().toString()
                ).addOnSuccessListener(authResult -> {
                    Constants.dismissDialog();
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Constants.initDialog(this);
        updateViews();
    }

    private void updateViews() {
        binding.create.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.forgot.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.email.getEditText().setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.password.getEditText().setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.login.setBackgroundColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
    }

    private boolean valid() {
        if (binding.email.getEditText().getText().toString().isEmpty()) {
            binding.email.setErrorEnabled(true);
            binding.email.setError(getString(R.string.email_is_empty));
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.email.getEditText().getText().toString()).matches()) {
            binding.email.setErrorEnabled(true);
            binding.email.setError(getString(R.string.email_is_not_valid));
            return false;
        }
        if (binding.password.getEditText().getText().toString().isEmpty()) {
            binding.password.setErrorEnabled(true);
            binding.password.setError(getString(R.string.password_is_empty));
            return false;
        }
        return true;
    }

}