package com.cinaf.android_tv.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ActorDetails {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("role")
    @Expose
    private String role;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("born")
    @Expose
    private String born;

    @SerializedName("height")
    @Expose
    private String height;

    @SerializedName("bio")
    @Expose
    private String bio;


    private PaletteColors paletteColors;
    private String director;

    public ActorDetails() {
    }

    public ActorDetails(Integer id, String name, String type, String role, String image, String born, String height, String bio, PaletteColors paletteColors, String director) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.role = role;
        this.image = image;
        this.born = born;
        this.height = height;
        this.bio = bio;
        this.paletteColors = paletteColors;
        this.director = director;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBorn() {
        return born;
    }

    public void setBorn(String born) {
        this.born = born;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
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
