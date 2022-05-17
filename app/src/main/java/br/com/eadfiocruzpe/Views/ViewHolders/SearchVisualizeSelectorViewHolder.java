package br.com.eadfiocruzpe.Views.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.eadfiocruzpe.R;

public class SearchVisualizeSelectorViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout containerSearch;
    public CheckBox checkBoxSelector;
    public ImageView imgStateFlag;
    public TextView lblCityName;
    public TextView lblStateName;
    public TextView lblSearchedPeriod;
    public View btnDelete;
    public View btnLblDelete;

    public SearchVisualizeSelectorViewHolder(View view) {
        super(view);

        containerSearch = view.findViewById(R.id.component_list_item_svs_searches);
        checkBoxSelector = view.findViewById(R.id.component_list_item_svs_btn_checkbox);
        imgStateFlag = view.findViewById(R.id.component_list_item_svs_state_flag);
        lblCityName = view.findViewById(R.id.component_list_item_svs_city_name);
        lblStateName = view.findViewById(R.id.component_list_item_svs_state_name);
        lblSearchedPeriod = view.findViewById(R.id.component_list_item_svs_searched_period);
        btnDelete = view.findViewById(R.id.component_list_item_svs_btn_delete);
        btnLblDelete = view.findViewById(R.id.component_list_item_svs_lbl_btn_delete);

        showCheckbox(false);
    }

    public void showCheckbox(boolean show) {
        checkBoxSelector.setVisibility(show? View.VISIBLE : View.GONE);
    }

}