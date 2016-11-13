package com.quotify.www.quotify.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.math.BigInteger;

// [START blog_user_class]
@IgnoreExtraProperties
public class User {

    public String username;
    public String email;

    private BigInteger correct;
    private BigInteger incorrect;

    public BigInteger getIncorrect() {
        return incorrect;
    }

    public void setIncorrect(BigInteger incorrect) {
        this.incorrect = incorrect;
    }

    public BigInteger getCorrect() {
        return correct;
    }

    public void setCorrect(BigInteger correct) {
        this.correct = correct;
    }

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;

        // User starts out having answered no questions.
        this.correct = BigInteger.ZERO;
        this.incorrect = BigInteger.ZERO;
    }

}
// [END blog_user_class]
