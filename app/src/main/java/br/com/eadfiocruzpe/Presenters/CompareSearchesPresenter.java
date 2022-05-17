package br.com.eadfiocruzpe.Presenters;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Contracts.DBQueryContract;
import br.com.eadfiocruzpe.Contracts.CompareSearchesContract;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBSearch;
import br.com.eadfiocruzpe.Persistence.DBManager;
import br.com.eadfiocruzpe.Utils.ConstantUtils;
import br.com.eadfiocruzpe.Utils.LogUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;
import br.com.eadfiocruzpe.Views.Validators.BasicValidators;

public class CompareSearchesPresenter extends BasePresenter<CompareSearchesContract.View> implements
        CompareSearchesContract.Presenter {

    private static final String TAG = "CompareSearchesPresenter";

    private ArrayList<LDBSearch> mLDBSearchHistory = new ArrayList<>();
    private ArrayList<LDBSearch> mSelectedSearches = new ArrayList<>();
    private SearchResultsPresenter mSearchResultsPresenterA;
    private SearchResultsPresenter mSearchResultsPresenterB;
    private LogUtils mLogUtils;

    public CompareSearchesPresenter(Context context) {
        mContext = context;
        mLogUtils = new LogUtils();
    }

    /**
     * Local Database Requests: Flow 1 - Select Searches
     */
    @Override
    public void loadSearches() {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        DBManager.getDB().runInTransaction(new Runnable() {

                            @Override
                            public void run() {

                                try {
                                    setSearchHistory(new ArrayList<>(DBManager.getDB().searchDao().getAll()));
                                    loadSearchHistory();

                                } catch (Exception e) {
                                    mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                            TAG + "Failed to loadSearches " + e.getMessage());
                                }

                            }

                        });

                    } catch (Exception e) {
                        mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                TAG + "Failed to loadSearches " + e.getMessage());
                    }

                }
            }).start();

        } catch (Exception e) {
            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to loadSearches " + e.getMessage());
        }
    }

    @Override
    public void validateSelectedSearches(LDBSearch search, boolean isChecked) {
        try {
            CompareSearchesContract.View view = getView();

            if (view != null) {

                if (isChecked) {

                    if (getSelectedSearches().size() < 2) {
                        getSelectedSearches().add(search);
                        view.updateSelectedItemsList(getSelectedSearches());
                        view.updateConfirmBtn();

                    } else if (getSelectedSearches().size() >= 2) {
                        view.displayMessage(R.string.compare_only_two_at_time);
                        view.unCheckSearch(search);
                        view.updateConfirmBtn();
                    }

                } else {
                    view.updateConfirmBtn();
                    removeSelectedSearch(search);
                }
            }

        } catch (NullPointerException npe) {
            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to validateSelectedSearches " + npe.getMessage());
        }
    }

    @Override
    public void removeSelectedSearch(LDBSearch search) {
        try {
            CompareSearchesContract.View view = getView();

            if (view != null) {
                BasicValidators validationUtils = new BasicValidators();
                int idxSearchRemove = -1;

                for (int i = 0; i < getSelectedSearches().size(); i++) {
                    LDBSearch selectedSearch = getSelectedSearches().get(i);

                    if (validationUtils.isSameSearch(search, selectedSearch) == 1) {
                        idxSearchRemove = i;
                    }
                }

                if (idxSearchRemove != -1) {
                    getSelectedSearches().remove(idxSearchRemove);
                    view.updateSelectedItemsList(getSelectedSearches());
                }
            }

        } catch (NullPointerException npe) {
            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to removeSelectedSearch " + npe.getMessage());
        }
    }

    /**
     * Local Database Requests: Flow 2 - Compare Searches
     */
    @Override
    public void loadReportsSelectedSearches(final LDBSearch searchA, final LDBSearch searchB) {
        try {
            final CompareSearchesContract.View view = getView();

            if (view != null) {
                mSearchResultsPresenterA = new SearchResultsPresenter();
                mSearchResultsPresenterB = new SearchResultsPresenter();

                tryLoadCityIndicatorsCache(view, searchA, searchB);
                tryLoadInvestmentsExpensesCache(view, searchA, searchB);
            } else {
                mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                        TAG + "Failed to loadReportsSelectedSearches");
            }

        } catch (Exception e) {
            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to loadReportsSelectedSearches " + e.getMessage());
        }
    }

    @Override
    public void tryLoadCityIndicatorsCache(final CompareSearchesContract.View view,
                                           final LDBSearch searchA, final LDBSearch searchB) {
        try {
            mSearchResultsPresenterA.loadCityIndicatorsFromCache(
                searchA.getSearchId(),
                new DBQueryContract() {

                @Override
                public void gotDataFromCache(final boolean gotDataA) {

                    mSearchResultsPresenterB.loadCityIndicatorsFromCache(
                        searchB.getSearchId(),
                        new DBQueryContract() {

                        @Override
                        public void gotDataFromCache(final boolean gotDataB) {
                            final Handler handler = new Handler(Looper.getMainLooper());
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    // Try to load the components that rely on the gathered data
                                    try {

                                        if (view != null) {
                                            view.initLoadValueCitizenDay(
                                                searchA,
                                                searchB,
                                                mSearchResultsPresenterA.getValueCitizenDay(),
                                                mSearchResultsPresenterB.getValueCitizenDay());

                                            view.initLoadValueCitizenYear(
                                                searchA,
                                                searchB,
                                                mSearchResultsPresenterA.getValueCitizenYear(),
                                                mSearchResultsPresenterB.getValueCitizenYear());

                                            view.initLoadValueInvestedOwnCity(
                                                searchA,
                                                searchB,
                                                mSearchResultsPresenterA.getValueInvestedCityYearlyCitizenValue(),
                                                mSearchResultsPresenterB.getValueInvestedCityYearlyCitizenValue());

                                            view.initLoadValueInvestedOtherSources(
                                                searchA,
                                                searchB,
                                                mSearchResultsPresenterA.getValueInvestedOtherSourcesYearlyCitizenValue(),
                                                mSearchResultsPresenterB.getValueInvestedOtherSourcesYearlyCitizenValue());
                                        }

                                    } catch (Exception e) {
                                        mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                                TAG + "Failed to tryLoadCityIndicatorsCache " + e.getMessage());
                                    }

                                }
                            }, ConstantUtils.TIMEOUT_LOAD_COMPONENTS);
                        }
                    });
                }
            });

        } catch (Exception e) {
            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to tryLoadCityIndicatorsCache " + e.getMessage());
        }
    }

    @Override
    public void tryLoadInvestmentsExpensesCache(final CompareSearchesContract.View view,
                                                final LDBSearch searchA, final LDBSearch searchB) {

        try {
            mSearchResultsPresenterA.loadRevenueDeclaredExpensesFromCache(
                searchA.getSearchId(),
                new DBQueryContract() {

                @Override
                public void gotDataFromCache(final boolean gotDataA) {

                    mSearchResultsPresenterB.loadRevenueDeclaredExpensesFromCache(
                         searchB.getSearchId(),
                         new DBQueryContract() {

                         @Override
                         public void gotDataFromCache(final boolean gotDataB) {
                             final Handler handler = new Handler(Looper.getMainLooper());
                             handler.postDelayed(new Runnable() {

                                 @Override
                                 public void run() {

                                     // Try to load the components that rely on the gathered data
                                     try {

                                         if (view != null) {
                                             view.onShowProgress(false);
                                             view.initLoadTotalInvestments(
                                                 searchA,
                                                 searchB,
                                                 mSearchResultsPresenterA.getDatasetInvestmentsCityPH(),
                                                 mSearchResultsPresenterB.getDatasetInvestmentsCityPH(),
                                                 mSearchResultsPresenterA.getLabelsInvestmentsCityPH(mContext.getResources()),
                                                 mSearchResultsPresenterB.getLabelsInvestmentsCityPH(mContext.getResources()));

                                             view.initLoadExpenses(
                                                 searchA,
                                                 searchB,
                                                 mSearchResultsPresenterA.getDeclaredExpenses(),
                                                 mSearchResultsPresenterB.getDeclaredExpenses());
                                         }

                                     } catch (Exception e) {
                                         mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                                 TAG + "Failed to tryLoadInvestmentsExpensesCache " + e.getMessage());
                                     }

                                 }
                             }, ConstantUtils.TIMEOUT_LOAD_COMPONENTS);
                         }
                    });
                }
            });

        } catch (Exception e) {
            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to tryLoadInvestmentsExpensesCache " + e.getMessage());
        }
    }

    @Override
    public void tryLoadRankingCache(CompareSearchesContract.View view, LDBSearch searchA, LDBSearch searchB) {
        // There is no comparison of the ranking values.
    }

    /**
     * Getters and Setters
     */
    private void setSearchHistory(ArrayList<LDBSearch> history) {
        mLDBSearchHistory = history;
    }

    public ArrayList<LDBSearch> getSearchHistory() {
        return mLDBSearchHistory;
    }

    public void setSelectedItems(ArrayList<LDBSearch> searches) {
        mSelectedSearches = searches;
    }

    public ArrayList<LDBSearch> getSelectedSearches() {
        return mSelectedSearches;
    }

    private void loadSearchHistory() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {

            @Override
            public void run() {
                CompareSearchesContract.View view = getView();

                if (view != null) {
                    view.onLoadSearchHistory(getSearchHistory());
                } else {
                    mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                            TAG + "Failed to loadSearchHistory");
                }
            }

        });
    }

}