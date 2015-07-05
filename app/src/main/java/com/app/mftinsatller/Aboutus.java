package com.app.mftinsatller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class Aboutus extends ActionBarActivity {

    Button txtvpn,youtube,txtContact,txtinst;

    String CONTACT_URL = "http://mtlfreetv.com/contact-us/contact-us-form";
    String YOUTUBE_URL = "https://www.youtube.com/channel/UC389S4_2Yt9cei1qNDwmBrA?sub_confirmation=1";
    String VPN_URL = "https://www.bit.ly/mftvpn";
    String INST_URL = "http://www.sn.im/mftinstaller";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutus);

        txtContact = (Button)findViewById(R.id.txtContact);
        youtube = (Button)findViewById(R.id.youtube);
        txtvpn = (Button)findViewById(R.id.txtvpn);
        txtinst = (Button)findViewById(R.id.txtinst);


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

        txtinst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(INST_URL));
                startActivity(i);
            }
        });
    }

}
