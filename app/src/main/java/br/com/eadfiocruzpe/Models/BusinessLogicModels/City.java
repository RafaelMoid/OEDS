package br.com.eadfiocruzpe.Models.BusinessLogicModels;

import com.google.gson.annotations.SerializedName;

public class City {

    @SerializedName("full_name")
    private String mFullName;

    @SerializedName("uf_id")
    private String mUfId;

    @SerializedName("city_id")
    private String mCityId;


    public String getFullName() {
        return mFullName;
    }

    public String getUfId() {
        return mUfId;
    }

    public String getCityId() {
        return mCityId;
    }

}