package com.example.tayang;

import com.google.gson.annotations.SerializedName;

public class Trailer {

    @SerializedName("key")
    private String key; // ID video YouTube

    @SerializedName("type")
    private String type;

    public String getKey() { return key; }
    public String getType() { return type; }
    public String getYoutubeUrl() {
        return "https://www.youtube.com/watch?v=" + key;
    }
}