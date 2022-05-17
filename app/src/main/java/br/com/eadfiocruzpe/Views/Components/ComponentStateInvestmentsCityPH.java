package br.com.eadfiocruzpe.Views.Components;

import android.content.res.Resources;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Utils.LogUtils;
import br.com.eadfiocruzpe.Utils.StringUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;

public class ComponentStateInvestmentsCityPH extends ComponentTextValueWithIcon {

    private final String TAG = "CStateInvestmentsCityPH";

    public LinearLayout rootLayout;
    private TextView mLblStateInvestmentPublicHealthCity;

    private Resources mResources;
    private LogUtils mLogUtils;

    public ComponentStateInvestmentsCityPH(LinearLayout rootLayout, Resources resources,
                                           String searchedCityName) {
        super(resources);

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

            mLblStateInvestmentPublicHealthCity = this.rootLayout.findViewById(
                    R.id.component_state_investment_public_health_city_header_msg);

            super.valueTextView = this.rootLayout.findViewById(
                    R.id.component_state_investment_public_health_city_value);

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
                    mResources.getString(R.string.component_state_investment_public_health_city_searching),
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

    public void loadInvestmentStateGovCityPH(double stateInvestment, String stateName) {
        try {

            if (stateInvestment > 0) {
                StringUtils strUtils = new StringUtils();
                String msg = String.format(
                        mResources.getString(R.string.component_state_investment_public_health_city),
                        strUtils.getArticleForStateName(stateName, false));

                setLabelMsg(msg);
                setValueMsg(stateInvestment);
            } else {
                setLabelMsg(mResources.getString(R.string.component_state_investment_public_health_city_not_found));
                mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to loadValueInvestedStateGovCity");
            }

        } catch (Exception e) {
            setLabelMsg(mResources.getString(R.string.component_state_investment_public_health_city_not_found));
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to loadValueInvestedStateGovCity");
        }
    }

    /**
     * Setters and Getters
     */
    private void setLabelMsg(String msg) {
        try {
            mLblStateInvestmentPublicHealthCity.setText(msg);

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to setLabelMsg");
        }
    }

    private void setValueMsg(double stateInvestment) {
        try {
            StringUtils strUtils = new StringUtils(mResources);
            String strVal = strUtils.getMoneyStringFromVal(stateInvestment);

            super.valueTextView.setText(strVal);

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to setValueMsg");
        }
    }

}