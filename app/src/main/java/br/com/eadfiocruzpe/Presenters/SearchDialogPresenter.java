package br.com.eadfiocruzpe.Presenters;

import java.util.ArrayList;
import java.util.HashMap;

import android.database.sqlite.SQLiteConstraintException;
import android.os.Handler;
import android.os.Looper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Contracts.SearchDialogContract;
import br.com.eadfiocruzpe.Utils.ConstantUtils;
import br.com.eadfiocruzpe.Utils.LogUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;
import br.com.eadfiocruzpe.Networking.OedsApiService.OedsApiService;
import br.com.eadfiocruzpe.Persistence.DBManager;
import br.com.eadfiocruzpe.Persistence.PreferencesManager;
import br.com.eadfiocruzpe.Models.DataResponses.ParamsAdvancedSearchResponseAPI;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.GooglePlacesCity;
import br.com.eadfiocruzpe.Models.DataResponses.ListYearsResponseAPI;
import br.com.eadfiocruzpe.Models.DataResponses.ListAvailableCitiesUfResponseAPI;
import br.com.eadfiocruzpe.Models.DataResponses.ListAvailablePeriodsYearResponseAPI;
import br.com.eadfiocruzpe.Models.DataResponses.ListUfsResponseAPI;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBSearch;
import br.com.eadfiocruzpe.Views.Validators.BasicValidators;

public class SearchDialogPresenter extends BasePresenter<SearchDialogContract.View> implements
        SearchDialogContract.Presenter {

    private final String TAG = "SearchDialogPresenter";

    private ArrayList<String> mUfs = new ArrayList<>();
    private ArrayList<String> mCities = new ArrayList<>();
    private ArrayList<String> mYears = new ArrayList<>();
    private ArrayList<String> mTimeRanges = new ArrayList<>();

    private HashMap<String, String> mMapStates = new HashMap<>();
    private HashMap<String, String> mMapCities = new HashMap<>();
    private HashMap<String, String> mMapTimeRanges = new HashMap<>();

    private LDBSearch mSearch;
    private int mSelectedUfCode = -1;
    private int mSelectedYear = -1;
    private int mNumStoredSearches = -1;

    private LogUtils mLogUtils = new LogUtils();
    private GooglePlacesCity mSuggestedCity;

    /**
     * Initialization
     */
    public SearchDialogPresenter() {
        initListsWithDefaultValues();
    }

    @Override
    public void initListsWithDefaultValues() {
        mCities.add(ConstantUtils.DEFAULT_VALUE_SEARCH_DIALOG_CITY);
        mTimeRanges.add(ConstantUtils.DEFAULT_VALUE_SEARCH_DIALOG_TIME_RANGE);
        mYears.add(ConstantUtils.DEFAULT_VALUE_SEARCH_DIALOG_YEAR);
        mUfs.add(ConstantUtils.DEFAULT_VALUE_SEARCH_DIALOG_STATE);
    }

    /**
     * Automatic Search
     */
    @Override
    public void startAutomaticSearch() {
        try {
            SearchDialogContract.View view = getView();

            if (view != null) {
                view.showProgressOnDialog(true);

                OedsApiService.build().requestParamsAdvancedSearch(
                        getSuggestedCity().getUf(),
                        getSuggestedCity().getCity()
                ).enqueue(mCallbackParamsAdvancedSearchAPI);
            }

        } catch (Exception ignored) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to startAutomaticSearch");
        }
    }

    private final Callback<ParamsAdvancedSearchResponseAPI> mCallbackParamsAdvancedSearchAPI =
            new Callback<ParamsAdvancedSearchResponseAPI>() {
        @Override
        public void onResponse(Call<ParamsAdvancedSearchResponseAPI> call,
                               Response<ParamsAdvancedSearchResponseAPI> response) {
            final SearchDialogContract.View view = getView();

            if (view != null) {
                view.showProgressOnDialog(false);

                try {

                    if (response != null) {
                        ParamsAdvancedSearchResponseAPI searchParams = response.body();

                        LDBSearch search = new LDBSearch();
                        search.setState(searchParams.getUf().getFullName());
                        search.setStateCode(searchParams.getUf().getUfId());
                        search.setCity(searchParams.getCity().getFullName());
                        search.setCityCode(searchParams.getCity().getCityId());
                        search.setYear(searchParams.getYear());
                        search.setPeriodYear(ConstantUtils.DESCRIPTION_LAST_TIME_PERIOD_APP);
                        search.setPeriodYearCode(searchParams.getCodePeriodYear());

                        loadCurrentSearch(search);
                        view.finishSearchSuccessfully(search);

                    } else {
                        view.informAdvancedSearchFailed();
                    }
                } catch (NullPointerException npe) {
                    view.informAdvancedSearchFailed();
                }
            }
        }

        @Override
        public void onFailure(Call<ParamsAdvancedSearchResponseAPI> call, Throwable t) {
            final SearchDialogContract.View view = getView();

            if (view != null) {
                view.informAdvancedSearchFailed();
            }
        }
    };

    @Override
    public void loadCurrentSearch(LDBSearch search) {
        try {
            SearchDialogContract.View view = getView();

            if (view != null) {
                mSearch = search;

                loadListUfs();
                loadListUfCities(getSelectedSearch().getStateCode());
                loadListYears();
                loadTimeRangesForYear(getSelectedSearch().getYear());

                setSelectedUfCode(getSelectedSearch().getStateCode());
                setSelectedYear(getSelectedSearch().getYear());

                mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                        TAG + "Failed to loadCurrentSearch");
            }
        } catch (Exception ignored) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to loadCurrentSearch");
        }
    }

    /**
     * Request data from the API
     */
    @Override
    public void loadListUfs() {
        SearchDialogContract.View view = getView();

        if (view != null) {
            view.showProgressOnDialog(true);
            OedsApiService.build().requestListUFs().enqueue(mCallbackListUfsFromAPI);
        }
    }

    private final Callback<ListUfsResponseAPI> mCallbackListUfsFromAPI = new Callback<ListUfsResponseAPI>() {

        @Override
        public void onResponse(Call<ListUfsResponseAPI> call, Response<ListUfsResponseAPI> rawResponse) {
            final SearchDialogContract.View view = getView();

            try {

                if (view != null) {
                    view.showProgressOnDialog(false);

                    if (rawResponse != null) {
                        ListUfsResponseAPI response = rawResponse.body();
                        response.buildMapAvailableUFs();

                        // The order in which the methods below are called is important,
                        // because the list with the time ranges gets built when the getMapUfsCodes()
                        // is invoked.
                        mMapStates = response.getMapUfsCodes();
                        mUfs = response.getListUfs();

                        view.loadListUfs();
                    } else {
                        view.onShowMessage(
                                mContext.getString(R.string.dialog_search_msg_problem_get_data_siops_try_again));
                    }
                } else {
                    mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                            TAG + "Failed to mCallbackListUfsFromAPI");
                }
            } catch (Exception e) {
                mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                        TAG + "Failed to mCallbackListUfsFromAPI");
            }
        }

        @Override
        public void onFailure(Call<ListUfsResponseAPI> call, Throwable t) {
            final SearchDialogContract.View view = getView();

            if (view != null) {
                view.showProgressOnDialog(false);
                view.onShowMessage(
                        mContext.getString(R.string.dialog_search_msg_problem_get_data_siops_try_again));
            }
        }
    };

    @Override
    public void loadListUfCities(String selectedUfCode) {
        final SearchDialogContract.View view = getView();

        if (view != null) {
            view.showProgressOnDialog(true);
            setSelectedUfCode(selectedUfCode);
            OedsApiService.build().requestListCitiesUf(selectedUfCode).enqueue(mCallbackListAvailableCitiesUfFromAPI);
        }
    }

    private final Callback<ListAvailableCitiesUfResponseAPI> mCallbackListAvailableCitiesUfFromAPI =
            new Callback<ListAvailableCitiesUfResponseAPI>() {

                @Override
                public void onResponse(Call<ListAvailableCitiesUfResponseAPI> call,
                                       Response<ListAvailableCitiesUfResponseAPI> rawResponse) {
                    final SearchDialogContract.View view = getView();

                    if (view != null) {
                        view.showProgressOnDialog(false);

                        try {
                            ListAvailableCitiesUfResponseAPI response = rawResponse.body();
                            response.buildMapAvailableCities();

                            try {
                                // The order in which the methods below are called is important,
                                // because the list with the time ranges gets built when the getMapAvailableCities()
                                // is invoked.
                                mMapCities = response.getMapAvailableCities();
                                mCities = response.getListAvailableCities();

                                view.loadListCitiesSelectedUf();

                            } catch (NullPointerException e) {
                                view.onShowMessage(
                                        mContext.getString(R.string.dialog_search_msg_problem_get_data_siops_try_again));
                            }
                        } catch (NullPointerException npe) {
                            view.onShowMessage(
                                    mContext.getString(R.string.dialog_search_msg_problem_get_data_siops_try_again));
                        }
                    } else {
                        mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                                TAG + "Failed to mCallbackListAvailableCitiesUfFromAPI");
                    }
                }

                @Override
                public void onFailure(Call<ListAvailableCitiesUfResponseAPI> call, Throwable t) {
                    final SearchDialogContract.View view = getView();

                    if (view != null) {
                        view.showProgressOnDialog(false);
                        view.onShowMessage(
                                mContext.getString(R.string.dialog_search_msg_problem_get_data_siops_try_again));
                    }
                }
            };

    @Override
    public void loadListYears() {
        final SearchDialogContract.View view = getView();

        if (view != null) {
            view.showProgressOnDialog(true);
            OedsApiService.build().requestListYears().enqueue(mCallbackListYears);
        }
    }

    private final Callback<ListYearsResponseAPI> mCallbackListYears = new Callback<ListYearsResponseAPI>() {

        @Override
        public void onResponse(Call<ListYearsResponseAPI> call, Response<ListYearsResponseAPI> rawResponse) {
            final SearchDialogContract.View view = getView();

            if (view != null) {
                view.showProgressOnDialog(false);

                try {

                    if (rawResponse != null) {
                        ListYearsResponseAPI responseListYears = rawResponse.body();

                        try {
                            mYears = responseListYears.getListYearsStr();
                            view.loadListYears();

                        } catch (NullPointerException e) {
                            view.onShowMessage(
                                    mContext.getString(R.string.dialog_search_msg_problem_get_data_siops_try_again));
                        }
                    } else {
                        view.onShowMessage(
                                mContext.getString(R.string.dialog_search_msg_problem_get_data_siops_try_again));
                    }
                } catch (NullPointerException npe) {
                    view.onShowMessage(
                            mContext.getString(R.string.dialog_search_msg_problem_get_data_siops_try_again));
                }
            } else {
                mLogUtils.logSystemMessage(
                        TypeUtils.LogMsgType.WARNING,
                        TAG + mContext.getString(R.string.dialog_search_msg_problem_get_data_siops_try_again));
            }
        }

        @Override
        public void onFailure(Call<ListYearsResponseAPI> call, Throwable t) {
            try {
                final SearchDialogContract.View view = getView();

                if (view != null) {
                    view.showProgressOnDialog(false);
                    view.onShowMessage(
                            mContext.getString(R.string.dialog_search_msg_problem_get_data_siops_try_again));
                }
            } catch (NullPointerException ignored) {}
        }
    };

    @Override
    public void loadTimeRangesForYear(String selectedYear) {
        final SearchDialogContract.View view = getView();
        BasicValidators validationUtils = new BasicValidators();

        if (view != null) {
            view.showProgressOnDialog(true);

            if (validationUtils.isValidYear(selectedYear)) {
                setSelectedYear(selectedYear);
                OedsApiService.build().requestListPeriodsYear().enqueue(mCallbackListPeriodsOnYearFromAPI);
            }
        }
    }

    private final Callback<ListAvailablePeriodsYearResponseAPI> mCallbackListPeriodsOnYearFromAPI =
            new Callback<ListAvailablePeriodsYearResponseAPI>() {

                @Override
                public void onResponse(Call<ListAvailablePeriodsYearResponseAPI> call,
                                       Response<ListAvailablePeriodsYearResponseAPI> rawResponse) {
                    final SearchDialogContract.View view = getView();

                    if (view != null) {
                        view.showProgressOnDialog(false);

                        try {

                            if (rawResponse != null) {
                                ListAvailablePeriodsYearResponseAPI response = rawResponse.body();
                                response.buildMapPeriodsYear();

                                try {
                                    // The order in which the methods below are called is important,
                                    // because the list with the time ranges gets built when the getmMapPeriodsYear()
                                    // is invoked.
                                    mMapTimeRanges = response.getMapPeriodsYear();
                                    mTimeRanges = response.getListPeriodsYear();

                                    view.loadListTimeRangesSelectedYear();

                                } catch (NullPointerException e) {
                                    view.onShowMessage(
                                            mContext.getString(R.string.dialog_search_msg_problem_get_data_siops_try_again));
                                }
                            } else {
                                view.onShowMessage(
                                        mContext.getString(R.string.dialog_search_msg_problem_get_data_siops_try_again));
                            }

                        } catch (NullPointerException npe) {
                            view.onShowMessage(
                                    mContext.getString(R.string.dialog_search_msg_problem_get_data_siops_try_again));
                        }
                    } else {
                        mLogUtils.logSystemMessage(
                                TypeUtils.LogMsgType.WARNING,
                                TAG + mContext.getString(R.string.dialog_search_msg_problem_get_data_siops_try_again));
                    }
                }

                @Override
                public void onFailure(Call<ListAvailablePeriodsYearResponseAPI> call, Throwable t) {
                    final SearchDialogContract.View view = getView();

                    if (view != null) {
                        view.showProgressOnDialog(false);
                        view.onShowMessage(
                                mContext.getString(R.string.dialog_search_msg_problem_get_data_siops_try_again));
                    }
                }
    };

    /**
     * Storage
     */
    public void verifyMaxNumStoredSearches(final String msgLimitExceeded) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        DBManager.getDB().runInTransaction(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    mNumStoredSearches = DBManager.getDB().searchDao().getNumStoredSearches();

                                    new Handler(Looper.getMainLooper()).post(new Runnable() {

                                        @Override
                                        public void run() {
                                            SearchDialogContract.View view = getView();

                                            if (view != null) {

                                                if (mNumStoredSearches >= PreferencesManager.getSettingMaxNumStoredSearches()) {
                                                    view.showMessage(true, msgLimitExceeded);
                                                }

                                            } else {
                                                mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                                        TAG + "Failed to verifyMaxNumStoredSearches");
                                            }
                                        }
                                    });

                                } catch (Exception e) {
                                    mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                            TAG + "Failed to verifyMaxNumStoredSearches " + e.getMessage());
                                }
                            }

                        });

                    } catch (Exception e) {
                        mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                TAG + "Failed to verifyMaxNumStoredSearches " + e.getMessage());
                    }

                }
            }).start();

        } catch (Exception e) {
            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to verifyMaxNumStoredSearches " + e.getMessage());
        }
    }

    public void saveValidSearch(final LDBSearch search) {
        try {

            if (mNumStoredSearches < PreferencesManager.getSettingMaxNumStoredSearches()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            DBManager.getDB().runInTransaction(new Runnable() {

                                @Override
                                public void run() {

                                    try {
                                        LDBSearch searchFromDB = DBManager.getDB().searchDao().find(
                                                search.getState(),
                                                search.getCity(),
                                                search.getYear(),
                                                search.getPeriodYear());

                                        if (searchFromDB == null) {
                                            DBManager.getDB().searchDao().insert(search);
                                        } else {
                                            DBManager.getDB().searchDao().update(search);
                                        }

                                    } catch (SQLiteConstraintException sqlException) {
                                        mLogUtils.logSystemMessage(TypeUtils.LogMsgType.REGULAR,
                                                TAG + "Failed to saveValidSearch: " + sqlException.getMessage());

                                    } catch (java.lang.IllegalStateException ies) {
                                        mLogUtils.logSystemMessage(TypeUtils.LogMsgType.REGULAR,
                                                TAG + "Failed to saveValidSearch: " + ies.getMessage());
                                    }
                                }

                            });
                        } catch (Exception e) {
                            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                    TAG + "Failed to saveValidSearch " + e.getMessage());
                        }

                    }
                }).start();
            }

        } catch (Exception e) {
            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to saveValidSearch " + e.getMessage());
        }
    }

    /**
     * Getters and Setters
     */
    @Override
    public ArrayList<String> getListUfs() {
        return mUfs;
    }

    @Override
    public ArrayList<String> getListCities() {
        return mCities;
    }

    @Override
    public ArrayList<String> getListYears() {
        return mYears;
    }

    @Override
    public ArrayList<String> getListTimeRanges() {
        return mTimeRanges;
    }

    @Override
    public HashMap<String, String> getMapUfsCodes() {
        return mMapStates;
    }

    @Override
    public HashMap<String, String> getMapCitiesCodes() {
        return mMapCities;
    }

    @Override
    public void setSelectedUfCode(String selectedUfCode) {
        try {
            mSelectedUfCode = Integer.parseInt(selectedUfCode);

        } catch (NumberFormatException nfe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to setSelectedUfCode: " + nfe.getMessage());
        }
    }

    @Override
    public String getSelectedUfCode() {
        return String.valueOf(mSelectedUfCode);
    }

    @Override
    public void setSelectedYear(String selectedYear) {
        try {
            mSelectedYear = Integer.parseInt(selectedYear);

        } catch (NumberFormatException nfe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to setSelectedYear: " + nfe.getMessage());
        }
    }

    @Override
    public int getSelectedYear() {
        return mSelectedYear;
    }

    @Override
    public HashMap<String, String> getMapTimeRangesCodes() {
        return mMapTimeRanges;
    }

    @Override
    public LDBSearch getSelectedSearch() {
        return mSearch;
    }

    public void setSuggestedCity(GooglePlacesCity suggestedCity) {
        mSuggestedCity = suggestedCity;
    }

    public GooglePlacesCity getSuggestedCity() {
        return mSuggestedCity;
    }

}