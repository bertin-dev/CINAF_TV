package com.cinaf.android_tv.dagger.components;


import com.cinaf.android_tv.App;
import com.cinaf.android_tv.dagger.AppScope;
import com.cinaf.android_tv.dagger.modules.ApplicationModule;
import com.cinaf.android_tv.dagger.modules.HttpClientModule;
import com.cinaf.android_tv.ui.check_subscription.IDCode;
import com.cinaf.android_tv.ui.detail.DetailFragment;
import com.cinaf.android_tv.ui.detail.DetailPosterFragment;
import com.cinaf.android_tv.ui.dialog.DialogFragment;
import com.cinaf.android_tv.ui.main.MainFragment;
import com.cinaf.android_tv.ui.main.SplashActivity;
import com.cinaf.android_tv.ui.player.caster.PlaybackVideoFragment;
import com.cinaf.android_tv.ui.search.SearchFragment;

import javax.inject.Singleton;

import dagger.Component;

@AppScope
@Singleton
@Component(modules = {
        ApplicationModule.class,
        HttpClientModule.class,
})
public interface ApplicationComponent {

    void inject(App app);
    void inject(MainFragment mainFragment);
    void inject(DetailFragment movieDetailsFragment);
    void inject(DetailPosterFragment detailPosterFragment);
    void inject(SearchFragment searchFragment);
    void inject(MainFragment.Accueil homeMovie);
    void inject(MainFragment.Films films);
    void inject(MainFragment.Series series);
    void inject(DialogFragment dialogFragment);
    void inject(IDCode idCode);
    void inject(SplashActivity splashActivity);
    void inject(MainFragment.Actors actors);
}
