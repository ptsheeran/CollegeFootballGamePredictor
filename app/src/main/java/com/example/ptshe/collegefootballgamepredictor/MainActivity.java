package com.example.ptshe.collegefootballgamepredictor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonGoToPredictor = findViewById(R.id.buttonGoToPredictor);
        Button buttonGoToRankings = findViewById(R.id.buttonGoToRankings);

        buttonGoToPredictor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Predictor.class));
            }
        });
        buttonGoToRankings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Rankings.class));
            }
        });

    }
}
