/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cinaf.android_tv.ui.publish_channel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.leanback.app.GuidedStepFragment;
import androidx.leanback.widget.GuidanceStylist;
import androidx.leanback.widget.GuidedAction;

import com.cinaf.android_tv.R;
import com.cinaf.android_tv.ui.main.SplashActivity;

import java.util.List;
import java.util.Locale;

/**
 * This fragment is designed specific for user to choose which channel they
 * want to publish/ un-publish
 * <p>
 * It extends the GuidedStepFragment to share the similar UI as GuidedStepFragment
 */
public class PublishChannelFragment extends GuidedStepFragment {

    String currentLanguage = (Locale.getDefault().getLanguage().contentEquals("fr")) ? "fr" : "en", currentLang;

    /**
     * Tracking the action which is currently selected by the user
     */
    private ChannelContents mSelectedChannelContents;
    private static final String TAG = "PublishChannelFragment";
    private static final boolean DEBUG = true;

    /**
     * Bitmap which will be put at the front of each checkbox
     */
    private static final int OPTION_DRAWABLE = R.drawable.icon;


    /**
     * Helper function to add checked Action to this fragment
     * <p>
     * In this fragment, the checked action is customized as checkbox
     */
    private static void addCheckedAction(List<GuidedAction> actions,
                                         int iconResId,
                                         Context context,
                                         String title,
                                         String desc,
                                         int id) {
        GuidedAction guidedAction = new GuidedAction.Builder(context)
                .title(title)
                .description(desc)
                .checkSetId(GuidedAction.CHECKBOX_CHECK_SET_ID)
                .icon(context.getResources().getDrawable(iconResId))
                .build();
        guidedAction.setId(id);
        /**
         * Set checkbox status to false initially
         */

        if(id == 0){
            if(Locale.getDefault().getLanguage().contentEquals("fr")) {
                guidedAction.setChecked(true);
            } else {
                guidedAction.setChecked(false);
            }
        } else if(id == 1) {
            if(Locale.getDefault().getLanguage().contentEquals("en")) {
                guidedAction.setChecked(true);
            } else {
                guidedAction.setChecked(false);
            }
        }
        actions.add(guidedAction);
    }

    /**
     * Using different theme as attached activity for consistent UI design requirement
     * <p>
     * The theme can be customized in themes.xml
     */
    @Override
    public int onProvideTheme() {
        return R.style.Theme_Leanback_GuidedStep;
    }


    /**
     * The list of channel contents, obtained from ChannelContents class
     */
    private List<ChannelContents> mChannelContents;

    @Override
    @NonNull
    public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {

        currentLanguage = getActivity().getIntent().getStringExtra(currentLang);

        String title = getString(R.string.langue);
        String breadcrumb = getString(R.string.app_name) +"\n\n\n";
        String description = getString(R.string.sloganCinaf);
        Drawable icon = getActivity().getDrawable(R.drawable.android_tv);
        return new GuidanceStylist.Guidance(title, description, breadcrumb, icon);
    }

    @Override
    public GuidanceStylist onCreateGuidanceStylist() {
        return new GuidanceStylist() {
            @Override
            public int onProvideLayoutId() {
                return R.layout.setting_page;
            }
        };
    }

    @Override
    public void onCreateActions(@NonNull List<GuidedAction> actions, Bundle savedInstanceState) {
        ChannelContents.initializePlaylists(this.getActivity());

        mChannelContents = ChannelContents.sChannelContents;

        /**
         * The id of check box was set to the index of current channel
         */
        for (int i = 0; i < mChannelContents.size(); i++) {
            addCheckedAction(actions,
                    OPTION_DRAWABLE,
                    getActivity(),
                    mChannelContents.get(i).getName(),
                    getString(R.string.changeLanguage),
                    i);
        }
    }

    @Override
    public void onGuidedActionClicked(GuidedAction action) {
        /**
         * Find channel through action ID
         */
        int currentId = (int) action.getId();
        mSelectedChannelContents = mChannelContents.get(currentId);

        /**
         * When the action cannot select valid mediaItem, just return from from this function
         */
        if (mSelectedChannelContents == null) {
            return;
        }

        if(currentId == 0){
            if(action.isChecked()){
                setLocale("fr");
            }
        } else {
            if(action.isChecked()){
                setLocale("en");
            }
        }

    }


    private void setLocale(String localeName) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Locale.Helper.Selected.Language", localeName);
        editor.apply();

        if (!localeName.equals(currentLanguage)) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Locale locale = new Locale(localeName);
                Locale.setDefault(locale);

                Configuration configuration = this.getResources().getConfiguration();
                configuration.setLocale(locale);
                configuration.setLayoutDirection(locale);
                getActivity().createConfigurationContext(configuration);
            } else {

                Locale locale = new Locale(localeName);
                Locale.setDefault(locale);

                Resources resources = getResources();

                Configuration configuration = resources.getConfiguration();
                configuration.locale = locale;
                configuration.setLayoutDirection(locale);

                resources.updateConfiguration(configuration, resources.getDisplayMetrics());
            }

            Intent refresh = new Intent(getActivity(), SplashActivity.class);
            refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            refresh.putExtra(currentLang, localeName);
            startActivity(refresh);
            getActivity().finish();

        } else{
            Toast.makeText(getActivity(), getString(R.string.selectedLanguage), Toast.LENGTH_SHORT).show();
        }
    }

}

