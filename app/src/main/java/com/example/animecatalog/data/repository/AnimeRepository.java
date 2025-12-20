package com.example.animecatalog.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.animecatalog.data.local.dao.AnimeDao;
import com.example.animecatalog.data.local.database.AppDatabase;
import com.example.animecatalog.data.local.entity.AnimeEntity;
import com.example.animecatalog.data.mapper.AnimeMapper;
import com.example.animecatalog.data.remote.RetrofitClient;
import com.example.animecatalog.data.remote.api.AnimeApiService;
import com.example.animecatalog.data.remote.dto.*;
import com.example.animecatalog.utils.Resource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AnimeRepository {
    private AnimeDao animeDao;
    private AnimeApiService apiService;
    private ExecutorService executorService;

    public AnimeRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        animeDao = database.animeDao();
        apiService = RetrofitClient.getClient().create(AnimeApiService.class);
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<Resource<List<AnimeEntity>>> getAnimeFromApi(String search, String type, String genre) {
        MutableLiveData<Resource<List<AnimeEntity>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        apiService.getAnime(search, type, genre, 1, 50).enqueue(new Callback<AnimeResponse>() {
            @Override
            public void onResponse(Call<AnimeResponse> call, Response<AnimeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<AnimeDto> dtoList = response.body().getData().getAnime();
                    List<AnimeEntity> entities = new ArrayList<>();

                    for (AnimeDto dto : dtoList) {
                        entities.add(AnimeMapper.toEntity(dto));
                    }

                    executorService.execute(() -> {
                        // Не удаляем всё, чтобы сохранить локально добавленные аниме
                        // Используем insertAll, который заменит существующие (REPLACE)
                        animeDao.insertAll(entities);
                    });

                    result.setValue(Resource.success(entities));
                } else {
                    result.setValue(Resource.error("Failed to load anime", null));
                }
            }

            @Override
            public void onFailure(Call<AnimeResponse> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<List<AnimeEntity>> getAllAnime() {
        return animeDao.getAllAnime();
    }

    public LiveData<AnimeEntity> getAnimeById(int id) {
        return animeDao.getAnimeById(id);
    }

    public LiveData<List<AnimeEntity>> getFavorites() {
        return animeDao.getFavorites();
    }

    public LiveData<List<AnimeEntity>> getFilteredAnime(String query, String type, String genre) {
        return animeDao.getFilteredAnime(query, type, genre);
    }

    // Локальные CRUD операции
    public void addAnime(AnimeEntity anime) {
        executorService.execute(() -> animeDao.insert(anime));
    }

    public void updateAnime(AnimeEntity anime) {
        executorService.execute(() -> animeDao.update(anime));
    }

    public void deleteAnime(AnimeEntity anime) {
        executorService.execute(() -> animeDao.delete(anime));
    }

    public LiveData<Resource<List<String>>> getGenres() {
        MutableLiveData<Resource<List<String>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        apiService.getGenres().enqueue(new Callback<GenresResponse>() {
            @Override
            public void onResponse(Call<GenresResponse> call, Response<GenresResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(Resource.success(response.body().getData()));
                } else {
                    result.setValue(Resource.error("Failed to load genres", null));
                }
            }

            @Override
            public void onFailure(Call<GenresResponse> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return result;
    }
}