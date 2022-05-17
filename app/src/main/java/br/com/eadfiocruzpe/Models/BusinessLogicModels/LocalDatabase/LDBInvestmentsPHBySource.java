package br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

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

@Entity(tableName = "investments_ph_by_source", primaryKeys = {"search_id"})
public class LDBInvestmentsPHBySource {

    /**
     * Primary Keys
     */
    @NonNull
    @ColumnInfo(name = "search_id")
    private String mLDBSearchId = "";

    /**
     * Other fields
     */
    @ColumnInfo(name = "federal_investment_ph_brazil")
    private double mFederalInvestmentPHBrazil;

    @ColumnInfo(name = "federal_investment_ph_city")
    private double mFederalInvestmentPHCity;

    @ColumnInfo(name = "state_investment_ph_city")
    private double mStateInvestmentPHCity;

    @ColumnInfo(name = "other_investment_ph_city")
    private double mOtherInvestmentPHCity;

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

    public double getFederalInvestmentPHBrazil() {
        return mFederalInvestmentPHBrazil;
    }

    public void setFederalInvestmentPHBrazil(double federalInvestmentPHBrazil) {
        mFederalInvestmentPHBrazil = federalInvestmentPHBrazil;
    }

    public double getFederalInvestmentPHCity() {
        return mFederalInvestmentPHCity;
    }

    public void setFederalInvestmentPHCity(double federalInvestmentPHCity) {
        mFederalInvestmentPHCity = federalInvestmentPHCity;
    }

    public double getStateInvestmentPHCity() {
        return mStateInvestmentPHCity;
    }

    public void setStateInvestmentPHCity(double stateInvestmentPHCity) {
        mStateInvestmentPHCity = stateInvestmentPHCity;
    }

    public double getOtherInvestmentPHCity() {
        return mOtherInvestmentPHCity;
    }

    public void setOtherInvestmentPHCity(double otherInvestmentPHCity) {
        mOtherInvestmentPHCity = otherInvestmentPHCity;
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