package com.example.photoverseapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

public class AddPhotoList extends AppCompatActivity {

    private Button saveButton;
    private ImageView addPhotoBtn;
    private ProgressBar progressBar;
    private EditText titleEditText;
    private EditText DescriptionEditText;
    private ImageView imageView;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Photoverse");


    private StorageReference storageReference;

    private String currentUserId;
    private String currentUserName;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    ActivityResultLauncher<String> mtakephoto;
    Uri imageUri;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo_list);

        progressBar = findViewById(R.id.post_progressBar);
        titleEditText = findViewById(R.id.post_title_et);
        DescriptionEditText = findViewById(R.id.post_description_et);
        imageView = findViewById(R.id.post_imageView);
        saveButton = findViewById(R.id.post_save_journal_button);
        addPhotoBtn = findViewById(R.id.postCameraButton);
        //ImageButton shareButton = findViewById(R.id.share_button);


        progressBar.setVisibility(View.INVISIBLE);

        // Firebase Storage Reference
        storageReference = FirebaseStorage.getInstance()
                .getReference();

        // Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // Getting the Current User
        authStateListener = firebaseAuth -> {
            user = firebaseAuth.getCurrentUser();
            if (user != null) {
                currentUserId = user.getUid();
                currentUserName = user.getDisplayName();

            }
        };
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savephoto();
            }
        });

      mtakephoto = registerForActivityResult(
              new ActivityResultContracts.GetContent(),
              new ActivityResultCallback<Uri>() {
                  @Override
                  public void onActivityResult(Uri o) {
                      imageView.setImageURI(o);
                  imageUri= o;
                  }
              }
      );

        addPhotoBtn.setOnClickListener(v -> mtakephoto.launch("image/*"));

    }

    public void savephoto() {

       // Toast.makeText(AddPhotoList.this, "Button Clicked", Toast.LENGTH_SHORT).show();


        String title = titleEditText.getText().toString().trim();
        String thoughts = DescriptionEditText.getText().toString().trim();
        //String users = currentUserName.toString().trim();

        progressBar.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(thoughts)
                && imageUri !=null){


            final StorageReference filePath = storageReference.
                    child("post_images")
                    .child("my_image_"+ Timestamp.now().getSeconds());


            filePath.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUrl = uri.toString();

                            Photoverse photoverse = new Photoverse();
                            photoverse.setTitle(title);
                            photoverse.setThoughts(thoughts);
                            photoverse.setImageUrl(imageUrl);

                            photoverse.setTimeAdded(new Timestamp(new Date()));
                            photoverse.setUsername(currentUserName);
                            photoverse.setUserId(currentUserId);



                            collectionReference.add(photoverse)
                                    .addOnSuccessListener(documentReference -> {
                                        progressBar.setVisibility(View.INVISIBLE);

                                        Intent i = new Intent(AddPhotoList.this, PhotoList.class);
//                                        i.putExtra("username", currentUserName);
                                        startActivity(i);
                                        finish();
                                    }).addOnFailureListener(e -> Toast.makeText(AddPhotoList.this, "Failed !", Toast.LENGTH_SHORT).show());

                        }
                    })).addOnFailureListener(e -> {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(AddPhotoList.this,
                                "Failed !!!!", Toast.LENGTH_SHORT).show();
                    });


        }else{
            progressBar.setVisibility(View.INVISIBLE);
        }
        //Toast.makeText(this, "Class called", Toast.LENGTH_SHORT).show();

    }



    @Override
    protected void onStart() {
        super.onStart();
        user = firebaseAuth.getCurrentUser();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAuth != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

//    public void onShareClicked(View view) {
//        // Implement share functionality here
//        Toast.makeText(this, "Share button clicked!", Toast.LENGTH_SHORT).show();
//
//        // Example: Share text
//        String shareText = "Check out this photo!";
//        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//        shareIntent.setType("text/plain");
//        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
//        startActivity(Intent.createChooser(shareIntent, "Share via"));
//    }
}