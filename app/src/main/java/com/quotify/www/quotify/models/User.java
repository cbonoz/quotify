package com.quotify.www.quotify.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

// [START blog_user_class]
@IgnoreExtraProperties
public class User {

    public String username;
    public String email;

    public List<Question> correct;
    public List<Question> incorrect;

        public User() {
        // Default constructor required for calls to DataSnapshot.getValue(DummyItem.class)
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;

        // DummyItem starts out having answered no questions.
        this.correct = new ArrayList<>();
        this.incorrect = new ArrayList<>();
    }

}
// [END blog_user_class]
