package br.com.eadfiocruzpe.Views.Components;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBExpense;
import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Utils.LogUtils;
import br.com.eadfiocruzpe.Utils.StringUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;
import br.com.eadfiocruzpe.Views.Adapters.DeclaredCityExpensesAdapter;
import br.com.eadfiocruzpe.Views.Validators.BasicValidators;

public class ComponentDeclaredCityExpensesPH {

    private final String TAG = "CCityExpensesPH";

    public LinearLayout rootLayout;
    private TextView mLblPublicHealthExpensesDeclaredByCity;
    private TextView mValueTotalAmount;
    private RecyclerView mRvDeclaredCityExpenses;
    private DeclaredCityExpensesAdapter mRvAdapterDeclaredCityExpenses;

    private Resources mResources;
    private LogUtils mLogUtils;

    public ComponentDeclaredCityExpensesPH(final Context context,
                                           LinearLayout rootLayout,
                                           Resources resources,
                                           String searchedCityName,
                                           ArrayList<LDBExpense> declaredCityExpenses) {
        initUI(rootLayout);
        initTools(resources);
        initData(context, searchedCityName, declaredCityExpenses);
    }

    /**
     * Initialization
     */
    private void initUI(LinearLayout rootLayout) {
        try {
            this.rootLayout = rootLayout;

            mLblPublicHealthExpensesDeclaredByCity = this.rootLayout.findViewById(
                    R.id.component_declared_city_expenses_header_msg);

            mRvDeclaredCityExpenses = this.rootLayout.findViewById(
                    R.id.component_declared_city_expenses_value);

            mValueTotalAmount = this.rootLayout.findViewById(
                    R.id.component_declared_city_expenses_total_value);

            show(false);

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to initUI");
        }
    }

    private void initTools(Resources resources) {
        mLogUtils = new LogUtils();
        mResources = resources;
    }

    private void initData(final Context context, String searchedCityName,
                          ArrayList<LDBExpense> listExpensesDeclaredByCity) {
        try {
            show(false);

            String msg = String.format(
                    mResources.getString(R.string.component_declared_city_expenses_table_searching),
                    searchedCityName);

            setLabelMsg(msg);

            mRvDeclaredCityExpenses.setLayoutManager(new LinearLayoutManager(context));
            mRvAdapterDeclaredCityExpenses = new DeclaredCityExpensesAdapter(
                    listExpensesDeclaredByCity,
                    mResources);
            mRvDeclaredCityExpenses.setAdapter(mRvAdapterDeclaredCityExpenses);
            mRvDeclaredCityExpenses.setNestedScrollingEnabled(false);

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to initData");
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

    public void loadPHExpensesDeclaredCity(ArrayList<LDBExpense> declaredCityExpenses,
                                           String searchedCityName) {
        try {
            BasicValidators basicValidators = new BasicValidators();

            if (basicValidators.isValidList(declaredCityExpenses)) {
                show(true);

                String msg = String.format(
                        mResources.getString(R.string.component_declared_city_expenses_table),
                        searchedCityName);

                setLabelMsg(msg);

                setTotalAmount(declaredCityExpenses);

                mRvAdapterDeclaredCityExpenses.updateList(declaredCityExpenses);

            } else {
                show(false);
                setLabelMsg(mResources.getString(R.string.component_declared_city_expenses_table_not_found));
                mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                        TAG + "Failed to loadPHExpensesDeclaredCity");
            }

        } catch (Exception e) {
            show(false);
            setLabelMsg(mResources.getString(R.string.component_declared_city_expenses_table_not_found));
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to loadPHExpensesDeclaredCity");
        }
    }

    /**
     * Setters and Getters
     */
    private void setLabelMsg(String msg) {
        try {
            mLblPublicHealthExpensesDeclaredByCity.setText(msg);

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to setLabelMsg");
        }
    }

    private void setTotalAmount(ArrayList<LDBExpense> declaredCityExpenses) {
        try {
            BasicValidators validationUtils = new BasicValidators();

            if (validationUtils.isValidList(declaredCityExpenses)) {
                double totalAmount = 0;

                for (LDBExpense expense : declaredCityExpenses) {

                    if (expense.getValue() != -1) {
                        totalAmount += expense.getValue();
                    }
                }

                StringUtils stringUtils = new StringUtils(mResources);
                String totalValue = stringUtils.getMoneyStringFromVal(totalAmount);
                mValueTotalAmount.setText(totalValue);
            }

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to setTotalAmount");
        }
    }

}