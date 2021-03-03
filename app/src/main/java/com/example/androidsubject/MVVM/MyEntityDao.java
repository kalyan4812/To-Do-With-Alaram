package com.example.androidsubject.MVVM;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MyEntityDao {
    @Insert
    void insert(MyEntity note);
    @Update
    void update(MyEntity note);
    @Delete
    void delete(MyEntity note);
    // we dont have ready made annotaion for deeleting all,so
    @Query("DELETE FROM note_table")
    void deleteAllNotes();
    // * means from all columns
    @Query("SELECT * FROM note_table ORDER BY priority ASC")
    LiveData<List<MyEntity>> getAllEntities();

    /* passing query at run time
    @RawQuery
    MyEntity getEntity(String query);

    MyEntity myentity=getEntity("SELECT * FROM note_table WHERE id=3 LIMIT 1");

    NOTE: RAW QUERY methods must return non-void type.It is available from Room 1.1.0 -alpha3


    other way:

    @QUERY(" SELECT * FROM note_table WHERE id=:myid")
    MyEntity getEntity(int myid);
     */
}
