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
@Entity(tableName = "city_indicators", primaryKeys = {"search_id", "id"})
public class LDBCityIndicator {

    /**
     * Primary keys
     */
    @NonNull
    @ColumnInfo(name = "search_id")
    private String mLDBSearchId = "";

    @NonNull
    @ColumnInfo(name = "id")
    private String mId = "";

    /**
     * Other fields
     */
    @ColumnInfo(name = "name")
    private String mName = "";

    @ColumnInfo(name = "value")
    private double mValue = -1;

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

    public double getValue() {
        return mValue;
    }

    public void setValue(double value) {
        mValue = value;
    }

    public void setValue(String value) {
        try {
            StringUtils stringUtils = new StringUtils();
            mValue = stringUtils.getDoubleValueFromString(value);

        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
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