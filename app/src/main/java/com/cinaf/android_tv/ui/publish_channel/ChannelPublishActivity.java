package com.cinaf.android_tv.ui.publish_channel;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.leanback.app.GuidedStepFragment;

import com.cinaf.android_tv.translate.LocaleHelper;

/**
 * Activity that showcases different aspects of GuidedStepFragments.
 */
public class ChannelPublishActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null == savedInstanceState) {
            GuidedStepFragment.addAsRoot(this, new PublishChannelFragment(), android.R.id.content);
        }
    }

    /**
     * attachBaseContext(Context newBase) methode callback permet de verifier la langue au demarrage
     * @param newBase
     * @since 2021
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

}