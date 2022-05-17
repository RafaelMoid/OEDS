package br.com.eadfiocruzpe.Views.Adapters;

import java.util.ArrayList;

import android.content.res.Resources;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBExpense;
import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Utils.StringUtils;
import br.com.eadfiocruzpe.Views.Dialogs.FAQDialog;
import br.com.eadfiocruzpe.Views.ViewHolders.DeclaredCityExpenseViewHolder;

public class DeclaredCityExpensesAdapter extends RecyclerView.Adapter<DeclaredCityExpenseViewHolder> {

    private ArrayList<LDBExpense> mExpenses;
    private StringUtils mStrUtils;
    private Resources mResources;

    public DeclaredCityExpensesAdapter(ArrayList<LDBExpense> expenses, Resources resources) {
        mExpenses = expenses;
        mResources = resources;
        mStrUtils = new StringUtils(resources);
    }

    @Override
    public DeclaredCityExpenseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View singleInterestView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.component_list_item_declared_city_expense, parent, false);

        return new DeclaredCityExpenseViewHolder(singleInterestView);
    }

    @Override
    public void onBindViewHolder(final DeclaredCityExpenseViewHolder holder, final int position) {
        LDBExpense expense = mExpenses.get(position);

        try {

            if (position % 2 == 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.dceContainer.setBackground(mResources.getDrawable(R.drawable.background_light_green));

            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                holder.dceContainer.setBackground(mResources.getDrawable(R.drawable.background_dark_white));
            }

            holder.dceId = expense.getExpenseId();
            holder.dceName.setText(expense.getTitle());
            holder.dceValue.setText(mStrUtils.getPercentageStringFromVal(expense.getPercentageValue()));

            String description = FAQDialog.expenseNameDescription.get(expense.getTitle());

            if (description != null) {

                if (!description.isEmpty()) {
                    holder.dceDescription.setText(description);
                } else {
                    holder.dceDescription.setText("");
                    holder.dceDescription.setVisibility(View.GONE);
                }
            }

        } catch (NullPointerException e) {
            e.getMessage();
        }
    }

    @Override
    public int getItemCount() {
        return mExpenses.size();
    }

    public void updateList(ArrayList<LDBExpense> expenses) {
        mExpenses = expenses;
        notifyDataSetChanged();
    }

}