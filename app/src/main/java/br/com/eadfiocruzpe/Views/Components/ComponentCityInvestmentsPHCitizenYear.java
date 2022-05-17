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

public class ComponentCityInvestmentsPHCitizenYear {

    private final String TAG = "CCityInvestPHCitizenYear";

    public LinearLayout rootLayout;
    private TextView mLblCityInvestmentPerCitizenPerYear;
    private TextView mValueCityInvestmentPerCitizenPerYear;

    private Resources mResources;
    private LogUtils mLogUtils;

    public ComponentCityInvestmentsPHCitizenYear(LinearLayout rootLayout, Resources resources,
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

            mLblCityInvestmentPerCitizenPerYear = this.rootLayout.findViewById(
                    R.id.component_public_health_city_investment_per_citizen_per_year_header_msg);

            mValueCityInvestmentPerCitizenPerYear = this.rootLayout.findViewById(
                    R.id.component_public_health_city_investment_per_citizen_per_year_value);

            show(false);
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
                    mResources.getString(R.string.component_city_investment_per_citizen_per_year_calculating),
                    searchedCityName);

            setLabelMsg(msg);

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

    public void loadCityInvestmentsPHCitizenYear(double valueCitizenPerYear, String searchedCity) {
        try {

            if (valueCitizenPerYear > 0) {
                show(true);

                String msg = String.format(
                        mResources.getString(R.string.component_city_investment_per_citizen_per_year),
                        searchedCity);

                setLabelMsg(msg);
                setValueMsg(valueCitizenPerYear);
            } else {
                show(false);
                setLabelMsg(mResources.getString((R.string.component_city_investment_per_citizen_per_year)));
                mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to loadCityInvestmentsPHCitizenYear");
            }

        } catch (Exception e) {
            show(false);
            setLabelMsg(mResources.getString((R.string.component_city_investment_per_citizen_per_year)));
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to loadCityInvestmentsPHCitizenYear");
        }
    }

    /**
     * Setters and Getters
     */
    public void setLabelMsg(String msg) {
        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mLblCityInvestmentPerCitizenPerYear.setText(Html.fromHtml(msg, Html.FROM_HTML_MODE_COMPACT));
            } else {
                mLblCityInvestmentPerCitizenPerYear.setText(Html.fromHtml(msg));
            }

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to setLabelMsg");
        }
    }

    private void setValueMsg(double valuePerCitizenPerDay) {
        try {
            StringUtils strUtils = new StringUtils(mResources);

            mValueCityInvestmentPerCitizenPerYear.setText(strUtils.getMoneyStringFromVal(
                    valuePerCitizenPerDay));

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to setValueMsg");
        }
    }

}