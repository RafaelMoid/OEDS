package br.com.eadfiocruzpe.Views.Components;

import android.content.res.Resources;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Utils.ConstantUtils;
import br.com.eadfiocruzpe.Utils.LogUtils;
import br.com.eadfiocruzpe.Utils.StringUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;

public class ComponentCityFinancialAutonomyPHInvestments {

    private final String TAG = "CCityFinAutPHInvest";

    public LinearLayout rootLayout;
    private TextView mLblCityFinancialAutonomy;
    private TextView mValueCityFinancialAutonomy;

    private Resources mResources;
    private LogUtils mLogUtils;

    public ComponentCityFinancialAutonomyPHInvestments(LinearLayout rootLayout, Resources resources) {
        initUI(rootLayout);
        initTools(resources);
    }

    /**
     * Initialization
     */
    private void initUI(LinearLayout rootLayout) {
        try {
            this.rootLayout = rootLayout;

            mLblCityFinancialAutonomy = this.rootLayout.findViewById(
                    R.id.component_city_financial_autonomy_label);

            mValueCityFinancialAutonomy = this.rootLayout.findViewById(
                    R.id.component_city_financial_autonomy);

            show(false);

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,TAG + "Failed to initUI");
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

    public void loadCityFinancialAutonomy(String searchedCityName, double cityFinancialAutonomy) {
        try {

            if (cityFinancialAutonomy > 0) {
                show(true);

                String msg = String.format(
                        mResources.getString(R.string.component_autonomy_index_aux_info_msg),
                        searchedCityName);

                setLabelMsg(msg);
                setValueMsg(cityFinancialAutonomy);
            } else {
                show(false);
                mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                        TAG + "Failed to loadCityFinancialAutonomy");
            }

        } catch (NullPointerException e) {
            show(false);
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to loadCityFinancialAutonomy");
        }
    }

    /**
     * Setters and Getters
     */
    private void setLabelMsg(String msg) {
        try {
            mLblCityFinancialAutonomy.setText(msg);

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to setLabelMsg");
        }
    }

    private void setValueMsg(double valuePerCitizenPerDay) {
        try {
            StringUtils strUtils = new StringUtils(mResources);

            mValueCityFinancialAutonomy.setText(strUtils.getPercentageStringFromVal(
                    (float) valuePerCitizenPerDay));

            if (valuePerCitizenPerDay > ConstantUtils.LAW_THRESHOLD_MINIMUM_CITY_INVESTMENT_PH) {
                mValueCityFinancialAutonomy.setTextColor(mResources.getColor(R.color.color_green_blue_3));
            } else {
                mValueCityFinancialAutonomy.setTextColor(mResources.getColor(R.color.color_red));
            }

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,TAG + "Failed to setValueMsg");
        }
    }

}