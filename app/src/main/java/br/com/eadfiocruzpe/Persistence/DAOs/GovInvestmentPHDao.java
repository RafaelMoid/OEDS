package br.com.eadfiocruzpe.Persistence.DAOs;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBGovInvestmentPH;

@Dao
public interface GovInvestmentPHDao {

    @Query("SELECT * FROM gov_investments_ph WHERE " +
            "search_id = :searchId ")
    List<LDBGovInvestmentPH> findAll(String searchId);

    @Query("SELECT * FROM gov_investments_ph WHERE " +
           "search_id = :searchId AND " +
           "id = :id AND " +
           "name = :name " +
           "LIMIT 1")
    LDBGovInvestmentPH find(String searchId, String id, String name);

    @Insert
    void insert(LDBGovInvestmentPH govInvestment);

    @Update
    void update(LDBGovInvestmentPH govInvestment);

    @Delete
    void delete(LDBGovInvestmentPH govInvestment);

}