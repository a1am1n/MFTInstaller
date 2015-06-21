package com.app.mftinsatller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import helpers.ComplexPreferences;
import helpers.PrefUtils;
import helpers.User;


public class LoginScreen extends ActionBarActivity implements View.OnClickListener {
    ProgressDialog progressDialog;
    TextView txtBTNForgotPassword, txtBTNLogin,txtBTNRegister;
    EditText etPassword, etEmailID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        setupAnimation();


        initViews();



    }

    @Override
    protected void onResume() {
        super.onResume();

        if (checkInternet()) {
            boolean isLogin = PrefUtils.getLogin(LoginScreen.this);

            if (isLogin) {
                Intent i = new Intent(LoginScreen.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        } else {
            Toast.makeText(LoginScreen.this, "Please connect your Internet", Toast.LENGTH_LONG).show();
        }
    }

    private void initViews() {
        txtBTNRegister= (TextView) findViewById(R.id.txtBTNRegister);
        txtBTNForgotPassword = (TextView) findViewById(R.id.txtBTNForgotPassword);
        txtBTNLogin = (TextView) findViewById(R.id.txtBTNLogin);

        txtBTNForgotPassword.setOnClickListener(this);
        txtBTNRegister.setOnClickListener(this);
        txtBTNLogin.setOnClickListener(this);

        etPassword = (EditText) findViewById(R.id.etPassword);
        etEmailID = (EditText) findViewById(R.id.etEmailID);
    }


    private void setupAnimation() {
        ImageView mScanner = (ImageView) findViewById(R.id.icon);
        Animation mAnimation;


        mAnimation = new TranslateAnimation(
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0.5f);
        mAnimation.setDuration(1000);
        mAnimation.setRepeatCount(-1);
        mAnimation.setRepeatMode(Animation.REVERSE);
        mAnimation.setInterpolator(new LinearInterpolator());
        mScanner.setAnimation(mAnimation);

    }


    private void processLogin(String email, String password) {

        progressDialog = new ProgressDialog(LoginScreen.this);
        progressDialog.setMessage("Checking login details...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("User_details");
        query.whereEqualTo("user_email", email);
        query.whereEqualTo("user_password", password);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                progressDialog.dismiss();

                if (e == null) {
                    if (parseObjects.size() != 0) {

                        String email = parseObjects.get(0).getString("user_email");
                        String mobile = parseObjects.get(0).getString("user_contact");
                        String name = parseObjects.get(0).getString("user_name");
                        String device = parseObjects.get(0).getString("user_deivce");

                        User userData = new User();
                        userData.Email = email;
                        userData.Mobile = mobile;
                        userData.Name = name;
                        userData.Device = device;



                        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(LoginScreen.this, "user_pref", 0);
                        complexPreferences.putObject("current-user", userData);
                        complexPreferences.commit();

                        PrefUtils.setUserData(userData, LoginScreen.this);

                        PrefUtils.setLogin(LoginScreen.this, true);

                        Toast.makeText(LoginScreen.this, "Login Sucesfully...", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(LoginScreen.this, MainActivity.class);
                        startActivity(i);
                        finish();

                    } else {

                        Toast.makeText(LoginScreen.this, "Login Failed. Please check email or password !!!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(LoginScreen.this, "Error to fetch details !!!", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtBTNLogin:
                if (checkInternet()) {
                    if (isEmptyField(etEmailID)) {
                        Toast.makeText(LoginScreen.this, "Please enter Email ID !!!", Toast.LENGTH_SHORT).show();
                    } else if (isEmptyField(etPassword)) {
                        Toast.makeText(LoginScreen.this, "Please enter Password !!!", Toast.LENGTH_SHORT).show();
                    } else if (!isEmailPatternMatch(etEmailID)) {
                        Toast.makeText(LoginScreen.this, "Please enter valid Email ID !!!", Toast.LENGTH_SHORT).show();
                    } else {
                        processLogin(etEmailID.getText().toString().trim().toUpperCase(), etPassword.getText().toString().trim());

                    }

                } else {
                    Toast.makeText(LoginScreen.this, "Please connect your Internet", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.txtBTNForgotPassword:

                Intent i = new Intent(LoginScreen.this,ForgotPasswordScreen.class);
                startActivity(i);
                break;

            case R.id.txtBTNRegister:

                Intent i2 = new Intent(LoginScreen.this,RegistrationVerification.class);
                startActivity(i2);
                break;


        }
    }

    private boolean checkInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(LoginScreen.this.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        } else {
            return false;
        }

    }

    public boolean isEmptyField(EditText param1) {

        boolean isEmpty = false;
        if (param1.getText() == null || param1.getText().toString().equalsIgnoreCase("")) {
            isEmpty = true;
        }
        return isEmpty;
    }

    public boolean isPasswordMatch(EditText param1, EditText param2) {
        boolean isMatch = false;
        if (param1.getText().toString().equals(param2.getText().toString())) {
            isMatch = true;
        }
        return isMatch;
    }

    public boolean isEmailPatternMatch(EditText param1) {
        // boolean isMatch = false;
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(param1.getText().toString()).matches();
    }
//end of main class
}
