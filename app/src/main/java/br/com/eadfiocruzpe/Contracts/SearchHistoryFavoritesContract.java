package br.com.eadfiocruzpe.Contracts;

import java.util.ArrayList;

import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBSearch;

public interface SearchHistoryFavoritesContract {

    interface View extends BaseContract.View {

        void onLoadSearchHistory(ArrayList<LDBSearch> searchHistory);

        void onLoadFavoriteSearches(ArrayList<LDBSearch> favoriteSearches);

    }

    interface Presenter {

        void loadSearches(int selectedList);

        void updateSearch(final LDBSearch search);

        void deleteSearch(final LDBSearch search);

    }

}