package com.example.practice;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.NoteViewHolder>
{
    Context context;
    public NoteAdapter(FirestoreRecyclerOptions<Note> options, Context context)
    {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(NoteViewHolder holder, int position, Note note)
    {
        holder.titletxt.setText(note.title);
        holder.contenttxt.setText(note.content);
        holder.timestamptxt.setText(Utility.timestampToString(note.timestamp));

        holder.itemView.setOnClickListener((view ->
        {
            Intent intent = new Intent(context,NoteDetails.class);
            intent.putExtra("title",note.title);
            intent.putExtra("content",note.content);
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId",docId);
            context.startActivity(intent);
        }));
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_note_item, parent,false);
        return new NoteViewHolder(view);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder
    {

        TextView titletxt,contenttxt,timestamptxt;

        public NoteViewHolder(View itemView)
        {
            super(itemView);
            titletxt = itemView.findViewById(R.id.note_title);
            contenttxt = itemView.findViewById(R.id.note_content);
            timestamptxt = itemView.findViewById(R.id.note_timestamp);

        }
    }
}
