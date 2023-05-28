package com.morax.productimate.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.morax.productimate.database.entity.Task;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    void insert(Task task);

    @Query("SELECT * FROM Task")
    List<Task> getTasks();

    @Query("SELECT * FROM Task WHERE id = :id")
    Task getTaskById(long id);

    @Query("SELECT * FROM Task WHERE userId = :userId")
    List<Task> getTaskByUserId(long userId);

    @Query("SELECT * FROM Task WHERE userId = :userId AND done = :done")
    List<Task> getTaskByUserIdAndDone(long userId, boolean done);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("DELETE FROM Task")
    void deleteAll();
}