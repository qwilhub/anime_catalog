package com.example.animecatalog.data.remote.api;

import com.example.animecatalog.data.remote.dto.*;
import retrofit2.Call;
import retrofit2.http.*;

public interface AnimeApiService {

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