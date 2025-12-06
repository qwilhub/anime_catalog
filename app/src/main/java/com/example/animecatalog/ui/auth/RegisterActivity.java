package com.example.animecatalog.ui.auth;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.animecatalog.databinding.ActivityRegisterBinding;
import com.example.animecatalog.utils.Resource;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private AuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        setupListeners();
    }

    private void setupListeners() {
        binding.btnRegister.setOnClickListener(v -> {
            String username = binding.etUsername.getText().toString().trim();
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();
            String confirmPassword = binding.etConfirmPassword.getText().toString().trim();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                return;
            }

            performRegister(username, email, password);
        });

        binding.tvLogin.setOnClickListener(v -> finish());
    }

    private void performRegister(String username, String email, String password) {
        viewModel.register(username, email, password).observe(this, resource -> {
            if (resource.getStatus() == Resource.Status.LOADING) {
                showLoading(true);
            } else if (resource.getStatus() == Resource.Status.SUCCESS) {
                showLoading(false);
                Toast.makeText(this, "Registration successful! Please login.", Toast.LENGTH_SHORT).show();
                finish();
            } else if (resource.getStatus() == Resource.Status.ERROR) {
                showLoading(false);
                Toast.makeText(this, "Registration failed: " + resource.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoading(boolean show) {
        binding.progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.btnRegister.setEnabled(!show);
    }
}