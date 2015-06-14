package com.app.mftinsatller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;
import java.util.regex.Pattern;

import helpers.ComplexPreferences;
import helpers.PrefUtils;
import helpers.User;


public class RegisterationScreen extends ActionBarActivity {
    private EditText etEmail,etUName,etPassword,etCnfPassword,etContact,etDevice;
    private TextView txtBtnRegister;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registeration);

        initView();

        txtBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInternet()) {
                    if (isEmptyField(etEmail)) {
                        Toast.makeText(RegisterationScreen.this, "Please enter email ID !!!", Toast.LENGTH_SHORT).show();
                    } else if (isEmptyField(etUName)) {
                        Toast.makeText(RegisterationScreen.this, "Please enter full name !!!", Toast.LENGTH_SHORT).show();
                    } else if (isEmptyField(etPassword)) {
                        Toast.makeText(RegisterationScreen.this, "Please enter password !!!", Toast.LENGTH_SHORT).show();
                    } else if (isEmptyField(etCnfPassword)) {
                        Toast.makeText(RegisterationScreen.this, "Please enter confirm password !!!", Toast.LENGTH_SHORT).show();
                    } else if (isEmptyField(etContact)) {
                        Toast.makeText(RegisterationScreen.this, "Please enter contact number !!!", Toast.LENGTH_SHORT).show();
                    } else if (isEmptyField(etDevice)) {
                        Toast.makeText(RegisterationScreen.this, "Please enter device name !!!", Toast.LENGTH_SHORT).show();
                    } else if (!isEmailPatternMatch(etEmail)) {
                        Toast.makeText(RegisterationScreen.this, "Please enter valid Email ID !!!", Toast.LENGTH_SHORT).show();
                    } else if (!isPasswordMatch(etPassword, etCnfPassword)) {
                        Toast.makeText(RegisterationScreen.this, "Password do not match !!!", Toast.LENGTH_SHORT).show();
                    } else {
                        processCheckID(etEmail.getText().toString().trim().toUpperCase(), etUName.getText().toString().trim(), etCnfPassword.getText().toString().trim(), etContact.getText().toString().trim(), etDevice.getText().toString().trim());

                    }

                } else {
                    Toast.makeText(RegisterationScreen.this, "Please connect your Internet", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void processCheckID(final String uEmail,final String uName,final String uPassword,final String uContact,final String uDevice){
        progressDialog = new ProgressDialog(RegisterationScreen.this);
        progressDialog.setMessage("Saving details...");

        progressDialog.setCancelable(false);
        progressDialog.show();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("User_details");
        query.whereEqualTo("user_email", uEmail);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {


                if (e == null) {
                    if (parseObjects.size() != 0) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterationScreen.this, "Email ID already exists !!!", Toast.LENGTH_SHORT).show();
                    } else {
                        processSaveDatatoServer(uEmail,uName,uPassword,uContact,uDevice);

                    }

                } else {
                    Toast.makeText(RegisterationScreen.this, "Error to fetch details !!!", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }


    private void   processSaveDatatoServer(String uEmail,String uName,String uPassword,String uContact,String uDevice){
        try{
            ParseObject gameScore = new ParseObject("User_details");

            gameScore.put("user_email", uEmail);
            gameScore.put("user_name",uName);
            gameScore.put("user_password", uPassword);
            gameScore.put("user_contact",uContact);
            gameScore.put("user_device", uDevice);

            gameScore.saveInBackground();
            Toast.makeText(RegisterationScreen.this, "New Account Created sucessfully....", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();

            User userData = new User();
            userData.Email = uEmail;
            userData.Mobile = uContact;
            userData.Name = uName;
            userData.Device = uDevice;



            ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(RegisterationScreen.this, "user_pref", 0);
            complexPreferences.putObject("current-user", userData);
            complexPreferences.commit();

            PrefUtils.setUserData(userData, RegisterationScreen.this);

            PrefUtils.setLogin(RegisterationScreen.this, true);

            Intent i = new Intent(RegisterationScreen.this, MainActivity.class);
            startActivity(i);
            finish();

        }catch(Exception e){
            progressDialog.dismiss();
            Toast.makeText(RegisterationScreen.this,"Error Occur while saving details !!!",Toast.LENGTH_LONG).show();
        }
    }


    private void initView(){
        txtBtnRegister = (TextView)findViewById(R.id.txtBtnRegister);
        etEmail = (EditText)findViewById(R.id.etEmail);
        etUName = (EditText)findViewById(R.id.etUName);
        etPassword = (EditText)findViewById(R.id.etPassword);
        etCnfPassword = (EditText)findViewById(R.id.etCnfPassword);
        etContact = (EditText)findViewById(R.id.etContact);
        etDevice = (EditText)findViewById(R.id.etDevice);
    }


    private boolean checkInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(RegisterationScreen.this.CONNECTIVITY_SERVICE);
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
