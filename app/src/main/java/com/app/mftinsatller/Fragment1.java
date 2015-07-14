package com.app.mftinsatller;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Krishna on 14-07-2015.
 */
public class Fragment1 extends Fragment {

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
            View view =  inflater.inflate(R.layout.fragment1, container, false);




        return  view;
    }


}
