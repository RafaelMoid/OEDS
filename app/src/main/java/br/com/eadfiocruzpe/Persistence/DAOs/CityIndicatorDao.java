package br.com.eadfiocruzpe.Persistence.DAOs;

import java.util.List;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBCityIndicator;

@Dao
public interface CityIndicatorDao {

    @Query("SELECT * FROM city_indicators WHERE " +
            "search_id = :searchId ")
    List<LDBCityIndicator> findAll(String searchId);

    @Query("SELECT * FROM city_indicators WHERE " +
           "search_id = :searchId AND " +
           "id = :id " +
           "LIMIT 1")
    LDBCityIndicator find(String searchId, String id);

    @Insert
    void insert(LDBCityIndicator indicator);

    @Update
    void update(LDBCityIndicator indicator);

    @Delete
    void delete(LDBCityIndicator indicator);

}