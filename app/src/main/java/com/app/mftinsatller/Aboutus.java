package com.app.mftinsatller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;


public class Aboutus extends ActionBarActivity {

    TextView txtvpn,youtube,txtContact;

    String CONTACT_URL = "http://mtlfreetv.com/contact-us/contact-us-form";
    String YOUTUBE_URL = "https://www.youtube.com/channel/UC389S4_2Yt9cei1qNDwmBrA?sub_confirmation=1";
    String VPN_URL = "https://www.bit.ly/mftvpn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutus);

        txtContact = (TextView)findViewById(R.id.txtContact);
        youtube = (TextView)findViewById(R.id.youtube);
        txtvpn = (TextView)findViewById(R.id.txtvpn);


        txtContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(CONTACT_URL));
                startActivity(i);
            }
        });

        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(YOUTUBE_URL));
                startActivity(i);
            }
        });

        txtvpn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(VPN_URL));
                startActivity(i);
            }
        });
    }

}
