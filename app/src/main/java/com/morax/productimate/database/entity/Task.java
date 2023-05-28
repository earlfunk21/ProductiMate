package com.morax.productimate.database.entity;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Task {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String title;
    public Date date;
    public String description;
    public boolean done = false;
    public long userId;

    public Task(String title, Date date, String description, long userId) {
        this.title = title;
        this.date = date;
        this.description = description;
        this.userId = userId;
    }
}
