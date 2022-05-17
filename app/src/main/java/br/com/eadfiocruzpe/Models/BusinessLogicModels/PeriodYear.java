package br.com.eadfiocruzpe.Models.BusinessLogicModels;

import com.google.gson.annotations.SerializedName;

public class PeriodYear {

    @SerializedName("id")
    private Integer mId;

    @SerializedName("name")
    private String mName;


    public Integer getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

}