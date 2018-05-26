package com.example.alejandro.roomexampleproject.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = User.class,
                                            parentColumns = "id",
                                            childColumns = "user_id",
                                            onDelete = ForeignKey.CASCADE))
public class Note {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "data")
    private String data;

    public Note() {
    }

    public Note(String data, int userId) {
        this.data = data;
        this.userId = userId;
    }


    @ColumnInfo(name = "user_id")
    private int userId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
