package br.com.eadfiocruzpe.Models.DataResponses;

import com.google.gson.annotations.SerializedName;

public class BaseResponse {

    @SerializedName("Status")
    private int mStatus;
    @SerializedName("Msg")
    private String mMsg;

    public boolean successful() {
        return mStatus == 200;
    }

    public String getMsg() {
        return mMsg;
    }

}