package com.example.gezenapp;

import java.util.HashMap;
import java.util.Map;

public class Post {

    public Map<String, Boolean> stars = new HashMap<>();

    private String header;

    private String context;

    private String imageUrl;

    public Post() {
    }

    public Post(String header, String context, String imageUrl) {

        this.header = header;
        this.context = context;
        this.imageUrl = imageUrl;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("header", header);
        result.put("context", context);
        result.put("imageUrl", imageUrl);

        return result;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
