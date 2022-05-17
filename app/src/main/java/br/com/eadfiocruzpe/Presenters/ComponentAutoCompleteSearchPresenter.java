package br.com.eadfiocruzpe.Presenters;

import android.support.annotation.NonNull;

import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBufferResponse;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import br.com.eadfiocruzpe.Contracts.ComponentAutoCompleteSearchPresenterContract;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.GooglePlacesCity;
import br.com.eadfiocruzpe.Utils.LogUtils;
import br.com.eadfiocruzpe.Utils.StringUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;

public class ComponentAutoCompleteSearchPresenter {

    private static final String TAG = "CompACSearchPresenter";

    private ComponentAutoCompleteSearchPresenterContract mCallback;
    private AutocompleteFilter mAutocompleteFilter;
    private LogUtils mLogUtils;

    public ComponentAutoCompleteSearchPresenter(ComponentAutoCompleteSearchPresenterContract callback) {
        mCallback = callback;
        mLogUtils = new LogUtils();
        mAutocompleteFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(Place.TYPE_SUBLOCALITY_LEVEL_2)
                .setCountry("BR")
                .build();
    }

    public void getAutoCompletePredictions(String searchTerms) {
        try {
            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Updated search terms " + searchTerms);

            Task<AutocompletePredictionBufferResponse> results =
                    mCallback.getGeoDataApiClient().getAutocompletePredictions(searchTerms, null,
                            mAutocompleteFilter);

            results.addOnSuccessListener(new OnSuccessListener<AutocompletePredictionBufferResponse>() {

                @Override
                public void onSuccess(AutocompletePredictionBufferResponse autocompletePredictions) {

                    if (autocompletePredictions != null) {

                        for (int i = 0; i < autocompletePredictions.getCount(); i++) {
                            AutocompletePrediction placePrediction = autocompletePredictions.get(i);
                            GooglePlacesCity gPlacesCity = filterGooglePlacesPrediction(placePrediction);

                            if (gPlacesCity != null && mCallback != null) {
                                mCallback.updateListSuggestions(gPlacesCity);
                            }
                        }

                        autocompletePredictions.release();
                    }
                }
            });

            results.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    if (mCallback != null) {
                        mCallback.userAnotherSearchOption();
                    }
                }

            });

        } catch (Exception e) {

            if (mCallback != null) {
                mCallback.userAnotherSearchOption();
            }

            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to getAutoCompletePredictions " + e.getMessage());
        }
    }

    /**
     * Fetch only predictions that return a city name and a valid state ID
     */
    private GooglePlacesCity filterGooglePlacesPrediction(AutocompletePrediction placePrediction) {
        StringUtils strUtils = new StringUtils();
        GooglePlacesCity gPlacesCity = null;

        try {
            String city = "";
            String uf = "";
            String googlePlaceId = "";

            // Filter 1: Avoid null values
            if (placePrediction.getSecondaryText(null) !=  null) {

                // Filter 2: Verify if all the important attributes are present
                String[] partsFilter2 = placePrediction.getSecondaryText(null).toString().split(",");

                if (partsFilter2.length > 1) {
                    String state = partsFilter2[0];
                    state = state.replace(" ","");
                    state = state.toLowerCase();
                    String stateAcronym = strUtils.getUfAcronym(state);

                    if (stateAcronym != null) {

                        if (!stateAcronym.isEmpty()) {
                            googlePlaceId = placePrediction.getPlaceId();
                            city = placePrediction.getPrimaryText(null).toString();
                            uf = stateAcronym;
                        }
                    }
                }

                // Filter 3: Verify if the UF id is valid
                if (googlePlaceId != null) {

                    if (!googlePlaceId.isEmpty() && !city.isEmpty() && !uf.isEmpty()) {
                        gPlacesCity = new GooglePlacesCity();
                        gPlacesCity.setGooglePlacesId(googlePlaceId);
                        gPlacesCity.setCity(city);
                        gPlacesCity.setUf(uf);
                    }
                }
            }

        } catch (Exception e) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to filterGooglePlacesPredictions " + e.getMessage());
        }

        return gPlacesCity;
    }

}