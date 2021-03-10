package com.cinaf.android_tv.dagger.modules;

import android.app.Application;
import android.util.Log;

import com.cinaf.android_tv.App;
import com.cinaf.android_tv.Config;
import com.cinaf.android_tv.dagger.AppScope;
import com.cinaf.android_tv.data.Api.TheMovieDbAPI;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

@Module
public class HttpClientModule {

    private static final long DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB
    private static final String HEADER_PRAGMA = "Pragma";
    private static final String HEADER_CACHE_CONTROL = "Cache-Control";


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
        return new OkHttpClient.Builder()
                .cache(cache(app))
                .readTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .build();
    }

    //This interceptor will be called both if the network is available and if the network is not available
    private Interceptor offlineInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Log.w("TAG", "offlineInterceptor: called" );
                Request request = chain.request();

                //prevent caching when network is on. For that we use the "networkInterceptor"
                if(!App.hasNetwork()){
                    CacheControl cacheControl = new CacheControl.Builder()
                            .maxStale(7, TimeUnit.DAYS)
                            .build();

                    request = request.newBuilder()
                            .removeHeader(HEADER_PRAGMA)
                            .removeHeader(HEADER_CACHE_CONTROL)
                            .cacheControl(cacheControl)
                            .build();
                }
                return chain.proceed(request);
            }
        };
    }

    //this interceptor will be called only if the network is availabe
    private Interceptor networkInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Log.w("TAG", "network intercept: called" );

                Response response = chain.proceed(chain.request());
                CacheControl cacheControl = new CacheControl.Builder()
                        .maxAge(5, TimeUnit.MINUTES)
                        .build();
                return response.newBuilder()
                        .removeHeader(HEADER_PRAGMA)
                        .removeHeader(HEADER_CACHE_CONTROL)
                        .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                        .build();
            }
        };
    }

    private Interceptor httpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.w("TAG", "log: http log" + message);
            }
        });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    private Cache cache(Application app) {
        return new Cache(new File(app.getCacheDir(), "http"), DISK_CACHE_SIZE);
    }

    @Provides
    @AppScope
    public Retrofit provideFithubRestAdapter(MoshiConverterFactory moshiConverterFactory, OkHttpClient okHttpClient) {
        okHttpClient = okHttpClient.newBuilder()
                .addInterceptor(httpLoggingInterceptor()) //used if network off OR on
                .addNetworkInterceptor(networkInterceptor()) //only used when network is on
                .addInterceptor(offlineInterceptor())
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
