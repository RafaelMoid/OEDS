package br.com.eadfiocruzpe.Persistence.DAOs;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBInvestmentsPHBySource;

@Dao
public interface GovInvestmentPHBySourceDao {

    @Query("SELECT * FROM investments_ph_by_source WHERE " +
           "search_id = :searchId " +
           "LIMIT 1")
    LDBInvestmentsPHBySource find(String searchId);

    @Insert
    void insert(LDBInvestmentsPHBySource govInvestmentBySource);

    @Update
    void update(LDBInvestmentsPHBySource govInvestmentBySource);

    @Delete
    void delete(LDBInvestmentsPHBySource govInvestmentBySource);

}