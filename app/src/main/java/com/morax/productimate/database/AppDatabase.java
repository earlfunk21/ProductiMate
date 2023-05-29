package com.morax.productimate.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.morax.productimate.database.converter.DateConverter;
import com.morax.productimate.database.dao.NoteDao;
import com.morax.productimate.database.dao.TaskDao;
import com.morax.productimate.database.dao.UserDao;
import com.morax.productimate.database.entity.Note;
import com.morax.productimate.database.entity.Task;
import com.morax.productimate.database.entity.User;

@Database(entities = {User.class, Note.class, Task.class}, version = 1)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();

    public abstract NoteDao noteDao();

    public abstract TaskDao taskDao();

    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,
                    "test_db3").allowMainThreadQueries().build();
        }
        return instance;
    }


}