package app.habitrac.com.ui.slideshow;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import app.habitrac.com.ImageUser;
import app.habitrac.com.MainActivity;
import app.habitrac.com.R;
import app.habitrac.com.RegisterActivity;
import app.habitrac.com.databinding.FragmentSlideshowBinding;

public class SlideshowFragment extends Fragment {


    private DatabaseReference rfUser = FirebaseDatabase.getInstance().getReference().child("Habitrac").child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    private TextView name,lastname,email;
    private ImageView img,editname,editnamelast;
    private EditText nameEditText, et_name,nameEditTextlast, et_namelast;
    private StorageReference mStorageRef;
    private MainActivity activity;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slideshow, container, false);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        getUserImageUrl(uid);
        loadUserData();
        //name = (TextView) view.findViewById(R.id.tvname);

        email = (TextView) view.findViewById(R.id.tvemail);
        img = (ImageView) view.findViewById(R.id.img);
        editname= (ImageView) view.findViewById(R.id.editname);
        nameEditText = (EditText) view.findViewById(R.id.et_name);
        et_name = (EditText) view.findViewById(R.id.et_name);
        editnamelast= (ImageView) view.findViewById(R.id.editnamelast);
        nameEditTextlast = (EditText) view.findViewById(R.id.et_namelast);
        et_namelast = (EditText) view.findViewById(R.id.et_namelast);

        activity = (MainActivity) getActivity();

        editname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserData();

            }
        });
        editnamelast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserDatalast();

            }
        });

        return view;
    }
    private void loadUserData() {

        rfUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    et_name.setText("" + dataSnapshot.child("name").getValue().toString());
                    et_namelast.setText("" + dataSnapshot.child("mlast").getValue().toString());
                    email.setText("" + dataSnapshot.child("email").getValue().toString());

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void updateUserData() {
        String newName = nameEditText.getText().toString().trim();
        rfUser.child("name").setValue(newName);
        Toast.makeText(getContext(), "Nombre Actualizado", Toast.LENGTH_LONG).show();


        // Update Firebase Database (explained below)

        // Optionally handle image update here (explained below)
    }

    private void updateUserDatalast() {
        String newName = nameEditTextlast.getText().toString().trim();
        rfUser.child("mlast").setValue(newName);
        Toast.makeText(getContext(), "Apellido Actualizado", Toast.LENGTH_LONG).show();


        // Update Firebase Database (explained below)

        // Optionally handle image update here (explained below)
    }



    private void getUserImageUrl(String uid) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        databaseRef.child("usuariosimg").child(uid).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ImageUser imageUser = dataSnapshot.getValue(ImageUser.class);
                    String imageUrl = imageUser.getAvatar();
                    // Now you have the image URL, use it to display the image
                    displayImage(imageUrl);
                } else {
                    // Handle the case where no image is found for the user
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle any errors during data retrieval
            }
        });
    }
    private void displayImage(String imageUrl) {
        Glide.with(this)
                .load(imageUrl)
                .transform(new CircleCrop())
                .into(img); // Replace profileImageView with your actual ImageView
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
    private void uploadImage(Uri imageUri, String uid) {
        if (imageUri != null) {
            // Create a unique filename
            String filename = System.currentTimeMillis() + "." + getFileExtension(imageUri);

            // Get a reference to the storage location
            StorageReference imageRef = mStorageRef.child(filename);

            // Upload the image
            imageRef.putFile(imageUri).continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return imageRef.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();

                    // Update the user's profile image URL in the database
                    updateImageURL(downloadUri.toString(), uid);
                } else {
                    // Handle upload failure
                    Toast.makeText(getContext(), "Image upload failed", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> {
                // Handle upload failure
                Toast.makeText(getContext(), "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void updateImageURL(String imageUrl, String uid) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("usuarios").child(uid);
        userRef.child("avatar").setValue(imageUrl);
    }
    private String getFileExtension(Uri uri) {
        if (activity != null) {
            ContentResolver cR = activity.getContentResolver();
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            return mime.getExtensionFromMimeType(cR.getType(uri));
        } else {
            // Handle the case where activity is null (e.g., return null or throw an exception)
            return null;
        }
    }

    // Método para cargar los datos del usuario de Firebase


}