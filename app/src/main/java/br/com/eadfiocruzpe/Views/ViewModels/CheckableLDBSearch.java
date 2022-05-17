package br.com.eadfiocruzpe.Views.ViewModels;

import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBSearch;

public class CheckableLDBSearch extends CheckableItem {

    private LDBSearch mSearch;

    public void setSearch(LDBSearch search) {
        mSearch = search;
    }

    public LDBSearch getSearch() {
        return mSearch;
    }

}
