package com.example.animecatalog.ui.details;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.animecatalog.data.local.entity.AnimeEntity;
import com.example.animecatalog.data.repository.AnimeRepository;

public class DetailsViewModel extends AndroidViewModel {
    private AnimeRepository repository;

    public DetailsViewModel(@NonNull Application application) {
        super(application);
        repository = new AnimeRepository(application);
    }

    public LiveData<AnimeEntity> getAnimeById(int id) {
        return repository.getAnimeById(id);
    }

    public void updateAnime(AnimeEntity anime) {
        repository.updateAnime(anime);
    }

    public void toggleFavorite(AnimeEntity anime) {
        anime.setFavorite(!anime.isFavorite());
        repository.updateAnime(anime);
    }

    public void updateFavorite(AnimeEntity anime, String comment, double rating) {
        anime.setComment(comment);
        anime.setUserRating(rating);
        repository.updateAnime(anime);
    }
}