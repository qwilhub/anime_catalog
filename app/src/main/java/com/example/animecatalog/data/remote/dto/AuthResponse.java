package com.example.animecatalog.data.remote.dto;

public class AuthResponse {
    private boolean success;
    private String message;
    private AuthData data;

    public static class AuthData {
        private int userId;
        private String username;
        private String email;
        private String token;

        public int getUserId() { return userId; }
        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public String getToken() { return token; }
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public AuthData getData() { return data; }
}