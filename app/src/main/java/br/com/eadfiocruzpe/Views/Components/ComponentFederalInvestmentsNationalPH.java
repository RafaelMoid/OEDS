package br.com.eadfiocruzpe.Views.Components;

import android.content.res.Resources;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Utils.LogUtils;
import br.com.eadfiocruzpe.Utils.StringUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;

public class ComponentFederalInvestmentsNationalPH {

    private final String TAG = "CFedInvestmentsNationalPH";

    public LinearLayout rootLayout;
    private TextView mLblFederalInvestmentPublicHealth;
    private TextView mValueFederalInvestmentPublicHealth;

    private Resources mResources;
    private LogUtils mLogUtils;

    public ComponentFederalInvestmentsNationalPH(LinearLayout rootLayout, Resources resources) {
        initUI(rootLayout);
        initTools(resources);
        initData();
    }

    /**
     * Initialization
     */
    private void initUI(LinearLayout rootLayout) {
        try {
            this.rootLayout = rootLayout;

            mLblFederalInvestmentPublicHealth = this.rootLayout.findViewById(
                    R.id.component_federal_investment_public_health_header_msg);

            mValueFederalInvestmentPublicHealth = this.rootLayout.findViewById(
                    R.id.component_federal_investment_public_health_value);

            show(false);

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to initUI");
        }
    }

    private void initTools(Resources resources) {
        mLogUtils = new LogUtils();
        mResources = resources;
    }

    private void initData() {
        try {
            setLabelMsg(mResources.getString(R.string.component_federal_investment_public_health_searching));

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

    public void loadValueInvestedByFederalGovernmentOnPublicHealth(double federalInvestmentOnBrazil) {
        try {

            if (federalInvestmentOnBrazil > 0) {
                show(true);
                setLabelMsg(mResources.getString(R.string.component_federal_investment_public_health_main_lbl));
                setValueMsg(federalInvestmentOnBrazil);
            } else {
                show(false);
                setLabelMsg(mResources.getString(R.string.component_federal_investment_public_health_not_found));
                mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to loadCityInvestmentsPHCitizenYear");
            }

        } catch (Exception e) {
            show(false);
            setLabelMsg(mResources.getString(R.string.component_federal_investment_public_health_not_found));
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to loadCityInvestmentsPHCitizenYear");
        }
    }

    /**
     * Setters and Getters
     */
    private void setLabelMsg(String msg) {
        try {
            mLblFederalInvestmentPublicHealth.setText(msg);

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to setLabelMsg");
        }
    }

    private void setValueMsg(double federalInvestmentOnBrazil) {
        try {
            StringUtils strUtils = new StringUtils(mResources);

            mValueFederalInvestmentPublicHealth.setText(strUtils.getMoneyStringFromVal(
                    federalInvestmentOnBrazil));

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to setValueMsg");
        }
    }

}