package br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import br.com.eadfiocruzpe.Utils.StringUtils;

/**
 * ATTENTION
 *
 * This Database is using the Room Persistence Library to manage the SQLite database, before you
 * perform any change the fields of this class tagged with "@ColumnInfo" and/or "@NonNull",
 * please read the links below to understand how it works and avoid deleting ALL the data from a
 * user's SQLite Database when the update is downloaded on their phone.
 *
 *    - Create tables:
 *    https://developer.android.com/topic/libraries/architecture/room
 *
 *    - Update tables, migrate database:
 *    https://medium.com/google-developers/understanding-migrations-with-room-f01e04b07929
 */

@Entity(tableName = "gov_investments_ph", primaryKeys = {"search_id", "id", "name"})
public class LDBGovInvestmentPH {

    /**
     * Primary Keys
     */
    @NonNull
    @ColumnInfo(name = "search_id")
    private String mLDBSearchId = "";

    @NonNull
    @ColumnInfo(name = "id")
    private String mId = "";

    @NonNull
    @ColumnInfo(name = "name")
    private String mName = "";

    /**
     * Other fields
     */
    @ColumnInfo(name = "initial_allocation")
    private double mInitialAllocation = -1f;

    @ColumnInfo(name = "allocation_until_search_date")
    private double mAllocationUntilSearchDate = -1f;

    @ColumnInfo(name = "expenses_prepared_for_payment_until_search_date")
    private double mExpensesPreparedForPaymentUntilSearchDate = -1f;

    @ColumnInfo(name = "percentage_expenses_prepared_for_payment")
    private double mPercentageExpensesPreparedForPayment = -1f;

    @ColumnInfo(name = "paid_expenses_until_search_date")
    private double mPaidExpensesUntilSearchDate = -1f;

    @ColumnInfo(name = "percentage_paid_expenses_until_search_date")
    private double mPercentagePaidExpensesUntilSearchDate = -1f;

    @ColumnInfo(name = "created_at")
    private String mCreatedAt;

    @ColumnInfo(name = "updated_at")
    private String mUpdatedAt;


    public String getLDBSearchId() {
        return mLDBSearchId;
    }

    public void setLDBSearchId(String ldbSearchId) {
        mLDBSearchId = ldbSearchId;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public double getInitialAllocation() {
        return mInitialAllocation;
    }

    public void setInitialAllocation(double initialAllocation) {
        mInitialAllocation = initialAllocation;
    }

    public void setInitialAllocation(String initialAllocation) {
        StringUtils stringUtils = new StringUtils();
        mInitialAllocation = stringUtils.getDoubleValueFromString(initialAllocation);
    }

    public double getAllocationUntilSearchDate() {
        return mAllocationUntilSearchDate;
    }

    public void setAllocationUntilSearchDate(String allocationUntilSearchDate) {
        StringUtils stringUtils = new StringUtils();
        mAllocationUntilSearchDate = stringUtils.getDoubleValueFromString(allocationUntilSearchDate);
    }

    public void setAllocationUntilSearchDate(double allocationUntilSearchDate) {
        mAllocationUntilSearchDate = allocationUntilSearchDate;
    }

    public double getExpensesPreparedForPaymentUntilSearchDate() {
        return mExpensesPreparedForPaymentUntilSearchDate;
    }

    public void setExpensesPreparedForPaymentUntilSearchDate(String expenses) {
        StringUtils stringUtils = new StringUtils();
        mExpensesPreparedForPaymentUntilSearchDate = stringUtils.getDoubleValueFromString(expenses);
    }

    public void setExpensesPreparedForPaymentUntilSearchDate(double expenses) {
        mExpensesPreparedForPaymentUntilSearchDate = expenses;
    }

    public double getPercentageExpensesPreparedForPayment() {
        return mPercentageExpensesPreparedForPayment;
    }

    public void setPercentageExpensesPreparedForPayment(String percentage) {
        StringUtils stringUtils = new StringUtils();
        mPercentageExpensesPreparedForPayment = stringUtils.getDoubleValueFromString(percentage);
    }

    public void setPercentageExpensesPreparedForPayment(double percentage) {
        mPercentageExpensesPreparedForPayment = percentage;
    }

    public double getPaidExpensesUntilSearchDate() {
        return mPaidExpensesUntilSearchDate;
    }

    public void setPaidExpensesUntilSearchDate(String paidExpenses) {
        StringUtils stringUtils = new StringUtils();
        mPaidExpensesUntilSearchDate = stringUtils.getDoubleValueFromString(paidExpenses);
    }

    public void setPaidExpensesUntilSearchDate(double paidExpenses) {
        mPaidExpensesUntilSearchDate = paidExpenses;
    }

    public double getPercentagePaidExpensesUntilSearchDate() {
        return mPercentagePaidExpensesUntilSearchDate;
    }

    public void setPercentagePaidExpensesUntilSearchDate(String percentage) {
        StringUtils stringUtils = new StringUtils();
        mPercentagePaidExpensesUntilSearchDate = stringUtils.getDoubleValueFromString(percentage);
    }

    public void setPercentagePaidExpensesUntilSearchDate(double percentage) {
        mPercentagePaidExpensesUntilSearchDate = percentage;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        mCreatedAt = createdAt;
    }

    public String getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        mUpdatedAt = updatedAt;
    }

}