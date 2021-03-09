package com.cinaf.android_tv.ui.detail;

import android.annotation.SuppressLint;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.leanback.widget.Presenter;

import com.cinaf.android_tv.R;
import com.cinaf.android_tv.data.models.Actor;
import com.cinaf.android_tv.data.models.Genre;
import com.cinaf.android_tv.data.models.Poster;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailViewHolder extends Presenter.ViewHolder {

    private static final String TAG = "DetailViewHolder";

    @BindView(R.id.movie_title)
    TextView movieTitleTV;

    @BindView(R.id.movie_year)
    TextView movieYearTV;

    @BindView(R.id.overview)
    TextView movieOverview;

    @BindView(R.id.runtime)
    TextView mRuntimeTV;

    @BindView(R.id.tagline)
    TextView mTaglineTV;

    @BindView(R.id.director_tv)
    TextView mDirectorTv;

    @BindView(R.id.overview_label)
    TextView mOverviewLabelTV;

    @BindView(R.id.genres)
    LinearLayout mGenresLayout;

    @BindView(R.id.rating_bar)
    AppCompatRatingBar rating_bar;

    @BindView(R.id.rating)
    TextView rating;

    @BindView(R.id.imdb)
    TextView imdb;

    @BindView(R.id.classification)
    LinearLayout classification;


    private View itemView;

    public DetailViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
        itemView = view;
    }

    @SuppressLint("SetTextI18n")
    public void bindActor(Actor actor) {
        if (actor != null && actor.getName() != null) {
            mOverviewLabelTV.setText(R.string.biographie);
            mRuntimeTV.setText(actor.getType() + " - " + actor.getBorn());
            movieTitleTV.setText(actor.getName());
            movieYearTV.setText(actor.getHeight());
            movieOverview.setText(actor.getBio());
            mGenresLayout.removeAllViews();

            if (actor.getPaletteColors() != null) {
                movieTitleTV.setTextColor(actor.getPaletteColors().getTitleColor());
                mRuntimeTV.setTextColor(actor.getPaletteColors().getTextColor());
                movieYearTV.setTextColor(actor.getPaletteColors().getTextColor());
                movieOverview.setTextColor(actor.getPaletteColors().getTextColor());
            }


        }

    }


    @SuppressLint("SetTextI18n")
    public void bindPoster(Poster poster){

        if (poster != null && poster.getTitle() != null) {
            classification.setVisibility(View.VISIBLE);
            mRuntimeTV.setText(poster.getDuration());
            mTaglineTV.setText(poster.getClassification());
            movieTitleTV.setText(poster.getTitle().toUpperCase());
            movieYearTV.setText(poster.getYear());
            movieOverview.setText(poster.getDescription());
            mGenresLayout.removeAllViews();

            /*if (poster.getDirector() != null) {
                mDirectorTv.setText(String.format(Locale.getDefault(), "Directeur: %s", poster.getDirector()));
            }*/

            rating_bar.setRating(poster.getRating());
            rating.setText(poster.getRating() + "/5");
            imdb.setText(poster.getImdb() + "/10");

            int _16dp = (int) itemView.getResources().getDimension(R.dimen.full_padding);
            int _8dp = (int) itemView.getResources().getDimension(R.dimen.half_padding);
            float corner = itemView.getResources().getDimension(R.dimen.genre_corner);

            if (poster.getPaletteColors() != null) {
                Log.w(TAG, "bindPoster---------BONJOUR: " );
                movieTitleTV.setTextColor(poster.getPaletteColors().getTitleColor());
                mOverviewLabelTV.setTextColor(poster.getPaletteColors().getTitleColor());
                mTaglineTV.setTextColor(poster.getPaletteColors().getTextColor());
                mRuntimeTV.setTextColor(poster.getPaletteColors().getTextColor());
                movieYearTV.setTextColor(poster.getPaletteColors().getTextColor());
                movieOverview.setTextColor(poster.getPaletteColors().getTextColor());
                mDirectorTv.setTextColor(poster.getPaletteColors().getTextColor());
                rating.setTextColor(poster.getPaletteColors().getTextColor());
                imdb.setTextColor(poster.getPaletteColors().getTextColor());
                int primaryDarkColor = poster.getPaletteColors().getStatusBarColor();
                // Adds each genre to the genre layout
                for (Genre genre : poster.getGenres()) {
                    TextView textView = new TextView(itemView.getContext());
                    Log.w(TAG, "bindPoster---------1: " + genre.getTitle() );
                    textView.setText(genre.getTitle());
                    /*GradientDrawable shape = new GradientDrawable();
                    shape.setShape(GradientDrawable.RECTANGLE);
                    shape.setCornerRadius(corner);
                    shape.setColor(primaryDarkColor);
                    textView.setPadding(_8dp, _8dp, _8dp, _8dp);
                    textView.setBackground(shape);*/

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    params.setMargins(0, 0, _16dp, 0);
                    textView.setLayoutParams(params);

                    mGenresLayout.addView(textView);
                }

            }


        }

    }
}
