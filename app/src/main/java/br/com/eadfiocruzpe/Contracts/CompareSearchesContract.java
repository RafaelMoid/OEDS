package br.com.eadfiocruzpe.Contracts;

import java.util.ArrayList;

import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBExpense;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBSearch;

public interface CompareSearchesContract {

    interface View extends BaseContract.View {

        void onLoadSearchHistory(ArrayList<LDBSearch> searchHistory);

        void updateSelectedItemsList(ArrayList<LDBSearch> selectedItems);

        void updateConfirmBtn();

        void displayMessage(int messageId);

        void unCheckSearch(LDBSearch search);

        void startSearchesComparison();

        void initLoadValueCitizenDay(LDBSearch searchA, LDBSearch searchB,
                                     double valueA, double valueB);

        void initLoadValueCitizenYear(LDBSearch searchA, LDBSearch searchB,
                                      double valueA, double valueB);

        void initLoadValueInvestedOwnCity(LDBSearch searchA, LDBSearch searchB,
                                          double valueA, double valueB);

        void initLoadValueInvestedOtherSources(LDBSearch searchA, LDBSearch searchB,
                                               double valueA, double valueB);

        void initLoadTotalInvestments(final LDBSearch searchA, final LDBSearch searchB,
                                      final ArrayList<Double> datasetA,
                                      final ArrayList<Double> datasetB,
                                      final ArrayList<String> labelsA,
                                      final ArrayList<String> labelsB);

        void initLoadExpenses(LDBSearch searchA, LDBSearch searchB,
                              ArrayList<LDBExpense> expensesA, ArrayList<LDBExpense> expensesB);

    } interface Presenter {

        void loadSearches();

        void validateSelectedSearches(LDBSearch search, boolean isChecked);

        void removeSelectedSearch(LDBSearch search);

        void loadReportsSelectedSearches(final LDBSearch searchA, final LDBSearch searchB);

        void tryLoadCityIndicatorsCache(final CompareSearchesContract.View view,
                                        final LDBSearch searchA, final LDBSearch searchB);

        void tryLoadInvestmentsExpensesCache(final CompareSearchesContract.View view,
                                             final LDBSearch searchA, final LDBSearch searchB);

        void tryLoadRankingCache(final CompareSearchesContract.View view,
                                 final LDBSearch searchA, final LDBSearch searchB);

    }

}