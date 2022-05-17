package br.com.eadfiocruzpe.Views.Pages.Home.Dashboard;

import java.util.ArrayList;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.ButterKnife;

import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Utils.ConnectivityUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;
import br.com.eadfiocruzpe.Utils.KeyboardUtils;
import br.com.eadfiocruzpe.Utils.ConstantUtils;
import br.com.eadfiocruzpe.Utils.JsonUtils;
import br.com.eadfiocruzpe.Utils.PermissionUtils;
import br.com.eadfiocruzpe.Contracts.ShareFeatureContract;
import br.com.eadfiocruzpe.Contracts.ComparePhotographerSelectedViewsContract;
import br.com.eadfiocruzpe.Contracts.DashboardContract;
import br.com.eadfiocruzpe.Contracts.ComponentTotalInvestmentsPHContract;
import br.com.eadfiocruzpe.Contracts.ComponentCuriosityAboutPHInvestmentsContract;
import br.com.eadfiocruzpe.Contracts.ComponentFloatingActionButtonsContract;
import br.com.eadfiocruzpe.Contracts.ComponentSocialNetworkPickerContract;
import br.com.eadfiocruzpe.Contracts.FAQDialogContract;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.SharedContent;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBSearch;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBCityRankingObject;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBExpense;
import br.com.eadfiocruzpe.Presenters.DashboardPresenter;
import br.com.eadfiocruzpe.Views.Components.ComponentPhotographerSelectedViews;
import br.com.eadfiocruzpe.Views.Components.ComponentShareableContentSelector;
import br.com.eadfiocruzpe.Views.Pages.Home.Sharing.SharingActivity;
import br.com.eadfiocruzpe.Views.Validators.BasicValidators;
import br.com.eadfiocruzpe.Views.Dialogs.GeneralPurposesDialog;
import br.com.eadfiocruzpe.Views.Dialogs.FAQDialog;
import br.com.eadfiocruzpe.Views.Pages.Home.BaseFragment;
import br.com.eadfiocruzpe.Views.Pages.Home.HomeActivity;
import br.com.eadfiocruzpe.Views.Components.ComponentCityFinancialAutonomyPHInvestments;
import br.com.eadfiocruzpe.Views.Components.ComponentOtherSourcesInvestmentsCityPH;
import br.com.eadfiocruzpe.Views.Components.ComponentCityInvestmentsPHCitizenYear;
import br.com.eadfiocruzpe.Views.Components.ComponentDeclaredCityExpensesPH;
import br.com.eadfiocruzpe.Views.Components.ComponentFederalInvestmentsCityPH;
import br.com.eadfiocruzpe.Views.Components.ComponentSocialNetworkPicker;
import br.com.eadfiocruzpe.Views.Components.ComponentCuriosityAboutPHInvestments;
import br.com.eadfiocruzpe.Views.Components.ComponentCityInvestmentsPHCitizenDay;
import br.com.eadfiocruzpe.Views.Components.ComponentInvestmentsOwnCityValueCitizenYear;
import br.com.eadfiocruzpe.Views.Components.ComponentInvestmentsOtherSourcesValueCitizenYear;
import br.com.eadfiocruzpe.Views.Components.ComponentRankingCitiesByFinancialAutonomy;
import br.com.eadfiocruzpe.Views.Components.ComponentTotalInvestmentCityPH;
import br.com.eadfiocruzpe.Views.Components.ComponentStateInvestmentsCityPH;

public class DashboardFragment extends BaseFragment implements
        DashboardContract.View,
        ShareFeatureContract {

    private final String TAG = "DashboardFragment";

    @BindView(R.id.dashboard_scroll_view)
    ScrollView dashboardScrollView;

    @BindView(R.id.dashboard_main_container)
    View dashboardMainContainer;

    @BindView(R.id.component_curiosity_about_ph_on_brazil_container)
    FrameLayout containerCuriosityAboutPH;
    private ComponentCuriosityAboutPHInvestments mComponentCuriosityAboutPHInvestments;

    @BindView(R.id.component_public_health_investment_per_citizen_per_day_container)
    LinearLayout containerPHInvestmentCitizenDay;
    private ComponentCityInvestmentsPHCitizenDay mComponentInvestmentsCityDay;

    @BindView(R.id.component_public_health_city_investment_per_citizen_per_year_container)
    LinearLayout containerPHCityInvestmentCitizenYear;
    private ComponentCityInvestmentsPHCitizenYear mComponentCityInvestmentsCityYear;

    @BindView(R.id.dashboard_page_container_explanation_investments_year)
    View containerExplanationValuesInvestmentsYear;

    @BindView(R.id.component_public_health_state_investment_per_citizen_per_year_container)
    LinearLayout containerPHInvestmentCityCitizenYear;
    private ComponentInvestmentsOwnCityValueCitizenYear mComponentCityOwnInvestmentsCityYear;

    @BindView(R.id.component_public_health_federal_investment_per_citizen_per_year_container)
    LinearLayout containerPHFederalInvestmentCitizenYear;
    private ComponentInvestmentsOtherSourcesValueCitizenYear mComponentFederalInvestmentsCityYear;

    @BindView(R.id.component_ranking_cities_by_financial_autonomy_container)
    LinearLayout containerRankingCitiesByFinancialAutonomy;
    private ComponentRankingCitiesByFinancialAutonomy mComponentRankingCities;

    @BindView(R.id.component_total_public_health_investments_container)
    LinearLayout containerTotalPHInvestments;
    private ComponentTotalInvestmentCityPH mComponentTotalInvestmentCityPH;

    @BindView(R.id.component_city_financial_autonomy_container)
    LinearLayout containerCityFinancialAutonomy;
    private ComponentCityFinancialAutonomyPHInvestments mComponentCityFinancialAutonomy;

    @BindView(R.id.component_federal_investment_public_health_city_container)
    LinearLayout containerFederalInvestmentPHCity;
    private ComponentFederalInvestmentsCityPH mComponentFederalInvestmentCityPH;

    @BindView(R.id.component_state_investment_public_health_city_container)
    LinearLayout containerStateInvestmentPHCity;
    private ComponentStateInvestmentsCityPH mComponentStateInvestmentPH;

    @BindView(R.id.component_other_sources_investment_public_health_container)
    LinearLayout containerCityInvestmentPHCity;
    private ComponentOtherSourcesInvestmentsCityPH mComponentOtherSourcesInvestmentsCityPH;

    @BindView(R.id.component_declared_city_expenses_container)
    LinearLayout containerPHExpensesDeclaredCity;
    private ComponentDeclaredCityExpensesPH mComponentDeclaredCityExpensesPH;

    @BindView(R.id.dashboard_shareable_content_p1_selector)
    FrameLayout containerShareableContentSelectorP1;
    @BindView(R.id.dashboard_shareable_content_p1)
    View containerShareableContentP1;

    @BindView(R.id.dashboard_shareable_content_p2_selector)
    FrameLayout containerShareableContentSelectorP2;
    @BindView(R.id.dashboard_shareable_content_p2)
    View containerShareableContentP2;

    @BindView(R.id.dashboard_shareable_content_p3_selector)
    FrameLayout containerShareableContentSelectorP3;
    @BindView(R.id.dashboard_shareable_content_p3)
    View containerShareableContentP3;

    @BindView(R.id.dashboard_shareable_content_p4_selector)
    FrameLayout containerShareableContentSelectorP4;
    @BindView(R.id.dashboard_shareable_content_p4)
    View containerShareableContentP4;

    @BindView(R.id.social_network_picker_holder)
    FrameLayout containerSocialNetworkPicker;
    private ComponentSocialNetworkPicker mSocialNetPicker;

    @BindView(R.id.component_permission_external_storage_container)
    FrameLayout containerNeedPermissionExternalStorage;
    @BindView(R.id.component_permission_external_storage_msg)
    TextView lblNeedPermissionExternalStorage;

    @BindView(R.id.dashboard_btns_confirm_cancel_sharing_action)
    View mBtnsConfirmCancelSharing;

    private final String DASHBOARD_SHAREABLE_CONTENT_P1 = "DASHBOARD_SHAREABLE_CONTENT_P1";
    private final String DASHBOARD_SHAREABLE_CONTENT_P2 = "DASHBOARD_SHAREABLE_CONTENT_P2";
    private final String DASHBOARD_SHAREABLE_CONTENT_P3 = "DASHBOARD_SHAREABLE_CONTENT_P3";
    private final String DASHBOARD_SHAREABLE_CONTENT_P4 = "DASHBOARD_SHAREABLE_CONTENT_P4";

    private DashboardPresenter mDashboardPresenter;
    private GeneralPurposesDialog mConfirmationDialog;
    private FAQDialog mFAQDialog;
    private LDBSearch mLDBSearch;
    private ComponentPhotographerSelectedViews mViewsPhotographer;

    private boolean mIsUpdatingScreenAfterSearch = false;
    private boolean mHasAddedScrollViewListener = false;

    /**
     * Initialization
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashbord, container, false);
        ButterKnife.bind(this, rootView);

        logUtils.logMessage(TypeUtils.LogMsgType.REGULAR,
                TAG + "Initializing DashboardFragment ...");

        return rootView;
    }

    public void init(LDBSearch search) {
        setSearch(search);
        initViews();
        initData();
    }

    /**
     * Reinitialization
     */
    @Override
    public void onResume() {
        super.onResume();
        initViews();
        initData();
    }

    private void initViews() {
        initToolbar();
        initFloatingActionButtons();
        initScrollView();
        initCuriosityAboutInvestmentsPH();
        initSocialNetPicker();
        initValuePHCitizenDay();
        initCityInvestmentPHCitizenYear();
        initInvestmentOwnCityValueCitizenYear();
        initInvestmentOtherSourcesValueCitizenYear();
        initRankingCitiesFinancialAutonomy();
        initTotalInvestmentOnCityPH();
        initCityFinancialAutonomy();
        initValueInvestedFederalGovCity();
        initValueInvestedStateGovCity();
        initValueInvestedCityGovOwnPH();
        initPHExpensesDeclaredCity();
        initFloatActionButton();
        initShareableContentSelectors();
        initNeedExternalStoragePermission(false);
    }

    private void initToolbar() {
        try {
            ((HomeActivity) getActivity()).getToolbar().setVisibility(
                    getSearch() != null? View.VISIBLE: View.GONE);
        } catch (NullPointerException npe) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to initToolbar");
        }
    }

    private void initFloatingActionButtons() {
        try {
            ((HomeActivity) getActivity()).setCallbackToolbarFloatingActionButtons(
                    mCallbackFloatingActionButtons);

            ((HomeActivity) getActivity()).setIsFavoriteSearch(getSearch().getFavorite());

        } catch (NullPointerException npe) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to initFloatingActionButtons");
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void initScrollView() {

        if (!mHasAddedScrollViewListener) {
            mHasAddedScrollViewListener = true;
            dashboardScrollView.getViewTreeObserver().addOnScrollChangedListener(mPageScrollerListener);
        }
    }

    @Override
    public void initCuriosityAboutInvestmentsPH() {
        try {
            mComponentCuriosityAboutPHInvestments = new ComponentCuriosityAboutPHInvestments(
                    getContext().getApplicationContext(),
                    containerCuriosityAboutPH,
                    mCallbackComponentCuriosityAboutPH,
                    getResources());

            if (mComponentCuriosityAboutPHInvestments.getSearchView() != null) {
                KeyboardUtils.dismissKeyboard(getContext(), mComponentCuriosityAboutPHInvestments.getSearchView());
            }

        } catch (Exception e) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to initCuriosityAboutInvestmentsPH " + e.getMessage());
        }
    }

    public void initSocialNetPicker() {
        try {
            mSocialNetPicker = new ComponentSocialNetworkPicker(
                    containerSocialNetworkPicker,
                    mCallbackComponentSocialNetworkPicker);

        } catch (NullPointerException npe) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to initSocialNetPicker");
        }
    }

    @Override
    public void initValuePHCitizenDay() {
        try {
            BasicValidators basicValidators = new BasicValidators();

            if (basicValidators.isValidSearch(getSearch())) {
                informUserSearchIsNeeded(false);

                mComponentInvestmentsCityDay = new ComponentCityInvestmentsPHCitizenDay(
                        containerPHInvestmentCitizenDay,
                        getResources(),
                        getSearch().getCity());

            } else {
                informUserSearchIsNeeded(true);
            }

        } catch (Exception e) {
            logUtils.logMessage(TypeUtils.LogMsgType.REGULAR,
                    TAG + "Failed to initValuePHCitizenDay");
        }
    }

    @Override
    public void initCityInvestmentPHCitizenYear() {
        try {
            BasicValidators basicValidators = new BasicValidators();

            if (basicValidators.isValidSearch(getSearch())) {
                informUserSearchIsNeeded(false);

                mComponentCityInvestmentsCityYear = new ComponentCityInvestmentsPHCitizenYear(
                        containerPHCityInvestmentCitizenYear,
                        getResources(),
                        getSearch().getCity());

            } else {
                informUserSearchIsNeeded(true);
            }

        } catch (Exception e) {
            logUtils.logMessage(TypeUtils.LogMsgType.REGULAR,
                    TAG + "Failed to initCityInvestmentPHCitizenYear");
        }
    }

    @Override
    public void initInvestmentOwnCityValueCitizenYear() {
        try {
            BasicValidators basicValidators = new BasicValidators();

            if (basicValidators.isValidSearch(getSearch())) {
                containerExplanationValuesInvestmentsYear.setVisibility(View.GONE);
                informUserSearchIsNeeded(false);

                mComponentCityOwnInvestmentsCityYear = new ComponentInvestmentsOwnCityValueCitizenYear(
                        containerPHInvestmentCityCitizenYear,
                        getResources(),
                        getSearch().getState());

            } else {
                informUserSearchIsNeeded(true);
            }

        } catch (Exception e) {
            logUtils.logMessage(TypeUtils.LogMsgType.REGULAR,
                    TAG + "Failed to initInvestmentOwnCityValueCitizenYear");
        }
    }

    @Override
    public void initInvestmentOtherSourcesValueCitizenYear() {
        try {
            BasicValidators basicValidators = new BasicValidators();

            if (basicValidators.isValidSearch(getSearch())) {
                informUserSearchIsNeeded(false);

                containerExplanationValuesInvestmentsYear.setVisibility(View.GONE);
                mComponentFederalInvestmentsCityYear = new ComponentInvestmentsOtherSourcesValueCitizenYear(
                        containerPHFederalInvestmentCitizenYear,
                        getResources());

            } else {
                informUserSearchIsNeeded(true);
            }

        } catch (Exception e) {
            logUtils.logMessage(TypeUtils.LogMsgType.REGULAR,
                    TAG + "Failed to initInvestmentOtherSourcesValueCitizenYear");
        }
    }

    @Override
    public void initRankingCitiesFinancialAutonomy() {
        try {
            mComponentRankingCities = new ComponentRankingCitiesByFinancialAutonomy(
                    containerRankingCitiesByFinancialAutonomy,
                    getLayoutInflater());

        } catch (Exception e) {
            logUtils.logMessage(TypeUtils.LogMsgType.REGULAR,
                    TAG + "Failed to initRankingCitiesFinancialAutonomy");
        }
    }

    @Override
    public void initTotalInvestmentOnCityPH() {
        try {
            mComponentTotalInvestmentCityPH = new ComponentTotalInvestmentCityPH(
                    containerTotalPHInvestments,
                    mCallbackComponentTotalInvestmentsPH,
                    getResources());

        } catch (Exception e) {
            logUtils.logMessage(TypeUtils.LogMsgType.REGULAR,
                    TAG + "Failed to initTotalInvestmentOnCityPH");
        }
    }

    @Override
    public void initValueInvestedFederalGovCity() {
        try {
            BasicValidators basicValidators = new BasicValidators();

            if (basicValidators.isValidSearch(getSearch())) {
                informUserSearchIsNeeded(false);

                mComponentFederalInvestmentCityPH = new ComponentFederalInvestmentsCityPH(
                        containerFederalInvestmentPHCity,
                        getResources(),
                        getSearch().getCity());
            } else {
                informUserSearchIsNeeded(true);
            }

        } catch (Exception e) {
            logUtils.logMessage(TypeUtils.LogMsgType.REGULAR,
                    TAG + "Failed to initValueInvestedFederalGovCity");
        }
    }

    @Override
    public void initValueInvestedStateGovCity() {
        try {
            BasicValidators basicValidators = new BasicValidators();

            if (basicValidators.isValidSearch(getSearch())) {
                informUserSearchIsNeeded(false);

                mComponentStateInvestmentPH = new ComponentStateInvestmentsCityPH(
                        containerStateInvestmentPHCity,
                        getResources(),
                        getSearch().getCity());
            } else {
                informUserSearchIsNeeded(true);
            }

        } catch (Exception e) {
            logUtils.logMessage(TypeUtils.LogMsgType.REGULAR,
                    TAG + "Failed to initValueInvestedStateGovCity");
        }
    }

    @Override
    public void initValueInvestedCityGovOwnPH() {
        try {
            BasicValidators basicValidators = new BasicValidators();

            if (basicValidators.isValidSearch(getSearch())) {
                informUserSearchIsNeeded(false);

                mComponentOtherSourcesInvestmentsCityPH = new ComponentOtherSourcesInvestmentsCityPH(
                        containerCityInvestmentPHCity,
                        getResources(),
                        getSearch().getCity());
            } else {
                informUserSearchIsNeeded(true);
            }

        } catch (Exception e) {
            logUtils.logMessage(TypeUtils.LogMsgType.REGULAR,
                    TAG + "Failed to initValueInvestedCityGovOwnPH");
        }
    }

    public void initCityFinancialAutonomy() {
        try {
            mComponentCityFinancialAutonomy = new ComponentCityFinancialAutonomyPHInvestments(
                    containerCityFinancialAutonomy,
                    getResources());

        } catch (Exception e) {
            logUtils.logMessage(TypeUtils.LogMsgType.REGULAR,
                    TAG + "Failed to initCityFinancialAutonomy");
        }
    }

    @Override
    public void initPHExpensesDeclaredCity() {
        try {
            BasicValidators basicValidators = new BasicValidators();

            if (basicValidators.isValidSearch(getSearch())) {
                informUserSearchIsNeeded(false);

                mComponentDeclaredCityExpensesPH = new ComponentDeclaredCityExpensesPH(
                        getActivity().getApplicationContext(),
                        containerPHExpensesDeclaredCity,
                        getResources(),
                        getSearch().getCity(),
                        mDashboardPresenter.getSearchResults().getDeclaredExpenses());

            } else {
                informUserSearchIsNeeded(true);
            }

        } catch (Exception e) {
            logUtils.logMessage(TypeUtils.LogMsgType.REGULAR,
                    TAG + "Failed to initPHExpensesDeclaredCity");
        }
    }

    public void informUserSearchIsNeeded(boolean needSearch) {
        mComponentCuriosityAboutPHInvestments.show(needSearch);
        dashboardScrollView.setVisibility(needSearch? View.GONE : View.VISIBLE);

        if (needSearch && !ConnectivityUtils.isConnected(getContext().getApplicationContext())) {
            onShowMessage(getString(R.string.msg_cannot_search_without_internet));
        }
    }

    public void initFloatActionButton() {
        try {
            BasicValidators basicValidators = new BasicValidators();

            if (basicValidators.isValidSearch(getSearch())) {
                ((HomeActivity) getActivity()).showFloatingActionButtons(true);
            } else {
                ((HomeActivity) getActivity()).showFloatingActionButtons(false);
            }
        } catch (Exception e) {
            logUtils.logMessage(TypeUtils.LogMsgType.REGULAR,
                    TAG + "Failed to initValuePHCitizenDay");
        }
    }

    @Override
    public void initShareableContentSelectors() {
        try {
            mBtnsConfirmCancelSharing.setVisibility(View.GONE);

            // Pass the selector of the shareable views into the component
            ArrayList<ComponentShareableContentSelector> shareContentSelectorViews = new ArrayList<>();

            shareContentSelectorViews.add(new ComponentShareableContentSelector(
                    containerShareableContentSelectorP1,
                    containerShareableContentP1,
                    DASHBOARD_SHAREABLE_CONTENT_P1));

            shareContentSelectorViews.add(new ComponentShareableContentSelector(
                    containerShareableContentSelectorP2,
                    containerShareableContentP2,
                    DASHBOARD_SHAREABLE_CONTENT_P2));

            shareContentSelectorViews.add(new ComponentShareableContentSelector(
                    containerShareableContentSelectorP3,
                    containerShareableContentP3,
                    DASHBOARD_SHAREABLE_CONTENT_P3));

            shareContentSelectorViews.add(new ComponentShareableContentSelector(
                    containerShareableContentSelectorP4,
                    containerShareableContentP4,
                    DASHBOARD_SHAREABLE_CONTENT_P4));

            mViewsPhotographer = new ComponentPhotographerSelectedViews(
                    getActivity().getApplicationContext(),
                    mCallbackComponentPhotographerSelectedViews,
                    shareContentSelectorViews);

        } catch (Exception e) {
            logUtils.logMessage(TypeUtils.LogMsgType.REGULAR,
                    TAG + "Failed to initShareableContentSelectorP1");
        }
    }

    /**
     * Show and hide the message that asks for the External Storage permission.
     *
     * It also restarts the sharing process, if the user has granted the permission during the
     * current session.
     */
    @Override
    public void initNeedExternalStoragePermission(boolean showPermissionMsg) {
        containerNeedPermissionExternalStorage.setVisibility(showPermissionMsg? View.VISIBLE : View.GONE);
    }

    /**
     * Events
     *
     * Event: Page scrolling
     */
    private final ViewTreeObserver.OnScrollChangedListener mPageScrollerListener =
        new ViewTreeObserver.OnScrollChangedListener() {

        @Override
        public void onScrollChanged() {
            updateScrollPosition();
            updateComponentInvestmentsCitizenDay();
            updateVerticalScrollerIndicatorToolbar();
        }
    };

    private void updateScrollPosition() {

        if (isUpdatingUIAfterSearch() && dashboardScrollView != null) {
            setIsUpdatingUIAfterSearch(false);
            dashboardScrollView.setSmoothScrollingEnabled(true);
            dashboardScrollView.smoothScrollTo(0,0);
        }
    }

    private void updateComponentInvestmentsCitizenDay() {

        if (mComponentInvestmentsCityDay != null) {
            mComponentInvestmentsCityDay.showLargeVersion(dashboardScrollView.getScrollY() == 0);
        }
    }

    private void updateVerticalScrollerIndicatorToolbar() {

        try {

            if (dashboardScrollView.getHeight() > 0) {
                float yPercentageTop = (dashboardScrollView.getScrollY() * 100) / dashboardMainContainer.getHeight();

                if (getActivity() != null) {
                    ((HomeActivity) getActivity()).updateToolbarPageScrollerIndicator(yPercentageTop);
                }
            }

        } catch (Exception e) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to updateVerticalScrollerIndicatorToolbar " + e.getMessage());
        }
    }

    /**
     * Event: Top action buttons (Info, Favorite, Share)
     */
    private final ComponentFloatingActionButtonsContract mCallbackFloatingActionButtons =
        new ComponentFloatingActionButtonsContract() {

        @Override
        public void onFavoriteBtnClicked() {
            showToggleFavoriteDialog();
        }

        @Override
        public void onAddBtnClicked() {
            mFAQDialog = new FAQDialog(getContext(), new FAQDialogContract.View() {
                @Override
                public void onCloseDialog() {
                    closeDialog();
                }
            });
        }

        @Override
        public void onShareBtnClicked() {
            startShare();
        }
    };

    private void showToggleFavoriteDialog() {
        try {
            String msg;

            if (!getSearch().getFavorite()) {
                msg = getString(R.string.dashboard_page_msg_added_list_favorites);
            } else {
                msg = getString(R.string.dashboard_page_msg_removed_list_favorites);
            }

            mConfirmationDialog = new GeneralPurposesDialog(getContext(),
                    new GeneralPurposesDialog.GeneralDialogCallback() {
                        @Override
                        public void onCloseDialog() {
                            closeDialog();
                        }

                        @Override
                        public void onInfoDialogActionConfirmed() {
                            toggleFavoriteSearch();
                            closeDialog();
                        }

                        @Override
                        public void onConfirmationDialogActionConfirmed() {
                            toggleFavoriteSearch();
                            closeDialog();
                        }
                    }, GeneralPurposesDialog.DIALOG_TYPE_INFORMATION,
                    GeneralPurposesDialog.DIALOG_THEME_INFO,
                    "",
                    R.drawable.ico_thanks_colorful,
                    msg,
                    getString(R.string.dialog_ok)
            );

        } catch (NullPointerException npe) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to showToggleFavoriteDialog " + npe.getMessage());
        }
    }

    /**
     * Event: Share
     */
    @Override
    public void startShare() {
        PermissionUtils permissionUtils = new PermissionUtils();

        if (permissionUtils.isStoragePermissionGranted(getContext().getApplicationContext())) {
            showSocialNetPicker();
        } else {
            requestPermissionUseExternalStorage();
        }
    }

    private final ComponentSocialNetworkPickerContract mCallbackComponentSocialNetworkPicker =
        new ComponentSocialNetworkPickerContract() {

        @Override
        public void onClickBtnStart() {
            enableShareFlow();
        }

        @Override
        public void onSocialNetworkPicked(int socialNetworkId) {
            finishShareFlow(socialNetworkId);
        }
    };

    @Override
    @OnClick(R.id.component_share_btn_confirm_sharing_action)
    public void confirmShare() {
        confirmShareFlow();
    }

    private ComparePhotographerSelectedViewsContract mCallbackComponentPhotographerSelectedViews =
        new ComparePhotographerSelectedViewsContract() {

        @Override
        public void askPermissionWriteExternalStorage() {
            requestPermissionUseExternalStorage();
        }

        @Override
        public void externalStorageNotWritable() {
            onShowMessage(getString(R.string.permissions_msg_is_there_a_connected_sd_card));
        }
    };

    @Override
    @OnClick({R.id.component_share_confirm_action_btn_undo_sharing_flow})
    public void undoShare() {
        undoShareFlow();
    }

    @Override
    public void showSocialNetPicker() {

        if (mSocialNetPicker != null) {
            mSocialNetPicker.show(!mSocialNetPicker.isShowing());
        }
    }

    @Override
    public void enableShareFlow() {
        try {

            if (mSocialNetPicker != null && mViewsPhotographer != null) {
                mSocialNetPicker.showContainerInstructions(false);
                mSocialNetPicker.show(false);
                mViewsPhotographer.enableSharingSelectors(true);
                mComponentInvestmentsCityDay.showLargeVersion(false);
                mBtnsConfirmCancelSharing.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            logUtils.logMessage(TypeUtils.LogMsgType.REGULAR,
                    TAG + "Failed to enableShareFlow");
        }
    }

    @Override
    public void confirmShareFlow() {
        try {
            mViewsPhotographer.enableSharingSelectors(false);
            mComponentInvestmentsCityDay.showLargeVersion(true);
            mBtnsConfirmCancelSharing.setVisibility(View.GONE);
            mViewsPhotographer.takePictureSelectedItems();
            mSocialNetPicker.show(true);

        } catch (Exception e) {
            logUtils.logMessage(TypeUtils.LogMsgType.REGULAR,
                    TAG + "Failed to confirmShareFlow");
        }
    }

    @Override
    public void finishShareFlow(int selectedSocialNetwork) {
        try {
            BasicValidators basicValidators = new BasicValidators();

            if (basicValidators.isValidSearch(getSearch())) {
                SharedContent sharedContent = new SharedContent();
                sharedContent.setListPathImgSharedContent(mViewsPhotographer.getAbsolutePathSharedImgs());
                sharedContent.setSelectedSocialNetworkId(mSocialNetPicker.getSelectedSocialNetworkId());
                sharedContent.setSharingComparison(false);
                sharedContent.setSearchA(getSearch());

                Intent intent = new Intent(getContext(), SharingActivity.class);
                intent.putExtra(ConstantUtils.REQUEST_PARAM_SHARED_CONTENT,
                        JsonUtils.toJson(sharedContent));

                startActivity(intent);

            } else {
                onShowMessage(getString(R.string.dashboard_could_not_share_search_verify_params));
            }
        } catch  (Exception e) {
            logUtils.logMessage(TypeUtils.LogMsgType.REGULAR,
                    TAG + "Failed to finishShareFlow");
        }
    }

    @Override
    public void undoShareFlow() {
        try {
            mSocialNetPicker.showContainerInstructions(true);
            mSocialNetPicker.show(false);
            mViewsPhotographer.enableSharingSelectors(false);
            mComponentInvestmentsCityDay.showLargeVersion(true);
            mBtnsConfirmCancelSharing.setVisibility(View.GONE);

        } catch (Exception e) {
            logUtils.logMessage(TypeUtils.LogMsgType.REGULAR,
                    TAG + "Failed to undoShareFlow");
        }
    }

    /**
     * Event: Permissions
     */
    @Override
    @OnClick({R.id.component_permission_external_storage_btn_force_request})
    public void redirectUserPermissionPageWriteExternalStorageDenied() {

        if (PermissionUtils.hasAskedExternalStoragePermission() &&
                !PermissionUtils.wasExternalStoragePermissionGranted()) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts(ConstantUtils.SCHEME_PACKAGE, (getActivity().getPackageName()), null);
            intent.setData(uri);
            startActivity(intent);
        } else {
            PermissionUtils.setHasAskedExternalStoragePermission(true);
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PermissionUtils.IDX_PERMISSION_WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void requestPermissionUseExternalStorage() {
        try {
            PermissionUtils permissionUtils = new PermissionUtils();

            if (permissionUtils.isExternalStorageWritable()) {

                if (!permissionUtils.isStoragePermissionGranted(getContext().getApplicationContext())) {
                    initNeedExternalStoragePermission(true);
                } else {
                    PermissionUtils.confirmExternalStoragePermissionGranted();
                    initNeedExternalStoragePermission(false);
                }
            } else {
                lblNeedPermissionExternalStorage.setText(getString(R.string.permissions_msg_is_there_a_connected_sd_card));
                initNeedExternalStoragePermission(true);
            }
        } catch (NullPointerException npe) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to ensurePermissions");
        }
    }

    /**
     * Event: Favorite
     */
    private void toggleFavoriteSearch() {

        if (mDashboardPresenter != null && getSearch() != null) {
            getSearch().setFavorite(!getSearch().getFavorite());
            mDashboardPresenter.updateSearch(getSearch());

            if (getActivity() != null) {
                ((HomeActivity) getActivity()).setIsFavoriteSearch(getSearch().getFavorite());
            }
        }
    }

    /**
     * Event: Finish Dialogs
     */
    private void closeDialog() {

        if (mConfirmationDialog != null) {
            mConfirmationDialog.dismiss();
            mConfirmationDialog = null;
        }

        if (mFAQDialog != null) {
            mFAQDialog.closeDialog();
            mFAQDialog = null;
        }
    }

    /**
     * Event: Curiosity panel
     */
    private final ComponentCuriosityAboutPHInvestmentsContract mCallbackComponentCuriosityAboutPH =
        new ComponentCuriosityAboutPHInvestmentsContract() {

        @Override
        public void onSearchInputInitiated(String searchTerm) {
            openSearchDialog(searchTerm);
        }
    };

    private void openSearchDialog(String searchTerm) {
        try {

            if (getActivity() != null) {
                ((HomeActivity)getActivity()).openSearchDialog(searchTerm);
                mComponentCuriosityAboutPHInvestments.setSearch("");
            }
        } catch (Exception e) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to openSearchDialog: " + e.getMessage());
        }
    }

    /**
     * Event: Total investments on PH
     */
    private final ComponentTotalInvestmentsPHContract mCallbackComponentTotalInvestmentsPH =
        new ComponentTotalInvestmentsPHContract() {

        @Override
        public void pieChartItemSelected(String selectedItem, float selectedValue) {
            displayDetailedInfoTotalInvestments(selectedItem, selectedValue);
        }
    };

    private void displayDetailedInfoTotalInvestments(String selectedItem, float selectedValue) {
        try {

            if (selectedItem != null) {
                mComponentOtherSourcesInvestmentsCityPH.show(false);
                mComponentStateInvestmentPH.show(false);
                mComponentFederalInvestmentCityPH.show(false);

                if (selectedItem.equals(getString(R.string.dashboard_page_lbl_city_government))) {
                    mComponentOtherSourcesInvestmentsCityPH.show(true);
                    mComponentOtherSourcesInvestmentsCityPH.setPercentageOnMsg(selectedValue);

                } else if (selectedItem.equals(getString(R.string.dashboard_page_lbl_state_government))) {
                    mComponentStateInvestmentPH.show(true);
                    mComponentStateInvestmentPH.setPercentageOnMsg(selectedValue);

                } else if (selectedItem.equals(getString(R.string.dashboard_page_lbl_federal_government))) {
                    mComponentFederalInvestmentCityPH.show(true);
                    mComponentFederalInvestmentCityPH.setPercentageOnMsg(selectedValue);
                }
            }

        } catch (NullPointerException npe) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to displayDetailedInfoTotalInvestments " + npe.getMessage());
        }
    }

    /**
     * Data loading
     */
    private void initData() {

        if (mDashboardPresenter == null) {
            mDashboardPresenter = new DashboardPresenter(getContext().getApplicationContext());
        } else {
            mDashboardPresenter.unbindView();
        }

        mDashboardPresenter.bindView(this);
        mDashboardPresenter.loadReports(getSearch());
    }

    @Override
    public void loadValuePHCitizenDay(final double valueCitizenPerDay) {

        try {
            containerPHInvestmentCitizenDay.post(new Runnable() {
                @Override
                public void run() {
                    try {

                        if (valueCitizenPerDay > 0) {
                            containerExplanationValuesInvestmentsYear.setVisibility(View.VISIBLE);
                            mComponentInvestmentsCityDay.loadValuePHCitizenDay(
                                    valueCitizenPerDay,
                                    getSearch().getCity(),
                                    getSearch().getYear());
                        } else {
                            mComponentInvestmentsCityDay.show(false);
                            containerExplanationValuesInvestmentsYear.setVisibility(View.GONE);
                            mViewsPhotographer.lockShareableSelector(DASHBOARD_SHAREABLE_CONTENT_P1,true);
                        }

                    } catch (Exception e) {
                        logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                                TAG + "Failed to loadValuePHCitizenDay" + e.getMessage());
                    }
                }
            });
        } catch (NullPointerException npe) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to loadValuePHCitizenDay" + npe.getMessage());
        }
    }

    @Override
    public void loadCityInvestmentsPHCitizenYear(final double valueCitizenPerYear) {

        try {
            containerPHCityInvestmentCitizenYear.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        mComponentCityInvestmentsCityYear.loadCityInvestmentsPHCitizenYear(
                                valueCitizenPerYear,
                                getSearch().getCity());

                    } catch (Exception e) {
                        logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                                TAG + "Failed to loadCityInvestmentsPHCitizenYear" + e.getMessage());
                    }
                }
            });
        } catch (NullPointerException npe) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to loadCityInvestmentsPHCitizenYear" + npe.getMessage());
        }
    }

    @Override
    public void loadInvestmentOwnCityValueCitizenYear(final double cityInvestment) {

        try {
            containerPHInvestmentCityCitizenYear.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        mComponentCityOwnInvestmentsCityYear
                                .loadStateInvestmentsOnPHPerCitizenPerDay(cityInvestment);

                    } catch (Exception e) {
                        logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                                TAG + "Failed to loadInvestmentOwnCityValueCitizenYear" + e.getMessage());
                    }
                }
            });
        } catch (NullPointerException npe) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to loadInvestmentOwnCityValueCitizenYear" + npe.getMessage());
        }
    }

    @Override
    public void loadInvestmentOtherSourcesValueCitizenYear(final double otherInvestments) {

        try {
            containerPHFederalInvestmentCitizenYear.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        mComponentFederalInvestmentsCityYear
                                .loadFederalInvestmentsPHCitizenYear(otherInvestments);

                    } catch (Exception e) {
                        logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                                TAG + "Failed to loadInvestmentOtherSourcesValueCitizenYear" + e.getMessage());
                    }
                }
            });

        } catch (NullPointerException npe) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to loadInvestmentOtherSourcesValueCitizenYear" + npe.getMessage());
        }
    }

    @Override
    public void loadRankingCities(final float nationalAvgFinancialAutonomyHealthInvestments,
                                  final ArrayList<LDBCityRankingObject> rankingData) {
        try {
            containerRankingCitiesByFinancialAutonomy.post(new Runnable() {
                @Override
                public void run() {
                    try {

                        mComponentRankingCities.loadRankingCities(
                                nationalAvgFinancialAutonomyHealthInvestments,
                                rankingData);

                    } catch (NullPointerException npe) {

                        if (mViewsPhotographer != null) {
                            mViewsPhotographer.lockShareableSelector(DASHBOARD_SHAREABLE_CONTENT_P2,true);
                        }

                        logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                                TAG + "Failed to loadRankingCities" + npe.getMessage());
                    }
                }
            });
        } catch (NullPointerException npe) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to loadRankingCities" + npe.getMessage());
        }
    }

    @Override
    public void loadChartTotalInvestmentsCityPH(final ArrayList<Double> dataSet,
                                                final int[] colors,
                                                final ArrayList<String> labels,
                                                final String chartDescription) {
        try {
            containerTotalPHInvestments.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        mComponentTotalInvestmentCityPH.loadInvestmentsCityPH(
                                getActivity().getApplicationContext(),
                                dataSet,
                                colors,
                                labels,
                                chartDescription,
                                getSearch().getCity());

                    } catch (Exception e) {

                        if (mViewsPhotographer != null) {
                            mViewsPhotographer.lockShareableSelector(DASHBOARD_SHAREABLE_CONTENT_P3,true);
                        }

                        logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                                TAG + "Failed to loadChartTotalInvestmentsCityPH" + e.getMessage());
                    }
                }
            });
        } catch (NullPointerException npe) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to loadChartTotalInvestmentsCityPH" + npe.getMessage());
        }
    }

    @Override
    public void loadValueInvestedFederalGovCity(final double federalInvestmentOnCity) {
        try {
            containerFederalInvestmentPHCity.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        mComponentFederalInvestmentCityPH.loadInvestmentFederalGovCityPH(
                                federalInvestmentOnCity);

                    } catch (Exception e) {
                        logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                                TAG + "Failed to loadValueInvestedFederalGovCity" + e.getMessage());
                    }
                }
            });
        } catch (NullPointerException npe) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to loadValueInvestedFederalGovCity" + npe.getMessage());
        }
    }

    @Override
    public void loadValueInvestedStateGovCity(final double stateInvestmentCity) {
        try {
            containerStateInvestmentPHCity.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        mComponentStateInvestmentPH.loadInvestmentStateGovCityPH(
                                stateInvestmentCity,
                                getSearch().getState());

                    } catch (Exception e) {
                        logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                                TAG + "Failed to loadValueInvestedStateGovCity" + e.getMessage());
                    }
                }
            });
        } catch (NullPointerException npe) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to loadValueInvestedStateGovCity" + npe.getMessage());
        }
    }

    @Override
    public void loadValueInvestedOtherSourcesPH(final double cityInvestment) {
        try {
            containerCityInvestmentPHCity.post(new Runnable() {
                @Override
                public void run() {
                    mComponentOtherSourcesInvestmentsCityPH.loadInvestmentsOtherSourcesCityPH(
                            cityInvestment);
                }
            });

        } catch (NullPointerException npe) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to loadValueInvestedOtherSourcesPH " + npe.getMessage());
        }
    }

    @Override
    public void loadCityFinancialAutonomy(final double cityFinancialAutonomy) {
        try {
            containerCityFinancialAutonomy.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        mComponentCityFinancialAutonomy.loadCityFinancialAutonomy(
                                getSearch().getCity(),
                                cityFinancialAutonomy);

                    } catch (Exception e) {
                        logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                                TAG + "Failed to loadCityFinancialAutonomy" + e.getMessage());
                    }
                }
            });
        } catch (NullPointerException npe) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to loadCityFinancialAutonomy " + npe.getMessage());
        }
    }

    @Override
    public void loadPHExpensesDeclaredCity(final ArrayList<LDBExpense> declaredCityExpenses) {
        try {
            containerPHExpensesDeclaredCity.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        mComponentDeclaredCityExpensesPH.loadPHExpensesDeclaredCity(
                                declaredCityExpenses,
                                getSearch().getCity());

                    } catch (Exception e) {

                        if (mViewsPhotographer != null) {
                            mViewsPhotographer.lockShareableSelector(DASHBOARD_SHAREABLE_CONTENT_P4,true);
                        }

                        logUtils.logMessage(TypeUtils.LogMsgType.REGULAR,
                                TAG + "Failed to loadPHExpensesDeclaredCity " + e.getMessage());
                    }
                }
            });

        } catch (NullPointerException npe) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to loadPHExpensesDeclaredCity " + npe.getMessage());
        }
    }

    /**
     * Navigation
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Messaging
     */
    @Override
    public void onShowMessage(String message) {
        try {

            if (!getActivity().isFinishing()) {
                ((HomeActivity) getActivity()).showMessage(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onShowProgress(boolean show) {
        try {

            if (getActivity() != null) {
                ((HomeActivity) getActivity()).showProgress(show);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Getters and Setters
     */
    public void setIsUpdatingUIAfterSearch(boolean isUpdatingUI) {
        mIsUpdatingScreenAfterSearch = isUpdatingUI;
    }

    public boolean isUpdatingUIAfterSearch() {
        return mIsUpdatingScreenAfterSearch;
    }

    @Override
    public void setSearch(LDBSearch search) {
        mLDBSearch = search;
    }

    @Override
    public LDBSearch getSearch() {
        return mLDBSearch;
    }

    /**
     * Finalization
     */
    @Override
    public void onStop() {
        super.onStop();

        if (mDashboardPresenter != null) {
            mDashboardPresenter.unbindView();
        }
    }

}