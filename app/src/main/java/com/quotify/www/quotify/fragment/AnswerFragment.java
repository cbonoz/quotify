package com.quotify.www.quotify.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.quotify.www.quotify.R;
import com.quotify.www.quotify.models.Question;
import com.quotify.www.quotify.models.User;
import com.quotify.www.quotify.viewholder.QuestionViewHolder;

import java.util.List;

public class AnswerFragment extends Fragment {

    private static final String TAG = "AnswerFragment";
    private int questionsToFetch = 2;

    private int questionIndex = 0;
    private List questionList;

    // [START define_database_reference]
    private DatabaseReference mDatabase;
    // [END define_database_reference]

    private FirebaseRecyclerAdapter<Question, QuestionViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    private TextView questionText;
    private Button answerButton;
    private EditText answerText;

    private boolean checkCurrentAnswer() {
        final String userAnswer = answerText.getText().toString().toLowerCase();
        final String questionAnswer = questionList.get(questionIndex).toString().toLowerCase();
        return userAnswer.equals(questionAnswer);
    }

    public AnswerFragment() {}

    private void questionRequest() {

    }

    // TODO: Add test switch animation using https://github.com/daimajia/AndroidViewAnimations.
    private void questionTransition() {



        if (questionIndex > questionList.size()) {
            // Fetch additional questions.
            questionRequest();
        }
    }

    private void fetchQuestions() {

        questionsToFetch *= 2;
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_answer, container, false);

        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END create_database_reference]

        mRecycler = (RecyclerView) rootView.findViewById(R.id.messages_list);
        mRecycler.setHasFixedSize(true);

        answerText = (EditText) rootView.findViewById(R.id.answer_text);
        answerButton = (Button) rootView.findViewById(R.id.answer_button);
        questionText = (TextView) rootView.findViewById(R.id.question_text);

        fetchQuestions();

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        Query questionsQuery = getQuery(mDatabase);

        mRecycler.setAdapter(mAdapter);
    }

    // Called when user submits on answer (could be correct or incorrect - needs to be verified here.
    // TODO: Change implementation.
    private void onAnswerClicked(DatabaseReference questionRef) {
        questionRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                final boolean isCorrect = checkCurrentAnswer();


                User u = mutableData.getValue(User.class);
                if (u == null) {
                    return Transaction.success(mutableData);
                }

                // TODO: Update question rating.
                if (isCorrect) {

                } else {

                }

//                if (p.stars.containsKey(getUid())) {
//                    // Unstar the question and remove self from stars
//                    p.rating = p.rating - 1;
//                    p.stars.remove(getUid());
//                } else {
//                    // Star the question and add self to stars
//                    p.rating = p.rating + 1;
//                    p.stars.put(getUid(), true);
//                }

                // Set value and report transaction success
                mutableData.setValue(u);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "questionTransaction:onComplete:" + databaseError);
                answerText.setText("");
            }
        });
    }
    // [END question_stars_transaction]

    private void onRateQuestionClicked(DatabaseReference questionRef) {
//        questionRef.runTransaction(new Transaction.Handler() {
//
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    private Query getQuery(DatabaseReference databaseReference) {
        // TODO: Query to return next random question with low or high votes.
        // Want to dispose and not show lowly rated questions.
        return databaseReference.child("questions")
                .child(getUid());
    }

}
