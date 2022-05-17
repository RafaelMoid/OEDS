package br.com.eadfiocruzpe.Presenters;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Handler;
import android.os.Looper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Contracts.DBQueryContract;
import br.com.eadfiocruzpe.Contracts.DashboardContract;
import br.com.eadfiocruzpe.Contracts.SearchResultsPresenterContract;
import br.com.eadfiocruzpe.Contracts.NationalRankingResultsPresenterContract;
import br.com.eadfiocruzpe.Networking.SiopsWebScrapingService.SiopsWebScrapingService;
import br.com.eadfiocruzpe.Networking.OedsApiService.OedsApiService;
import br.com.eadfiocruzpe.Utils.ConnectivityUtils;
import br.com.eadfiocruzpe.Utils.ColorUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;
import br.com.eadfiocruzpe.Utils.LogUtils;
import br.com.eadfiocruzpe.Utils.ConstantUtils;
import br.com.eadfiocruzpe.Persistence.DBManager;
import br.com.eadfiocruzpe.Models.DataResponses.CityIndicatorsPHResponseSiops;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBSearch;
import br.com.eadfiocruzpe.Models.DataResponses.GovInvestmentsPHExpensesDeclaredCitySiops;
import br.com.eadfiocruzpe.Models.DataResponses.RankingCitiesYearlyCitizenValuePHResponseAPI;
import br.com.eadfiocruzpe.Models.DataResponses.BaseWebScrapingResponse;
import br.com.eadfiocruzpe.Views.Validators.BasicValidators;

public class DashboardPresenter extends BasePresenter<DashboardContract.View> implements
        DashboardContract.Presenter,
        SearchResultsPresenterContract.Client,
        NationalRankingResultsPresenterContract.Client {

    private final String TAG = "DashboardPresenter";

    private SearchResultsPresenter mSearchResultsPresenter = new SearchResultsPresenter();
    private NationalRankingResultsPresenter mNationalRankingResultsPresenter = new NationalRankingResultsPresenter();
    private LogUtils mLogUtils;

    public DashboardPresenter(Context context) {
        mContext = context;
        mLogUtils = new LogUtils(context.getApplicationContext());
    }

    /**
     * Data: Update Search
     *
     * Try to recover the search results from the cache DB, if a block of content isn't
     * available in cache, request it to the web services.
     */
    public void updateSearch(final LDBSearch search) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {

                        DBManager.getDB().runInTransaction(new Runnable() {

                            @Override
                            public void run() {

                                try {
                                    DBManager.getDB().searchDao().update(search);

                                } catch (SQLiteConstraintException sqlException) {
                                    mLogUtils.logSystemMessage(TypeUtils.LogMsgType.REGULAR,
                                            TAG + "Failed to updateSearch: " + sqlException.getMessage());
                                }
                            }

                        });

                    } catch (Exception e) {
                        mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                TAG + "Failed to updateSearch " + e.getMessage());
                    }

                }

            }).start();
        } catch (Exception e) {
            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to updateSearch " + e.getMessage());
        }
    }

    /**
     * Data: load reports
     *
     * Try to recover the search results from the cache DB, if a block of the content isn't
     * available in cache, request it to the web services.
     *
     * The methods of the database run on background threads, that's why we have to use
     * DBQueryContract to listen to the responses.
     */
    @Override
    public void loadReports(LDBSearch search) {
        tryLoadCityIndicatorsCache(search);
        tryLoadInvestmentsExpensesCache(search);
        tryLoadRankingCache(search);
    }

    private void tryLoadCityIndicatorsCache(final LDBSearch search) {
        try {
            mSearchResultsPresenter.loadCityIndicatorsFromCache(search.getSearchId(),
                    new DBQueryContract() {

                        @Override
                        public void gotDataFromCache(boolean gotData){

                            if (gotData) {
                                loadComponentsDependentCityIndicators(true);
                            }else{
                                requestCityIndicatorsOnPH();
                            }
                        }
                    });
        } catch (NullPointerException npe) {
            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to tryLoadCityIndicatorsCache " + npe.getMessage());
        }
    }

    private void tryLoadInvestmentsExpensesCache(final LDBSearch search) {

        try {
            mSearchResultsPresenter.loadRevenueDeclaredExpensesFromCache(search.getSearchId(),
                    new DBQueryContract() {

                        @Override
                        public void gotDataFromCache(boolean gotData) {

                            if (gotData) {
                                loadComponentsDependentGovInvestmentsExpenses(true);
                            } else {
                                requestGovInvestmentsDeclaredExpensesCityPH();
                            }
                        }
                    });
        } catch (NullPointerException npe) {
            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to tryLoadInvestmentsExpensesCache " + npe.getMessage());
        }
    }

    private void tryLoadRankingCache(LDBSearch search) {

        try {
            mNationalRankingResultsPresenter.loadRankingCitiesPHCache(search.getSearchId(),
                    new DBQueryContract() {

                        @Override
                        public void gotDataFromCache(boolean gotData) {

                            if (gotData) {
                                loadComponentsDependentRankingCities(true);
                            } else {
                                requestSummaryRankingCities();
                            }
                        }
                    });
        } catch (NullPointerException npe) {
            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to tryLoadInvestmentsExpensesCache " + npe.getMessage());
        }
    }

    /**
     * City Indicators on Public Health
     */
    private void requestCityIndicatorsOnPH() {
        try {
            final DashboardContract.View view = getView();
            BasicValidators basicValidators = new BasicValidators();

            if (view != null && ConnectivityUtils.isConnected(mContext.getApplicationContext())) {
                view.onShowProgress(true);

                if (basicValidators.isValidSearch(view.getSearch())) {
                    getSearchResults().setValidSearch(view.getSearch());

                    SiopsWebScrapingService.build().requestCityIndicatorsPublicHealth(
                            view.getSearch().getYear(),
                            view.getSearch().getStateCode(),
                            view.getSearch().getPeriodYearCode(),
                            view.getSearch().getCityCode(),
                            SiopsWebScrapingService.PARAM_ORDER_BY,
                            SiopsWebScrapingService.PARAM_AUTH,
                            SiopsWebScrapingService.PARAM_CODE_HIST
                    ).enqueue(mCallbackCityIndicatorsPH);
                }
            } else {
                mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                        TAG + "Failed to requestCityIndicatorsOnPH");
            }
        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to requestCityIndicatorsOnPH");
        }
    }

    private final Callback<BaseWebScrapingResponse> mCallbackCityIndicatorsPH =
            new Callback<BaseWebScrapingResponse>() {

        @Override
        public void onResponse(Call<BaseWebScrapingResponse> call,
                               final Response<BaseWebScrapingResponse> rawResponse) {
            try {

                if (rawResponse != null) {
                    final CityIndicatorsPHResponseSiops response =
                            new CityIndicatorsPHResponseSiops(rawResponse.body());

                    getSearchResults().setCityIndicators(response.getCityIndexes());

                    getSearchResults().save();

                    loadComponentsDependentCityIndicators(true);

                } else {
                    mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                            TAG + " Failed to loadComponentsDependentCityIndicators");

                    loadComponentsDependentCityIndicators(false);
                }

            } catch (Exception e) {
                mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                        TAG + " Failed to loadComponentsDependentCityIndicators " + e.getMessage());

                loadComponentsDependentCityIndicators(false);
            }
        }

        @Override
        public void onFailure(Call<BaseWebScrapingResponse> call, Throwable t) {
            final DashboardContract.View view = getView();

            if (view != null) {
                view.onShowProgress(false);
                view.onShowMessage(
                        mContext.getString(R.string.dialog_search_msg_problem_get_data_siops_try_again));
            }
        }
    };

    private void loadComponentsDependentCityIndicators(final boolean loadData) {
        final DashboardContract.View view = getView();

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (view != null) {
                    view.onShowProgress(false);

                    if (loadData) {
                        try {
                            view.loadValuePHCitizenDay(getSearchResults()
                                    .getValueCitizenDay());
                        } catch (Exception e) {
                            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                                    TAG + " Failed to loadComponentsDependentCityIndicators " + e.getMessage());
                        }

                        try {
                            view.loadCityInvestmentsPHCitizenYear(getSearchResults()
                                    .getValueCitizenYear());
                        } catch (Exception e) {
                            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                                    TAG + " Failed to loadComponentsDependentCityIndicators " + e.getMessage());
                        }

                        try {
                            view.loadInvestmentOwnCityValueCitizenYear(getSearchResults()
                                    .getValueInvestedCityYearlyCitizenValue());
                        } catch (Exception e) {
                            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                                    TAG + " Failed to loadComponentsDependentCityIndicators " + e.getMessage());
                        }

                        try {
                            view.loadInvestmentOtherSourcesValueCitizenYear(getSearchResults()
                                    .getValueInvestedOtherSourcesYearlyCitizenValue());
                        } catch (Exception e) {
                            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                                    TAG + " Failed to loadComponentsDependentCityIndicators " + e.getMessage());
                        }

                        try {
                            view.loadCityFinancialAutonomy(getSearchResults()
                                    .getPercentageCityFinancialAutonomy());
                        } catch (Exception e) {
                            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                                    TAG + " Failed to loadComponentsDependentCityIndicators " + e.getMessage());
                        }
                    } else {
                        view.onShowMessage(mContext.getString(R.string.dashboard_msg_could_not_load_indicators));
                    }
                }
            }
        }, ConstantUtils.TIMEOUT_LOAD_COMPONENTS);
    }

    /**
     * Expenses declared by the City Government
     */
    private void requestGovInvestmentsDeclaredExpensesCityPH() {
        try {
            final DashboardContract.View view = getView();
            BasicValidators basicValidators = new BasicValidators();

            if (view != null && ConnectivityUtils.isConnected(mContext.getApplicationContext())) {

                if (basicValidators.isValidSearch(view.getSearch())) {
                    view.onShowProgress(true);

                    getSearchResults().setValidSearch(view.getSearch());

                    SiopsWebScrapingService.build().requestGovInvestmentsDeclaredExpensesCityPH(
                            view.getSearch().getYear(),
                            view.getSearch().getStateCode(),
                            view.getSearch().getPeriodYearCode(),
                            view.getSearch().getCityCode(),
                            SiopsWebScrapingService.PARAM_BTN_CONSULTAR
                    ).enqueue(mCallbackGovInvestmentsExpensesCityPH);
                }
            } else {
                mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                        TAG + "Failed to requestGovInvestmentsDeclaredExpensesCityPH");
            }
        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to requestGovInvestmentsDeclaredExpensesCityPH");
        }
    }

    private final Callback<BaseWebScrapingResponse> mCallbackGovInvestmentsExpensesCityPH =
            new Callback<BaseWebScrapingResponse>() {

        @Override
        public void onResponse(Call<BaseWebScrapingResponse> call,
                               final Response<BaseWebScrapingResponse> rawResponse) {

            try {
                GovInvestmentsPHExpensesDeclaredCitySiops response =
                        new GovInvestmentsPHExpensesDeclaredCitySiops(
                                rawResponse.body(),
                                getSearchResults().getValidSearch().getPeriodYearCode());

                getSearchResults().setRevenuesAndExpensesDeclaredByCity(
                        response.getMapCityRevenueSources(),
                        response.getMapCityExpenses());

                getSearchResults().save();

                loadComponentsDependentGovInvestmentsExpenses(true);

            } catch (Exception e) {
                mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                        TAG + "Failed to requestGovInvestmentsDeclaredExpensesCityPH " + e.getMessage());

                loadComponentsDependentGovInvestmentsExpenses(false);
            }
        }

        @Override
        public void onFailure(Call<BaseWebScrapingResponse> call, Throwable t) {
            final DashboardContract.View view = getView();

            if (view != null) {
                view.onShowProgress(false);
                view.onShowMessage(
                        mContext.getString(R.string.dialog_search_msg_problem_get_data_siops_try_again));
            }
        }
    };

    private void loadComponentsDependentGovInvestmentsExpenses(final boolean show) {
        final DashboardContract.View view = getView();

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (view != null) {
                    view.onShowProgress(false);

                    if (show) {
                        try {
                            view.loadChartTotalInvestmentsCityPH(
                                    getSearchResults().getDatasetInvestmentsCityPH(),
                                    ColorUtils.getColorsInvestmentsForCityPH(),
                                    getSearchResults().getLabelsInvestmentsCityPH(mContext.getResources()),
                                    mContext.getString(R.string.component_total_investment_public_health_for_city_title));
                        } catch (Exception e) {
                            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                                    TAG + " Failed to loadComponentsDependentGovInvestmentsExpenses " + e.getMessage());
                        }

                        try {
                            view.loadValueInvestedFederalGovCity(
                                    getSearchResults().getGovInvestmentsPHBySource().getFederalInvestmentPHCity());
                        } catch (Exception e) {
                            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                                    TAG + " Failed to loadComponentsDependentGovInvestmentsExpenses " + e.getMessage());
                        }

                        try {
                            view.loadValueInvestedStateGovCity(
                                    getSearchResults().getGovInvestmentsPHBySource().getStateInvestmentPHCity());
                        } catch (Exception e) {
                            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                                    TAG + " Failed to loadComponentsDependentGovInvestmentsExpenses " + e.getMessage());
                        }

                        try {
                            view.loadValueInvestedOtherSourcesPH(
                                    getSearchResults().getGovInvestmentsPHBySource().getOtherInvestmentPHCity());
                        } catch (Exception e) {
                            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                                    TAG + " Failed to loadComponentsDependentGovInvestmentsExpenses " + e.getMessage());
                        }

                        try {
                            view.loadPHExpensesDeclaredCity(getSearchResults().getDeclaredExpenses());
                        } catch (Exception e) {
                            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                                    TAG + " Failed to loadComponentsDependentGovInvestmentsExpenses " + e.getMessage());
                        }
                    } else {
                        view.onShowMessage(mContext.getString(R.string.dashboard_msg_could_not_load_investments));
                    }
                }
            }
        }, ConstantUtils.TIMEOUT_LOAD_COMPONENTS);
    }

    /**
     * Ranking of cities
     */
    private void requestSummaryRankingCities() {
        try {
            final DashboardContract.View view = getView();
            BasicValidators basicValidators = new BasicValidators();

            if (view != null && ConnectivityUtils.isConnected(mContext.getApplicationContext())) {

                if (basicValidators.isValidSearch(view.getSearch())) {
                    view.onShowProgress(true);

                    getNationalRankingResults().setValidSearch(view.getSearch());

                    OedsApiService.build().requestSummaryNationalRankingMainIndicator(
                            view.getSearch().getYear(),
                            view.getSearch().getStateCode(),
                            view.getSearch().getCityCode()
                    ).enqueue(mCallbackRankingCitiesPH);
                }
            } else {
                mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                        TAG + "Failed to requestSummaryRankingCities");
            }
        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to requestSummaryRankingCities");
        }
    }

    private final Callback<RankingCitiesYearlyCitizenValuePHResponseAPI> mCallbackRankingCitiesPH =
            new Callback<RankingCitiesYearlyCitizenValuePHResponseAPI>() {

        @Override
        public void onResponse(Call<RankingCitiesYearlyCitizenValuePHResponseAPI> call,
                               final Response<RankingCitiesYearlyCitizenValuePHResponseAPI> rawResponse) {
            try {

                if (rawResponse != null) {
                    BasicValidators validationUtils = new BasicValidators();
                    RankingCitiesYearlyCitizenValuePHResponseAPI ranking = rawResponse.body();

                    if (validationUtils.isValidRanking(ranking)) {
                        getNationalRankingResults().setRankingCitiesPH(ranking);

                        getNationalRankingResults().save();

                        loadComponentsDependentRankingCities(true);
                    }
                } else {
                    mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                            TAG + "Failed to requestSummaryRankingCities");

                    loadComponentsDependentRankingCities(false);
                }

            } catch (NullPointerException e) {
                mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                        TAG + "Failed to requestSummaryRankingCities");

                loadComponentsDependentRankingCities(false);
            }
        }

        @Override
        public void onFailure(Call<RankingCitiesYearlyCitizenValuePHResponseAPI> call, Throwable t) {
            final DashboardContract.View view = getView();

            if (view != null) {
                view.onShowProgress(false);
                view.onShowMessage(mContext.getString(R.string.error_api_unknown));
            }
        }
    };

    private void loadComponentsDependentRankingCities(final boolean show) {
        final DashboardContract.View view = getView();

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (view != null) {
                    view.onShowProgress(false);

                    try {

                        if (show) {
                            view.loadRankingCities(
                                    getNationalRankingResults().getAvgValueFullRankingCitiesPH(),
                                    getNationalRankingResults().getRankingCitiesPH());
                        } else {
                            view.onShowMessage(mContext.getString(R.string.dashboard_msg_could_not_load_ranking));
                        }

                    } catch (Exception e) {
                        mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                                TAG + " Failed to loadComponentsDependentRankingCities " + e.getMessage());
                    }
                }
            }
        }, ConstantUtils.TIMEOUT_LOAD_COMPONENTS);
    }

    /**
     * Getters and Setters
     */
    public SearchResultsPresenter getSearchResults() {
        return mSearchResultsPresenter;
    }

    private NationalRankingResultsPresenter getNationalRankingResults() {
        return mNationalRankingResultsPresenter;
    }

}