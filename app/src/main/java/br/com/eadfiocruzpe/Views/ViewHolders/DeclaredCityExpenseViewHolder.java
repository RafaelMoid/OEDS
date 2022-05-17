package br.com.eadfiocruzpe.Views.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Utils.UIUtils;

public class DeclaredCityExpenseViewHolder extends RecyclerView.ViewHolder {

    public String dceId;
    public LinearLayout dceContainer;
    public TextView dceName;
    public TextView dceValue;
    public TextView dceDescription;

    private UIUtils mUIUtils = new UIUtils();

    public DeclaredCityExpenseViewHolder(View view) {
        super(view);

        initViews(view);
        initEvents();
    }

    private void initViews(View view) {
        dceContainer = view.findViewById(R.id.component_list_declared_city_expense);
        dceName = view.findViewById(R.id.component_list_declared_city_expense_name);
        dceValue = view.findViewById(R.id.component_list_declared_city_expense_value);
        dceDescription = view.findViewById(R.id.component_list_declared_city_expense_description);
    }

    private void initEvents() {
        dceContainer.setOnClickListener(mClickListenerToggleDescription);
        dceName.setOnClickListener(mClickListenerToggleDescription);
        dceValue.setOnClickListener(mClickListenerToggleDescription);
        dceDescription.setOnClickListener(mClickListenerToggleDescription);
    }

    private final View.OnClickListener mClickListenerToggleDescription = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mUIUtils.toggleDescriptionVisibility(dceName, dceDescription);
        }
    };

}