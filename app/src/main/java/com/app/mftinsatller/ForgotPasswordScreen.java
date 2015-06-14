package com.app.mftinsatller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import helpers.ComplexPreferences;
import helpers.PrefUtils;
import helpers.User;


public class ForgotPasswordScreen extends ActionBarActivity {
    ProgressDialog progressDialog;
    TextView txtSend;
    EditText etEmailID;
    private static final String username = "unipexapp@gmail.com";
    private static final String password = "exp@uni2015";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpassword);


        setupAnimation();


        initViews();

        txtSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInternet()) {
                    if (isEmptyField(etEmailID)) {
                        Toast.makeText(ForgotPasswordScreen.this, "Please enter Email ID !!!", Toast.LENGTH_SHORT).show();
                    } else if (!isEmailPatternMatch(etEmailID)) {
                        Toast.makeText(ForgotPasswordScreen.this, "Please enter valid Email ID !!!", Toast.LENGTH_SHORT).show();
                    } else {
                        processCheckUser(etEmailID.getText().toString().trim().toUpperCase());

                    }

                } else {
                    Toast.makeText(ForgotPasswordScreen.this, "Please connect your Internet", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (checkInternet()) {
            boolean isLogin = PrefUtils.getLogin(ForgotPasswordScreen.this);

            if (isLogin) {
                Intent i = new Intent(ForgotPasswordScreen.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        } else {
            Toast.makeText(ForgotPasswordScreen.this, "Please connect your Internet", Toast.LENGTH_LONG).show();
        }
    }

    private void initViews() {
        txtSend = (TextView) findViewById(R.id.txtSend);

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


    private void processCheckUser(String useremail) {

        progressDialog = new ProgressDialog(ForgotPasswordScreen.this);
        progressDialog.setMessage("Checking login details...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("User_details");
        query.whereEqualTo("user_email", useremail);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                progressDialog.dismiss();

                if (e == null) {
                    if (parseObjects.size() != 0) {

                        String email = parseObjects.get(0).getString("user_email");
                        String mobile = parseObjects.get(0).getString("user_contact");
                        String name = parseObjects.get(0).getString("user_name");
                        String password = parseObjects.get(0).getString("user_password");

                        processSendEmail(email, mobile, name, password);


                    } else {

                        Toast.makeText(ForgotPasswordScreen.this, "No user Found !!!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(ForgotPasswordScreen.this, "Error to fetch details !!!", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    private void processSendEmail(String uEmail,String uMobile,String uName,String uPassword){
        if (checkInternet()) {
            String msgBody="Dear "+uName+",\n"+"You are requested for forgot passsword.\n\nYour current password is :   "+uPassword+"\n\n\n\nThank you,\nFor Using MFT Services";
            sendMail(etEmailID.getText().toString().trim(), "MFT Password Notification", msgBody);
        } else {
            Toast.makeText(ForgotPasswordScreen.this, "Please connect your Internet", Toast.LENGTH_SHORT).show();
        }
    }


    private void sendMail(String email, String subject, String messageBody) {
        Session session = createSessionObject();

        try {
            Message message = createMessage(email, subject, messageBody, session);
            new SendMailTask().execute(message);

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Message createMessage(String email, String subject, String messageBody, Session session) throws MessagingException, UnsupportedEncodingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("unipexapp@gmail.com", "Container Details"));

        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
        message.setSubject("MFT Password Notification ");

        message.setText(messageBody);


        return message;
    }

    private class SendMailTask extends AsyncTask<Message, Void, Void> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ForgotPasswordScreen.this);
            progressDialog.setMessage("Sending mail");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();


            Toast.makeText(ForgotPasswordScreen.this,"E-Mail Sent, Please check your email.",Toast.LENGTH_LONG).show();
            finish();
        }

        @Override
        protected Void doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
            } catch (MessagingException e) {
                Log.e("exc", e.toString());
                e.printStackTrace();
            }
            return null;
        }
    }




    private Session createSessionObject() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        return Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }


    private boolean checkInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(ForgotPasswordScreen.this.CONNECTIVITY_SERVICE);
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
