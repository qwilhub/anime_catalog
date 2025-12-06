package com.example.animecatalog.data.remote.dto;

import java.util.List;

public class AnimeResponse {
    private boolean success;
    private ResponseData data;

    public static class ResponseData {
        private List<AnimeDto> anime;
        private Pagination pagination;

        public List<AnimeDto> getAnime() { return anime; }
        public Pagination getPagination() { return pagination; }
    }

    public static class Pagination {
        private int page;
        private int limit;
        private int total;
        private int totalPages;
    }

    public boolean isSuccess() { return success; }
    public ResponseData getData() { return data; }
}