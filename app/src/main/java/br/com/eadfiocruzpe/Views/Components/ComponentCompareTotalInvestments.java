package br.com.eadfiocruzpe.Views.Components;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Contracts.ComponentTotalInvestmentsPHContract;
import br.com.eadfiocruzpe.Utils.LogUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;
import br.com.eadfiocruzpe.Utils.UIUtils;
import br.com.eadfiocruzpe.Views.Validators.BasicValidators;

public class ComponentCompareTotalInvestments {

    private final String TAG = "CCompareTotalInvest";

    public FrameLayout rootLayout;

    private LinearLayout mMainContainer;
    private ComponentTotalInvestmentCityPH mCTotalInvestmentA;
    private ComponentTotalInvestmentCityPH mCTotalInvestmentB;
    private ComponentFederalInvestmentsCityPH mCFederalInvestmentA;
    private ComponentFederalInvestmentsCityPH mCFederalInvestmentB;
    private ComponentStateInvestmentsCityPH mCStateInvestmentA;
    private ComponentStateInvestmentsCityPH mCStateInvestmentB;
    private ComponentOtherSourcesInvestmentsCityPH mCOtherInvestmentA;
    private ComponentOtherSourcesInvestmentsCityPH mCOtherInvestmentB;

    private LogUtils mLogUtils;
    private Resources mResources;

    public ComponentCompareTotalInvestments(FrameLayout rootLayout, Resources resources) {
        initTools(resources);
        initUI(rootLayout);
    }

    /**
     * Initialization
     */
    private void initTools(Resources resources) {
        mLogUtils = new LogUtils();
        mResources = resources;
    }

    private void initUI(FrameLayout rootLayout) {
        try {
            this.rootLayout = rootLayout;

            mMainContainer = this.rootLayout.findViewById(R.id.component_compare_total_investments_container);

            LinearLayout mContainerA = this.rootLayout.findViewById(R.id.component_compare_total_investments_a)
                    .findViewById(R.id.component_total_public_health_investments_container);

            LinearLayout mContainerB = this.rootLayout.findViewById(R.id.component_compare_total_investments_b)
                    .findViewById(R.id.component_total_public_health_investments_container);

            LinearLayout mContainerFederalInvestmentA = this.rootLayout.findViewById(
                    R.id.component_compare_total_investments_legend_federal_gov_a)
                    .findViewById(R.id.component_federal_investment_public_health_city_container);

            LinearLayout mContainerFederalInvestmentB = this.rootLayout.findViewById(
                    R.id.component_compare_total_investments_legend_federal_gov_b)
                    .findViewById(R.id.component_federal_investment_public_health_city_container);

            LinearLayout mContainerStateInvestmentA = this.rootLayout.findViewById(
                    R.id.component_compare_total_investments_legend_state_gov_a)
                    .findViewById(R.id.component_state_investment_public_health_city_container);

            LinearLayout mContainerStateInvestmentB = this.rootLayout.findViewById(
                    R.id.component_compare_total_investments_legend_state_gov_b)
                    .findViewById(R.id.component_state_investment_public_health_city_container);

            LinearLayout mContainerOtherSourcesInvestmentA = this.rootLayout.findViewById(
                    R.id.component_compare_total_investments_legend_other_source_a)
                    .findViewById(R.id.component_other_sources_investment_public_health_container);

            LinearLayout mContainerOtherSourcesInvestmentB = this.rootLayout.findViewById(
                    R.id.component_compare_total_investments_legend_other_source_b)
                    .findViewById(R.id.component_other_sources_investment_public_health_container);

            mCTotalInvestmentA = new ComponentTotalInvestmentCityPH(
                    mContainerA,
                    mCallbackComponentTIA,
                    mResources);

            mCTotalInvestmentB = new ComponentTotalInvestmentCityPH(
                    mContainerB,
                    mCallbackComponentTIB,
                    mResources);

            mCFederalInvestmentA = new ComponentFederalInvestmentsCityPH(
                    mContainerFederalInvestmentA,
                    mResources,
                    "");

            mCFederalInvestmentB = new ComponentFederalInvestmentsCityPH(
                    mContainerFederalInvestmentB,
                    mResources,
                    "");

            mCStateInvestmentA = new ComponentStateInvestmentsCityPH(
                    mContainerStateInvestmentA,
                    mResources,
                    "");

            mCStateInvestmentB = new ComponentStateInvestmentsCityPH(
                    mContainerStateInvestmentB,
                    mResources,
                    "");

            mCOtherInvestmentA = new ComponentOtherSourcesInvestmentsCityPH(
                    mContainerOtherSourcesInvestmentA,
                    mResources,
                    "");

            mCOtherInvestmentB = new ComponentOtherSourcesInvestmentsCityPH(
                    mContainerOtherSourcesInvestmentB,
                    mResources,
                    "");

            mCTotalInvestmentA.showTitle(false);
            mCTotalInvestmentA.showMsgAbove(true);
            mCTotalInvestmentA.showFullWidthChart(true);
            mCTotalInvestmentA.setRootContainerPadding(
                    UIUtils.dpToPx(mResources, 10),
                    UIUtils.dpToPx(mResources, 0),
                    UIUtils.dpToPx(mResources, 10),
                    UIUtils.dpToPx(mResources, 0));

            mCTotalInvestmentB.showTitle(false);
            mCTotalInvestmentB.showMsgAbove(true);
            mCTotalInvestmentB.showFullWidthChart(true);
            mCTotalInvestmentB.setRootContainerPadding(
                    UIUtils.dpToPx(mResources, 10),
                    UIUtils.dpToPx(mResources, 0),
                    UIUtils.dpToPx(mResources, 10),
                    UIUtils.dpToPx(mResources, 0));

            show(false);

        } catch (Exception e) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to initUI");
        }
    }

    /**
     * Events
     */
    private final ComponentTotalInvestmentsPHContract mCallbackComponentTIA = new
        ComponentTotalInvestmentsPHContract() {

        @Override
        public void pieChartItemSelected(String selectedItem, float selectedValue) {
            try {

                if (selectedItem != null) {
                    hideAllDetailedLegends();
                    mCTotalInvestmentB.selectChartItem(selectedItem);

                    if (selectedItem.equals(mResources.getString(R.string.dashboard_page_lbl_city_government))) {
                        mCOtherInvestmentA.show(true);
                        mCOtherInvestmentB.show(true);
                        mCOtherInvestmentA.setPercentageOnMsg(selectedValue);

                    } else if (selectedItem.equals(mResources.getString(R.string.dashboard_page_lbl_state_government))) {
                        mCStateInvestmentA.show(true);
                        mCStateInvestmentB.show(true);
                        mCStateInvestmentA.setPercentageOnMsg(selectedValue);

                    } else if (selectedItem.equals(mResources.getString(R.string.dashboard_page_lbl_federal_government))) {
                        mCFederalInvestmentA.show(true);
                        mCFederalInvestmentB.show(true);
                        mCFederalInvestmentA.setPercentageOnMsg(selectedValue);
                    }
                }

            } catch (Exception e) {
                    mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                            TAG + "Failed on mCallbackComponentTIA");
                }
            }
    };

    private final ComponentTotalInvestmentsPHContract mCallbackComponentTIB = new
        ComponentTotalInvestmentsPHContract() {

        @Override
        public void pieChartItemSelected(String selectedItem, float selectedValue) {
            try {

                if (selectedItem != null) {
                    hideAllDetailedLegends();
                    mCTotalInvestmentA.selectChartItem(selectedItem);

                    if (selectedItem.equals(mResources.getString(R.string.dashboard_page_lbl_city_government))) {
                        mCOtherInvestmentB.show(true);
                        mCOtherInvestmentA.show(true);
                        mCOtherInvestmentB.setPercentageOnMsg(selectedValue);

                    } else if (selectedItem.equals(mResources.getString(R.string.dashboard_page_lbl_state_government))) {
                        mCStateInvestmentB.show(true);
                        mCStateInvestmentA.show(true);
                        mCStateInvestmentB.setPercentageOnMsg(selectedValue);

                    } else if (selectedItem.equals(mResources.getString(R.string.dashboard_page_lbl_federal_government))) {
                        mCFederalInvestmentB.show(true);
                        mCFederalInvestmentA.show(true);
                        mCFederalInvestmentB.setPercentageOnMsg(selectedValue);
                    }
                }

            } catch (Exception e) {
                mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                        TAG + "Failed on mCallbackComponentTIB");
            }
        }
    };

    private void hideAllDetailedLegends() {
        try {
            mCFederalInvestmentA.show(false);
            mCFederalInvestmentB.show(false);
            mCStateInvestmentA.show(false);
            mCStateInvestmentB.show(false);
            mCOtherInvestmentA.show(false);
            mCOtherInvestmentB.show(false);

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to hideAllDetailedLegends");
        }
    }

    /**
     * Other methods
     */
    public void show(boolean showComponent) {

        if (rootLayout != null) {
            rootLayout.setVisibility(showComponent ? View.VISIBLE : View.GONE);
            mMainContainer.setVisibility(showComponent ? View.VISIBLE : View.GONE);
        }
    }

    public void loadValuesForChart(final Context context, boolean loadChartA,
                                   ArrayList<Double> dataSet, int[] colors, ArrayList<String> labels,
                                   String description, String cityName, String stateName) {

        try {
            BasicValidators basicValidators = new BasicValidators();

            if (basicValidators.isValidList(dataSet) && basicValidators.isValidList(labels) &&
                    basicValidators.isValidIntArray(colors) && basicValidators.isNotNull(description)) {

                if (dataSet.size() == labels.size()) {

                    if (loadChartA) {
                        mCTotalInvestmentA.loadInvestmentsCityPH(
                                context,
                                dataSet,
                                colors,
                                labels,
                                description,
                                cityName);

                        mCTotalInvestmentA.selectAllValues();
                        setInvestmentsBySource(true, dataSet, labels, cityName, stateName);
                    } else {
                        mCTotalInvestmentB.loadInvestmentsCityPH(
                                context,
                                dataSet,
                                colors,
                                labels,
                                description,
                                cityName);

                        mCTotalInvestmentB.selectAllValues();
                        setInvestmentsBySource(false, dataSet, labels, cityName, stateName);
                    }

                    show(true);
                    mCTotalInvestmentA.show(true);
                    mCTotalInvestmentB.show(true);

                } else {
                    show(false);
                    mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                            TAG + "Failed to loadValuesForChart");
                }

            } else {
                show(false);
                mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                        TAG + "Failed to loadValuesForChart");
            }

        } catch (Exception e) {
            show(false);
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to loadValuesForChart");
        }
    }

    /**
     * This method assumes that the order in which the dataset is build is:
     *
     *   - Investments from other sources
     *   - Investments from state government
     *   - Investments from federal government
     *
     * For more information, take a look at SearchResultsPresenter -> getDatasetInvestmentsCityPH()
     */
    private void setInvestmentsBySource(boolean loadA,
                                        ArrayList<Double> values, ArrayList<String> labels,
                                        String city, String state) {
        try {
            double totalAmount = 0;

            if (loadA) {
                mCOtherInvestmentA.loadInvestmentsOtherSourcesCityPH(0);
                mCOtherInvestmentA.setPercentageOnMsg(0);
                mCStateInvestmentA.loadInvestmentStateGovCityPH(0, "");
                mCStateInvestmentA.setPercentageOnMsg(0);
                mCFederalInvestmentA.loadInvestmentFederalGovCityPH(0);
                mCFederalInvestmentA.setPercentageOnMsg(0);
            } else {
                mCOtherInvestmentB.loadInvestmentsOtherSourcesCityPH(0);
                mCOtherInvestmentB.setPercentageOnMsg(0);
                mCStateInvestmentB.loadInvestmentStateGovCityPH(0, "");
                mCStateInvestmentB.setPercentageOnMsg(0);
                mCFederalInvestmentB.loadInvestmentFederalGovCityPH(0);
                mCFederalInvestmentB.setPercentageOnMsg(0);
            }

            for (int i = 0; i < values.size(); i++) {
                totalAmount += values.get(i);
            }

            for (int i = 0; i < values.size(); i++) {

                if (loadA) {

                    if (values.size() > i && labels.get(i)
                            .equals(mResources.getString(R.string.dashboard_page_lbl_city_government))) {
                        mCOtherInvestmentA.loadInvestmentsOtherSourcesCityPH(values.get(i));
                        mCOtherInvestmentA.setPercentageOnMsg((float) (values.get(i) / totalAmount) * 100);
                    }

                    if (values.size() > i && labels.get(i)
                            .equals(mResources.getString(R.string.dashboard_page_lbl_state_government))) {
                        mCStateInvestmentA.loadInvestmentStateGovCityPH(values.get(i), state);
                        mCStateInvestmentA.setPercentageOnMsg((float) (values.get(i) / totalAmount) * 100);
                    }

                    if (values.size() > i && labels.get(i)
                            .equals(mResources.getString(R.string.dashboard_page_lbl_federal_government))) {
                        mCFederalInvestmentA.loadInvestmentFederalGovCityPH(values.get(i));
                        mCFederalInvestmentA.setPercentageOnMsg((float) (values.get(i) / totalAmount) * 100);
                    }
                } else {

                    if (values.size() > i && labels.get(i)
                            .equals(mResources.getString(R.string.dashboard_page_lbl_city_government))) {
                        mCOtherInvestmentB.loadInvestmentsOtherSourcesCityPH(values.get(i));
                        mCOtherInvestmentB.setPercentageOnMsg((float) (values.get(i) / totalAmount) * 100);
                    }

                    if (values.size() > i && labels.get(i)
                            .equals(mResources.getString(R.string.dashboard_page_lbl_state_government))) {
                        mCStateInvestmentB.loadInvestmentStateGovCityPH(values.get(i), state);
                        mCStateInvestmentB.setPercentageOnMsg((float) (values.get(i) / totalAmount) * 100);
                    }

                    if (values.size() > i && labels.get(i)
                            .equals(mResources.getString(R.string.dashboard_page_lbl_federal_government))) {
                        mCFederalInvestmentB.loadInvestmentFederalGovCityPH(values.get(i));
                        mCFederalInvestmentB.setPercentageOnMsg((float) (values.get(i) / totalAmount) * 100);
                    }
                }
            }

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to setInvestmentsBySource");

        } catch (IndexOutOfBoundsException iobe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to setInvestmentsBySource");
        }
    }

}