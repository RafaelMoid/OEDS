package br.com.eadfiocruzpe.Views.Components;

import android.content.res.Resources;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Utils.LogUtils;
import br.com.eadfiocruzpe.Utils.StringUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;

public class ComponentOtherSourcesInvestmentsCityPH extends ComponentTextValueWithIcon {

    private final String TAG = "COtherSourcesInvestCityPH";

    public LinearLayout rootLayout;
    private TextView mLblCityInvestmentPublicHealthCity;

    private Resources mResources;
    private LogUtils mLogUtils;

    public ComponentOtherSourcesInvestmentsCityPH(LinearLayout rootLayout, Resources resources,
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

            mLblCityInvestmentPublicHealthCity = this.rootLayout.findViewById(
                    R.id.component_city_investment_public_health_header_msg);

            super.valueTextView = this.rootLayout.findViewById(
                    R.id.component_city_investment_public_health_value);

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
                    mResources.getString(R.string.component_other_investment_public_health_searching),
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

    public void loadInvestmentsOtherSourcesCityPH(double cityInvestment) {
        try {

            if (cityInvestment > 0) {
                String msg = mResources.getString(R.string.component_other_investment_public_health_main_lbl);

                setLabelMsg(msg);
                setValueMsg(cityInvestment);
            } else {
                setLabelMsg(mResources.getString(R.string.component_other_investment_public_health_not_found));
                mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to loadValueInvestedOtherSourcesPH");
            }

        } catch (Exception e) {
            setLabelMsg(mResources.getString(R.string.component_other_investment_public_health_not_found));
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to loadValueInvestedOtherSourcesPH");
        }
    }

    /**
     * Setters and Getters
     */
    private void setLabelMsg(String msg) {
        try {
            mLblCityInvestmentPublicHealthCity.setText(msg);

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to setLabelMsg");
        }
    }

    private void setValueMsg(double cityInvestment) {
        try {
            StringUtils strUtils = new StringUtils(mResources);

            super.valueTextView.setText(strUtils.getMoneyStringFromVal(
                    cityInvestment));

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to setValueMsg");
        }
    }

}