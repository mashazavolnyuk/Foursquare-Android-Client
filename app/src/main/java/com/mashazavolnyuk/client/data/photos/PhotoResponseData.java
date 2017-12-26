package com.mashazavolnyuk.client.data.photos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mashazavolnyuk.client.data.Data;
import com.mashazavolnyuk.client.data.Meta;

public class PhotoResponseData {
    @SerializedName("meta")
    @Expose
    private Meta meta;
    @SerializedName("response")
    @Expose
    private Response response;

    public Response getResponseData() {
        return response;
    }
    public void setResponse(Response response) {
        this.response = response;
    }
}
