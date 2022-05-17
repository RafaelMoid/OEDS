package br.com.eadfiocruzpe.Models.DataResponses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import br.com.eadfiocruzpe.Models.BusinessLogicModels.City;
import br.com.eadfiocruzpe.Utils.ConstantUtils;
import br.com.eadfiocruzpe.Utils.LogUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;
import br.com.eadfiocruzpe.Views.Validators.BasicValidators;

public class ListAvailableCitiesUfResponseAPI {

    private final String TAG = "ListAvaCitiesUfRespAPI";

    @SerializedName("list_cities_ufs")
    private ArrayList<City> mAvailableCitiesUf = new ArrayList<>();
    private HashMap<String, String> mMapAvailableCities = new HashMap<>();
    private LogUtils mLogUtils;

    /**
     * Initialization
     */
    public ListAvailableCitiesUfResponseAPI() {
        mLogUtils = new LogUtils();
    }

    public void buildMapAvailableCities() {
        try {

            if (!this.getAvailableCities().isEmpty()) {
                BasicValidators validationUtils = new BasicValidators();
                ArrayList<String> listAvailableCities = new ArrayList<>();
                ArrayList<String> listAvailableCitiesCodes = new ArrayList<>();

                for (City city: getAvailableCities()) {

                    if (city != null) {

                        if (validationUtils.isValidString(city.getFullName()) &&
                                validationUtils.isValidString(String.valueOf(city.getCityId()))) {

                            listAvailableCities.add(city.getFullName());
                            listAvailableCitiesCodes.add(String.valueOf(city.getCityId()));
                        }
                    }
                }

                if (listAvailableCities.size() > 0 &&
                        listAvailableCities.size() == listAvailableCitiesCodes.size()) {

                    // Build a Map between the UFs and their SIOPS's codes
                    mMapAvailableCities.clear();

                    for (int i = 0; i < listAvailableCities.size(); i++) {
                        mMapAvailableCities.put(listAvailableCities.get(i), listAvailableCitiesCodes.get(i));
                    }
                } else {
                    mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                            TAG + " Failed to buildMapAvailableCities");
                }
            }
        } catch (NullPointerException npe) {
            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to buildMapAvailableCities");
        }
    }

    /**
     * Getters and Setters
     */
    private ArrayList<City> getAvailableCities() {
        return mAvailableCitiesUf;
    }

    public HashMap<String, String> getMapAvailableCities() {
        return mMapAvailableCities;
    }

    public ArrayList<String> getListAvailableCities() {
        ArrayList<String> listCities = new ArrayList<>();

        try {
            listCities = new ArrayList<>(mMapAvailableCities.keySet());

            Collections.sort(listCities, new Comparator<String>() {
                @Override
                public int compare(String s1, String s2) {
                    return s1.compareToIgnoreCase(s2);
                }
            });

            listCities.add(0, ConstantUtils.DEFAULT_VALUE_SEARCH_DIALOG_CITY);

        } catch (Exception e) {
            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to getListAvailableCities");
        }

        return listCities;
    }

}