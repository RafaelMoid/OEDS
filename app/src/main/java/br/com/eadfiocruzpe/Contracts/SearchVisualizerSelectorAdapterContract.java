package br.com.eadfiocruzpe.Contracts;

import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBSearch;
import br.com.eadfiocruzpe.Views.ViewModels.CheckableLDBSearch;

public interface SearchVisualizerSelectorAdapterContract {

    interface Client {

        void onSearchItemRemoved(LDBSearch search);

        void onSearchItemDetails(LDBSearch search);

        void onCheckableLDBSearchDetails(CheckableLDBSearch search);

        void onSearchChecked(LDBSearch search, boolean isChecked);

    }

}