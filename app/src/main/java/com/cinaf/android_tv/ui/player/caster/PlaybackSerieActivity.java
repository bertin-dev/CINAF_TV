package com.cinaf.android_tv.ui.player.caster;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.cinaf.android_tv.data.models.Episode;
import com.cinaf.android_tv.data.models.Poster;
import com.cinaf.android_tv.translate.LocaleHelper;
import com.cinaf.android_tv.ui.main.MainActivity;
import com.google.android.gms.cast.tv.CastReceiverContext;
import com.google.android.gms.cast.tv.media.MediaManager;

import java.util.List;
import java.util.Locale;


public class PlaybackSerieActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<List<Episode>> {

    private PlaybackSerieVideoFragment playbackSerieVideoFragment;
    String currentLanguage = (Locale.getDefault().getLanguage().contentEquals("fr")) ? "fr" : "en", currentLang;
    private Poster poster;

    @Override
    protected void onStart() {
        super.onStart();
        //langue par défaut
        currentLanguage = getIntent().getStringExtra(currentLang);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Poster
        poster = getIntent().getExtras().getParcelable(MainActivity.POSTER);

        getLoaderManager().initLoader(0, null, this);
        playbackSerieVideoFragment = new PlaybackSerieVideoFragment();
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, playbackSerieVideoFragment)
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
        playbackSerieVideoFragment.processIntent(intent);
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


    @Override
    public Loader<List<Episode>> onCreateLoader(int id, Bundle args) {
        Log.w("TAG11", "onCreateLoader: " + poster.getId() );
        return new SerieListLoader(this,"https://cinaf.fr/api/season/by/serie/"+poster.getId()+"/4F5A9C3D9A86FA54EACEDDD635185/d506abfd-9fe2-4b71-b979-feff21bcad13/");
    }

    @Override
    public void onLoadFinished(Loader<List<Episode>> loader, List<Episode> data) {
    }

    @Override
    public void onLoaderReset(Loader<List<Episode>> loader) {

    }

}