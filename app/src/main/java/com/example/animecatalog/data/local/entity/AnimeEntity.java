package com.example.animecatalog.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.example.animecatalog.data.local.converter.StringListConverter;
import java.util.List;

@Entity(tableName = "anime")
@TypeConverters(StringListConverter.class)
public class AnimeEntity {
    @PrimaryKey(autoGenerate = true)
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
    private boolean isFavorite;
    private String comment;
    private double userRating;

    // Конструктор
    public AnimeEntity() {}

    // Геттеры и сеттеры
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getTitleJapanese() { return titleJapanese; }
    public void setTitleJapanese(String titleJapanese) { this.titleJapanese = titleJapanese; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public int getEpisodes() { return episodes; }
    public void setEpisodes(int episodes) { this.episodes = episodes; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public List<String> getGenres() { return genres; }
    public void setGenres(List<String> genres) { this.genres = genres; }

    public String getStudio() { return studio; }
    public void setStudio(String studio) { this.studio = studio; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getSynopsis() { return synopsis; }
    public void setSynopsis(String synopsis) { this.synopsis = synopsis; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public double getUserRating() { return userRating; }
    public void setUserRating(double userRating) { this.userRating = userRating; }
}