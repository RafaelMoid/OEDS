package br.com.eadfiocruzpe.Contracts;

import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBSearch;

public interface NationalRankingResultsPresenterContract {

    interface Client {

        void loadReports(LDBSearch search);

    }

    interface Presenter {

        void save();

        void loadRankingCitiesPHCache(final String searchId, final DBQueryContract loadingCallback);

    }

}