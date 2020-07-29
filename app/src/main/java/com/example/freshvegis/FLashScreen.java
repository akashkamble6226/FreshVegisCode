package com.example.freshvegis;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class FLashScreen extends AppCompatActivity
{

    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_screen);


        final Runnable r = new Runnable() {
            @Override
            public void run()
            {
                Intent intent = new Intent(FLashScreen.this,FirstScreen.class);
                startActivity(intent);
                finish();

            }
        };

        handler.postDelayed(r,5000);

    }
}
