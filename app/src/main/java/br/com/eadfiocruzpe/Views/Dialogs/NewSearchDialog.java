package br.com.eadfiocruzpe.Views.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Contracts.ComponentAutoCompleteSearchContract;
import br.com.eadfiocruzpe.Contracts.NewSearchDialogCallbackContract;
import br.com.eadfiocruzpe.Contracts.SearchDialogContract;
import br.com.eadfiocruzpe.Utils.ConnectivityUtils;
import br.com.eadfiocruzpe.Utils.ConstantUtils;
import br.com.eadfiocruzpe.Utils.DateTimeUtils;
import br.com.eadfiocruzpe.Utils.LogUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;
import br.com.eadfiocruzpe.Persistence.PreferencesManager;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.GooglePlacesCity;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBSearch;
import br.com.eadfiocruzpe.Presenters.SearchDialogPresenter;
import br.com.eadfiocruzpe.Views.Components.ComponentAutoCompleteSearch;
import br.com.eadfiocruzpe.Views.Validators.BasicValidators;

public class NewSearchDialog extends Dialog implements
        SearchDialogContract.View {

    private final String TAG = "NewSearchDialog: ";

    private LinearLayout mAutoCompleteSearchContainer;
    private LinearLayout mAdvancedSearchContainer;
    private TextView mTitle;
    private Spinner mStateSpinner;
    private Spinner mCitySpinner;
    private Spinner mYearSpinner;
    private Spinner mTimeSpinner;
    private TextView mMsgView;
    private ProgressBar mProgressCircle;
    private TextView mBtnClose;
    private Button mBtnConfirm;

    private ComponentAutoCompleteSearch mComponentAutoCompleteSearch;
    private ArrayAdapter<String> mStateSpinnerAdapter;
    private ArrayAdapter<String> mCitySpinnerAdapter;
    private ArrayAdapter<String> mYearSpinnerAdapter;
    private ArrayAdapter<String> mTimeSpinnerAdapter;

    private SearchDialogPresenter mSearchDialogPresenter;
    private NewSearchDialogCallbackContract mCallback;
    private LogUtils mLogUtils = new LogUtils();

    public NewSearchDialog(@NonNull Context context, NewSearchDialogCallbackContract callback,
                           String searchTerms) {
        super(context, R.style.DialogThemeFull);
        setContentView(R.layout.dialog_search);

        if (ConnectivityUtils.isConnected(context.getApplicationContext())) {
            initData(callback);
            initViews(this);
            initEvents();
            loadData(searchTerms);
        } else {
            initDialogNoInternet(context.getApplicationContext(), callback);
        }
    }

    private void initDialogNoInternet(@NonNull Context context, NewSearchDialogCallbackContract callback) {
        mCallback = callback;
        bindViews(this);
        initEvents();
        initSearchContainers();
        lockUI(true);
        mComponentAutoCompleteSearch.lockUI(true, "...");
        showMessage(true, context.getString(R.string.msg_cannot_search_without_internet));
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mSearchDialogPresenter != null) {
            mSearchDialogPresenter.bindView(this);
        }
    }

    /**
     * Initialization
     */
    private void initData(NewSearchDialogCallbackContract callback) {
        mSearchDialogPresenter = new SearchDialogPresenter();
        mSearchDialogPresenter.bindView(this);
        mCallback = callback;
    }

    private void initViews(final Dialog dialog) {
        bindViews(dialog);
        initSearchContainers();
        initStateSpinner();
        initCitySpinner();
        initYearSpinner();
        initTimeSpinner();
    }

    private void bindViews(final Dialog dialog) {
        mAutoCompleteSearchContainer = dialog.findViewById(R.id.component_auto_complete_search);
        mAdvancedSearchContainer = dialog.findViewById(R.id.dialog_search_advanced_search_container);
        mTitle = dialog.findViewById(R.id.dialog_search_title);
        mStateSpinner = dialog.findViewById(R.id.dialog_search_spinner_state);
        mCitySpinner = dialog.findViewById(R.id.dialog_search_spinner_city);
        mYearSpinner = dialog.findViewById(R.id.dialog_search_spinner_year);
        mTimeSpinner = dialog.findViewById(R.id.dialog_search_spinner_time);
        mMsgView = dialog.findViewById(R.id.dialog_search_msg);
        mProgressCircle = dialog.findViewById(R.id.dialog_search_progress);
        mBtnClose = dialog.findViewById(R.id.dialog_search_btn_close);
        mBtnConfirm = dialog.findViewById(R.id.dialog_search_btn_ok);
    }

    private void initSearchContainers() {
        initAutoCompleteSearchContainer();

        if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getContext()) ==
                ConnectionResult.SUCCESS) {
            showAdvancedSearchContainer(false);
        } else {
            showAdvancedSearchContainer(true);
        }
    }

    private void initAutoCompleteSearchContainer() {
        mComponentAutoCompleteSearch = new ComponentAutoCompleteSearch(mAutoCompleteSearchContainer,
                mCallbackAutoCompleteSearchComponent,
                getContext().getApplicationContext());

        mComponentAutoCompleteSearch.setHintSearchView(
                getContext().getApplicationContext().getString(R.string.dialog_search_hint_input_auto_complete_search));

        mComponentAutoCompleteSearch.setMsgBtnAdvancedSearch(
                getContext().getApplicationContext().getString(R.string.dialog_search_lbl_advanced_search));
    }

    private void showAdvancedSearchContainer(boolean showAdvancedSearch) {
        mTitle.setText(showAdvancedSearch?
                getContext().getString(R.string.dialog_search_advanced_search_label) :
                getContext().getString(R.string.dialog_search_label_header));
        mAdvancedSearchContainer.setVisibility(showAdvancedSearch? View.VISIBLE: View.GONE);
        mComponentAutoCompleteSearch.show(!showAdvancedSearch);
        mComponentAutoCompleteSearch.lockUI(showAdvancedSearch, "");
        lockUI(false);
    }

    private void initStateSpinner() {
        mStateSpinnerAdapter = new ArrayAdapter<>(getContext().getApplicationContext(),
                R.layout.component_spinner_item,
                mSearchDialogPresenter.getListUfs());

        mStateSpinnerAdapter.setDropDownViewResource(R.layout.component_simple_spinner_drowpdown_item);
        mStateSpinner.setAdapter(mStateSpinnerAdapter);
        mStateSpinner.setSelection(0);
    }

    private void initCitySpinner() {
        mCitySpinnerAdapter = new ArrayAdapter<>(getContext().getApplicationContext(),
                R.layout.component_spinner_item,
                mSearchDialogPresenter.getListCities());

        mCitySpinnerAdapter.setDropDownViewResource(R.layout.component_simple_spinner_drowpdown_item);
        mCitySpinner.setAdapter(mCitySpinnerAdapter);
        mCitySpinner.setSelection(0);
    }

    private void initYearSpinner() {
        mYearSpinnerAdapter = new ArrayAdapter<>(getContext().getApplicationContext(),
                R.layout.component_spinner_item,
                mSearchDialogPresenter.getListYears());

        mYearSpinnerAdapter.setDropDownViewResource(R.layout.component_simple_spinner_drowpdown_item);
        mYearSpinner.setAdapter(mYearSpinnerAdapter);
        mYearSpinner.setSelection(0);
    }

    private void initTimeSpinner() {
        mTimeSpinnerAdapter = new ArrayAdapter<>(getContext().getApplicationContext(),
                R.layout.component_spinner_item,
                mSearchDialogPresenter.getListTimeRanges());

        mTimeSpinnerAdapter.setDropDownViewResource(R.layout.component_simple_spinner_drowpdown_item);
        mTimeSpinner.setAdapter(mTimeSpinnerAdapter);
        mTimeSpinner.setSelection(0);
    }

    /**
     * Events
     */
    private void initEvents() {
        mStateSpinner.setOnItemSelectedListener(mOnStateItemSelectedListener);
        mCitySpinner.setOnItemSelectedListener(mOnCityItemSelectedListener);
        mYearSpinner.setOnItemSelectedListener(mOnYearItemSelectedListener);
        mTimeSpinner.setOnItemSelectedListener(mOnTimeRangeItemSelectedListener);
        mBtnClose.setOnClickListener(mBtnCloseClickListener);
        mBtnConfirm.setOnClickListener(mBtnConfirmClickListener);
    }

    private final ComponentAutoCompleteSearchContract mCallbackAutoCompleteSearchComponent =
            new ComponentAutoCompleteSearchContract() {
        @Override
        public void onAdvancedSearchBtnClicked() {
            showAdvancedSearchContainer(true);
        }

        @Override
        public void lockUI() {
            mStateSpinner.setEnabled(false);
            mCitySpinner.setEnabled(false);
            mYearSpinner.setEnabled(false);
            mTimeSpinner.setEnabled(false);
        }

        @Override
        public void setSearchWithSuggestion(GooglePlacesCity suggestedCity) {
            mSearchDialogPresenter.setSuggestedCity(suggestedCity);
        }
    };

    private final AdapterView.OnItemSelectedListener mOnStateItemSelectedListener =
            new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            try {
                BasicValidators basicValidators = new BasicValidators();
                String selectedState = mStateSpinnerAdapter.getItem(i);

                if (basicValidators.isValidState(selectedState)) {
                    showMessage(false, "");
                    mSearchDialogPresenter.loadListUfCities(
                            mSearchDialogPresenter.getMapUfsCodes().get(selectedState));
                }
            } catch (NullPointerException npe) {
                mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                        TAG + "Failed to get the selected state from the adapter");

            } catch (ArrayIndexOutOfBoundsException aiobe) {
                mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                        TAG + "Failed to get the selected state from the adapter");
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.REGULAR, TAG + "User clicked on the state spinner but didn't select an option.");
        }
    };

    private final AdapterView.OnItemSelectedListener mOnCityItemSelectedListener =
            new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            try {
                BasicValidators basicValidators = new BasicValidators();
                String selectedCity = mCitySpinnerAdapter.getItem(i);

                if (basicValidators.isValidString(selectedCity)) {
                    assert selectedCity != null;

                    if (!selectedCity.equals(ConstantUtils.DEFAULT_VALUE_SEARCH_DIALOG_CITY)) {
                        showMessage(false, "");
                        mSearchDialogPresenter.loadListYears();
                    }
                }
            } catch (NullPointerException npe) {
                mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                        TAG + "Failed to get the selected city from the adapter");
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.REGULAR, TAG + "User clicked on the city spinner but didn't select an option.");
        }
    };

    private final AdapterView.OnItemSelectedListener mOnYearItemSelectedListener =
            new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            try {
                BasicValidators basicValidators = new BasicValidators();
                String selectedYear = mYearSpinnerAdapter.getItem(i);

                if (basicValidators.isValidYear(selectedYear)) {
                    showMessage(false, "");
                    mSearchDialogPresenter.loadTimeRangesForYear(selectedYear);
                }
            } catch (NullPointerException npe) {
                mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                        TAG + "Failed to get the selected year from the adapter");
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.REGULAR, TAG + "User clicked on the year spinner but didn't select an option.");
        }
    };

    private final AdapterView.OnItemSelectedListener mOnTimeRangeItemSelectedListener =
            new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            try {
                BasicValidators basicValidators = new BasicValidators();
                String selectedPeriodYear = mTimeSpinnerAdapter.getItem(i);

                if (basicValidators.isNotNull(selectedPeriodYear)) {
                    assert selectedPeriodYear != null;

                    if(!selectedPeriodYear.equals(ConstantUtils.DEFAULT_VALUE_SEARCH_DIALOG_TIME_RANGE)) {
                        showMessage(false, "");
                        mLogUtils.logMessage(TypeUtils.LogMsgType.SUCCESS,
                                TAG + "The user has picked a valid set of inputs");
                    }
                }
            } catch (NullPointerException npe) {
                mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                        TAG + "Failed to get the selected year from the adapter");
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.REGULAR, TAG + "User clicked on the time range spinner but didn't select an option.");
        }
    };

    private final View.OnClickListener mBtnCloseClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (mCallback != null) {
                closeDialog();
            }
        }
    };

    private final View.OnClickListener mBtnConfirmClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            try {

                if (mSearchDialogPresenter.getSuggestedCity() != null && mAdvancedSearchContainer.getVisibility() == View.GONE) {
                    mComponentAutoCompleteSearch.lockUI(true, getContext().getString(
                            R.string.component_auto_complete_search_suggestion_format,
                            mSearchDialogPresenter.getSuggestedCity().getCity(),
                            mSearchDialogPresenter.getSuggestedCity().getUf().toUpperCase()));

                    showMessage(true, getContext().getString(R.string.dialog_search_msg_searching));

                    lockUI(true);

                    mSearchDialogPresenter.startAutomaticSearch();

                } else {
                    createSearchFromSelectedParams();
                }
            } catch (NullPointerException ignored) {}
        }
    };

    /**
     * Data loading
     */
    private void loadData(String searchTerms) {
        setSearchTerms(searchTerms);
        mSearchDialogPresenter.loadListUfs();
    }

    @Override
    public void loadListUfs() {
        try {
            mStateSpinnerAdapter.clear();
            mStateSpinnerAdapter.addAll(mSearchDialogPresenter.getListUfs());
            mStateSpinnerAdapter.notifyDataSetChanged();

            mSearchDialogPresenter.verifyMaxNumStoredSearches(getContext().getString(R.string.dialog_search_msg_limit_stored_searches_exceeded));

            tryUpdateSelectedUf();

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to loadListUfs");
        }
    }

    private void tryUpdateSelectedUf() {
        try {

            if (mSearchDialogPresenter.getSelectedSearch() != null) {

                for (int i = 0; i < mSearchDialogPresenter.getListUfs().size(); i++) {
                    String uf = mSearchDialogPresenter.getListUfs().get(i);

                    if (uf.equals(mSearchDialogPresenter.getSelectedSearch().getState())) {
                        mStateSpinner.setSelection(i);
                        break;
                    }
                }
            }

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to tryUpdateSelectedUf");
        }
    }

    @Override
    public void loadListCitiesSelectedUf() {
        try {
            mCitySpinnerAdapter.clear();
            mCitySpinnerAdapter.addAll(mSearchDialogPresenter.getListCities());
            mCitySpinnerAdapter.notifyDataSetChanged();

            tryUpdateSelectedCity();

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to loadListCitiesSelectedUf");
        }
    }

    private void tryUpdateSelectedCity() {
        try {

            if (mSearchDialogPresenter.getSelectedSearch() != null) {

                for (int i = 0; i < mSearchDialogPresenter.getListCities().size(); i++) {
                    String city = mSearchDialogPresenter.getListCities().get(i);

                    if (city.equals(mSearchDialogPresenter.getSelectedSearch().getCity())) {
                        mCitySpinner.setSelection(i);
                        break;
                    }
                }
            }

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to tryUpdateSelectedCity");
        }
    }

    @Override
    public void loadListYears() {
        try {
            mYearSpinnerAdapter.clear();
            mYearSpinnerAdapter.addAll(mSearchDialogPresenter.getListYears());
            mYearSpinnerAdapter.notifyDataSetChanged();

            tryUpdateSelectedYear();

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to loadListYears");
        }
    }

    private void tryUpdateSelectedYear() {
        try {

            if (mSearchDialogPresenter.getSelectedSearch() != null) {

                for (int i = 0; i < mSearchDialogPresenter.getListYears().size(); i++) {
                    String year = mSearchDialogPresenter.getListYears().get(i);

                    if (year.equals(mSearchDialogPresenter.getSelectedSearch().getYear())) {
                        mYearSpinner.setSelection(i);
                        break;
                    }
                }
            }

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to tryUpdateSelectedYear");
        }
    }

    @Override
    public void loadListTimeRangesSelectedYear() {
        try {
            mTimeSpinnerAdapter.clear();
            mTimeSpinnerAdapter.addAll(mSearchDialogPresenter.getListTimeRanges());
            mTimeSpinnerAdapter.notifyDataSetChanged();

            tryUpdateSelectedPeriod();

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to loadListTimeRangesSelectedYear");
        }
    }

    private void tryUpdateSelectedPeriod() {
        try {

            if (mSearchDialogPresenter.getSelectedSearch() != null) {

                for (int i = 0; i < mSearchDialogPresenter.getListTimeRanges().size(); i++) {
                    String periodYear = mSearchDialogPresenter.getListTimeRanges().get(i);

                    if (periodYear.equals(mSearchDialogPresenter.getSelectedSearch().getPeriodYear())) {
                        mTimeSpinner.setSelection(i);
                        break;
                    }
                }
            }

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to tryUpdateSelectedPeriod");
        }
    }

    @Override
    public void loadCurrentSearch(LDBSearch currentSearch) {
        try {
            mSearchDialogPresenter.loadCurrentSearch(currentSearch);

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to loadCurrentSearch");
        }
    }

    @Override
    public void informAdvancedSearchFailed() {
        try {
            showAdvancedSearchContainer(false);
            showMessage(true, getContext().getString(R.string.dialog_search_msg_advanced_search_failed));

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to informAdvancedSearchFailed");
        }
    }

    /**
     * Events
     */
    private void createSearchFromSelectedParams() {
        try {
            BasicValidators basicValidators = new BasicValidators();

            if (basicValidators.isNotNull(mCallback)) {
                LDBSearch search = getSearchFromSelectedParams();

                if (basicValidators.isValidSearch(search)) {
                    finishSearchSuccessfully(search);
                } else {
                    showMessage(true,
                            getContext().getString(R.string.dialog_search_could_not_validate_search_params));
                }
            } else {
                mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                        TAG + "Failed to createSearchFromSelectedParams");
            }
        } catch (Exception e) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to createSearchFromSelectedParams: " + e.getMessage());
        }
    }

    private LDBSearch getSearchFromSelectedParams() {
        try {
            DateTimeUtils dtUtils = new DateTimeUtils();

            LDBSearch search = new LDBSearch();
            search.setUserId(PreferencesManager.getCurrentUser().getUserId());
            search.setEmail(PreferencesManager.getCurrentUser().getEmail());
            search.setLocation(PreferencesManager.getCurrentUser().getLocation());
            search.setCreatedAt(dtUtils.getNowTimeStamp());

            String selectedState = mStateSpinnerAdapter.getItem(mStateSpinner.getSelectedItemPosition());
            String selectedCity = mCitySpinnerAdapter.getItem(mCitySpinner.getSelectedItemPosition());
            String selectedYear = mYearSpinnerAdapter.getItem(mYearSpinner.getSelectedItemPosition());
            String selectedTimeRange = mTimeSpinnerAdapter.getItem(mTimeSpinner.getSelectedItemPosition());

            search.setState(selectedState);
            search.setStateCode(mSearchDialogPresenter.getMapUfsCodes().get(selectedState));
            search.setCity(selectedCity);
            search.setCityCode(mSearchDialogPresenter.getMapCitiesCodes().get(selectedCity));
            search.setYear(selectedYear);
            search.setPeriodYear(selectedTimeRange);
            search.setPeriodYearCode(mSearchDialogPresenter.getMapTimeRangesCodes().get(selectedTimeRange));

            return search;

        } catch (NumberFormatException e) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to create LDBSearch object from selected inputs.");

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to create LDBSearch object from selected inputs.");
        }

        return null;
    }

    @Override
    public void finishSearchSuccessfully(LDBSearch search) {
        try {
            mSearchDialogPresenter.saveValidSearch(search);
            mCallback.onActionConfirmed(search);
            closeDialog();

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to create LDBSearch object from selected inputs.");
        }
    }

    @Override
    public void showMessage(boolean show, String message) {
        mMsgView.setText(show? message : "");
        mMsgView.setVisibility(show? View.VISIBLE : View.GONE);
    }

    @Override
    public void onShowMessage(String message) {
        mCallback.onShowDialogMessage(message);
    }

    @Override
    public void onShowProgress(boolean show) {
        // ..
    }

    @Override
    public void showProgressOnDialog(boolean show) {
        showMessage(false, "");
        mProgressCircle.setVisibility(show? View.VISIBLE : View.GONE);
        mStateSpinner.setEnabled(!show);
        mCitySpinner.setEnabled(!show);
        mYearSpinner.setEnabled(!show);
        mTimeSpinner.setEnabled(!show);
    }

    private void closeDialog() {
        showProgressOnDialog(false);
        onShowProgress(false);
        lockUI(false);
        mComponentAutoCompleteSearch.lockUI(false, "");

        NewSearchDialog.this.hide();
    }

    private void lockUI(boolean lock) {
        try {
            mBtnConfirm.setAlpha(lock? ConstantUtils.ALPHA_DISABLED_BUTTON: 1f);
            mBtnConfirm.setEnabled(!lock);
            mStateSpinner.setEnabled(!lock);
            mCitySpinner.setEnabled(!lock);
            mYearSpinner.setEnabled(!lock);
            mTimeSpinner.setEnabled(!lock);

        } catch (NullPointerException ignored) {}
    }

    /**
     * Getters and Setters
     */
    public void setSearchTerms(String searchTerms) {

        if (mComponentAutoCompleteSearch != null) {
            mComponentAutoCompleteSearch.setSearchTerms(searchTerms);
            initSearchContainers();
        }
    }

}