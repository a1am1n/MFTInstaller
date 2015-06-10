package com.app.mftinsatller;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Krishna on 10-06-2015.
 */
public class MyApplication extends Application {


    @Override
    public void onCreate() {
        // app id......client key
        Parse.initialize(MyApplication.this, "bT7pJXziyKjemShn5hXNqVs1d4QFGrYMvto7jBBL", "JT433l6lEsbBA5dHsOcUAunxb4xHdgv33ZlmBfgT");
    }
}
