package com.example.animecatalog.data.local.dao;

import androidx.room.*;
import com.example.animecatalog.data.local.entity.UserEntity;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserEntity user);

    @Query("SELECT * FROM users LIMIT 1")
    UserEntity getUser();

    @Query("DELETE FROM users")
    void deleteAll();
}