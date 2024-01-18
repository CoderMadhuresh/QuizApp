package com.interndesire.quizapp;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tlQuests, quests;
    Button ans1, ans2, ans3, ans4, submit;

    int score = 0;
    int totalQuests;
    int currentQuesIndex = 0;
    String selectedAns = "";

    List<Integer> remainingIndices; // to store remaining indices of questions

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tlQuests = findViewById(R.id.tlQuesns);
        quests = findViewById(R.id.questions);
        ans1 = findViewById(R.id.ans1);
        ans2 = findViewById(R.id.ans2);
        ans3 = findViewById(R.id.ans3);
        ans4 = findViewById(R.id.ans4);
        submit = findViewById(R.id.submit);

        ans1.setOnClickListener(this);
        ans2.setOnClickListener(this);
        ans3.setOnClickListener(this);
        ans4.setOnClickListener(this);
        submit.setOnClickListener(this);

        totalQuests = QuesAns.questions.length;
        tlQuests.setText(String.format("Total Questions : %s", totalQuests));

        remainingIndices = new ArrayList<>();
        for (int i = 0; i < totalQuests; i++) {
            remainingIndices.add(i);
        }
        Collections.shuffle(remainingIndices);

        loadNewQuests();
    }

    @Override
    public void onClick(View v) {
        ans1.setBackgroundColor(Color.rgb(103, 79, 163));
        ans2.setBackgroundColor(Color.rgb(103, 79, 163));
        ans3.setBackgroundColor(Color.rgb(103, 79, 163));
        ans4.setBackgroundColor(Color.rgb(103, 79, 163));

        Button clickedButton = (Button) v;
        if (clickedButton.getId() == R.id.submit) {
            if (selectedAns.equals(QuesAns.answer[remainingIndices.get(currentQuesIndex)])) {
                score++;
            }
            currentQuesIndex++;
            loadNewQuests();
        } else {
            selectedAns = clickedButton.getText().toString();
            clickedButton.setBackgroundColor(Color.BLUE);
        }
    }

    void loadNewQuests() {
        if (currentQuesIndex < totalQuests) {
            int currentIndex = remainingIndices.get(currentQuesIndex);

            quests.setText(QuesAns.questions[currentIndex]);
            ans1.setText(QuesAns.choices[currentIndex][0]);
            ans2.setText(QuesAns.choices[currentIndex][1]);
            ans3.setText(QuesAns.choices[currentIndex][2]);
            ans4.setText(QuesAns.choices[currentIndex][3]);
        } else {
            finishQuiz();
        }
    }

    void finishQuiz() {
        String passStatus = (score > totalQuests * 0.60) ? "PASSED" : "FAIL";

        new AlertDialog.Builder(this)
                .setTitle(passStatus)
                .setMessage("Score is " + score + " out of " + totalQuests)
                .setPositiveButton("Restart", (dialog, which) -> restartQuiz())
                .setCancelable(false).show();
    }

    void restartQuiz() {
        score = 0;
        currentQuesIndex = 0;
        Collections.shuffle(remainingIndices);
        loadNewQuests();
    }
}
