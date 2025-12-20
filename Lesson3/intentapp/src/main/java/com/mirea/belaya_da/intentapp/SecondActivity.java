package com.mirea.belaya_da.intentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    private TextView tvResult;
    private final int MY_NUMBER = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        tvResult = findViewById(R.id.tv_result);
        Intent receivedIntent = getIntent();
        String receivedTime = receivedIntent.getStringExtra(MainActivity.EXTRA_TIME_KEY);
        if (receivedTime == null) {
            receivedTime = "время не передано";
        }
        int squaredNumber = MY_NUMBER * MY_NUMBER; //3*3
        String finalText = "КВАДРАТ ЗНАЧЕНИЯ НОМЕРА ПО СПИСКУ В ГРУППЕ СОСТАВЛЯЕТ ЧИСЛО "
                + squaredNumber + ", а текущее время " + receivedTime + ".";
        tvResult.setText(finalText);
    }
}