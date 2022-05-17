package br.com.eadfiocruzpe.Contracts;

import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBSearch;

public interface SearchResultsPresenterContract {

    interface Client {

        void loadReports(LDBSearch search);

    }

    interface Presenter {

        void save();

        void loadCityIndicatorsFromCache(final String searchId,
                                         final DBQueryContract loadingCallback);

        void loadRevenueDeclaredExpensesFromCache(final String searchId,
                                                  final DBQueryContract loadingCallback);

    }

}