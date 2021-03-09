package com.cinaf.android_tv.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Movie implements Parcelable {

    @SerializedName("channels")
    @Expose
    private List<Channel> channels = null;


    @SerializedName("genres")
    @Expose
    private List<Genre> genres = null;

    @SerializedName("actors")
    @Expose
    private List<Actor> actors = null;

    @SerializedName("posters")
    @Expose
    private List<Poster> posters = null;


    /*@SerializedName("genre")
    @Expose
    private Genre genre;*/

    private int viewType = 1;



    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public List<Actor> getActors() {
        return actors;
    }

   /* public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Genre getGenre() {
        return genre;
    }*/

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public Movie setViewType(int viewType) {
        this.viewType = viewType;
        return this;
    }
    public int getViewType() {
        return viewType;
    }



    protected Movie(Parcel in) {
        channels = in.createTypedArrayList(Channel.CREATOR);
        genres = in.createTypedArrayList(Genre.CREATOR);
        actors = in.createTypedArrayList(Actor.CREATOR);
        viewType = in.readInt();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(channels);
        dest.writeTypedList(genres);
        dest.writeTypedList(actors);
        dest.writeInt(viewType);
    }

    public List<Poster> getPosters() {
        return posters;
    }

    public void setPosters(List<Poster> posters) {
        this.posters = posters;
    }
}
