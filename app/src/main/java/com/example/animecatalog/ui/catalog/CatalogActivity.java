package com.example.animecatalog.ui.catalog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import com.example.animecatalog.data.local.entity.AnimeEntity;
import com.example.animecatalog.databinding.ActivityCatalogBinding;
import com.example.animecatalog.databinding.DialogAddEditAnimeBinding;
import com.example.animecatalog.databinding.DialogConfirmDeleteBinding;
import com.example.animecatalog.ui.catalog.adapter.AnimeAdapter;
import com.example.animecatalog.ui.details.DetailsActivity;
import com.example.animecatalog.utils.Resource;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CatalogActivity extends AppCompatActivity {
    private ActivityCatalogBinding binding;
    private CatalogViewModel viewModel;
    private AnimeAdapter adapter;

    private static final String[] TYPES_ARRAY = {"TV", "Movie", "OVA", "Special", "ONA"};
    private static final String[] GENRES_ARRAY = {
            "Action", "Adventure", "Comedy", "Drama", "Fantasy", "Horror",
            "Mystery", "Psychological", "Romance", "Sci-Fi", "Slice of Life",
            "Sports", "Supernatural", "Thriller"
    };

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
        
        observeSearchResults();
        loadAnime();
    }
    
    private void setupUI() {
        binding.btnBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        binding.fabAdd.setOnClickListener(v -> showAddEditBottomSheet(null));
    }

    private void setupRecyclerView() {
        adapter = new AnimeAdapter(new AnimeAdapter.OnAnimeClickListener() {
            @Override
            public void onAnimeClick(AnimeEntity anime) {
                Intent intent = new Intent(CatalogActivity.this, DetailsActivity.class);
                intent.putExtra("ANIME_ID", anime.getId());
                startActivity(intent);
            }

            @Override
            public void onAnimeLongClick(AnimeEntity anime) {
                showOptionsDialog(anime);
            }
        });

        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        binding.recyclerView.setAdapter(adapter);
    }

    private void showOptionsDialog(AnimeEntity anime) {
        String[] options = {"Edit", "Delete"};
        new AlertDialog.Builder(this)
                .setTitle(anime.getTitle())
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        showAddEditBottomSheet(anime);
                    } else {
                        showDeleteConfirmDialog(anime);
                    }
                })
                .show();
    }

    private void showDeleteConfirmDialog(AnimeEntity anime) {
        DialogConfirmDeleteBinding deleteBinding = DialogConfirmDeleteBinding.inflate(getLayoutInflater());
        deleteBinding.tvDeleteMessage.setText("Are you sure you want to delete \"" + anime.getTitle() + "\"? This action cannot be undone.");

        AlertDialog dialog = new AlertDialog.Builder(this, com.google.android.material.R.style.MaterialAlertDialog_MaterialComponents_Title_Icon)
                .setView(deleteBinding.getRoot())
                .create();

        deleteBinding.btnCancelDelete.setOnClickListener(v -> dialog.dismiss());
        deleteBinding.btnConfirmDelete.setOnClickListener(v -> {
            viewModel.deleteAnime(anime);
            showSnackbar("Anime deleted");
            dialog.dismiss();
        });

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    private void showAddEditBottomSheet(AnimeEntity anime) {
        DialogAddEditAnimeBinding dialogBinding = DialogAddEditAnimeBinding.inflate(getLayoutInflater());
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, com.google.android.material.R.style.Theme_Design_BottomSheetDialog);
        bottomSheetDialog.setContentView(dialogBinding.getRoot());

        boolean isEdit = anime != null;
        dialogBinding.tvDialogTitle.setText(isEdit ? "Edit Anime" : "Add New Anime");
        dialogBinding.btnSaveAnime.setText(isEdit ? "Update Changes" : "Save Anime");

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, TYPES_ARRAY);
        dialogBinding.actvType.setAdapter(typeAdapter);

        ArrayAdapter<String> genreAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, GENRES_ARRAY);
        dialogBinding.actvGenre.setAdapter(genreAdapter);

        if (isEdit) {
            dialogBinding.etTitle.setText(anime.getTitle());
            dialogBinding.actvType.setText(anime.getType(), false);
            dialogBinding.etRating.setText(String.valueOf(anime.getRating()));
            dialogBinding.etEpisodes.setText(String.valueOf(anime.getEpisodes()));
            dialogBinding.etImageUrl.setText(anime.getImageUrl());
            if (anime.getGenres() != null && !anime.getGenres().isEmpty()) {
                dialogBinding.actvGenre.setText(anime.getGenres().get(0), false);
            }
        }

        dialogBinding.btnSaveAnime.setOnClickListener(v -> {
            String title = dialogBinding.etTitle.getText().toString();
            String type = dialogBinding.actvType.getText().toString();
            String ratingStr = dialogBinding.etRating.getText().toString();
            String episodesStr = dialogBinding.etEpisodes.getText().toString();
            String imageUrl = dialogBinding.etImageUrl.getText().toString();
            String genre = dialogBinding.actvGenre.getText().toString();

            if (title.isEmpty()) {
                dialogBinding.etTitle.setError("Title is required");
                return;
            }

            AnimeEntity target = isEdit ? anime : new AnimeEntity();
            target.setTitle(title);
            target.setType(type);
            target.setRating(ratingStr.isEmpty() ? 0.0 : Double.parseDouble(ratingStr));
            target.setEpisodes(episodesStr.isEmpty() ? 0 : Integer.parseInt(episodesStr));
            target.setImageUrl(imageUrl);
            if (!genre.isEmpty()) target.setGenres(Arrays.asList(genre));

            if (isEdit) viewModel.updateAnime(target);
            else viewModel.addAnime(target);

            showSnackbar(isEdit ? "Anime updated" : "Anime added");
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }

    private void showSnackbar(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
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
        List<String> types = Arrays.asList("All", "TV", "Movie", "OVA", "Special");
        for (String type : types) {
            Chip chip = new Chip(this);
            chip.setText(type);
            chip.setCheckable(true);
            if (type.equals("All")) chip.setChecked(true);
            
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    viewModel.setTypeFilter(type.equals("All") ? "" : type);
                }
            });
            binding.chipGroupType.addView(chip);
        }

        viewModel.getGenres().observe(this, resource -> {
            if (resource.getStatus() == Resource.Status.SUCCESS && resource.getData() != null && !resource.getData().isEmpty()) {
                populateGenreChips(resource.getData());
            } else if (resource.getStatus() == Resource.Status.ERROR) {
                populateGenreChips(Arrays.asList(GENRES_ARRAY));
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
                }
            });
            binding.chipGroupGenre.addView(chip);
        }
    }

    private void observeSearchResults() {
        viewModel.getSearchResults().observe(this, animeList -> {
            if (animeList != null && !animeList.isEmpty()) {
                adapter.setAnimeList(animeList);
                showEmpty(false);
            } else {
                adapter.setAnimeList(new ArrayList<>());
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