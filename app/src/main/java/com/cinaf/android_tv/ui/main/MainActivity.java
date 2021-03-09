package com.cinaf.android_tv.ui.main;

import android.os.Bundle;

import com.cinaf.android_tv.ui.base.BaseTVActivity;


public class MainActivity extends BaseTVActivity {

    public static final String POSTER = "Poster";
    public static final String EPISODE = "Episode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addFragment(MainFragment.newInstance());
    }

}
