package com.example.alejandro.roomexampleproject.database.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.alejandro.roomexampleproject.models.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User... users);

    @Delete
    void delete(User user);

    @Update
    void update(User... users);

    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE id IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM user WHERE username LIKE (:username) LIMIT 1")
    User findByUsername(String username);

    @Query("SELECT * FROM user WHERE first_name LIKE (:name) LIMIT 1")
    User findByName(String name);

    @Query("SELECT * FROM user WHERE last_name LIKE (:lastName) LIMIT 1")
    User findByLastName(String lastName);

    @Query("SELECT * FROM user WHERE first_name LIKE (:first) AND last_name LIKE (:last) LIMIT 1")
    User findByFullName(String first, String last);
}
