package br.com.eadfiocruzpe.Contracts;

import java.util.ArrayList;

import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBCityRankingObject;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBExpense;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBSearch;

public interface DashboardContract {

    interface View extends BaseContract.View {

        void initCuriosityAboutInvestmentsPH();

        void initValuePHCitizenDay();

        void initCityInvestmentPHCitizenYear();

        void initInvestmentOwnCityValueCitizenYear();

        void initInvestmentOtherSourcesValueCitizenYear();

        void initRankingCitiesFinancialAutonomy();

        void initTotalInvestmentOnCityPH();

        void initCityFinancialAutonomy();

        void initValueInvestedFederalGovCity();

        void initValueInvestedStateGovCity();

        void initValueInvestedCityGovOwnPH();

        void initPHExpensesDeclaredCity();

        void loadValuePHCitizenDay(double valueCitizenPerDay);

        void loadCityInvestmentsPHCitizenYear(double valueCitizenPerYear);

        void loadInvestmentOwnCityValueCitizenYear(double valueCitizenPerYear);

        void loadInvestmentOtherSourcesValueCitizenYear(double valueCitizenPerYear);

        void loadRankingCities(float nationalAvgFinancialAutonomyHealthInvestments,
                                                final ArrayList<LDBCityRankingObject> rankingData);

        void loadChartTotalInvestmentsCityPH(ArrayList<Double> dataSet,
                                             int [] colors,
                                             ArrayList<String> labels,
                                             String chartDescription);

        void loadCityFinancialAutonomy(double cityFinancialAutonomy);

        void loadValueInvestedFederalGovCity(double federalInvestment);

        void loadValueInvestedStateGovCity(double stateInvestment);

        void loadValueInvestedOtherSourcesPH(double cityInvestment);

        void loadPHExpensesDeclaredCity(ArrayList<LDBExpense> declaredCityExpenses);

        void setSearch(LDBSearch search);

        LDBSearch getSearch();

        void setIsUpdatingUIAfterSearch(boolean isUpdatingUI);

        boolean isUpdatingUIAfterSearch();

    }

    interface Presenter {

        void loadReports(LDBSearch search);

    }

}