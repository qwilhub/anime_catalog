package com.example.animecatalog.data.mapper;

import com.example.animecatalog.data.local.entity.AnimeEntity;
import com.example.animecatalog.data.remote.dto.AnimeDto;

public class AnimeMapper {
    public static AnimeEntity toEntity(AnimeDto dto) {
        AnimeEntity entity = new AnimeEntity();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setTitleJapanese(dto.getTitleJapanese());
        entity.setType(dto.getType());
        entity.setEpisodes(dto.getEpisodes());
        entity.setRating(dto.getRating());
        entity.setGenres(dto.getGenres());
        entity.setStudio(dto.getStudio());
        entity.setYear(dto.getYear());
        entity.setStatus(dto.getStatus());
        entity.setImageUrl(dto.getImageUrl());
        entity.setSynopsis(dto.getSynopsis());
        entity.setFavorite(false);
        entity.setComment("");
        entity.setUserRating(0.0);
        return entity;
    }
}