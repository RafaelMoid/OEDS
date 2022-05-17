package br.com.eadfiocruzpe.Views.Components;

import android.content.res.Resources;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Utils.LogUtils;
import br.com.eadfiocruzpe.Utils.StringUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;

public class ComponentFederalInvestmentsCityPH extends ComponentTextValueWithIcon {

    private final String TAG = "CFedInvestmentsCityPH";

    public LinearLayout rootLayout;
    private TextView mLblFederalInvestmentPublicHealthCity;

    private Resources mResources;
    private LogUtils mLogUtils;

    public ComponentFederalInvestmentsCityPH(LinearLayout rootLayout, Resources resources,
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

            mLblFederalInvestmentPublicHealthCity = this.rootLayout.findViewById(
                    R.id.component_federal_investment_public_health_city_header_msg);

            super.valueTextView = this.rootLayout.findViewById(
                    R.id.component_federal_investment_public_health_city_value);

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
                    mResources.getString(R.string.component_federal_investment_public_health_city_searching),
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

    public void loadInvestmentFederalGovCityPH(double investmentCityPH) {
        try {

            if (investmentCityPH > 0) {
                setLabelMsg(mResources.getString(R.string.component_federal_investment_public_health_city_main_lbl));
                setValueMsg(investmentCityPH);
            } else {
                setLabelMsg(mResources.getString(R.string.component_federal_investment_public_health_city_not_found));
                mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to loadValueInvestedFederalGovCity");
            }

        } catch (Exception e) {
            setLabelMsg(mResources.getString(R.string.component_federal_investment_public_health_city_not_found));
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to loadValueInvestedFederalGovCity");
        }
    }

    /**
     * Setters and Getters
     */
    private void setLabelMsg(String msg) {
        try {
            mLblFederalInvestmentPublicHealthCity.setText(msg);

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to setLabelMsg");
        }
    }

    private void setValueMsg(double investmentCityPH) {
        try {
            StringUtils strUtils = new StringUtils(mResources);

            super.valueTextView.setText(strUtils.getMoneyStringFromVal(investmentCityPH));

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to setValueMsg");
        }
    }

}