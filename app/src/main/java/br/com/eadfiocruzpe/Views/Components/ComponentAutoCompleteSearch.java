package br.com.eadfiocruzpe.Views.Components;

import java.util.ArrayList;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;

import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Contracts.ComponentAutoCompleteSearchPresenterContract;
import br.com.eadfiocruzpe.Contracts.SearchSuggestionsAdapterContract;
import br.com.eadfiocruzpe.Contracts.ComponentAutoCompleteSearchContract;
import br.com.eadfiocruzpe.Utils.ConnectivityUtils;
import br.com.eadfiocruzpe.Utils.ConstantUtils;
import br.com.eadfiocruzpe.Utils.KeyboardUtils;
import br.com.eadfiocruzpe.Utils.LogUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;
import br.com.eadfiocruzpe.Presenters.ComponentAutoCompleteSearchPresenter;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.GooglePlacesCity;
import br.com.eadfiocruzpe.Views.Adapters.SearchSuggestionsAdapter;

public class ComponentAutoCompleteSearch {

    private final String TAG = "CACSearch";

    private LinearLayout mRootLayout;
    private SearchView mSearchView;
    private TextView mSearchViewLocker;
    private TextView mBtnAdvancedSearch;
    private RecyclerView mRvSearchSuggestions;

    private Context mContext;
    private LogUtils mLogUtils;

    private ComponentAutoCompleteSearchContract mCallback;
    private SearchSuggestionsAdapter mRvAdapterSearchSuggestions;
    private ComponentAutoCompleteSearchPresenter mPresenter;
    private GeoDataClient mGeoDataClient;
    private CountDownTimer mTypeEndCountDown;

    public ComponentAutoCompleteSearch(LinearLayout rootLayout,
                                       ComponentAutoCompleteSearchContract callback,
                                       Context context) {
        mCallback = callback;
        mContext = context;
        mGeoDataClient = Places.getGeoDataClient(mContext);

        initUI(rootLayout);
        initTools();
        initEvents();
        initData();
    }

    /**
     * Initialization
     */
    private void initUI(LinearLayout rootLayout) {
        bindViews(rootLayout);
        setIsUILocked(false, "");
        initRvSearchSuggestions();
    }

    private void bindViews(LinearLayout rootLayout) {
        try {
            mRootLayout = rootLayout;

            mSearchView = mRootLayout.findViewById(
                    R.id.component_auto_complete_search_input);

            mSearchViewLocker = mRootLayout.findViewById(
                    R.id.component_auto_complete_search_input_locker);

            mRvSearchSuggestions = mRootLayout.findViewById(
                    R.id.component_auto_complete_search_suggestions_rv);

            mBtnAdvancedSearch = mRootLayout.findViewById(
                    R.id.component_auto_complete_search_btn_advanced_search);

            show(false);

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to bindViews");
        }
    }

    private void initRvSearchSuggestions() {
        try {
            mRvSearchSuggestions.setVisibility(View.GONE);
            mRvSearchSuggestions.setLayoutManager(new LinearLayoutManager(mContext));
            mRvAdapterSearchSuggestions = new SearchSuggestionsAdapter(
                    mCallbackSearchSuggestions,
                    new ArrayList<GooglePlacesCity>(),
                    mContext.getResources());
            mRvSearchSuggestions.setAdapter(mRvAdapterSearchSuggestions);

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to initRvSearchSuggestions");
        }
    }

    private void initTools() {
        mLogUtils = new LogUtils();
    }

    private void initEvents() {
        mSearchView.setOnClickListener(mOnClickSearchView);
        mSearchView.setOnQueryTextListener(mOnMakeSearchActionSelected);
        mSearchView.setOnCloseListener(mOnCloseListener);
        mBtnAdvancedSearch.setOnClickListener(mOnClickAdvancedSearchBtn);
    }

    /**
     * Events
     */
    private final View.OnClickListener mOnClickSearchView = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (isShowing()) {
                mSearchView.setIconified(false);
                mSearchView.setQueryHint(mContext.getString(R.string.component_curiosity_hint_search_input));
            }
        }
    };

    private final SearchView.OnQueryTextListener mOnMakeSearchActionSelected = new SearchView.OnQueryTextListener() {

        @Override
        public boolean onQueryTextSubmit(String s) {

            if (!isIsUILocked() && ConnectivityUtils.isConnected(mContext.getApplicationContext())) {
                mPresenter.getAutoCompletePredictions(s);
            }

            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {

            if (!isIsUILocked() && ConnectivityUtils.isConnected(mContext.getApplicationContext())) {
                mPresenter.getAutoCompletePredictions(s);
            }

            waitDismissKeyboard();

            return false;
        }
    };

    private void waitDismissKeyboard() {
        try {

            if (mTypeEndCountDown != null) {
                mTypeEndCountDown.cancel();
            }

            mTypeEndCountDown = new CountDownTimer(ConstantUtils.TIMEOUT_DISMISS_KEYBOARD, 100) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    KeyboardUtils.dismissKeyboard(mContext, mSearchView);
                }
            }.start();

        } catch (Exception e) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to waitDismissKeyboard " + e.getMessage());
        }
    }

    private final SearchView.OnCloseListener mOnCloseListener = new SearchView.OnCloseListener() {
        @Override
        public boolean onClose() {
            mRvAdapterSearchSuggestions.cleanSuggestions();

            return false;
        }
    };

    private final ComponentAutoCompleteSearchPresenterContract mCallbackAutoCompletePresenter =
        new ComponentAutoCompleteSearchPresenterContract() {

            @Override
            public GeoDataClient getGeoDataApiClient() {
                return mGeoDataClient;
            }

            @Override
            public void updateListSuggestions(GooglePlacesCity suggestedCity) {
                try {
                    mRvSearchSuggestions.setVisibility(View.VISIBLE);
                    mRvAdapterSearchSuggestions.updateList(suggestedCity);

                } catch (Exception e) {
                    mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                            TAG + "Failed to updateListSuggestions " + e.getMessage());
                }
            }

            @Override
            public void userAnotherSearchOption() {
                openAdvancedSearch();
            }

        };

    private final SearchSuggestionsAdapterContract.Client mCallbackSearchSuggestions =
        new SearchSuggestionsAdapterContract.Client() {

        @Override
        public void onClickSelectedSuggestion(final GooglePlacesCity suggestedCity) {

            try {
                mSearchView.setFocusable(false);
                mSearchView.setQuery(mContext.getString(
                        R.string.component_auto_complete_search_suggestion_format,
                        suggestedCity.getCity(),
                        suggestedCity.getUf().toUpperCase()), false);

                mCallback.setSearchWithSuggestion(suggestedCity);

                KeyboardUtils.dismissKeyboard(mContext, mSearchView);

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        new CountDownTimer(ConstantUtils.TIMEOUT_PRESENT_DASHBOARD,
                                ConstantUtils.TIMEOUT_SHOW_KEYBOARD_AUTO_COMPLETE_SEARCH_ON_SEARCH_DIALOG) {

                            public void onTick(long millisUntilFinished) {}

                            public void onFinish() {
                                mRvSearchSuggestions.setVisibility(View.GONE);
                            }

                        }.start();
                    }
                });
            } catch (Exception e) {
                mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                        TAG + "Failed on mCallbackSearchSuggestions " + e.getMessage());
            }
        }
    };

    private final View.OnClickListener mOnClickAdvancedSearchBtn = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            openAdvancedSearch();
        }
    };

    private void openAdvancedSearch() {

        if (mCallback != null) {
            mCallback.onAdvancedSearchBtnClicked();
        }
    }

    public void lockUI(boolean lock, String msg) {
        try {
            setIsUILocked(lock, msg);

            mRvSearchSuggestions.setVisibility(lock? View.GONE: View.VISIBLE );
            mSearchView.setEnabled(!lock);
            mSearchView.setFocusable(!lock);
            mBtnAdvancedSearch.setAlpha(lock? ConstantUtils.ALPHA_DISABLED_BUTTON: 1f);
            mBtnAdvancedSearch.setEnabled(!lock);

            if (lock) {
                KeyboardUtils.dismissKeyboard(mContext, mSearchView);
            }

            mCallback.lockUI();

        } catch(NullPointerException ignored) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to lockUI");
        }
    }

    /**
     * Data
     */
    private void initData() {
        try {
            mPresenter = new ComponentAutoCompleteSearchPresenter(mCallbackAutoCompletePresenter);

        } catch (Exception e) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to initData: " + e.getMessage());
        }
    }

    /**
     * Setters and Getters
     */
    public void show(boolean showComponent) {

        if (mRootLayout != null) {
            mRootLayout.setVisibility(showComponent ? View.VISIBLE : View.GONE);
        }
    }

    private boolean isShowing() {
        return mRootLayout.getVisibility() == View.VISIBLE;
    }

    public void showBtnAdvancedSearch(boolean showBtn) {

        if (mBtnAdvancedSearch != null) {
            mBtnAdvancedSearch.setVisibility(showBtn? View.VISIBLE : View.GONE);
        }
    }

    public void setMsgBtnAdvancedSearch(String msg) {
        try {
            mBtnAdvancedSearch.setText(msg);

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to setMsgBtnAdvancedSearch");
        }
    }

    public void setHintSearchView(final String hint) {
        try {
            mSearchView.setIconified(false);
            mSearchView.setQueryHint(hint);

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to setHintSearchView");
        }
    }

    public void setSearchTerms(final String searchTerms) {
        try {
            mSearchView.setIconified(false);
            mSearchView.setQuery(searchTerms, false);

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to setSearchTerms");
        }
    }

    private void setIsUILocked(boolean locked, String msg) {
        try {
            mSearchView.setVisibility(locked? View.GONE : View.VISIBLE);
            mSearchViewLocker.setVisibility(locked? View.VISIBLE: View.GONE);
            mSearchViewLocker.setText(msg);

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to setSearchTerms");
        }
    }

    private boolean isIsUILocked() {
        return mSearchViewLocker.getVisibility() == View.VISIBLE;
    }

}