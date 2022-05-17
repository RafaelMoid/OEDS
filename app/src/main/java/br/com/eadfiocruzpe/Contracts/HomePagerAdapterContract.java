package br.com.eadfiocruzpe.Contracts;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBSearch;

public interface HomePagerAdapterContract {

    interface Manager {

        void addFragment(Fragment fragment);

        void setupFragmentsPagination();

        void setupTabViews();

        void setTabView(LinearLayout tabView, int tabIdx, boolean selected);

        void restoreSelectedTab();

        void initEvents();

        void reloadSelectedFragment(int position);

        void updateSelectedTab(int selectedTabIdx);

        void updateSelectedFragment(int selectedTabIdx);

        void updateSelectedFragmentContent(int selectedTabIdx);

        void setTabLayout(TabLayout tabLayout);

        void setViewPager(ViewPager viewPager);

        TabLayout getTabLayout();

        ViewPager getViewPager();

    }

    interface Client {

        Toolbar getToolbar();

        Context getAppContext();

        void onReloadSelectedFragment();

        void openSearchDialog(String searchTerm);

        void openSearchDialog(LDBSearch search);

    }

}