package com.cinaf.android_tv.dagger.modules;

import android.app.Application;

import com.cinaf.android_tv.Config;
import com.cinaf.android_tv.dagger.AppScope;
import com.cinaf.android_tv.data.Api.TheMovieDbAPI;

import java.io.File;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

@Module
public class HttpClientModule {

    private static final long DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB


    public static final String API_URL = "https://cinaf.fr/api/";
    public static final String ACCUEIL = "first/" + Config.API_KEY_URL + "/" + Config.ITEM_PURCHASE_CODE + "/";
    public static final String ACTOR_DETAILS = "movie/by/actor/";
    public static final String RANDOM_GENRE = "movie/random/";
    public static final String ROLE_BY_POSTER = "role/by/poster/";
    public static final String SEARCH_BY_ACTOR = "actor/all/";
    public static final String GLOBAL_SEARCH = "search/";
    public static final String MOVIES = "movie/by/filtres/";
    public static final String SERIES = "serie/by/filtres/";
    public static final String LOGIN = "user/login/";
    public static final String EDIT_TOKEN = "user/token/";
    public static final String REGISTER = "user/register/";
    public static final String SEASON = "season/by/serie/";
    public static final String IDCODE = "user/tvlogin/";



    @Provides
    @AppScope
    public OkHttpClient provideOkHttpClient(Application app) {
        File cacheDir = new File(app.getCacheDir(), "http");
        return new OkHttpClient.Builder()
                .readTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .cache(new okhttp3.Cache(cacheDir, DISK_CACHE_SIZE))
                .build();
    }

    @Provides
    @AppScope
    public Retrofit provideFithubRestAdapter(MoshiConverterFactory moshiConverterFactory, OkHttpClient okHttpClient) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient = okHttpClient.newBuilder()
                .addInterceptor(interceptor)
                .build();
        return new Retrofit.Builder()
                .baseUrl(API_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(moshiConverterFactory)
                .build();
    }

    @Provides
    public TheMovieDbAPI provideFithubApi(Retrofit restAdapter) {
        return restAdapter.create(TheMovieDbAPI.class);
    }

    @Provides
    @AppScope
    public MoshiConverterFactory provideMoshiConverterFactory() {
        return MoshiConverterFactory.create();
    }
}
