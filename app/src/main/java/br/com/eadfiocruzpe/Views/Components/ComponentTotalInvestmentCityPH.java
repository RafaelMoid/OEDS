package br.com.eadfiocruzpe.Views.Components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Contracts.ComponentTotalInvestmentsPHContract;
import br.com.eadfiocruzpe.Utils.ConstantUtils;
import br.com.eadfiocruzpe.Utils.LogUtils;
import br.com.eadfiocruzpe.Utils.StringUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;
import br.com.eadfiocruzpe.Utils.UIUtils;
import br.com.eadfiocruzpe.Views.Validators.BasicValidators;

public class ComponentTotalInvestmentCityPH {

    private final String TAG = "CTotalInvestCityPH";

    public LinearLayout rootLayout;
    private View mMsgAbove;
    private View mMsgAside;

    private TextView lblTitle;
    private TextView lblPHInvestments;
    private TextView lblPHInvestmentsP2;
    private TextView lblPHInvestmentsValue;
    private TextView lblPHInvestmentsAbove;
    private TextView lblPHInvestmentsP2Above;
    private TextView lblPHInvestmentsValueAbove;
    private PieChart pieChartPHInvestments;

    private LogUtils mLogUtils;
    private ComponentTotalInvestmentsPHContract mCallback;
    private Resources mResources;

    private HashMap<Float, String> mMapValuesInvestor = new HashMap<>();

    public ComponentTotalInvestmentCityPH(LinearLayout rootLayout,
                                          ComponentTotalInvestmentsPHContract callback,
                                          Resources resources) {
        mCallback = callback;
        initTools(resources);
        initUI(rootLayout);
        initEvents();
    }

    /**
     * Initialization
     */
    private void initUI(LinearLayout rootLayout) {
        try {
            this.rootLayout = rootLayout;

            lblTitle = this.rootLayout.findViewById(
                    R.id.component_total_public_health_investments_title);

            mMsgAbove = this.rootLayout.findViewById(
                    R.id.component_total_public_health_investments_msg_above);
            mMsgAside = this.rootLayout.findViewById(
                    R.id.component_total_public_health_investments_msg_aside);

            lblPHInvestments = this.rootLayout.findViewById(
                    R.id.component_total_public_health_investments_header_msg);
            lblPHInvestmentsAbove = this.rootLayout.findViewById(
                    R.id.component_total_public_health_investments_header_msg_above);

            lblPHInvestmentsP2 = this.rootLayout.findViewById(
                    R.id.component_total_public_health_investments_header_msg_p2);
            lblPHInvestmentsP2Above = this.rootLayout.findViewById(
                    R.id.component_total_public_health_investments_header_msg_p2_above);

            lblPHInvestmentsValue = this.rootLayout.findViewById(
                    R.id.component_total_public_health_investments_value);
            lblPHInvestmentsValueAbove = this.rootLayout.findViewById(
                    R.id.component_total_public_health_investments_value_above);

            pieChartPHInvestments = this.rootLayout.findViewById(
                    R.id.component_chart_total_public_health_investments);

            showTitle(true);
            showMsgAbove(false);
            showFullWidthChart(false);
            setRootContainerPadding(
                    UIUtils.dpToPx(mResources, 20),
                    UIUtils.dpToPx(mResources, 30),
                    UIUtils.dpToPx(mResources, 20),
                    UIUtils.dpToPx(mResources, 15));
            show(false);

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to initUI");
        }
    }

    private void initTools(Resources resources) {
        mLogUtils = new LogUtils();
        mResources = resources;
    }

    /**
     * Events
     */
    private void initEvents() {
        pieChartPHInvestments.setOnChartValueSelectedListener(mOnValueSelectedListener);
    }

    private final OnChartValueSelectedListener mOnValueSelectedListener = new OnChartValueSelectedListener() {
        @Override
        public void onValueSelected(Entry e, Highlight h) {

            if (mCallback != null) {
                mCallback.pieChartItemSelected(getInvestorNameByValue(e.getY()), e.getY());
            }
        }

        @Override
        public void onNothingSelected() {
            Log.d(TAG, "Nothing selected ...");
        }
    };

    /**
     * Setters
     */
    public void show(boolean showComponent) {

        if (rootLayout != null) {
            rootLayout.setVisibility(showComponent ? View.VISIBLE : View.GONE);
        }
    }

    void showTitle(boolean showTitle) {

        if (rootLayout != null && lblTitle != null) {
            lblTitle.setVisibility(showTitle? View.VISIBLE : View.GONE);
        }
    }

    void showMsgAbove(boolean showAbove) {

        if (rootLayout != null && lblTitle != null) {
            mMsgAbove.setVisibility(showAbove? View.VISIBLE : View.GONE);
            mMsgAside.setVisibility(showAbove? View.GONE : View.VISIBLE);
        }
    }

    void showFullWidthChart(boolean showFullWidth) {
        try {

            if (rootLayout != null && pieChartPHInvestments != null) {

                if (showFullWidth) {
                    ((LinearLayout.LayoutParams) pieChartPHInvestments.getLayoutParams()).weight = 8;

                    pieChartPHInvestments.getLegend().setOrientation(
                            Legend.LegendOrientation.VERTICAL);
                    pieChartPHInvestments.getLegend().setHorizontalAlignment(
                            Legend.LegendHorizontalAlignment.CENTER);
                }

            }
        } catch (Exception e) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to showFullWidthChart");
        }
    }

    void setRootContainerPadding(int left, int top, int right, int bottom) {

        if (rootLayout != null) {
            rootLayout.setPadding(left, top, right, bottom);
        }
    }

    void selectAllValues() {

        if (rootLayout != null && pieChartPHInvestments != null) {

            for (int i = 0; i < mMapValuesInvestor.values().size(); i++) {
                pieChartPHInvestments.highlightValue(i, 0, true);
            }
        }
    }

    void selectChartItem(String pieChartSectionName) {
        try {

            if (rootLayout != null && pieChartPHInvestments != null) {
                float sectionValue = -1;
                PieData chartData = pieChartPHInvestments.getData();
                IPieDataSet dataset = chartData.getDataSet();

                // Find value of the section with the given string
                for (float key : mMapValuesInvestor.keySet()) {
                    String pieChartSection = mMapValuesInvestor.get(key);

                    if (pieChartSection.equals(pieChartSectionName)) {
                        sectionValue = key;
                        break;
                    }
                }

                // Find the section with the value that was encountered and highlight it
                for (int i = 0; i < dataset.getEntryCount(); i++) {
                    PieEntry pieEntry = dataset.getEntryForIndex(i);

                    if (pieEntry.getValue()== sectionValue) {
                        pieChartPHInvestments.highlightValue(i, 0, false);
                        break;
                    }
                }
            }

        } catch (Exception e) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to selectChartItem");
        }
    }

    /**
     * Other methods
     */
    public void loadInvestmentsCityPH(final Context context, ArrayList<Double> dataSet, int[] colors,
                                      ArrayList<String> labels, String description, String cityName) {

        try {
            BasicValidators basicValidators = new BasicValidators();

            if (basicValidators.isValidList(dataSet) && basicValidators.isValidList(labels) &&
                    basicValidators.isValidIntArray(colors) && basicValidators.isNotNull(description)) {

                // Adjust the dataset, labels and colors if there is a dataPoint with 0;
                ArrayList<Integer> removedDataPoints = getRemovedDataPoints(dataSet);

                for (int i = 0; i < removedDataPoints.size(); i++) {
                    int idxItemRemove = removedDataPoints.get(i);
                    dataSet.remove(idxItemRemove);
                    labels.remove(idxItemRemove);
                }

                colors = getAdjustedColors(removedDataPoints, colors);

                // Calculate the value of the total investment on public health
                double totalValue = getTotalInvestmentOnPH(dataSet);

                if (totalValue > 0) {
                    // Get the data of the PieChart tha represents the amount of value invested by each
                    // part of the government
                    PieData data = getPieChartData(context, totalValue, dataSet, labels, colors, description);

                    // Build the chart legend
                    customizeChartLegend(context, colors, labels);

                    // Update the main message of the component
                    updateMainMessage(cityName, totalValue);

                    // Customize the appearance of the chart
                    pieChartPHInvestments.setPadding(10,40,10,0);
                    pieChartPHInvestments.setDrawHoleEnabled(false);
                    pieChartPHInvestments.getDescription().setEnabled(false);
                    pieChartPHInvestments.setRotationEnabled(true);
                    pieChartPHInvestments.animateY(
                            ConstantUtils.TIMEOUT_ANIMATIONS_DASHBOARD,
                            Easing.EasingOption.EaseOutBack);

                    // Load the data into the chart and update its UI
                    pieChartPHInvestments.setData(data);
                    pieChartPHInvestments.invalidate();

                    show(true);

                } else {
                    show(false);
                    mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                            TAG + "Failed to loadChartTotalInvestmentsCityPH");
                }

            } else {
                show(false);
                mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                        TAG + "Failed to loadChartTotalInvestmentsCityPH");
            }

        } catch (Exception e) {
            show(false);
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to loadChartTotalInvestmentsCityPH");
        }
    }

    private ArrayList<Integer> getRemovedDataPoints(ArrayList<Double> dataSet) {
        ArrayList<Integer> dataPointsRemove = new ArrayList<>();

        try {

            for (int i = dataSet.size() - 1; i >= 0; i--) {
                double value = dataSet.get(i);

                if (value <= 0) {
                    dataPointsRemove.add(i);
                }
            }

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to getListRemovedDataPoints");
        }

        return dataPointsRemove;
    }

    private int[] getAdjustedColors(ArrayList<Integer> idxRemovedDps, int[] colors) {
        try {
            int[] adjustedColors = new int[colors.length - idxRemovedDps.size()];
            int idxLastAdjustedColor = -1;

            for (int i = 0; i < colors.length; i++) {

                if (!idxRemovedDps.contains(i)) {
                    idxLastAdjustedColor++;
                    adjustedColors[idxLastAdjustedColor] = colors[i];
                }
            }

            return adjustedColors;

        } catch (IndexOutOfBoundsException iobe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to getAdjustedColors");

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to getAdjustedColors");
        }

        return null;
    }

    private void initMapValuesInvestors(ArrayList<Float> dataSet, ArrayList<String> investorNames) {
        try {

            if (dataSet.size() == investorNames.size()) {

                for (int i = 0; i < dataSet.size(); i++) {
                    mMapValuesInvestor.put(dataSet.get(i), investorNames.get(i));
                }
            }
        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to initMapValuesInvestors");
        }
    }

    private String getInvestorNameByValue(float value) {
        try {
            return mMapValuesInvestor.get(value);

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to getInvestorNameByValue");
        }

        return "";
    }

    private double getTotalInvestmentOnPH(ArrayList<Double> dataSet) {
        try {
            double totalInvestmentsPH = 0;

            for (int i = 0; i < dataSet.size(); i++) {
                totalInvestmentsPH += dataSet.get(i);
            }

            return totalInvestmentsPH;

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to getTotalInvestmentOnPH");
        }

        return 0f;
    }

    /**
     * Return the data of the PieChart tha represents the amount of value invested by each
     * jurisdiction of the government
     */
    private PieData getPieChartData(final Context context, double totalInvestmentsPH,
                                    ArrayList<Double> dataSet, ArrayList<String> labels, int[] colors,
                                    String chartDescription) {
        try {
            // Build the DataSet
            StringUtils stringUtils = new StringUtils(mResources);
            List<PieEntry> entries = new ArrayList<>();
            ArrayList<Float> percentageDataset = new ArrayList<>();

            for (int i = 0; i < dataSet.size(); i++) {
                double value = (dataSet.get(i) * 100) / totalInvestmentsPH;
                percentageDataset.add((float) value);

                if (value > 0) {
                    PieEntry pieEntry = new PieEntry((float) value);
                    pieEntry.setLabel(stringUtils.getPercentageStringFromVal((float) value));
                    entries.add(pieEntry);
                }
            }

            // Create a map to identify an investor by the invested value
            initMapValuesInvestors(percentageDataset, labels);

            // Set the colors
            int[] chartColors = new int[entries.size()];
            System.arraycopy(colors, 0, chartColors, 0, chartColors.length);

            // Build the DataSet object
            PieDataSet set = new PieDataSet(entries, chartDescription);
            set.setColors(chartColors, context);
            set.setDrawValues(false);
            set.setValueTextSize(ConstantUtils.CHART_VALUE_SM_TEXT_SIZE);
            set.setValueTextColor(mResources.getColor(R.color.color_white));

            return new PieData(set);

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to getPieChartData");

        } catch (IndexOutOfBoundsException iob) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to getPieChartData");
        }

        return null;
    }

    private void customizeChartLegend(final Context context, int[] colors, ArrayList<String> labels) {
        try {
            Legend legend = pieChartPHInvestments.getLegend();
            legend.setFormSize(ConstantUtils.CHART_LEGEND_FORM_SIZE);
            legend.setForm(Legend.LegendForm.CIRCLE);
            legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
            legend.setOrientation(Legend.LegendOrientation.VERTICAL);
            legend.setDrawInside(false);
            legend.setWordWrapEnabled(true);
            legend.setTextSize(ConstantUtils.CHART_LEGEND_TEXT_SIZE);
            legend.setTextColor(Color.BLACK);

            ArrayList<LegendEntry> legends = new ArrayList<>();

            for (int i = colors.length - 1; i >= 0; i--) {
                LegendEntry le = new LegendEntry();
                le.formColor = ContextCompat.getColor(context, colors[i]);
                le.label = labels.get(i);

                legends.add(le);
            }

            legend.setCustom(legends);

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to customizeChartLegend");
        }
    }

    private void updateMainMessage(String searchedCityName, double totalInvestmentsPH) {
        try {
            StringUtils strUtils = new StringUtils(mResources);

            String msg = String.format(
                    mResources.getString(R.string.component_total_investment_public_health_for_city_prefix),
                    searchedCityName);

            lblPHInvestments.setText(msg);
            lblPHInvestmentsP2.setText(Html.fromHtml(mResources.getString(R.string.component_total_investment_public_health_main_msg_p2)));
            lblPHInvestmentsValue.setText(strUtils.getMoneyStringFromVal(totalInvestmentsPH));

            lblPHInvestmentsAbove.setText(msg);
            lblPHInvestmentsP2Above.setText(Html.fromHtml(mResources.getString(R.string.component_total_investment_public_health_main_msg_p2)));
            lblPHInvestmentsValueAbove.setText(strUtils.getMoneyStringFromVal(totalInvestmentsPH));

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to updateMainMessage");
        }
    }

}