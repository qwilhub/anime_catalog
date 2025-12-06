package com.example.animecatalog.data.remote.dto;

import java.util.List;

public class GenresResponse {
    private boolean success;
    private List<String> data;

    public boolean isSuccess() { return success; }
    public List<String> getData() { return data; }
}