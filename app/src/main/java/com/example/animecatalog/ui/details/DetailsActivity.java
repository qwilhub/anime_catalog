package com.example.animecatalog.ui.details;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.example.animecatalog.R;
import com.example.animecatalog.databinding.ActivityDetailsBinding;
import com.example.animecatalog.databinding.DialogFavoriteBinding;
import com.example.animecatalog.data.local.entity.AnimeEntity;

public class DetailsActivity extends AppCompatActivity {
    private ActivityDetailsBinding binding;
    private DetailsViewModel viewModel;
    private AnimeEntity currentAnime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(DetailsViewModel.class);

        int animeId = getIntent().getIntExtra("ANIME_ID", -1);
        if (animeId != -1) {
            loadAnimeDetails(animeId);
        }

        setupListeners();
    }

    private void loadAnimeDetails(int id) {
        viewModel.getAnimeById(id).observe(this, anime -> {
            if (anime != null) {
                currentAnime = anime;
                displayAnimeDetails(anime);
            }
        });
    }

    private void displayAnimeDetails(AnimeEntity anime) {
        binding.tvTitle.setText(anime.getTitle());
        binding.tvTitleJapanese.setText(anime.getTitleJapanese());
        binding.tvType.setText(anime.getType());
        binding.tvEpisodes.setText(anime.getEpisodes() + " episodes");
        binding.tvRating.setText("★ " + anime.getRating());
        binding.tvStudio.setText(anime.getStudio());
        binding.tvYear.setText(String.valueOf(anime.getYear()));
        binding.tvStatus.setText(anime.getStatus());
        binding.tvSynopsis.setText(anime.getSynopsis());

        if (anime.getGenres() != null) {
            binding.tvGenres.setText(String.join(", ", anime.getGenres()));
        }

        Glide.with(this)
                .load(anime.getImageUrl())
                .placeholder(R.drawable.placeholder_anime)
                .into(binding.ivPoster);

        updateFavoriteButton(anime.isFavorite());
    }

    private void setupListeners() {
        binding.fabFavorite.setOnClickListener(v -> {
            if (currentAnime != null) {
                if (currentAnime.isFavorite()) {
                    viewModel.toggleFavorite(currentAnime);
                    Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show();
                } else {
                    showFavoriteDialog();
                }
            }
        });

        binding.btnBack.setOnClickListener(v -> finish());
    }

    private void showFavoriteDialog() {
        DialogFavoriteBinding dialogBinding = DialogFavoriteBinding.inflate(getLayoutInflater());

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogBinding.getRoot())
                .create();

        dialogBinding.btnSave.setOnClickListener(v -> {
            String comment = dialogBinding.etComment.getText().toString();
            float rating = dialogBinding.ratingBar.getRating();

            currentAnime.setFavorite(true);
            viewModel.updateFavorite(currentAnime, comment, rating);

            Toast.makeText(this, "Added to favorites!", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialogBinding.btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void updateFavoriteButton(boolean isFavorite) {
        binding.fabFavorite.setImageResource(
                isFavorite ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite_border
        );
    }
}
