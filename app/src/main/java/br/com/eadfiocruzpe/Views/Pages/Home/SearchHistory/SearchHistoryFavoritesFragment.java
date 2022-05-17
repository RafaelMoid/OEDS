package br.com.eadfiocruzpe.Views.Pages.Home.SearchHistory;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.eadfiocruzpe.Utils.ImageUtils;
import br.com.eadfiocruzpe.Utils.UIUtils;
import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Utils.TypeUtils;
import br.com.eadfiocruzpe.Utils.ConstantUtils;
import br.com.eadfiocruzpe.Contracts.SearchVisualizerSelectorAdapterContract;
import br.com.eadfiocruzpe.Contracts.SearchHistoryFavoritesContract;
import br.com.eadfiocruzpe.Contracts.SearchHistoryAdapterContract;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBSearch;
import br.com.eadfiocruzpe.Presenters.SearchHistoryFavoritesPresenter;
import br.com.eadfiocruzpe.Views.ViewModels.CheckableLDBSearch;
import br.com.eadfiocruzpe.Views.Adapters.SearchVisualizerSelectorAdapter;
import br.com.eadfiocruzpe.Views.Adapters.HomePagerAdapter;
import br.com.eadfiocruzpe.Views.Adapters.SearchHistoryAdapter;
import br.com.eadfiocruzpe.Views.Dialogs.GeneralPurposesDialog;
import br.com.eadfiocruzpe.Views.Validators.BasicValidators;
import br.com.eadfiocruzpe.Views.Pages.Home.BaseFragment;
import br.com.eadfiocruzpe.Views.Pages.Home.HomeActivity;

public class SearchHistoryFavoritesFragment extends BaseFragment implements
        SearchHistoryFavoritesContract.View,
        SearchHistoryAdapterContract.Client,
        SearchVisualizerSelectorAdapterContract.Client {

    private final String TAG = "SearchHistFavFrag";

    @BindView(R.id.search_history_btn_see_favorite)
    TextView mBtnFavorite;
    @BindView(R.id.search_history_btn_see_history)
    TextView mBtnHistory;

    @BindView(R.id.search_history_rv)
    RecyclerView mRvSearchHistory;
    @BindView(R.id.search_favorites_rv)
    RecyclerView mRvFavoriteSearches;
    @BindView(R.id.component_msg_no_search_selected)
    FrameLayout mEmptyListContainer;

    @BindView(R.id.component_msg_no_search_selected_img)
    ImageView mEmptyMsgImg;
    @BindView(R.id.component_msg_no_search_selected_text)
    TextView mEmptyMsgText;
    @BindView(R.id.component_msg_no_search_selected_btn_search)
    Button mEmptyMsgBtn;

    private SearchHistoryFavoritesPresenter mSearchHistoryFavoritesPresenter;
    private SearchHistoryAdapter mRvAdapterSearchHistory;
    private SearchVisualizerSelectorAdapter mRvAdapterFavoriteSearches;
    private GeneralPurposesDialog mConfirmationDialog;
    private BasicValidators mValidationUtils = new BasicValidators();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_history, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    /**
     * Initialization
     */
    public void init() {
        initViews();
        initData();
    }

    private void initViews() {
        initMainContainer();
        initSearchHistoryList();
        initFavoriteSearchesList();
        initEmptyListMsg();
    }

    private void initMainContainer() {
        try {
            ((HomeActivity)getActivity()).updateBackgroundRootView(
                    HomePagerAdapter.IDX_SEARCH_HISTORY_OPTION,
                    false,
                    R.color.color_gray_1,
                    R.color.color_gray_1);

            selectHistoryList();

        } catch (Exception ignored) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to initMainContainer");
        }
    }

    private void initSearchHistoryList() {
        mRvSearchHistory.setVisibility(View.GONE);
        mRvSearchHistory.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));
        mRvAdapterSearchHistory = new SearchHistoryAdapter(
                this,
                getResources(),
                new ArrayList<LDBSearch>());
        mRvSearchHistory.setAdapter(mRvAdapterSearchHistory);
    }

    private void initEmptyListMsg() {
        mEmptyListContainer.setVisibility(View.GONE);

        new CountDownTimer(ConstantUtils.TIMEOUT_SHOW_EMPTY_LIST_MSG, 100) {
            public void onTick(long millisUntilFinished) {
                // ..
            }

            public void onFinish() {
                try {
                    mEmptyListContainer.setVisibility(mRvAdapterSearchHistory.getItemCount() > 0?
                            View.VISIBLE: View.GONE);
                } catch (Exception e) {
                    logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                            TAG + " Failed to initEmptyListMsg");
                }
            }
        }.start();
    }

    private void initFavoriteSearchesList() {
        mRvFavoriteSearches.setVisibility(View.GONE);
        mRvFavoriteSearches.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));
        mRvAdapterFavoriteSearches = new SearchVisualizerSelectorAdapter(
                this,
                getContext().getApplicationContext(),
                false,
                true,
                new ArrayList<LDBSearch>());
        mRvFavoriteSearches.setAdapter(mRvAdapterFavoriteSearches);
    }

    private void initData() {
        logUtils.logMessage(TypeUtils.LogMsgType.SUCCESS,
                TAG + "Initializing SearchHistoryFavoritesFragment ...");

        mSearchHistoryFavoritesPresenter = new SearchHistoryFavoritesPresenter(getContext().getApplicationContext());
        mSearchHistoryFavoritesPresenter.bindView(this);
        mSearchHistoryFavoritesPresenter.loadSearches(SearchHistoryFavoritesPresenter.IDX_LIST_SEARCH_HISTORY);
    }

    /**
     * Events
     */
    @Override
    public void onLoadSearchHistory(final ArrayList<LDBSearch> searchHistory) {
        try {
            mRvSearchHistory.post(new Runnable() {
                @Override
                public void run() {
                    updateSearchHistory(searchHistory);
                }
            });
        } catch (NullPointerException npe) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed onLoadSearchHistory " + npe.getMessage());
        }
    }

    @Override
    public void onLoadFavoriteSearches(final ArrayList<LDBSearch> favoriteSearches) {
        try {
            mRvFavoriteSearches.post(new Runnable() {
                @Override
                public void run() {
                    updateFavoriteSearches(favoriteSearches);
                }
            });
        } catch (NullPointerException npe) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed onLoadSearchHistory " + npe.getMessage());
        }
    }

    @Override
    public void onSearchHistoryItemDetails(LDBSearch search) {
        navigateDashboardPage(search);
    }

    @Override
    public void onSearchHistoryItemDeleted(final LDBSearch search) {
        showConfirmationDialogRemoveFromList(false, search);
    }

    public void onClickBtnToggleFavorite(LDBSearch search) {
        toggleSearchFavoriteStatus(search);
    }

    @Override
    public void onSearchItemDetails(LDBSearch search) {
        navigateDashboardPage(search);
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
        showConfirmationDialogRemoveFromList(true, search);
    }

    @OnClick({R.id.component_msg_no_search_selected_btn_search})
    public void search() {
        openSearchDialog();
    }

    @OnClick({R.id.search_history_btn_see_favorite})
    public void selectFavoriteList() {
        updateSelectedList(R.id.search_history_btn_see_favorite);
    }

    @OnClick({R.id.search_history_btn_see_history})
    public void selectHistoryList() {
        updateSelectedList(R.id.search_history_btn_see_history);
    }

    /**
     * Other methods
     */
    private void updateSelectedList(int selectedButtonId) {
        try {

            switch (selectedButtonId) {

                case R.id.search_history_btn_see_history:
                    mRvSearchHistory.setVisibility(View.VISIBLE);
                    mRvFavoriteSearches.setVisibility(View.GONE);
                    setTextSettings(true);
                    setMainContainerBackgroundColor(true);
                    mSearchHistoryFavoritesPresenter.loadSearches(SearchHistoryFavoritesPresenter.IDX_LIST_SEARCH_HISTORY);
                    setEmptyMessage(true);

                    break;

                case R.id.search_history_btn_see_favorite:
                    mRvFavoriteSearches.setVisibility(View.VISIBLE);
                    mRvSearchHistory.setVisibility(View.GONE);
                    setTextSettings(false);
                    setMainContainerBackgroundColor(false);
                    mSearchHistoryFavoritesPresenter.loadSearches(SearchHistoryFavoritesPresenter.IDX_LIST_FAVORITE);
                    setEmptyMessage(false);

                    break;
            }

        } catch (NullPointerException npe) {
            logUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to updateSelectedList");
        }
    }

    private void updateSearchHistory(ArrayList<LDBSearch> searchHistory) {
        try {

            if (mValidationUtils.isNotNull(searchHistory)) {
                mRvAdapterSearchHistory.updateList(searchHistory);

                // Clean the Dashboard page if the user has deleted all successful searches
                if (searchHistory.isEmpty()) {
                    ((HomeActivity) getActivity()).setSearch(null);
                }
            }

            verifyEmptyListMsgMustBeDisplayed();

        } catch (NullPointerException npe) {
            logUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed updateSearchHistory");
        }
    }

    private void updateFavoriteSearches(ArrayList<LDBSearch> favoriteSearches) {
        try {

            if (mValidationUtils.isNotNull(favoriteSearches)) {
                mRvAdapterFavoriteSearches.updateList(favoriteSearches);
            }

            verifyEmptyListMsgMustBeDisplayed();

        } catch (NullPointerException npe) {
            logUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed updateFavoriteSearches");
        }
    }

    private void verifyEmptyListMsgMustBeDisplayed() {
        try {

            if (mSearchHistoryFavoritesPresenter.getSelectedList() == SearchHistoryFavoritesPresenter.IDX_LIST_SEARCH_HISTORY) {
                mEmptyListContainer.setVisibility(mRvAdapterSearchHistory.getItemCount() == 0?
                        View.VISIBLE : View.GONE);
                mRvSearchHistory.setVisibility(mRvAdapterSearchHistory.getItemCount() == 0?
                        View.GONE : View.VISIBLE);

            } else if (mSearchHistoryFavoritesPresenter.getSelectedList() == SearchHistoryFavoritesPresenter.IDX_LIST_FAVORITE) {
                mEmptyListContainer.setVisibility(mRvAdapterFavoriteSearches.getItemCount() == 0?
                        View.VISIBLE : View.GONE);
                mRvFavoriteSearches.setVisibility(mRvAdapterFavoriteSearches.getItemCount() == 0?
                        View.GONE : View.VISIBLE);
            }

        } catch (NullPointerException npe) {
            logUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed verifyEmptyListMsgMustBeDisplayed");
        }
    }

    private void showConfirmationDialogRemoveFromList(final boolean isFavoritesList, final LDBSearch search) {
        String msg = isFavoritesList?
                getString(R.string.search_history_page_confirmation_dialog_remove_favorite_msg) :
                getString(R.string.search_history_page_confirmation_dialog_msg);

        mConfirmationDialog = new GeneralPurposesDialog(getContext(),
                new GeneralPurposesDialog.GeneralDialogCallback() {
                    @Override
                    public void onCloseDialog() {
                        destroyConfirmationDialog();
                    }

                    @Override
                    public void onInfoDialogActionConfirmed() {}

                    @Override
                    public void onConfirmationDialogActionConfirmed() {
                        confirmListRemoval(isFavoritesList, search);
                    }
                }, GeneralPurposesDialog.DIALOG_TYPE_CONFIRMATION,
                GeneralPurposesDialog.DIALOG_THEME_DANGER,
                getString(R.string.search_history_page_confirmation_dialog_title),
                R.drawable.ico_warning,
                msg,
                getString(R.string.search_history_page_confirmation_dialog_label_btn_confirm)
        );
    }

    private void confirmListRemoval(boolean isFavoritesList, LDBSearch search) {
        try {
            destroyConfirmationDialog();

            if (isFavoritesList) {
                unFavoriteSearch(search);
            } else {
                mSearchHistoryFavoritesPresenter.deleteSearch(search);
            }

            verifyEmptyListMsgMustBeDisplayed();

        } catch(Exception e) {
            logUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed confirmListRemoval");
        }
    }

    private void unFavoriteSearch(LDBSearch search) {

        if (search != null && mSearchHistoryFavoritesPresenter != null) {
            search.setFavorite(false);
            mSearchHistoryFavoritesPresenter.updateSearch(search);
        }
    }

    private void toggleSearchFavoriteStatus(LDBSearch search) {

        if (search != null && mSearchHistoryFavoritesPresenter != null) {
            search.setFavorite(!search.getFavorite());
            mSearchHistoryFavoritesPresenter.updateSearch(search);

            if (mSearchHistoryFavoritesPresenter.getSelectedList() == SearchHistoryFavoritesPresenter.IDX_LIST_SEARCH_HISTORY) {
                mRvAdapterSearchHistory.updateSearch(search);
            }
        }
    }

    private void destroyConfirmationDialog() {
        mConfirmationDialog.dismiss();
        mConfirmationDialog = null;
    }

    /**
     * Setters and Getters
     */
    private void setTextSettings(boolean history) {
        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mBtnHistory.setTextAppearance(history?
                        R.style.ListSelectorLgBlackTextStyle :
                        R.style.ListSelectorSmWhiteTextStyle);

                mBtnFavorite.setTextAppearance(history?
                        R.style.ListSelectorSmGrayTextStyle :
                        R.style.ListSelectorLgWhiteTextStyle);

            } else {
                mBtnFavorite.setTextSize(history?
                        ConstantUtils.LIST_SELECTOR_BTN_TEXT_SIZE :
                        ConstantUtils.LIST_SELECTOR_BTN_SELECTED_TEXT_SIZE);

                mBtnFavorite.setTextColor(history?
                        getResources().getColor(R.color.color_gray_9) :
                        getResources().getColor(R.color.color_white));

                mBtnHistory.setTextSize(history?
                        ConstantUtils.LIST_SELECTOR_BTN_SELECTED_TEXT_SIZE :
                        ConstantUtils.LIST_SELECTOR_BTN_TEXT_SIZE);

                mBtnHistory.setTextColor(history?
                        getResources().getColor(R.color.color_black_3) :
                        getResources().getColor(R.color.color_white));
            }

        } catch (Exception e) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to setTextSettings: " + e.getMessage());
        }
    }

    private void setMainContainerBackgroundColor(boolean history) {
        try {

            if (history) {

                // History
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ((HomeActivity) getActivity()).updateBackgroundRootView(
                            HomePagerAdapter.IDX_SEARCH_HISTORY_OPTION,
                            true,
                            R.drawable.background_white,
                            R.color.color_white);

                } else {
                    ((HomeActivity) getActivity()).updateBackgroundRootView(
                            HomePagerAdapter.IDX_SEARCH_HISTORY_OPTION,
                            false,
                            R.color.color_white,
                            R.color.color_white);
                }
            } else {

                // Favorites
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ((HomeActivity) getActivity()).updateBackgroundRootView(
                            HomePagerAdapter.IDX_SEARCH_HISTORY_OPTION,
                            true,
                            R.drawable.background_green_gradient,
                            R.color.color_green_blue_4);

                } else {
                    ((HomeActivity) getActivity()).updateBackgroundRootView(
                            HomePagerAdapter.IDX_SEARCH_HISTORY_OPTION,
                            false,
                            R.color.color_green_blue_4,
                            R.color.color_green_blue_4);
                }
            }

        } catch (Exception e) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to setMainContainerBackgroundColor: " + e.getMessage());
        }
    }

    private void setEmptyMessage(boolean history) {
        try {

            if (history) {
                ImageUtils.loadImage(
                        getContext().getApplicationContext(),
                        R.drawable.ico_logo_brazil_sus_search_gray,
                        mEmptyMsgImg);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mEmptyMsgImg.setBackground(null);
                }

                mEmptyMsgImg.setPadding(0, 0, 0, 0);

                mEmptyMsgText.setText(getString(R.string.sharing_page_msg_no_search_selected));
                mEmptyMsgBtn.setVisibility(View.VISIBLE);
            } else {
                ImageUtils.loadImage(
                        getContext().getApplicationContext(),
                        R.drawable.ico_tab_history_favorites_star_white,
                        mEmptyMsgImg);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mEmptyMsgImg.setBackground(
                            ContextCompat.getDrawable(getContext(),
                            R.drawable.background_circle_gray));

                    mEmptyMsgImg.setPadding(
                            UIUtils.dpToPx(getResources(), 15),
                            UIUtils.dpToPx(getResources(), 15),
                            UIUtils.dpToPx(getResources(), 15),
                            0);
                } else {
                    ImageUtils.loadImage(
                            getContext().getApplicationContext(),
                            R.drawable.ico_star_gray,
                            mEmptyMsgImg);

                    mEmptyMsgImg.setPadding(0, 0, 0, 0);
                }

                mEmptyMsgText.setText(getString(R.string.search_history_page_msg_need_favorite));
                mEmptyMsgBtn.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to setEmptyMessage: " + e.getMessage());
        }
    }

    /**
     * Navigation
     */
    private void openSearchDialog() {
        try {

            if (getActivity() != null) {
                ((HomeActivity) getActivity()).openSearchDialog("");
            }
        } catch(Exception e) {
            logUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to openSearchDialog.");
        }
    }

    private void navigateDashboardPage(LDBSearch search) {
        try {

            if (getActivity() != null) {
                ((HomeActivity) getActivity()).navigateToPage(HomePagerAdapter.IDX_DASHBOARD_OPTION, search);
            }

        } catch(NullPointerException npe) {
            logUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to navigateDashboardPage.");
        }
    }

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

            if (getActivity() != null) {
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

        if (mSearchHistoryFavoritesPresenter != null) {
            mSearchHistoryFavoritesPresenter.unbindView();
        }

        super.onStop();
    }

}