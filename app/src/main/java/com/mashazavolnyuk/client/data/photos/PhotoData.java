package com.mashazavolnyuk.client.data.photos;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PhotoData {

    @SerializedName("photos")
    @Expose
    private DetailedPhoto photos;

    public DetailedPhoto getPhotos() {
        return photos;
    }

    public void setPhotos(DetailedPhoto photos) {
        this.photos = photos;
    }
}
