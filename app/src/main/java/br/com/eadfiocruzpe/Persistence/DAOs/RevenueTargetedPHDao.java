package br.com.eadfiocruzpe.Persistence.DAOs;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBRevenueTargetedPH;

@Dao
public interface RevenueTargetedPHDao {

    @Query("SELECT * FROM revenues_targeted_ph WHERE " +
            "search_id = :searchId ")
    List<LDBRevenueTargetedPH> findAll(String searchId);

    @Query("SELECT * FROM revenues_targeted_ph WHERE " +
           "search_id = :searchId AND " +
           "id = :id AND " +
           "source = :source AND " +
           "name = :name " +
           "LIMIT 1")
    LDBRevenueTargetedPH find(String searchId, String id, String source, String name);

    @Insert
    void insert(LDBRevenueTargetedPH revenue);

    @Update
    void update(LDBRevenueTargetedPH revenue);

    @Delete
    void delete(LDBRevenueTargetedPH revenue);

}