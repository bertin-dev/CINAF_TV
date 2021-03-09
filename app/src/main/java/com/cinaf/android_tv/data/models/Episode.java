package com.cinaf.android_tv.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Episode implements Parcelable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("downloadas")
    @Expose
    private String downloadas;


    @SerializedName("playas")
    @Expose
    private String playas;

    @SerializedName("duration")
    @Expose
    private String duration;

    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("sources")
    @Expose
    private List<Source> sources = null;

    //ajouter pour decompte
    private int count;

    public Episode() {
    }


    protected Episode(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        title = in.readString();
        description = in.readString();
        downloadas = in.readString();
        playas = in.readString();
        duration = in.readString();
        image = in.readString();
        sources = in.createTypedArrayList(Source.CREATOR);
        count = in.readInt();
    }

    public static final Creator<Episode> CREATOR = new Creator<Episode>() {
        @Override
        public Episode createFromParcel(Parcel in) {
            return new Episode(in);
        }

        @Override
        public Episode[] newArray(int size) {
            return new Episode[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(downloadas);
        dest.writeString(playas);
        dest.writeString(duration);
        dest.writeString(image);
        dest.writeTypedList(sources);
        dest.writeInt(count);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDownloadas() {
        return downloadas;
    }

    public void setDownloadas(String downloadas) {
        this.downloadas = downloadas;
    }

    public String getPlayas() {
        return playas;
    }

    public void setPlayas(String playas) {
        this.playas = playas;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Source> getSources() {
        return sources;
    }

    public void setSources(List<Source> sources) {
        this.sources = sources;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public static Creator<Episode> getCREATOR() {
        return CREATOR;
    }
}
