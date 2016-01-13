package com.app.mftinsatller;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appnext.appnextinterstitial.InterstitialManager;
import com.appnext.appnextinterstitial.OnAdLoaded;
import com.astuetz.PagerSlidingTabStrip;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.twotoasters.jazzylistview.JazzyListView;
import com.twotoasters.jazzylistview.effects.CardsEffect;
import com.twotoasters.jazzylistview.effects.CurlEffect;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import helpers.ComplexPreferences;
import helpers.PrefUtils;
import helpers.User;
import android.util.Log;

public class MainActivity extends ActionBarActivity {


   // InterstitialAd interstitial;
    AdRequest adRequest;

    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InterstitialManager.cacheInterstitial(MainActivity.this, getResources().getString(R.string.appnext_placementId),
                InterstitialManager.INTERSTITIAL_VIDEO);
        InterstitialManager.showInterstitial(MainActivity.this, getResources().getString(R.string.appnext_placementId),
                InterstitialManager.INTERSTITIAL_VIDEO);
        InterstitialManager.setSkipText("Skip");
        InterstitialManager.setButtonColor("#273d4e");
        InterstitialManager.setCanClose(true);





        //   interstitial = new InterstitialAd(MainActivity.this);
      //  interstitial.setAdUnitId("ca-app-pub-4832975497842027/7436223590");

/*
        AdView adView = (AdView) findViewById(R.id.adView);
        // Request for Ads
        adRequest = new AdRequest.Builder()
                .build();

        // Load ads into Banner Ads
        adView.loadAd(adRequest);
*/





/*
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(MainActivity.this, "user_pref", 0);
        User userData = complexPreferences.getObject("current-user", User.class);
        Log.e("email ",userData.Email);

        checkBlockStatus(userData.Email);
*/
        init();




    }

    private void init(){
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        adapter = new MyPagerAdapter(getSupportFragmentManager());


//        tabs.setDividerColorResource(Color.WHITE);
        tabs.setIndicatorColorResource(R.color.theme_primary);
        tabs.setShouldExpand(true);
        pager.setAdapter(adapter);


        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);


        tabs.setViewPager(pager);
    }


    public void displayInterstitial() {
        // If Ads are loaded, show Interstitial else show nothing.
       /* if (interstitial.isLoaded()) {
            interstitial.show();
        }*/
    }


    /*private void checkBlockStatus(final String useremail) {

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Checking details...");
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

                        boolean isBlock = parseObjects.get(0).getBoolean("block");

                        Date sub_end_date = parseObjects.get(0).getDate("subscription_end_date");

                        String sub_end_date_string = String.valueOf(sub_end_date);

                        boolean isSubscriptionExpiry=false;

                        //Tue Jun 23 16:42:00 GMT 2015
                        SimpleDateFormat sdfTime = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT' yyyy");


                        try {
                            Date sub_endDate = sdfTime.parse(sub_end_date_string);

                            Date nowDate = new Date();
                            String today_date_string = String.valueOf(sub_end_date);

                            if (nowDate.after(sub_endDate)) {
                                Log.e("### inside if",""+ nowDate);
                                isSubscriptionExpiry = true;
                            } else if (nowDate.before(sub_endDate)) {
                                Log.e("### inside else",""+ nowDate);
                                isSubscriptionExpiry = false;
                            } else if (nowDate.equals(sub_endDate)) {
                                Log.e("### inside equal",""+ nowDate);
                                isSubscriptionExpiry = false;
                            }


                        }catch (Exception ee){
                            Log.e("---- EXCe",""+ ee.toString());
                            isSubscriptionExpiry = false;
                        }





                        if(isBlock){

                            Log.e("inside block","inside");
                            Toast.makeText(MainActivity.this, "You are account is BLOCKED, Please contact to MFT Administrator !!!", Toast.LENGTH_LONG).show();

                            PrefUtils.setLogin(MainActivity.this, false);

                            Intent i = new Intent(MainActivity.this,LoginScreen.class);
                            startActivity(i);
                            finish();
                        }else if(isSubscriptionExpiry){
                            Log.e("inside else if", "inside sub expiry");

                            Toast.makeText(MainActivity.this, "Your Free Trial version is expired, Please contact to MFT Administrator !!!", Toast.LENGTH_LONG).show();

                            PrefUtils.setLogin(MainActivity.this, false);

                            Intent i = new Intent(MainActivity.this,LoginScreen.class);
                            startActivity(i);
                            finish();
                        }else {

                            Log.e("inside block","else ");
                         //   fetchAPK_Info();
                        }


                    }

                } else {
                    Toast.makeText(MainActivity.this, "Error to fetch details !!!", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }
*/






    public class MyPagerAdapter extends FragmentPagerAdapter {




        private final String[] TITLES = { "Recommended", "Movies/TV", "Live TV","Music","Other Apps" };




        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }






        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }




        @Override
        public int getCount() {
            return TITLES.length;
        }




        @Override
        public Fragment getItem(int position) {


            if (position==0) {
                return Fragment1.newInstance();
            }else if(position==1) {
                return Fragment2.newInstance();
            }else if(position==2) {
                return Fragment3.newInstance();
            }else if(position==3) {
                return Fragment4.newInstance();
            }else if(position==4) {
                return Fragment5.newInstance();
            }else{
                return Fragment1.newInstance();
            }
        }




    }











    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        switch (id){
            case R.id.action_settings:
                Intent i = new Intent(MainActivity.this,Aboutus.class);
                startActivity(i);

                break;

        }

        return super.onOptionsItemSelected(item);
    }
}
