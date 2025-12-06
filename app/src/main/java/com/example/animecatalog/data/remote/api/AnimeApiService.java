package com.example.animecatalog.data.remote.api;

import com.example.animecatalog.data.remote.dto.*;
import retrofit2.Call;
import retrofit2.http.*;

public interface AnimeApiService {
    @POST("api/auth/register")
    Call<AuthResponse> register(@Body RegisterRequest request);

    @POST("api/auth/login")
    Call<AuthResponse> login(@Body LoginRequest request);

    @GET("api/anime")
    Call<AnimeResponse> getAnime(
            @Query("search") String search,
            @Query("type") String type,
            @Query("genre") String genre,
            @Query("page") Integer page,
            @Query("limit") Integer limit
    );

    @GET("api/anime/{id}")
    Call<AnimeDetailResponse> getAnimeById(@Path("id") int id);

    @GET("api/genres")
    Call<GenresResponse> getGenres();
}