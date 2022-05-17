package br.com.eadfiocruzpe.Views.Components;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBExpense;
import br.com.eadfiocruzpe.Utils.ConstantUtils;
import br.com.eadfiocruzpe.Utils.LogUtils;
import br.com.eadfiocruzpe.Utils.StringUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;
import br.com.eadfiocruzpe.Views.Adapters.CompareDeclaredCityExpensesAdapter;
import br.com.eadfiocruzpe.Views.Validators.BasicValidators;
import br.com.eadfiocruzpe.Views.ViewModels.ExpensesComparison;

public class ComponentCompareExpenses {

    private final String TAG = "ComponentCompareExpenses";

    public FrameLayout rootLayout;
    private LinearLayout mMainContainer;
    private TextView mCityA;
    private TextView mCityB;
    private TextView mTotalAmountA;
    private TextView mTotalAmountB;
    private RecyclerView mRvListExpenses;

    private CompareDeclaredCityExpensesAdapter mRvAdapterExpenses;
    private Resources mResources;
    private LogUtils mLogUtils;

    public ComponentCompareExpenses(Context context, FrameLayout rootLayout, Resources resources,
                                    String cityA, String cityB) {
        initTools(resources);
        initUI(context, rootLayout);
        initData(cityA, cityB);
    }

    /**
     * Initialization
     */
    private void initTools(Resources resources) {
        mLogUtils = new LogUtils();
        mResources = resources;
    }

    private void initUI(Context context, FrameLayout rootLayout) {
        bindViews(rootLayout);
        initListExpenses(context);
    }

    private void bindViews(FrameLayout rootLayout) {
        try {
            this.rootLayout = rootLayout;

            mMainContainer = this.rootLayout.findViewById(R.id.component_compare_list_expenses);
            mRvListExpenses = this.rootLayout.findViewById(R.id.component_compare_list_expenses_rv);
            mCityA = this.rootLayout.findViewById(R.id.component_compare_list_expenses_city_a);
            mCityB = this.rootLayout.findViewById(R.id.component_compare_list_expenses_city_b);
            mTotalAmountA = this.rootLayout.findViewById(R.id.component_compare_list_expenses_total_value_a);
            mTotalAmountB = this.rootLayout.findViewById(R.id.component_compare_list_expenses_total_value_b);

            show(false);

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to bindViews");
        }
    }

    private void initListExpenses(Context context) {
        try {
            mRvListExpenses.setLayoutManager(new LinearLayoutManager(context));
            mRvAdapterExpenses = new CompareDeclaredCityExpensesAdapter(
                    new ArrayList<ExpensesComparison>(),
                    mResources);
            mRvListExpenses.setAdapter(mRvAdapterExpenses);
            mRvListExpenses.setNestedScrollingEnabled(false);

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to initListExpenses");
        }
    }

    private void initData(String cityA, String cityB) {
        try {
            setHeader(cityA, cityB);

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to initData");
        }
    }

    /**
     * Data loading
     */
    public void loadDeclaredExpenses(ArrayList<LDBExpense> declaredExpensesA, String cityA,
                                     ArrayList<LDBExpense> declaredExpensesB, String cityB) {
        try {
            BasicValidators valUtils = new BasicValidators();
            show(false);

            if (valUtils.isValidString(cityA) && valUtils.isValidString(cityB) &&
                (valUtils.isValidList(declaredExpensesA) || valUtils.isValidList(declaredExpensesB))) {

                if (!valUtils.isValidList(declaredExpensesA)) {
                    declaredExpensesA = zeroList(declaredExpensesB);
                }

                if (!valUtils.isValidList(declaredExpensesB)) {
                    declaredExpensesB = zeroList(declaredExpensesA);
                }

                if (valUtils.isValidList(declaredExpensesA) && valUtils.isValidList(declaredExpensesB)) {
                    loadListExpenses(declaredExpensesA, declaredExpensesB);
                    setTotalValue(true, getTotalValue(declaredExpensesA));
                    setTotalValue(false, getTotalValue(declaredExpensesB));

                    show(true);
                } else {
                    mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                            TAG + "Failed to loadDeclaredExpenses");
                }

            } else {
                mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                        TAG + "Failed to loadDeclaredExpenses");
            }

        } catch (Exception e) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to loadDeclaredExpenses");
        }
    }

    private ArrayList<LDBExpense> zeroList(ArrayList<LDBExpense> expenses) {
        try {
            ArrayList<LDBExpense> zeroList = new ArrayList<>();

            if (expenses != null) {

                for (int i = 0; i < expenses.size(); i++) {
                    LDBExpense listExpense = expenses.get(i);
                    LDBExpense newExpense = new LDBExpense();

                    newExpense.setLDBSearchId("");
                    newExpense.setUpdatedAt("");
                    newExpense.setCreatedAt("");
                    newExpense.setCategory(ConstantUtils.DEFAULT_VALUE_ZERO_LIST);
                    newExpense.setExpenseId(listExpense.getExpenseId());
                    newExpense.setTitle(listExpense.getTitle());
                    newExpense.setValue(0);
                    newExpense.setPercentageValue(0);

                    zeroList.add(newExpense);
                }
            }

            return zeroList;

        } catch (Exception e) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to zeroList");
        }

        return null;
    }

    private void loadListExpenses(ArrayList<LDBExpense> expensesA, ArrayList<LDBExpense> expensesB) {
        try {
            ArrayList<ExpensesComparison> listExpenses = new ArrayList<>();

            if (expensesA.size() == expensesB.size()) {

                for (int i = 0; i < expensesA.size(); i++) {
                    ExpensesComparison expensesComparison = new ExpensesComparison();
                    expensesComparison.setExpenseA(expensesA.get(i));
                    expensesComparison.setExpenseB(expensesB.get(i));
                    listExpenses.add(expensesComparison);
                }
            }

            mRvAdapterExpenses.updateList(listExpenses);

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to loadListExpenses");
        }
    }

    private double getTotalValue(ArrayList<LDBExpense> listExpenses) {
        double totalValue = 0;

        try {

            for (LDBExpense expense : listExpenses) {

                if (expense != null) {
                    totalValue += expense.getValue();
                }
            }

        } catch (Exception e) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to getTotalValue");
        }

        return totalValue;
    }

    /**
     * Setters and Getters
     */
    private void setHeader(String cityA, String cityB) {
        try {
            BasicValidators valUtils = new BasicValidators();

            if (valUtils.isValidString(cityA) && valUtils.isValidString(cityB)) {
                mCityA.setText(cityA);
                mCityB.setText(cityB);
            }

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to setHeader");
        }
    }

    private void setTotalValue(boolean isSettingA, double totalValue) {
        try {
            StringUtils strUtils = new StringUtils(mResources);
            String strVal = strUtils.getShortMoneyStringFromVal(totalValue);

            if (isSettingA) {
                mTotalAmountA.setText(strVal);
            } else {
                mTotalAmountB.setText(strVal);
            }

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to setTotalValue");
        }
    }

    public void show(boolean showComponent) {

        if (rootLayout != null) {
            rootLayout.setVisibility(showComponent ? View.VISIBLE : View.GONE);
            mMainContainer.setVisibility(showComponent ? View.VISIBLE : View.GONE);
        }
    }

}