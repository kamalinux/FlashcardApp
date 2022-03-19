package com.example.flashcardapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    boolean isShowingAnswers = false;

    TextView flashcardQuestion;
    TextView flashcardAnswer;
    TextView wrongAnswer1;
    TextView wrongAnswer2;
    TextView correctAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flashcardQuestion = findViewById(R.id.flashcard_question);
        flashcardAnswer = findViewById(R.id.flashcard_answer);

        wrongAnswer1 = findViewById(R.id.answer1);
        wrongAnswer2 = findViewById(R.id.answer2);
        correctAnswer = findViewById(R.id.answer3);
        ImageView toggleChoices = findViewById(R.id.toggle_choices_visibility);

        ImageView addButton = findViewById(R.id.add_card);
        ImageView editButton = findViewById(R.id.edit_card);

        flashcardQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flashcardQuestion.setVisibility(View.INVISIBLE);
                flashcardAnswer.setVisibility(View.VISIBLE);
            }
        });

        flashcardAnswer.setOnClickListener(view -> {
            flashcardQuestion.setVisibility(View.VISIBLE);
            flashcardAnswer.setVisibility(View.INVISIBLE);
        });

        wrongAnswer1.setOnClickListener(view -> {
            wrongAnswer1.setBackgroundColor(getResources().getColor(R.color.my_red_color, null));
            wrongAnswer1.setTextColor(getResources().getColor(R.color.black));

            correctAnswer.setBackgroundColor(getResources().getColor(R.color.my_green_color, null));
            correctAnswer.setTextColor(getResources().getColor(R.color.black));
        });

        wrongAnswer2.setOnClickListener(view -> {
            wrongAnswer2.setBackgroundColor(getResources().getColor(R.color.my_red_color, null));
            wrongAnswer2.setTextColor(getResources().getColor(R.color.black));

            correctAnswer.setBackgroundColor(getResources().getColor(R.color.my_green_color, null));
            correctAnswer.setTextColor(getResources().getColor(R.color.black));
        });

        correctAnswer.setOnClickListener(view -> {
            correctAnswer.setBackgroundColor(getResources().getColor(R.color.my_green_color, null));
            correctAnswer.setTextColor(getResources().getColor(R.color.black));
        });

        toggleChoices.setOnClickListener(view -> {
            if (isShowingAnswers) {
                toggleChoices.setImageResource(R.drawable.ic_iconmonstr_eye_8);
                wrongAnswer1.setVisibility(View.INVISIBLE);
                wrongAnswer2.setVisibility(View.INVISIBLE);
                correctAnswer.setVisibility(View.INVISIBLE);
            } else {
                toggleChoices.setImageResource(R.drawable.ic_iconmonstr_eye_5);
                wrongAnswer1.setVisibility(View.VISIBLE);
                wrongAnswer2.setVisibility(View.VISIBLE);
                correctAnswer.setVisibility(View.VISIBLE);
            }
            isShowingAnswers = !isShowingAnswers;
        });

        addButton.setOnClickListener(view -> {
            Intent toAddCard = new Intent(MainActivity.this, AddCardActivity.class);
            if (toAddCard != null) {
                startActivityForResult(toAddCard, 100);
            }
        });

        editButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
            if (intent != null) {
                String currQuestion = flashcardQuestion.getText().toString();
                String currAnswer = flashcardAnswer.getText().toString();

                String wrongChoice1 = wrongAnswer1.getText().toString();
                String wrongChoice2 = wrongAnswer2.getText().toString();
                String correctChoice = correctAnswer.getText().toString();

                intent.putExtra(AddCardActivity.QUESTION_KEY, currQuestion);
                intent.putExtra(AddCardActivity.ANSWER_KEY, currAnswer);
                intent.putExtra(AddCardActivity.WRONG_ANSWER1_KEY, wrongChoice1);
                intent.putExtra(AddCardActivity.WRONG_ANSWER2_KEY, wrongChoice2);

                startActivityForResult(intent, 100);
            }

        });

        findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.answer1).setVisibility(View.VISIBLE);
                findViewById(R.id.answer1).setBackgroundColor(getResources().getColor(R.color.my_reset_color, null));
                findViewById(R.id.answer2).setVisibility(View.VISIBLE);
                findViewById(R.id.answer2).setBackgroundColor(getResources().getColor(R.color.my_reset_color, null));
                findViewById(R.id.answer3).setVisibility(View.VISIBLE);
                findViewById(R.id.answer3).setBackgroundColor(getResources().getColor(R.color.my_reset_color, null));
                findViewById(R.id.flashcard_question).setVisibility(View.VISIBLE);
                findViewById(R.id.flashcard_answer).setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            if (data != null) {
                String questionString = data.getExtras().getString(AddCardActivity.QUESTION_KEY);
                String answerString = data.getExtras().getString(AddCardActivity.ANSWER_KEY);
                String wrongAnswer1String = data.getExtras().getString(AddCardActivity.WRONG_ANSWER1_KEY);
                String wrongAnswer2String = data.getExtras().getString(AddCardActivity.WRONG_ANSWER2_KEY);

                flashcardQuestion.setText(questionString);
                flashcardAnswer.setText(answerString);
                wrongAnswer1.setText(wrongAnswer1String);
                wrongAnswer2.setText(wrongAnswer2String);
                correctAnswer.setText(answerString);

                Snackbar.make(findViewById(R.id.flashcard_question),
                        "Created card successfully.",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
    }
}