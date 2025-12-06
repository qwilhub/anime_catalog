package com.example.animecatalog.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.animecatalog.data.local.entity.AnimeEntity;
import java.util.List;

@Dao
public interface AnimeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<AnimeEntity> animeList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AnimeEntity anime);

    @Update
    void update(AnimeEntity anime);

    @Query("SELECT * FROM anime")
    LiveData<List<AnimeEntity>> getAllAnime();

    @Query("SELECT * FROM anime WHERE id = :id")
    LiveData<AnimeEntity> getAnimeById(int id);

    @Query("SELECT * FROM anime WHERE isFavorite = 1")
    LiveData<List<AnimeEntity>> getFavorites();

    @Query("SELECT * FROM anime WHERE title LIKE '%' || :query || '%'")
    LiveData<List<AnimeEntity>> searchAnime(String query);

    @Query("DELETE FROM anime")
    void deleteAll();
}