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

import com.cinaf.android_tv.data.models.Genre;
import com.cinaf.android_tv.data.models.Poster;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public final class PosterList {
    private static final String TAG = "PosterList";

    private static final String TAG_TITLE = "title";
    private static final String TAG_LABEL = "label";
    private static final String TAG_SUBLABEL = "sublabel";
    private static final String TAG_TYPE = "type";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_YEAR = "year";
    private static final String TAG_IMDB = "imdb";
    private static final String TAG_RATING = "rating";
    private static final String TAG_COMMENT = "false";
    private static final String TAG_DURATION = "duration";
    private static final String TAG_DOWNLOADAS = "downloadas";
    private static final String TAG_PLAYAS = "playas";
    private static final String TAG_CLASSIFICATION = "classification";
    private static final String TAG_IMAGE = "image";
    private static final String TAG_COVER = "cover";
    //genre
    private static final String TAG_GENRE_TITLE = "title";
    //trailer
    private static final String TAG_TRAILER_TYPE = "type";
    private static final String TAG_TRAILER_URL = "url";
    //sources
    private static final String TAG_SOURCES_TITLE = "title";
    private static final String TAG_SOURCES_QUALITY = "quality";
    private static final String TAG_SOURCES_SIZE = "size";
    private static final String TAG_SOURCES_KIND = "kind";
    private static final String TAG_SOURCES_PREMIUM = "premium";
    private static final String TAG_SOURCES_EXTERNAL = "false";
    private static final String TAG_SOURCES_TYPE = "type";
    private static final String TAG_SOURCES_URL = "url";


    private static final String GENRES = "genres";
    private static final String POSTERS = "posters";
    private static final String TRAILER = "trailer";
    private static final String SOURCES = "sources";


    private static List<Poster> list;
    private static List<Source> sourceList;
    private static Source trailer;
    private static int count = 0;


    public static List<Poster> getList() {
        return list;
    }

    public static List<Poster> setupMovies(String url) throws JSONException {

        Log.w("TAG", "setupMovies: " + url);
        if (null != list) {
            return list;
        }
        list = new ArrayList<>();
        sourceList = new ArrayList<>();
        Map<String, String> urlPrefixMap = new HashMap<>();
        JSONObject jsonObj = new PosterList().parseUrl(url);
        //Log.w("TAG", "setupMovies: " + jsonObj );

        JSONArray genres = jsonObj.getJSONArray(GENRES);
        if (null != genres) {
            for (int i = 0; i < genres.length(); i++) {
                JSONObject genresJSONObject = genres.getJSONObject(i);
                urlPrefixMap.put(TAG_GENRE_TITLE, genresJSONObject.getString(TAG_GENRE_TITLE));

                //posters
                JSONArray postersList = genresJSONObject.getJSONArray(POSTERS);
                if (null != postersList) {
                    for (int j = 0; j < postersList.length(); j++) {

                        JSONObject postersListJSONObject = postersList.getJSONObject(j);
                        //posters list
                        String title = postersListJSONObject.getString(TAG_TITLE);
                        Log.w("TAG", "setupMovies: " + title);
                        String label = postersListJSONObject.getString(TAG_LABEL);
                        String sublabel = postersListJSONObject.getString(TAG_SUBLABEL);
                        String type = postersListJSONObject.getString(TAG_TYPE);
                        String description = postersListJSONObject.getString(TAG_DESCRIPTION);
                        String year = postersListJSONObject.getString(TAG_YEAR);
                        String imdb = postersListJSONObject.getString(TAG_IMDB);
                        String rating = postersListJSONObject.getString(TAG_RATING);
                        Log.w("TAG", "setupMovies: " + rating);
                        boolean comment = false;
                        String duration = postersListJSONObject.getString(TAG_DURATION);
                        Log.w("TAG", "setupMovies: " + duration);
                        String downloadas = postersListJSONObject.getString(TAG_DOWNLOADAS);
                        String playas = postersListJSONObject.getString(TAG_PLAYAS);
                        String classification = postersListJSONObject.getString(TAG_CLASSIFICATION);
                        String image = postersListJSONObject.getString(TAG_IMAGE);
                        String cover = postersListJSONObject.getString(TAG_COVER);
                        Log.w("TAG", "setupMovies: " + cover);

                        //second genre
                        /*JSONArray genres2ListJSONObject = postersListJSONObject.getJSONArray(GENRES);
                        if(null != genres2ListJSONObject){
                            for(int k=0; k<genres2ListJSONObject.length();k++){
                                JSONObject genres2 = genres2ListJSONObject.getJSONObject(k);
                                String genre2 = genres2.getString(TAG_GENRE_TITLE);
                            }
                        }*/

                        //trailer
                        JSONObject trailerJsonObject = postersListJSONObject.getJSONObject(TRAILER);
                        String type_trailer = trailerJsonObject.getString(TAG_TRAILER_TYPE);
                        String url_trailer = trailerJsonObject.getString(TAG_TRAILER_URL);

                        Log.w("TAG", "NDEMBA: " + type_trailer );
                        Log.w("TAG", "NDEMBA: " + url_trailer );

                        //Source
                        JSONArray sourcesListJSONObject = postersListJSONObject.getJSONArray(SOURCES);
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

                                //CONVERT JSONOBJECT TO OBJECT SOURCE
                                trailer = new Gson().fromJson(trailerJsonObject.toString(), new TypeToken<Source>(){}.getType());


                                Log.w("TAG", "NDEMBA0000----------: " + url_sources);
                                list.add(buildMovieInfo(
                                        title, label, sublabel, type, description, year, imdb, rating, comment, duration, downloadas, playas, classification, image, cover,
                                        sourceList, trailer));

                            }
                        }
                    }
                }


            }
        }
        return list;
    }

    private static Poster buildMovieInfo(
            String title,
            String label,
            String sublabel,
            String type,
            String description,
            String year,
            String imdb,
            String rating,
            boolean comment,
            String duration,
            String downloadas,
            String playas,
            String classification,
            String image,
            String cover,
            List<Source> sources,
            Source trailer) {
        Poster poster = new Poster();
        poster.setCount(count++);
        poster.setTitle(title);
        poster.setLabel(label);
        poster.setSublabel(sublabel);
        poster.setType(type);
        poster.setDescription(description);
        poster.setYear(year);
        poster.setImdb(imdb);
        poster.setRating(Float.parseFloat(rating));
        poster.setComment(comment);
        poster.setDuration(duration);
        poster.setDownloadas(downloadas);
        poster.setPlayas(playas);
        poster.setClassification(classification);
        poster.setImage(image);
        poster.setCover(cover);
        poster.setSources(sources);
        poster.setTrailer(trailer);

        return poster;
    }

    protected JSONObject parseUrl(String urlString) {
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
            return new JSONObject(json);
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