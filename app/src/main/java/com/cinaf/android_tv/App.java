package com.cinaf.android_tv;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.ProcessLifecycleOwner;

import com.cinaf.android_tv.dagger.components.ApplicationComponent;
import com.cinaf.android_tv.dagger.components.DaggerApplicationComponent;
import com.cinaf.android_tv.dagger.modules.ApplicationModule;
import com.cinaf.android_tv.dagger.modules.HttpClientModule;
import com.google.android.gms.cast.tv.CastReceiverContext;
import com.google.android.gms.cast.tv.SenderDisconnectedEventInfo;
import com.google.android.gms.cast.tv.SenderInfo;

import timber.log.Timber;

public class App extends Application {

    private static App instance;
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        // Creates Dagger Graph
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .httpClientModule(new HttpClientModule())
                .build();

        applicationComponent.inject(this);


        //CASTER
        CastReceiverContext.initInstance(this);
        CastReceiverContext.getInstance().registerEventCallback(new EventCallback());
        ProcessLifecycleOwner.get().getLifecycle().addObserver(new AppLifecycleObserver());
    }

    public static App instance() {
        return instance;
    }

    public ApplicationComponent appComponent() {
        return applicationComponent;
    }

    private class EventCallback extends CastReceiverContext.EventCallback {
        @Override
        public void onSenderConnected(SenderInfo senderInfo) {
            Toast.makeText(
                    App.this,
                    "Sender connected " + senderInfo.getSenderId(),
                    Toast.LENGTH_LONG)
                    .show();
        }

        @Override
        public void onSenderDisconnected(SenderDisconnectedEventInfo eventInfo) {
            Toast.makeText(
                    App.this,
                    "Sender disconnected " + eventInfo.getSenderInfo().getSenderId(),
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

}
