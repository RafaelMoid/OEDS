package br.com.eadfiocruzpe.Views.Adapters;

import java.util.ArrayList;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Contracts.SearchSuggestionsAdapterContract;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.GooglePlacesCity;
import br.com.eadfiocruzpe.Utils.ConstantUtils;
import br.com.eadfiocruzpe.Views.Validators.BasicValidators;
import br.com.eadfiocruzpe.Views.ViewHolders.SearchSuggestionViewHolder;

public class SearchSuggestionsAdapter extends RecyclerView.Adapter<SearchSuggestionViewHolder> {

    private SearchSuggestionsAdapterContract.Client mCallback;
    private Resources mResources;
    private ArrayList<GooglePlacesCity> mSearchSuggestions;
    private BasicValidators mValidationUtils;

    public SearchSuggestionsAdapter(SearchSuggestionsAdapterContract.Client client,
                                    ArrayList<GooglePlacesCity> suggestions, Resources resources) {
        mCallback = client;
        mSearchSuggestions = suggestions;
        mResources = resources;
        mValidationUtils = new BasicValidators();
    }

    @Override
    public SearchSuggestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View searchSuggestionView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.component_list_item_search_suggestion, parent, false);

        return new SearchSuggestionViewHolder(searchSuggestionView);
    }

    @Override
    public void onBindViewHolder(final SearchSuggestionViewHolder holder, final int position) {
        final GooglePlacesCity searchSuggestion = mSearchSuggestions.get(position);

        try {

            if (mValidationUtils.isValidSearchSuggestion(searchSuggestion)) {
                holder.containerSearchSuggestion.setVisibility(View.VISIBLE);

                // Init values
                holder.lblLocationName.setText(mResources.getString(
                        R.string.component_auto_complete_search_suggestion_format,
                        searchSuggestion.getCity(),
                        searchSuggestion.getUf().toUpperCase()));

                // Events
                holder.lblLocationName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCallback.onClickSelectedSuggestion(searchSuggestion);
                    }
                });

                holder.containerSearchSuggestion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCallback.onClickSelectedSuggestion(searchSuggestion);
                    }
                });

            } else {
                holder.containerSearchSuggestion.setVisibility(View.GONE);
            }
        } catch (NullPointerException ignored) {
            holder.containerSearchSuggestion.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mSearchSuggestions.size();
    }

    public void updateList(GooglePlacesCity suggestedCity) {

        if (!itemAlreadyInList(suggestedCity)) {
            mSearchSuggestions.add(0, suggestedCity);
            notifyItemChanged(0);

            if (mSearchSuggestions.size() > ConstantUtils.MAX_NUM_SEARCH_SUGGESTIONS_SHOWING) {
                mSearchSuggestions.remove(mSearchSuggestions.size() - 1);
            }
        }

        notifyDataSetChanged();
    }

    private boolean itemAlreadyInList(GooglePlacesCity suggestedCity) {
        boolean alreadyExist = false;

        for (GooglePlacesCity city : mSearchSuggestions) {

            if (city.getCity().equals(suggestedCity.getCity()) &&
                    city.getUf().equals(suggestedCity.getUf())) {
                alreadyExist = true;
                break;
            }
        }

        return alreadyExist;
    }

    public void cleanSuggestions() {

        for (int i = 0; i < mSearchSuggestions.size(); i++) {
            mSearchSuggestions.remove(0);
        }

        notifyDataSetChanged();
    }

}