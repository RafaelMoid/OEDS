package br.com.eadfiocruzpe.Views.Components;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Contracts.ComponentVerticalSliderIndicatorContract;
import br.com.eadfiocruzpe.Contracts.ComponentFloatingActionButtonsContract;
import br.com.eadfiocruzpe.Contracts.CToolbarCallbackContract;
import br.com.eadfiocruzpe.Utils.ImageUtils;
import br.com.eadfiocruzpe.Utils.LogUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;
import br.com.eadfiocruzpe.Views.Validators.BasicValidators;

public class ComponentToolbar {

    private static final String TAG = "CToolbar";

    public static final int TOOLBAR_STYLE_LIGHT = 1;
    public static final int TOOLBAR_STYLE_DARK = 2;
    public static final int TOOLBAR_STYLE_TRANSPARENT = 3;

    public static final int IDX_MAIN_TOOLBAR_ITEM_PAGE_TITLE = 1;
    public static final int IDX_MAIN_TOOLBAR_ITEM_PAGE_TITLE_COMPARISON = 2;
    private static final int IDX_MAIN_TOOLBAR_ITEM_PAGE_SEARCH_INPUT = 3;
    public static final int IDX_MAIN_TOOLBAR_ITEM_PAGE_SEARCH_DESCRIPTION = 4;

    private Context mContext;
    private android.support.v7.widget.Toolbar mToolbar;
    private CToolbarCallbackContract mCallback;

    private LinearLayout mSearchDescription;
    private View mPageTitleSmContainer;
    private TextView mPageTitleSm;
    private TextView mPageTitle;
    private ImageView mPageIcon;
    private View mPageTitleComparison;
    private TextView mPageTitleComparisonSearchA;
    private TextView mPageTitleComparisonSearchB;
    private EditText mSearchInput;
    private TextView mSearchDescriptionCityView;
    private ImageView mSearchDescriptionStateFlag;
    private TextView mSearchDescriptionStateView;
    private TextView mSearchDescriptionTimeRangeView;
    private ImageView mSearchBtn;
    private TextView mBackBtn;

    private View mBottomSeparator;

    private LogUtils mLogUtils = new LogUtils();
    private ComponentVerticalSliderIndicator mVerticalSliderIndicator;
    private ComponentFloatingActionButtons mFloatingActionButtons;

    // Initialization
    public ComponentToolbar(Context context, android.support.v7.widget.Toolbar toolbar) {
        mContext = context;
        mToolbar = toolbar;

        bindViews();
        initListeners();
    }

    private void bindViews() {
        try {
            mPageTitle = mToolbar.findViewById(R.id.toolbar_page_title);
            mPageTitleSmContainer = mToolbar.findViewById(R.id.toolbar_page_title_sm_container);
            mPageTitleSm = mToolbar.findViewById(R.id.toolbar_page_title_sm);
            mPageIcon = mToolbar.findViewById(R.id.toolbar_page_icon);
            mPageTitleComparison = mToolbar.findViewById(R.id.toolbar_page_title_comparison_searches);
            mPageTitleComparisonSearchA = mToolbar.findViewById(R.id.toolbar_page_title_comparison_search_a);
            mPageTitleComparisonSearchB = mToolbar.findViewById(R.id.toolbar_page_title_comparison_search_b);
            mSearchInput = mToolbar.findViewById(R.id.toolbar_search_input);
            mSearchDescription = mToolbar.findViewById(R.id.toolbar_search_description);
            mSearchDescriptionCityView = mToolbar.findViewById(R.id.toolbar_search_description_city);
            mSearchDescriptionStateFlag = mToolbar.findViewById(R.id.toolbar_search_description_state_flag);
            mSearchDescriptionStateView = mToolbar.findViewById(R.id.toolbar_search_description_state);
            mSearchDescriptionTimeRangeView = mToolbar.findViewById(R.id.toolbar_search_description_time_range);
            mSearchBtn = mToolbar.findViewById(R.id.toolbar_search_btn);
            mBackBtn = mToolbar.findViewById(R.id.toolbar_back_btn);
            mBottomSeparator = mToolbar.findViewById(R.id.toolbar_bottom_separator);

            mVerticalSliderIndicator = new ComponentVerticalSliderIndicator(
                    (RelativeLayout) mToolbar.findViewById(R.id.vertical_slider_indicator),
                    mVSICallback);

            mFloatingActionButtons = new ComponentFloatingActionButtons(
                    (LinearLayout) mToolbar.findViewById(R.id.component_floating_action_buttons_container),
                    null);

        } catch (NullPointerException npe) {
            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to bindViews");
        }
    }

    private void initListeners() {
        mSearchInput.setOnClickListener(mSearchCallback);
        mSearchBtn.setOnClickListener(mSearchCallback);
        mBackBtn.setOnClickListener(mBackBtnPressedListener);
    }

    /**
     * Events
     */
    public void openSearchDialogFromToolbar() {

        if (mCallback != null) {
            mCallback.onSearchButtonClicked();
        }
    }

    private View.OnClickListener mSearchCallback = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                mCallback.onSearchButtonClicked();
            } catch (NullPointerException npe) {
                mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                        TAG + "Warning: failed to trigger on click callback for the toolbar search button");
            }
        }
    };

    private View.OnClickListener mBackBtnPressedListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                mCallback.onBackBtnClicked();
            } catch (NullPointerException npe) {
                mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                        TAG + "Warning: failed to trigger on click callback for the toolbar back button");
            }
        }
    };

    private ComponentVerticalSliderIndicatorContract mVSICallback =
            new ComponentVerticalSliderIndicatorContract() {
                @Override
                public void onVSIFinishedSliding(float percentage) {

                }
            };

    /**
     * Getters and Setters
     */
    public void setMainToolbarItem(int item) {

        switch (item) {

            case IDX_MAIN_TOOLBAR_ITEM_PAGE_TITLE: {
                mPageTitle.setVisibility(View.VISIBLE);
                mPageTitleSm.setVisibility(View.GONE);
                mPageTitleComparison.setVisibility(View.GONE);
                mSearchInput.setVisibility(View.GONE);
                mSearchDescription.setVisibility(View.GONE);
                mVerticalSliderIndicator.show(false);
                mFloatingActionButtons.setShowFavoriteBtn(true);

                break;
            }

            case IDX_MAIN_TOOLBAR_ITEM_PAGE_TITLE_COMPARISON: {
                mPageTitle.setVisibility(View.GONE);
                mPageTitleSm.setVisibility(View.VISIBLE);
                mPageTitleComparison.setVisibility(View.VISIBLE);
                mSearchDescription.setVisibility(View.GONE);
                mVerticalSliderIndicator.show(true);
                setShowFloatingActionButtons(true);
                mFloatingActionButtons.setShowFavoriteBtn(false);

                break;
            }

            case IDX_MAIN_TOOLBAR_ITEM_PAGE_SEARCH_INPUT: {
                mPageTitle.setVisibility(View.GONE);
                mPageTitleSm.setVisibility(View.GONE);
                mPageTitleComparison.setVisibility(View.GONE);
                mSearchInput.setVisibility(View.VISIBLE);
                mSearchDescription.setVisibility(View.GONE);
                mVerticalSliderIndicator.show(false);
                mFloatingActionButtons.setShowFavoriteBtn(true);

                break;
            }

            case IDX_MAIN_TOOLBAR_ITEM_PAGE_SEARCH_DESCRIPTION: {
                mPageTitle.setVisibility(View.GONE);
                mPageTitleSm.setVisibility(View.GONE);
                mPageTitleComparison.setVisibility(View.GONE);
                mSearchInput.setVisibility(View.GONE);
                mSearchDescription.setVisibility(View.VISIBLE);
                mVerticalSliderIndicator.show(true);
                mFloatingActionButtons.setShowFavoriteBtn(true);

                break;
            }

            default: {
                mPageTitle.setVisibility(View.VISIBLE);
                mPageTitleSm.setVisibility(View.GONE);
                mPageTitleComparison.setVisibility(View.GONE);
                mSearchInput.setVisibility(View.GONE);
                mSearchDescription.setVisibility(View.GONE);
                mVerticalSliderIndicator.show(false);
                mFloatingActionButtons.setShowFavoriteBtn(true);

                break;
            }
        }
    }

    public void setToolbarStyle(int toolbarStyleIdx) {

        switch(toolbarStyleIdx) {

            case TOOLBAR_STYLE_LIGHT: {
                setToolbarTitleColor(R.color.color_black_1);
                setToolbarBackground(
                        ContextCompat.getDrawable(mContext.getApplicationContext(), R.drawable.background_gray),
                        R.color.color_white);
                setBackBtnBackground(
                        ContextCompat.getDrawable(mContext.getApplicationContext(), R.drawable.background_circle_white_blackish_borders),
                        R.color.color_white);
                mBackBtn.setTextColor(ContextCompat.getColor(mContext.getApplicationContext(), R.color.color_black_3));
                mBottomSeparator.setVisibility(View.VISIBLE);
                break;
            }

            case TOOLBAR_STYLE_DARK: {
                setToolbarTitleColor(R.color.color_white_1);
                setToolbarBackground(
                        ContextCompat.getDrawable(mContext.getApplicationContext(), R.drawable.background_green_gradient_1),
                        R.color.color_green_blue_4);
                setBackBtnBackground(
                        ContextCompat.getDrawable(mContext.getApplicationContext(), R.drawable.background_circle_transparent_white_borders),
                        R.color.color_green_blue_4);
                mBackBtn.setTextColor(ContextCompat.getColor(mContext.getApplicationContext(), R.color.color_white));
                mBottomSeparator.setVisibility(View.GONE);
                break;
            }

            case TOOLBAR_STYLE_TRANSPARENT: {
                setToolbarTitleColor(R.color.color_white_1);
                setToolbarBackground(
                        ContextCompat.getDrawable(mContext.getApplicationContext(), R.drawable.background_rectangle_transparent_no_borders),
                        R.color.color_green_blue_4);
                setBackBtnBackground(
                        ContextCompat.getDrawable(mContext.getApplicationContext(), R.drawable.background_circle_transparent_white_borders),
                        R.color.color_green_blue_4);
                mBackBtn.setTextColor(ContextCompat.getColor(mContext.getApplicationContext(), R.color.color_white));
                mBottomSeparator.setVisibility(View.VISIBLE);
                break;
            }
        }
    }

    public void setCallbackToolbarButtons(CToolbarCallbackContract callback) {
        mCallback = callback;
    }

    public void setCallbackToolbarFloatingActionButtons(ComponentFloatingActionButtonsContract callback) {
        try {

            if (mFloatingActionButtons != null) {
                mFloatingActionButtons.setCallback(callback);
            }

        } catch (NullPointerException npe) {
            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to setCallbackToolbarFloatingActionButtons");
        }
    }

    public void setPageTitle(String pageTitle) {
        mPageTitle.setText(pageTitle != null? pageTitle : "");
        mPageTitleSm.setText(pageTitle != null? pageTitle : "");
    }

    public void setPageTitle(String descSearchA, String descSearchB) {

        if (mPageTitleComparison != null) {

            if (descSearchA != null && descSearchB != null) {
                mPageTitleComparisonSearchA.setText(descSearchA);
                mPageTitleComparisonSearchB.setText(descSearchB);
            }
        }
    }

    public void setToolbarTitleColor(int titleColor) {
        mPageTitle.setTextColor(ContextCompat.getColor(mContext, titleColor));
        mPageTitleSm.setTextColor(ContextCompat.getColor(mContext, titleColor));
    }

    public void setPageIcon(boolean showIcon, Drawable pageDrawable) {

        if (mPageIcon != null) {
            mPageTitleSmContainer.setVisibility(showIcon? View.VISIBLE : View.GONE);
            mPageIcon.setVisibility(showIcon? View.VISIBLE : View.GONE);

            if (showIcon) {
                mPageIcon.setImageDrawable(pageDrawable);
            }
        }
    }

    private void setToolbarBackground(Drawable background, int emergencyColor) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mToolbar.setBackground(background);
        } else {
            mToolbar.setBackgroundColor(emergencyColor);
        }
    }

    private void setBackBtnBackground(Drawable background, int emergencyColor) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mBackBtn.setBackground(background);
        } else {
            mBackBtn.setBackgroundColor(emergencyColor);
        }
    }

    public void setSearchDescription(String city, String state, int stateFlag, String timeRange) {
        BasicValidators basicValidators = new BasicValidators();

        if (basicValidators.isValidString(city) && basicValidators.isValidString(timeRange)) {
            mSearchDescriptionCityView.setText(city);
            ImageUtils.loadImage(mContext, stateFlag, mSearchDescriptionStateFlag);
            mSearchDescriptionStateView.setText(state);
            mSearchDescriptionTimeRangeView.setText(timeRange);
        }
    }

    public void setShowSearchBtn(boolean show) {
        mSearchBtn.setVisibility(show? View.VISIBLE : View.GONE);
    }

    public void setShowBackBtn(boolean show) {
        mBackBtn.setVisibility(show? View.VISIBLE : View.GONE);
    }

    public void setShowFloatingActionButtons(boolean show) {
        mFloatingActionButtons.show(show);
    }

    public void setYPageScrollerIndicator(float yPercentageTop) {

        if (mVerticalSliderIndicator != null) {
            mVerticalSliderIndicator.setPositionSliderIndicator(yPercentageTop);
        }
    }

    public void setIsFavoriteSearch(boolean isFavorite) {

        if (mFloatingActionButtons != null) {
            mFloatingActionButtons.setIsFavorite(mContext.getResources(), isFavorite);
        }
    }

}