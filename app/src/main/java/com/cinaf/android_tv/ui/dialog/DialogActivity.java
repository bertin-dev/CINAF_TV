package com.cinaf.android_tv.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.leanback.app.GuidedStepFragment;

import com.cinaf.android_tv.translate.LocaleHelper;

import java.util.Locale;

public class DialogActivity extends Activity {

    String currentLanguage = (Locale.getDefault().getLanguage().contentEquals("fr")) ? "fr" : "en", currentLang;

    @Override
    protected void onStart() {
        super.onStart();
        //langue par défaut
        currentLanguage = getIntent().getStringExtra(currentLang);
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffa100")));

        if (savedInstanceState == null) {
            GuidedStepFragment fragment = new DialogFragment();
            GuidedStepFragment.addAsRoot(this, fragment, android.R.id.content);
        }
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