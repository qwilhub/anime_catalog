package com.example.animecatalog.ui.catalog;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
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
    private LiveData<List<AnimeEntity>> searchResults;

    public CatalogViewModel(@NonNull Application application) {
        super(application);
        repository = new AnimeRepository(application);

        MediatorLiveData<Void> filterTrigger = new MediatorLiveData<>();
        filterTrigger.setValue(null);
        
        filterTrigger.addSource(searchQuery, x -> filterTrigger.setValue(null));
        filterTrigger.addSource(genreFilter, x -> filterTrigger.setValue(null));
        filterTrigger.addSource(typeFilter, x -> filterTrigger.setValue(null));

        searchResults = Transformations.switchMap(filterTrigger, x -> {
            String query = searchQuery.getValue();
            String genre = genreFilter.getValue();
            String type = typeFilter.getValue();

            return repository.getFilteredAnime(query, type, genre);
        });
    }

    public LiveData<Resource<List<AnimeEntity>>> loadAnime() {
        return repository.getAnimeFromApi(
                searchQuery.getValue(),
                typeFilter.getValue(),
                genreFilter.getValue()
        );
    }

    public LiveData<List<AnimeEntity>> getSearchResults() {
        return searchResults;
    }

    public LiveData<Resource<List<String>>> getGenres() {
        return repository.getGenres();
    }

    // Локальные CRUD операции
    public void addAnime(AnimeEntity anime) {
        repository.addAnime(anime);
    }

    public void updateAnime(AnimeEntity anime) {
        repository.updateAnime(anime);
    }

    public void deleteAnime(AnimeEntity anime) {
        repository.deleteAnime(anime);
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
}