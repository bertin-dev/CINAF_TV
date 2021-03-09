/**
 * Copyright 2020 Google LLC. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cinaf.android_tv.ui.player.caster;

import android.util.Log;

import com.cinaf.android_tv.data.models.Episode;
import com.cinaf.android_tv.data.models.Source;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


public final class SerieList {
    private static final String TAG = "SerieList";

    private static final String TAG_TITLE = "title";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_DURATION = "duration";
    private static final String TAG_DOWNLOADAS = "downloadas";
    private static final String TAG_PLAYAS = "playas";
    private static final String TAG_IMAGE = "image";
    //sources
    private static final String TAG_SOURCES_TITLE = "title";
    private static final String TAG_SOURCES_QUALITY = "quality";
    private static final String TAG_SOURCES_SIZE = "size";
    private static final String TAG_SOURCES_KIND = "kind";
    private static final String TAG_SOURCES_PREMIUM = "premium";
    private static final String TAG_SOURCES_EXTERNAL = "false";
    private static final String TAG_SOURCES_TYPE = "type";
    private static final String TAG_SOURCES_URL = "url";


    private static final String EPISODE = "episodes";
    private static final String SOURCES = "sources";


    private static List<Episode> list;
    private static List<Source> sourceList;
    private static int count = 0;


    public static List<Episode> getList() {
        return list;
    }

    public static List<Episode> setupSerie(String url) throws JSONException {

        Log.w("TAG", "setupSerie: " + url);
        if (null != list) {
            return list;
        }
        list = new ArrayList<>();
        sourceList = new ArrayList<>();
        JSONArray jsonArray = new SerieList().parseUrl(url);
        Log.w("TAG", "setupSerie: " + jsonArray);

        //JSONArray genres = jsonObj.getJSONArray(GENRES);
        if (null != jsonArray) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String episodes = jsonObject.getString(TAG_TITLE);
                //Episode
                JSONArray jsonArrayEpisode = jsonObject.getJSONArray(EPISODE);
                if (null != jsonArrayEpisode) {
                    for (int j = 0; j < jsonArrayEpisode.length(); j++) {

                        JSONObject episodeJSONObject = jsonArrayEpisode.getJSONObject(j);
                        //episode list
                        String title = episodeJSONObject.getString(TAG_TITLE);
                        Log.w("TAG", "setupSerie: " + title);
                        String description = episodeJSONObject.getString(TAG_DESCRIPTION);
                        String duration = episodeJSONObject.getString(TAG_DURATION);
                        Log.w("TAG", "setupSerie: " + duration);
                        String downloadas = episodeJSONObject.getString(TAG_DOWNLOADAS);
                        String playas = episodeJSONObject.getString(TAG_PLAYAS);
                        String image = episodeJSONObject.getString(TAG_IMAGE);
                        Log.w("TAG", "setupSerie: " + image);

                        //Source
                        JSONArray sourcesListJSONObject = episodeJSONObject.getJSONArray(SOURCES);
                        if (null != sourcesListJSONObject) {
                            for (int l = 0; l < sourcesListJSONObject.length(); l++) {
                                JSONObject sourcesListJSONObjectJSONObject = sourcesListJSONObject.getJSONObject(l);
                                String title_sources = sourcesListJSONObjectJSONObject.getString(TAG_SOURCES_TITLE);
                                String quality = sourcesListJSONObjectJSONObject.getString(TAG_SOURCES_QUALITY);
                                String size = sourcesListJSONObjectJSONObject.getString(TAG_SOURCES_SIZE);
                                String kind = sourcesListJSONObjectJSONObject.getString(TAG_SOURCES_KIND);
                                String premium = sourcesListJSONObjectJSONObject.getString(TAG_SOURCES_PREMIUM);
                                boolean source_external = false;
                                String type_sources = sourcesListJSONObjectJSONObject.getString(TAG_SOURCES_TYPE);
                                String url_sources = sourcesListJSONObjectJSONObject.getString(TAG_SOURCES_URL);

                                //CONVERT JSONARRAY TO LIST
                               sourceList = new Gson().fromJson(sourcesListJSONObject.toString(), new TypeToken<List<Source>>(){}.getType());


                                Log.w("TAG", "NDEMBA0000----------: " + url_sources);
                                list.add(buildMovieInfo(
                                        title, description, duration, downloadas, playas, image, sourceList));

                            }
                        }
                    }
                }


            }
        }

        return list;
    }

    private static Episode buildMovieInfo(
            String title,
            String description,
            String duration,
            String downloadas,
            String playas,
            String image,
            List<Source> sources ) {
        Episode episode = new Episode();
        episode.setCount(count++);
        episode.setTitle(title);
        episode.setDescription(description);
        episode.setDuration(duration);
        episode.setDownloadas(downloadas);
        episode.setPlayas(playas);
        episode.setImage(image);
        episode.setSources(sources);
        return episode;
    }

    protected JSONArray parseUrl(String urlString) {
        InputStream is = null;
        try {
            java.net.URL url = new java.net.URL(urlString);
            URLConnection urlConnection = url.openConnection();
            is = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream(), "iso-8859-1"), 1024);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String json = sb.toString();
            return new JSONArray(json);
        } catch (Exception e) {
            Log.d(TAG, "Failed to parse the json for media list", e);
            return null;
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }
}