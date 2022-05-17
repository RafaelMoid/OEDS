package br.com.eadfiocruzpe.Views.Validators;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBCityRankingObject;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.GooglePlacesCity;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBSearch;
import br.com.eadfiocruzpe.Models.DataResponses.RankingCitiesYearlyCitizenValuePHResponseAPI;
import br.com.eadfiocruzpe.Utils.ConstantUtils;
import br.com.eadfiocruzpe.Utils.DateTimeUtils;
import br.com.eadfiocruzpe.Utils.LogUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;

public class BasicValidators {

    public boolean isNotNull(Object obj) {
        return obj != null;
    }

    public boolean isValidString(String name) {

        if (isNotNull(name)) {

            if (!name.isEmpty()) {

                if (!name.equals(" ")) {
                    return true;
                }
            }
        }

        return false;
    }

    public <T> boolean isValidList(ArrayList<T> list) {
        return isNotNull(list) && !list.isEmpty();
    }

    public <T> boolean isValidList(List<T> list) {
        return isNotNull(list) && !list.isEmpty();
    }

    public boolean isValidDate(String dateTimeStamp) {

        if (isValidString(dateTimeStamp)) {
            DateTimeUtils dateTimeUtils = new DateTimeUtils();
            Date date = dateTimeUtils.getDate(dateTimeStamp);

            if (isNotNull(date)) {
                return true;
            }
        }

        return false;
    }

    public boolean isValidIntArray(int[] array) {

        if (array != null) {

            if (array.length > 0) {
                return true;
            }
        }

        return false;
    }

    public boolean isValidState(String state) {

        if (isValidString(state)) {

            if (!state.equals(ConstantUtils.DEFAULT_VALUE_SEARCH_DIALOG_STATE)) {
                return true;
            }
        }

        return false;
    }

    private boolean isValidCity(String city) {

        if (isValidState(city)) {

            if (!city.equals(ConstantUtils.DEFAULT_VALUE_SEARCH_DIALOG_CITY)) {
                return true;
            }
        }

        return false;
    }

    public boolean isValidYear(String yearStr) {
        LogUtils logUtils = new LogUtils();

        if (isValidString(yearStr)) {

            try {
                Integer.parseInt(yearStr);
                return true;

            } catch (NumberFormatException nfe) {
                logUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING, "Failed to validate at isValidYear");
            }
        }

        return false;
    }

    private boolean isValidTimeRange(String timeRange) {

        if (isValidString(timeRange)) {

            if (!timeRange.equals(ConstantUtils.DEFAULT_VALUE_SEARCH_DIALOG_TIME_RANGE)) {
                return true;
            }
        }

        return false;
    }

    public boolean isValidSearch(LDBSearch search) {

        if (isNotNull(search)) {

            if (isValidState(search.getState()) && isValidCity(search.getCity()) &&
                    isValidYear(search.getYear()) && isValidTimeRange(search.getPeriodYear())) {
                return true;
            }
        }

        return false;
    }

    public boolean isValidSearchSuggestion(GooglePlacesCity searchSuggestion) {

        if (isNotNull(searchSuggestion)) {

            if (isValidString(searchSuggestion.getCity()) && isValidString(searchSuggestion.getUf())) {
                return true;
            }
        }

        return false;
    }

    public boolean isValidCityAutonomyLevel(LDBCityRankingObject cityAL) {

        if (isNotNull(cityAL)) {

            if (isValidString(cityAL.getCityName()) && isValidString(cityAL.getCityState())
                    && isNotNull(cityAL.getRankingValue()) && isNotNull(cityAL.getRankingPosition())) {
                return true;
            }
        }

        return false;
    }

    public boolean isValidRanking(RankingCitiesYearlyCitizenValuePHResponseAPI rankingObj) {

        if (isNotNull(rankingObj)) {

            if (isNotNull(rankingObj.getBestInCountry()) &&
                isNotNull(rankingObj.getWorstInCountry()) &&
                isNotNull(rankingObj.getSearched()) &&
                isNotNull(rankingObj.getBestInState()) &&
                isNotNull(rankingObj.getWorstInState()) &&
                rankingObj.getNationalAvg_2_1() != -1) {

                return true;
            }
        }

        return false;
    }

    public boolean isValidRankableObject(float valueIndicator, int pos, String cityName,
                                         String ufShortName) {

        return valueIndicator != -1 &&
               pos != -1 &&
               isValidString(cityName) &&
               isValidString(ufShortName);

    }

    public int isSameSearch(LDBSearch searchA, LDBSearch searchB) {
        int isSameSearch = -1;

        try {

            if (isNotNull(searchA) && isNotNull(searchB)) {

                if (searchA.getStateCode().equals(searchB.getStateCode()) &&
                        searchA.getCityCode().equals(searchB.getCityCode()) &&
                            searchA.getPeriodYear().equals(searchB.getPeriodYear()) &&
                                searchA.getYear().equals(searchB.getYear())) {
                    isSameSearch = 1;
                } else {
                    isSameSearch = 0;
                }
            }

        } catch (Exception ignored) {}

        return isSameSearch;
    }

}