package com.cinaf.android_tv.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.app.ActivityOptionsCompat;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.DetailsOverviewRow;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.ObjectAdapter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.cinaf.android_tv.App;
import com.cinaf.android_tv.R;
import com.cinaf.android_tv.data.Api.TheMovieDbAPI;
import com.cinaf.android_tv.data.models.Actor;
import com.cinaf.android_tv.data.models.Movie;
import com.cinaf.android_tv.data.models.Poster;
import com.cinaf.android_tv.ui.detail.DetailActivity;
import com.cinaf.android_tv.ui.detail.DetailFragment;
import com.cinaf.android_tv.ui.detail.DetailPosterActivity;
import com.cinaf.android_tv.ui.detail.DetailPosterFragment;
import com.cinaf.android_tv.ui.movie.ActorCardView;
import com.cinaf.android_tv.ui.movie.ActorPresenter;
import com.cinaf.android_tv.ui.movie.MovieCardView;
import com.cinaf.android_tv.ui.movie.MoviePresenter;

import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class SearchFragment extends androidx.leanback.app.SearchFragment
        implements androidx.leanback.app.SearchFragment.SearchResultProvider, OnItemViewClickedListener {


    @Inject
    TheMovieDbAPI theMovieDbAPI;

    ArrayObjectAdapter mAdapter;

    DetailsOverviewRow detailsOverviewRow;

    ArrayObjectAdapter arrayObjectAdapter = new ArrayObjectAdapter(new ActorPresenter());

    ArrayObjectAdapter arrayObjectAdapterPoster = new ArrayObjectAdapter(new MoviePresenter());

    List<Actor> actorList;
    Actor actor;

    Movie movie;
    Poster posterobj;



    public static SearchFragment newInstance() {
        Bundle args = new Bundle();
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        view.setBackgroundResource(R.color.default_color);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.instance().appComponent().inject(this);

        mAdapter = new ArrayObjectAdapter(new ListRowPresenter());

        setSearchResultProvider(this);

        setupSearchRow();
    }


    @Override
    public ObjectAdapter getResultsAdapter() {

        return mAdapter;
    }

    @Override
    public boolean onQueryTextChange(String query) {

        if(query.isEmpty()){
            return false;
        } else {

            //actor
            theMovieDbAPI.getActorsList(0, query)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::bindSearch, e -> {
                        loadImage(actor.getImage());
                        bindActorDetails(actorList);
                        performSearch();
                        Timber.e(e, "Error fetching search actor response: %query", e.getMessage());
                    });
            performSearch();

            //poster
            theMovieDbAPI.searchData(query)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::bindPosterSearch, e -> {
                        loadImage(posterobj.getImage());
                        bindPosterDetails(movie);
                        performPosterSearch();
                        Timber.e(e, "Error fetching search poster response: %query", e.getMessage());
                    });

            performPosterSearch();

            return true;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        if(query.isEmpty()){
            return false;
        } else {
            //actor
            theMovieDbAPI.getActorsList(0, query)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::bindSearch, e -> {
                        loadImage(actor.getImage());
                        bindActorDetails(actorList);
                        performSearch();
                        Timber.e(e, "Error fetching search actor response: %query", e.getMessage());
                    });

            performSearch();


            //poster
            theMovieDbAPI.searchData(query)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::bindPosterSearch, e -> {
                        loadImage(posterobj.getImage());
                        bindPosterDetails(movie);
                        performPosterSearch();
                        Timber.e(e, "Error fetching search poster response: %query", e.getMessage());
                    });

            performPosterSearch();

            return true;
        }
    }


    private void bindActorDetails(List<Actor> actorResponse) {

        for (Actor actor1: actorResponse){
            this.actor = actor1;
        }
        this.actorList = actorResponse;
    }

    private void bindPosterDetails(Movie movieResponse) {

        this.movie = movieResponse;
        List<Poster> posterList = movieResponse.getPosters();
        //films les mieux notés (Poster)
        for(Poster poster1 : posterList){
            this.posterobj = poster1;
        }
    }


    private void setupSearchRow() {
        mAdapter.add(new ListRow(new HeaderItem(0, "" + ""), arrayObjectAdapter));
        mAdapter.add(new ListRow(new HeaderItem(1, "" + ""), arrayObjectAdapterPoster));
        setOnItemViewClickedListener(this);

    }


    private void bindSearch(List<Actor> responseObj) {
        arrayObjectAdapter.addAll(0, responseObj);
    }

    private void bindPosterSearch(Movie responseObj) {
        arrayObjectAdapterPoster.addAll(0, responseObj.getPosters());
    }

    private SimpleTarget<GlideDrawable> mGlideDrawableSimpleTarget = new SimpleTarget<GlideDrawable>() {
        @Override
        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
            detailsOverviewRow.setImageDrawable(resource);
        }
    };

    private void loadImage(String url) {
        Glide.with(getActivity())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(mGlideDrawableSimpleTarget);
    }


    private void performSearch() {

        arrayObjectAdapter.clear();
    }

    private void performPosterSearch() {

        arrayObjectAdapterPoster.clear();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        setSearchQuery(data, true);
    }


    @Override
    public void onItemClicked(Presenter.ViewHolder viewHolder, Object item, RowPresenter.ViewHolder itemViewHolder, Row row) {

        if (item instanceof Actor) {
            Actor actor = (Actor) item;
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra(Actor.class.getSimpleName(), actor);

            if (itemViewHolder.view instanceof ActorCardView) {
                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        ((ActorCardView) itemViewHolder.view).getPosterIV(),
                        DetailFragment.TRANSITION_NAME).toBundle();
                getActivity().startActivity(intent, bundle);
            } else {
                startActivity(intent);
            }
        }

        if( item instanceof Poster){
            Poster poster = (Poster) item;
            Intent intent = new Intent(getActivity(), DetailPosterActivity.class);
            // Pass the langue to the activity
            intent.putExtra(Poster.class.getSimpleName(), poster);

            if (itemViewHolder.view instanceof MovieCardView) {
                // Pass the ImageView to allow a nice transition
                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        ((MovieCardView) itemViewHolder.view).getPosterIV(),
                        DetailPosterFragment.TRANSITION_NAME).toBundle();
                getActivity().startActivity(intent, bundle);
            } else {
                startActivity(intent);
            }
        }

    }

}
