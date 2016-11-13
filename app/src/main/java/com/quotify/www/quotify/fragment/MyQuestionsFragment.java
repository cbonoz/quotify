package com.quotify.www.quotify.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

// Most similar to the MyTopPostsFragment (but for questions).
public class MyQuestionsFragment extends PostListFragment {

    public MyQuestionsFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // All my posts
        return databaseReference.child("user-questions")
                .child(getUid());
    }
}
