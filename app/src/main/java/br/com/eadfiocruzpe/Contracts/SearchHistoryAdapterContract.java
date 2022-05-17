package br.com.eadfiocruzpe.Contracts;

import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBSearch;

public interface SearchHistoryAdapterContract {

    interface Client {

        void onSearchHistoryItemDeleted(LDBSearch search);

        void onSearchHistoryItemDetails(LDBSearch search);

        void onClickBtnToggleFavorite(LDBSearch search);

    }

}