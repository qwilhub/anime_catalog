package com.example.animecatalog;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.animecatalog.databinding.ActivityMainBinding;
import com.example.animecatalog.ui.catalog.CatalogActivity;
import com.example.animecatalog.ui.favorites.FavoritesActivity;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupClickListeners();
    }

    private void setupClickListeners() {
        binding.cardCatalog.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, CatalogActivity.class));
        });

        binding.cardFavorites.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, FavoritesActivity.class));
        });
        
        // Кнопка выхода удалена, обработчик не нужен
    }
}