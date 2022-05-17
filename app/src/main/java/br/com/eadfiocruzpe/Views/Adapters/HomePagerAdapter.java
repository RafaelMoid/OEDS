package br.com.eadfiocruzpe.Views.Adapters;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Contracts.HomePagerAdapterContract;
import br.com.eadfiocruzpe.Utils.ConstantUtils;
import br.com.eadfiocruzpe.Utils.LogUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;
import br.com.eadfiocruzpe.Persistence.PreferencesManager;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBSearch;
import br.com.eadfiocruzpe.Views.Pages.Home.Compare.CompareSearchesFragment;
import br.com.eadfiocruzpe.Views.Pages.Home.Dashboard.DashboardFragment;
import br.com.eadfiocruzpe.Views.Pages.Home.SearchHistory.SearchHistoryFavoritesFragment;
import br.com.eadfiocruzpe.Views.Pages.Home.Settings.SettingsFragment;

/**
 * This pager adapter is used to handle the events of the main menu, the tabbed layout at the
 * bottom of the HomeActivity.
 */
public class HomePagerAdapter extends FragmentStatePagerAdapter implements
        HomePagerAdapterContract.Manager {

    public static final String TAG = "HomePagerAdapter";

    public static final int IDX_DASHBOARD_OPTION = 0;
    public static final int IDX_SEARCH_HISTORY_OPTION = 1;
    public static final int IDX_COMPARE_OPTION = 2;
    public static final int IDX_SETTINGS_OPTION = 3;

    private int[] MAIN_MENU_ICONS = {
            R.drawable.ico_tab_search_white,
            R.drawable.ico_tab_history_favorites_white,
            R.drawable.ico_tab_compare_white,
            R.drawable.ico_tab_about_info_white
    };
    private int[] MAIN_MENU_ICONS_SELECTED = {
            R.drawable.ico_tab_search_white,
            R.drawable.ico_tab_history_favorites_white,
            R.drawable.ico_tab_compare_white,
            R.drawable.ico_tab_about_info_white
    };
    private int[] MAIN_MENU_TEXT = {
            R.string.menu_opt_search,
            R.string.menu_opt_favorites_history,
            R.string.menu_opt_compare,
            R.string.menu_opt_about
    };

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private HomePagerAdapterContract.Client mCallbackHomePagerAdapter;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private LogUtils mLogUtils = new LogUtils();

    private LDBSearch mLDBSearch = null;

    public static int sClickedTab = ConstantUtils.UNSELECTED_TAB_IDX;

    /**
     * Initialization
     */
    public HomePagerAdapter(FragmentManager manager, HomePagerAdapterContract.Client hpaClient) {
        super(manager);
        mCallbackHomePagerAdapter = hpaClient;
    }

    @Override
    public void addFragment(Fragment fragment) {
        mFragmentList.add(fragment);
    }

    @Override
    public void setupFragmentsPagination() {
        getViewPager().setAdapter(this);
        getTabLayout().setupWithViewPager(getViewPager());
        setupTabViews();
        initEvents();
    }

    @Override
    public void setupTabViews() {

        for (int i = 0; i < getTabLayout().getTabCount(); i++) {

            if (getTabLayout().getTabAt(i) != null) {
                @SuppressLint("InflateParams")
                LinearLayout tabView = (LinearLayout) LayoutInflater
                        .from(mCallbackHomePagerAdapter.getAppContext())
                        .inflate(R.layout.component_tab, null);

                if (i == IDX_DASHBOARD_OPTION) {
                    setTabView(tabView, i, true);
                } else {
                    setTabView(tabView, i, false);
                }
            }
        }
    }

    @Override
    public void setTabView(LinearLayout tabView, int tabIdx, boolean selected) {

        if (tabView != null) {
            ImageView tabImage = tabView.findViewById(R.id.tab_icon);
            TextView tabText = tabView.findViewById(R.id.tab_text);
            TabLayout.Tab tab = getTabLayout().getTabAt(tabIdx);

            if (tabImage != null && tabText != null && tab != null) {
                // Sets the tab's content
                tabText.setText(mCallbackHomePagerAdapter.getAppContext().getString(MAIN_MENU_TEXT[tabIdx]));

                if (selected) {
                    tabImage.setImageDrawable(ContextCompat.getDrawable(mCallbackHomePagerAdapter.getAppContext(), MAIN_MENU_ICONS_SELECTED[tabIdx]));

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        tabView.setBackground(ContextCompat.getDrawable(mCallbackHomePagerAdapter.getAppContext(), R.drawable.background_green_gradient_2));
                    } else {
                        tabView.setBackgroundColor(ContextCompat.getColor(mCallbackHomePagerAdapter.getAppContext(), R.color.color_green_blue_5));
                    }
                } else {
                    tabImage.setImageDrawable(ContextCompat.getDrawable(mCallbackHomePagerAdapter.getAppContext(), MAIN_MENU_ICONS[tabIdx]));

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        tabView.setBackground(ContextCompat.getDrawable(mCallbackHomePagerAdapter.getAppContext(), R.drawable.background_green_gradient_1));
                    } else {
                        tabView.setBackgroundColor(ContextCompat.getColor(mCallbackHomePagerAdapter.getAppContext(), R.color.color_green_blue_4));
                    }
                }

                // Sets the tab' layout
                try {
                    // noinspection ConstantConditions
                    getTabLayout().getTabAt(tabIdx).setCustomView(tabView);

                    // noinspection ConstantConditions
                    LinearLayout tabParent = (LinearLayout) getTabLayout().getTabAt(tabIdx).getCustomView().getParent();

                    // noinspection ConstantConditions
                    if (tabParent != null) {
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                        lp.setMargins(0, 0, 0, 0);
                        lp.weight = MAIN_MENU_ICONS.length;

                        tabParent.setLayoutParams(lp);
                        tabParent.setPadding(0, 0, 0, 0);
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        }
    }

    @Override
    public void restoreSelectedTab() {

        new CountDownTimer(ConstantUtils.TIMEOUT_RESTORE_FRAGMENT, 100) {
            @Override
            public void onTick(long l) {}

            @Override
            public void onFinish() {

                if (mCallbackHomePagerAdapter != null) {
                    int selectedMenuOpt = PreferencesManager.getSelectedHomeMenuOption();

                    if (selectedMenuOpt != ConstantUtils.UNSELECTED_TAB_IDX) {
                        sClickedTab = selectedMenuOpt;
                    } else {
                        sClickedTab = IDX_DASHBOARD_OPTION;
                    }

                    reloadSelectedFragment(sClickedTab);
                }
            }
        }.start();
    }

    /**
     * Events
     */
    @Override
    public void initEvents() {
        getTabLayout().addOnTabSelectedListener(mTabSelectedListener);
        getViewPager().addOnPageChangeListener(mPageChangedListener);
    }

    private TabLayout.OnTabSelectedListener mTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            sClickedTab = tab.getPosition();
            PreferencesManager.setSelectedHomeMenuOption(sClickedTab);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            // ..
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

            // If the currently selected page is the Search page and the user has clicked on the
            // search tab, open the Search dialog
            if (sClickedTab == IDX_DASHBOARD_OPTION &&
                tab.getPosition() == IDX_DASHBOARD_OPTION) {

                if (mCallbackHomePagerAdapter != null) {

                    if (getSearch() != null) {
                        mCallbackHomePagerAdapter.openSearchDialog(getSearch());
                    } else {
                        mCallbackHomePagerAdapter.openSearchDialog("");
                    }
                }
            }
        }
    };

    private ViewPager.OnPageChangeListener mPageChangedListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // ..
        }

        @Override
        public void onPageSelected(int position) {
            reloadSelectedFragment(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            // ..
        }
    };

    public void selectTab(TabLayout.Tab tab) {

        if (tab != null) {
            tab.select();
        }
    }

    @Override
    public void reloadSelectedFragment(int position) {
        try {
            mCallbackHomePagerAdapter.onReloadSelectedFragment();
            updateSelectedTab(position);
            updateSelectedFragment(position);
            updateSelectedFragmentContent(position);

        } catch (Exception e) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to reloadSelectedFragment " + e.getMessage());
        }
    }

    @Override
    public void updateSelectedTab(int selectedTabIdx) {

        for (int i = 0; i < getTabLayout().getTabCount(); i++) {
            TabLayout.Tab tab = getTabLayout().getTabAt(i);

            if (tab != null) {
                LinearLayout tabView = (LinearLayout) tab.getCustomView();

                if (tabView != null) {

                    if (i == selectedTabIdx) {
                        setTabView(tabView, i, true);

                    } else if (getTabLayout().getTabAt(i) != null) {
                        setTabView(tabView, i, false);
                    }
                }
            }
        }
    }

    @Override
    public void updateSelectedFragment(int selectedTabIdx) {

        if (selectedTabIdx == HomePagerAdapter.IDX_DASHBOARD_OPTION ||
                selectedTabIdx == HomePagerAdapter.IDX_SEARCH_HISTORY_OPTION ||
                    selectedTabIdx == HomePagerAdapter.IDX_SETTINGS_OPTION ||
                        selectedTabIdx == HomePagerAdapter.IDX_COMPARE_OPTION) {
            getViewPager().setCurrentItem(selectedTabIdx);
        }
    }

    @Override
    public void updateSelectedFragmentContent(int selectedTabIdx) {
        try {

            if (sClickedTab != ConstantUtils.UNSELECTED_TAB_IDX) {

                switch (selectedTabIdx) {

                    case HomePagerAdapter.IDX_DASHBOARD_OPTION: {

                        if (!PreferencesManager.isLandingOnInitialPage() && mCallbackHomePagerAdapter != null) {

                            // This is the place where you would put a condition to open the search
                            // dialog automatically on a click on the Dashboard tab after the initialization of the app.
                            if (getSearch() != null) {
                                ((DashboardFragment) getItem(selectedTabIdx)).init(getSearch());
                            }
                        } else {
                            ((DashboardFragment) getItem(selectedTabIdx)).init(getSearch());
                        }

                        PreferencesManager.setIsLandingOnInitialPage(false);

                        break;
                    }

                    case HomePagerAdapter.IDX_SEARCH_HISTORY_OPTION: {
                        ((SearchHistoryFavoritesFragment) getItem(selectedTabIdx)).init();
                        break;
                    }

                    case HomePagerAdapter.IDX_COMPARE_OPTION: {
                        ((CompareSearchesFragment) getItem(selectedTabIdx)).init();
                        break;
                    }

                    case HomePagerAdapter.IDX_SETTINGS_OPTION: {
                        ((SettingsFragment) getItem(selectedTabIdx)).init();
                        break;
                    }
                }
            } else {
                ((DashboardFragment) getItem(HomePagerAdapter.IDX_DASHBOARD_OPTION)).init(getSearch());
            }
        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Warning: could not updateSelectedFragmentContent");
        }
    }

    /**
     * Getters and Setters
     */
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public void setTabLayout(TabLayout tabLayout) {
        mTabLayout = tabLayout;
    }

    @Override
    public TabLayout getTabLayout() {
        return mTabLayout;
    }

    @Override
    public void setViewPager(ViewPager viewPager) {
        mViewPager = viewPager;
    }

    @Override
    public ViewPager getViewPager() {
        return mViewPager;
    }

    /**
     * Getters and Setters that manage the data passed from one page to another
     */
    public void setSearch(LDBSearch search) {
        mLDBSearch = search;
    }

    public LDBSearch getSearch() {
        return mLDBSearch;
    }

}