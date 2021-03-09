package com.cinaf.android_tv.ui.detail;

import android.os.Bundle;

import com.cinaf.android_tv.data.models.Poster;
import com.cinaf.android_tv.ui.base.BaseTVActivity;


public class DetailPosterActivity extends BaseTVActivity {

    //GlideBackgroundManager glideBackgroundManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Poster
        Poster poster = getIntent().getExtras().getParcelable(Poster.class.getSimpleName());

        DetailPosterFragment detailPosterFragment = DetailPosterFragment.newInstance(poster);
        addFragment(detailPosterFragment);



        /*glideBackgroundManager = new GlideBackgroundManager(this);

        if(poster != null && poster.getImage() != null){
            //glideBackgroundManager.loadImage(poster.getCover());
            glideBackgroundManager.setBackgroundColors(Color.parseColor("#FF263238"));
        }
        else {
            glideBackgroundManager.setBackground(ContextCompat.getDrawable(this, R.drawable.placeholder));
        }*/

    }
}
