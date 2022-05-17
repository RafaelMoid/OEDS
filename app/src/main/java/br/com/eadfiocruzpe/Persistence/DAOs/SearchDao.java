package br.com.eadfiocruzpe.Persistence.DAOs;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBSearch;

@Dao
public interface SearchDao {

    @Query("SELECT * FROM searches")
    List<LDBSearch> getAll();

    @Query("SELECT * FROM searches WHERE favorite = 1")
    List<LDBSearch> getFavorite();

    @Query("SELECT * FROM searches WHERE " +
           "state = :state AND " +
           "city = :city AND "+
           "year = :year AND "+
           "period_year = :periodYear "+
           "LIMIT 1")
    LDBSearch find(String state, String city, String year, String periodYear);

    @Query("SELECT count(*) FROM searches")
    int getNumStoredSearches();

    @Insert
    void insert(LDBSearch search);

    @Update
    void update(LDBSearch search);

    @Insert
    void insertAll(LDBSearch... searches);

    @Delete
    void delete(LDBSearch search);

}