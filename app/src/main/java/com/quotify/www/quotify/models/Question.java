package com.quotify.www.quotify.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Question {

    public String uid;
    public String author;
    public String body;
    public String answer;
    public long timestamp;
    public int rating;

    public Map<String, Boolean> stars = new HashMap<>();

    public Question() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Question(String uid, String author, String body, String answer, long timestamp) {
        this.uid = uid;
        this.author = author;
        this.body = body;
        this.answer = answer;
        this.timestamp = timestamp;
        this.rating = 0;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("timestamp", timestamp);
        result.put("body", body);
        result.put("answer", answer);
        result.put("rating", rating);

        return result;
    }

}
