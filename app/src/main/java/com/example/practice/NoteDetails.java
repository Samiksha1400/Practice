package com.example.practice;

import  androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class NoteDetails extends AppCompatActivity
{

    EditText title_txt,main_content;
    ImageButton save_imagebtn,delete_imagebtn;
    TextView addnotetitle;
    String title,content,docId;
    boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        title_txt = findViewById(R.id.title_txt);
        main_content = findViewById(R.id.main_content);
        save_imagebtn = findViewById(R.id.save_imagebtn);
        delete_imagebtn = findViewById(R.id.delete_imagebtn);
        save_imagebtn.setOnClickListener(view -> saveNote());

        addnotetitle = findViewById(R.id.addnotetitle);
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");


        if(docId!= null && !docId.isEmpty())
        {
            isEditMode = true;
        }
        title_txt.setText(title);
        main_content.setText(content);
        if (isEditMode)
        {
            addnotetitle.setText("Edit Note");
            delete_imagebtn.setVisibility(View.VISIBLE);
        }
        delete_imagebtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                DocumentReference documentReference;
                documentReference = collectionReference().document(docId);
                documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(NoteDetails.this, "Note Deleted successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else
                        {
                            Toast.makeText(NoteDetails.this, "Not Deleted", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
    void saveNote()
    {
        String noteTitle = title_txt.getText().toString();
        String content = main_content.getText().toString();
        if (noteTitle == null || noteTitle.isEmpty())
        {
            title_txt.setError("Title is Required");
            return;
        }

        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(content);
        note.setTimestamp(Timestamp.now());
        saveNoteToDB(note);
    }

    static CollectionReference collectionReference()
    {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("Notes")
                .document(currentUser.getUid()).collection("MyNotes");
    }
    void saveNoteToDB(Note note)
    {
        DocumentReference documentReference;
        if(isEditMode)
        {
            documentReference = collectionReference().document(docId);
        }
        else
        {
            documentReference = collectionReference().document();
        }
        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    Toast.makeText(NoteDetails.this, "Note added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    Toast.makeText(NoteDetails.this, "Not Added", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}