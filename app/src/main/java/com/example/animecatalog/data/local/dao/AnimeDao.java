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

    @Query("SELECT * FROM anime WHERE genres LIKE '%' || :genre || '%'")
    LiveData<List<AnimeEntity>> getAnimeByGenre(String genre);

    @Query("SELECT * FROM anime WHERE title LIKE '%' || :query || '%' AND genres LIKE '%' || :genre || '%'")
    LiveData<List<AnimeEntity>> searchAnimeByTitleAndGenre(String query, String genre);

    // Универсальный метод фильтрации
    // Проверяем каждое условие: если параметр пустой, то условие игнорируется (возвращает true)
    @Query("SELECT * FROM anime WHERE " +
           "(:query IS NULL OR :query = '' OR title LIKE '%' || :query || '%') AND " +
           "(:type IS NULL OR :type = '' OR type = :type) AND " +
           "(:genre IS NULL OR :genre = '' OR genres LIKE '%' || :genre || '%')")
    LiveData<List<AnimeEntity>> getFilteredAnime(String query, String type, String genre);

    @Query("DELETE FROM anime")
    void deleteAll();
}