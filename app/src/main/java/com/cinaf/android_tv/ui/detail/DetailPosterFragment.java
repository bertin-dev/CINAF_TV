package com.cinaf.android_tv.ui.detail;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityOptionsCompat;
import androidx.leanback.app.DetailsFragment;
import androidx.leanback.widget.Action;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ClassPresenterSelector;
import androidx.leanback.widget.DetailsOverviewLogoPresenter;
import androidx.leanback.widget.DetailsOverviewRow;
import androidx.leanback.widget.FullWidthDetailsOverviewSharedElementHelper;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;
import androidx.leanback.widget.SparseArrayObjectAdapter;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.cinaf.android_tv.App;
import com.cinaf.android_tv.R;
import com.cinaf.android_tv.data.Api.TheMovieDbAPI;
import com.cinaf.android_tv.data.models.Actor;
import com.cinaf.android_tv.data.models.Episode;
import com.cinaf.android_tv.data.models.PaletteColors;
import com.cinaf.android_tv.data.models.Poster;
import com.cinaf.android_tv.data.models.PosterDetails;
import com.cinaf.android_tv.data.models.Season;
import com.cinaf.android_tv.data.models.Source;
import com.cinaf.android_tv.ui.base.GlideBackgroundManager;
import com.cinaf.android_tv.ui.base.PaletteUtils;
import com.cinaf.android_tv.ui.dialog.DialogActivity;
import com.cinaf.android_tv.ui.main.MainActivity;
import com.cinaf.android_tv.ui.movie.ActorCardView;
import com.cinaf.android_tv.ui.movie.MovieCardView;
import com.cinaf.android_tv.ui.movie.MoviePresenter;
import com.cinaf.android_tv.ui.movie.SerieSaisonPresenter;
import com.cinaf.android_tv.ui.player.EmbedActivity;
import com.cinaf.android_tv.ui.player.Play;
import com.cinaf.android_tv.ui.player.PlayerActivity;
import com.cinaf.android_tv.ui.player.caster.PlaybackActivity;
import com.cinaf.android_tv.ui.player.caster.PlaybackSerieActivity;
import com.cinaf.android_tv.ui.player.caster.PlaybackTrailerActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class DetailPosterFragment extends DetailsFragment implements Palette.PaletteAsyncListener, OnItemViewClickedListener {

    private static final String TAG = "DetailPosterFragment";
    public static String TRANSITION_NAME = "poster_transition";
    @Inject
    TheMovieDbAPI theMovieDbAPI;

    ArrayObjectAdapter mAdapter;
    CustomDetailPresenter customDetailPresenter;
    DetailsOverviewRow detailsOverviewRow;
    ArrayObjectAdapter mCastAdapter = new ArrayObjectAdapter(new PersonPresenter_casting());
    ArrayObjectAdapter mRecommendationsAdapter = new ArrayObjectAdapter(new MoviePresenter());
    ArrayObjectAdapter mSeason = new ArrayObjectAdapter(new SerieSaisonPresenter());
    String mYoutubeID;
    Poster poster;
    String allSeason = "Saison";
    GlideBackgroundManager glideBackgroundManager;
    private SimpleTarget<GlideDrawable> mGlideDrawableSimpleTarget = new SimpleTarget<GlideDrawable>() {
        @Override
        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
            detailsOverviewRow.setImageDrawable(resource);
        }
    };

    public static DetailPosterFragment newInstance(Poster poster) {
        Bundle args = new Bundle();
        args.putParcelable(Poster.class.getSimpleName(), poster);
        DetailPosterFragment fragment = new DetailPosterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.instance().appComponent().inject(this);
        if (getArguments() == null || !getArguments().containsKey(Poster.class.getSimpleName())) {
            throw new RuntimeException("A poster is necessary for DetailPosterFragment");
        }
        poster = getArguments().getParcelable(Poster.class.getSimpleName());
        glideBackgroundManager = new GlideBackgroundManager(getActivity());
        glideBackgroundManager.setBackgroundColors(Color.parseColor("#FF263238"));
        setUpAdapter();
        setUpDetailsOverviewRow();
        setUpCastMembers();
        if (poster.getType().toLowerCase().trim().equalsIgnoreCase("serie")) {
            setUpSeason();
        }
        setupRecommendationsRow();
        setupEventListeners1();
    }

    private void setupEventListeners1() {
        setOnItemViewClickedListener(this);
    }

    public void playTrailer() {

        if (poster != null) {
            if (poster.getTrailer().getType() != null && poster.getTrailer().getType().equals("youtube")) {
                mYoutubeID = getTrailer(poster.getTrailer(), "official");
                Intent intent = new Intent(getActivity(), PlayerActivity.class);
                intent.putExtra("videoId", poster.getTrailer().getUrl());
                startActivity(intent);
                return;
            }
            if (poster.getTrailer().getType() != null && poster.getTrailer().getType().equals("embed")) {
                Intent intent = new Intent(getActivity(), EmbedActivity.class);
                intent.putExtra("url", poster.getTrailer().getUrl());
                startActivity(intent);
                return;
            } else {

                if (poster.getTrailer().getUrl() != null && poster.getTrailer().getUrl().contains("vimeo")) {
                    Log.w(TAG, "vimeo: PLAY" + poster.getTrailer().getUrl());
                    Intent intent = new Intent(getActivity(), EmbedActivity.class);
                    intent.putExtra("url", poster.getTrailer().getUrl());
                    startActivity(intent);
                } else {
                    Log.w(TAG, "playTrailer: PLAY");
                    Intent intent = new Intent(getActivity(), Play.class);
                    intent.putExtra("url", poster.getTrailer().getUrl());
                    intent.putExtra("type", poster.getTrailer().getType());
                    intent.putExtra("image", poster.getImage());
                    intent.putExtra("title", poster.getTitle());
                    intent.putExtra("subtitle", poster.getTitle() + " Trailer");
                    startActivity(intent);
                }
            }
        } else {
            Log.w(TAG, "playTrailer: Objet Poster Null");
        }
    }

    private void setUpAdapter() {

        customDetailPresenter = new CustomDetailPresenter(new DetailPosterDescriptionPresenter(),
                new DetailsOverviewLogoPresenter());


        FullWidthDetailsOverviewSharedElementHelper helper = new FullWidthDetailsOverviewSharedElementHelper();
        helper.setSharedElementEnterTransition(getActivity(), TRANSITION_NAME);
        customDetailPresenter.setListener(helper);
        customDetailPresenter.setParticipatingEntranceTransition(false);

        customDetailPresenter.setOnActionClickedListener(action -> {
            int actionId = (int) action.getId();
            switch (actionId) {
                //Bande Annonce
                case 0: {
                    //playTrailer();
                    Intent intent = new Intent(getActivity(), PlaybackTrailerActivity.class);
                    intent.putExtra(MainActivity.POSTER, poster);
                    startActivity(intent);
                }
                break;
                //REGARDER
                case 1: {

                    /*for(Source source : poster.getSources()){
                        if(source.getUrl() != null && source.getUrl().contains("vimeo")){
                            Log.w(TAG, "vimeo: PLAY" + source.getUrl());
                            Intent intent = new Intent(getActivity(), EmbedActivity.class);
                            intent.putExtra("url", source.getUrl());
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(getActivity(), Play.class);
                            intent.putExtra("url", source.getUrl());
                            intent.putExtra("type", source.getType());
                            intent.putExtra("image", poster.getImage());
                            intent.putExtra("title", poster.getTitle());
                            intent.putExtra("subtitle", poster.getTitle());
                            startActivity(intent);
                        }
                    }*/

                    Intent intent = new Intent(getActivity(), PlaybackActivity.class);
                    intent.putExtra(MainActivity.POSTER, poster);
                    startActivity(intent);
                }
                break;
                case 2: {
                    Intent intent = new Intent(getActivity().getBaseContext(), DialogActivity.class);
                    Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity())
                            .toBundle();
                    startActivity(intent, bundle);
                }
                break;
            }
        });


        ClassPresenterSelector classPresenterSelector = new ClassPresenterSelector();
        classPresenterSelector.addClassPresenter(DetailsOverviewRow.class, customDetailPresenter);
        classPresenterSelector.addClassPresenter(ListRow.class, new ListRowPresenter());
        mAdapter = new ArrayObjectAdapter(classPresenterSelector);
        setAdapter(mAdapter);
    }

    private void setUpDetailsOverviewRow() {

        if (poster != null) {
            detailsOverviewRow = new DetailsOverviewRow(new PosterDetails());
            mAdapter.add(detailsOverviewRow);

            loadImage(poster.getImage());
            detailsOverviewRow.setItem(this.poster);
            fetchVideos();
        }
    }

    private void fetchCastMembers() {
        theMovieDbAPI.getRolesByPoster(poster.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::bindCastMembers, e -> Timber.e(e, "Error fetching data: %s", e.getMessage()));
    }

    private void setUpCastMembers() {
        mAdapter.add(new ListRow(new HeaderItem(0, getString(R.string.casting)), mCastAdapter));
        fetchCastMembers();
    }

    private void setUpSeason() {
        mAdapter.add(new ListRow(new HeaderItem(1, allSeason), mSeason));
        fetchSeason();
    }

    private void fetchSeason() {
        theMovieDbAPI.getSeasonsBySerie(poster.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::bindSeason, e -> Timber.e(e, "Error fetching season: %s", e.getMessage()));
    }

    private void bindSeason(List<Season> seasons) {
        if (seasons.isEmpty()) {
            Toast.makeText(getActivity(), getString(R.string.eltIndisponible), Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < seasons.size(); i++) {
                allSeason = seasons.get(i).getTitle();
                mSeason.addAll(0, seasons.get(i).getEpisodes());
            }
            notifyDetailsChanged();
        }
    }

    private void setupRecommendationsRow() {
        mAdapter.add(new ListRow(new HeaderItem(2, getString(R.string.recommandePourVous)), mRecommendationsAdapter));
        fetchRecommendations();
    }

    private void bindCastMembers(List<Actor> actors) {
        mCastAdapter.addAll(0, actors);
        poster.setDirector("Bertin Mounok");
        notifyDetailsChanged();
    }

    private void fetchRecommendations() {

        String genres_list = "";
        for (int i = 0; i < poster.getGenres().size(); i++) {
            if (poster.getGenres().size() - 1 == i) {
                genres_list += poster.getGenres().get(i).getId();
            } else {
                genres_list += poster.getGenres().get(i).getId() + ",";
            }
        }

        theMovieDbAPI.getRandomMovies(genres_list)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::bindRecommendations, e -> Timber.e(e, "Error fetching recommendations: %s", e.getMessage()));
    }

    private void bindRecommendations(List<Poster> posters) {
        List<Poster> posterList = new ArrayList<>();
        posterList.clear();
        for (int i = 0; i < posters.size(); i++) {
            if (posters.get(i).getId() != poster.getId())
                posterList.add(posters.get(i));
        }
        mRecommendationsAdapter.addAll(0, posterList);
    }

    private String getTrailer(Source source, String keyword) {
        String id = null;

        if (source.getUrl().toLowerCase().contains(keyword)) {
            String[] tab = source.getUrl().split("=");
            id = tab[1];
            String[] tab2 = id.split("&");
            id = tab2[0];
        }
        return id;
    }

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
                        changePalette(((GlideBitmapDrawable) resource).getBitmap());
                        return false;
                    }
                })
                .into(mGlideDrawableSimpleTarget);
    }


    private void changePalette(Bitmap bmp) {
        Palette.from(bmp).generate(this);
    }

    @Override
    public void onGenerated(Palette palette) {
        PaletteColors colors = PaletteUtils.getPaletteColors(palette);
        /*customDetailPresenter.setActionsBackgroundColor(colors.getStatusBarColor());
        customDetailPresenter.setBackgroundColor(colors.getToolbarBackgroundColor());*/

        customDetailPresenter.setActionsBackgroundColor(Color.parseColor("#FF263238"));
        customDetailPresenter.setBackgroundColor(Color.parseColor("#FF263238"));

        if (poster != null) {
            this.poster.setPaletteColors(colors);
        }
        notifyDetailsChanged();
    }

    private void notifyDetailsChanged() {
        detailsOverviewRow.setItem(this.poster);
        int index = mAdapter.indexOf(detailsOverviewRow);
        mAdapter.notifyArrayItemRangeChanged(index, 1);
    }

    private void fetchVideos() {
        SparseArrayObjectAdapter adapter = new SparseArrayObjectAdapter();
        adapter.set(0, new Action(0, getString(R.string.bAnnonce), null, getResources().getDrawable(R.drawable.ic_movie)));
        if (poster.getType().toLowerCase().equalsIgnoreCase("movie")) {
            adapter.set(1, new Action(1, getString(R.string.regarder), null, getResources().getDrawable(R.drawable.play)));
        }
        adapter.set(2, new Action(2, getString(R.string.favoris), null, getResources().getDrawable(R.drawable.ic_check)));
        detailsOverviewRow.setActionsAdapter(adapter);
        notifyDetailsChanged();
    }


    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
        if (item instanceof Actor) {
            Actor actor = (Actor) item;
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            // Pass the langue to the activity
            intent.putExtra(Actor.class.getSimpleName(), actor);

            if (itemViewHolder.view instanceof ActorCardView) {
                // Pass the ImageView to allow a nice transition
                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        ((ActorCardView) itemViewHolder.view).getPosterIV(),
                        DetailFragment.TRANSITION_NAME).toBundle();
                getActivity().startActivity(intent, bundle);
            } else {
                startActivity(intent);
            }
        }
        if (item instanceof Poster) {
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
        if (item instanceof Episode) {
            Episode episode = (Episode) item;

            Intent intent = new Intent(getActivity(), PlaybackSerieActivity.class);
            intent.putExtra(MainActivity.EPISODE, episode);
            intent.putExtra(MainActivity.POSTER, poster);
            startActivity(intent);
        }
    }
}
