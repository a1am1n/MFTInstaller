package com.app.mftinsatller;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.SaveCallback;

/**
 * Created by Krishna on 10-06-2015.
 */
public class MyApplication extends Application {


    @Override
    public void onCreate() {
        // app id......client key
     //   Parse.initialize(MyApplication.this, "bT7pJXziyKjemShn5hXNqVs1d4QFGrYMvto7jBBL", "JT433l6lEsbBA5dHsOcUAunxb4xHdgv33ZlmBfgT");

        Parse.initialize(MyApplication.this, "rSAMWry0vTv35qB8Y9uAh9n8yYIBs2YfULIY2Xdn", "IPt8c5XhP4FbY3GcNiDz5Bef9leyP96WpdLp15C6");

        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.e("###### com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("###### com.parse.push", "failed to subscribe for push", e);
                }
            }
        });
    }
}
