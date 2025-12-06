package com.example.animecatalog.ui.auth;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.animecatalog.data.remote.dto.AuthResponse;
import com.example.animecatalog.data.repository.AuthRepository;
import com.example.animecatalog.utils.Resource;

public class AuthViewModel extends AndroidViewModel {
    private AuthRepository repository;

    public AuthViewModel(@NonNull Application application) {
        super(application);
        repository = new AuthRepository(application);
    }

    public LiveData<Resource<AuthResponse.AuthData>> login(String email, String password) {
        return repository.login(email, password);
    }

    public LiveData<Resource<AuthResponse.AuthData>> register(String username, String email, String password) {
        return repository.register(username, email, password);
    }

    public boolean isLoggedIn() {
        return repository.isLoggedIn();
    }
}