package com.example.animecatalog.ui.favorites;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.animecatalog.data.local.entity.AnimeEntity;
import com.example.animecatalog.data.repository.AnimeRepository;
import java.util.List;

public class FavoritesViewModel extends AndroidViewModel {
    private AnimeRepository repository;

    public FavoritesViewModel(@NonNull Application application) {
        super(application);
        repository = new AnimeRepository(application);
    }

    public LiveData<List<AnimeEntity>> getFavorites() {
        return repository.getFavorites();
    }

    public void removeFromFavorites(AnimeEntity anime) {
        anime.setFavorite(false);
        repository.updateAnime(anime);
    }

    public void updateFavorite(AnimeEntity anime, String comment, double rating) {
        anime.setComment(comment);
        anime.setUserRating(rating);
        repository.updateAnime(anime);
    }
}