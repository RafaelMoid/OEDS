package br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import br.com.eadfiocruzpe.Utils.StringUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;
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
 *    - Update tables, migrate database:
 *    https://medium.com/google-developers/understanding-migrations-with-room-f01e04b07929
 */

@Entity(tableName = "revenues_targeted_ph", primaryKeys = {"search_id", "id", "source", "name"})
public class LDBRevenueTargetedPH {

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
    @ColumnInfo(name = "source")
    private String mSource = "";

    @NonNull
    @ColumnInfo(name = "name")
    private String mName = "";

    /**
     * Other fields
     */
    @ColumnInfo(name = "type")
    private String mType;

    @ColumnInfo(name = "initial_allocation")
    private double mInitialAllocation = -1;

    @ColumnInfo(name = "updated_allocation_prediction")
    private double mUpdatedAllocationPrediction = -1;

    @ColumnInfo(name = "value_received_so_far")
    private double mValueReceivedSoFar = -1;

    @ColumnInfo(name = "percentage_received_value_so_far")
    private double mPercentageReceivedValueSoFar = -1;

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

    public String getSource() {
        return mSource;
    }

    public void setSource(String source) {
        mSource = source;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type, String name) {
        BasicValidators validationUtils = new BasicValidators();
        TypeUtils typeUtils = new TypeUtils();

        if (validationUtils.isValidString(type)) {
            mType = type;
            setSource(typeUtils.getRevenueSource(getType(), name));
        }
    }

    public void setType(String type) {
        mType = type;
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

    public void setInitialAllocation(String initialAllocation) {
        StringUtils stringUtils = new StringUtils();
        mInitialAllocation = stringUtils.getDoubleValueFromString(initialAllocation);
    }

    public void setInitialAllocation(double initialAllocation) {
        mInitialAllocation = initialAllocation;
    }

    public double getUpdatedAllocationPrediction() {
        return mUpdatedAllocationPrediction;
    }

    public void setUpdatedAllocationPrediction(String updatedAllocationPrediction) {
        StringUtils stringUtils = new StringUtils();
        mUpdatedAllocationPrediction = stringUtils.getDoubleValueFromString(updatedAllocationPrediction);
    }

    public void setUpdatedAllocationPrediction(double updatedAllocationPrediction) {
        mUpdatedAllocationPrediction = updatedAllocationPrediction;
    }

    public double getValueReceivedSoFar() {
        return mValueReceivedSoFar;
    }

    public void setValueReceivedSoFar(String receivedValueSoFar) {
        StringUtils stringUtils = new StringUtils();
        mValueReceivedSoFar = stringUtils.getDoubleValueFromString(receivedValueSoFar);
    }

    public void setValueReceivedSoFar(double receivedValueSoFar) {
        mValueReceivedSoFar = receivedValueSoFar;
    }

    public double getPercentageReceivedValueSoFar() {
        return mPercentageReceivedValueSoFar;
    }

    public void setPercentageReceivedValueSoFar(String percentageReceivedValueSoFar) {
        StringUtils stringUtils = new StringUtils();
        mPercentageReceivedValueSoFar = stringUtils.getDoubleValueFromString(percentageReceivedValueSoFar);
    }

    public void setPercentageReceivedValueSoFar(double percentageReceivedValueSoFar) {
        mPercentageReceivedValueSoFar = percentageReceivedValueSoFar;
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