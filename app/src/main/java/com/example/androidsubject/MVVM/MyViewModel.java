package com.example.androidsubject.MVVM;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MyViewModel extends AndroidViewModel {
    private MyRepository myRepository;

    private LiveData<List<MyEntity>> allNotes;
    public MyViewModel(@NonNull Application application) {
        super(application);
        myRepository= new MyRepository(application);
        allNotes = myRepository.getAllEntities();
    }
    public void insert(MyEntity myEntity) {
        myRepository.insert(myEntity);
    }
    public void update(MyEntity myEntity) {
        myRepository.update(myEntity);
    }
    public void delete(MyEntity myEntity) {
        myRepository.delete(myEntity);
    }
    public void deleteAllNotes() {
        myRepository.deleteAllNotes();
    }
    public LiveData<List<MyEntity>> getAllNotes() {
        return allNotes;
    }
}
