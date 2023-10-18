package com.example.practice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Random;

public class Homepage extends AppCompatActivity
{
    FloatingActionButton addbtn;
    RecyclerView recyclerView;
    ImageButton signoutbtn,delete_imagebtn;
    NoteAdapter noteAdapter;
    ImageView imageView;
    Random r;
    Integer[] images = {R.drawable.image_1,R.drawable.image_2,R.drawable.image_3,R.drawable.image_4,R.drawable.image_5};
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        addbtn = findViewById(R.id.addbtn);
        recyclerView = findViewById(R.id.recycler_view);
        signoutbtn = findViewById(R.id.signoutbtn);
        delete_imagebtn = findViewById(R.id.delete_imagebtn);
        delete_imagebtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(Homepage.this, "This is working", Toast.LENGTH_SHORT).show();
            }
        });


        imageView = findViewById(R.id.imageView);
        r = new Random();
        imageView.setImageResource(images[r.nextInt(images.length)]);

        addbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(Homepage.this,NoteDetails.class));
            }
        });

        setupRecyclerView();

        signoutbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                PopupMenu popupMenu = new PopupMenu(Homepage.this,signoutbtn);
                popupMenu.getMenu().add("LogOut");
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem)
                    {
                        if (menuItem.getTitle()=="LogOut")
                        {
                            /*AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);*/
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(Homepage.this,Login.class));
                            finish();
                            return true;
                        }
                        return false;
                    }
                });
            }
        });
    }

    static CollectionReference collectionReference()
    {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("Notes")
                .document(currentUser.getUid()).collection("MyNotes");
    }

    //Fetch data from firebase and display in recyclerview
    void setupRecyclerView()
    {
        Query query = collectionReference().orderBy("timestamp",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query,Note.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter(options,this);
        recyclerView.setAdapter(noteAdapter);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        noteAdapter.stopListening();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        noteAdapter.notifyDataSetChanged();
    }
}