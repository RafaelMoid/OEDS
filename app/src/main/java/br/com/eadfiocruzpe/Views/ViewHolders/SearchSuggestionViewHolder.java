package br.com.eadfiocruzpe.Views.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.eadfiocruzpe.R;

public class SearchSuggestionViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout containerSearchSuggestion;
    public TextView lblLocationName;

    public SearchSuggestionViewHolder(View view) {
        super(view);

        containerSearchSuggestion = view.findViewById(R.id.component_list_search_suggestion);
        lblLocationName = view.findViewById(R.id.component_list_search_suggestion_location_name);
    }

}