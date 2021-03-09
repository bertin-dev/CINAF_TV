package com.cinaf.android_tv.ui.player.caster;


import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.cinaf.android_tv.data.models.Poster;

import java.util.List;

public class PosterListLoader extends AsyncTaskLoader<List<Poster>> {

    private static final String TAG = "PosterListLoader";
    private final String mUrl;

    public PosterListLoader(Context context, String url ) {
        super(context);
        this.mUrl = url;
    }

    @Override
    public List<Poster> loadInBackground() {
        Log.w("TAG", "DADI: ");
        try {

            Log.w("TAG", "MovieListLoader11111111111: "+ PosterList.setupMovies(mUrl) );
            return PosterList.setupMovies(mUrl);
        } catch (Exception e) {
            Log.w("TAG", "PAPOU: "+ e.getMessage());
            Log.e(TAG, "Failed to fetch media data", e);
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
