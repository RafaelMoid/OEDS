package br.com.eadfiocruzpe.Models.BusinessLogicModels;

import com.google.gson.annotations.SerializedName;

public class UF {

    @SerializedName("full_name")
    private String mFullName;

    @SerializedName("short_name")
    private String mShortName;

    @SerializedName("uf_id")
    private String mUfId;


    public String getFullName() {
        return mFullName;
    }

    public String getShortName() {
        return mShortName;
    }

    public String getUfId() {
        return mUfId;
    }

}