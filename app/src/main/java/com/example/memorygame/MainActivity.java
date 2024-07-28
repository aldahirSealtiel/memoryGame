package com.example.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.memorygame.database.Score;
import com.example.memorygame.database.ScoreDatabase;

public class MainActivity extends AppCompatActivity {

    private Button startButton;
    private TextView textViewScore;
    private ScoreDatabase database;
    private Score scoreDatabase;
    private Typeface retroFont;

    private TextView textTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = ScoreDatabase.getDatabase(getApplicationContext());
        ScoreDatabase.databaseWriteExecutor.execute( ()->{
            database.scoreDao().insertScore(new Score(0));
        });
        showScore();
        getElementFromLayout();
        setListeners();

    }
    @Override
    protected void onResume() {

        super.onResume();

        showScore();
    }
    private void showScore()
    {


        ScoreDatabase.databaseWriteExecutor.execute(()->{
            scoreDatabase = database.scoreDao().getScore();
            textViewScore.setText(""+scoreDatabase.getPointsScore());
            textViewScore.setTypeface(retroFont);
        });

    }

    private void setListeners()
    {
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });
    }
    private void getElementFromLayout()
    {
        startButton = findViewById(R.id.startButton);
        retroFont = Typeface.createFromAsset(getAssets(), "fonts/poxel-font.ttf");
        textViewScore = findViewById(R.id.Score);
        textTitle = findViewById(R.id.titleScore);
        textTitle.setTypeface(retroFont);
    }

}