package br.com.eadfiocruzpe.Contracts;

import com.google.android.gms.location.places.GeoDataClient;

import br.com.eadfiocruzpe.Models.BusinessLogicModels.GooglePlacesCity;

public interface ComponentAutoCompleteSearchPresenterContract {

    GeoDataClient getGeoDataApiClient();

    void updateListSuggestions(GooglePlacesCity suggestedCity);

    void userAnotherSearchOption();

}