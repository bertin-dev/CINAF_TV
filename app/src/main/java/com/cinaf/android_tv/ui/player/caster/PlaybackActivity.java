package com.cinaf.android_tv.ui.player.caster;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.cinaf.android_tv.translate.LocaleHelper;
import com.google.android.gms.cast.tv.CastReceiverContext;
import com.google.android.gms.cast.tv.media.MediaManager;

import java.util.Locale;

public class PlaybackActivity extends FragmentActivity {

    private PlaybackVideoFragment playbackVideoFragment;

    String currentLanguage = (Locale.getDefault().getLanguage().contentEquals("fr")) ? "fr" : "en", currentLang;
    @Override
    protected void onStart() {
        super.onStart();
        //langue par défaut
        currentLanguage = getIntent().getStringExtra(currentLang);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        playbackVideoFragment = new PlaybackVideoFragment();
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, playbackVideoFragment)
                    .commit();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        MediaManager mediaManager = CastReceiverContext.getInstance().getMediaManager();
        if (mediaManager.onNewIntent(intent)) {
            // If the SDK recognizes the intent, you should early return.
            return;
        }

        // If the SDK doesn’t recognize the intent, you can handle the intent with
        // your own logic.
        playbackVideoFragment.processIntent(intent);
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