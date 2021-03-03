package com.example.androidsubject.MVVM;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

// you can add more than one tables/entities in{}
@Database(entities = {MyEntity.class}, version = 2)
public abstract class MyEntityDatabase extends RoomDatabase {
    // to make class singleton
    private static MyEntityDatabase instance;
    public abstract MyEntityDao myEntityDao();
    public static synchronized MyEntityDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    MyEntityDatabase.class, "note_database")
                    .fallbackToDestructiveMigration().addCallback(roomCallBack)
                    .build();
        }
        return instance;
    }
    private static RoomDatabase.Callback roomCallBack=new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private MyEntityDao myEntityDao;
        private PopulateDbAsyncTask(MyEntityDatabase db) {
            myEntityDao = db.myEntityDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            myEntityDao.insert(new MyEntity("Title 1", "Description 1", 1,"",""));
            myEntityDao.insert(new MyEntity("Title 2", "Description 2", 2,"",""));
            myEntityDao.insert(new MyEntity("Title 3", "Description 3", 3,"",""));
            return null;
        }
    }
}
