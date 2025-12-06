package com.example.animecatalog;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.animecatalog.databinding.ActivityMainBinding;
import com.example.animecatalog.ui.auth.LoginActivity;
import com.example.animecatalog.ui.catalog.CatalogActivity;
import com.example.animecatalog.ui.favorites.FavoritesActivity;
import com.example.animecatalog.utils.SharedPrefsManager;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private SharedPrefsManager prefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prefsManager = new SharedPrefsManager(this);

        setupToolbar();
        setupClickListeners();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Anime Catalog");
        }
    }

    private void setupClickListeners() {
        binding.cardCatalog.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, CatalogActivity.class));
        });

        binding.cardFavorites.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, FavoritesActivity.class));
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        prefsManager.clearToken();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
