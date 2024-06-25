package app.habitrac.com.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.fxn.stash.Stash;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import app.habitrac.com.ImageUser;
import app.habitrac.com.MainActivity;
import app.habitrac.com.R;
import app.habitrac.com.RegisterActivity;
import app.habitrac.com.databinding.ActivitySignUpBinding;
import app.habitrac.com.models.UserModel;
import app.habitrac.com.utils.Constants;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;
    private int currentQuestion = 1;
    private ImageView selectImage;
    private Uri imageUri = null;
    private static final int PICK_IMAGE_REQUEST = 7178;
    private String avatar;
    private StorageReference mStorageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        int theme = Stash.getInt(Constants.THEME);
        setTheme(theme);
        Constants.changeTheme(this);
        setContentView(binding.getRoot());

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        selectImage =  findViewById(R.id.selectImageL);

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,PICK_IMAGE_REQUEST);
            }
        });

        LinearLayout[] linearLayouts = new LinearLayout[]{
                findViewById(R.id.x1),
                findViewById(R.id.x2),

        };
        if (Stash.getBoolean(Constants.LANGUAGE, true)){
            Constants.setLocale(getBaseContext(), Constants.EN);
        } else {
            Constants.setLocale(getBaseContext(), Constants.ES);
        }

        binding.toolbar.tittle.setText(getString(R.string.create_account));
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        binding.login.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        binding.create.setOnClickListener(v -> {


            if (currentQuestion < 1 || currentQuestion > 6) {
                // Handle invalid currentQuestion value
                return;
            }

            if (currentQuestion == 1) {
                // Check if a radio button is selected in question 1 (optional)

            }
            switch (currentQuestion) {
                case 1:


                    String password = binding.password.getEditText().getText().toString();
                    if (!validatePassword(password)) {
                        binding.password.setErrorEnabled(true);
                        binding.password.setError(getString(R.string.password_invalid_format)); // Add a new string resource for this error
                        return ;
                    }
                    linearLayouts[0].setVisibility(View.GONE);
                    currentQuestion = 2;
                    break;
                case 2:
                    linearLayouts[0].setVisibility(View.GONE);
// Extract and handle age separately
                    int age = convertAge(binding.age.getEditText().getText().toString());
                    if (age == -1) { // Or any invalid value you return from convertAge
                        return; // Handle invalid age input (e.g., display error message)
                    }

                    if (valid()) {
                        if (age < 10 || age > 100) {
                            binding.age.setErrorEnabled(true);
                            binding.age.setError(getString(R.string.age_out_of_range)); // Add a new string resource for this error
                            return;
                        }

                        Constants.showDialog();
                        Constants.auth().createUserWithEmailAndPassword(
                                binding.email.getEditText().getText().toString(),
                                binding.password.getEditText().getText().toString()
                        ).addOnSuccessListener(authResult -> {
                            UserModel model = new UserModel(
                                    Constants.auth().getCurrentUser().getUid(),
                                    binding.name.getEditText().getText().toString(),
                                    binding.email.getEditText().getText().toString(),
                                    binding.password.getEditText().getText().toString(),
                                    binding.mlastname.getEditText().getText().toString(),
                                    binding.plastname.getEditText().getText().toString(),
                                    age,
                                    binding.username.getEditText().getText().toString(),
                                    ""
                            );
                            Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid()).setValue(model)
                                    .addOnSuccessListener(unused -> {
                                        Constants.dismissDialog();
                                        startActivity(new Intent(SignUpActivity.this, MessageWelcome.class));
                                        finish();
                                        String uid = Constants.auth().getCurrentUser().getUid();
                                        uploadFile(uid);
                                    }).addOnFailureListener(e -> {
                                        Constants.dismissDialog();
                                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }).addOnFailureListener(e -> {
                            Constants.dismissDialog();
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    }


                    break;


            }

            updateLayoutVisibility(linearLayouts,  currentQuestion);










        });

    }

    private int convertAge(String ageString) {

        try {
            return Integer.parseInt(ageString);
        } catch (NumberFormatException e) {
            // Handle invalid input (e.g., display error message)
            binding.age.setErrorEnabled(true);
            binding.age.setError(getString(R.string.resume));
            return -1; // Or any other invalid value to indicate conversion failure
        }
    }

    private void updateLayoutVisibility(LinearLayout[] linearLayouts, int currentQuestion) {
        for (int i = 0; i < linearLayouts.length; i++) {
            linearLayouts[i].setVisibility(i == (currentQuestion - 1) ? View.VISIBLE : View.GONE);
        }

        }

    @Override
    protected void onResume() {
        super.onResume();
        Constants.initDialog(this);
        binding.name.getEditText().setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.mlastname.getEditText().setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.plastname.getEditText().setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.age.getEditText().setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.username.getEditText().setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.email.getEditText().setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.password.getEditText().setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.create.setBackgroundColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.login.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
    }

    private boolean valid() {
        if (binding.name.getEditText().getText().toString().isEmpty()) {
            binding.name.setErrorEnabled(true);
            binding.name.setError(getString(R.string.name_is_empty));
            return false;
        }
        if (binding.mlastname.getEditText().getText().toString().isEmpty()) {
            binding.mlastname.setErrorEnabled(true);
            binding.mlastname.setError(getString(R.string.name_is_empty));
            return false;
        }
        if (binding.plastname.getEditText().getText().toString().isEmpty()) {
            binding.plastname.setErrorEnabled(true);
            binding.plastname.setError(getString(R.string.name_is_empty));
            return false;
        }
        // Validate name (more than 3 letters, no numbers)
        if (!validateName(binding.name.getEditText().getText().toString())) {
            binding.name.setErrorEnabled(true);
            binding.name.setError(getString(R.string.name_invalid_format)); // Adjust error message as needed
            return false;
        }
        if (!validateName(binding.mlastname.getEditText().getText().toString())) {
            binding.mlastname.setErrorEnabled(true);
            binding.mlastname.setError(getString(R.string.name_invalid_format)); // Adjust error message as needed
            return false;
        }
        if (!validateName(binding.plastname.getEditText().getText().toString())) {
            binding.plastname.setErrorEnabled(true);
            binding.plastname.setError(getString(R.string.name_invalid_format)); // Adjust error message as needed
            return false;
        }

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
        String password = binding.password.getEditText().getText().toString();
        if (!validatePassword(password)) {
            binding.password.setErrorEnabled(true);
            binding.password.setError(getString(R.string.password_invalid_format)); // Add a new string resource for this error
            return false;
        }

        return true;
    }

    private boolean validatePassword(String password) {
        // Check length (at least 8 characters)
        if (password.length() < 8) {
            return false;
        }

        // Check for at least one uppercase letter
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }

        // Check for at least one special character (regular expression)
        if (!password.matches(".*[!@#$%^&*-+=()\\[\\]{}|:;'<>,./?~\\\\s].*")) {
            return false;
        }

        return true;
    }
    private boolean validateName(String name) {
        // Check length (more than 3 characters)
        if (name.length() <= 3) {
            return false;
        }

        // Check for no numbers (use regular expression)
        return name.matches("[a-zA-Z]+"); // Matches only letters (a-z and A-Z)
    }

    private void uploadFile(String uid)  {

        DatabaseReference databaseimg = FirebaseDatabase.getInstance().getReference();


        avatar = mStorageRef.getDownloadUrl().toString();


        if (imageUri != null) {
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));

            fileReference.putFile(imageUri).continueWithTask(
                            new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw task.getException(); }
                                    return fileReference.getDownloadUrl();
                                } })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();

                                ImageUser upload = new ImageUser(downloadUri.toString());
                                databaseimg.child("usuariosimg").child(uid).setValue(upload);
                                Toast.makeText(SignUpActivity.this, "Foto Cargada!", Toast.LENGTH_LONG).show();

                                Toast.makeText(SignUpActivity.this, "Registro completado!", Toast.LENGTH_SHORT).show();


                            }
                            else { Toast.makeText(SignUpActivity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            Toast.makeText(SignUpActivity.this, "No file selected", Toast.LENGTH_LONG).show();
        }

        // Handler for delayed execution
        //   final Handler handler = new Handler();

        // Runnable that starts the LoginActivity after a delay
      /*  final Runnable startLoginActivity = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(RegisterActivity.this, SplashScreen.class);
                startActivity(intent);
                finish(); // Finish the splash screen activity
            }
        };*/

        // Set delay in milliseconds
        //  int delay = 15000; // 3 seconds

        // Post the runnable with the delay
        // handler.postDelayed(startLoginActivity, delay);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            Glide.with(this)
                    .load(imageUri)
                    .transform(new CircleCrop())
                    .into(selectImage);
          //  selectImage.setImageURI(imageUri);



        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }




}