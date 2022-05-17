package br.com.eadfiocruzpe.Contracts;

import br.com.eadfiocruzpe.Models.BusinessLogicModels.GooglePlacesCity;

public interface ComponentAutoCompleteSearchContract {

    void onAdvancedSearchBtnClicked();

    void lockUI();

    void setSearchWithSuggestion(GooglePlacesCity suggestedCity);

}