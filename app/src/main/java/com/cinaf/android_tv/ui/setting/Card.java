/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.cinaf.android_tv.ui.setting;
import com.google.gson.annotations.SerializedName;

/**
 * This is a generic example of a custom data object, containing info we might want to keep with
 * each card on the home screen
 */
public class Card {

    @SerializedName("title") private String mTitle = "";
    @SerializedName("description") private String mDescription = "";
    @SerializedName("id") private int mId;
    @SerializedName("width") private int mWidth;
    @SerializedName("height") private int mHeight;
    @SerializedName("localImageResource") private String mLocalImageResource = null;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setId(int id) {
        mId = id;
    }

    public void setWidth(int width) {
        mWidth = width;
    }

    public void setHeight(int height) {
        mHeight = height;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public int getId() {
        return mId;
    }


    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }


    public String getLocalImageResource() {
        return mLocalImageResource;
    }

    public void setLocalImageResource(String localImageResource) {
        mLocalImageResource = localImageResource;
    }

    public String getLocalImageResourceName() {
        return mLocalImageResource;
    }

}
