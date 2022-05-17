package br.com.eadfiocruzpe.Presenters;

import android.database.sqlite.SQLiteConstraintException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.eadfiocruzpe.Contracts.DBQueryContract;
import br.com.eadfiocruzpe.Contracts.NationalRankingResultsPresenterContract;
import br.com.eadfiocruzpe.Utils.ColorUtils;
import br.com.eadfiocruzpe.Utils.ConstantUtils;
import br.com.eadfiocruzpe.Utils.DateTimeUtils;
import br.com.eadfiocruzpe.Utils.LogUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;
import br.com.eadfiocruzpe.Persistence.DBManager;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBCityRankingObject;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBSearch;
import br.com.eadfiocruzpe.Models.DataResponses.RankingCitiesYearlyCitizenValuePHResponseAPI;
import br.com.eadfiocruzpe.Views.Validators.BasicValidators;

public class NationalRankingResultsPresenter implements
        NationalRankingResultsPresenterContract.Presenter {

    private final String TAG = "NatRankResPresenter";

    private LDBSearch mValidLDBSearch;
    private String mCreatedAt;

    private ArrayList<LDBCityRankingObject> mRankingCitiesPH = new ArrayList<>();

    private LogUtils mLogUtils = new LogUtils();

    /**
     * Getters and Setters
     */
    private LDBSearch getValidSearch() {
        return mValidLDBSearch;
    }

    void setValidSearch(LDBSearch validSearch) {
        mValidLDBSearch = validSearch;
    }

    ArrayList<LDBCityRankingObject> getRankingCitiesPH() {
        return mRankingCitiesPH;
    }

    void setRankingCitiesPH(RankingCitiesYearlyCitizenValuePHResponseAPI ranking) {
        try {
            // Build the ranking of cities [bestCountry, bestState, searched, worstState, worstCountry]
            getRankingCitiesPH().clear();
            boolean searchedInMiddle = true;

            if (ranking.getBestInCountry().getCity().getCityId().equals(ranking.getSearched().getCity().getCityId())) {
                searchedInMiddle = false;

                addCityToRanking((float) ranking.getNationalAvg_2_1(),
                        (float) ranking.getSearched().getIndicator_2_1(),
                        ranking.getSearched().getPosRankIndex_2_1(),
                        ranking.getSearched().getCity().getFullName(),
                        ranking.getSearched().getUf().getShortName(),
                        ColorUtils.RGB_COLOR_BLUE,
                        ConstantUtils.CHART_RANKING_FLAG_BEST_IN_COUNTRY
                );
            } else {
                addCityToRanking((float) ranking.getNationalAvg_2_1(),
                        (float) ranking.getBestInCountry().getIndicator_2_1(),
                        ranking.getBestInCountry().getPosRankIndex_2_1(),
                        ranking.getBestInCountry().getCity().getFullName(),
                        ranking.getBestInCountry().getUf().getShortName(),
                        ColorUtils.RGB_COLOR_BLUE,
                        ConstantUtils.CHART_RANKING_FLAG_BEST_IN_COUNTRY
                );
            }

            if (ranking.getBestInState().getCity().getCityId().equals(ranking.getSearched().getCity().getCityId())) {
                searchedInMiddle = false;

                addCityToRanking((float) ranking.getNationalAvg_2_1(),
                        (float) ranking.getSearched().getIndicator_2_1(),
                        ranking.getSearched().getPosRankIndex_2_1(),
                        ranking.getSearched().getCity().getFullName(),
                        ranking.getSearched().getUf().getShortName(),
                        ColorUtils.RGB_COLOR_GREEN_2,
                        ConstantUtils.CHART_RANKING_FLAG_BEST_IN_STATE
                );
            } else {
                addCityToRanking((float) ranking.getNationalAvg_2_1(),
                        (float) ranking.getBestInState().getIndicator_2_1(),
                        ranking.getBestInState().getPosRankIndex_2_1(),
                        ranking.getBestInState().getCity().getFullName(),
                        ranking.getBestInState().getUf().getShortName(),
                        ColorUtils.RGB_COLOR_GREEN_2,
                        ConstantUtils.CHART_RANKING_FLAG_BEST_IN_STATE
                );
            }

            if (ranking.getWorstInCountry().getCity().getCityId().equals(ranking.getSearched().getCity().getCityId())) {
                searchedInMiddle = false;

                addCityToRanking((float) ranking.getNationalAvg_2_1(),
                        (float) ranking.getSearched().getIndicator_2_1(),
                        ranking.getSearched().getPosRankIndex_2_1(),
                        ranking.getSearched().getCity().getFullName(),
                        ranking.getSearched().getUf().getShortName(),
                        ColorUtils.RGB_COLOR_RED,
                        ConstantUtils.CHART_RANKING_FLAG_WORST_IN_COUNTRY
                );
            } else {
                addCityToRanking((float) ranking.getNationalAvg_2_1(),
                        (float) ranking.getWorstInCountry().getIndicator_2_1(),
                        ranking.getWorstInCountry().getPosRankIndex_2_1(),
                        ranking.getWorstInCountry().getCity().getFullName(),
                        ranking.getWorstInCountry().getUf().getShortName(),
                        ColorUtils.RGB_COLOR_RED,
                        ConstantUtils.CHART_RANKING_FLAG_WORST_IN_COUNTRY
                );
            }

            if (ranking.getWorstInState().getCity().getCityId().equals(ranking.getSearched().getCity().getCityId())) {
                searchedInMiddle = false;

                addCityToRanking((float) ranking.getNationalAvg_2_1(),
                        (float) ranking.getSearched().getIndicator_2_1(),
                        ranking.getSearched().getPosRankIndex_2_1(),
                        ranking.getSearched().getCity().getFullName(),
                        ranking.getSearched().getUf().getShortName(),
                        ColorUtils.RGB_COLOR_RED_1,
                        ConstantUtils.CHART_RANKING_FLAG_WORST_IN_STATE
                );
            } else {
                addCityToRanking((float) ranking.getNationalAvg_2_1(),
                        (float) ranking.getWorstInState().getIndicator_2_1(),
                        ranking.getWorstInState().getPosRankIndex_2_1(),
                        ranking.getWorstInState().getCity().getFullName(),
                        ranking.getWorstInState().getUf().getShortName(),
                        ColorUtils.RGB_COLOR_RED_1,
                        ConstantUtils.CHART_RANKING_FLAG_WORST_IN_STATE
                );
            }

            if (searchedInMiddle) {
                addCityToRanking((float) ranking.getNationalAvg_2_1(),
                        (float) ranking.getSearched().getIndicator_2_1(),
                        ranking.getSearched().getPosRankIndex_2_1(),
                        ranking.getSearched().getCity().getFullName(),
                        ranking.getSearched().getUf().getShortName(),
                        ColorUtils.RGB_COLOR_GREEN_1,
                        ConstantUtils.CHART_RANKING_FLAG_SEARCHED_IN_MIDDLE
                );
            }

            sortRanking();

        } catch (NullPointerException npe) {
            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to build ranking ...");
        }
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        mCreatedAt = createdAt;
    }

    /**
     * Other methods
     */
    private void addCityToRanking(float avgValueFullRanking, float valueIndicator, int pos,
                                  String cityName, String ufShortName, int color, String chartFlag) {
        try {
            BasicValidators validationUtils = new BasicValidators();

            if (validationUtils.isValidRankableObject(valueIndicator, pos, cityName, ufShortName)) {
                LDBCityRankingObject citiesRankingItem = new LDBCityRankingObject();

                citiesRankingItem.setAvgValueFullRanking(avgValueFullRanking);
                citiesRankingItem.setRankingValue(valueIndicator);
                citiesRankingItem.setRankingPosition(pos);
                citiesRankingItem.setCityName(cityName);
                citiesRankingItem.setCityState(ufShortName);
                citiesRankingItem.setRankingColor(color);
                citiesRankingItem.setChartFlag(chartFlag);
                citiesRankingItem.setLocation("");

                if (!searchCityInRanking(cityName, ufShortName)) {
                    mRankingCitiesPH.add(citiesRankingItem);
                }
            }

        } catch (NullPointerException npe) {
            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to add item to ranking ...");
        }
    }

    private boolean searchCityInRanking(String cityName, String ufShortName) {
        try {
            BasicValidators validationUtils = new BasicValidators();

            if (validationUtils.isValidString(cityName) &&
                    validationUtils.isValidString(ufShortName)) {

                for (LDBCityRankingObject cityAutonomyLevel : getRankingCitiesPH()) {

                    if (cityAutonomyLevel.getCityName().equals(cityName) &&
                            cityAutonomyLevel.getCityState().equals(ufShortName)) {
                        return true;
                    }
                }
            }

        } catch (NullPointerException npe) {
            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to searchCityInRanking");
        }

        return false;
    }

    private void sortRanking() {
        try {
            Collections.sort(getRankingCitiesPH(), new Comparator<LDBCityRankingObject>() {
                @Override
                public int compare(LDBCityRankingObject al1, LDBCityRankingObject al2) {

                    if (al1.getRankingValue() < al2.getRankingValue()) {
                        return 1;

                    } else if (al1.getRankingValue() == al2.getRankingValue()) {
                        return 0;

                    } else {
                        return -1;
                    }
                }
            });
        } catch (Exception e) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to sortRanking " + e.getMessage());
        }
    }

    float getAvgValueFullRankingCitiesPH() {

        if (getRankingCitiesPH() != null) {

            if (getRankingCitiesPH().size() > 0) {
                return getRankingCitiesPH().get(0).getAvgValueFullRanking();
            }
        }

        return 0;
    }

    /**
     * Storage: Write
     */
    @Override
    public void save() {
        try {
            BasicValidators valUtils = new BasicValidators();
            String searchId = getValidSearch().getSearchId();

            if (valUtils.isValidString(searchId)) {
                saveRankingCitiesPHCache(searchId);
            }

        } catch (Exception e) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to save" + e.getMessage());
        }
    }

    private void saveRankingCitiesPHCache(final String searchId) {
        try {

            if (getRankingCitiesPH() != null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            DBManager.getDB().runInTransaction(new Runnable() {

                                @Override
                                public void run() {

                                    for (LDBCityRankingObject cityRankingObj : getRankingCitiesPH()) {
                                        cityRankingObj.setLDBSearchId(searchId);

                                        try {
                                            DateTimeUtils dtUtils = new DateTimeUtils();
                                            LDBCityRankingObject rankingCitiesFromDB = DBManager.getDB()
                                                    .cityRankingObjectDao().find(
                                                            cityRankingObj.getLDBSearchId(),
                                                            cityRankingObj.getCityName(),
                                                            cityRankingObj.getCityState());

                                            if (rankingCitiesFromDB == null) {
                                                cityRankingObj.setCreatedAt(dtUtils.getNowTimeStamp());
                                                DBManager.getDB().cityRankingObjectDao().insert(cityRankingObj);
                                            } else {
                                                cityRankingObj.setUpdatedAt(dtUtils.getNowTimeStamp());
                                                DBManager.getDB().cityRankingObjectDao().update(cityRankingObj);
                                            }

                                        } catch (SQLiteConstraintException sqlException) {
                                            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                                    TAG + "Failed to saveRankingCitiesPHCache: " + sqlException.getMessage());

                                        } catch (java.lang.IllegalStateException ies) {
                                            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                                    TAG + "Failed to saveRankingCitiesPHCache: " + ies.getMessage());

                                        } catch (NullPointerException npe) {
                                            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                                    TAG + "Failed to saveRankingCitiesPHCache: " + npe.getMessage());
                                        }
                                    }
                                }
                            });

                        } catch (Exception e) {
                            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                    TAG + "Failed to saveRankingCitiesPHCache: " + e.getMessage());
                        }

                    }
                }).start();
            }
        } catch (Exception e) {
            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to saveRankingCitiesPHCache: " + e.getMessage());
        }
    }

    /**
     * Storage: Read
     */
    @Override
    public void loadRankingCitiesPHCache(final String searchId,
                                         final DBQueryContract loadingCallback) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        DBManager.getDB().runInTransaction(new Runnable() {

                            @Override
                            public void run() {

                                try {
                                    List<LDBCityRankingObject> rankingCities = DBManager.getDB()
                                            .cityRankingObjectDao().findAll(searchId);

                                    if (rankingCities != null) {

                                        if (getRankingCitiesPH() == null) {
                                            mRankingCitiesPH = new ArrayList<>();
                                        }

                                        getRankingCitiesPH().clear();

                                        for (LDBCityRankingObject cityRankingObject : rankingCities) {
                                            getRankingCitiesPH().add(cityRankingObject);
                                        }

                                        sortRanking();

                                        if (getRankingCitiesPH().size() > 0) {
                                            // Successfully recovered the ranking from the cache database,
                                            // load the components with the cache data
                                            loadingCallback.gotDataFromCache(true);
                                        } else {
                                            // Failed to recover ranking from cache, make a new web request
                                            loadingCallback.gotDataFromCache(false);
                                        }
                                    } else {
                                        // Failed to recover ranking from cache, make a new web request
                                        loadingCallback.gotDataFromCache(false);
                                    }

                                } catch (SQLiteConstraintException sqlException) {
                                    mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                            TAG + "Failed to loadRankingCitiesPHCache: " + sqlException.getMessage());

                                    // Failed to recover ranking from cache, make a new web request
                                    loadingCallback.gotDataFromCache(false);

                                } catch (java.lang.IllegalStateException ies) {
                                    mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                            TAG + "Failed to loadRankingCitiesPHCache: " + ies.getMessage());

                                    // Failed to recover ranking from cache, make a new web request
                                    loadingCallback.gotDataFromCache(false);

                                } catch (NullPointerException npe) {
                                    mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                            TAG + "Failed to loadRankingCitiesPHCache: " + npe.getMessage());

                                    // Failed to recover ranking from cache, make a new web request
                                    loadingCallback.gotDataFromCache(false);
                                }

                            }
                        });

                    } catch (Exception e) {
                        mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                                TAG + "Failed to loadRankingCitiesPHCache" + e.getMessage());

                        // Failed to recover ranking from cache, make a new web request
                        loadingCallback.gotDataFromCache(false);
                    }

                }
            }).start();

        } catch (Exception e) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to loadRankingCitiesPHCache" + e.getMessage());

            // Failed to recover ranking from cache, make a new web request
            loadingCallback.gotDataFromCache(false);
        }
    }

}