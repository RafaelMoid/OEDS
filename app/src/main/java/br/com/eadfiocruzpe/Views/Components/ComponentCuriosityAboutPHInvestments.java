package br.com.eadfiocruzpe.Views.Components;

import android.content.Context;
import android.content.res.Resources;
import android.os.CountDownTimer;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.FrameLayout;

import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Contracts.ComponentCuriosityAboutPHInvestmentsContract;
import br.com.eadfiocruzpe.Utils.ConstantUtils;
import br.com.eadfiocruzpe.Utils.KeyboardUtils;
import br.com.eadfiocruzpe.Utils.LogUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;

public class ComponentCuriosityAboutPHInvestments {

    private final String TAG = "CCuriosAboPHInvestments";

    public FrameLayout rootLayout;
    private SearchView mSearchView;
    private LogUtils mLogUtils;
    private Resources mResources;
    private CountDownTimer mTypeEndCountDown;

    private ComponentCuriosityAboutPHInvestmentsContract mCallback;
    private Context mContext;

    public ComponentCuriosityAboutPHInvestments(Context context,
                                                FrameLayout rootLayout,
                                                ComponentCuriosityAboutPHInvestmentsContract callback,
                                                Resources resources) {
        mContext = context;
        mCallback = callback;
        mResources = resources;
        bindViews(rootLayout);
        initUI();
        initTools();
        initEvents();
    }

    /**
     * Initialization
     */
    private void bindViews(FrameLayout rootLayout) {
        try {
            this.rootLayout = rootLayout;
            mSearchView = this.rootLayout.findViewById(R.id.component_curiosity_about_ph_search_input);

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to initUI");
        }
    }

    private void initUI() {
        try {
            show(false);

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to initUI");
        }
    }

    private void initTools() {
        mLogUtils = new LogUtils();
    }

    private void initEvents() {
        mSearchView.setOnClickListener(mOnClickSearchBox);
        mSearchView.setOnQueryTextListener(mOnMakeSearchActionSelected);
    }

    /**
     * Events
     */
    private final View.OnClickListener mOnClickSearchBox = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (isShowing()) {
                mSearchView.setIconified(false);
                mSearchView.setQueryHint(mResources.getString(R.string.component_curiosity_hint_search_input));
            }
        }
    };

    private final SearchView.OnQueryTextListener mOnMakeSearchActionSelected = new SearchView.OnQueryTextListener() {

        @Override
        public boolean onQueryTextSubmit(String s) {

            try {

                if (mCallback != null) {
                    mCallback.onSearchInputInitiated(s);
                }
            } catch (Exception ignored) {}

            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {

            try {

                if (s.length() > ConstantUtils.MAX_NUM_CHARS_TRIGGER_SEARCH_INITIAL_PAGE) {

                    if (mCallback != null) {
                        mCallback.onSearchInputInitiated(s);
                    }
                } else {
                    mLogUtils.logMessage(TypeUtils.LogMsgType.REGULAR,
                            TAG + "User is typing a search term ... " + s);
                }

                waitDismissKeyboard();
            } catch (Exception ignored) {}

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

    /**
     * Getters and Setters
     */
    public void show(boolean showComponent) {

        if (rootLayout != null) {
            rootLayout.setVisibility(showComponent ? View.VISIBLE : View.GONE);
        }
    }

    private boolean isShowing() {
        return rootLayout.getVisibility() == View.VISIBLE;
    }

    public void setSearch(String searchTerm) {

        if (mSearchView != null) {
            mSearchView.setQuery(searchTerm, false);
        }
    }

    public SearchView getSearchView() {
        return mSearchView;
    }

}