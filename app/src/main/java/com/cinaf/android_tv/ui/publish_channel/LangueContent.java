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

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * LangueContent class
 * <p>
 * Parcelable interface has been implemented, so it can be passed between different components
 */
public class LangueContent implements Parcelable {

    /**
     * The description of this video
     */
    @SerializedName("description") private String mDescription;

    /**
     * The title of this video
     */
    @SerializedName("title") private String mTitle;


    /**
     * Getter and Setter for class's member
     *
     * Set access permission to public so other component outside of this package can access
     */


    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mDescription);
    }

    // Constructor to construct video content from parcel
    private LangueContent(Parcel in) {
        mTitle = in.readString();
        mDescription = in.readString();
    }

    /* package */ static final Creator CREATOR = new Creator() {
        public LangueContent createFromParcel(Parcel in) {
            return new LangueContent(in);
        }

        public LangueContent[] newArray(int size) {
            return new LangueContent[size];
        }
    };

    /**
     * For debugging purpose
     */
    @Override
    public String toString() {
        return "LangueContent{" +
                "mDescription='" + mDescription + '\'' +
                ", mTitle='" + mTitle + '\'' +
                '}';
    }
}
