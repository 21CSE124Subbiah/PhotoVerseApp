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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button login,createaccount;
    private EditText emailtext,passwordtext;

    private FirebaseAuth firebaseAuth;

    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        createaccount=findViewById(R.id.create);
        createaccount.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this,signup.class);
            startActivity(i);
        });

        login= findViewById(R.id.login);
        emailtext=findViewById(R.id.EmailText);
        passwordtext=findViewById(R.id.PasswordText);

        firebaseAuth= FirebaseAuth.getInstance();
        login.setOnClickListener(v -> {
            loginUser(
                    emailtext.getText().toString().trim(),
                    passwordtext.getText().toString().trim()
            );
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Clear the input fields
        emailtext.setText("");
        passwordtext.setText("");
    }

    private void loginUser(String email,String password){
        if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)){
            firebaseAuth.signInWithEmailAndPassword(
                    email,password
            ).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();

                    Intent i = new Intent(MainActivity.this,PhotoList.class);
                    startActivity(i);
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Incorrect Email or Password", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();

        }

    }
}