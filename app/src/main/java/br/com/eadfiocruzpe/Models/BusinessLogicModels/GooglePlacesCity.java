package br.com.eadfiocruzpe.Models.BusinessLogicModels;

public class GooglePlacesCity {

    private String mGooglePlacesId;
    private String mUf;
    private String mCity;


    private String getGooglePlacesId() {
        return mGooglePlacesId;
    }

    public void setGooglePlacesId(String googlePlacesId) {
        mGooglePlacesId = googlePlacesId;
    }

    public String getUf() {
        return mUf;
    }

    public void setUf(String uf) {
        mUf = uf;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public String toString() {
        try {
            return getGooglePlacesId() + " : " + getCity() + " - " + getUf();
        } catch (NullPointerException ignored) {
            return "";
        }
    }

}