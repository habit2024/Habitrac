package app.habitrac.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    SharedPreferences prefs ;
    public EditText signupEmail, signupPassword,signupName, signupLastname, signupAge;
    Button btnSignUp;
    TextView loginRedirectText;
    FirebaseAuth firebaseAuth,auhtforce;
    private ImageView selectImage;
    private Uri imageUri = null;
    private static final int PICK_IMAGE_REQUEST = 7178;
    private String avatar;
    private StorageReference mStorageRef;
    private boolean uploadFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        // Inicializar campos para nombre, apellido y edad
        signupName = findViewById(R.id.edName); // Reemplaza "edName" con el ID del campo de nombre en tu layout
        signupLastname = findViewById(R.id.edLastname); // Reemplaza "edLastname" con el ID del campo de apellido en tu layout
        signupAge = findViewById(R.id.edAge); // Reemplaza "edAge" con el ID del campo de edad en tu layout

        signupEmail = findViewById(R.id.edEmail);
        signupPassword = findViewById(R.id.edPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        loginRedirectText = findViewById(R.id.txtSignIn);
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        selectImage =  findViewById(R.id.selectImage);

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,PICK_IMAGE_REQUEST);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailID = signupEmail.getText().toString();
                String paswd = signupPassword.getText().toString();
                String name = signupName.getText().toString();
                String lastname = signupLastname.getText().toString();
                int age = 0; // Maneja el caso de que el campo de edad esté vacío




                try {
                    age = Integer.parseInt(signupAge.getText().toString());
                } catch (NumberFormatException e) {
                    // Maneja el error si el valor de edad no es un número entero
                    signupAge.setError("Introduce una edad válida");
                    signupAge.requestFocus();
                    return;
                }

                if (emailID.isEmpty()) {
                    signupEmail.setError("¡Proporcione su correo electrónico primero!");
                    signupEmail.requestFocus();
                } else if (paswd.isEmpty()) {
                    signupPassword.setError("Establece tu contraseña");
                    signupPassword.requestFocus();
                } else if (name.isEmpty()) {
                    signupName.setError("¡Proporcione su nombre!");
                    signupName.requestFocus();
                } else if (lastname.isEmpty()) {
                    signupLastname.setError("¡Proporcione su apellido!");
                    signupLastname.requestFocus();

                } else if (emailID.isEmpty() && paswd.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "¡Campos vacíos!", Toast.LENGTH_SHORT).show();
                } else if (!(emailID.isEmpty() && paswd.isEmpty())) {
                    auhtforce= FirebaseAuth.getInstance();
                    Users usuario = new Users(name,lastname, age, emailID);
                    auhtforce.createUserWithEmailAndPassword(emailID,paswd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            FirebaseUser currentUser = auth.getCurrentUser();


                            String uid = currentUser.getUid();
                            Log.d("RegisterActivity", "1opcion" + uid);
                            guardarDatosUsuarioEnFirebaseDatabase(uid, usuario);



                                uploadFile(uid);
                            Toast.makeText(RegisterActivity.this, "Cargando foto de perfil", Toast.LENGTH_LONG).show();



                        }
                    });







                } else {
                    Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }




        });
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(I);
            }
        });

    }

    private void guardarDatosUsuarioEnFirebaseDatabase(String uid, Users usuario) {
        // Referencia a la base de datos Firebase
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference databaseimg = FirebaseDatabase.getInstance().getReference();

        // Guarda los datos del usuario en la ruta "usuarios/" + uid
        Map<String, Object> userData = new HashMap<>();
        userData.put("nombre", usuario.getNombre());
        userData.put("apellido", usuario.getApellido());
        userData.put("edad", usuario.getEdad());
        userData.put("email", usuario.getEmail());


        database.child("usuarios").child(uid).setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("RegisterActivity", "Usuario guardado en Firebase Database");
                } else {
                    Log.w("RegisterActivity", "Error al guardar usuario en Firebase Database: " + task.getException().getMessage());
                }
            }
        });
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
                                Toast.makeText(RegisterActivity.this, "Foto Cargada!", Toast.LENGTH_LONG).show();

                                Toast.makeText(RegisterActivity.this, "Registro completado!", Toast.LENGTH_SHORT).show();


                            }
                            else { Toast.makeText(RegisterActivity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            Toast.makeText(RegisterActivity.this, "No file selected", Toast.LENGTH_LONG).show();
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
            selectImage.setImageURI(imageUri);



        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }






}