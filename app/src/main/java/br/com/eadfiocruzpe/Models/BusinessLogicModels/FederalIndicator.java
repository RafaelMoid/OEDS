package br.com.eadfiocruzpe.Models.BusinessLogicModels;

import com.google.gson.annotations.SerializedName;

public class FederalIndicator {

    @SerializedName("id")
    private String mId;

    @SerializedName("description")
    private String mDescription;

    @SerializedName("denominator")
    private double mDenominator;

    @SerializedName("numerator")
    private double mNumerator;

    @SerializedName("value")
    private double mValue;


    public String getId() {
        return mId;
    }

    public String getDescription() {
        return mDescription;
    }

    public double getDenominator() {
        return mDenominator;
    }

    public double getNumerator() {
        return mNumerator;
    }

    public double getValue() {
        return mValue;
    }

}
