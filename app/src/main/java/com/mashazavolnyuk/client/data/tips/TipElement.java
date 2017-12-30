package com.mashazavolnyuk.client.data.tips;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TipElement {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("items")
    @Expose
    private List<DetailTip> items ;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<DetailTip> getItems() {
        return items;
    }

    public void setItems(List<DetailTip> items) {
        this.items = items;
    }
}
