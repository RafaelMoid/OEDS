package br.com.eadfiocruzpe.Views.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Utils.UIUtils;

public class CompareDeclaredCityExpensesViewHolder extends RecyclerView.ViewHolder {

    public String dceId;
    public LinearLayout dceContainer;
    public TextView dceName;
    public TextView dceValueA;
    public TextView dceValueB;
    public TextView dceDescription;

    private UIUtils mUIUtils = new UIUtils();

    public CompareDeclaredCityExpensesViewHolder(View view) {
        super(view);

        initViews(view);
        initEvents();
    }

    private void initViews(View view) {
        dceContainer = view.findViewById(R.id.component_list_compare_declared_city_expenses);
        dceName = view.findViewById(R.id.component_list_compare_declared_city_expenses_name);
        dceValueA = view.findViewById(R.id.component_list_compare_declared_city_expenses_a);
        dceValueB = view.findViewById(R.id.component_list_compare_declared_city_expenses_b);
        dceDescription = view.findViewById(R.id.component_list_compare_declared_city_expenses_description);
    }

    private void initEvents() {
        dceContainer.setOnClickListener(mClickListenerToggleDescription);
        dceName.setOnClickListener(mClickListenerToggleDescription);
        dceValueA.setOnClickListener(mClickListenerToggleDescription);
        dceValueB.setOnClickListener(mClickListenerToggleDescription);
        dceDescription.setOnClickListener(mClickListenerToggleDescription);
    }

    private final View.OnClickListener mClickListenerToggleDescription = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mUIUtils.toggleDescriptionVisibility(dceName, dceDescription);
        }
    };

}