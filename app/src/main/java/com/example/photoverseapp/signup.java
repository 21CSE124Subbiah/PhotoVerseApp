package com.example.photoverseapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class signup extends AppCompatActivity {

    EditText password_create,email_create,username_create;
    Button signupBtn;

    private FirebaseAuth firebaseAuth;

    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentuser;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Photoverse");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        signupBtn=findViewById(R.id.signup_btn);
        password_create=findViewById(R.id.create_password);
        email_create=findViewById(R.id.create_email);
        username_create=findViewById(R.id.create_username);


        firebaseAuth= FirebaseAuth.getInstance();
        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentuser=firebaseAuth.getCurrentUser();

                if(currentuser!=null){

                }
                else {

                }
            }
        };

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(email_create.getText().toString())&&!TextUtils.isEmpty(password_create.getText().toString())
                        &&!TextUtils.isEmpty(username_create.getText().toString())){

                    String email = email_create.getText().toString().trim();
                    String password=password_create.getText().toString().trim();
                    String username=username_create.getText().toString().trim();

                    CreateAccount(email,password,username);
                }
                else {
                    Toast.makeText(signup.this, "Fill up all the Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void CreateAccount(String email, String password, String username){
        if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)&&!TextUtils.isEmpty(username)){

            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(signup.this, "Success", Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(signup.this,MainActivity.class);
                        startActivity(i);
                    }
                    else {
                        Toast.makeText(signup.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}