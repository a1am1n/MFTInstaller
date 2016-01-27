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
import com.astuetz.PagerSlidingTabStrip;
import com.eslav.zeori276120.AdConfig;
import com.eslav.zeori276120.AdListener;
import com.eslav.zeori276120.AdView;
import com.eslav.zeori276120.EulaListener;
import com.eslav.zeori276120.Main;

public class MainActivity extends ActionBarActivity   implements AdListener, EulaListener {



    private Main main; //Declare here
    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AdConfig.setAppId(298354);  //setting appid.
        AdConfig.setApiKey("1453826219276120737"); //setting apikey
        AdConfig.setEulaListener(this); //setting EULA listener.
        AdConfig.setAdListener(this);  //setting global Ad listener.
        AdConfig.setCachingEnabled(true); //Enabling SmartWall ad caching.
        AdConfig.setPlacementId(0); //pass the placement id.
        AdConfig.setEulaLanguage(AdConfig.EulaLanguage.ENGLISH); //Set the eula langauge

        setContentView(R.layout.activity_main);

        //Initialize Airpush
        main=new Main(this);


        //for calling Smartwall ad
        main.startInterstitialAd(AdConfig.AdType.smartwall);

        AdView adView=(AdView) findViewById(R.id.myAdView);

        adView.setBannerType(AdView.BANNER_TYPE_IN_APP_AD);
        adView.setBannerAnimation(AdView.ANIMATION_TYPE_FADE);
        adView.showMRinInApp(false);
       // adView.setNewAdListener(this);
        adView.loadAd();

        init();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
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




    @Override
    public void optinResult(boolean b) {

    }

    @Override
    public void showingEula() {

    }

    @Override
    public void onAdCached(AdConfig.AdType adType) {

    }

    @Override
    public void onIntegrationError(String s) {

    }

    @Override
    public void onAdError(String s) {

    }

    @Override
    public void noAdListener() {

    }

    @Override
    public void onAdShowing() {

    }

    @Override
    public void onAdClosed() {

    }

    @Override
    public void onAdLoadingListener() {

    }

    @Override
    public void onAdLoadedListener() {

    }

    @Override
    public void onCloseListener() {

    }

    @Override
    public void onAdExpandedListner() {

    }

    @Override
    public void onAdClickedListener() {

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
