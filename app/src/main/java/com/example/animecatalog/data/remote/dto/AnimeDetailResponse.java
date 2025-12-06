package com.example.animecatalog.data.remote.dto;

public class AnimeDetailResponse {
    private boolean success;
    private AnimeDto data;

    public boolean isSuccess() { return success; }
    public AnimeDto getData() { return data; }
}