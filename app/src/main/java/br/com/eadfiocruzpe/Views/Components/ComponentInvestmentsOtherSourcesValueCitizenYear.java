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

public class ComponentInvestmentsOtherSourcesValueCitizenYear {

    private final String TAG = "CInvestOtherSrcsValCitYear";

    public LinearLayout rootLayout;
    private TextView mLblFederalInvestmentsPHPerCitizenPerYear;
    private TextView mValueFederalInvestmentPHPerCitizenPerYear;

    private Resources mResources;
    private LogUtils mLogUtils;

    public ComponentInvestmentsOtherSourcesValueCitizenYear(LinearLayout rootLayout, Resources resources) {
        initUI(rootLayout);
        initTools(resources);
    }

    /**
     * Initialization
     */
    private void initUI(LinearLayout rootLayout) {
        try {
            this.rootLayout = rootLayout;

            mLblFederalInvestmentsPHPerCitizenPerYear = this.rootLayout.findViewById(
                    R.id.component_public_health_federal_investment_per_citizen_per_year_header_msg);

            mValueFederalInvestmentPHPerCitizenPerYear = this.rootLayout.findViewById(
                    R.id.component_public_health_federal_investment_per_citizen_per_year_value);

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
    public void show(boolean showComponent) {

        if (rootLayout != null) {
            rootLayout.setVisibility(showComponent ? View.VISIBLE : View.GONE);
        }
    }

    public void loadFederalInvestmentsPHCitizenYear(double valueCitizenPerYear) {
        try {

            if (valueCitizenPerYear > 0) {
                show(true);

                setLabelMsg(mResources.getString(R.string.component_investment_other_sources_value_per_citizen_per_year));
                setValueMsg(valueCitizenPerYear);
            } else {
                show(false);
                setLabelMsg(mResources.getString((R.string.component_federal_investment_per_citizen_per_year_not_found)));
                mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to loadFederalInvestmentsPHCitizenYear");
            }

        } catch (Exception e) {
            show(false);
            setLabelMsg(mResources.getString((R.string.component_federal_investment_per_citizen_per_year_not_found)));
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to loadFederalInvestmentsPHCitizenYear");
        }
    }

    /**
     * Setters and Getters
     */
    public void setLabelMsg(String msg) {
        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mLblFederalInvestmentsPHPerCitizenPerYear.setText(Html.fromHtml(msg, Html.FROM_HTML_MODE_COMPACT));
            } else {
                mLblFederalInvestmentsPHPerCitizenPerYear.setText(Html.fromHtml(msg));
            }

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to setLabelMsg");
        }
    }

    private void setValueMsg(double valuePerCitizenPerDay) {
        try {
            StringUtils strUtils = new StringUtils(mResources);

            mValueFederalInvestmentPHPerCitizenPerYear.setText(strUtils.getMoneyStringFromVal(
                    valuePerCitizenPerDay));

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to setValueMsg");
        }
    }

}