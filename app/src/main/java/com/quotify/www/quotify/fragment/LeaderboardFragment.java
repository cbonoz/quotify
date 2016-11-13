package com.quotify.www.quotify.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class LeaderboardFragment extends QuestionListFragment {

    public LeaderboardFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        final int NUM_LEADERS = 25;
        // Get top users by number of correct answers.
        return databaseReference
                .child("users")
                .orderByChild("correct")
                .limitToFirst(NUM_LEADERS);
    }
}
