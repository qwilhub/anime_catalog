package com.example.animecatalog.data.remote.dto;

import java.util.List;

public class AnimeDto {
    private int id;
    private String title;
    private String titleJapanese;
    private String type;
    private int episodes;
    private double rating;
    private List<String> genres;
    private String studio;
    private int year;
    private String status;
    private String imageUrl;
    private String synopsis;

    // Геттеры
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getTitleJapanese() { return titleJapanese; }
    public String getType() { return type; }
    public int getEpisodes() { return episodes; }
    public double getRating() { return rating; }
    public List<String> getGenres() { return genres; }
    public String getStudio() { return studio; }
    public int getYear() { return year; }
    public String getStatus() { return status; }
    public String getImageUrl() { return imageUrl; }
    public String getSynopsis() { return synopsis; }
}