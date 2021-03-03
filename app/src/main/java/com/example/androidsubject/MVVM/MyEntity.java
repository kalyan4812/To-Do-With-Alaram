package com.example.androidsubject.MVVM;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "note_table")

public class MyEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String description;
    // to change column name from priority to my_priority
  //   @ColumnInfo(name = "my_priority")
    private int priority;
    private String date;
    private String time;

    public MyEntity(String title, String description, int priority,String date,String time) {

        this.title = title;
        this.description = description;

        this.priority = priority;
        this.date=date;
        this.time=time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }
}
