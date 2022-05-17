package br.com.eadfiocruzpe.Contracts;

import br.com.eadfiocruzpe.Models.BusinessLogicModels.GooglePlacesCity;

public interface SearchSuggestionsAdapterContract {

    interface Client {

        void onClickSelectedSuggestion(GooglePlacesCity suggestedCity);

    }

}