package br.com.eadfiocruzpe.Views.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import br.com.eadfiocruzpe.R;

public class ShareableItemViewHolder extends RecyclerView.ViewHolder {

    public View container;
    public ImageView img;
    public View btnDelete;

    public ShareableItemViewHolder(View view) {
        super(view);

        initViews(view);
    }

    private void initViews(View view) {
        container = view.findViewById(R.id.component_list_shareable_item_container);
        img = view.findViewById(R.id.component_list_shareable_item_img);
        btnDelete = view.findViewById(R.id.component_list_shareable_item_btn_delete);
    }

}