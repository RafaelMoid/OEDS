package br.com.eadfiocruzpe.Presenters;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import br.com.eadfiocruzpe.Contracts.SearchHistoryFavoritesContract;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBSearch;
import br.com.eadfiocruzpe.Persistence.DBManager;
import br.com.eadfiocruzpe.Utils.LogUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;

public class SearchHistoryFavoritesPresenter extends BasePresenter<SearchHistoryFavoritesContract.View> implements
        SearchHistoryFavoritesContract.Presenter {

    private static final String TAG = "SearchHFPresenter";
    public static final int IDX_LIST_SEARCH_HISTORY = 1;
    public static final int IDX_LIST_FAVORITE = 2;

    private ArrayList<LDBSearch> mLDBSearchHistory = new ArrayList<>();
    private ArrayList<LDBSearch> mLDBFavoriteSearches = new ArrayList<>();
    private LogUtils mLogUtils;
    private int mSelectedList = 1;

    public SearchHistoryFavoritesPresenter(Context context) {
        mContext = context;
        mLogUtils = new LogUtils();
    }

    /**
     * Local Database Requests
     */
    @Override
    public void loadSearches(final int selectedList) {
        setSelectedList(selectedList);

        try {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        DBManager.getDB().runInTransaction(new Runnable() {

                            @Override
                            public void run() {

                                try {

                                    switch (mSelectedList) {

                                        case IDX_LIST_SEARCH_HISTORY:
                                            setSearchHistory(new ArrayList<>(DBManager.getDB().searchDao().getAll()));
                                            loadSearchHistory();
                                            break;

                                        case IDX_LIST_FAVORITE:
                                            setFavoriteSearches(new ArrayList<>(DBManager.getDB().searchDao().getFavorite()));
                                            loadFavoriteSearches();
                                            break;
                                    }

                                } catch (Exception e) {
                                    mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                            TAG + "Failed to loadSearchHistory " + e.getMessage());
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
                                    loadSearches(getSelectedList());

                                } catch (Exception e) {
                                    mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                            TAG + "Failed to updateSearch " + e.getMessage());
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

    @Override
    public void deleteSearch(final LDBSearch search) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        DBManager.getDB().runInTransaction(new Runnable() {

                            @Override
                            public void run() {

                                try {
                                    DBManager.getDB().searchDao().delete(search);
                                    loadSearches(getSelectedList());

                                } catch (Exception e) {
                                    mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                            TAG + "Failed to deleteSearch " + e.getMessage());
                                }

                            }
                        });

                    } catch (Exception e) {
                        mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                TAG + "Failed to deleteSearch " + e.getMessage());
                    }

                }
            }).start();

        } catch (Exception e) {
            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to deleteSearch " + e.getMessage());
        }
    }

    /**
     * Getters and Setters
     */
    private void setSelectedList(int selectedList) {
        mSelectedList = selectedList;
    }

    public int getSelectedList() {
        return mSelectedList;
    }

    private void setSearchHistory(ArrayList<LDBSearch> history) {
        mLDBSearchHistory = history;
    }

    private ArrayList<LDBSearch> getSearchHistory() {
        return mLDBSearchHistory;
    }

    private void loadSearchHistory() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {

            @Override
            public void run() {
                SearchHistoryFavoritesContract.View view = getView();

                if (view != null) {
                    ArrayList<LDBSearch> history = getSearchHistory();
                    Collections.reverse(history);
                    view.onLoadSearchHistory(history);
                } else {
                    mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                            TAG + "Failed to loadSearchHistory");
                }
            }

        });
    }

    private void setFavoriteSearches(ArrayList<LDBSearch> favorites) {
        mLDBFavoriteSearches = favorites;
    }

    private ArrayList<LDBSearch> getFavoriteSearches() {
        return mLDBFavoriteSearches;
    }

    private void loadFavoriteSearches() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {

            @Override
            public void run() {
                SearchHistoryFavoritesContract.View view = getView();

                if (view != null) {
                    ArrayList<LDBSearch> favorites = getFavoriteSearches();
                    Collections.reverse(favorites);
                    view.onLoadFavoriteSearches(favorites);
                } else {
                    mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                            TAG + "Failed to loadFavoriteSearches");
                }
            }

        });
    }

}