package br.com.eadfiocruzpe.Views.Components;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBCityRankingObject;
import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Utils.ColorUtils;
import br.com.eadfiocruzpe.Utils.ConstantUtils;
import br.com.eadfiocruzpe.Utils.LogUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;
import br.com.eadfiocruzpe.Views.Formatters.EmptyValueFormatter;
import br.com.eadfiocruzpe.Views.Formatters.MoneyValueFormatter;
import br.com.eadfiocruzpe.Views.Validators.BasicValidators;

public class ComponentRankingCitiesByFinancialAutonomy {

    private final String TAG = "CRankCitiesFinAutonomy";

    public LinearLayout rootLayout;
    private LayoutInflater mLayoutInflater;
    private CombinedChart mRankingCitiesByFinancialAutonomy;
    private LinearLayout mCustomChartLegend;
    private TextView mLegendBestInCountry;
    private TextView mLegendWorstInCountry;
    private TextView mLegendBestInState;
    private TextView mLegendWorstInState;
    private TextView mLegendSearchedCity;
    private LogUtils mLogUtils;

    public ComponentRankingCitiesByFinancialAutonomy(LinearLayout rootLayout,
                                                     LayoutInflater layoutInflater) {
        mLayoutInflater = layoutInflater;
        initUI(rootLayout);
        initTools();
    }

    /**
     * Initialization
     */
    private void initUI(LinearLayout rootLayout) {
        try {
            this.rootLayout = rootLayout;

            mRankingCitiesByFinancialAutonomy = this.rootLayout.findViewById(
                    R.id.component_ranking_cities_by_financial_autonomy);

            mCustomChartLegend = this.rootLayout.findViewById(
                    R.id.component_ranking_cities_by_financial_autonomy_custom_chart_legend);

            mLegendSearchedCity = this.rootLayout.findViewById(
                    R.id.component_ranking_cities_by_financial_autonomy_legend_searched_city);
            mLegendBestInCountry = this.rootLayout.findViewById(
                    R.id.component_ranking_cities_by_financial_autonomy_legend_best_in_country);
            mLegendWorstInCountry = this.rootLayout.findViewById(
                    R.id.component_ranking_cities_by_financial_autonomy_legend_worst_in_country);
            mLegendBestInState = this.rootLayout.findViewById(
                    R.id.component_ranking_cities_by_financial_autonomy_legend_best_in_state);
            mLegendWorstInState = this.rootLayout.findViewById(
                    R.id.component_ranking_cities_by_financial_autonomy_legend_worst_in_state);

            show(false);

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to initUI");
        }
    }

    private void initTools() {
        mLogUtils = new LogUtils();
    }

    /**
     * Events
     */
    public void show(boolean showComponent) {

        if (rootLayout != null) {
            rootLayout.setVisibility(showComponent ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * Create a CombinedChart to display the ranking with the information that was gathered from
     * the API.
     */
    public void loadRankingCities(final float avgValuesFullRanking,
                                  final ArrayList<LDBCityRankingObject> rankingData) {
        try {

            BasicValidators validationUtils = new BasicValidators();

            if (validationUtils.isValidList(rankingData)) {
                show(true);

                try {
                    // Build the data of the LineChart that represents the national average of the cities'
                    // autonomy regarding the investments received for public health
                    LineData lineChartDataNationalAvg = getLineChartData(rankingData, avgValuesFullRanking);

                    // Build the data of the BarChart that represents the ranking of the national cities'
                    // autonomy regarding investments received for public health
                    BarData barChartDataCitiesAutonomy = getBarChartData(rankingData);

                    // Customize the appearance of the CombinedChart
                    customizeCombinedChartAppearance(rankingData);

                    // Merge the data of the LineChart and the BarChart
                    CombinedData data = new CombinedData();
                    data.setData(lineChartDataNationalAvg);
                    data.setData(barChartDataCitiesAutonomy);

                    // Build the custom legend for the chart
                    buildCustomChartLegend(rankingData);

                    // Update the data and display the chart
                    mRankingCitiesByFinancialAutonomy.setData(data);
                    mRankingCitiesByFinancialAutonomy.invalidate();

                } catch (Exception e) {
                    mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                            TAG + "Failed to loadRankingCities");
                }
            } else {
                show(false);
            }
        } catch (Exception e) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to loadRankingCities");
        }
    }

    /**
     * Build the data of the LineChart that represents the national average of the cities'
     * autonomy regarding the investments received for public health
     */
    private LineData getLineChartData(final ArrayList<LDBCityRankingObject> rankingData,
                                      float nationalAvgFinancialAutonomyHealthInvestments) {
        try {
            LineData lineData = new LineData();

            // Create a dataset with the value of the national average for all data points, a line.
            ArrayList<Entry> lineChartEntries = new ArrayList<>();

            for (int i = 1; i <= rankingData.size(); i++) {
                lineChartEntries.add(new Entry(i, nationalAvgFinancialAutonomyHealthInvestments));
            }

            // Customize the LineChart
            LineDataSet set = new LineDataSet(lineChartEntries, "");
            set.setLineWidth(ConstantUtils.CHART_LINE_CHART_WIDTH);
            set.setCircleRadius(ConstantUtils.CHART_LINE_CHART_POINT_CIRCLE_RADIUS);
            set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set.setDrawValues(true);
            set.setValueTextSize(ConstantUtils.CHART_DATASET_SM_TEXT_SIZE);
            set.setColor(ColorUtils.RGB_COLOR_YELLOW_2);
            set.setCircleColor(ColorUtils.RGB_COLOR_ORANGE);
            set.setFillColor(ColorUtils.RGB_COLOR_YELLOW);
            set.setValueTextColor(ColorUtils.RGB_COLOR_YELLOW_1);
            set.setAxisDependency(YAxis.AxisDependency.LEFT);
            set.setValueFormatter(new MoneyValueFormatter());
            lineData.addDataSet(set);

            return lineData;

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to getLineChartData");
        }

        return null;
    }

    /**
     * Build the data of the BarChart that represents the ranking of the national cities'
     * autonomy regarding investments received for public health
     */
    private BarData getBarChartData(final ArrayList<LDBCityRankingObject> rankingData) {
        try {
            BasicValidators validationUtils = new BasicValidators();

            // Create the data points
            ArrayList<BarEntry> barChartDataEntries = new ArrayList<>();

            for (int i = 1; i <= rankingData.size(); i++) {
                LDBCityRankingObject cityAutonomyLevel = rankingData.get(i - 1);

                if (validationUtils.isValidCityAutonomyLevel(cityAutonomyLevel)) {
                    barChartDataEntries.add(new BarEntry(i, cityAutonomyLevel.getRankingValue()));
                }
            }

            // Customize the BarChart
            BarDataSet set = new BarDataSet(barChartDataEntries, "");
            set.setValueTextColor(ColorUtils.RGB_COLOR_GREEN);
            set.setValueTextSize(ConstantUtils.CHART_DATASET_SM_TEXT_SIZE);
            set.setAxisDependency(YAxis.AxisDependency.LEFT);
            set.setValueFormatter(new MoneyValueFormatter());

            ArrayList<Integer> barChartColors = new ArrayList<>();

            for (int i = 0; i < rankingData.size(); i++) {
                LDBCityRankingObject cityAutonomyLevel = rankingData.get(i);

                if (validationUtils.isValidCityAutonomyLevel(cityAutonomyLevel)) {
                    barChartColors.add(cityAutonomyLevel.getRankingColor());
                }
            }

            set.setColors(barChartColors);

            // Create the BarChart data model and return it
            BarData barData = new BarData(set);
            barData.setBarWidth(ConstantUtils.CHART_BAR_CHART_WIDTH);

            return barData;

        } catch(NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to getBarChartData");
        }

        return null;
    }

    /**
     * Customize the appearance of the CombinedChart (LineChart and BarChart).
     */
    private void customizeCombinedChartAppearance(final ArrayList<LDBCityRankingObject> rankingData) {
        try {
            // Overall adjustments
            mRankingCitiesByFinancialAutonomy.getDescription().setEnabled(false);
            mRankingCitiesByFinancialAutonomy.getAxisRight().setEnabled(false);
            mRankingCitiesByFinancialAutonomy.setDrawGridBackground(false);
            mRankingCitiesByFinancialAutonomy.setDrawBarShadow(false);
            mRankingCitiesByFinancialAutonomy.setHighlightFullBarEnabled(false);
            mRankingCitiesByFinancialAutonomy.setBackgroundColor(Color.WHITE);
            mRankingCitiesByFinancialAutonomy.setDrawOrder(new CombinedChart.DrawOrder[]{
                    CombinedChart.DrawOrder.BAR,
                    CombinedChart.DrawOrder.LINE
            });
            mRankingCitiesByFinancialAutonomy.animateY(
                    ConstantUtils.TIMEOUT_ANIMATIONS_DASHBOARD,
                    Easing.EasingOption.EaseOutBack);
            mRankingCitiesByFinancialAutonomy.getLegend().setEnabled(false);

            // Improvements on the x and y axis
            YAxis leftAxis = mRankingCitiesByFinancialAutonomy.getAxisLeft();
            leftAxis.setDrawGridLines(false);
            leftAxis.setAxisMinimum(0f);
            leftAxis.setDrawTopYLabelEntry(true);
            leftAxis.setTextColor(ColorUtils.RGB_COLOR_GRAY);

            XAxis xAxis = mRankingCitiesByFinancialAutonomy.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(ConstantUtils.CHART_GRANULARITY_X_AXIS);
            xAxis.setLabelRotationAngle(ConstantUtils.CHART_LABEL_ROTATION_ANGLE);
            xAxis.setAxisMinimum(0f);
            xAxis.setAxisMaximum(rankingData.size() + 1f);
            xAxis.setValueFormatter(new EmptyValueFormatter());

            // Decide which legends are going to show up
            mLegendBestInCountry.setVisibility(View.GONE);
            mLegendWorstInCountry.setVisibility(View.GONE);
            mLegendBestInState.setVisibility(View.GONE);
            mLegendWorstInState.setVisibility(View.GONE);
            mLegendSearchedCity.setVisibility(View.GONE);

            for (LDBCityRankingObject rankingItem : rankingData) {

                if (rankingItem != null) {

                    if (rankingItem.getChartFlag() != null) {

                        switch (rankingItem.getChartFlag()) {

                            case ConstantUtils.CHART_RANKING_FLAG_BEST_IN_COUNTRY:
                                mLegendBestInCountry.setVisibility(View.VISIBLE);
                                break;

                            case ConstantUtils.CHART_RANKING_FLAG_WORST_IN_COUNTRY:
                                mLegendWorstInCountry.setVisibility(View.VISIBLE);
                                break;

                            case ConstantUtils.CHART_RANKING_FLAG_BEST_IN_STATE:
                                mLegendBestInState.setVisibility(View.VISIBLE);
                                break;

                            case ConstantUtils.CHART_RANKING_FLAG_WORST_IN_STATE:
                                mLegendWorstInState.setVisibility(View.VISIBLE);
                                break;

                            case ConstantUtils.CHART_RANKING_FLAG_SEARCHED_IN_MIDDLE:
                                mLegendSearchedCity.setVisibility(View.VISIBLE);
                                break;
                        }
                    }
                }
            }

        } catch(NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to customizeCombinedChartAppearance");
        }
    }

    /**
     * Build the legend of the X axis, which represent the position of each item in the ranking.
     */
    private void buildCustomChartLegend(ArrayList<LDBCityRankingObject> rankingData) {
        try {
            mCustomChartLegend.setWeightSum(rankingData.size());
            mCustomChartLegend.removeAllViews();

            for (LDBCityRankingObject cityAutonomyLevel: rankingData) {
                @SuppressLint("InflateParams")
                LinearLayout rankingLegendItem = (LinearLayout) mLayoutInflater.inflate(
                        R.layout.component_ranking_city_financial_autonomy_on_ph_legend_item, null);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1);
                layoutParams.setMargins(3,0,3,0);
                rankingLegendItem.setLayoutParams(layoutParams);

                TextView rankingNumberView = rankingLegendItem.findViewById(
                        R.id.component_chart_legend_ranking_number);
                TextView rankingNumberCityName = rankingLegendItem.findViewById(
                        R.id.component_chart_legend_ranking_name);
                TextView rankingNumberCityStart = rankingLegendItem.findViewById
                        (R.id.component_chart_legend_ranking_name_extra);

                rankingNumberView.setText(String.format("%1$sº", cityAutonomyLevel.getRankingPosition()));
                rankingNumberCityName.setText(cityAutonomyLevel.getCityName());
                rankingNumberCityStart.setText(cityAutonomyLevel.getCityState());

                mCustomChartLegend.addView(rankingLegendItem);
            }

            // noinspection SynchronizeOnNonFinalField
            synchronized(mCustomChartLegend) {
                mCustomChartLegend.notify();
            }

        } catch (Exception e) {
            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to buildCustomChartLegend");
        }
    }

}