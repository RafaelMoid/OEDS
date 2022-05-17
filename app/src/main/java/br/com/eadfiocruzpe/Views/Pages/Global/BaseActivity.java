package br.com.eadfiocruzpe.Views.Pages.Global;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.annotation.SuppressLint;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Contracts.ComponentFloatingActionButtonsContract;
import br.com.eadfiocruzpe.Contracts.CToolbarCallbackContract;
import br.com.eadfiocruzpe.Utils.ConstantUtils;
import br.com.eadfiocruzpe.Utils.LogUtils;
import br.com.eadfiocruzpe.Utils.SnackbarUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;
import br.com.eadfiocruzpe.Views.Components.ComponentToolbar;
import br.com.eadfiocruzpe.Views.Validators.BasicValidators;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    private ComponentToolbar mToolbarComponent;
    private CoordinatorLayout mRootView;
    private View mProgressView;
    public LogUtils logUtils = new LogUtils();

    /**
     * Initialization
     */
    public void initMainView(CoordinatorLayout pageRootView) {
        mRootView = pageRootView;
    }

    public void initToolbar(boolean showHomeBtn, String pageTitle, Toolbar toolbar) {
        BasicValidators basicValidators = new BasicValidators();

        if (basicValidators.isNotNull(toolbar)) {
            mToolbarComponent = new ComponentToolbar(getApplicationContext(), toolbar);
            setSupportActionBar(toolbar);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(showHomeBtn);

                if (basicValidators.isNotNull(mToolbarComponent)) {
                    mToolbarComponent.setPageTitle(pageTitle);
                }
            }
        }
    }

    /**
     * Setters
     */
    public void setToolbarTitleColor(int toolbarTitleColor) {

        if (mToolbarComponent != null) {
            mToolbarComponent.setToolbarTitleColor(toolbarTitleColor);
        }
    }

    public void setMainToolbarItem(int toolbarItem) {

        if (mToolbarComponent != null) {
            mToolbarComponent.setMainToolbarItem(toolbarItem);
        }
    }

    public void setToolbarSearchDescription(String city, String state, int stateFlag, String year, String timeRange) {
        BasicValidators basicValidators = new BasicValidators();

        if (basicValidators.isValidString(city) && basicValidators.isValidString(state) &&
                basicValidators.isValidString(year) && basicValidators.isValidString(timeRange)) {
            String time = year + " - " + timeRange;

            mToolbarComponent.setSearchDescription(city, state, stateFlag, time);
        }
    }

    public void setCallbackToolbarButtons(CToolbarCallbackContract callback) {
        try {
            mToolbarComponent.setCallbackToolbarButtons(callback);

        } catch (NullPointerException npe) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Warning: failed to set a setCallbackToolbarButtons");
        }
    }

    public void setCallbackToolbarFloatingActionButtons(ComponentFloatingActionButtonsContract callback) {
        try {
            mToolbarComponent.setCallbackToolbarFloatingActionButtons(callback);
        } catch (NullPointerException npe) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Warning failed to setCallbackToolbarFloatingActionButtons");
        }
    }

    public void initProgressView(View progressView) {
        mProgressView = progressView;
    }

    public void showMessage(String message) {
        try {

            if (!message.isEmpty()) {
                final Snackbar snackbar = Snackbar.make(mRootView, message, Snackbar.LENGTH_LONG);
                SnackbarUtils.alignSnackbarTextCenter(snackbar);
                SnackbarUtils.setSnackbarTextColor(snackbar);
                SnackbarUtils.setSnackbarBackgroundColor(snackbar, ContextCompat.getColor(this, R.color.color_black_1));
                SnackbarUtils.setSnackbarHeight(snackbar, SnackbarUtils.MAX_NUM_LINES_SNACKBAR);
                snackbar.setDuration(ConstantUtils.SNACKBAR_MESSAGE_DURATION);
                snackbar.show();
            }

        } catch(NullPointerException npe) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Warning: failed to show message");
        }
    }

    public void showProgress(boolean show) {

        if (mProgressView != null) {

            if (show) {
                mProgressView.setVisibility(View.VISIBLE);
            } else {
                mProgressView.setVisibility(View.GONE);
            }
        }
    }

    public void updateToolbarTitle(String title) {

        if (mToolbarComponent != null) {
            mToolbarComponent.setPageTitle(title);
        }
    }

    public void updateToolbarTitle(String descSearchA, String descSearchB) {

        if (mToolbarComponent != null) {
            mToolbarComponent.setPageTitle(descSearchA, descSearchB);
        }
    }

    public void updateToolbarIcon(boolean showIcon, Drawable img) {

        if (mToolbarComponent != null) {
            mToolbarComponent.setPageIcon(showIcon, img);
        }
    }

    public void updateToolbarPageScrollerIndicator(float yPercentageTop) {

        if (mToolbarComponent != null) {
            mToolbarComponent.setYPageScrollerIndicator(yPercentageTop);
        }
    }

    public void setToolbarStyle(int toolbarStyle) {

        if (mToolbarComponent != null) {
            mToolbarComponent.setToolbarStyle(toolbarStyle);
        }
    }

    public void showSearchBtn(boolean show) {

        if (mToolbarComponent != null) {
            mToolbarComponent.setShowSearchBtn(show);
        }
    }

    public void showBackBtn(boolean show) {

        if (mToolbarComponent != null) {
            mToolbarComponent.setShowBackBtn(show);
        }
    }

    public void showFloatingActionButtons(boolean show) {

        if (mToolbarComponent != null) {
            mToolbarComponent.setShowFloatingActionButtons(show);
        }
    }

    public void openSearchDialogFromToolbar() {

        if (mToolbarComponent != null) {
            mToolbarComponent.openSearchDialogFromToolbar();
        }
    }

    public void setIsFavoriteSearch(boolean isFavorite) {

        if (mToolbarComponent != null) {
            mToolbarComponent.setIsFavoriteSearch(isFavorite);
        }
    }

}