package com.toni.juegomemoria;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickSalir(View view) {
        finish();
    }

    public void onClickJugar(View view) {
        Intent intent = new Intent(this, JuegoActivity.class);
        startActivity(intent);
    }
}