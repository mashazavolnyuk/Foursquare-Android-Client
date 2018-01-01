package com.mashazavolnyuk.client.data.detailedVenue;

import android.graphics.Bitmap;

import com.mashazavolnyuk.client.data.IDataDetailVenue;
import com.mashazavolnyuk.client.data.Price;
import com.mashazavolnyuk.client.data.photos.PhotoItem;

import java.util.ArrayList;
import java.util.List;

public class DataInfoVenue implements IDataDetailVenue {

    private List<PhotoItem> photoItems;
    private List<String> listInfo;
    private String location;
    private Price price;
    private Double rating;
    private String color;
    private Bitmap map;

    public DataInfoVenue() {
        listInfo = new ArrayList<>();
        photoItems = new ArrayList<>();
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setPhotoItems(List<PhotoItem> photoItems) {
        this.photoItems = photoItems;
    }

    public List<PhotoItem> getGallery() {
        return photoItems;
    }

    public List<String> getListInfo() {
        return listInfo;
    }

    public void addInfo(String info) {
        listInfo.add(info);
    }

    public void setMap(Bitmap map) {
        this.map = map;
    }

    public Bitmap getMap() {
        return map;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

}
