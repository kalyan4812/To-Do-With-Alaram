package com.example.androidsubject.MVVM;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidsubject.R;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {
    public static final int EDIT_NOTE_REQUEST = 2;
    Context context;
    private List<MyEntity> notes = new ArrayList<>();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_note_view, parent, false);
        context = parent.getContext();
        sharedPreferences = context.getSharedPreferences("color", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        return new MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        final MyEntity currentNote = notes.get(position);
        if(sharedPreferences.getBoolean(currentNote.getDescription(),false)){
            holder.cardView.setCardBackgroundColor(Color.RED);
            Log.i("color","red");
        }
        holder.textViewTitle.setText(currentNote.getTitle());
        holder.textViewDescription.setText(currentNote.getDescription());
        holder.textViewPriority.setText(String.valueOf(currentNote.getPriority()));
        holder.date.setText(currentNote.getDate());
        holder.time.setText(currentNote.getTime());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddNote.class);
                intent.putExtra(AddNote.EXTRA_ID, currentNote.getId());
                intent.putExtra(AddNote.EXTRA_TITLE, currentNote.getTitle());
                intent.putExtra(AddNote.EXTRA_DESCRIPTION, currentNote.getDescription());
                intent.putExtra(AddNote.EXTRA_PRIORITY, currentNote.getPriority());
                intent.putExtra("mydate", currentNote.getDate());
                intent.putExtra("mytime", currentNote.getTime());
                ((Activity) context).startActivityForResult(intent, EDIT_NOTE_REQUEST);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void setNotes(List<MyEntity> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    public MyEntity getNoteAt(int position) {
        return notes.get(position);
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewDescription;
        private TextView textViewPriority, date, time;
        private CardView cardView;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPriority = itemView.findViewById(R.id.text_view_priority);
            cardView = itemView.findViewById(R.id.cardbg);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
        }
    }
}
