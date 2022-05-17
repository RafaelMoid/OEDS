package br.com.eadfiocruzpe.Views.Components;

import android.content.res.Resources;
import android.os.Build;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Utils.LogUtils;
import br.com.eadfiocruzpe.Utils.StringUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;

public class ComponentCityInvestmentsPHCitizenDay {

    private final String TAG = "CCityInvestPHCitizenDay";

    public LinearLayout rootLayout;
    private LinearLayout mContainerLargeV;
    private TextView mLblPublicHealthInvestmentPerCitizenPerDayLargeV;
    private TextView mValuePublicHealthInvestmentPerCitizenPerDayLargeV;
    private LinearLayout mContainerSmallV;
    private TextView mLblPublicHealthInvestmentPerCitizenPerDaySmallV;
    private TextView mValuePublicHealthInvestmentPerCitizenPerDaySmallV;

    private Resources mResources;
    private LogUtils mLogUtils;

    public ComponentCityInvestmentsPHCitizenDay(LinearLayout rootLayout,
                                                Resources resources,
                                                String searchedCityName) {
        initUI(rootLayout);
        initTools(resources);
        initData(searchedCityName);
    }

    /**
     * Initialization
     */
    private void initUI(LinearLayout rootLayout) {
        try {
            this.rootLayout = rootLayout;

            // UI elements of the Large version
            mContainerLargeV = this.rootLayout.findViewById(
                    R.id.component_public_health_investment_per_citizen_per_day_large_v);

            mLblPublicHealthInvestmentPerCitizenPerDayLargeV = this.rootLayout.findViewById(
                    R.id.component_public_health_investment_per_citizen_per_day_header_msg_large_v);

            mValuePublicHealthInvestmentPerCitizenPerDayLargeV = this.rootLayout.findViewById(
                    R.id.component_public_health_investment_per_citizen_per_day_value_large_v);

            // UI elements of the small version
            mContainerSmallV = this.rootLayout.findViewById(
                    R.id.component_public_health_investment_per_citizen_per_day_small_v);

            mLblPublicHealthInvestmentPerCitizenPerDaySmallV = this.rootLayout.findViewById(
                    R.id.component_public_health_investment_per_citizen_per_day_header_msg_small_v);

            mValuePublicHealthInvestmentPerCitizenPerDaySmallV = this.rootLayout.findViewById(
                    R.id.component_public_health_investment_per_citizen_per_day_value_small_v);

            // Initialize the version that is going to appear when the component gets displayed
            show(false);
            showLargeVersion(true);

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to initUI");
        }
    }

    private void initTools(Resources resources) {
        mLogUtils = new LogUtils();
        mResources = resources;
    }

    private void initData(String searchedCityName) {
        try {
            String msg = String.format(
                    mResources.getString(R.string.component_investment_per_citizen_per_day_calculating),
                    searchedCityName);

            setLabelMsg("", "", msg);

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to initData");
        }
    }

    /**
     * Events
     */
    public void show(boolean showComponent) {

        if (rootLayout != null) {
            rootLayout.setVisibility(showComponent ? View.VISIBLE : View.GONE);
        }
    }

    public void showLargeVersion(boolean showLargeVersion) {

        if (rootLayout != null && mContainerLargeV != null && mContainerSmallV != null) {

            if (showLargeVersion && mContainerLargeV.getVisibility() != View.VISIBLE) {
                mContainerLargeV.setVisibility(View.VISIBLE);
                mContainerSmallV.setVisibility(View.GONE);

            } else if (!showLargeVersion && mContainerSmallV.getVisibility() != View.VISIBLE) {
                mContainerSmallV.setVisibility(View.VISIBLE);
                mContainerLargeV.setVisibility(View.GONE);
            }
        }
    }

    public void loadValuePHCitizenDay(double valueCitizenPerDay, String searchedCity, String year) {
        try {

            if (valueCitizenPerDay > 0) {
                show(true);

                setLabelMsg(searchedCity, year, "");
                setValueMsg(valueCitizenPerDay);
            } else {
                show(false);
                setLabelMsg("", "", "");
                mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                        TAG + "Failed to loadCityInvestmentsPHCitizenYear");
            }

        } catch (Exception e) {
            show(false);
            setLabelMsg("", "", "");
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to loadCityInvestmentsPHCitizenYear");
        }
    }

    /**
     * Setters and Getters
     */
    public void setLabelMsg(String searchedCity, String year, String otherMsg) {
        try {
            // Build the message
            String msgLargeV;
            String msgSmallV;

            if (!searchedCity.isEmpty() && !year.isEmpty()) {
                msgLargeV = String.format(
                        mResources.getString(R.string.component_investment_per_citizen_per_day_large_v),
                        searchedCity);

                msgSmallV = String.format(
                        mResources.getString(R.string.component_investment_per_citizen_per_day_small_v),
                        searchedCity);

            } else if (!otherMsg.isEmpty()) {
                msgLargeV = otherMsg;
                msgSmallV = otherMsg;

            } else {
                msgLargeV = mResources.getString((R.string.component_investment_per_citizen_per_day_public_health_not_found));
                msgSmallV = mResources.getString((R.string.component_investment_per_citizen_per_day_public_health_not_found));
            }

            // Load the message on the different versions of the component
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mLblPublicHealthInvestmentPerCitizenPerDayLargeV.setText(Html.fromHtml(msgLargeV, Html.FROM_HTML_MODE_COMPACT));
                mLblPublicHealthInvestmentPerCitizenPerDaySmallV.setText(Html.fromHtml(msgSmallV, Html.FROM_HTML_MODE_COMPACT));
            } else {
                mLblPublicHealthInvestmentPerCitizenPerDayLargeV.setText(Html.fromHtml(msgLargeV));
                mLblPublicHealthInvestmentPerCitizenPerDaySmallV.setText(Html.fromHtml(msgSmallV));
            }

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to setLabelMsg");
        }
    }

    private void setValueMsg(double valuePerCitizenPerDay) {
        try {
            StringUtils strUtils = new StringUtils(mResources);

            mValuePublicHealthInvestmentPerCitizenPerDayLargeV.setText(strUtils.getMoneyStringFromVal(
                    valuePerCitizenPerDay));
            mValuePublicHealthInvestmentPerCitizenPerDaySmallV.setText(strUtils.getMoneyStringFromVal(
                    valuePerCitizenPerDay));

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to setValueMsg");
        }
    }

}