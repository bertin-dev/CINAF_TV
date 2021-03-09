package com.cinaf.android_tv.ui.base;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;

import com.cinaf.android_tv.R;
import com.cinaf.android_tv.translate.LocaleHelper;

import java.util.Locale;


public class BaseTVActivity extends Activity {

    String currentLanguage = (Locale.getDefault().getLanguage().contentEquals("fr")) ? "fr" : "en", currentLang;

    @Override
    protected void onStart() {
        super.onStart();
        //langue par défaut
        currentLanguage = getIntent().getStringExtra(currentLang);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    public void addFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.tv_frame_content, fragment);
        fragmentTransaction.commit();
    }

    /**
     * attachBaseContext(Context newBase) methode callback permet de verifier la langue au demarrage
     * @param newBase
     * @since 2021
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }
}
