package com.example.animecatalog.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.animecatalog.MainActivity;
import com.example.animecatalog.databinding.ActivityLoginBinding;
import com.example.animecatalog.utils.Resource;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private AuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        if (viewModel.isLoggedIn()) {
            navigateToMain();
            return;
        }

        setupListeners();
    }

    private void setupListeners() {
        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            performLogin(email, password);
        });

        binding.tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void performLogin(String email, String password) {
        viewModel.login(email, password).observe(this, resource -> {
            if (resource.getStatus() == Resource.Status.LOADING) {
                showLoading(true);
            } else if (resource.getStatus() == Resource.Status.SUCCESS) {
                showLoading(false);
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                navigateToMain();
            } else if (resource.getStatus() == Resource.Status.ERROR) {
                showLoading(false);
                Toast.makeText(this, "Login failed: " + resource.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoading(boolean show) {
        binding.progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.btnLogin.setEnabled(!show);
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}