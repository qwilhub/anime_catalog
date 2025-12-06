package com.example.animecatalog.data.repository;

import android.app.Application;
import androidx.lifecycle.MutableLiveData;
import com.example.animecatalog.data.local.dao.UserDao;
import com.example.animecatalog.data.local.database.AppDatabase;
import com.example.animecatalog.data.local.entity.UserEntity;
import com.example.animecatalog.data.remote.RetrofitClient;
import com.example.animecatalog.data.remote.api.AnimeApiService;
import com.example.animecatalog.data.remote.dto.*;
import com.example.animecatalog.utils.Resource;
import com.example.animecatalog.utils.SharedPrefsManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AuthRepository {
    private UserDao userDao;
    private AnimeApiService apiService;
    private SharedPrefsManager prefsManager;
    private ExecutorService executorService;

    public AuthRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        userDao = database.userDao();
        apiService = RetrofitClient.getClient().create(AnimeApiService.class);
        prefsManager = new SharedPrefsManager(application);
        executorService = Executors.newSingleThreadExecutor();
    }

    public MutableLiveData<Resource<AuthResponse.AuthData>> login(String email, String password) {
        MutableLiveData<Resource<AuthResponse.AuthData>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        LoginRequest request = new LoginRequest(email, password);
        apiService.login(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse.AuthData data = response.body().getData();

                    // Сохраняем токен
                    prefsManager.saveToken(data.getToken());
                    prefsManager.saveUserEmail(data.getEmail());

                    // Сохраняем в Room
                    executorService.execute(() -> {
                        UserEntity user = new UserEntity();
                        user.setUsername(data.getUsername());
                        user.setEmail(data.getEmail());
                        user.setToken(data.getToken());
                        userDao.insert(user);
                    });

                    result.setValue(Resource.success(data));
                } else {
                    result.setValue(Resource.error("Login failed", null));
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return result;
    }

    public MutableLiveData<Resource<AuthResponse.AuthData>> register(String username, String email, String password) {
        MutableLiveData<Resource<AuthResponse.AuthData>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        RegisterRequest request = new RegisterRequest(username, email, password);
        apiService.register(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(Resource.success(response.body().getData()));
                } else {
                    result.setValue(Resource.error("Registration failed", null));
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return result;
    }

    public void logout() {
        executorService.execute(() -> {
            userDao.deleteAll();
            prefsManager.clearToken();
        });
    }

    public boolean isLoggedIn() {
        return prefsManager.getToken() != null;
    }
}