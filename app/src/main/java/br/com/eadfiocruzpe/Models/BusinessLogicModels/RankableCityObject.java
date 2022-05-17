package br.com.eadfiocruzpe.Models.BusinessLogicModels;

import com.google.gson.annotations.SerializedName;

public class RankableCityObject {

    @SerializedName("ufCode")
    private String mUfCode;

    @SerializedName("cityCode")
    private String mCityCode;

    @SerializedName("uf")
    private UF mUf;

    @SerializedName("city")
    private City mCity;

    @SerializedName("rank_index_2_1")
    private int mPosRankIndex_2_1;

    @SerializedName("rank_index_3_2")
    private int mPosRankIndex_3_2;

    @SerializedName("index_1_1")
    private double mIndicator_1_1;

    @SerializedName("index_1_2")
    private double mIndicator_1_2;

    @SerializedName("index_1_3")
    private double mIndicator_1_3;

    @SerializedName("index_1_4")
    private double mIndicator_1_4;

    @SerializedName("index_1_5")
    private double mIndicator_1_5;

    @SerializedName("index_1_6")
    private double mIndicator_1_6;

    @SerializedName("index_2_1")
    private double mIndicator_2_1;

    @SerializedName("index_2_2")
    private double mIndicator_2_2;

    @SerializedName("index_2_3")
    private double mIndicator_2_3;

    @SerializedName("index_2_4")
    private double mIndicator_2_4;

    @SerializedName("index_2_5")
    private double mIndicator_2_5;

    @SerializedName("index_3_1")
    private double mIndicator_3_1;

    @SerializedName("index_3_2")
    private double mIndicator_3_2;


    public String getUfCode() {
        return mUfCode;
    }

    public String getCityCode() {
        return mCityCode;
    }

    public UF getUf() {
        return mUf;
    }

    public City getCity() {
        return mCity;
    }

    public int getPosRankIndex_2_1() {
        return mPosRankIndex_2_1;
    }

    public int getPosRankIndex_3_2() {
        return mPosRankIndex_3_2;
    }

    public double getIndicator_1_1() {
        return mIndicator_1_1;
    }

    public double getIndicator_1_2() {
        return mIndicator_1_2;
    }

    public double getIndicator_1_3() {
        return mIndicator_1_3;
    }

    public double getIndicator_1_4() {
        return mIndicator_1_4;
    }

    public double getIndicator_1_5() {
        return mIndicator_1_5;
    }

    public double getIndicator_1_6() {
        return mIndicator_1_6;
    }

    public double getIndicator_2_1() {
        return mIndicator_2_1;
    }

    public double getIndicator_2_2() {
        return mIndicator_2_2;
    }

    public double getIndicator_2_3() {
        return mIndicator_2_3;
    }

    public double getIndicator_2_4() {
        return mIndicator_2_4;
    }

    public double getIndicator_2_5() {
        return mIndicator_2_5;
    }

    public double getIndicator_3_1() {
        return mIndicator_3_1;
    }

    public double getIndicator_3_2() {
        return mIndicator_3_2;
    }

}