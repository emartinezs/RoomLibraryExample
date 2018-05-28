package com.example.alejandro.roomexampleproject.database.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.alejandro.roomexampleproject.models.Category;
import com.example.alejandro.roomexampleproject.models.Note;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Category category);

    @Update
    void update(Category... categories);

    @Delete
    void delete(Category... categories);

    @Query("SELECT * FROM Category")
    List<Category> getAll();

    @Query("SELECT * FROM Category WHERE id=(:id)")
    Note findCategoryById(int id);

    @Query("SELECT * FROM Category WHERE user_id=(:userId)")
    List<Note> getAllCategoriesByUser(int userId);
}
