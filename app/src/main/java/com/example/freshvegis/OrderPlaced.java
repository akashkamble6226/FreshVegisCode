package com.example.freshvegis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

public class OrderPlaced extends AppCompatActivity
{
    MediaPlayer notifySong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placed);

        notifySong = MediaPlayer.create(OrderPlaced.this,R.raw.sound2);
        playIt();
    }

    private void playIt()
    {

        notifySong.start();
    }


    public void GoingBack(View V)
    {
        Intent intent = new Intent (OrderPlaced.this,HomeActivity.class);

        startActivity(intent);
    }


}
