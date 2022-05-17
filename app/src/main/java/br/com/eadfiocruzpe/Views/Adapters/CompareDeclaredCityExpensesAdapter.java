package br.com.eadfiocruzpe.Views.Adapters;

import android.content.res.Resources;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Utils.ConstantUtils;
import br.com.eadfiocruzpe.Utils.StringUtils;
import br.com.eadfiocruzpe.Views.Dialogs.FAQDialog;
import br.com.eadfiocruzpe.Views.ViewHolders.CompareDeclaredCityExpensesViewHolder;
import br.com.eadfiocruzpe.Views.ViewModels.ExpensesComparison;

public class CompareDeclaredCityExpensesAdapter extends RecyclerView.Adapter<CompareDeclaredCityExpensesViewHolder> {

    private ArrayList<ExpensesComparison> mExpenses;
    private StringUtils mStrUtils;
    private Resources mResources;

    public CompareDeclaredCityExpensesAdapter(ArrayList<ExpensesComparison> expenses, Resources resources) {
        mExpenses = expenses;
        mResources = resources;
        mStrUtils = new StringUtils(resources);
    }

    @Override
    public CompareDeclaredCityExpensesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View singleInterestView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.component_list_compare_declared_city_expenses, parent, false);

        return new CompareDeclaredCityExpensesViewHolder(singleInterestView);
    }

    @Override
    public void onBindViewHolder(final CompareDeclaredCityExpensesViewHolder holder, final int position) {
        ExpensesComparison expComp = mExpenses.get(position);

        try {

            if (position % 2 == 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.dceContainer.setBackground(mResources.getDrawable(R.drawable.background_light_green));

            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                holder.dceContainer.setBackground(mResources.getDrawable(R.drawable.background_dark_white));
            }

            holder.dceId = expComp.getExpenseA().getExpenseId() + expComp.getExpenseB().getExpenseId();
            holder.dceName.setText(expComp.getExpenseA().getTitle());

            if (!expComp.getExpenseA().getCategory().equals(ConstantUtils.DEFAULT_VALUE_ZERO_LIST)) {
                holder.dceValueA.setText(mStrUtils.getPercentageStringFromVal(expComp.getExpenseA().getPercentageValue()));
            } else {
                holder.dceValueA.setText(mResources.getString(R.string.lbl_non_informed));
            }

            if (!expComp.getExpenseB().getCategory().equals(ConstantUtils.DEFAULT_VALUE_ZERO_LIST)) {
                holder.dceValueB.setText(mStrUtils.getPercentageStringFromVal(expComp.getExpenseB().getPercentageValue()));
            } else {
                holder.dceValueB.setText(mResources.getString(R.string.lbl_non_informed));
            }

            String description = FAQDialog.expenseNameDescription.get(expComp.getExpenseA().getTitle());

            if (!description.isEmpty()) {
                holder.dceDescription.setText(description);
            } else {
                holder.dceDescription.setText("");
                holder.dceDescription.setVisibility(View.GONE);
            }

        } catch (NullPointerException e) {
            e.getMessage();
        }
    }

    @Override
    public int getItemCount() {
        return mExpenses.size();
    }

    public void updateList(ArrayList<ExpensesComparison> expenses) {
        mExpenses = expenses;
        notifyDataSetChanged();
    }

}