package br.com.eadfiocruzpe.Views.Pages.Home.Compare;

import java.util.ArrayList;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import butterknife.OnClick;
import butterknife.BindView;
import butterknife.ButterKnife;

import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Contracts.CompareSearchesContract;
import br.com.eadfiocruzpe.Contracts.SearchVisualizerSelectorAdapterContract;
import br.com.eadfiocruzpe.Contracts.ComponentFloatingActionButtonsContract;
import br.com.eadfiocruzpe.Contracts.FAQDialogContract;
import br.com.eadfiocruzpe.Contracts.ComparePhotographerSelectedViewsContract;
import br.com.eadfiocruzpe.Contracts.ComponentSocialNetworkPickerContract;
import br.com.eadfiocruzpe.Contracts.ShareFeatureContract;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.SharedContent;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBSearch;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBExpense;
import br.com.eadfiocruzpe.Presenters.CompareSearchesPresenter;
import br.com.eadfiocruzpe.Utils.JsonUtils;
import br.com.eadfiocruzpe.Utils.PermissionUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;
import br.com.eadfiocruzpe.Utils.ConstantUtils;
import br.com.eadfiocruzpe.Utils.StringUtils;
import br.com.eadfiocruzpe.Utils.ColorUtils;
import br.com.eadfiocruzpe.Views.Components.ComponentPhotographerSelectedViews;
import br.com.eadfiocruzpe.Views.Components.ComponentShareableContentSelector;
import br.com.eadfiocruzpe.Views.Components.ComponentSocialNetworkPicker;
import br.com.eadfiocruzpe.Views.Pages.Home.Sharing.SharingActivity;
import br.com.eadfiocruzpe.Views.Components.ComponentCompareExpenses;
import br.com.eadfiocruzpe.Views.Components.ComponentCompareTotalInvestments;
import br.com.eadfiocruzpe.Views.Dialogs.FAQDialog;
import br.com.eadfiocruzpe.Views.Components.ComponentCompareHorizontalBarChart;
import br.com.eadfiocruzpe.Views.Adapters.HomePagerAdapter;
import br.com.eadfiocruzpe.Views.Adapters.SearchVisualizerSelectorAdapter;
import br.com.eadfiocruzpe.Views.ViewModels.CheckableLDBSearch;
import br.com.eadfiocruzpe.Views.Validators.BasicValidators;
import br.com.eadfiocruzpe.Views.Pages.Home.BaseFragment;
import br.com.eadfiocruzpe.Views.Pages.Home.HomeActivity;

public class CompareSearchesFragment extends BaseFragment implements
        CompareSearchesContract.View,
        ShareFeatureContract {

    private final String TAG = "CompareSearchesFragment";

    // Views of the Flow 1
    @BindView(R.id.compare_searches_select_items_comparison)
    View containerSelectSearches;

    @BindView(R.id.compare_searches_msg_select_items)
    TextView msgSelectItems;

    @BindView(R.id.compare_searches_selected_items_container)
    View selectedItemsContainer;

    @BindView(R.id.compare_searches_selected_items)
    RecyclerView rvSelectedItems;

    @BindView(R.id.compare_searches_search_history_rv_container)
    View searchHistoryContainer;

    @BindView(R.id.compare_searches_search_history_rv)
    RecyclerView rvSearchHistory;

    @BindView(R.id.component_msg_no_search_selected)
    FrameLayout emptyListContainer;

    @BindView(R.id.compare_searches_btn_confirm)
    Button confirmBtn;

    // Views of the Flow 2
    @BindView(R.id.compare_searches_comparison_container_scroll)
    ScrollView compareSearchesScrollView;

    @BindView(R.id.compare_searches_comparison_main_container)
    View compareSearchesMainContainer;

    @BindView(R.id.compare_searches_comparison_lbl_values_period)
    TextView lblValuesPeriod;

    @BindView(R.id.compare_searches_comparison_lbl_investments_ph)
    TextView lblInvestmentsPH;

    @BindView(R.id.compare_searches_comparison_lbl_declared_expenses)
    TextView lblDeclaredExpenses;

    @BindView(R.id.compare_searches_investments_per_citizen)
    LinearLayout containerInvestmentsPerCitizen;

    @BindView(R.id.compare_searches_compare_value_citizen_day)
    FrameLayout containerCompareValueCitizenDay;

    @BindView(R.id.compare_searches_compare_value_citizen_year)
    FrameLayout containerCompareValueCitizenYear;

    @BindView(R.id.compare_searches_compare_value_invested_own_city)
    FrameLayout containerCompareValueInvestedOwnCity;

    @BindView(R.id.compare_searches_compare_value_invested_other_sources)
    FrameLayout containerCompareValueInvestedOtherSources;

    @BindView(R.id.compare_searches_compare_total_investments)
    FrameLayout containerCompareTotalInvestments;

    @BindView(R.id.compare_searches_compare_expenses)
    FrameLayout containerCompareExpenses;

    @BindView(R.id.compare_searches_shareable_content_p1_selector)
    FrameLayout containerShareableContentSelectorP1;
    @BindView(R.id.compare_searches_shareable_content_p1)
    View containerShareableContentP1;

    @BindView(R.id.compare_searches_shareable_content_p2_selector)
    FrameLayout containerShareableContentSelectorP2;
    @BindView(R.id.compare_searches_shareable_content_p2)
    View containerShareableContentP2;

    @BindView(R.id.compare_searches_shareable_content_p3_selector)
    FrameLayout containerShareableContentSelectorP3;
    @BindView(R.id.compare_searches_shareable_content_p3)
    View containerShareableContentP3;

    @BindView(R.id.compare_searches_shareable_content_p4_selector)
    FrameLayout containerShareableContentSelectorP4;
    @BindView(R.id.compare_searches_shareable_content_p4)
    View containerShareableContentP4;

    @BindView(R.id.social_network_picker_holder)
    FrameLayout containerSocialNetworkPicker;
    private ComponentSocialNetworkPicker mSocialNetPicker;

    @BindView(R.id.component_permission_external_storage_container)
    FrameLayout containerNeedPermissionExternalStorage;
    @BindView(R.id.component_permission_external_storage_msg)
    TextView lblNeedPermissionExternalStorage;

    @BindView(R.id.compare_searches_btns_confirm_cancel_sharing_action)
    View mBtnsConfirmCancelSharing;

    private final String COMPARE_SEARCHES_SHAREABLE_CONTENT_P1 = "COMPARE_SEARCHES_SHAREABLE_CONTENT_P1";
    private final String COMPARE_SEARCHES_SHAREABLE_CONTENT_P2 = "COMPARE_SEARCHES_SHAREABLE_CONTENT_P2";
    private final String COMPARE_SEARCHES_SHAREABLE_CONTENT_P3 = "COMPARE_SEARCHES__SHAREABLE_CONTENT_P3";
    private final String COMPARE_SEARCHES_SHAREABLE_CONTENT_P4 = "COMPARE_SEARCHES__SHAREABLE_CONTENT_P4";

    private ComponentPhotographerSelectedViews mViewsPhotographer;


    private CompareSearchesPresenter mCompareSearchesPresenter;
    private SearchVisualizerSelectorAdapter mRvAdapterSelectedItems;
    private SearchVisualizerSelectorAdapter mRvAdapterSearchHistory;
    private FAQDialog mFAQDialog;
    private ComponentCompareHorizontalBarChart mCValueCitizenDay;
    private ComponentCompareHorizontalBarChart mCValueCitizenYear;
    private ComponentCompareHorizontalBarChart mCValueInvestedOwnCity;
    private ComponentCompareHorizontalBarChart mCValueInvestedOthersCity;
    private ComponentCompareTotalInvestments mCCompareInvestments;
    private ComponentCompareExpenses mCCompareExpenses;
    private BasicValidators mValidationUtils;

    private LDBSearch mLDBSearchA;
    private LDBSearch mLDBSearchB;

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
        View rootView = inflater.inflate(R.layout.fragment_compare_searches, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    public void init() {
        mCompareSearchesPresenter = new CompareSearchesPresenter(getContext().getApplicationContext());
        mCompareSearchesPresenter.bindView(this);

        initViews();
        initData();
    }

    private void initViews() {
        initMainContainerSelectSearches(true);

        // Flow 1
        initScrollView();
        initSelectedItems();
        initSearchHistoryList();
        updateConfirmBtn();

        // Flow 2
        initFloatingActionButtons();
        initSocialNetPicker();
        initShareableContentSelectors();
        initNeedExternalStoragePermission(false);
    }

    /**
     * Initialization: Parent's views
     */
    public void initScrollView() {

        if (!mHasAddedScrollViewListener) {
            mHasAddedScrollViewListener = true;
            compareSearchesScrollView.getViewTreeObserver().addOnScrollChangedListener(mPageScrollerListener);
        }
    }

    /**
     * Initialization: Flow 1 - Selection of the searches
     */
    private void initMainContainerSelectSearches(final boolean visualizeIt) {
        try {
            ((HomeActivity)getActivity()).updateBackgroundRootView(
                    HomePagerAdapter.IDX_COMPARE_OPTION,
                    true,
                    R.drawable.background_green_gradient_1,
                    R.color.color_green_blue_4);

            containerSelectSearches.setVisibility(visualizeIt? View.VISIBLE: View.GONE);
            compareSearchesScrollView.setVisibility(visualizeIt? View.GONE: View.VISIBLE);

            selectedItemsContainer.setVisibility(visualizeIt &&
                    mCompareSearchesPresenter.getSelectedSearches().size() > 0? View.VISIBLE: View.GONE);
            searchHistoryContainer.setVisibility(visualizeIt? View.VISIBLE: View.GONE);

        } catch (Exception ignored) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to initMainContainerSelectSearches");
        }
    }

    private void initSelectedItems() {
        mCompareSearchesPresenter.setSelectedItems(new ArrayList<LDBSearch>());
        rvSelectedItems.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));
        mRvAdapterSelectedItems = new SearchVisualizerSelectorAdapter(
                mCallbackSelectedItemsRv,
                getContext().getApplicationContext(),
                false,
                true,
                mCompareSearchesPresenter.getSelectedSearches());
        rvSelectedItems.setAdapter(mRvAdapterSelectedItems);
    }

    private void initSearchHistoryList() {
        rvSearchHistory.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));
        mRvAdapterSearchHistory = new SearchVisualizerSelectorAdapter(
                mCallbackSearchHistoryRv,
                getContext().getApplicationContext(),
                true,
                false,
                new ArrayList<LDBSearch>());
        rvSearchHistory.setAdapter(mRvAdapterSearchHistory);
    }

    private void initEmptyListMsg() {
        try {
            emptyListContainer.setVisibility(
                    mCompareSearchesPresenter.getSearchHistory().size() > 0?
                            View.GONE: View.VISIBLE);
        } catch (Exception e) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to initEmptyListMsg");
        }
    }

    /**
     * Data: Flow 1 - Selection of the searches
     */
    private void initData() {
        logUtils.logMessage(TypeUtils.LogMsgType.SUCCESS,
                TAG + "Initializing CompareSearchesFragment ...");
        mValidationUtils = new BasicValidators();
        mCompareSearchesPresenter.loadSearches();
    }

    /**
     * Events: Flow 1 - Selection of the searches
     */
    @Override
    public void onLoadSearchHistory(final ArrayList<LDBSearch> searchHistory) {

        try {
            rvSearchHistory.post(new Runnable() {
                @Override
                public void run() {
                    updateSearchHistory(searchHistory);
                    initEmptyListMsg();
                }
            });
        } catch (NullPointerException npe) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed onLoadSearchHistory " + npe.getMessage());
        }
    }

    private final SearchVisualizerSelectorAdapterContract.Client mCallbackSelectedItemsRv =
            new SearchVisualizerSelectorAdapterContract.Client() {

        @Override
        public void onSearchItemDetails(LDBSearch search) {
            // ..
        }

        @Override
        public void onCheckableLDBSearchDetails(CheckableLDBSearch search) {
            // ..
        }

        @Override
        public void onSearchChecked(LDBSearch search, boolean isChecked) {
            // ..
        }

        @Override
        public void onSearchItemRemoved(final LDBSearch search) {
            mCompareSearchesPresenter.removeSelectedSearch(search);
            mRvAdapterSearchHistory.checkSearch(search, false);
            mCompareSearchesPresenter.validateSelectedSearches(search, false);
        }
    };

    private final SearchVisualizerSelectorAdapterContract.Client mCallbackSearchHistoryRv =
            new SearchVisualizerSelectorAdapterContract.Client() {

        @Override
        public void onSearchItemDetails(LDBSearch search) {
            updateConfirmBtn();
        }

        @Override
        public void onCheckableLDBSearchDetails(CheckableLDBSearch listItem) {
            mRvAdapterSearchHistory.checkSearch(listItem.getSearch(), !listItem.isChecked());
            mCompareSearchesPresenter.validateSelectedSearches(listItem.getSearch(), listItem.isChecked());
        }

        @Override
        public void onSearchChecked(LDBSearch search, boolean isChecked) {
            mCompareSearchesPresenter.validateSelectedSearches(search, isChecked);
        }

        @Override
        public void onSearchItemRemoved(final LDBSearch search) {
            // ..
        }
    };

    @Override
    public void updateSelectedItemsList(ArrayList<LDBSearch> items) {
        mRvAdapterSelectedItems.updateList(items);
        initMainContainerSelectSearches(mRvAdapterSearchHistory.getItemCount() > 0);
    }

    @Override
    public void updateConfirmBtn() {
        try {
            boolean enable = mCompareSearchesPresenter.getSelectedSearches().size() == 2;

            confirmBtn.setEnabled(enable);
            confirmBtn.setAlpha(enable? 1 : ConstantUtils.ALPHA_DISABLED_BUTTON);
            msgSelectItems.setVisibility(enable? View.GONE : View.VISIBLE);
            rvSearchHistory.setEnabled(!enable);
            rvSearchHistory.setAlpha(enable? ConstantUtils.ALPHA_DISABLED_LIST : 1);

        } catch (NullPointerException npe) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to initConfirmBtn");
        }
    }

    @Override
    public void displayMessage(int messageId) {
        onShowMessage(getString(messageId));
    }

    @Override
    public void unCheckSearch(LDBSearch search) {
        mRvAdapterSearchHistory.checkSearch(search, false);
        initMainContainerSelectSearches(mRvAdapterSearchHistory.getItemCount() > 0);
    }

    /**
     * Other methods: Flow 1 - Selection of the searches
     */
    private void updateSearchHistory(final ArrayList<LDBSearch> searchHistory) {

        try {
            rvSearchHistory.post(new Runnable() {
                @Override
                public void run() {
                    try {

                        if (mValidationUtils.isNotNull(searchHistory)) {
                            mRvAdapterSearchHistory.updateList(searchHistory);

                            // Clean the Dashboard page if the user has deleted all successful searches
                            if (searchHistory.isEmpty()) {
                                ((HomeActivity) getActivity()).setSearch(null);
                            }
                        }

                        initMainContainerSelectSearches(mRvAdapterSearchHistory.getItemCount() > 0);

                    } catch (NullPointerException npe) {
                        logUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                TAG + "Failed updateSearchHistory");
                    }
                }
            });

        } catch (NullPointerException npe) {
            logUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed updateSearchHistory");
        }
    }

    /**
     * Initialization: Flow 2 - Comparison of the searches
     */
    @Override
    public void startSearchesComparison() {
        try {
            StringUtils strUtils = new StringUtils();

            if (mCompareSearchesPresenter.getSelectedSearches().size() == 2) {
                setSearchA(mCompareSearchesPresenter.getSelectedSearches().get(0));
                setSearchB(mCompareSearchesPresenter.getSelectedSearches().get(1));

                // Update main UI
                initMainContainerSelectSearches(false);

                ((HomeActivity) getActivity()).initToolbarForComparePage(
                        "A: " + strUtils.getSummarizedSearchDescription(getSearchA()),
                        "B: " + strUtils.getSummarizedSearchDescription(getSearchB()));

                ((HomeActivity) getActivity()).updateBackgroundRootView(
                        HomePagerAdapter.IDX_COMPARE_OPTION,
                        true,
                        R.drawable.background_white,
                        R.color.color_white);

                // Initialize components used to compare the results of both searches
                initFloatingActionButtons();
                initComparisonComponents();

                // Recover the search results of each LDBSearch in the cache DB and load the
                // components used to compare the searches
                mCompareSearchesPresenter.loadReportsSelectedSearches(getSearchA(), getSearchB());
            }

        } catch (NullPointerException npe) {
            logUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed startSearchesComparison");
        }
    }

    private void initFloatingActionButtons() {
        try {
            ((HomeActivity) getActivity()).setCallbackToolbarFloatingActionButtons(
                    mCallbackFloatingActionButtons);

        } catch (NullPointerException npe) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to initFloatingActionButtons");
        }
    }

    private void initComparisonComponents() {
        try {
            initLoadValueCitizenDay(null, null, -1, -1);
            initLoadValueCitizenYear(null, null, -1, -1);
            initLoadValueInvestedOwnCity(null, null, -1, -1);
            initLoadValueInvestedOtherSources(null, null, -1, -1);
            initLoadTotalInvestments(null, null, null, null, null, null);
            initLoadExpenses(null, null, null, null);

        } catch (NullPointerException npe) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to initComparisonComponents");
        }
    }

    /**
     * Data: Flow 2 - Comparison of the searches
     */
    @Override
    public void initLoadValueCitizenDay(final LDBSearch searchA, final LDBSearch searchB,
                                        final double valueA, final double valueB) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        containerCompareValueCitizenDay.invalidate();
                        mViewsPhotographer.lockShareableSelector(COMPARE_SEARCHES_SHAREABLE_CONTENT_P1,true);

                        if (mCValueCitizenDay != null) {
                            mCValueCitizenDay.show(false);
                        }

                        mCValueCitizenDay = new ComponentCompareHorizontalBarChart(
                                containerCompareValueCitizenDay,
                                getResources(),
                                getString(R.string.component_compare_horizontal_bar_chart_value_citizen_day));

                        if (valueA >= 0 && valueB >= 0) {
                            lblValuesPeriod.setVisibility(View.VISIBLE);

                            mCValueCitizenDay.loadComponent(
                                    getContext().getApplicationContext(),
                                    R.drawable.ico_person_colorful,
                                    getString(R.string.component_compare_horizontal_bar_chart_value_citizen_day),
                                    (float) valueA,
                                    (float) valueB,
                                    searchA.getCity(),
                                    searchB.getCity());

                            mCValueCitizenDay.show(true);

                            mViewsPhotographer.lockShareableSelector(COMPARE_SEARCHES_SHAREABLE_CONTENT_P1, false);
                        } else {
                            lblValuesPeriod.setVisibility(View.GONE);
                        }

                    } catch (Exception ignored) {
                        logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                                TAG + " Failed to initValueCitizenDay");
                    }
                }
            });
        }
    }

    @Override
    public void initLoadValueCitizenYear(final LDBSearch searchA, final LDBSearch searchB,
                                         final double valueA, final double valueB) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        containerCompareValueCitizenYear.invalidate();

                        if (mCValueCitizenYear != null) {
                            mCValueCitizenYear.show(false);
                        }

                        mCValueCitizenYear = new ComponentCompareHorizontalBarChart(
                                containerCompareValueCitizenYear,
                                getResources(),
                                getString(R.string.component_compare_horizontal_bar_chart_value_citizen_year));

                        if (valueA >= 0 && valueB >= 0) {
                            mCValueCitizenYear.loadComponent(
                                    getContext().getApplicationContext(),
                                    R.drawable.ico_city_colorful,
                                    getString(R.string.component_compare_horizontal_bar_chart_value_citizen_year),
                                    (float) valueA,
                                    (float) valueB,
                                    searchA.getCity(),
                                    searchB.getCity());

                            mCValueCitizenYear.show(true);
                        }

                    } catch (Exception ignored) {
                        logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                                TAG + " Failed to initLoadValueCitizenYear");
                    }
                }
            });
        }
    }

    @Override
    public void initLoadValueInvestedOwnCity(final LDBSearch searchA, final LDBSearch searchB,
                                             final double valueA, final double valueB) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        containerCompareValueInvestedOwnCity.invalidate();
                        mViewsPhotographer.lockShareableSelector(COMPARE_SEARCHES_SHAREABLE_CONTENT_P2,true);

                        if (mCValueInvestedOwnCity != null) {
                            mCValueInvestedOwnCity.show(false);
                        }

                        mCValueInvestedOwnCity = new ComponentCompareHorizontalBarChart(
                                containerCompareValueInvestedOwnCity,
                                getResources(),
                                getString(R.string.component_compare_horizontal_bar_chart_investments_own_city));

                        if (valueA >= 0 && valueB >= 0) {
                            mCValueInvestedOwnCity.loadComponent(
                                    getContext().getApplicationContext(),
                                    R.drawable.ico_state_colorful,
                                    getString(R.string.component_compare_horizontal_bar_chart_investments_own_city),
                                    (float) valueA,
                                    (float) valueB,
                                    searchA.getCity(),
                                    searchB.getCity());

                            mCValueInvestedOwnCity.show(true);

                            mViewsPhotographer.lockShareableSelector(COMPARE_SEARCHES_SHAREABLE_CONTENT_P2,false);
                        }

                    } catch (Exception ignored) {
                        logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                                TAG + " Failed to initLoadValueInvestedOwnCity");
                    }
                }
            });
        }
    }

    @Override
    public void initLoadValueInvestedOtherSources(final LDBSearch searchA, final LDBSearch searchB,
                                                  final double valueA, final double valueB) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        containerCompareValueInvestedOtherSources.invalidate();

                        if (mCValueInvestedOthersCity != null) {
                            mCValueInvestedOthersCity.show(false);
                        }

                        mCValueInvestedOthersCity = new ComponentCompareHorizontalBarChart(
                                containerCompareValueInvestedOtherSources,
                                getResources(),
                                getString(R.string.component_compare_horizontal_bar_chart_investments_other_sources));

                        if (valueA >= 0 && valueB >= 0) {
                            mCValueInvestedOthersCity.loadComponent(
                                    getContext().getApplicationContext(),
                                    R.drawable.ico_brazil_colorful,
                                    getString(R.string.component_compare_horizontal_bar_chart_investments_other_sources),
                                    (float) valueA,
                                    (float) valueB,
                                    searchA.getCity(),
                                    searchB.getCity());

                            mCValueInvestedOthersCity.show(true);
                        }

                    } catch (Exception ignored) {
                        logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                                TAG + " Failed to initLoadValueInvestedOtherSources");
                    }
                }
            });
        }
    }

    @Override
    public void initLoadTotalInvestments(final LDBSearch searchA, final LDBSearch searchB,
                                         final ArrayList<Double> datasetA,
                                         final ArrayList<Double> datasetB,
                                         final ArrayList<String> labelsA,
                                         final ArrayList<String> labelsB) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        containerCompareTotalInvestments.invalidate();
                        mViewsPhotographer.lockShareableSelector(COMPARE_SEARCHES_SHAREABLE_CONTENT_P3,true);

                        if (mCCompareInvestments != null) {
                            mCCompareInvestments.show(false);
                        }

                        mCCompareInvestments = new ComponentCompareTotalInvestments(
                                containerCompareTotalInvestments,
                                getResources());

                        if (mValidationUtils.isValidList(datasetA) && mValidationUtils.isValidList(datasetB)) {
                            lblInvestmentsPH.setVisibility(View.VISIBLE);

                            mCCompareInvestments.loadValuesForChart(
                                    getContext().getApplicationContext(),
                                    true,
                                    datasetA,
                                    ColorUtils.getColorsInvestmentsForCityPH(),
                                    labelsA,
                                    String.format(getString(R.string.compare_prefix_lbl_investments_in),
                                            searchA.getCity()),
                                    searchA.getCity(),
                                    searchA.getState());

                            mCCompareInvestments.loadValuesForChart(
                                    getContext().getApplicationContext(),
                                    false,
                                    datasetB,
                                    ColorUtils.getColorsInvestmentsForCityPH(),
                                    labelsB,
                                    String.format(getString(R.string.compare_prefix_lbl_investments_in),
                                            searchB.getCity()),
                                    searchB.getCity(),
                                    searchB.getState());

                            mCCompareInvestments.show(true);

                            mViewsPhotographer.lockShareableSelector(COMPARE_SEARCHES_SHAREABLE_CONTENT_P3,false);
                        } else {
                            lblInvestmentsPH.setVisibility(View.GONE);
                        }

                    } catch (Exception ignored) {
                        logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                                TAG + " Failed to initLoadValueInvestedOtherSources");
                    }
                }
            });
        }
    }

    @Override
    public void initLoadExpenses(final LDBSearch searchA, final LDBSearch searchB,
                                 final ArrayList<LDBExpense> expensesA,
                                 final ArrayList<LDBExpense> expensesB) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        containerCompareExpenses.invalidate();
                        mViewsPhotographer.lockShareableSelector(COMPARE_SEARCHES_SHAREABLE_CONTENT_P4,true);

                        if (mCCompareExpenses != null) {
                            mCCompareExpenses.show(false);
                        }

                        mCCompareExpenses = new ComponentCompareExpenses(
                                getContext().getApplicationContext(),
                                containerCompareExpenses,
                                getResources(),
                                searchA.getCity(),
                                searchB.getCity());

                        if (mValidationUtils.isValidList(expensesA) || mValidationUtils.isValidList(expensesB)) {
                            lblDeclaredExpenses.setVisibility(View.VISIBLE);

                            mCCompareExpenses.loadDeclaredExpenses(
                                    expensesA,
                                    searchA.getCity(),
                                    expensesB,
                                    searchB.getCity());

                            mCCompareExpenses.show(true);

                            mViewsPhotographer.lockShareableSelector(COMPARE_SEARCHES_SHAREABLE_CONTENT_P4,false);

                        } else {
                            lblDeclaredExpenses.setVisibility(View.GONE);
                        }

                    } catch (Exception ignored) {
                        logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                                TAG + " Failed to initLoadValueInvestedOtherSources");
                    }
                }
            });
        }
    }

    /**
     * Events: Flow 2 - Comparison of the searches
     */
    private final ViewTreeObserver.OnScrollChangedListener mPageScrollerListener =
            new ViewTreeObserver.OnScrollChangedListener() {

                @Override
                public void onScrollChanged() {
                    scrollTop();
                    updateVerticalScrollIndicator();
                }
            };

    @OnClick({R.id.compare_searches_btn_confirm})
    public void onClickBtnConfirm() {
        startSearchesComparison();
    }

    private final ComponentFloatingActionButtonsContract mCallbackFloatingActionButtons =
            new ComponentFloatingActionButtonsContract() {

                @Override
                public void onFavoriteBtnClicked() {
                    // ..
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

    /**
     * Other methods: Flow 2 - Comparison of the searches
     */
    private void closeDialog() {

        if (mFAQDialog != null) {
            mFAQDialog.closeDialog();
            mFAQDialog = null;
        }
    }

    private void scrollTop() {

        if (mIsUpdatingScreenAfterSearch) {
            mIsUpdatingScreenAfterSearch = false;
            compareSearchesScrollView.setSmoothScrollingEnabled(true);
            compareSearchesScrollView.smoothScrollTo(0,0);
        }
    }

    private void updateVerticalScrollIndicator() {
        try {

            if (compareSearchesScrollView.getHeight() > 0) {
                float yPercentageTop = (compareSearchesScrollView.getScrollY() * 100) /
                        compareSearchesMainContainer.getHeight();

                if (getActivity() != null) {
                    ((HomeActivity) getActivity()).updateToolbarPageScrollerIndicator(yPercentageTop);
                }
            }

        } catch (Exception e) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to initMainContainerSelectSearches");
        }
    }

    /**
     * Initialization: Flow 3 - Share
     */
    @Override
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
    public void initShareableContentSelectors() {
        try {
            mBtnsConfirmCancelSharing.setVisibility(View.GONE);

            // Pass the selector of the shareable views into the component
            ArrayList<ComponentShareableContentSelector> shareContentSelectorViews = new ArrayList<>();

            shareContentSelectorViews.add(new ComponentShareableContentSelector(
                    containerShareableContentSelectorP1,
                    containerShareableContentP1,
                    COMPARE_SEARCHES_SHAREABLE_CONTENT_P1));

            shareContentSelectorViews.add(new ComponentShareableContentSelector(
                    containerShareableContentSelectorP2,
                    containerShareableContentP2,
                    COMPARE_SEARCHES_SHAREABLE_CONTENT_P2));

            shareContentSelectorViews.add(new ComponentShareableContentSelector(
                    containerShareableContentSelectorP3,
                    containerShareableContentP3,
                    COMPARE_SEARCHES_SHAREABLE_CONTENT_P3));

            shareContentSelectorViews.add(new ComponentShareableContentSelector(
                    containerShareableContentSelectorP4,
                    containerShareableContentP4,
                    COMPARE_SEARCHES_SHAREABLE_CONTENT_P4));

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

            if (basicValidators.isValidSearch(getSearchA()) && basicValidators.isValidSearch(getSearchB())) {
                SharedContent sharedContent = new SharedContent();
                sharedContent.setListPathImgSharedContent(mViewsPhotographer.getAbsolutePathSharedImgs());
                sharedContent.setSelectedSocialNetworkId(mSocialNetPicker.getSelectedSocialNetworkId());
                sharedContent.setSharingComparison(true);
                sharedContent.setSearchA(getSearchA());
                sharedContent.setSearchB(getSearchB());

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
     * Finalization
     */
    @Override
    public void onStop() {

        if (mCompareSearchesPresenter != null) {
            mCompareSearchesPresenter.unbindView();
        }

        super.onStop();
    }

    /**
     * Getters and Setters
     */
    public LDBSearch getSearchA() {
        return mLDBSearchA;
    }

    public void setSearchA(LDBSearch searchA) {
        mLDBSearchA = searchA;
    }

    public LDBSearch getSearchB() {
        return mLDBSearchB;
    }

    public void setSearchB(LDBSearch searchB) {
        mLDBSearchB = searchB;
    }

}