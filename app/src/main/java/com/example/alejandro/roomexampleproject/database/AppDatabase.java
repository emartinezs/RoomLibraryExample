package com.example.alejandro.roomexampleproject.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.alejandro.roomexampleproject.database.daos.NoteDao;
import com.example.alejandro.roomexampleproject.database.daos.UserDao;
import com.example.alejandro.roomexampleproject.models.Note;
import com.example.alejandro.roomexampleproject.models.User;

@Database(entities = {User.class, Note.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase{
    private static final String DB_NAME = "notesDatabase.db";
    private static volatile AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context){
        if (instance == null){
            instance = create(context);
        }

        return instance;
    }

    private static AppDatabase create(Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, DB_NAME).build();
    }

    public abstract UserDao userDao();
    public abstract NoteDao noteDao();
}
