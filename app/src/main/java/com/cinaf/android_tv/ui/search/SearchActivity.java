package com.cinaf.android_tv.ui.search;

import android.content.Intent;
import android.os.Bundle;

import androidx.leanback.app.SearchFragment;

import com.cinaf.android_tv.R;
import com.cinaf.android_tv.ui.base.BaseTVActivity;

public class SearchActivity extends BaseTVActivity {

    SearchFragment searchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addFragment(com.cinaf.android_tv.ui.search.SearchFragment.newInstance());

        searchFragment = (SearchFragment) getFragmentManager().findFragmentById(R.id.search_fragment);

    }


    @Override
    public boolean onSearchRequested() {
        startActivity(new Intent(this, SearchActivity.class));
        return true;
    }
}
