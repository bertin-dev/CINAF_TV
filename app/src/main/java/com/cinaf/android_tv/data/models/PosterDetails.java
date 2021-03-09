package com.cinaf.android_tv.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PosterDetails {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("label")
    @Expose
    private String label;

    @SerializedName("sublabel")
    @Expose
    private String sublabel;

    @SerializedName("imdb")
    @Expose
    private String imdb;

    @SerializedName("downloadas")
    @Expose
    private String downloadas;

    @SerializedName("comment")
    @Expose
    private Boolean comment;

    @SerializedName("playas")
    @Expose
    private String playas;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("classification")
    @Expose
    private String classification;

    @SerializedName("year")
    @Expose
    private String year;

    @SerializedName("duration")
    @Expose
    private String duration;

    @SerializedName("rating")
    @Expose
    private Float rating;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("cover")
    @Expose
    private String cover;

    @SerializedName("genres")
    @Expose
    private List<Genre> genres = new ArrayList<>();

    @SerializedName("sources")
    @Expose
    private List<Source> sources = new ArrayList<>();


    @SerializedName("trailer")
    @Expose
    private Source trailer ;

    private int typeView = 1;

    private PaletteColors paletteColors;
    private String director;

    public PosterDetails() {
    }


    public PosterDetails(Integer id, String title, String type, String label, String sublabel, String imdb, String downloadas, Boolean comment, String playas, String description, String classification, String year, String duration, Float rating, String image, String cover, List<Genre> genres, List<Source> sources, Source trailer, int typeView, PaletteColors paletteColors, String director) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.label = label;
        this.sublabel = sublabel;
        this.imdb = imdb;
        this.downloadas = downloadas;
        this.comment = comment;
        this.playas = playas;
        this.description = description;
        this.classification = classification;
        this.year = year;
        this.duration = duration;
        this.rating = rating;
        this.image = image;
        this.cover = cover;
        this.genres = genres;
        this.sources = sources;
        this.trailer = trailer;
        this.typeView = typeView;
        this.paletteColors = paletteColors;
        this.director = director;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSublabel() {
        return sublabel;
    }

    public void setSublabel(String sublabel) {
        this.sublabel = sublabel;
    }

    public String getImdb() {
        return imdb;
    }

    public void setImdb(String imdb) {
        this.imdb = imdb;
    }

    public String getDownloadas() {
        return downloadas;
    }

    public void setDownloadas(String downloadas) {
        this.downloadas = downloadas;
    }

    public Boolean getComment() {
        return comment;
    }

    public void setComment(Boolean comment) {
        this.comment = comment;
    }

    public String getPlayas() {
        return playas;
    }

    public void setPlayas(String playas) {
        this.playas = playas;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<Source> getSources() {
        return sources;
    }

    public void setSources(List<Source> sources) {
        this.sources = sources;
    }

    public Source getTrailer() {
        return trailer;
    }

    public void setTrailer(Source trailer) {
        this.trailer = trailer;
    }

    public int getTypeView() {
        return typeView;
    }

    public void setTypeView(int typeView) {
        this.typeView = typeView;
    }

    public PaletteColors getPaletteColors() {
        return paletteColors;
    }

    public void setPaletteColors(PaletteColors paletteColors) {
        this.paletteColors = paletteColors;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }
}
