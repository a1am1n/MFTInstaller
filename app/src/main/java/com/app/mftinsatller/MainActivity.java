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
    JazzyListView applistView;
    ProgressDialog progressDialog,progressDialog2;
    InterstitialAd interstitial;
    AdRequest adRequest;

    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        interstitial = new InterstitialAd(MainActivity.this);
        interstitial.setAdUnitId("ca-app-pub-4832975497842027/7436223590");


        AdView adView = (AdView) findViewById(R.id.adView);
        // Request for Ads
        adRequest = new AdRequest.Builder()
                .build();

        // Load ads into Banner Ads
        adView.loadAd(adRequest);


        applistView = (JazzyListView)findViewById(R.id.applistView);

        applistView.setTransitionEffect(new CardsEffect());



/*
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(MainActivity.this, "user_pref", 0);
        User userData = complexPreferences.getObject("current-user", User.class);
        Log.e("email ",userData.Email);

        checkBlockStatus(userData.Email);
*/
        init();

        fetchAPK_Info();


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
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }


    private void checkBlockStatus(final String useremail) {

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
                            fetchAPK_Info();
                        }


                    }

                } else {
                    Toast.makeText(MainActivity.this, "Error to fetch details !!!", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    private void fetchAPK_Info(){
        progressDialog2 = new ProgressDialog(MainActivity.this);
        progressDialog2.setMessage("Please wait...");
        progressDialog2.setCancelable(false);
        progressDialog2.show();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("APK_Info");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                progressDialog2.dismiss();

                if (e == null) {
                    if (parseObjects.size() != 0) {

                        ArrayList<String> appNames = new ArrayList<String>();
                        ArrayList<String> appDates = new ArrayList<String>();
                        ArrayList<String> appLinks = new ArrayList<String>();

                        for (int i = 0; i < parseObjects.size(); i++) {

                            String app_name = parseObjects.get(i).getString("app_name");
                            String created_on = parseObjects.get(i).getString("upload_date");
                            String download_link = parseObjects.get(i).getString("download_link");


                            appNames.add(i, app_name);
                            appDates.add(i, created_on);
                            appLinks.add(i, download_link);
                        }

                        ListAdapter adp = new ListAdapter(MainActivity.this, appNames, appDates, appLinks);
                        applistView.setAdapter(adp);


                    } else {

                        Toast.makeText(MainActivity.this, "Unable to fetch data !!!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Error to fetch details !!!", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }



    void callDownloadAscyntask(final String appURL,final String appName){

        new AsyncTask<Void,Void,Void>(){
            ProgressDialog dialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ProgressDialog(MainActivity.this);
                dialog.setMessage("Downloading application");
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                DownloadOnSDcard(appURL,appName);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                dialog.dismiss();

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/.MFT/" + appName)), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        }.execute();
    }


    // Download On My Mobile SDCard or Emulator.
    public void DownloadOnSDcard(String apkpath,String apkname)
    {
        try{
            //URL url = new URL(urlpath.toString()); // Your given URL.

            URL url = new URL(apkpath); // Your given URL.

            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect(); // Connection Complete here.!

            //Toast.makeText(getApplicationContext(), "HttpURLConnection complete.", Toast.LENGTH_SHORT).show();

            String PATH = Environment.getExternalStorageDirectory() + "/.MFT/";
            File file = new File(PATH);

try {
    // to delete previous files starts
    String[] entries = file.list();
    for (String s : entries) {
        File currentFile = new File(file.getPath(), s);
        currentFile.delete();
    }
    file.delete();
    // to delete previous files ends
}catch (Exception e){
    Log.e("###### Exc ",e.toString());
}

            if (!file.exists()) {
                file.mkdirs();
            }
            File outputFile = new File(file, apkname);
            FileOutputStream fos = new FileOutputStream(outputFile);

            //      Toast.makeText(getApplicationContext(), "SD Card Path: " + outputFile.toString(), Toast.LENGTH_SHORT).show();

            InputStream is = c.getInputStream(); // Get from Server and Catch In Input Stream Object.

            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len1); // Write In FileOutputStream.
            }
            fos.close();
            is.close();//till here, it works fine - .apk is download to my sdcard in download file.
            // So plz Check in DDMS tab and Select your Emualtor.

            //Toast.makeText(getApplicationContext(), "Download Complete on SD Card.!", Toast.LENGTH_SHORT).show();
            //download the APK to sdcard then fire the Intent.
        }
        catch (IOException e)
        {
            Log.e("Error in download - ",e.toString());
          /*  Toast.makeText(getApplicationContext(), "Error! " +
                    e.toString(), Toast.LENGTH_LONG).show();*/
        }
    }




    public class MyPagerAdapter extends FragmentPagerAdapter {




        private final String[] TITLES = { "Pending", "Participant", "Message" };




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
                return Fragment1.newInstance();
            }else if(position==2) {
                return Fragment1.newInstance();
            }else{
                return Fragment1.newInstance();
            }
        }




    }










    class ListAdapter extends BaseAdapter {
        LayoutInflater layoutInflator;
        private Context ctx;
        ArrayList<String> valuesAppNames;
        ArrayList<String> valuesAppDates;
        ArrayList<String> valuesAppLinks;

        public ListAdapter(Context ctx, ArrayList<String> name, ArrayList<String> datee, ArrayList<String> link){
            this.ctx = ctx;
            this.valuesAppNames = name;
            this.valuesAppDates = datee;
            this.valuesAppLinks = link;

        }


        @Override
        public int getCount() {
            return valuesAppNames.size();
        }


        @Override
        public Object getItem(int i) {
            return valuesAppNames.get(i);
        }


        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View convertView, ViewGroup viewGroup) {
            layoutInflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = convertView;
            view = layoutInflator.inflate(R.layout.list_item, viewGroup, false);

            TextView txtAppName  = (TextView)view.findViewById(R.id.txtAppName);
            TextView txtDate  = (TextView)view.findViewById(R.id.txtDate);
            ImageView imgDownload = (ImageView)view.findViewById(R.id.imgDownload);


            txtAppName.setText(valuesAppNames.get(i));
            txtDate.setText(valuesAppDates.get(i));
           // txtAppName.setText(valuesAppLinks.get(i));

            imgDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    callDownloadAscyntask(valuesAppLinks.get(i), valuesAppNames.get(i) + ".apk");
                    Log.e("Download link:- ", valuesAppLinks.get(i));
                    //Toast.makeText(ctx,"Download link:- "+valuesAppLinks.get(i),Toast.LENGTH_SHORT).show();


                    interstitial.loadAd(adRequest);
                    // Prepare an Interstitial Ad Listener
                    interstitial.setAdListener(new AdListener() {
                        public void onAdLoaded() {
                            // Call displayInterstitial() function
                            displayInterstitial();
                        }
                    });
                }
            });

            return view;
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
