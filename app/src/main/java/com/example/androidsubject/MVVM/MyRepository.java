package com.example.androidsubject.MVVM;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Update;

import java.util.List;

public class MyRepository {
    private MyEntityDao myEntityDao;
    private LiveData<List<MyEntity>> allNotes;

    public MyRepository(Application application) {
        MyEntityDatabase myEntityDatabase = MyEntityDatabase.getInstance(application);
        // room provides implemenation for myEntitiyDao ,so although its abstract we can access it.
        myEntityDao = myEntityDatabase.myEntityDao();
        allNotes = myEntityDao.getAllEntities();

    }

    public void insert(MyEntity myEntity) {
        new InsertNoteAsyncTask(myEntityDao).execute(myEntity);
    }

    public void update(MyEntity myEntity) {
        new UpdateNoteAsyncTask(myEntityDao).execute(myEntity);
    }

    public void delete(MyEntity myEntity) {
        new DeleteNoteAsyncTask(myEntityDao).execute(myEntity);
    }

    public void deleteAllNotes() {
        new DeleteAllNoteAsyncTask(myEntityDao).execute();
    }

    public LiveData<List<MyEntity>> getAllEntities() {
        return allNotes;
    }

    private static class InsertNoteAsyncTask extends AsyncTask<MyEntity, Void, Void> {
        private MyEntityDao myEntityDao;

        private InsertNoteAsyncTask(MyEntityDao myEntityDao) {
            this.myEntityDao = myEntityDao;
        }

        @Override
        protected Void doInBackground(MyEntity... notes) {
            myEntityDao.insert(notes[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<MyEntity, Void, Void> {
        private MyEntityDao myEntityDao;

        private DeleteNoteAsyncTask(MyEntityDao myEntityDao) {
            this.myEntityDao = myEntityDao;
        }

        @Override
        protected Void doInBackground(MyEntity... notes) {
            myEntityDao.delete(notes[0]);
            return null;
        }

    }
    private static class UpdateNoteAsyncTask extends AsyncTask<MyEntity, Void, Void> {
        private MyEntityDao myEntityDao;

        private UpdateNoteAsyncTask(MyEntityDao myEntityDao) {
            this.myEntityDao = myEntityDao;
        }

        @Override
        protected Void doInBackground(MyEntity... notes) {
            myEntityDao.update(notes[0]);
            return null;
        }

    }
    private static class DeleteAllNoteAsyncTask extends AsyncTask<  Void, Void, Void> {
        private MyEntityDao myEntityDao;

        private DeleteAllNoteAsyncTask(MyEntityDao myEntityDao) {
            this.myEntityDao = myEntityDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            myEntityDao.deleteAllNotes();
            return null;
        }

    }

}



