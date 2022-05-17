package br.com.eadfiocruzpe.Persistence.DAOs;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBExpense;

@Dao
public interface ExpenseDao {

    @Query("SELECT * FROM city_expenses WHERE " +
            "search_id = :searchId ")
    List<LDBExpense> findAll(String searchId);

    @Query("SELECT * FROM city_expenses WHERE " +
           "search_id = :searchId AND " +
           "title = :title " +
           "LIMIT 1")
    LDBExpense find(String searchId, String title);

    @Insert
    void insert(LDBExpense expense);

    @Update
    void update(LDBExpense expense);

    @Delete
    void delete(LDBExpense expense);

}