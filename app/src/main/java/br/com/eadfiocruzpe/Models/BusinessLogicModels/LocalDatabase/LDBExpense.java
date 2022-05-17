package br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

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

@Entity(tableName = "city_expenses", primaryKeys = {"search_id", "title"})
public class LDBExpense {

    /**
     * Primary Keys
     */
    @NonNull
    @ColumnInfo(name = "search_id")
    private String mLDBSearchId = "";

    @NonNull
    @ColumnInfo(name = "title")
    @SerializedName("title")
    private String mTitle = "";

    /**
     * Other fields
     */
    @ColumnInfo(name = "expense_id")
    @SerializedName("expense_id")
    private String mExpenseId;

    @ColumnInfo(name = "category")
    @SerializedName("category")
    private String mCategory;

    @ColumnInfo(name = "value")
    @SerializedName("value")
    private double mValue;
    private float mPercentageValue;

    @ColumnInfo(name = "created_at")
    @SerializedName("created_at")
    private String mCreatedAt;

    @ColumnInfo(name = "updated_at")
    @SerializedName("updated_at")
    private String mUpdatedAt;


    public String getLDBSearchId() {
        return mLDBSearchId;
    }

    public void setLDBSearchId(String ldbSearchId) {
        mLDBSearchId = ldbSearchId;
    }

    public String getExpenseId() {
        return mExpenseId;
    }

    public void setExpenseId(String expenseId) {
        mExpenseId = expenseId;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public double getValue() {
        return mValue;
    }

    public void setValue(double value) {
        mValue = value;
    }

    public void setPercentageValue(float value) {
        mPercentageValue = value;
    }

    public float getPercentageValue() {
        return mPercentageValue;
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