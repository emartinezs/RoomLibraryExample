package com.example.alejandro.roomexampleproject.database.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.alejandro.roomexampleproject.models.Note;

import java.util.List;

@Dao
public interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Note note);

    @Update
    void update(Note... notes);

    @Delete
    void delete(Note... notes);

    @Query("SELECT * FROM Note")
    List<Note> getAll();

    @Query("SELECT * FROM Note WHERE id=(:id)")
    Note findNoteById(int id);

    @Query("SELECT * FROM Note WHERE user_id=(:userId)")
    List<Note> getAllNotesByUser(int userId);
}
