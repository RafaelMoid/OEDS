package br.com.eadfiocruzpe.Persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBCityRankingObject;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBExpense;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBGovInvestmentPH;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBInvestmentsPHBySource;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBRevenueTargetedPH;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBSearch;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBCityIndicator;
import br.com.eadfiocruzpe.Persistence.DAOs.CityRankingObjectDao;
import br.com.eadfiocruzpe.Persistence.DAOs.CityIndicatorDao;
import br.com.eadfiocruzpe.Persistence.DAOs.ExpenseDao;
import br.com.eadfiocruzpe.Persistence.DAOs.GovInvestmentPHBySourceDao;
import br.com.eadfiocruzpe.Persistence.DAOs.GovInvestmentPHDao;
import br.com.eadfiocruzpe.Persistence.DAOs.RevenueTargetedPHDao;
import br.com.eadfiocruzpe.Persistence.DAOs.SearchDao;

/**
 * ATTENTION
 *
 * This Database is using Room Persistence Library to manage the SQLite database, before you perform
 * any change on the local database, please read the links below:
 * understand how it works:
 *
 *    - Create tables:
 *    https://developer.android.com/topic/libraries/architecture/room
 *
 *    - Update tables, migrate database:
 *    https://medium.com/google-developers/understanding-migrations-with-room-f01e04b07929
 */
@Database(entities = {
            LDBSearch.class,
            LDBCityIndicator.class,
            LDBCityRankingObject.class,
            LDBExpense.class,
            LDBGovInvestmentPH.class,
            LDBInvestmentsPHBySource.class,
            LDBRevenueTargetedPH.class
          }, version = 1)
public abstract class DB extends RoomDatabase {

    public abstract SearchDao searchDao();
    public abstract CityIndicatorDao cityIndicatorDao();
    public abstract CityRankingObjectDao cityRankingObjectDao();
    public abstract ExpenseDao expenseDao();
    public abstract GovInvestmentPHDao govInvestmentPHDao();
    public abstract GovInvestmentPHBySourceDao govInvestmentPHBySourceDao();
    public abstract RevenueTargetedPHDao revenueTargetedPHDao();

    @Override
    public void clearAllTables() {
        // ..
    }

}