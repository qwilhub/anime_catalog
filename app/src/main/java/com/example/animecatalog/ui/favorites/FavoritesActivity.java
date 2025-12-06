package com.example.animecatalog.ui.favorites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.animecatalog.databinding.ActivityFavoritesBinding;
import com.example.animecatalog.databinding.DialogFavoriteBinding;
import com.example.animecatalog.data.local.entity.AnimeEntity;
import com.example.animecatalog.ui.details.DetailsActivity;
import com.example.animecatalog.ui.favorites.adapter.FavoritesAdapter;

public class FavoritesActivity extends AppCompatActivity {
    private ActivityFavoritesBinding binding;
    private FavoritesViewModel viewModel;
    private FavoritesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoritesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);

        setupToolbar();
        setupRecyclerView();
        observeFavorites();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("My Favorites");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        adapter = new FavoritesAdapter(new FavoritesAdapter.OnFavoriteActionListener() {
            @Override
            public void onFavoriteClick(AnimeEntity anime) {
                Intent intent = new Intent(FavoritesActivity.this, DetailsActivity.class);
                intent.putExtra("ANIME_ID", anime.getId());
                startActivity(intent);
            }

            @Override
            public void onRemoveClick(AnimeEntity anime) {
                showRemoveDialog(anime);
            }

            @Override
            public void onEditClick(AnimeEntity anime) {
                showEditDialog(anime);
            }
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }

    private void observeFavorites() {
        viewModel.getFavorites().observe(this, favorites -> {
            if (favorites != null && !favorites.isEmpty()) {
                adapter.setFavoritesList(favorites);
                showEmpty(false);
            } else {
                showEmpty(true);
            }
        });
    }

    private void showRemoveDialog(AnimeEntity anime) {
        new AlertDialog.Builder(this)
                .setTitle("Remove from Favorites")
                .setMessage("Are you sure you want to remove \"" + anime.getTitle() + "\" from favorites?")
                .setPositiveButton("Remove", (dialog, which) -> {
                    viewModel.removeFromFavorites(anime);
                    Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showEditDialog(AnimeEntity anime) {
        DialogFavoriteBinding dialogBinding = DialogFavoriteBinding.inflate(getLayoutInflater());

        // Pre-fill with existing data
        dialogBinding.etComment.setText(anime.getComment());
        dialogBinding.ratingBar.setRating((float) anime.getUserRating());

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Edit Review")
                .setView(dialogBinding.getRoot())
                .create();

        dialogBinding.btnSave.setOnClickListener(v -> {
            String comment = dialogBinding.etComment.getText().toString();
            float rating = dialogBinding.ratingBar.getRating();

            viewModel.updateFavorite(anime, comment, rating);

            Toast.makeText(this, "Review updated!", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialogBinding.btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void showEmpty(boolean show) {
        binding.tvEmpty.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}