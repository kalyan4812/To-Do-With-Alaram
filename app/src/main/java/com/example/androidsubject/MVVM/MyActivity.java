package com.example.androidsubject.MVVM;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.example.androidsubject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static com.example.androidsubject.MVVM.MyAdapter.EDIT_NOTE_REQUEST;

public class MyActivity extends AppCompatActivity {
    boolean saved = false;
    private MyViewModel noteViewModel;
    FloatingActionButton addnote;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        addnote = findViewById(R.id.button_add_note);
        sharedPreferences = getSharedPreferences("color", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        setTitle("TO DO APP");
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.colorPrimary));

        addnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saved = true;
                Intent i = new Intent(getApplicationContext(), AddNote.class);
                startActivityForResult(i, 120);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setHasFixedSize(true);
        final MyAdapter adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);
        noteViewModel = ViewModelProviders.of(this).get(MyViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<MyEntity>>() {
            @Override
            public void onChanged(@Nullable List<MyEntity> notes) {
                adapter.setNotes(notes);
            }
        });
        // to make recycler view swipwable
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                editor.remove(adapter.getNoteAt(viewHolder.getAdapterPosition()).getDescription()).apply();
                Toast.makeText(getApplicationContext(), "Note deleted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if(actionState==ItemTouchHelper.ACTION_STATE_SWIPE){
                    View view=viewHolder.itemView;
                    Paint p=new Paint();
                    Bitmap bitmap;
                    //dx>0 swipe left to right
                    if(dX<0){
                        bitmap= BitmapFactory.decodeResource(getResources(),R.mipmap.ic_delete_white_png);
                        p.setColor(Color.RED);
                        c.drawRect(view.getRight()+dX,view.getTop(),view.getRight(),view.getBottom(),p);
                        c.drawBitmap(bitmap,view.getRight()-bitmap.getWidth(),view.getTop()+(view.getBottom()-view.getTop()-bitmap.getHeight())/2 ,p);
                    }
                    else {
                        bitmap= BitmapFactory.decodeResource(getResources(),R.mipmap.ic_delete_white_png);
                        p.setColor(Color.GREEN);
                        c.drawRect(view.getLeft(),view.getTop(),view.getLeft()+dX,view.getBottom(),p);
                        c.drawBitmap(bitmap,view.getLeft(),view.getTop()+(view.getBottom()-view.getTop()-bitmap.getHeight())/2 ,p);
                    }
                    viewHolder.itemView.setTranslationX(dX);;
                }
                else {
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }
        }).attachToRecyclerView(recyclerView);

    }

    private void layoutAnimation(RecyclerView recyclerView) {
        Context context = recyclerView.getContext();
        LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_fall_down);
        recyclerView.setLayoutAnimation(layoutAnimationController);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_notes:
                noteViewModel.deleteAllNotes();
                Toast.makeText(this, "All notes deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 120 && resultCode == RESULT_OK) {

            String title = data.getStringExtra(AddNote.EXTRA_TITLE);
            String description = data.getStringExtra(AddNote.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddNote.EXTRA_PRIORITY, 1);
            String date = data.getStringExtra("mydate");
            String time = data.getStringExtra("mytime");
            MyEntity note = new MyEntity(title, description, priority, date, time);
            noteViewModel.insert(note);
            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddNote.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = data.getStringExtra(AddNote.EXTRA_TITLE);
            String description = data.getStringExtra(AddNote.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddNote.EXTRA_PRIORITY, 1);
            String dates = data.getStringExtra("mydate");
            String times = data.getStringExtra("mytime");
            MyEntity note = new MyEntity(title, description, priority, dates, times);
            note.setId(id);
            noteViewModel.update(note);
            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();
        } else if (saved = true) {
            saved = false;
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show();
            return;
        } else if (saved = false) {
            Toast.makeText(this, "Note not updated", Toast.LENGTH_SHORT).show();
        }


    }
}
