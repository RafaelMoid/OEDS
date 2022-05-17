package br.com.eadfiocruzpe.Models.BusinessLogicModels;

import com.google.gson.annotations.SerializedName;

public class StateIndicator {

    @SerializedName("id")
    private String mId;

    @SerializedName("description")
    private String mDescription;

    @SerializedName("value")
    private double mValue;


    public String getId() {
        return mId;
    }

    public String getDescription() {
        return mDescription;
    }

    public double getValue() {
        return mValue;
    }

}