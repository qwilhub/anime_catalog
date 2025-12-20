package com.example.animecatalog.data.local.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.example.animecatalog.data.local.converter.StringListConverter;
import com.example.animecatalog.data.local.dao.AnimeDao;
import com.example.animecatalog.data.local.entity.AnimeEntity;

@Database(entities = {AnimeEntity.class}, version = 3, exportSchema = false)
@TypeConverters(StringListConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract AnimeDao animeDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "anime_database"
            ).fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}