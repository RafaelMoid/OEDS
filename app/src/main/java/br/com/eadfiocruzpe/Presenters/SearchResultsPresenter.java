package br.com.eadfiocruzpe.Presenters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.res.Resources;
import android.database.sqlite.SQLiteConstraintException;

import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Contracts.DBQueryContract;
import br.com.eadfiocruzpe.Contracts.SearchResultsPresenterContract;
import br.com.eadfiocruzpe.Utils.ConstantUtils;
import br.com.eadfiocruzpe.Utils.DateTimeUtils;
import br.com.eadfiocruzpe.Utils.LogUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;
import br.com.eadfiocruzpe.Persistence.DBManager;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBExpense;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBGovInvestmentPH;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBInvestmentsPHBySource;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBRevenueTargetedPH;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBCityIndicator;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBSearch;
import br.com.eadfiocruzpe.Views.Validators.BasicValidators;

public class SearchResultsPresenter implements
        SearchResultsPresenterContract.Presenter {

    private final String TAG = "SearchResPresenter";

    private LDBSearch mValidLDBSearch;
    private String mCreatedAt;

    private HashMap<String, LDBCityIndicator> mCityIndicators;
    private HashMap<String, LDBRevenueTargetedPH> mRevenuesCityPH;
    private HashMap<String, LDBGovInvestmentPH> mGovInvestmentsCityPH;
    private LDBInvestmentsPHBySource mInvestmentsPHBySource;
    private ArrayList<LDBExpense> mDeclaredExpenses;

    private LogUtils mLogUtils = new LogUtils();

    /**
     * Getters and Setters: LDBSearch
     */
    LDBSearch getValidSearch() {
        return mValidLDBSearch;
    }

    void setValidSearch(LDBSearch validSearch) {
        mValidLDBSearch = validSearch;
    }

    /**
     * Getters and Setters: City indicators
     */
    void setCityIndicators(HashMap<String, LDBCityIndicator> cityIndicators) {
        mCityIndicators = cityIndicators;
    }

    private HashMap<String, LDBCityIndicator> getCityIndicators() {
        return mCityIndicators;
    }

    /**
     * Getters and Setters: Investments
     */
    LDBInvestmentsPHBySource getGovInvestmentsPHBySource() {
        return mInvestmentsPHBySource;
    }

    private void setGovInvestmentsPHBySource(LDBInvestmentsPHBySource investmentsPHBySource) {
        mInvestmentsPHBySource = investmentsPHBySource;
    }

    private HashMap<String, LDBGovInvestmentPH> getGovInvestmentsCityPH(){
        return mGovInvestmentsCityPH;
    }

    private void setGovInvestmentsCityPH(HashMap<String, LDBGovInvestmentPH> govInvestmentsCityPH){
        mGovInvestmentsCityPH = govInvestmentsCityPH;
    }

    /**
     * Getters and Setters: Revenues and expenses declared by city
     */
    void setRevenuesAndExpensesDeclaredByCity(HashMap<String, LDBRevenueTargetedPH> revenue,
                                              HashMap<String, LDBGovInvestmentPH> expenses) {
        try {
            mRevenuesCityPH = revenue;
            mGovInvestmentsCityPH = expenses;

            // Set the Investments object, a summary of the information used by the app
            if (getGovInvestmentsPHBySource() == null) {
                setGovInvestmentsPHBySource(new LDBInvestmentsPHBySource());
            }

            // Set the value invested by the federal, state and city government on a city PHs
            splitInvestmentsPHBySource(getRevenuesCityPH());

            // Set declared city expenses that are going to be displayed on the Dashboard
            setDeclaredExpenses(getGovInvestmentsCityPH());

            // Set Created at
            DateTimeUtils dateTimeUtils = new DateTimeUtils();

            if (getGovInvestmentsPHBySource().getCreatedAt() == null) {
                getGovInvestmentsPHBySource().setCreatedAt(dateTimeUtils.getNowTimeStamp());
            } else {
                getGovInvestmentsPHBySource().setUpdatedAt(dateTimeUtils.getNowTimeStamp());
            }

            setInvestmentOtherSources();

        } catch (NullPointerException ignored) {}
        catch (NumberFormatException ignored) {}
    }

    private void splitInvestmentsPHBySource(HashMap<String, LDBRevenueTargetedPH> cityRevenuesForPH) {
        try {
            BasicValidators validationUtils = new BasicValidators();
            ArrayList<String> revenueSourcesKeys = new ArrayList<>(cityRevenuesForPH.keySet());
            double federalInvestmentCityPH = 0;
            double stateInvestmentCityPH = 0;
            // Not used in this version of the app
            // double investmentsOtherSourcesCityPH = 0;

            for (String key : revenueSourcesKeys) {
                LDBRevenueTargetedPH revenue = cityRevenuesForPH.get(key);

                if (validationUtils.isNotNull(revenue)) {

                    if (validationUtils.isValidString(revenue.getSource()) &&
                            validationUtils.isValidString(revenue.getType()) &&
                            revenue.getValueReceivedSoFar() != -1) {

                        switch (revenue.getSource()) {

                            case ConstantUtils.REVENUE_SOURCE_FEDERAL_GOV: {
                                federalInvestmentCityPH += revenue.getValueReceivedSoFar();
                                break;
                            }

                            case ConstantUtils.REVENUE_SOURCE_STATE_GOV: {
                                stateInvestmentCityPH += revenue.getValueReceivedSoFar();
                                break;
                            }

                            case ConstantUtils.REVENUE_SOURCE_OTHER: {
                                // Not used in this version of the app, but is very likely to appear
                                // in the next release
                                // investmentsOtherSourcesCityPH += revenue.getValueReceivedSoFar();
                                break;
                            }
                        }
                    }
                }
            }

            getGovInvestmentsPHBySource().setFederalInvestmentPHCity(federalInvestmentCityPH);
            getGovInvestmentsPHBySource().setStateInvestmentPHCity(stateInvestmentCityPH);

        } catch(NullPointerException ignored) {}
    }

    private void setDeclaredExpenses(HashMap<String, LDBGovInvestmentPH> declaredExpenses) {
        try {
            mDeclaredExpenses = new ArrayList<>();

            BasicValidators validationUtils = new BasicValidators();
            DateTimeUtils dateTimeUtils = new DateTimeUtils();
            ArrayList<String> expensesKeys = new ArrayList<>(declaredExpenses.keySet());

            // Add the expenses into the list
            for (String key : expensesKeys) {
                LDBGovInvestmentPH declaredExpense = declaredExpenses.get(key);
                LDBExpense expense = new LDBExpense();

                if (validationUtils.isNotNull(declaredExpense)) {

                    if (validationUtils.isValidString(declaredExpense.getId()) &&
                            validationUtils.isValidString(declaredExpense.getName()) &&
                            declaredExpense.getPaidExpensesUntilSearchDate() != -1) {

                        expense.setExpenseId(declaredExpense.getId());
                        expense.setCategory(declaredExpense.getName());
                        expense.setTitle(declaredExpense.getName());
                        expense.setValue(declaredExpense.getPaidExpensesUntilSearchDate());
                        expense.setCreatedAt(dateTimeUtils.getNowTimeStamp());

                        mDeclaredExpenses.add(expense);
                    }
                }
            }

            // Set the percentage value of each expense
            double totalExpenses = 0;

            for (LDBExpense expense : getDeclaredExpenses()) {
                totalExpenses += expense.getValue();
            }

            for (LDBExpense expense : getDeclaredExpenses()) {
                expense.setPercentageValue((float)((expense.getValue() * 100) / totalExpenses));
            }

        } catch (NullPointerException ignored) {}
    }

    private HashMap<String, LDBRevenueTargetedPH> getRevenuesCityPH(){
        return mRevenuesCityPH;
    }

    /**
     * TotalInvestments dataset and its labels
     *
     * The order in which the elements of this dataset must be build is this:
     *
     *   - Investments from other sources
     *   - Investments from state government
     *   - Investments from federal government
     *
     * Otherwise the component ComponentCompareTotalInvestments would not work properly
     */
    ArrayList<Double> getDatasetInvestmentsCityPH() {
        try {
            ArrayList<Double> dataSet = new ArrayList<>();

            // Build the dataset
            if (getGovInvestmentsPHBySource() != null) {
                setInvestmentOtherSources();

                dataSet.add(getGovInvestmentsPHBySource().getOtherInvestmentPHCity());
                dataSet.add(getGovInvestmentsPHBySource().getStateInvestmentPHCity());
                dataSet.add(getGovInvestmentsPHBySource().getFederalInvestmentPHCity());
            }

            if (getGovInvestmentsPHBySource().getOtherInvestmentPHCity() != 0 ||
                getGovInvestmentsPHBySource().getStateInvestmentPHCity() != 0 ||
                getGovInvestmentsPHBySource().getFederalInvestmentPHCity() != 0) {

                return dataSet;
            } else {
                return null;
            }

        } catch(NullPointerException npe) {
            npe.printStackTrace();
        }

        return null;
    }

    private void setInvestmentOtherSources() {
        try {

            if (getGovInvestmentsPHBySource() != null) {
                double totalDeclaredExpenses = 0;
                double otherInvestments = 0;
                double investmentsOtherSources = 0;

                if (getDeclaredExpenses() != null) {

                    for (LDBExpense expense : getDeclaredExpenses()) {
                        totalDeclaredExpenses += expense.getValue();
                    }
                }

                otherInvestments += getGovInvestmentsPHBySource().getStateInvestmentPHCity();
                otherInvestments += getGovInvestmentsPHBySource().getFederalInvestmentPHCity();

                if (totalDeclaredExpenses >= otherInvestments) {
                    investmentsOtherSources = totalDeclaredExpenses - otherInvestments;
                }

                getGovInvestmentsPHBySource().setOtherInvestmentPHCity(investmentsOtherSources);
            }

        } catch(NullPointerException ignored) {}
    }


    ArrayList<String> getLabelsInvestmentsCityPH(Resources resources) {
        try {
            ArrayList<String> labels = new ArrayList<>();
            labels.add(resources.getString(R.string.dashboard_page_lbl_city_government));
            labels.add(resources.getString(R.string.dashboard_page_lbl_state_government));
            labels.add(resources.getString(R.string.dashboard_page_lbl_federal_government));

            return labels;

        } catch(NullPointerException npe) {
            npe.printStackTrace();
        }

        return null;
    }

    public ArrayList<LDBExpense> getDeclaredExpenses() {
        return mDeclaredExpenses;
    }

    /**
     * Getters and Setters: Other values
     */
    double getPercentageCityFinancialAutonomy() {
        try {
            LDBCityIndicator ldbCityIndicator = getCityIndicators().get(ConstantUtils.KEY_IND_CITY_AUTONOMY_LEVEL_INVESTMENTS_ON_PH);

            if (ldbCityIndicator.getValue() > 0) {
                return ldbCityIndicator.getValue();
            } else {
                return 0;
            }
        } catch (NullPointerException npe) {
            return 0;
        }
    }

    /**
     * This method returns the indicator 2.1 / 365, this is the description of the indicator 2.1:
     * "Despesa total com Saúde, em R$/hab, sob a responsabilidade do Município, por habitante".
     * Ex: R$ 736.96
     *
     * An example can be found here:
     *      1 - Access: http://siops.datasus.gov.br/rel_LRF.php
     *      2 - Click on the button "< voltar"
     *      3 - Fill the form
     *      4 - Submit the form and you'll see a table with the table "Indicadores do Ente Federado"
     */
    double getValueCitizenDay() {
        try {
            LDBCityIndicator ldbCityIndicator = getCityIndicators().get(ConstantUtils.KEY_IND_VALUE_PER_CITIZEN_PER_YEAR_FOR_PH);

            if (ldbCityIndicator.getValue() > 0) {
                return ldbCityIndicator.getValue() / 365;
            } else {
                return 0;
            }
        } catch (NullPointerException npe) {
            return 0;
        }
    }

    /**
     * This method return the indicator 2.1 as it is, this is the description of the indicator 2.1:
     * "Despesa total com Saúde, em R$/hab, sob a responsabilidade do Município, por habitante".
     * Ex: R$ 736.96
     *
     * An example can be found here:
     *      1 - Access: http://siops.datasus.gov.br/rel_LRF.php
     *      2 - Click on the button "< voltar"
     *      3 - Fill the form
     *      4 - Submit the form and you'll see a table with the table "Indicadores do Ente Federado"
     */
    double getValueCitizenYear() {
        try {
            LDBCityIndicator ldbCityIndicator = getCityIndicators().get(ConstantUtils.KEY_IND_VALUE_PER_CITIZEN_PER_YEAR_FOR_PH);

            if (ldbCityIndicator.getValue() > 0) {
                return ldbCityIndicator.getValue();
            } else {
                return 0;
            }
        } catch (NullPointerException npe) {
            return 0;
        }
    }

    /**
     * This is the description of the indicator 2.1:
     * "Despesa total com Saúde, em R$/hab, sob a responsabilidade do Município, por habitante".
     * Ex: R$ 736.96
     *
     * This is the description of the indicator 3.1:
     * "Participação das transferências para a Saúde em relação à despesa total do Município com saúde".
     * Ex: 66.19 %
     *
     * An example can be found here:
     *      1 - Access: http://siops.datasus.gov.br/rel_LRF.php
     *      2 - Click on the button "< voltar"
     *      3 - Fill the form
     *      4 - Submit the form and you'll see a table with the table "Indicadores do Ente Federado"
     */
    float getValueInvestedOtherSourcesYearlyCitizenValue() {
        try {
            LDBCityIndicator indicator_2_1 = getCityIndicators().get(ConstantUtils.KEY_IND_VALUE_PER_CITIZEN_PER_YEAR_FOR_PH);
            LDBCityIndicator indicator_3_1 = getCityIndicators().get(ConstantUtils.KEY_IND_PARTICIPATION_OTHER_SOURCES_TOTAL_EXPENSES_ON_PH);

            return (float) (indicator_2_1.getValue() * (indicator_3_1.getValue() / 100));

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to getValueInvestedCityYearlyCitizenValue");
        }

        return 0;
    }

    float getValueInvestedCityYearlyCitizenValue() {
        try {
            LDBCityIndicator indicator_2_1 = getCityIndicators().get(ConstantUtils.KEY_IND_VALUE_PER_CITIZEN_PER_YEAR_FOR_PH);

            return (float) (indicator_2_1.getValue() - getValueInvestedOtherSourcesYearlyCitizenValue());

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to getValueInvestedCityYearlyCitizenValue");
        }

        return 0;
    }

    /**
     * Getters and Setters: Timestamps
     */
    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        mCreatedAt = createdAt;
    }

    /**
     * Storage: Write
     */
    @Override
    public void save() {
        try {
            BasicValidators valUtils = new BasicValidators();
            String searchId = getValidSearch().getSearchId();

            if (valUtils.isValidString(searchId)) {
                saveCityIndicatorsCache(searchId);
                saveRevenueTargetedPHCache(searchId);
                saveGovInvestmentsPHCache(searchId);
                saveGovInvestmentsPHBySourceCache(searchId);
                saveDeclaredExpensesCache(searchId);
            } else {
                mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                        TAG + "Failed to save, invalid search id");
            }

        } catch (Exception e) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to save" + e.getMessage());
        }
    }

    private void saveCityIndicatorsCache(final String searchId) {
        try {

            if (getCityIndicators() != null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            DBManager.getDB().runInTransaction(new Runnable() {

                                @Override
                                public void run() {

                                    try {

                                        for (final LDBCityIndicator indicator : getCityIndicators().values()) {
                                            indicator.setLDBSearchId(searchId);

                                            DateTimeUtils dtUtils = new DateTimeUtils();
                                            LDBCityIndicator indicatorFromDB = DBManager.getDB().cityIndicatorDao().find(
                                                    indicator.getLDBSearchId(),
                                                    indicator.getId());

                                            if (indicatorFromDB == null) {
                                                indicator.setCreatedAt(dtUtils.getNowTimeStamp());
                                                DBManager.getDB().cityIndicatorDao().insert(indicator);
                                            } else {
                                                indicator.setUpdatedAt(dtUtils.getNowTimeStamp());
                                                DBManager.getDB().cityIndicatorDao().update(indicator);
                                            }

                                        }

                                    } catch (SQLiteConstraintException sqlException) {
                                        mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                                TAG + "Failed to saveCityIndicatorsCache: " + sqlException.getMessage());

                                    } catch (java.lang.IllegalStateException ies) {
                                        mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                                TAG + "Failed to saveCityIndicatorsCache: " + ies.getMessage());

                                    } catch (NullPointerException npe) {
                                        mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                                TAG + "Failed to saveCityIndicatorsCache: " + npe.getMessage());
                                    }

                                }
                            });

                        } catch (Exception e) {
                            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                                    TAG + "Failed to saveCityIndicatorsCache" + e.getMessage());
                        }

                    }
                }).start();
            }

        } catch (Exception e) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to saveCityIndicatorsCache" + e.getMessage());
        }
    }

    private void saveRevenueTargetedPHCache(final String searchId) {
        try {

            if (getRevenuesCityPH() != null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            DBManager.getDB().runInTransaction(new Runnable() {

                                @Override
                                public void run() {

                                    try {

                                        for (final LDBRevenueTargetedPH revenue : getRevenuesCityPH().values()) {
                                            revenue.setLDBSearchId(searchId);

                                            DateTimeUtils dtUtils = new DateTimeUtils();
                                            LDBRevenueTargetedPH revenueFromDB = DBManager.getDB()
                                                    .revenueTargetedPHDao().find(
                                                            revenue.getLDBSearchId(),
                                                            revenue.getId(),
                                                            revenue.getSource(),
                                                            revenue.getName());

                                            if (revenueFromDB == null) {
                                                revenue.setCreatedAt(dtUtils.getNowTimeStamp());
                                                DBManager.getDB().revenueTargetedPHDao().insert(revenue);
                                            } else {
                                                revenue.setUpdatedAt(dtUtils.getNowTimeStamp());
                                                DBManager.getDB().revenueTargetedPHDao().update(revenue);
                                            }
                                        }

                                    } catch (SQLiteConstraintException sqlException) {
                                        mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                                TAG + "Failed to saveRevenueTargetedPHCache: " + sqlException.getMessage());

                                    } catch (java.lang.IllegalStateException ies) {
                                        mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                                TAG + "Failed to saveRevenueTargetedPHCache: " + ies.getMessage());

                                    } catch (NullPointerException npe) {
                                        mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                                TAG + "Failed to saveRevenueTargetedPHCache: " + npe.getMessage());
                                    }

                                }
                            });

                        } catch (NullPointerException npe) {
                            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                    TAG + "Failed to saveRevenueTargetedPHCache: " + npe.getMessage());
                        }

                    }
                }).start();
            }

        } catch (Exception e) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to saveRevenueTargetedPHCache" + e.getMessage());
        }
    }

    private void saveGovInvestmentsPHCache(final String searchId) {
        try {

            if (getGovInvestmentsCityPH() != null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            DBManager.getDB().runInTransaction(new Runnable() {

                                @Override
                                public void run() {

                                    try {

                                        for (final LDBGovInvestmentPH govInvestment : getGovInvestmentsCityPH().values()) {
                                            govInvestment.setLDBSearchId(searchId);

                                            DateTimeUtils dtUtils = new DateTimeUtils();
                                            LDBGovInvestmentPH govInvestmentsFromDB = DBManager.getDB()
                                                    .govInvestmentPHDao().find(
                                                            govInvestment.getLDBSearchId(),
                                                            govInvestment.getId(),
                                                            govInvestment.getName());

                                            if (govInvestmentsFromDB == null) {
                                                govInvestment.setCreatedAt(dtUtils.getNowTimeStamp());
                                                DBManager.getDB().govInvestmentPHDao().insert(govInvestment);
                                            } else {
                                                govInvestment.setUpdatedAt(dtUtils.getNowTimeStamp());
                                                DBManager.getDB().govInvestmentPHDao().update(govInvestment);
                                            }
                                        }

                                    } catch (SQLiteConstraintException sqlException) {
                                        mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                                TAG + "Failed to saveGovInvestmentsPHCache: " + sqlException.getMessage());

                                    } catch (java.lang.IllegalStateException ies) {
                                        mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                                TAG + "Failed to saveGovInvestmentsPHCache: " + ies.getMessage());

                                    } catch (NullPointerException npe) {
                                        mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                                TAG + "Failed to saveGovInvestmentsPHCache: " + npe.getMessage());
                                    }
                                }
                            });

                        } catch (Exception e) {
                            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                                    TAG + "Failed to saveGovInvestmentsPHCache" + e.getMessage());
                        }

                    }
                }).start();
            }

        } catch (Exception e) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to saveGovInvestmentsPHCache" + e.getMessage());
        }
    }

    private void saveGovInvestmentsPHBySourceCache(String searchId) {
        try {

            if (getGovInvestmentsPHBySource() != null) {
                getGovInvestmentsPHBySource().setLDBSearchId(searchId);

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            DBManager.getDB().runInTransaction(new Runnable() {

                                @Override
                                public void run() {

                                    try {
                                        DateTimeUtils dtUtils = new DateTimeUtils();
                                        LDBInvestmentsPHBySource govInvestmentsFromDB = DBManager.getDB()
                                                .govInvestmentPHBySourceDao().find(
                                                        getGovInvestmentsPHBySource().getLDBSearchId());

                                        if (govInvestmentsFromDB == null) {
                                            getGovInvestmentsPHBySource().setCreatedAt(dtUtils.getNowTimeStamp());
                                            DBManager.getDB().govInvestmentPHBySourceDao().insert(mInvestmentsPHBySource);
                                        } else {
                                            getGovInvestmentsPHBySource().setUpdatedAt(dtUtils.getNowTimeStamp());
                                            DBManager.getDB().govInvestmentPHBySourceDao().update(mInvestmentsPHBySource);
                                        }

                                    } catch (SQLiteConstraintException sqlException) {
                                        mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                                TAG + "Failed to saveGovInvestmentsPHBySourceCache: " + sqlException.getMessage());

                                    } catch (java.lang.IllegalStateException ies) {
                                        mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                                TAG + "Failed to saveGovInvestmentsPHBySourceCache: " + ies.getMessage());

                                    } catch (NullPointerException npe) {
                                        mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                                TAG + "Failed to saveGovInvestmentsPHBySourceCache: " + npe.getMessage());
                                    }

                                }
                            });

                        } catch (Exception e) {
                            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                    TAG + "Failed to saveGovInvestmentsPHBySourceCache: " + e.getMessage());
                        }

                    }
                }).start();
            }

        } catch (Exception e) {
            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to saveGovInvestmentsPHBySourceCache: " + e.getMessage());
        }
    }

    private void saveDeclaredExpensesCache(final String searchId) {
        try {

            if (getDeclaredExpenses() != null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            DBManager.getDB().runInTransaction(new Runnable() {

                                @Override
                                public void run() {

                                    try {

                                        for (final LDBExpense expense : getDeclaredExpenses()) {
                                            expense.setLDBSearchId(searchId);

                                            DateTimeUtils dtUtils = new DateTimeUtils();
                                            LDBExpense expenseFromDB = DBManager.getDB().expenseDao().find(
                                                    expense.getLDBSearchId(),
                                                    expense.getTitle());

                                            if (expenseFromDB == null) {
                                                expense.setCreatedAt(dtUtils.getNowTimeStamp());
                                                DBManager.getDB().expenseDao().insert(expense);
                                            } else {
                                                expense.setUpdatedAt(dtUtils.getNowTimeStamp());
                                                DBManager.getDB().expenseDao().update(expense);
                                            }
                                        }

                                    } catch (SQLiteConstraintException sqlException) {
                                        mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                                TAG + "Failed to saveDeclaredExpensesCache: " + sqlException.getMessage());

                                    } catch (java.lang.IllegalStateException ies) {
                                        mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                                TAG + "Failed to saveDeclaredExpensesCache: " + ies.getMessage());

                                    } catch (NullPointerException npe) {
                                        mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                                TAG + "Failed to saveDeclaredExpensesCache: " + npe.getMessage());
                                    }
                                }
                            });

                        } catch (Exception e) {
                            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                                    TAG + "Failed to saveDeclaredExpensesCache" + e.getMessage());
                        }

                    }
                }).start();
            }

        } catch (Exception e) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to saveDeclaredExpensesCache" + e.getMessage());
        }
    }

    /**
     * Storage - Read: CityIndicators
     */
    @Override
    public void loadCityIndicatorsFromCache(final String searchId,
                                            final DBQueryContract loadingCallback) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        DBManager.getDB().runInTransaction(new Runnable() {

                            @Override
                            public void run() {

                                try {
                                    List<LDBCityIndicator> indicatorsFromDB = DBManager.getDB()
                                            .cityIndicatorDao().findAll(searchId);

                                    if (indicatorsFromDB != null) {

                                        if (getCityIndicators() == null) {
                                            setCityIndicators(new HashMap<String, LDBCityIndicator>());
                                        }

                                        getCityIndicators().clear();

                                        for (LDBCityIndicator indicator : indicatorsFromDB) {
                                            getCityIndicators().put(indicator.getId(), indicator);
                                        }

                                        if (getCityIndicators().size() > 0) {

                                            if (getValueInvestedCityYearlyCitizenValue() > 0) {
                                                // City indicators were successfully recovered from cache, load
                                                // the UI with the recovered values, otherwise make a request to the
                                                // backend
                                                loadingCallback.gotDataFromCache(true);
                                            } else {
                                                // Failed to recover data from cache, make a request to the backend
                                                loadingCallback.gotDataFromCache(false);
                                            }
                                        } else {
                                            // Failed to recover data from cache, make a request to the backend
                                            loadingCallback.gotDataFromCache(false);
                                        }
                                    } else {
                                        // Failed to recover data from cache, make a request to the backend
                                        loadingCallback.gotDataFromCache(false);
                                    }

                                } catch (SQLiteConstraintException sqlException) {
                                    mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                            TAG + "Failed to loadCityIndicatorsFromCache: " + sqlException.getMessage());

                                    // Failed to recover data from cache, make a request to the backend
                                    loadingCallback.gotDataFromCache(false);

                                } catch (java.lang.IllegalStateException ies) {
                                    mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                            TAG + "Failed to loadCityIndicatorsFromCache: " + ies.getMessage());

                                    // Failed to recover data from cache, make a request to the backend
                                    loadingCallback.gotDataFromCache(false);

                                } catch (NullPointerException npe) {
                                    mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                            TAG + "Failed to loadCityIndicatorsFromCache: " + npe.getMessage());

                                    // Failed to recover data from cache, make a request to the backend
                                    loadingCallback.gotDataFromCache(false);
                                }

                            }
                        });

                    } catch (Exception e) {
                        mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                                TAG + "Failed to loadCityIndicatorsFromCache" + e.getMessage());

                        // Failed to recover data from cache, make a request to the backend
                        loadingCallback.gotDataFromCache(false);
                    }

                }
            }).start();

        } catch (Exception e) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to loadCityIndicatorsFromCache" + e.getMessage());

            // Failed to recover data from cache, make a request to the backend
            loadingCallback.gotDataFromCache(false);
        }
    }

    /**
     * Storage - Read: Investments and Expenses
     */
    @Override
    public void loadRevenueDeclaredExpensesFromCache(final String searchId,
                                                 final DBQueryContract loadingCallback) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        DBManager.getDB().runInTransaction(new Runnable() {

                            @Override
                            public void run() {

                                try {
                                    List<LDBRevenueTargetedPH> revenuesFromDB = DBManager.getDB()
                                            .revenueTargetedPHDao().findAll(searchId);

                                    if (revenuesFromDB != null) {

                                        if (getRevenuesCityPH() == null) {
                                            setRevenuesAndExpensesDeclaredByCity(
                                                    new HashMap<String, LDBRevenueTargetedPH>(),
                                                    new HashMap<String, LDBGovInvestmentPH>());
                                        }

                                        getRevenuesCityPH().clear();

                                        for (LDBRevenueTargetedPH revenue : revenuesFromDB) {
                                            getRevenuesCityPH().put(revenue.getId(), revenue);
                                        }

                                        if (getRevenuesCityPH().size() > 0) {
                                            // List of revenues were successfully recovered from cache, load
                                            // the next dataset that relies on the InvestmentsAndExpenses
                                            // request
                                            loadGovInvestmentsPHFromCache(searchId, loadingCallback);
                                        } else {
                                            // Failed to reload data from the cache, make a request to the backend
                                            loadingCallback.gotDataFromCache(false);
                                        }
                                    } else {
                                        // Failed to reload data from the cache, make a request to the backend
                                        loadingCallback.gotDataFromCache(false);
                                    }

                                } catch (SQLiteConstraintException sqlException) {
                                    mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                            TAG + "Failed to loadRevenueTargetedOnPHFromCache: " + sqlException.getMessage());

                                    // Failed to reload data from the cache, make a request to the backend
                                    loadingCallback.gotDataFromCache(false);

                                } catch (java.lang.IllegalStateException ies) {
                                    mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                            TAG + "Failed to loadRevenueTargetedOnPHFromCache: " + ies.getMessage());

                                    // Failed to reload data from the cache, make a request to the backend
                                    loadingCallback.gotDataFromCache(false);

                                } catch (NullPointerException npe) {
                                    mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                            TAG + "Failed to loadRevenueTargetedOnPHFromCache: " + npe.getMessage());

                                    // Failed to reload data from the cache, make a request to the backend
                                    loadingCallback.gotDataFromCache(false);
                                }

                            }
                        });

                    } catch (Exception npe) {
                        mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                TAG + "Failed to loadRevenueTargetedOnPHFromCache: " + npe.getMessage());

                        // Failed to reload data from the cache, make a request to the backend
                        loadingCallback.gotDataFromCache(false);
                    }

                }
            }).start();

        } catch (Exception e) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to loadRevenueTargetedOnPHFromCache" + e.getMessage());

            // Failed to reload data from the cache, make a request to the backend
            loadingCallback.gotDataFromCache(false);
        }
    }

    private void loadGovInvestmentsPHFromCache(final String searchId,
                                               final DBQueryContract loadingCallback) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        DBManager.getDB().runInTransaction(new Runnable() {

                            @Override
                            public void run() {

                                try {
                                    List<LDBGovInvestmentPH> investmentsPHFromDB = DBManager.getDB()
                                            .govInvestmentPHDao().findAll(searchId);

                                    if (investmentsPHFromDB != null) {

                                        if (getGovInvestmentsCityPH() == null) {
                                            setGovInvestmentsCityPH(new HashMap<String, LDBGovInvestmentPH>());
                                        }

                                        getGovInvestmentsCityPH().clear();

                                        for (LDBGovInvestmentPH govInvestmentPH : investmentsPHFromDB) {
                                            getGovInvestmentsCityPH().put(govInvestmentPH.getId(), govInvestmentPH);
                                        }

                                        // Call the next method that relies on the same web request from SIOPS
                                        loadGovInvestmentsPHBySourceFromCache(searchId, loadingCallback);
                                    } else {
                                        // Failed to reload data from the cache, make a request to the backend
                                        loadingCallback.gotDataFromCache(false);
                                    }

                                } catch (SQLiteConstraintException sqlException) {
                                    mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                            TAG + "Failed to loadGovInvestmentsPHFromCache: " + sqlException.getMessage());

                                    // Failed to reload data from the cache, make a request to the backend
                                    loadingCallback.gotDataFromCache(false);

                                } catch (java.lang.IllegalStateException ies) {
                                    mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                            TAG + "Failed to loadGovInvestmentsPHFromCache: " + ies.getMessage());

                                    // Failed to reload data from the cache, make a request to the backend
                                    loadingCallback.gotDataFromCache(false);

                                } catch (NullPointerException npe) {
                                    mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                            TAG + "Failed to loadGovInvestmentsPHFromCache: " + npe.getMessage());

                                    // Failed to reload data from the cache, make a request to the backend
                                    loadingCallback.gotDataFromCache(false);
                                }
                            }
                        });

                    } catch (Exception e) {
                        mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                                TAG + "Failed to loadGovInvestmentsPHFromCache" + e.getMessage());

                        // Failed to reload data from the cache, make a request to the backend
                        loadingCallback.gotDataFromCache(false);
                    }

                }
            }).start();

        } catch (Exception e) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to loadGovInvestmentsPHFromCache" + e.getMessage());

            // Failed to reload data from the cache, make a request to the backend
            loadingCallback.gotDataFromCache(false);
        }
    }

    private void loadGovInvestmentsPHBySourceFromCache(final String searchId,
                                                       final DBQueryContract loadingCallback) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        DBManager.getDB().runInTransaction(new Runnable() {

                            @Override
                            public void run() {

                                try {
                                    LDBInvestmentsPHBySource investmentsPHFromDB = DBManager.getDB()
                                            .govInvestmentPHBySourceDao().find(searchId);

                                    if (investmentsPHFromDB != null) {
                                        setGovInvestmentsPHBySource(investmentsPHFromDB);
                                        loadDeclaredExpensesFromCache(searchId, loadingCallback);
                                    } else {
                                        // Failed to reload data from the cache, make a request to the backend
                                        loadingCallback.gotDataFromCache(false);
                                    }

                                } catch (SQLiteConstraintException sqlException) {
                                    mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                            TAG + "Failed to loadGovInvestmentsPHBySourceFromCache: " + sqlException.getMessage());

                                    // Failed to reload data from the cache, make a request to the backend
                                    loadingCallback.gotDataFromCache(false);

                                } catch (java.lang.IllegalStateException ies) {
                                    mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                            TAG + "Failed to loadGovInvestmentsPHBySourceFromCache: " + ies.getMessage());

                                    // Failed to reload data from the cache, make a request to the backend
                                    loadingCallback.gotDataFromCache(false);

                                } catch (NullPointerException npe) {
                                    mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                            TAG + "Failed to loadGovInvestmentsPHBySourceFromCache: " + npe.getMessage());

                                    // Failed to reload data from the cache, make a request to the backend
                                    loadingCallback.gotDataFromCache(false);
                                }
                            }
                        });

                    } catch (Exception e) {
                        mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                                TAG + "Failed to loadGovInvestmentsPHBySourceFromCache" + e.getMessage());

                        // Failed to reload data from the cache, make a request to the backend
                        loadingCallback.gotDataFromCache(false);
                    }

                }
            }).start();

        } catch (Exception e) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to loadGovInvestmentsPHBySourceFromCache" + e.getMessage());

            // Failed to reload data from the cache, make a request to the backend
            loadingCallback.gotDataFromCache(false);
        }
    }

    private void loadDeclaredExpensesFromCache(final String searchId,
                                               final DBQueryContract loadingCallback) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        DBManager.getDB().runInTransaction(new Runnable() {

                            @Override
                            public void run() {

                                try {
                                    List<LDBExpense> expensesPHFromDB = DBManager.getDB()
                                            .expenseDao().findAll(searchId);

                                    if (expensesPHFromDB != null) {

                                        if (getDeclaredExpenses() == null) {
                                            setDeclaredExpenses(new HashMap<String, LDBGovInvestmentPH>());
                                        }

                                        getDeclaredExpenses().clear();
                                        getDeclaredExpenses().addAll(expensesPHFromDB);

                                        if (getDeclaredExpenses().size() > 0) {
                                            // Successfully recovered list of expenses from cache, load the
                                            // components with the cache information.
                                            loadingCallback.gotDataFromCache(true);
                                        } else {
                                            // Failed to reload data from the cache, make a request to the backend
                                            loadingCallback.gotDataFromCache(false);
                                        }
                                    } else {
                                        // Failed to reload data from the cache, make a request to the backend
                                        loadingCallback.gotDataFromCache(false);
                                    }

                                } catch (SQLiteConstraintException sqlException) {
                                    mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                            TAG + "Failed to loadDeclaredExpensesFromCache: " + sqlException.getMessage());

                                    // Failed to reload data from the cache, make a request to the backend
                                    loadingCallback.gotDataFromCache(false);

                                } catch (java.lang.IllegalStateException ies) {
                                    mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                            TAG + "Failed to loadDeclaredExpensesFromCache: " + ies.getMessage());

                                    // Failed to reload data from the cache, make a request to the backend
                                    loadingCallback.gotDataFromCache(false);

                                } catch (NullPointerException npe) {
                                    mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                                            TAG + "Failed to loadDeclaredExpensesFromCache: " + npe.getMessage());

                                    // Failed to reload data from the cache, make a request to the backend
                                    loadingCallback.gotDataFromCache(false);
                                }
                            }
                        });

                    } catch (Exception e) {
                        mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                                TAG + "Failed to loadDeclaredExpensesFromCache" + e.getMessage());

                        // Failed to reload data from the cache, make a request to the backend
                        loadingCallback.gotDataFromCache(false);
                    }

                }
            }).start();

        } catch (Exception e) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to loadDeclaredExpensesFromCache" + e.getMessage());

            // Failed to reload data from the cache, make a request to the backend
            loadingCallback.gotDataFromCache(false);
        }
    }

}