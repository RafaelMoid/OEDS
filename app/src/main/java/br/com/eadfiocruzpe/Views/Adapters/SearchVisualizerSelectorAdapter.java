package br.com.eadfiocruzpe.Views.Adapters;

import java.util.ArrayList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Contracts.SearchVisualizerSelectorAdapterContract;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBSearch;
import br.com.eadfiocruzpe.Utils.ImageUtils;
import br.com.eadfiocruzpe.Utils.LogUtils;
import br.com.eadfiocruzpe.Utils.StringUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;
import br.com.eadfiocruzpe.Views.Validators.BasicValidators;
import br.com.eadfiocruzpe.Views.ViewHolders.SearchVisualizeSelectorViewHolder;
import br.com.eadfiocruzpe.Views.ViewModels.CheckableLDBSearch;

public class SearchVisualizerSelectorAdapter extends RecyclerView.Adapter<SearchVisualizeSelectorViewHolder> {

    private final String TAG = "SearchVisSlctAdapter";

    private SearchVisualizerSelectorAdapterContract.Client mCallback;
    private Context mContext;
    private ArrayList<CheckableLDBSearch> mLDBSearches = new ArrayList<>();
    private BasicValidators mValidationUtils;
    private LogUtils mLogUtils;
    private StringUtils mStrUtils;
    private boolean mShowItemSelector;
    private boolean mShowDeleteBtn;

    public SearchVisualizerSelectorAdapter(SearchVisualizerSelectorAdapterContract.Client callback,
                                           Context context,
                                           boolean showItemSelector,
                                           boolean showDeleteBtn,
                                           ArrayList<LDBSearch> searches) {
        mCallback = callback;
        mContext = context;
        mShowItemSelector = showItemSelector;
        mShowDeleteBtn = showDeleteBtn;

        mValidationUtils = new BasicValidators();
        mLogUtils = new LogUtils();
        mStrUtils = new StringUtils();

        loadSearches(searches);
    }

    private void loadSearches(ArrayList<LDBSearch> searches) {
        mLDBSearches.clear();

        for (LDBSearch search : searches) {
            CheckableLDBSearch listItem = new CheckableLDBSearch();
            listItem.setSearch(search);
            mLDBSearches.add(listItem);
        }

        notifyDataSetChanged();
    }

    @Override
    public SearchVisualizeSelectorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View searchFavoriteView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.component_list_item_checkable_search, parent, false);

        return new SearchVisualizeSelectorViewHolder(searchFavoriteView);
    }

    @Override
    public void onBindViewHolder(final SearchVisualizeSelectorViewHolder holder, final int position) {
        final CheckableLDBSearch listItem = mLDBSearches.get(position);

        try {

            if (mValidationUtils.isValidSearch(listItem.getSearch())) {
                holder.containerSearch.setVisibility(View.VISIBLE);

                ImageUtils.loadCircularImage(mContext, ImageUtils.getStateFlag(listItem.getSearch().getState()), holder.imgStateFlag);
                holder.lblCityName.setText(listItem.getSearch().getCity());
                holder.lblStateName.setText(listItem.getSearch().getState());
                holder.lblSearchedPeriod.setText(mStrUtils.getPrettyPeriodYear(listItem.getSearch()));
                holder.showCheckbox(mShowItemSelector);
                holder.btnDelete.setVisibility(mShowDeleteBtn? View.VISIBLE: View.GONE);
                holder.btnLblDelete.setVisibility(mShowDeleteBtn? View.VISIBLE: View.GONE);

                if (mShowItemSelector) {
                    holder.checkBoxSelector.setChecked(listItem.isChecked());
                }

                // Events
                holder.containerSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (mCallback != null) {
                            mCallback.onCheckableLDBSearchDetails(listItem);
                            mCallback.onSearchItemDetails(listItem.getSearch());
                        }
                    }
                });

                holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (mCallback != null) {
                            mCallback.onSearchItemRemoved(listItem.getSearch());
                        }
                    }
                });

                holder.checkBoxSelector.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (mCallback != null && holder.checkBoxSelector != null) {
                            listItem.setIsChecked(!listItem.isChecked());
                            mCallback.onSearchChecked(listItem.getSearch(), holder.checkBoxSelector.isChecked());
                        }
                    }
                });

            } else {
                holder.containerSearch.setVisibility(View.GONE);

                mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                        TAG + "Failed to onBindViewHolder");
            }

        } catch (Exception ignored) {
            holder.containerSearch.setVisibility(View.GONE);

            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to onBindViewHolder");
        }
    }

    @Override
    public int getItemCount() {
        return mLDBSearches.size();
    }

    public void updateList(ArrayList<LDBSearch> searches) {
        try {

            if (searches != null) {
                loadSearches(searches);
            }

        } catch (Exception ignored) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to updateList");
        }
    }

    public void checkSearch(LDBSearch search, boolean isChecked) {
        try {
            BasicValidators validationUtils = new BasicValidators();

            for (CheckableLDBSearch listItem : mLDBSearches) {

                if (validationUtils.isSameSearch(listItem.getSearch(), search) == 1) {
                    listItem.setIsChecked(isChecked);
                }
            }

            notifyDataSetChanged();

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to checkSearch");
        }
    }

}