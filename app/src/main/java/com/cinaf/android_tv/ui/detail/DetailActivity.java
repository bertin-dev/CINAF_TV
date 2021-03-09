package com.cinaf.android_tv.ui.detail;

import android.os.Bundle;

import com.cinaf.android_tv.data.models.Actor;
import com.cinaf.android_tv.ui.base.BaseTVActivity;


public class DetailActivity extends BaseTVActivity {

    //GlideBackgroundManager glideBackgroundManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Actor
        Actor actor = getIntent().getExtras().getParcelable(Actor.class.getSimpleName());

        DetailFragment detailsFragment = DetailFragment.newInstance(actor);
        addFragment(detailsFragment);

        /*glideBackgroundManager = new GlideBackgroundManager(this);

        if(actor.getImage() != null){
            //glideBackgroundManager.loadImage(actor.getImage());
            glideBackgroundManager.setBackgroundColors(Color.parseColor("#FF263238"));
        } else {
            glideBackgroundManager.setBackground(ContextCompat.getDrawable(this, R.drawable.placeholder));
        }*/

    }
}
