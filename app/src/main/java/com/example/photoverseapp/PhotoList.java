package com.example.photoverseapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class PhotoList extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Photoverse");
    private List<Photoverse> photoverseList;
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_list);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        recyclerView = findViewById(R.id.recylerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        photoverseList = new ArrayList<>();
        myAdapter = new MyAdapter(PhotoList.this, photoverseList);
        recyclerView.setAdapter(myAdapter);

        floatingActionButton = findViewById(R.id.floatab);
        floatingActionButton.setOnClickListener(v -> {
            Intent i = new Intent(PhotoList.this, AddPhotoList.class);
            startActivity(i);
        });



    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);

        MenuItem signOutItem = menu.findItem(R.id.action_signout);
        SpannableString s = new SpannableString(signOutItem.getTitle());
        s.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s.length(), 0);
        signOutItem.setTitle(s);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_add) {
            if (user != null && firebaseAuth != null) {
                Intent i = new Intent(PhotoList.this, AddPhotoList.class);
                startActivity(i);
            }
        } else if (itemId == R.id.action_signout) {
            if (user != null && firebaseAuth != null) {
                firebaseAuth.signOut();
                Intent i = new Intent(PhotoList.this, MainActivity.class);
                startActivity(i);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        photoverseList.clear();

        collectionReference.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot photos : queryDocumentSnapshots) {
                Photoverse photoverse = photos.toObject(Photoverse.class);
                photoverseList.add(photoverse);
            }
            myAdapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> Toast.makeText(PhotoList.this, "Sorry, something went wrong!", Toast.LENGTH_SHORT).show());
    }




}
