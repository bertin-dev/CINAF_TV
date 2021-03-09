package com.cinaf.android_tv.ui.player.caster;


import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.cinaf.android_tv.data.models.Episode;

import java.util.List;

public class SerieListLoader extends AsyncTaskLoader<List<Episode>> {

    private static final String TAG = "SerieListLoader";
    private final String mUrl;

    public SerieListLoader(Context context, String url ) {
        super(context);
        this.mUrl = url;
    }

    @Override
    public List<Episode> loadInBackground() {
        Log.w("TAG", "DADI: ");
        try {
            return SerieList.setupSerie(mUrl);
        } catch (Exception e) {
            Log.w("TAG", "PAPOU: "+ e.getMessage());
            return null;
        }
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

}
