package com.example.animecatalog.ui.catalog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCatalogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(CatalogViewModel.class);

        setupRecyclerView();
        setupSearchView();
        setupFilters();
        loadAnime();
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
        binding.searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                viewModel.setSearchQuery(query);
                observeSearchResults();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    viewModel.setSearchQuery("");
                    observeAllAnime();
                }
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
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    viewModel.setTypeFilter(type.equals("All") ? "" : type);
                    loadAnime();
                }
            });
            binding.chipGroupType.addView(chip);
        }

        // Load genres
        viewModel.getGenres().observe(this, resource -> {
            if (resource.getStatus() == Resource.Status.SUCCESS && resource.getData() != null) {
                binding.chipGroupGenre.removeAllViews();

                Chip allChip = new Chip(this);
                allChip.setText("All");
                allChip.setCheckable(true);
                allChip.setChecked(true);
                allChip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        viewModel.setGenreFilter("");
                        loadAnime();
                    }
                });
                binding.chipGroupGenre.addView(allChip);

                for (String genre : resource.getData()) {
                    Chip chip = new Chip(this);
                    chip.setText(genre);
                    chip.setCheckable(true);
                    chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        if (isChecked) {
                            viewModel.setGenreFilter(genre);
                            loadAnime();
                        }
                    });
                    binding.chipGroupGenre.addView(chip);
                }
            }
        });
    }

    private void loadAnime() {
        viewModel.loadAnime().observe(this, resource -> {
            if (resource.getStatus() == Resource.Status.LOADING) {
                showLoading(true);
            } else if (resource.getStatus() == Resource.Status.SUCCESS) {
                showLoading(false);
                observeAllAnime();
            } else if (resource.getStatus() == Resource.Status.ERROR) {
                showLoading(false);
                showError(resource.getMessage());
            }
        });
    }

    private void observeAllAnime() {
        viewModel.getAllAnime().observe(this, animeList -> {
            if (animeList != null && !animeList.isEmpty()) {
                adapter.setAnimeList(animeList);
                showEmpty(false);
            } else {
                showEmpty(true);
            }
        });
    }

    private void observeSearchResults() {
        viewModel.getSearchResults().observe(this, animeList -> {
            if (animeList != null && !animeList.isEmpty()) {
                adapter.setAnimeList(animeList);
                showEmpty(false);
            } else {
                showEmpty(true);
            }
        });
    }

    private void showLoading(boolean show) {
        binding.progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void showEmpty(boolean show) {
        binding.tvEmpty.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void showError(String message) {
        Toast.makeText(this, "Error: " + message, Toast.LENGTH_SHORT).show();
    }
}