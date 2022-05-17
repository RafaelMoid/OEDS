package br.com.eadfiocruzpe.Views.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.eadfiocruzpe.R;

public class SearchHistoryViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout containerSearch;
    public TextView lblCityName;
    public TextView lblStateName;
    public TextView lblSearchedPeriod;
    public View btnToggleFavorite;
    public View btnDelete;

    public SearchHistoryViewHolder(View view) {
        super(view);

        containerSearch = view.findViewById(R.id.component_list_search_history);
        lblCityName = view.findViewById(R.id.component_list_search_history_city_name);
        lblStateName = view.findViewById(R.id.component_list_search_history_state_name);
        lblSearchedPeriod = view.findViewById(R.id.component_list_search_history_searched_period);
        btnToggleFavorite = view.findViewById(R.id.component_list_search_history_btn_toggle_favorite);
        btnDelete = view.findViewById(R.id.component_list_search_history_btn_delete_interest);
    }

}