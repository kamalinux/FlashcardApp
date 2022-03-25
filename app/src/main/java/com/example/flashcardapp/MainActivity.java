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

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    boolean isShowingAnswers = false;

    FlashcardDatabase flashcardDatabase;
    List<Flashcard> allFlashcards;
    int cardIndex = 0;
    Flashcard cardToEdit;
    int ADD_CARD_REQUEST_CODE = 100;
    int EDIT_CARD_REQUEST_CODE = 200;
    TextView flashcardQuestion;
    TextView flashcardAnswer;
    TextView wrongAnswer1;
    TextView wrongAnswer2;
    TextView correctAnswer;

    private int getRandomNumber(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    private void updateCurrCardDisplay(int currCardIndex) {
        Flashcard currCard = allFlashcards.get(currCardIndex);
        flashcardQuestion.setText(currCard.getQuestion());
        flashcardAnswer.setText(currCard.getAnswer());
        wrongAnswer1.setText(currCard.getWrongAnswer1());
        wrongAnswer2.setText(currCard.getWrongAnswer2());
        correctAnswer.setText(currCard.getAnswer());
    }

    private void showQuestion() {
        flashcardQuestion.setVisibility(View.VISIBLE);
        flashcardAnswer.setVisibility(View.INVISIBLE);
    }

    private void resetMultipleChoice() {
        wrongAnswer1.setBackgroundColor(getResources().getColor(R.color.my_red_color, null));
        wrongAnswer1.setTextColor(getResources().getColor(R.color.black));

        wrongAnswer2.setBackgroundColor(getResources().getColor(R.color.my_red_color, null));
        wrongAnswer2.setTextColor(getResources().getColor(R.color.black));

        correctAnswer.setBackgroundColor(getResources().getColor(R.color.my_green_color, null));
        correctAnswer.setTextColor(getResources().getColor(R.color.black));
    }

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
        ImageView nextButton = findViewById(R.id.next_card);
        ImageView prevButton = findViewById(R.id.prev_card);
        ImageView deleteButton = findViewById(R.id.delete_card);

        flashcardDatabase = new FlashcardDatabase(getApplicationContext());
        allFlashcards = flashcardDatabase.getAllCards();

        if (allFlashcards != null && allFlashcards.size() > 0) {
            System.out.println(allFlashcards.size());
            flashcardQuestion.setText(allFlashcards.get(0).getQuestion());
            flashcardAnswer.setText(allFlashcards.get(0).getAnswer());
            wrongAnswer1.setText(allFlashcards.get(0).getWrongAnswer1());
            wrongAnswer2.setText(allFlashcards.get(0).getWrongAnswer2());
            correctAnswer.setText(allFlashcards.get(0).getAnswer());
        } else {
            flashcardDatabase.initFirstCard();
        }

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
            for (int i = 0; i < allFlashcards.size(); i++) {
                if (allFlashcards.get(i).getQuestion().equals(flashcardQuestion.getText().toString())) {
                    cardToEdit = allFlashcards.get(i);
                }
            }

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

                startActivityForResult(intent, EDIT_CARD_REQUEST_CODE);
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

        nextButton.setOnClickListener(view -> {
            int randomIndex = getRandomNumber(0, allFlashcards.size() - 1);

            while (allFlashcards.size() != 1 && cardIndex == randomIndex) {
                randomIndex = getRandomNumber(0, allFlashcards.size() - 1);
            }

            cardIndex = randomIndex;
            System.out.println("cardIndex: " + cardIndex);

            if (cardIndex >= allFlashcards.size()) {
                Snackbar.make(view, "You've reached the end of the cards, going back to start.", Snackbar.LENGTH_SHORT).show();
                cardIndex = 0;
            }

            try {
                updateCurrCardDisplay(cardIndex);
                showQuestion();
                resetMultipleChoice();
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Accessed an index out of bounds of allFlashcards");
            }
        });

        prevButton.setOnClickListener(view -> {
            int randomIndex = getRandomNumber(0, allFlashcards.size() - 1);

            while (allFlashcards.size() != 1 && cardIndex == randomIndex) {
                randomIndex = getRandomNumber(0, allFlashcards.size() - 1);
            }

            cardIndex = randomIndex;
            System.out.println("cardIndex: " + cardIndex);

            if (cardIndex >= allFlashcards.size()) {
                Snackbar.make(view, "You've reached the end of the cards, going back to start.", Snackbar.LENGTH_SHORT).show();
                cardIndex = 0;
            }

            try {
                updateCurrCardDisplay(cardIndex);
                showQuestion();
                resetMultipleChoice();
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Accessed an index out of bounds of allFlashcards");
            }
        });

        deleteButton.setOnClickListener(view -> {
            flashcardDatabase.deleteCard(flashcardQuestion.getText().toString());
            allFlashcards = flashcardDatabase.getAllCards();
            cardIndex--;

            if (allFlashcards.size() > 0) {
                try {
                    updateCurrCardDisplay(cardIndex);
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Accessed an index out of bounds of allFlashcards");
                }
            } else {
                cardIndex = 0;
                String emptyQuestion = "Add a new flashcard!";
                String emptyAnswer = "Press the '+' button";

                flashcardQuestion.setText(emptyQuestion);
                flashcardAnswer.setText(emptyAnswer);
                wrongAnswer1.setText("");
                wrongAnswer2.setText("");
                correctAnswer.setText("");
            }

            showQuestion();
            System.out.println("cardIndex: " + cardIndex);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String questionString = data.getExtras().getString(AddCardActivity.QUESTION_KEY);
        String answerString = data.getExtras().getString(AddCardActivity.ANSWER_KEY);
        String wrongAnswer1String = data.getExtras().getString(AddCardActivity.WRONG_ANSWER1_KEY);
        String wrongAnswer2String = data.getExtras().getString(AddCardActivity.WRONG_ANSWER2_KEY);

        if (data != null) {

            flashcardQuestion.setText(questionString);
            flashcardAnswer.setText(answerString);
            wrongAnswer1.setText(wrongAnswer1String);
            wrongAnswer2.setText(wrongAnswer2String);
            correctAnswer.setText(answerString);
        }

        if (requestCode == ADD_CARD_REQUEST_CODE) {

            flashcardDatabase.insertCard(new Flashcard(questionString, answerString, wrongAnswer1String, wrongAnswer2String));
            allFlashcards = flashcardDatabase.getAllCards();

            Snackbar.make(findViewById(R.id.flashcard_question),
                        "Created card successfully.",
                        Snackbar.LENGTH_SHORT)
                        .show();
        }  else if (requestCode == EDIT_CARD_REQUEST_CODE) {
            cardToEdit.setQuestion(questionString);
            cardToEdit.setAnswer(answerString);
            cardToEdit.setWrongAnswer1(wrongAnswer1String);
            cardToEdit.setWrongAnswer2(wrongAnswer2String);

            flashcardDatabase.updateCard(cardToEdit);

            Snackbar.make(findViewById(R.id.flashcard_question),
                    "Card successfully edited",
                    Snackbar.LENGTH_SHORT)
                    .show();
        }
    }
}