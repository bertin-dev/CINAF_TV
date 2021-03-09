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

import com.cinaf.android_tv.R;
import com.cinaf.android_tv.ui.setting.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is mainly used for the channel which has been added to the main-screen
 */
public final class ChannelContents {

    /* package */ static List<ChannelContents> sChannelContents;
    /**
     * The channel name shown in the main screen after adding it
     */
    @SerializedName("category")
    private String mName;

    /* package */
    static void initializePlaylists(Context context) {
        if (sChannelContents == null) {

            sChannelContents = new ArrayList<>();
            String json = Utils.inputStreamToString(
                    context.getResources().openRawResource(R.raw.langue));

            /**
             * Populate playlist from json file
             */
            ChannelContents[] channels = new Gson().fromJson(json, ChannelContents[].class);
            for (int i = 0; i < channels.length; i++) {
                sChannelContents.add(channels[i]);
            }
        }
    }

    /* package */ String getName() {
        return mName;
    }



}


