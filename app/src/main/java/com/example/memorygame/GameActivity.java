package com.example.memorygame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.memorygame.database.Score;
import com.example.memorygame.database.ScoreDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private int score;
    private TextView tvMessage, scoreMessage;
    private Button btnStart;
    private Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;

    private List<Integer> sequence = new ArrayList<>();
    private int currentStep = 0;
    private boolean playerTurn = false;
    private Random random = new Random();
    private ScoreDatabase database;
    private Score existingScore;

    private AlertDialog alertDialog;

    private Typeface retroFont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        database = ScoreDatabase.getDatabase(getApplicationContext());
        ScoreDatabase.databaseWriteExecutor.execute(()->{
            existingScore = database.scoreDao().getScore();
        });
        retroFont = Typeface.createFromAsset(getAssets(), "fonts/poxel-font.ttf");
        score = 0;
        tvMessage = findViewById(R.id.tv_message);
        tvMessage.setTypeface(retroFont);
        scoreMessage = findViewById(R.id.score_message);
        scoreMessage.setTypeface(retroFont);
        btnStart = findViewById(R.id.btn_start);
        btn1 = findViewById(R.id.btn_1);
        btn2 = findViewById(R.id.btn_2);
        btn3 = findViewById(R.id.btn_3);
        btn4 = findViewById(R.id.btn_4);
        btn5 = findViewById(R.id.btn_5);
        btn6 = findViewById(R.id.btn_6);
        btn7 = findViewById(R.id.btn_7);
        btn8 = findViewById(R.id.btn_8);
        btn9 = findViewById(R.id.btn_9);

        btnStart.setVisibility(View.GONE);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });

        View.OnClickListener colorButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerTurn) {
                    int color = -1;
                    if (v == btn1) color = 0;
                    if (v == btn2) color = 1;
                    if (v == btn3) color = 2;
                    if (v == btn4) color = 3;
                    if (v == btn5) color = 4;
                    if (v == btn6) color = 5;
                    if (v == btn7) color = 6;
                    if (v == btn8) color = 7;
                    if (v == btn9) color = 8;
                    checkPlayerMove(color);
                }
            }
        };

        btn1.setOnClickListener(colorButtonListener);
        btn2.setOnClickListener(colorButtonListener);
        btn3.setOnClickListener(colorButtonListener);
        btn4.setOnClickListener(colorButtonListener);
        btn5.setOnClickListener(colorButtonListener);
        btn6.setOnClickListener(colorButtonListener);
        btn7.setOnClickListener(colorButtonListener);
        btn8.setOnClickListener(colorButtonListener);
        btn9.setOnClickListener(colorButtonListener);
        showAlertDialogWithCountdown();

    }

    private void showAlertDialogWithCountdown()
    {
        Log.d("showAlertDialogWithCountdown", "se iniciara el conteo");
        LayoutInflater inflater = getLayoutInflater();

        View dialogLayout = inflater.inflate(R.layout.custom_alert_dialog_normal_message, null);
        TextView dialogTitle = dialogLayout.findViewById(R.id.dialog_title);
        TextView dialogMessage = dialogLayout.findViewById(R.id.dialog_message);

        dialogTitle.setTypeface(retroFont);
        dialogMessage.setTypeface(retroFont);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        dialogTitle.setText("Get Ready!");
//        final TextView textView = new TextView(this);
//        SpannableString spannableString = new SpannableString("Get Ready!");
//        spannableString.setSpan(new CustomTypefaceSpan(retroFont),0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE );
//
//        textView.setTextSize(24);
//        textView.setTypeface(retroFont);
//        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//
//        final AlertDialog alertDialog = new AlertDialog.Builder(this)
//                                            .setTitle(spannableString)
//                                            .setView(textView)
//                                            .setCancelable(false)
//                                            .create();
        builder.setView(dialogLayout);
        builder.setCancelable(false);
        AlertDialog alertDialog1 = builder.create();

        new CountDownTimer(4000, 1000){
            int counter = 3;

            public void onTick(long milisUntilFinished){

                if(counter > 0 )
                {
                    //textView.setText(String.valueOf(counter));
                    dialogMessage.setText(String.valueOf(counter));
                }else {
                    //textView.setText("GO!");
                    dialogMessage.setText("GO!");
                }
                counter--;
            }
            public void onFinish(){
                //alertDialog.dismiss();
                alertDialog1.dismiss();
                startGame();
            }
        }.start();
        //alertDialog.show();
        alertDialog1.show();
    }

    // Clase personalizada para aplicar la fuente

    private void startGame() {
        Log.d("startGame", "Inicio del cambio de colores");
        sequence.clear();
        currentStep = 0;
        playerTurn = false;
        tvMessage.setText("Watch the sequence");
        addNextColor();
    }

    private void addNextColor() {
        int number = random.nextInt(9);
        Log.d("addNextColor","new color" + number);
        sequence.add(number);
        showSequence();
    }

    private void showSequence() {

        playerTurn = false;
        currentStep = 0;
        tvMessage.setText("Watch the sequence");

        Handler handler = new Handler();
        long delay = 1000;

        for (int i = 0; i < sequence.size(); i++)
        {

            final int color = sequence.get(i);
            Log.d("showSequence", "color: " + color);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    highlightColor(color);
                }
            }, delay * (i + 1));
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tvMessage.setText("Your turn");
                playerTurn = true;
            }
        }, delay * (sequence.size() + 1));
    }

    private void highlightColor(int color) {
        switch (color) {
            case 0:
                btn1.setPressed(true);
                break;
            case 1:
                btn2.setPressed(true);
                break;
            case 2:
                btn3.setPressed(true);
                break;
            case 3:
                btn4.setPressed(true);
                break;
            case 4:
                btn5.setPressed(true);
                break;
            case 5:
                btn6.setPressed(true);
                break;
            case 6:
                btn7.setPressed(true);
                break;
            case 7:
                btn8.setPressed(true);
                break;
            case 8:
                btn9.setPressed(true);
                break;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                btn1.setPressed(false);
                btn2.setPressed(false);
                btn3.setPressed(false);
                btn4.setPressed(false);
                btn5.setPressed(false);
                btn6.setPressed(false);
                btn7.setPressed(false);
                btn8.setPressed(false);
                btn9.setPressed(false);
            }
        }, 700);
    }

    private void checkPlayerMove(int color) {
        if (color == sequence.get(currentStep)) {
            currentStep++;
            if (currentStep == sequence.size()) {
                score += sequence.size();
                scoreMessage.setText("Score: " + score);
                playerTurn = false;
                tvMessage.setText("Well done! Watch the next sequence");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        addNextColor();
                    }
                }, 1000);
            }
        } else {
            ScoreDatabase.databaseWriteExecutor.execute(()->{
                if(existingScore != null)
                {
                    Log.d("save score db","no es null se hara update");
                    if(existingScore.getPointsScore() < score)
                    {
                        existingScore.setPointsScore(score);
                        database.scoreDao().updateScore(existingScore);
                    }
                }
                else {
                    Log.d("save score db","es nuevo");
                    Score newScore = new Score(score);
                    database.scoreDao().insertScore(newScore);
                }
            });
            if(alertDialog != null && alertDialog.isShowing())
            {
                alertDialog.dismiss();
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            LayoutInflater inflater = getLayoutInflater();
            View dialogLayout = inflater.inflate(R.layout.custom_alert_dialog, null);

            TextView dialogTitle = dialogLayout.findViewById(R.id.dialog_title);
            TextView dialogMessage = dialogLayout.findViewById(R.id.dialog_message);

            dialogTitle.setTypeface(retroFont);
            dialogMessage.setTypeface(retroFont);
            dialogTitle.setText("Game Over");
            dialogMessage.setText("Score: " + score);
            builder.setView(dialogLayout);
            builder.setCancelable(false);
            AlertDialog alertDialog1 = builder.create();
            alertDialog1.show();

//            TextView textView = new TextView(this);
//            textView.setTextSize(24);
//            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//            textView.setTypeface(retroFont);
//            SpannableString spannableString = new SpannableString("Game Over");
//            spannableString.setSpan(new CustomTypefaceSpan(retroFont),0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE );
//            AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.RetroAlertDialogGameOver)
//                    .setTitle(spannableString)
//                    .setView(textView)
//                    .setCancelable(false)
//                    .create();
//            textView.setText("Score: " + score );
//            alertDialog.show();


            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    alertDialog1.dismiss();
                    finish();
                }
            }, 3000);

            playerTurn = false;
        }
    }
}