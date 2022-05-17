package br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import br.com.eadfiocruzpe.Utils.StringUtils;
import br.com.eadfiocruzpe.Views.Validators.BasicValidators;

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
 *    - Update tables -> migrate database:
 *    https://medium.com/google-developers/understanding-migrations-with-room-f01e04b07929
 */

@Entity(tableName = "searches", primaryKeys = {"state", "city", "year", "period_year"})
public class LDBSearch {

    /**
     * Primary keys
     */
    @NonNull
    @ColumnInfo(name = "state")
    private String mState = "";

    @NonNull
    @ColumnInfo(name = "city")
    private String mCity = "";

    @ColumnInfo(name = "state_code")
    private String mStateCode;

    @NonNull
    @ColumnInfo(name = "year")
    private String mYear = "";

    @NonNull
    @ColumnInfo(name = "period_year")
    private String mPeriodYear = "";

    /**
     * Other attributes
     */
    @ColumnInfo(name = "city_code")
    private String mCityCode;

    @ColumnInfo(name = "period_year_code")
    private String mPeriodYearCode;

    @ColumnInfo(name = "favorite")
    private boolean mFavorite;

    @ColumnInfo(name = "searcher_id")
    private String mUserId;

    @ColumnInfo(name = "searcher_email")
    private String mEmail;

    @ColumnInfo(name = "searcher_location")
    private String mLocation;

    @ColumnInfo(name = "created_at")
    private String mCreatedAt;

    @ColumnInfo(name = "updated_at")
    private String mUpdatedAt;


    public String getSearchId() {
        try {
            StringUtils strUtils = new StringUtils();

            String id = strUtils.getCleanString(getState()) +
                    StringUtils.ID_JOINER +
                    strUtils.getCleanString(getCity()) +
                    StringUtils.ID_JOINER +
                    strUtils.getCleanString(getYear()) +
                    StringUtils.ID_JOINER +
                    strUtils.getCleanString(getPeriodYear());

            id = strUtils.getCleanString(id);

            return id;

        } catch (NullPointerException ignored) {
            return "";
        }
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        mState = state;
    }

    public String getStateCode() {
        return mStateCode;
    }

    public void setStateCode(String stateCode) {
        mStateCode = stateCode;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public String getCityCode() {
        return mCityCode;
    }

    public void setCityCode(String cityCode) {
        mCityCode = cityCode;
    }

    public String getYear() {
        return mYear;
    }

    public void setYear(String year) {
        mYear = year;
    }

    public String getPeriodYear() {
        return mPeriodYear;
    }

    public void setPeriodYear(String periodYear) {
        mPeriodYear = periodYear;
    }

    public String getPeriodYearCode() {
        return mPeriodYearCode;
    }

    public void setPeriodYearCode(String periodYearCode) {
        mPeriodYearCode = periodYearCode;
    }

    public void setFavorite(boolean favorite) {
        mFavorite = favorite;
    }

    public boolean getFavorite() {
        return mFavorite;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
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

    public String toString() {
        try {
            BasicValidators mValidationUtils = new BasicValidators();
            StringBuilder objStrRepr = new StringBuilder();

            if (mValidationUtils.isValidString(mCity)) {
                objStrRepr.append(mCity);
                objStrRepr.append(" - ");
            }

            if (mValidationUtils.isValidString(mState)) {
                objStrRepr.append(mState);
                objStrRepr.append(", ");
            }

            if (mValidationUtils.isValidString(mPeriodYear)) {
                objStrRepr.append(mPeriodYear);
                objStrRepr.append(" - ");
            }

            if (mValidationUtils.isValidYear(mYear)) {
                objStrRepr.append(mYear);
            }

            return objStrRepr.toString();

        } catch(Exception e) {
            e.printStackTrace();
        }

        return "";
    }

}