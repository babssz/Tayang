package com.example.tayang;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class VideoResponse {

    @SerializedName("results")
    private List<Trailer> results;

    public List<Trailer> getResults() { return results; }
}