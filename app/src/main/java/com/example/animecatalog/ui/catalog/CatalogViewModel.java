package com.example.animecatalog.ui.catalog;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import com.example.animecatalog.data.local.entity.AnimeEntity;
import com.example.animecatalog.data.repository.AnimeRepository;
import com.example.animecatalog.utils.Resource;
import java.util.List;

public class CatalogViewModel extends AndroidViewModel {
    private AnimeRepository repository;
    private MutableLiveData<String> searchQuery = new MutableLiveData<>("");
    private MutableLiveData<String> typeFilter = new MutableLiveData<>("");
    private MutableLiveData<String> genreFilter = new MutableLiveData<>("");

    public CatalogViewModel(@NonNull Application application) {
        super(application);
        repository = new AnimeRepository(application);
    }

    public LiveData<Resource<List<AnimeEntity>>> loadAnime() {
        return repository.getAnimeFromApi(
                searchQuery.getValue(),
                typeFilter.getValue(),
                genreFilter.getValue()
        );
    }

    public LiveData<List<AnimeEntity>> getAllAnime() {
        return repository.getAllAnime();
    }

    public LiveData<List<AnimeEntity>> getSearchResults() {
        return Transformations.switchMap(searchQuery, query -> {
            if (query == null || query.isEmpty()) {
                return repository.getAllAnime();
            } else {
                return repository.searchAnime(query);
            }
        });
    }

    public LiveData<Resource<List<String>>> getGenres() {
        return repository.getGenres();
    }

    public void setSearchQuery(String query) {
        searchQuery.setValue(query);
    }

    public void setTypeFilter(String type) {
        typeFilter.setValue(type);
    }

    public void setGenreFilter(String genre) {
        genreFilter.setValue(genre);
    }

    public String getCurrentSearchQuery() {
        return searchQuery.getValue();
    }

    public String getCurrentTypeFilter() {
        return typeFilter.getValue();
    }

    public String getCurrentGenreFilter() {
        return genreFilter.getValue();
    }
}