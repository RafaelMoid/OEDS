package br.com.eadfiocruzpe.Models.DataResponses;

import com.google.gson.annotations.SerializedName;

import br.com.eadfiocruzpe.Models.BusinessLogicModels.RankableCityObject;

public class RankingCitiesYearlyCitizenValuePHResponseAPI {

    @SerializedName("best_in_country")
    private RankableCityObject mBestCountry;

    @SerializedName("worst_in_country")
    private RankableCityObject mWorstCountry;

    @SerializedName("searched")
    private RankableCityObject mSearched;

    @SerializedName("best_in_state")
    private RankableCityObject mBestState;

    @SerializedName("worst_in_state")
    private RankableCityObject mWorstState;

    @SerializedName("national_avg_index_3_2")
    private double mNationalAvg_3_2;

    @SerializedName("national_avg_index_2_1")
    private double mNationalAvg_2_1;

    public RankableCityObject getBestInCountry() {
        return mBestCountry;
    }

    public RankableCityObject getWorstInCountry() {
        return mWorstCountry;
    }

    public RankableCityObject getSearched() {
        return mSearched;
    }

    public RankableCityObject getBestInState() {
        return mBestState;
    }

    public RankableCityObject getWorstInState() {
        return mWorstState;
    }

    public double getNationalAvg_3_2() {
        return mNationalAvg_3_2;
    }

    public double getNationalAvg_2_1() {
        return mNationalAvg_2_1;
    }

}