package br.com.eadfiocruzpe.Persistence.DAOs;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBCityRankingObject;

@Dao
public interface CityRankingObjectDao {

    @Query("SELECT * FROM city_ranking_object WHERE " +
            "search_id = :searchId ")
    List<LDBCityRankingObject> findAll(String searchId);

    @Query("SELECT * FROM city_ranking_object WHERE " +
           "search_id = :searchId AND " +
           "city_name = :cityName AND " +
           "city_state = :cityState " +
           "LIMIT 1")
    LDBCityRankingObject find(String searchId, String cityName, String cityState);

    @Insert
    void insert(LDBCityRankingObject cityRankingObject);

    @Update
    void update(LDBCityRankingObject cityRankingObject);

    @Delete
    void delete(LDBCityRankingObject cityRankingObject);

}