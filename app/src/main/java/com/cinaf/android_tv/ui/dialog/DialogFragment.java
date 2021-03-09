package com.cinaf.android_tv.ui.dialog;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.leanback.app.GuidedStepFragment;
import androidx.leanback.widget.GuidanceStylist.Guidance;
import androidx.leanback.widget.GuidedAction;

import com.cinaf.android_tv.App;
import com.cinaf.android_tv.R;
import com.cinaf.android_tv.data.Api.TheMovieDbAPI;

import java.util.List;

import javax.inject.Inject;

public class DialogFragment extends GuidedStepFragment {

    @Inject
    TheMovieDbAPI theMovieDbAPI;

    private static final String TAG = "DialogFragment";
    private static final int ACTION_ID_POSITIVE = 1;
    private static final int ACTION_ID_NEGATIVE = ACTION_ID_POSITIVE + 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.instance().appComponent().inject(this);
    }

    @NonNull
    @Override
    public Guidance onCreateGuidance(Bundle savedInstanceState) {
        Guidance guidance = new Guidance(getString(R.string.mesFavoris),
                getString(R.string.addFavoris),
                "", null);
        return guidance;
    }

    @Override
    public void onCreateActions(@NonNull List<GuidedAction> actions, Bundle savedInstanceState) {
        GuidedAction action = new GuidedAction.Builder()
                .id(ACTION_ID_POSITIVE)
                .title(getString(R.string.dialog_example_button_positive)).build();
        actions.add(action);
        action = new GuidedAction.Builder()
                .id(ACTION_ID_NEGATIVE)
                .title(getString(R.string.dialog_example_button_negative)).build();
        actions.add(action);
    }

    @Override
    public void onGuidedActionClicked(GuidedAction action) {
        if (ACTION_ID_POSITIVE == action.getId()) {
            Toast.makeText(getActivity(), R.string.dialog_example_button_toast_positive_clicked,
                    Toast.LENGTH_SHORT).show();


            /*theMovieDbAPI.getNowPlayingMovies()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {

                        //startEntranceTransition();
                    }, e -> Timber.e(e, "Error fetching Top Rated movies: %s", e.getMessage()));*/



        } else {
            Toast.makeText(getActivity(), R.string.dialog_example_button_toast_negative_clicked,
                    Toast.LENGTH_SHORT).show();
        }
        getActivity().finish();
    }

}
