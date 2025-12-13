package com.example.animecatalog.ui.catalog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import com.example.animecatalog.databinding.ActivityCatalogBinding;
import com.example.animecatalog.ui.catalog.adapter.AnimeAdapter;
import com.example.animecatalog.ui.details.DetailsActivity;
import com.example.animecatalog.utils.Resource;
import com.google.android.material.chip.Chip;
import java.util.Arrays;
import java.util.List;

public class CatalogActivity extends AppCompatActivity {
    private ActivityCatalogBinding binding;
    private CatalogViewModel viewModel;
    private AnimeAdapter adapter;

    // Список жанров на случай, если API не вернет список, но мы знаем какие жанры поддерживаются
    private static final List<String> FALLBACK_GENRES = Arrays.asList(
            "Action", "Adventure", "Comedy", "Drama", "Fantasy", "Horror",
            "Mystery", "Psychological", "Romance", "Sci-Fi", "Slice of Life",
            "Sports", "Supernatural", "Thriller"
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCatalogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(CatalogViewModel.class);

        setupUI();
        setupRecyclerView();
        setupSearchView();
        setupFilters();
        
        // Подписываемся на результаты поиска/фильтрации
        observeSearchResults();
        
        // Загружаем данные с сервера
        loadAnime();
    }
    
    private void setupUI() {
        // Настройка кнопки назад
        binding.btnBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
    }

    private void setupRecyclerView() {
        adapter = new AnimeAdapter(anime -> {
            Intent intent = new Intent(CatalogActivity.this, DetailsActivity.class);
            intent.putExtra("ANIME_ID", anime.getId());
            startActivity(intent);
        });

        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        binding.recyclerView.setAdapter(adapter);
    }

    private void setupSearchView() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                viewModel.setSearchQuery(query);
                binding.searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                viewModel.setSearchQuery(newText);
                return true;
            }
        });
    }

    private void setupFilters() {
        // Type filter chips
        List<String> types = Arrays.asList("All", "TV", "Movie", "OVA", "Special");
        for (String type : types) {
            Chip chip = new Chip(this);
            chip.setText(type);
            chip.setCheckable(true);
            if (type.equals("All")) chip.setChecked(true);
            
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    viewModel.setTypeFilter(type.equals("All") ? "" : type);
                    // Перезагружаем данные с API с новым фильтром типа
                    loadAnime();
                }
            });
            binding.chipGroupType.addView(chip);
        }

        // Загружаем жанры
        viewModel.getGenres().observe(this, resource -> {
            if (resource.getStatus() == Resource.Status.SUCCESS && resource.getData() != null && !resource.getData().isEmpty()) {
                populateGenreChips(resource.getData());
            } else if (resource.getStatus() == Resource.Status.ERROR) {
                // Если ошибка загрузки жанров, используем локальный список
                populateGenreChips(FALLBACK_GENRES);
            }
        });
    }

    private void populateGenreChips(List<String> genres) {
        binding.chipGroupGenre.removeAllViews();

        Chip allChip = new Chip(this);
        allChip.setText("All Genres");
        allChip.setCheckable(true);
        allChip.setChecked(true);
        allChip.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                viewModel.setGenreFilter("");
                // Можно вызвать loadAnime() если хотим подгрузить с сервера без фильтра
                // loadAnime(); 
            }
        });
        binding.chipGroupGenre.addView(allChip);

        for (String genre : genres) {
            Chip chip = new Chip(this);
            chip.setText(genre);
            chip.setCheckable(true);
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    viewModel.setGenreFilter(genre);
                    // Опционально: загружаем с сервера конкретный жанр
                    // loadAnime();
                }
            });
            binding.chipGroupGenre.addView(chip);
        }
    }

    private void observeSearchResults() {
        // Подписываемся на getSearchResults(), который автоматически фильтрует данные из БД
        // на основе введенного текста и выбранного жанра
        viewModel.getSearchResults().observe(this, animeList -> {
            if (animeList != null && !animeList.isEmpty()) {
                adapter.setAnimeList(animeList);
                showEmpty(false);
            } else {
                adapter.setAnimeList(java.util.Collections.emptyList());
                showEmpty(true);
            }
        });
    }

    private void loadAnime() {
        viewModel.loadAnime().observe(this, resource -> {
            if (resource.getStatus() == Resource.Status.LOADING) {
                showLoading(true);
            } else if (resource.getStatus() == Resource.Status.SUCCESS) {
                showLoading(false);
                // Данные обновятся через observeSearchResults
            } else if (resource.getStatus() == Resource.Status.ERROR) {
                showLoading(false);
                showError(resource.getMessage());
            }
        });
    }

    private void showLoading(boolean show) {
        binding.progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        if (show && adapter.getItemCount() == 0) {
             binding.recyclerView.setVisibility(View.GONE);
        } else {
             binding.recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void showEmpty(boolean show) {
        binding.tvEmpty.setVisibility(show ? View.VISIBLE : View.GONE);
        if (show) {
            binding.recyclerView.setVisibility(View.GONE);
        } else {
            binding.recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void showError(String message) {
        Toast.makeText(this, "Error: " + message, Toast.LENGTH_SHORT).show();
    }
}