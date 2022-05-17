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

@Entity(tableName = "city_ranking_object", primaryKeys = {"search_id", "city_name", "city_state"})
public class LDBCityRankingObject {

    /**
     * Primary keys
     */
    @NonNull
    @ColumnInfo(name = "search_id")
    private String mLDBSearchId = "";

    @NonNull
    @ColumnInfo(name = "city_name")
    @SerializedName("city_name")
    private String mCityName = "";

    @NonNull
    @ColumnInfo(name = "city_state")
    @SerializedName("city_state")
    private String mCityState = "";

    /**
     * Other fields
     */
    @ColumnInfo(name = "location")
    @SerializedName("location")
    private String mLocation;

    @ColumnInfo(name = "ranking_value")
    @SerializedName("autonomy_level")
    private float mRankingValue;

    @ColumnInfo(name = "ranking_position")
    @SerializedName("ranking_position")
    private int mRankingPosition;

    @ColumnInfo(name = "created_at")
    private String mCreatedAt;

    @ColumnInfo(name = "updated_at")
    private String mUpdatedAt;

    @ColumnInfo(name = "avg_value_full_ranking")
    private float mAvgValueFullRanking;

    private int mRankingColor;

    private String mChartFlag;


    public String getLDBSearchId() {
        return mLDBSearchId;
    }

    public void setLDBSearchId(String ldbSearchId) {
        mLDBSearchId = ldbSearchId;
    }

    public String getCityName() {
        return mCityName;
    }

    public void setCityName(String cityName) {
        mCityName = cityName;
    }

    public String getCityState() {
        return mCityState;
    }

    public void setCityState(String cityState) {
        mCityState = cityState;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public float getRankingValue() {
        return mRankingValue;
    }

    public void setRankingValue(float rankingValue) {
        mRankingValue = rankingValue;
    }

    public int getRankingPosition() {
        return mRankingPosition;
    }

    public void setRankingPosition(int position) {
        mRankingPosition = position;
    }

    public void setRankingColor(int color) {
        mRankingColor = color;
    }

    public int getRankingColor() {
        return mRankingColor;
    }

    public void setChartFlag(String flag) {
        mChartFlag = flag;
    }

    public String getChartFlag() {
        return mChartFlag;
    }

    public float getAvgValueFullRanking() {
        return mAvgValueFullRanking;
    }

    public void setAvgValueFullRanking(float avgValueFullRanking) {
        mAvgValueFullRanking = avgValueFullRanking;
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

            if (!getCityName().isEmpty()) {
                String rankingPosition = "<b>" + String.valueOf(getRankingPosition()) + "º</b>\n";
                rankingPosition += getCityName() + "\n";
                rankingPosition += getCityState();

                return rankingPosition;
            } else {
                return "";
            }

        } catch (NullPointerException npe) {
            npe.printStackTrace();
            return "";
        }
    }

}