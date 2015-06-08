package com.app.mftinsatller;

import android.content.Context;
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

import com.twotoasters.jazzylistview.JazzyListView;
import com.twotoasters.jazzylistview.effects.CardsEffect;
import com.twotoasters.jazzylistview.effects.CurlEffect;


public class MainActivity extends ActionBarActivity {
    JazzyListView applistView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        applistView = (JazzyListView)findViewById(R.id.applistView);

        applistView.setTransitionEffect(new CardsEffect());

        ListAdapter adp = new ListAdapter(MainActivity.this);
        applistView.setAdapter(adp);
        applistView.setAdapter(adp);

    }


    class ListAdapter extends BaseAdapter {
        LayoutInflater layoutInflator;
        private Context ctx;

        public ListAdapter(Context ctx){
            this.ctx = ctx;

        }


        @Override
        public int getCount() {
            return 10;
        }


        @Override
        public Object getItem(int i) {
            return 0;
        }


        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            layoutInflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = convertView;
            view = layoutInflator.inflate(R.layout.list_item, viewGroup, false);

            TextView txtAppName  = (TextView)findViewById(R.id.txtAppName);
            TextView txtDate  = (TextView)findViewById(R.id.txtDate);
            ImageView imgDownload = (ImageView)findViewById(R.id.imgDownload);

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
