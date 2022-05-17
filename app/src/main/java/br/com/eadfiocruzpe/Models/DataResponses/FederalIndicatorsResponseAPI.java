package br.com.eadfiocruzpe.Models.DataResponses;

import com.google.gson.annotations.SerializedName;

import br.com.eadfiocruzpe.Models.BusinessLogicModels.FederalIndicator;

public class FederalIndicatorsResponseAPI {

    @SerializedName("indicator_2_1")
    private FederalIndicator mFederalIndicator2_1;

    @SerializedName("indicator_2_2")
    private FederalIndicator mFederalIndicator2_2;

    @SerializedName("indicator_2_3")
    private FederalIndicator mFederalIndicator2_3;

    @SerializedName("indicator_2_4")
    private FederalIndicator mFederalIndicator2_4;

    @SerializedName("indicator_2_5")
    private FederalIndicator mFederalIndicator2_5;

    @SerializedName("indicator_3_2")
    private FederalIndicator mFederalIndicator3_2;

    @SerializedName("searchYear")
    private int mSearchYear;

    @SerializedName("searchYearPeriod")
    private int mSearchYearPeriod;

    public FederalIndicator getFederalIndicator2_1() {
        return mFederalIndicator2_1;
    }

    public FederalIndicator getFederalIndicator2_2() {
        return mFederalIndicator2_2;
    }

    public FederalIndicator getFederalIndicator2_3() {
        return mFederalIndicator2_3;
    }

    public FederalIndicator getFederalIndicator2_4() {
        return mFederalIndicator2_4;
    }

    public FederalIndicator getFederalIndicator2_5() {
        return mFederalIndicator2_5;
    }

    public FederalIndicator getFederalIndicator3_2() {
        return mFederalIndicator3_2;
    }

    public int getSearchYear() {
        return mSearchYear;
    }

    public int getSearchYearPeriod() {
        return mSearchYearPeriod;
    }

}