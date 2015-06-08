package com.app.mftinsatller;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class SplashScreen extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashlayout);

        Shimmer.ShimmerTextView txtTitle = (Shimmer.ShimmerTextView)findViewById(R.id.Title);
        txtTitle.setText("MTL FREE TV");

        Shimmer.Shimmer shimmer = new Shimmer.Shimmer();
        shimmer.start(txtTitle);

        new CountDownTimer(2500,1000){

            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                Intent i = new Intent(SplashScreen.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        }.start();

    }


}
