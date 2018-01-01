package com.mashazavolnyuk.client.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Group {


    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("items")
    @Expose
    private List<PlaceItem> placeItems = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PlaceItem> getPlaceItems() {
        return placeItems;
    }

    public void setPlaceItems(List<PlaceItem> placeItems) {
        this.placeItems = placeItems;
    }


}
