package br.com.eadfiocruzpe.Views.Pages.Home;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;

import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Contracts.CToolbarCallbackContract;
import br.com.eadfiocruzpe.Contracts.NewSearchDialogCallbackContract;
import br.com.eadfiocruzpe.Contracts.HomePagerAdapterContract;
import br.com.eadfiocruzpe.Utils.ConstantUtils;
import br.com.eadfiocruzpe.Utils.ImageUtils;
import br.com.eadfiocruzpe.Utils.PermissionUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBSearch;
import br.com.eadfiocruzpe.Persistence.PreferencesManager;
import br.com.eadfiocruzpe.Views.Validators.BasicValidators;
import br.com.eadfiocruzpe.Views.Pages.Home.Compare.CompareSearchesFragment;
import br.com.eadfiocruzpe.Views.Pages.Home.SearchHistory.SearchHistoryFavoritesFragment;
import br.com.eadfiocruzpe.Views.Pages.Home.Settings.SettingsFragment;
import br.com.eadfiocruzpe.Views.Pages.Global.BaseActivity;
import br.com.eadfiocruzpe.Views.Pages.Home.Dashboard.DashboardFragment;
import br.com.eadfiocruzpe.Views.Components.ComponentToolbar;
import br.com.eadfiocruzpe.Views.Dialogs.NewSearchDialog;
import br.com.eadfiocruzpe.Views.Adapters.HomePagerAdapter;

import static br.com.eadfiocruzpe.Views.Adapters.HomePagerAdapter.IDX_COMPARE_OPTION;
import static br.com.eadfiocruzpe.Views.Adapters.HomePagerAdapter.IDX_DASHBOARD_OPTION;
import static br.com.eadfiocruzpe.Views.Adapters.HomePagerAdapter.IDX_SEARCH_HISTORY_OPTION;
import static br.com.eadfiocruzpe.Views.Adapters.HomePagerAdapter.IDX_SETTINGS_OPTION;

public class HomeActivity extends BaseActivity implements HomePagerAdapterContract.Client {

    private final String TAG = "HomeActivity";

    @BindView(R.id.root)
    public CoordinatorLayout rootView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.home_view_pager)
    ViewPager viewPager;

    @BindView(R.id.home_tab_menu)
    TabLayout tabLayout;

    @BindView(R.id.progress_bar_container)
    public View progressBarContainer;

    private HomePagerAdapter mHomeAdapter;
    private NewSearchDialog mSearchDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mHomeAdapter != null) {
            mHomeAdapter.restoreSelectedTab();
        }
    }

    /**
     * Initialization
     */
    private void initViews() {
        try {
            initMainView(rootView);
            initProgressView(progressBarContainer);
            initToolbar();
            initHomeAdapter(viewPager, tabLayout);

        } catch(Exception e) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to initViews: " + e.getMessage());
        }
    }

    public void initToolbar() {
        // Set the basic attributes of the toolbar
        initToolbar(false, "Dashboard", toolbar);

        // Set if the search input is going to be displayed and what the page title is
        if (HomePagerAdapter.sClickedTab != ConstantUtils.UNSELECTED_TAB_IDX) {

            switch (HomePagerAdapter.sClickedTab) {

                case IDX_DASHBOARD_OPTION:{
                    initToolbarForDashboardPage();
                    break;
                }

                case IDX_SEARCH_HISTORY_OPTION:{
                    initToolbarForHistoryPage();
                    break;
                }

                case IDX_COMPARE_OPTION:{
                    initToolbarForComparePage();
                    break;
                }

                case IDX_SETTINGS_OPTION:{
                    initToolbarForSettingsPage();
                    break;
                }
            }
        } else {
            initToolbarForDashboardPage();
        }
    }

    private void emptyCurrentSearchAfterBackClick() {

        if (mHomeAdapter != null) {

            // Remove the current LDBSearch and refresh the DashboardFragment
            if (PreferencesManager.getSelectedHomeMenuOption() == IDX_DASHBOARD_OPTION) {
                ((DashboardFragment) mHomeAdapter.getItem(IDX_DASHBOARD_OPTION)).init(null);
                mHomeAdapter.setSearch(null);
            }
        }
    }

    private void initToolbarForDashboardPage() {

        if (mHomeAdapter != null) {

            if (mHomeAdapter.getSearch() != null) {
                toolbar.setVisibility(View.VISIBLE);
                setMainToolbarItem(ComponentToolbar.IDX_MAIN_TOOLBAR_ITEM_PAGE_TITLE);
                setToolbarStyle(ComponentToolbar.TOOLBAR_STYLE_DARK);
                updateToolbarTitle(getResources().getString(R.string.toolbar_search));
                updateToolbarIcon(false, null);
                addSearchToUI(mHomeAdapter.getSearch());
                showSearchBtn(false);
                showBackBtn(true);
                setCallbackBtnBackSearch();
            } else {
                toolbar.setVisibility(View.GONE);
            }
        }
    }

    private void initToolbarForHistoryPage() {
        toolbar.setVisibility(View.GONE);
        setMainToolbarItem(ComponentToolbar.IDX_MAIN_TOOLBAR_ITEM_PAGE_TITLE);
        setToolbarStyle(ComponentToolbar.TOOLBAR_STYLE_LIGHT);
        updateToolbarTitle(getResources().getString(R.string.search_history_page_title));
        updateToolbarIcon(false, null);
        showSearchBtn(false);
        showBackBtn(false);
    }

    private void initToolbarForComparePage() {
        toolbar.setVisibility(View.VISIBLE);
        setMainToolbarItem(ComponentToolbar.IDX_MAIN_TOOLBAR_ITEM_PAGE_TITLE);
        setToolbarStyle(ComponentToolbar.TOOLBAR_STYLE_TRANSPARENT);
        updateToolbarTitle(getResources().getString(R.string.compare_page_title));
        updateToolbarIcon(true, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ico_tab_compare_white));
        showSearchBtn(false);
        showBackBtn(false);
    }

    public void initToolbarForComparePage(String descSearchA, String descSearchB) {
        toolbar.setVisibility(View.VISIBLE);
        setMainToolbarItem(ComponentToolbar.IDX_MAIN_TOOLBAR_ITEM_PAGE_TITLE_COMPARISON);
        setToolbarStyle(ComponentToolbar.TOOLBAR_STYLE_DARK);
        updateToolbarTitle(descSearchA, descSearchB);
        updateToolbarIcon(true, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ico_tab_compare_white));
        showSearchBtn(false);
        showBackBtn(true);
        setCallbackBtnBackComparisonSelection();
    }

    private void initToolbarForSettingsPage() {
        toolbar.setVisibility(View.VISIBLE);
        setMainToolbarItem(ComponentToolbar.IDX_MAIN_TOOLBAR_ITEM_PAGE_TITLE);
        setToolbarStyle(ComponentToolbar.TOOLBAR_STYLE_LIGHT);
        updateToolbarTitle(getResources().getString(R.string.settings_page_title));
        updateToolbarIcon(true, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ico_info_black));
        showSearchBtn(false);
        showBackBtn(false);
    }

    private void initHomeAdapter(ViewPager viewPager, TabLayout tabLayout) {
        mHomeAdapter = new HomePagerAdapter(getSupportFragmentManager(), this);

        mHomeAdapter.addFragment(new DashboardFragment());
        mHomeAdapter.addFragment(new SearchHistoryFavoritesFragment());
        mHomeAdapter.addFragment(new CompareSearchesFragment());
        mHomeAdapter.addFragment(new SettingsFragment());

        mHomeAdapter.setViewPager(viewPager);
        mHomeAdapter.setTabLayout(tabLayout);
        mHomeAdapter.setupFragmentsPagination();
    }

    private void addSearchToUI(LDBSearch search) {
        BasicValidators basicValidators = new BasicValidators();

        if (basicValidators.isValidSearch(search) && ImageUtils.mapStateFlagImg != null) {
            setToolbarSearchDescription(
                    search.getCity(),
                    search.getState(),
                    ImageUtils.getStateFlag(search.getState()),
                    search.getYear(),
                    search.getPeriodYear());

            setMainToolbarItem(ComponentToolbar.IDX_MAIN_TOOLBAR_ITEM_PAGE_SEARCH_DESCRIPTION);
        }
    }

    /**
     * Events
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        try {

            switch (requestCode) {

                case PermissionUtils.IDX_PERMISSION_WRITE_EXTERNAL_STORAGE: {

                    if (HomePagerAdapter.sClickedTab == IDX_DASHBOARD_OPTION) {
                        PermissionUtils.setExternalStoragePermissionGranted(grantResults);
                        mHomeAdapter.reloadSelectedFragment(IDX_DASHBOARD_OPTION);
                    }

                    if (HomePagerAdapter.sClickedTab == IDX_COMPARE_OPTION) {
                        PermissionUtils.setExternalStoragePermissionGranted(grantResults);
                        mHomeAdapter.reloadSelectedFragment(IDX_COMPARE_OPTION);
                    }
                }
            }

        } catch(NullPointerException npe) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to execute onRequestPermissionsResult.");
        }
    }

    /**
     * Set a callback behavior for the toolbar:
     *   - Click on the search button : Opens a new search dialog
     *   - Click on the back button   : Navigate to the initial page
     */
    private void setCallbackBtnBackSearch() {
        setCallbackToolbarButtons(new CToolbarCallbackContract() {
            @Override
            public void onSearchButtonClicked() {

                if (mSearchDialog == null) {
                    mSearchDialog = new NewSearchDialog(HomeActivity.this, mCallbackNewSearchDialog, "");
                }
            }

            @Override
            public void onBackBtnClicked() {
                emptyCurrentSearchAfterBackClick();
                initToolbarForDashboardPage();
            }
        });
    }

    /**
     * Set a callback behavior for the toolbar:
     *   - Click on the search button : Do nothing
     *   - Click on the back button   : Navigate back to the initial flow of the Comparison page
     */
    private void setCallbackBtnBackComparisonSelection() {
        setCallbackToolbarButtons(new CToolbarCallbackContract() {
            @Override
            public void onSearchButtonClicked() {
                // ..
            }

            @Override
            public void onBackBtnClicked() {
                mHomeAdapter.reloadSelectedFragment(IDX_COMPARE_OPTION);
            }
        });
    }

    @Override
    public void onReloadSelectedFragment() {
        initToolbar();
    }

    @Override
    public void openSearchDialog(String searchTerm) {

        if (mSearchDialog == null) {
            mSearchDialog = new NewSearchDialog(HomeActivity.this, mCallbackNewSearchDialog, searchTerm);
        } else {
            mSearchDialog.setSearchTerms(searchTerm);
        }

        mSearchDialog.show();
    }

    @Override
    public void openSearchDialog(LDBSearch search) {

        if (mSearchDialog == null) {
            mSearchDialog = new NewSearchDialog(HomeActivity.this, mCallbackNewSearchDialog, search.getCity());
        } else {
            mSearchDialog.setSearchTerms(search.getCity());
        }

        mSearchDialog.loadCurrentSearch(search);
        mSearchDialog.show();
    }

    private final NewSearchDialogCallbackContract mCallbackNewSearchDialog = new NewSearchDialogCallbackContract() {
        @Override
        public void onActionConfirmed(final LDBSearch search) {

            rootView.post(new Runnable() {
                @Override
                public void run() {
                    BasicValidators basicValidators = new BasicValidators();

                    if (basicValidators.isValidSearch(search)) {
                        setSearch(search);
                        mHomeAdapter.reloadSelectedFragment(IDX_DASHBOARD_OPTION);
                        addSearchToUI(search);
                    }
                }
            });
        }

        @Override
        public void onShowDialogMessage(String message) {
            showMessage(message);
        }
    };

    /**
     * UI
     */
    public void updateBackgroundRootView(int callerId, boolean drawable, int resourceId, int backupColorId) {

        if (rootView != null && HomePagerAdapter.sClickedTab == callerId) {

            if (drawable) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    rootView.setBackground(ContextCompat.getDrawable(getApplicationContext(), resourceId));
                } else {
                    rootView.setBackgroundColor(backupColorId);
                }
            } else {
                rootView.setBackgroundColor(resourceId);
            }
        }
    }

    /**
     * Navigation
     *
     * This method programmatically navigates to the selected page and updates the LDBSearch object that
     * was set in the previous page.
     */
    public void navigateToPage(final int pageIdx, final LDBSearch search) {

        switch (pageIdx) {

            case IDX_DASHBOARD_OPTION: {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        BasicValidators basicValidators = new BasicValidators();
                        TabLayout.Tab tab = mHomeAdapter.getTabLayout().getTabAt(IDX_DASHBOARD_OPTION);

                        if (basicValidators.isNotNull(search)) {
                            setSearch(search);
                            mHomeAdapter.selectTab(tab);
                        }
                    }
                });

                break;
            }
        }
    }

    /**
     * Getters and Setters
     */
    public void setSearch(LDBSearch search) {

        if (mHomeAdapter != null) {
            mHomeAdapter.setSearch(search);
        }
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public Context getAppContext() {
        return getApplicationContext();
    }

}