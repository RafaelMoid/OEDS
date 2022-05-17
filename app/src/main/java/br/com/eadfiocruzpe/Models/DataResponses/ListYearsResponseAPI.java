package br.com.eadfiocruzpe.Models.DataResponses;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

import br.com.eadfiocruzpe.Utils.ConstantUtils;
import br.com.eadfiocruzpe.Utils.DateTimeUtils;

public class ListYearsResponseAPI {

    @SerializedName("list_years")
    private ArrayList<Integer> mYears = new ArrayList<>();

    /**
     * Getters and Setters
     */
    public ArrayList<String> getListYearsStr() {
        ArrayList<String> mStrYears = new ArrayList<>();

        if (!getListYears().isEmpty()) {
            mStrYears.add(ConstantUtils.DEFAULT_VALUE_SEARCH_DIALOG_YEAR);

            for (Integer year : getListYears()) {
                mStrYears.add(String.valueOf(year));
            }

            return mStrYears;
        } else {
            return getListYearsEmergency();
        }
    }

    private ArrayList<Integer> getListYears() {
        return mYears;
    }

    private ArrayList<String> getListYearsEmergency() {
        ArrayList<String> years = new ArrayList<>();
        DateTimeUtils dateTimeUtils = new DateTimeUtils();

        years.add(ConstantUtils.DEFAULT_VALUE_SEARCH_DIALOG_YEAR);

        for (int i = dateTimeUtils.getCurrentYear(); i >= ConstantUtils.TIME_RANGE_START_YEAR; i--) {
            years.add(String.valueOf(i));
        }

        return years;
    }

}