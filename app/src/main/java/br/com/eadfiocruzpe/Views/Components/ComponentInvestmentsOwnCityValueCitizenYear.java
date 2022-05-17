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

public class ComponentInvestmentsOwnCityValueCitizenYear {

    private final String TAG = "CInvestOwnCityValCitYear";

    public LinearLayout rootLayout;
    private TextView mLblStateInvestmentOnPHPerCitizenPerYear;
    private TextView mValueStateInvestmentsOnPHPerCitizenPerYear;

    private Resources mResources;
    private LogUtils mLogUtils;

    public ComponentInvestmentsOwnCityValueCitizenYear(LinearLayout rootLayout, Resources resources,
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

            mLblStateInvestmentOnPHPerCitizenPerYear = this.rootLayout.findViewById(
                    R.id.component_public_health_state_investment_per_citizen_per_year_header_msg);

            mValueStateInvestmentsOnPHPerCitizenPerYear = this.rootLayout.findViewById(
                    R.id.component_public_health_state_investment_per_citizen_per_year_value);

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
                    mResources.getString(R.string.component_state_investment_per_citizen_per_year_calculating),
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

    public void loadStateInvestmentsOnPHPerCitizenPerDay(double valueCitizenPerYear) {
        try {

            if (valueCitizenPerYear > 0) {
                show(true);

                setLabelMsg(mResources.getString(R.string.component_investment_own_city_value_per_citizen_per_year));
                setValueMsg(valueCitizenPerYear);
            } else {
                show(false);
                setLabelMsg(mResources.getString((R.string.component_state_investment_per_citizen_per_year_not_found)));
                mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to loadStateInvestmentsOnPHPerCitizenPerDay");
            }

        } catch (Exception e) {
            show(false);
            setLabelMsg(mResources.getString((R.string.component_state_investment_per_citizen_per_year_not_found)));
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to loadStateInvestmentsOnPHPerCitizenPerDay");
        }
    }

    /**
     * Setters and Getters
     */
    public void setLabelMsg(String msg) {
        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mLblStateInvestmentOnPHPerCitizenPerYear.setText(Html.fromHtml(msg, Html.FROM_HTML_MODE_COMPACT));
            } else {
                mLblStateInvestmentOnPHPerCitizenPerYear.setText(Html.fromHtml(msg));
            }

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to setLabelMsg");
        }
    }

    private void setValueMsg(double valuePerCitizenPerDay) {
        try {
            StringUtils strUtils = new StringUtils(mResources);

            mValueStateInvestmentsOnPHPerCitizenPerYear.setText(strUtils.getMoneyStringFromVal(
                    valuePerCitizenPerDay));

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to setValueMsg");
        }
    }

}