package com.morax.productimate.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Note {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String title;
    public String content;
    public long userId;

    public Note(String title, String content, long userId) {
        this.title = title;
        this.content = content;
        this.userId = userId;
    }
}
