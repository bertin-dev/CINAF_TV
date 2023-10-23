package com.cinaf.android_tv.ui.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.leanback.app.BrowseFragment;
import androidx.leanback.app.RowsFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.FocusHighlight;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.PageRow;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.PresenterSelector;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;
import androidx.leanback.widget.VerticalGridPresenter;

import com.cinaf.android_tv.App;
import com.cinaf.android_tv.BuildConfig;
import com.cinaf.android_tv.R;
import com.cinaf.android_tv.data.Api.TheMovieDbAPI;
import com.cinaf.android_tv.data.models.Actor;
import com.cinaf.android_tv.data.models.Genre;
import com.cinaf.android_tv.data.models.Movie;
import com.cinaf.android_tv.data.models.Poster;
import com.cinaf.android_tv.provider.PrefManager;
import com.cinaf.android_tv.ui.base.GlideBackgroundManager;
import com.cinaf.android_tv.ui.base.IconHeaderItemPresenter;
import com.cinaf.android_tv.ui.detail.DetailActivity;
import com.cinaf.android_tv.ui.detail.DetailFragment;
import com.cinaf.android_tv.ui.detail.DetailPosterActivity;
import com.cinaf.android_tv.ui.detail.DetailPosterFragment;
import com.cinaf.android_tv.ui.movie.ActorCardView;
import com.cinaf.android_tv.ui.movie.ActorPresenter;
import com.cinaf.android_tv.ui.movie.MovieCardView;
import com.cinaf.android_tv.ui.movie.MoviePresenter;
import com.cinaf.android_tv.ui.player.caster.PosterListLoader;
import com.cinaf.android_tv.ui.publish_channel.ChannelPublishActivity;
import com.cinaf.android_tv.ui.search.SearchActivity;
import com.cinaf.android_tv.ui.setting.Card;
import com.cinaf.android_tv.ui.setting.CardRow;
import com.cinaf.android_tv.ui.setting.SettingsIconPresenter;
import com.cinaf.android_tv.ui.setting.utils.CardListRowSetting;
import com.cinaf.android_tv.ui.setting.utils.Utils;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;


/**
 * Sample {@link BrowseFragment} implementation showcasing the use of {@link PageRow} and
 * {@link ListRow}.
 */
public class MainFragment extends BrowseFragment implements LoaderManager.LoaderCallbacks<List<Poster>>{

    @Inject
    TheMovieDbAPI theMovieDbAPI;

    private static final long HEADER_ID_1 = 1;
    //private static final String HEADER_NAME_1 = "Accueil";
    private static final long HEADER_ID_2 = 2;
    //private static final String HEADER_NAME_2 = "Films";
    private static final long HEADER_ID_3 = 3;
    //private static final String HEADER_NAME_3 = "Séries";

    private static final long HEADER_ID_4 = 4;
    //private static final String HEADER_NAME_4 = "Acteurs";

    private static final long HEADER_ID_5 = 5;
    //private static final String HEADER_NAME_5 = "Paramètres";

    private static final long HEADER_ID_6 = 6;
    //private static final String HEADER_NAME_6 = "Deconnexion";

    private static final long HEADER_ID_7 = 7;
    //private static final String HEADER_NAME_7 = "Contactez nous";
    private static final long HEADER_ID_8 = 8;
    //private static final String HEADER_NAME_8 = "Politique de Confidentialité";
    public GlideBackgroundManager glideBackgroundManager;
    private ArrayObjectAdapter mRowsAdapter;
    private static final String TAG = "MainFragment";

    public static MainFragment newInstance() {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.instance().appComponent().inject(this);

        setupUIElements();
        prepareEntranceTransition();
        loadData();
        setupEventListeners();
        getLoaderManager().initLoader(0, null, this);
        //glideBackgroundManager.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.placeholder));
        glideBackgroundManager.setBackgroundColors(Color.parseColor("#FF263238"));
        //getMainFragmentRegistry().registerFragment(PageRow.class, new PageRowFragmentFactory());
        getMainFragmentRegistry().registerFragment(PageRow.class, new PageRowFragmentFactory(glideBackgroundManager));
    }

    private void setupUIElements() {
        glideBackgroundManager = new GlideBackgroundManager(getActivity());
        setBadgeDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.icon));
        // Badge, when set, takes precedent over title
        setTitle(getString(R.string.accueil));
        // set headers background color
        setBrandColor(ContextCompat.getColor(getActivity(), R.color.primary));
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);
        // search icon background color
        setSearchAffordanceColor(getResources().getColor(R.color.colorPrimary));

        setHeaderPresenterSelector(new PresenterSelector() {
            @Override
            public Presenter getPresenter(Object o) {
                return new IconHeaderItemPresenter();
            }
        });
    }

    private void setupEventListeners() {

        // search icon clicked
        setOnSearchClickedListener(v -> {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
        });
    }


    private void loadData() {
        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        setAdapter(mRowsAdapter);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                createOwnerRows();
                startEntranceTransition();
            }
        }, 2000);
    }


    private void createOwnerRows() {
        HeaderItem headerItem1 = new HeaderItem(HEADER_ID_1, getString(R.string.accueil));
        PageRow pageRow1 = new PageRow(headerItem1);
        mRowsAdapter.add(pageRow1);

        HeaderItem headerItem2 = new HeaderItem(HEADER_ID_2, getString(R.string.films));
        PageRow pageRow2 = new PageRow(headerItem2);
        mRowsAdapter.add(pageRow2);

        HeaderItem headerItem3 = new HeaderItem(HEADER_ID_3, getString(R.string.serie));
        PageRow pageRow3 = new PageRow(headerItem3);
        mRowsAdapter.add(pageRow3);

        HeaderItem headerItem4 = new HeaderItem(HEADER_ID_4, getString(R.string.acteurs));
        PageRow pageRow4 = new PageRow(headerItem4);
        mRowsAdapter.add(pageRow4);

        HeaderItem headerItem5 = new HeaderItem(HEADER_ID_5, getString(R.string.parametres));
        PageRow pageRow5 = new PageRow(headerItem5);
        mRowsAdapter.add(pageRow5);


        HeaderItem headerItem6 = new HeaderItem(HEADER_ID_6, getString(R.string.deconnexion));
        PageRow pageRow6 = new PageRow(headerItem6);
        mRowsAdapter.add(pageRow6);

        HeaderItem headerItem7 = new HeaderItem(HEADER_ID_7, getString(R.string.contactNous));
        PageRow pageRow7 = new PageRow(headerItem7);
        mRowsAdapter.add(pageRow7);

        HeaderItem headerItem8 = new HeaderItem(HEADER_ID_8, getString(R.string.policy));
        PageRow pageRow8 = new PageRow(headerItem8);
        mRowsAdapter.add(pageRow8);
    }


    @Override
    public Loader<List<Poster>> onCreateLoader(int id, Bundle args) {
        return new PosterListLoader(getActivity(), getString(R.string.url_principal));
    }

    @Override
    public void onLoadFinished(Loader<List<Poster>> loader, List<Poster> data) {

    }

    @Override
    public void onLoaderReset(Loader<List<Poster>> loader) {

    }


    public static class PageRowFragmentFactory extends FragmentFactory {
        private final GlideBackgroundManager mBackgroundManager;

        PageRowFragmentFactory(GlideBackgroundManager backgroundManager) {
            this.mBackgroundManager = backgroundManager;
        }

        @Override
        public Fragment createFragment(Object rowObj) {
            Row row = (Row)rowObj;
            //mBackgroundManager.setDrawable(null);
            if (row.getHeaderItem().getId() == HEADER_ID_1) {
                return new Accueil();
            } else if (row.getHeaderItem().getId() == HEADER_ID_2) {
                return new Films();
            } else if (row.getHeaderItem().getId() == HEADER_ID_3) {
                return new Series();
            }else if (row.getHeaderItem().getId() == HEADER_ID_4) {
                return new Actors();
            }else if (row.getHeaderItem().getId() == HEADER_ID_5) {
                return new setting();
            }else if (row.getHeaderItem().getId() == HEADER_ID_6) {
                return new Logout();
            }else if (row.getHeaderItem().getId() == HEADER_ID_7) {
                return new ContactUsWebView();
            }else if (row.getHeaderItem().getId() == HEADER_ID_8) {
                return new PolicyConfidentialityWebView();
            }

            throw new IllegalArgumentException(String.format("Invalid row %s", rowObj));
        }
    }

    /**
     * Page fragment embeds a rows fragment.
     */
    public static class Accueil extends RowsFragment implements OnItemViewSelectedListener, OnItemViewClickedListener {

        @Inject
        TheMovieDbAPI theMovieDbAPI;
        private static final String TAG = "Accueil";

        // rows - 0 - Accueil
        private static final int ACCUEIL = 0;

        // rows - 1 - top rated
        private static final int TOP_RATED = 1;

        // rows - 2 - popular
        private static final int POPULAR = 2;

        // rows - 3 - upcoming
        private static final int UPCOMING = 3;

        // rows - 4 - drame
        private static final int DRAME = 4;

        // rows - 5 - humour
        private static final int HUMOUR = 5;

        // rows - 6 - production cinaf
        private static final int PRODUCTION_CINAF = 6;

        // rows - 7 - action
        private static final int ACTION = 7;

        // rows - 8 - action
        private static final int ROMANCE = 8;

        SparseArray<MovieRow> movieRowSparseArray;

        ArrayObjectAdapter rowsAdapter;

        public Accueil() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            //Toast.makeText(getActivity(), "1", Toast.LENGTH_SHORT).show();
            App.instance().appComponent().inject(this);
            createDataRows();
            createRows();
            fetchActorsMovies();
            fetchTopRatedMovies();
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            fetchPopularMovies();
            fetchUpcomingMovies();
            //Toast.makeText(getActivity(), "4", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            //Toast.makeText(getActivity(), "2", Toast.LENGTH_SHORT).show();
            //prepareEntranceTransition();
            //fetchSlideMovies();
            fetchDrameMovies();
            fetchHumourMovies();
            fetchProductionCinafMovies();
            setupEventListeners1();
        }



        @Override
        public void onResume() {
            super.onResume();
            //Toast.makeText(getActivity(), "3", Toast.LENGTH_SHORT).show();
            fetchActionMovies();
            fetchRomanceMovies();
        }

        private void setupEventListeners1() {
            setOnItemViewSelectedListener(this);
            setOnItemViewClickedListener(this);
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getMainFragmentAdapter().getFragmentHost().notifyDataReady(getMainFragmentAdapter());
        }

        // Creates the data rows objects
        private void createDataRows() {

            movieRowSparseArray = new SparseArray<>();

            //The CardPresenter defines the UI of the items in the row
            ActorPresenter actorPresenter = new ActorPresenter();
            MoviePresenter moviePresenter = new MoviePresenter();

            //row - 0 - create objects
            movieRowSparseArray.put(ACCUEIL, new MovieRow()
                    .setId(ACCUEIL)
                    .setAdapter(new ArrayObjectAdapter(actorPresenter))
                    .setTitle(getString(R.string.acteurPopulaire))
                    .setPage(1)
            );

            //row - 1 - create objects
            movieRowSparseArray.put(TOP_RATED, new MovieRow()
                    .setId(TOP_RATED)
                    .setAdapter(new ArrayObjectAdapter(moviePresenter))
                    .setTitle(getString(R.string.mieuxNote))
                    .setPage(1)
            );

            //row - 2 - create objects
            movieRowSparseArray.put(POPULAR, new MovieRow()
                    .setId(POPULAR)
                    .setAdapter(new ArrayObjectAdapter(moviePresenter))
                    .setTitle(getString(R.string.plusPopulaire))
                    .setPage(1)
            );

            //row - 3 - create objects
            movieRowSparseArray.put(UPCOMING, new MovieRow()
                    .setId(UPCOMING)
                    .setAdapter(new ArrayObjectAdapter(moviePresenter))
                    .setTitle(getString(R.string.bientot))
                    .setPage(1)
            );


            //row - 4 - create objects
            movieRowSparseArray.put(DRAME, new MovieRow()
                    .setId(DRAME)
                    .setAdapter(new ArrayObjectAdapter(moviePresenter))
                    .setTitle(getString(R.string.drame))
                    .setPage(1)
            );


            //row - 5 - create objects
            movieRowSparseArray.put(HUMOUR, new MovieRow()
                    .setId(HUMOUR)
                    .setAdapter(new ArrayObjectAdapter(moviePresenter))
                    .setTitle(getString(R.string.humour))
                    .setPage(1)
            );


            //row - 6 - create objects
            movieRowSparseArray.put(PRODUCTION_CINAF, new MovieRow()
                    .setId(PRODUCTION_CINAF)
                    .setAdapter(new ArrayObjectAdapter(moviePresenter))
                    .setTitle(getString(R.string.productionCinaf))
                    .setPage(1)
            );

            //row - 7 - create objects
            movieRowSparseArray.put(ACTION, new MovieRow()
                    .setId(ACTION)
                    .setAdapter(new ArrayObjectAdapter(moviePresenter))
                    .setTitle(getString(R.string.action))
                    .setPage(1)
            );

            //row - 8 - create objects
            movieRowSparseArray.put(ROMANCE, new MovieRow()
                    .setId(ROMANCE)
                    .setAdapter(new ArrayObjectAdapter(moviePresenter))
                    .setTitle(getString(R.string.romance))
                    .setPage(1)
            );

        }

        // Creates the rows and sets up the adapter of the fragment
        private void createRows() {
            // Creates the RowsAdapter for the Fragment
            // The ListRowPresenter tells to render ListRow objects
            rowsAdapter = new ArrayObjectAdapter(new ShadowRowPresenterSelector());
            setAdapter(rowsAdapter);
        }

        private void fetchActorsMovies() {
            theMovieDbAPI.getNowPlayingMovies()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        bindActorsResponse(response, ACCUEIL);
                        //startEntranceTransition();
                    }, e -> {
                        Timber.e(e, "Error fetching now Actor movies: %s", e.getMessage());
                    });
        }

        private void fetchTopRatedMovies() {
            theMovieDbAPI.getNowPlayingMovies()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        bindTopRatedResponse(response, TOP_RATED);
                        //startEntranceTransition();
                    }, e -> Timber.e(e, "Error fetching Top Rated movies: %s", e.getMessage()));
        }

        private void fetchPopularMovies() {
            theMovieDbAPI.getNowPlayingMovies()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        bindPopularResponse(response, POPULAR);
                        //startEntranceTransition();
                    }, e -> {
                        Timber.e(e, "Error fetching popular movies: %s", e.getMessage());
                    });
        }


        private void fetchUpcomingMovies() {
            theMovieDbAPI.getNowPlayingMovies()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        bindUpComingResponse(response, UPCOMING);
                        //startEntranceTransition();
                    }, e -> Timber.e(e, "Error fetching upcoming movies: %s", e.getMessage()));
        }


        private void fetchDrameMovies() {
            theMovieDbAPI.getNowPlayingMovies()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        bindDrameResponse(response, DRAME);
                        //startEntranceTransition();
                    }, e -> Timber.e(e, "Error fetching drame movies: %s", e.getMessage()));
        }


        private void fetchHumourMovies() {
            theMovieDbAPI.getNowPlayingMovies()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        bindHumourResponse(response, HUMOUR);
                        //startEntranceTransition();
                    }, e -> Timber.e(e, "Error fetching Humour movies: %s", e.getMessage()));
        }


        private void fetchProductionCinafMovies() {
            theMovieDbAPI.getNowPlayingMovies()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        bindProductionCinafResponse(response, PRODUCTION_CINAF);
                        //startEntranceTransition();
                    }, e -> Timber.e(e, "Error fetching production cinaf movies: %s", e.getMessage()));
        }


        private void fetchActionMovies() {
            theMovieDbAPI.getNowPlayingMovies()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        bindActionResponse(response, ACTION);
                        //startEntranceTransition();
                    }, e -> Timber.e(e, "Error fetching action movies: %s", e.getMessage()));
        }

        private void fetchRomanceMovies(){
            theMovieDbAPI.getNowPlayingMovies()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        bindRomanceResponse(response, ROMANCE);
                        //startEntranceTransition();
                    }, e -> Timber.e(e, "Error fetching romance movies: %s", e.getMessage()));
        }


        private void bindActorsResponse(Movie response, int id) {
            Log.w("ACTOR", "bindActorsResponse: " + response.getActors() );
            MovieRow movieRow = movieRowSparseArray.get(id);
            movieRow.setPage(movieRow.getPage() + 1);
            HeaderItem headerItem = new HeaderItem(movieRow.getTitle());
            for(Actor actor : response.getActors()){
                if(actor.getImage() != null){
                    movieRow.getAdapter().add(actor);
                }
            }
            rowsAdapter.add(new CardListRow(headerItem, movieRow.getAdapter(),response));
        }

        private void bindTopRatedResponse(Movie response, int id) {
            Log.w("TOP RATED", "bindTopRatedResponse: " + response.getGenres().get(0).getTitle().toLowerCase() );
            MovieRow movieRow = movieRowSparseArray.get(id);
            movieRow.setPage(movieRow.getPage() + 1);
            HeaderItem headerItem = new HeaderItem(movieRow.getTitle());
            List<Genre> genres = response.getGenres();

            //films les mieux notés (Poster)
            for(Genre genre : genres){
                if(genre.getTitle().trim().toLowerCase().equalsIgnoreCase("top rated")){
                    for (Poster poster : genre.getPosters()){
                        if(poster.getImage() != null){
                            movieRow.getAdapter().add(poster);
                        }
                    }
                }
            }
            rowsAdapter.add(new CardListRow(headerItem, movieRow.getAdapter(),response));
        }


        private void bindUpComingResponse(Movie response, int id) {
            Log.w("UP COMING", "bindUpComingResponse: " + response.getGenres().get(0).getTitle().toLowerCase() );
            MovieRow movieRow = movieRowSparseArray.get(id);
            movieRow.setPage(movieRow.getPage() + 1);
            HeaderItem headerItem = new HeaderItem(movieRow.getTitle());
            List<Genre> genres = response.getGenres();

            //films les mieux notés (Poster)
            for(Genre genre : genres){

                if(genre.getTitle().trim().toLowerCase().equalsIgnoreCase("bientôt")){
                    for (Poster poster : genre.getPosters()){
                        if(poster.getImage() != null){
                            movieRow.getAdapter().add(poster);
                        }
                    }
                }
            }
            rowsAdapter.add(new CardListRow(headerItem, movieRow.getAdapter(),response));
        }


        private void bindPopularResponse(Movie response, int id) {
            Log.w("POPULAR", "bindPopularResponse: " + response.getGenres().get(0).getTitle().toLowerCase() );
            MovieRow movieRow = movieRowSparseArray.get(id);
            movieRow.setPage(movieRow.getPage() + 1);
            HeaderItem headerItem = new HeaderItem(movieRow.getTitle());
            List<Genre> genres = response.getGenres();

            //films populaires
            for(Genre genre : genres){

                if(genre.getTitle().trim().toLowerCase().equalsIgnoreCase("popular")){

                    for (Poster poster : genre.getPosters()){
                        if(poster.getImage() != null){
                            movieRow.getAdapter().add(poster);
                        }
                    }

                }
            }
            rowsAdapter.add(new CardListRow(headerItem, movieRow.getAdapter(),response));
        }


        private void bindDrameResponse(Movie response, int id) {
            Log.w("DRAME", "bindDrameResponse: " + response.getGenres().get(0).getTitle().toLowerCase() );
            MovieRow movieRow = movieRowSparseArray.get(id);
            movieRow.setPage(movieRow.getPage() + 1);
            HeaderItem headerItem = new HeaderItem(movieRow.getTitle());
            List<Genre> genres = response.getGenres();

            //films les mieux notés (Poster)
            for(Genre genre : genres){

                if(genre.getTitle().trim().toLowerCase().equalsIgnoreCase("drame")){
                    for (Poster poster : genre.getPosters()){
                        if(poster.getImage() != null){
                            movieRow.getAdapter().add(poster);
                        }
                    }
                }
            }
            rowsAdapter.add(new CardListRow(headerItem, movieRow.getAdapter(),response));
        }


        private void bindHumourResponse(Movie response, int id) {
            Log.w("HUMOUR", "bindHumourResponse: " + response.getGenres().get(0).getTitle().toLowerCase() );
            MovieRow movieRow = movieRowSparseArray.get(id);
            movieRow.setPage(movieRow.getPage() + 1);
            HeaderItem headerItem = new HeaderItem(movieRow.getTitle());
            List<Genre> genres = response.getGenres();

            //films les mieux notés (Poster)
            for(Genre genre : genres){

                if(genre.getTitle().trim().toLowerCase().equalsIgnoreCase("humour")){
                    for (Poster poster : genre.getPosters()){
                        if(poster.getImage() != null){
                            movieRow.getAdapter().add(poster);
                        }
                    }
                }
            }
            rowsAdapter.add(new CardListRow(headerItem, movieRow.getAdapter(),response));
        }


        private void bindProductionCinafResponse(Movie response, int id) {
            Log.w("PRODUCTION CINAF", "bindProductionCinafResponse: " + response.getGenres().get(0).getTitle().toLowerCase() );
            MovieRow movieRow = movieRowSparseArray.get(id);
            movieRow.setPage(movieRow.getPage() + 1);
            HeaderItem headerItem = new HeaderItem(movieRow.getTitle());
            List<Genre> genres = response.getGenres();

            //films les mieux notés (Poster)
            for(Genre genre : genres){

                if(genre.getTitle().trim().toLowerCase().equalsIgnoreCase("production cinaf")){
                    for (Poster poster : genre.getPosters()){
                        if(poster.getImage() != null){
                            movieRow.getAdapter().add(poster);
                        }
                    }
                }
            }
            rowsAdapter.add(new CardListRow(headerItem, movieRow.getAdapter(),response));
        }


        private void bindActionResponse(Movie response, int id) {
            Log.w("ACTION", "bindActionResponse: " + response.getGenres().get(0).getTitle().toLowerCase());
            MovieRow movieRow = movieRowSparseArray.get(id);
            movieRow.setPage(movieRow.getPage() + 1);
            HeaderItem headerItem = new HeaderItem(movieRow.getTitle());
            List<Genre> genres = response.getGenres();

            //films les mieux notés (Poster)
            for(Genre genre : genres){

                if(genre.getTitle().trim().toLowerCase().equalsIgnoreCase("action")){
                    for (Poster poster : genre.getPosters()){
                        if(poster.getImage() != null){
                            movieRow.getAdapter().add(poster);
                        }
                    }
                }
            }
            rowsAdapter.add(new CardListRow(headerItem, movieRow.getAdapter(),response));
        }


        private void bindRomanceResponse(Movie response, int id) {
            Log.w("ACTION", "bindRomancenResponse: " + response.getGenres().get(0).getTitle().toLowerCase());
            MovieRow movieRow = movieRowSparseArray.get(id);
            movieRow.setPage(movieRow.getPage() + 1);
            HeaderItem headerItem = new HeaderItem(movieRow.getTitle());
            List<Genre> genres = response.getGenres();

            //films les mieux notés (Poster)
            for(Genre genre : genres){

                if(genre.getTitle().trim().toLowerCase().equalsIgnoreCase("romance")){
                    for (Poster poster : genre.getPosters()){
                        if(poster.getImage() != null){
                            movieRow.getAdapter().add(poster);
                        }
                    }
                }
            }
            rowsAdapter.add(new CardListRow(headerItem, movieRow.getAdapter(),response));
        }



        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
            // Check if the item is a langue
            if (item instanceof Actor) {
                Actor actor = (Actor) item;
                // Check if the langue has a backdrop
                /*if(actor.getImage() != null) {
                    glideBackgroundManager.loadImage(actor.getImage());
                } else {
                    // If there is no backdrop for the langue we just use a default one
                    glideBackgroundManager.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.place_holder_channel));
                }*/
            }


            // Check if the item is a langue
            if (item instanceof Poster) {
                Poster poster = (Poster) item;
                // Check if the langue has a backdrop
                /*if(poster.getImage() != null) {
                    glideBackgroundManager.loadImage(poster.getImage());
                } else {
                    // If there is no backdrop for the langue we just use a default one
                    glideBackgroundManager.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.place_holder_channel));
                }*/
            }
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


    /**
     * Simple page Films implementation.
     */
    public static class Films extends GridFragment implements OnItemViewSelectedListener, OnItemViewClickedListener {

        @Inject
        TheMovieDbAPI theMovieDbAPI;

        private static final int COLUMNS = 6;
        private final int ZOOM_FACTOR = FocusHighlight.ZOOM_FACTOR_SMALL;
        private ArrayObjectAdapter mAdapter;
        private SparseArray<MovieRow> movieRowSparseArrayFilms;
        // rows - 0 - MOVIE
        private static final int MOVIE = 0;
        //private GlideBackgroundManager glideBackgroundManager;
        private static String TAG= "Films";

        public Films() {
        }

        /*public Films(GlideBackgroundManager glideBackgroundManager) {
            this.glideBackgroundManager = glideBackgroundManager;
        }*/

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            App.instance().appComponent().inject(this);

            setupAdapter();
            loadData(0, "created", 0);

            setupEventListeners2();
            getMainFragmentAdapter().getFragmentHost().notifyDataReady(getMainFragmentAdapter());
        }

        private void setupEventListeners2() {
            setOnItemViewSelectedListener(this);
            setOnItemViewClickedListener(this);
        }

        // Creates the data rows objects
        private void createDataRows() {


            movieRowSparseArrayFilms = new SparseArray<>();

            //The CardPresenter defines the UI of the items in the row
            MoviePresenter moviePresenter = new MoviePresenter();

            //row - 1 - create objects
            movieRowSparseArrayFilms.put(MOVIE, new MovieRow()
                    .setId(MOVIE)
                    .setAdapter(new ArrayObjectAdapter(moviePresenter))
                    .setTitle(getString(R.string.films))
                    .setPage(1)
            );
        }


        private void setupAdapter() {
            VerticalGridPresenter presenter = new VerticalGridPresenter(ZOOM_FACTOR);
            presenter.setNumberOfColumns(COLUMNS);
            setGridPresenter(presenter);

            createDataRows();

            MovieRow movieRow = movieRowSparseArrayFilms.get(MOVIE);

            mAdapter = movieRow.getAdapter();
            setAdapter(mAdapter);
        }

        private void loadData(int genre, String created, int page) {

            theMovieDbAPI.getMoviesByFiltres(genre,created, page)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        bindMovieResponse(response, MOVIE);
                        //startEntranceTransition();
                    }, e -> {
                        Timber.e(e, "Error fetching now playing movies: %s", e.getMessage());
                    });
        }


        private void bindMovieResponse(List<Poster> response, int id) {
            Log.w(TAG, "bindMovieResponse: "+ response );
            if(response.size()>0){
                MovieRow movieRow = movieRowSparseArrayFilms.get(id);
                movieRow.setPage(movieRow.getPage() + 1);

                for(Poster poster : response){
                    if(poster.getImage() != null){
                        movieRow.getAdapter().add(poster);
                    }
                }
            }
        }

        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
            // Check if the item is a langue
            if (item instanceof Actor) {
                Actor actor = (Actor) item;
                // Check if the langue has a backdrop
                /*if(actor.getImage() != null) {
                    glideBackgroundManager.loadImage(actor.getImage());
                } else {
                    // If there is no backdrop for the langue we just use a default one
                    glideBackgroundManager.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.place_holder_channel));
                }*/
            }


            // Check if the item is a langue
            if (item instanceof Poster) {
                Poster poster = (Poster) item;
                // Check if the langue has a backdrop
                /*if(poster.getImage() != null) {
                    glideBackgroundManager.loadImage(poster.getImage());
                } else {
                    // If there is no backdrop for the langue we just use a default one
                    glideBackgroundManager.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.place_holder_channel));
                }*/
            }
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

        @Override
        public void onResume() {
            super.onResume();
            loadData(0, "created", 1);
        }


    }

    /**
     * Simple page Serie implementation.
     */
    public static class Series extends GridFragment implements OnItemViewSelectedListener, OnItemViewClickedListener {

        @Inject
        TheMovieDbAPI theMovieDbAPI;

        private static final String TAG = "Series";
        private static final int COLUMNS = 6;
        private final int ZOOM_FACTOR = FocusHighlight.ZOOM_FACTOR_LARGE;
        private ArrayObjectAdapter mAdapter;
        private SparseArray<MovieRow> movieRowSparseArraySerie;
        // rows - 0 - MOVIE
        private static final int SERIE = 0;
        //private GlideBackgroundManager glideBackgroundManager;


        /*public Series(GlideBackgroundManager glideBackgroundManager) {
            this.glideBackgroundManager = glideBackgroundManager;
        }*/

        public Series() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            App.instance().appComponent().inject(this);
            setupAdapter();
            loadData(0,"created", 0);
            setupEventListeners3();
            getMainFragmentAdapter().getFragmentHost().notifyDataReady(getMainFragmentAdapter());
        }


        private void setupEventListeners3() {
            setOnItemViewSelectedListener(this);
            setOnItemViewClickedListener(this);
        }
        // Creates the data rows objects
        private void createDataRows() {

            movieRowSparseArraySerie = new SparseArray<>();

            //The CardPresenter defines the UI of the items in the row
            MoviePresenter moviePresenter = new MoviePresenter();

            //row - 1 - create objects
            movieRowSparseArraySerie.put(SERIE, new MovieRow()
                    .setId(SERIE)
                    .setAdapter(new ArrayObjectAdapter(moviePresenter))
                    .setTitle(getString(R.string.serie))
                    .setPage(1)
            );
        }


        private void setupAdapter() {
            VerticalGridPresenter presenter = new VerticalGridPresenter(ZOOM_FACTOR);
            presenter.setNumberOfColumns(COLUMNS);
            setGridPresenter(presenter);

            createDataRows();

            MovieRow movieRow = movieRowSparseArraySerie.get(SERIE);

            mAdapter = movieRow.getAdapter();
            setAdapter(mAdapter);
        }

        private void loadData(int genre, String created, int page) {

            theMovieDbAPI.getSeriesByFiltres(genre,created, page)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        bindSeriesResponse(response, SERIE);
                        //startEntranceTransition();
                    }, e -> {
                        Timber.e(e, "Error fetching now playing serie: %s", e.getMessage());
                    });
        }


        private void bindSeriesResponse(List<Poster> response, int id) {

            if(response.size()>0){
                MovieRow movieRow = movieRowSparseArraySerie.get(id);
                movieRow.setPage(movieRow.getPage() + 1);

                for(Poster poster : response){
                    if(poster.getImage() != null){
                        movieRow.getAdapter().add(poster);
                    }
                }
            }
        }


        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
            // Check if the item is a actor
            if (item instanceof Actor) {
                Actor actor = (Actor) item;
                // Check if the langue has a backdrop
                /*if(actor.getImage() != null) {
                    glideBackgroundManager.loadImage(actor.getImage());
                } else {
                    // If there is no backdrop for the langue we just use a default one
                    glideBackgroundManager.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.place_holder_channel));
                }*/
            }

            // Check if the item is a poster
            if (item instanceof Poster) {
                Poster poster = (Poster) item;
                // Check if the langue has a backdrop
                /*if(poster.getImage() != null) {
                    glideBackgroundManager.loadImage(poster.getImage());
                } else {
                    // If there is no backdrop for the langue we just use a default one
                    glideBackgroundManager.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.place_holder_channel));
                }*/
            }
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

        @Override
        public void onResume() {
            super.onResume();
            loadData(0,"created", 1);
        }
    }


    public static class Logout extends Fragment implements MainFragmentAdapterProvider{

        private static String TAG= "Logout";
        private MainFragmentAdapter mMainFragmentAdapter = new MainFragmentAdapter(this);

        public Logout() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Deconnexion();
        }


        @Override
        public MainFragmentAdapter getMainFragmentAdapter() {
            return mMainFragmentAdapter;
        }

        private void Deconnexion() {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setTitle(getString(R.string.confirmExit));
            alertDialogBuilder.setIcon(R.drawable.ic_exit_to_app_24);
            alertDialogBuilder.setMessage(getString(R.string.confirmExitMsg));
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton(getString(R.string.oui), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ProgressDialog dialog2 = ProgressDialog.show(getActivity(), getString(R.string.deconnexion), getString(R.string.deconnexion), true);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            PrefManager prf = new PrefManager(getActivity());
                            prf.remove("CODE_TV");
                            dialog.dismiss();
                            dialog2.dismiss();
                            getActivity().finish();
                        }
                    }, 2000); // 2000 milliseconds delay
                }
            });
            alertDialogBuilder.setNegativeButton(getString(R.string.non), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

    }


    public static class Actors extends GridFragment implements OnItemViewSelectedListener, OnItemViewClickedListener {

        @Inject
        TheMovieDbAPI theMovieDbAPI;

        private static final String TAG = "Actors";
        private static final int COLUMNS = 6;
        private final int ZOOM_FACTOR = FocusHighlight.ZOOM_FACTOR_SMALL;
        private ArrayObjectAdapter mAdapter;
        private SparseArray<MovieRow> movieRowSparseArraySerie;
        // rows - 0 - Actors
        private static final int ACTEUR = 0;

        public Actors() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            App.instance().appComponent().inject(this);
            setupAdapter();
            fetchActorsMovies();
            setupEventListeners3();
            getMainFragmentAdapter().getFragmentHost().notifyDataReady(getMainFragmentAdapter());
        }


        private void setupEventListeners3() {
            setOnItemViewSelectedListener(this);
            setOnItemViewClickedListener(this);
        }
        // Creates the data rows objects
        private void createDataRows() {

            movieRowSparseArraySerie = new SparseArray<>();

            //The CardPresenter defines the UI of the items in the row
            ActorPresenter actorPresenter = new ActorPresenter();

            //row - 1 - create objects
            movieRowSparseArraySerie.put(ACTEUR, new MovieRow()
                    .setId(ACTEUR)
                    .setAdapter(new ArrayObjectAdapter(actorPresenter))
                    .setTitle(getString(R.string.acteurs))
                    .setPage(1)
            );
        }


        private void setupAdapter() {
            VerticalGridPresenter presenter = new VerticalGridPresenter(ZOOM_FACTOR);
            presenter.setNumberOfColumns(COLUMNS);
            setGridPresenter(presenter);

            createDataRows();

            MovieRow movieRow = movieRowSparseArraySerie.get(ACTEUR);

            mAdapter = movieRow.getAdapter();
            setAdapter(mAdapter);
        }

        private void fetchActorsMovies() {

            theMovieDbAPI.getNowPlayingMovies()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        bindActorsResponse(response, ACTEUR);
                        //startEntranceTransition();
                    }, e -> {
                        Timber.e(e, "Error fetching now Actor movies: %s", e.getMessage());
                    });
        }

        private void bindActorsResponse(Movie response, int id) {
            MovieRow movieRow = movieRowSparseArraySerie.get(id);
            movieRow.setPage(movieRow.getPage() + 1);

            for(Actor actor : response.getActors()){
                if(actor.getImage() != null){
                    movieRow.getAdapter().add(actor);
                }
            }
        }


        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
            // Check if the item is a actor
            if (item instanceof Actor) {
                Actor actor = (Actor) item;
                // Check if the langue has a backdrop
                /*if(actor.getImage() != null) {
                    glideBackgroundManager.loadImage(actor.getImage());
                } else {
                    // If there is no backdrop for the langue we just use a default one
                    glideBackgroundManager.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.place_holder_channel));
                }*/
            }

            // Check if the item is a poster
            if (item instanceof Poster) {
                Poster poster = (Poster) item;
                // Check if the langue has a backdrop
                /*if(poster.getImage() != null) {
                    glideBackgroundManager.loadImage(poster.getImage());
                } else {
                    // If there is no backdrop for the langue we just use a default one
                    glideBackgroundManager.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.place_holder_channel));
                }*/
            }
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



    public static class ContactUsWebView extends Fragment implements MainFragmentAdapterProvider {
        private MainFragmentAdapter mMainFragmentAdapter = new MainFragmentAdapter(this);
        private WebView mWebview;

        @Override
        public MainFragmentAdapter getMainFragmentAdapter() {
            return mMainFragmentAdapter;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getMainFragmentAdapter().getFragmentHost().showTitleView(false);
        }

        @Override
        public View onCreateView(
                LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            FrameLayout root = new FrameLayout(getActivity());
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT);
            lp.setMarginStart(32);
            mWebview = new WebView(getActivity());
            mWebview.setWebViewClient(new WebViewClient());
            mWebview.getSettings().setJavaScriptEnabled(true);
            root.addView(mWebview, lp);
            return root;
        }

        @Override
        public void onResume() {
            super.onResume();
            mWebview.loadUrl("https://cinaf.fr/tv/contact.html");
            getMainFragmentAdapter().getFragmentHost().notifyDataReady(getMainFragmentAdapter());
        }
    }
    public static class PolicyConfidentialityWebView extends Fragment implements MainFragmentAdapterProvider {
        private MainFragmentAdapter mMainFragmentAdapter = new MainFragmentAdapter(this);
        private WebView mWebview;

        @Override
        public MainFragmentAdapter getMainFragmentAdapter() {
            return mMainFragmentAdapter;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getMainFragmentAdapter().getFragmentHost().showTitleView(false);
        }

        @Override
        public View onCreateView(
                LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            FrameLayout root = new FrameLayout(getActivity());
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT);
            lp.setMarginStart(32);
            mWebview = new WebView(getActivity());
            mWebview.setWebViewClient(new WebViewClient());
            mWebview.getSettings().setJavaScriptEnabled(true);
            root.addView(mWebview, lp);
            return root;
        }

        @Override
        public void onResume() {
            super.onResume();
            mWebview.loadUrl("https://cinaf.fr/tv/police.html");
            getMainFragmentAdapter().getFragmentHost().notifyDataReady(getMainFragmentAdapter());
        }
    }
    public static class setting extends RowsFragment implements OnItemViewClickedListener{

        private final ArrayObjectAdapter mRowsAdapter;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setupEventListeners4();
        }

        private void setupEventListeners4() {
            setOnItemViewClickedListener(this);
        }

        public setting() {
            ListRowPresenter selector = new ListRowPresenter();
            selector.setNumRows(1);
            mRowsAdapter = new ArrayObjectAdapter(selector);
            setAdapter(mRowsAdapter);
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadData();
                }
            }, 200);
        }

        private void loadData() {
            if (isAdded()) {
                String json = Utils.inputStreamToString(getResources().openRawResource(
                        R.raw.menu_setting));
                CardRow cardRow = new Gson().fromJson(json, CardRow.class);
                mRowsAdapter.add(createCardRow(cardRow));
                getMainFragmentAdapter().getFragmentHost().notifyDataReady(
                        getMainFragmentAdapter());
            }
        }

        private ListRow createCardRow(CardRow cardRow) {
            SettingsIconPresenter iconCardPresenter = new SettingsIconPresenter(getActivity());
            ArrayObjectAdapter adapter = new ArrayObjectAdapter(iconCardPresenter);
            for(Card card : cardRow.getCards()) {
                adapter.add(card);
            }

            HeaderItem headerItem = new HeaderItem(cardRow.getTitle());
            return new CardListRowSetting(headerItem, adapter, cardRow);
        }

        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
            if (item instanceof Card) {
                Card card = (Card) item;

                if(card.getTitle().equalsIgnoreCase("Langue")){



                    /**
                     * Add to home screen feature is not allowed in the system less than android O
                     * a toast will be popped up as notification
                     */
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                        Toast.makeText(getActivity(), "Add to home screen not supported", Toast.LENGTH_SHORT)
                                .show();
                        return;
                    }
                    Intent intent = new Intent(getActivity(), ChannelPublishActivity.class);
                    startActivity(intent);



                }else {
                    showSourcesPlayDialog();
                }
            }
        }

        @SuppressLint("SetTextI18n")
        private void showSourcesPlayDialog() {
            Dialog play_source_dialog = new Dialog(getActivity(),
                    R.style.Theme_Dialog);
            play_source_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            play_source_dialog.setCancelable(true);
            play_source_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            Window window = play_source_dialog.getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            getActivity().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
            wlp.gravity = Gravity.BOTTOM;
            window.setAttributes(wlp);
            play_source_dialog.setContentView(R.layout.dialog_sources);

            RelativeLayout relative_layout_dialog_source_close =  play_source_dialog.findViewById(R.id.relative_layout_dialog_source_close);

            TextView version = play_source_dialog.findViewById(R.id.version);
            version.setText("Android TV Version " + BuildConfig.VERSION_NAME);

            TextView copy = (TextView) play_source_dialog.findViewById(R.id.copy);
            copy.setText("© " + Calendar.getInstance().get(Calendar.YEAR) + " " + getString(R.string.sloganCinaf));

            relative_layout_dialog_source_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    play_source_dialog.dismiss();
                }
            });
            play_source_dialog.setOnKeyListener(new Dialog.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface arg0, int keyCode,
                                     KeyEvent event) {
                    // TODO Auto-generated method stub
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        play_source_dialog.dismiss();
                    }
                    return true;
                }
            });
            play_source_dialog.show();
        }


    }
}
