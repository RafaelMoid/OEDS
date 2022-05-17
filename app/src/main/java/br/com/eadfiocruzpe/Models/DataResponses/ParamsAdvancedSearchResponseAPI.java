package br.com.eadfiocruzpe.Models.DataResponses;

import com.google.gson.annotations.SerializedName;

import br.com.eadfiocruzpe.Models.BusinessLogicModels.City;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.UF;

public class ParamsAdvancedSearchResponseAPI {

    @SerializedName("uf")
    private UF mUf;
    @SerializedName("city")
    private City mCity;
    @SerializedName("code_period_year")
    private String mCodePeriodYear;
    @SerializedName("year")
    private String mYear;

    public UF getUf() {
        return mUf;
    }

    public City getCity() {
        return mCity;
    }

    public String getCodePeriodYear() {
        return mCodePeriodYear;
    }

    public String getYear() {
        return mYear;
    }

}