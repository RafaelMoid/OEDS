package br.com.eadfiocruzpe.Contracts;

import java.util.ArrayList;
import java.util.HashMap;

import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBSearch;

public interface SearchDialogContract {

    interface View extends BaseContract.View {

        void loadListUfs();

        void loadListCitiesSelectedUf();

        void loadListYears();

        void loadListTimeRangesSelectedYear();

        void loadCurrentSearch(LDBSearch currentSearch);

        void informAdvancedSearchFailed();

        void finishSearchSuccessfully(LDBSearch search);

        void showMessage(boolean show, String message);

        void showProgressOnDialog(boolean show);

    }

    interface Presenter {

        void initListsWithDefaultValues();

        void startAutomaticSearch();

        void loadListUfs();

        void loadListUfCities(String selectedUfCode);

        void loadListYears();

        void loadTimeRangesForYear(String selectedYear);

        void loadCurrentSearch(LDBSearch search);

        LDBSearch getSelectedSearch();

        ArrayList<String> getListYears();

        ArrayList<String> getListUfs();

        ArrayList<String> getListCities();

        ArrayList<String> getListTimeRanges();

        HashMap<String, String> getMapUfsCodes();

        HashMap<String, String> getMapCitiesCodes();

        void setSelectedUfCode(String selectedUfCode);

        String getSelectedUfCode();

        void setSelectedYear(String selectedYear);

        int getSelectedYear();

        HashMap<String, String> getMapTimeRangesCodes();

        void saveValidSearch(LDBSearch search);

    }

}