package com.app.mftinsatller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
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

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import helpers.PrefUtils;


public class RegistrationVerification extends ActionBarActivity {
    ProgressDialog progressDialog;
    TextView txtVerify;
    EditText etCode,etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_verifcation);


        setupAnimation();


        initViews();

        txtVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInternet()) {
                    if (isEmptyField(etEmail)) {
                        Toast.makeText(RegistrationVerification.this, "Please enter email id !!!", Toast.LENGTH_SHORT).show();
                    }else if (!isEmailPatternMatch(etEmail)) {
                        Toast.makeText(RegistrationVerification.this, "Please enter valid email !!!", Toast.LENGTH_SHORT).show();
                    }
                    else if (isEmptyField(etCode)) {
                        Toast.makeText(RegistrationVerification.this, "Please enter verification code !!!", Toast.LENGTH_SHORT).show();
                    } else {
                        processCheckUser(etEmail.getText().toString().trim().toUpperCase(),etCode.getText().toString().trim());

                    }

                } else {
                    Toast.makeText(RegistrationVerification.this, "Please connect your Internet", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (checkInternet()) {
            boolean isLogin = PrefUtils.getLogin(RegistrationVerification.this);

            if (isLogin) {
                Intent i = new Intent(RegistrationVerification.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        } else {
            Toast.makeText(RegistrationVerification.this, "Please connect your Internet", Toast.LENGTH_LONG).show();
        }
    }

    private void initViews() {
        txtVerify = (TextView) findViewById(R.id.txtVerify);
        etEmail= (EditText) findViewById(R.id.etEmail);
        etCode = (EditText) findViewById(R.id.etCode);
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


    private void processCheckUser(final String useremail,String code) {

        progressDialog = new ProgressDialog(RegistrationVerification.this);
        progressDialog.setMessage("Checking details...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Registration_Verification");
        query.whereEqualTo("user_email", useremail);
        query.whereEqualTo("verification_code", code);


        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                progressDialog.dismiss();

                if (e == null) {
                    if (parseObjects.size() != 0) {

                    /*    String email = parseObjects.get(0).getString("user_email");
                        String mobile = parseObjects.get(0).getString("user_contact");
                        String name = parseObjects.get(0).getString("user_name");
                        String password = parseObjects.get(0).getString("user_password");
*/
                        Intent i2 = new Intent(RegistrationVerification.this,RegisterationScreen.class);
                        i2.putExtra("email",useremail);
                        startActivity(i2);
                        finish();



                    } else {

                        Toast.makeText(RegistrationVerification.this, "Sorry you are not authorize for registration, Please Contact to MFT Administrator !!!", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(RegistrationVerification.this, "Error to fetch details !!!", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }



    private boolean checkInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(RegistrationVerification.this.CONNECTIVITY_SERVICE);
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
