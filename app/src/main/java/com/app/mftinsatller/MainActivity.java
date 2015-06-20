package com.app.mftinsatller;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import helpers.ComplexPreferences;
import helpers.PrefUtils;
import helpers.User;
import android.util.Log;

public class MainActivity extends ActionBarActivity {
    JazzyListView applistView;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        applistView = (JazzyListView)findViewById(R.id.applistView);

        applistView.setTransitionEffect(new CardsEffect());


        fetchAPK_Info();






    }


    private void fetchAPK_Info(){
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("APK_Info");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                progressDialog.dismiss();

                if (e == null) {
                    if (parseObjects.size() != 0) {

                        ArrayList<String> appNames = new ArrayList<String>();
                        ArrayList<String> appDates = new ArrayList<String>();
                        ArrayList<String> appLinks = new ArrayList<String>();

                        for (int i = 0; i < parseObjects.size(); i++) {

                            String app_name = parseObjects.get(i).getString("app_name");
                            String created_on = parseObjects.get(i).getString("upload_date");
                            String download_link = parseObjects.get(i).getString("download_link");


/*

                            String MainDate =  String.valueOf(created_on);
                           String tempDate =  MainDate;
                        //   String formattedDate = tempDate.substring(0,MainDate.lastIndexOf(","));

*/

                            appNames.add(i, app_name);
                            appDates.add(i, created_on);
                            appLinks.add(i, download_link);
                        }

                        ListAdapter adp = new ListAdapter(MainActivity.this, appNames, appDates, appLinks);
                        applistView.setAdapter(adp);


                    } else {

                        Toast.makeText(MainActivity.this, "Login Failed. Please check email or password !!!", Toast.LENGTH_SHORT).show();
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
                intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/MFT/" + appName)), "application/vnd.android.package-archive");
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

            String PATH = Environment.getExternalStorageDirectory() + "/MFT/";
            File file = new File(PATH); // PATH = /mnt/sdcard/download/
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

                break;
            case R.id.logout:
                PrefUtils.setLogin(MainActivity.this, false);
                Intent i = new Intent(MainActivity.this,LoginScreen.class);
                startActivity(i);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
