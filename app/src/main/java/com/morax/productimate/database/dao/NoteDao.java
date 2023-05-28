package com.morax.productimate.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.morax.productimate.database.entity.Note;

import java.util.List;

@Dao
public interface NoteDao {
    @Insert
    void insert(Note note);

    @Query("SELECT * FROM Note")
    List<Note> getNotes();

    @Query("SELECT * FROM Note WHERE id = :id")
    Note getNoteById(long id);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    @Query("DELETE FROM Note")
    void deleteAll();
}