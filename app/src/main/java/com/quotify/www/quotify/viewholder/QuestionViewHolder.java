package com.quotify.www.quotify.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.quotify.www.quotify.models.Question;

public class QuestionViewHolder extends RecyclerView.ViewHolder {

    public TextView authorView;
    public TextView ratingView;
    public TextView bodyView;
    public TextView answerView;

    public QuestionViewHolder(View itemView) {
        super(itemView);

        
//        authorView = (TextView) itemView.findViewById(R.id.question_author);
//        ratingView = (TextView) itemView.findViewById(R.id.question_num_stars);
//        bodyView = (TextView) itemView.findViewById(R.id.question_body);
    }

    public void bindToPostPos(Question question, View.OnClickListener starClickListener) {
        authorView.setText(question.author);
        ratingView.setText(String.valueOf(question.rating));
        bodyView.setText(question.body);
    }
}
