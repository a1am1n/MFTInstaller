package com.app.mftinsatller;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Krishna on 14-07-2015.
 */
public class Fragment1 extends Fragment {
    ProgressDialog progressDialog,progressDialog2;
    JazzyListView applistView;

    InterstitialAd interstitial;
    AdRequest adRequest;
    public static Fragment1 newInstance() {
        Fragment1 f = new Fragment1();
        return f;
    }
    public Fragment1() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public void displayInterstitial() {
        // If Ads are loaded, show Interstitial else show nothing.
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
            View view =  inflater.inflate(R.layout.fragment1, container, false);



        interstitial = new InterstitialAd(getActivity());
        interstitial.setAdUnitId("ca-app-pub-4832975497842027/7436223590");
        // Request for Ads
        adRequest = new AdRequest.Builder()
                .build();



        applistView = (JazzyListView)view.findViewById(R.id.applistView);

        applistView.setTransitionEffect(new CardsEffect());

        fetchAPK_Info();
        return  view;
    }

    private void fetchAPK_Info(){
        progressDialog2 = new ProgressDialog(getActivity());
        progressDialog2.setMessage("Please wait...");
        progressDialog2.setCancelable(false);
        progressDialog2.show();
//Recommended_APK
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Recommended_APK");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                progressDialog2.dismiss();

                if (e == null) {
                    if (parseObjects.size() != 0) {

                        ArrayList<String> appNames = new ArrayList<String>();
                        ArrayList<String> appDates = new ArrayList<String>();
                        ArrayList<String> appLinks = new ArrayList<String>();

                        ArrayList<currentData> currArray = new ArrayList<currentData>();

                        Data1 obj = new Data1();

                        for (int i = 0; i < parseObjects.size(); i++) {

                            String app_name = parseObjects.get(i).getString("app_name");
                            String created_on = parseObjects.get(i).getString("upload_date");
                            String download_link = parseObjects.get(i).getString("download_link");


                            currentData cur = new currentData();
                            cur.appName = app_name;
                            cur.date = created_on;
                            cur.link = download_link;

                            appNames.add(i, app_name);
                            appDates.add(i, created_on);
                            appLinks.add(i, download_link);

                            currArray.add(cur);

                        }


                        obj.DATA = currArray;



                        Collections.sort(appNames);
                        Collections.sort(appDates);
                        Collections.sort(appLinks);





                        ListAdapter adp = new ListAdapter(getActivity(), appNames, appDates, appLinks,obj);
                        adp.sorting();
                        applistView.setAdapter(adp);


                    } else {

                        Toast.makeText(getActivity(), "Unable to fetch data !!!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getActivity(), "Error to fetch details !!!", Toast.LENGTH_SHORT).show();

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
                dialog = new ProgressDialog(getActivity());
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
                Log.e("###### Exc ", e.toString());
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

    class ListAdapter extends BaseAdapter {
        LayoutInflater layoutInflator;
        private Context ctx;
        ArrayList<String> valuesAppNames;
        ArrayList<String> valuesAppDates;
        ArrayList<String> valuesAppLinks;
        Data1 valueOBJ;


        List<currentData> ValuesSearch;
        ArrayList<currentData> arraylist;

        public ListAdapter(Context ctx, ArrayList<String> name, ArrayList<String> datee, ArrayList<String> link,Data1 dataObj){
            this.ctx = ctx;
            this.valuesAppNames = name;
            this.valuesAppDates = datee;
            this.valuesAppLinks = link;
            this.valueOBJ = dataObj;

            this.ValuesSearch = dataObj.DATA;

            arraylist = new ArrayList<currentData>();
            arraylist.addAll(ValuesSearch);
        }


        @Override
        public int getCount() {
            return valueOBJ.DATA.size();
        }


        @Override
        public Object getItem(int i) {
            return valueOBJ.DATA.get(i);
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


            txtAppName.setText(valueOBJ.DATA.get(i).appName);
            txtDate.setText(valueOBJ.DATA.get(i).date);
            // txtAppName.setText(valuesAppLinks.get(i));

            imgDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    callDownloadAscyntask(valueOBJ.DATA.get(i).link, valueOBJ.DATA.get(i).appName + ".apk");
                    Log.e("Download link:- ", valueOBJ.DATA.get(i).link);
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

        // Filter Class
        public void sorting() {

            Collections.sort(ValuesSearch, new Comparator<currentData>() {
                @Override
                public int compare(currentData object1, currentData object2) {
                    return object1.appName.compareTo(object2.appName);
                }
            });



            notifyDataSetChanged();
        }
    }

}
