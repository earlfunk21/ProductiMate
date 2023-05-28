package com.morax.productimate.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.morax.productimate.database.dao.NoteDao;
import com.morax.productimate.database.dao.UserDao;
import com.morax.productimate.database.entity.Note;
import com.morax.productimate.database.entity.User;

@Database(entities = {User.class, Note.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract NoteDao noteDao();
    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,
                    "test_db1").allowMainThreadQueries().build();
        }
        return instance;
    }


}