package com.quotify.www.quotify;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quotify.www.quotify.models.Question;
import com.quotify.www.quotify.models.User;

import java.util.HashMap;
import java.util.Map;

public class NewQuestionActivity extends com.quotify.www.quotify.BaseActivity {

    private static final String TAG = "NewQuestionActivity";
    private static final String REQUIRED = "Required";

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    private EditText mAnswerField;
    private EditText mBodyField;
    private FloatingActionButton mSubmitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_question);

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        mAnswerField = (EditText) findViewById(R.id.field_title);
        mBodyField = (EditText) findViewById(R.id.field_body);
        mSubmitButton = (FloatingActionButton) findViewById(R.id.fab_submit_question);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
            }
        });
    }

    private void submitPost() {
        final String answer = mAnswerField.getText().toString();
        final String body = mBodyField.getText().toString();

        // Title is required
        if (TextUtils.isEmpty(answer)) {
            mAnswerField.setError(REQUIRED);
            return;
        }

        if (answer.split(" ").length != 2) {
            mAnswerField.setError("Answer must have two words.");
            return;
        }

        // Body is required
        if (TextUtils.isEmpty(body)) {
            mBodyField.setError(REQUIRED);
            return;
        }

        // Disable button so there are no multi-questions
        setEditingEnabled(false);
        Toast.makeText(this, "Saving...", Toast.LENGTH_SHORT).show();

        // [START single_value_read]
        final String userId = getUid();
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

                        // [START_EXCLUDE]
                        if (user == null) {
                            // DummyItem is null, error out
                            Log.e(TAG, "DummyItem " + userId + " is unexpectedly null");
                            Toast.makeText(NewQuestionActivity.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Write new question
                            addNewQuestion(userId, user.username, body, answer);
                        }

                        // Finish this Activity, back to the stream
                        setEditingEnabled(true);
                        finish();
                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        // [START_EXCLUDE]
                        setEditingEnabled(true);
                        // [END_EXCLUDE]
                    }
                });
        // [END single_value_read]
    }

    private void setEditingEnabled(boolean enabled) {
        mAnswerField.setEnabled(enabled);
        mBodyField.setEnabled(enabled);
        if (enabled) {
            mSubmitButton.setVisibility(View.VISIBLE);
        } else {
            mSubmitButton.setVisibility(View.GONE);
        }
    }

    // [START write_fan_out]
    private void addNewQuestion(String userId, String username, String body, String answer) {
        // Create new question at /user-questions/$userid/$questionid and at
        // /questions/$questionid simultaneously
        final long timestamp = System.currentTimeMillis();
        String key = mDatabase.child("questions").push().getKey();
        Question question = new Question(userId, username, body, answer, timestamp);
        Map<String, Object> questionValues = question.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/questions/" + key, questionValues);
        childUpdates.put("/user-questions/" + userId + "/" + key, questionValues);

        mDatabase.updateChildren(childUpdates);
    }
    // [END write_fan_out]
}
