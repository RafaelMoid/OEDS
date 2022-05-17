package br.com.eadfiocruzpe.Views.Adapters;

import java.util.ArrayList;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Contracts.SearchHistoryAdapterContract;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBSearch;
import br.com.eadfiocruzpe.Utils.ImageUtils;
import br.com.eadfiocruzpe.Utils.LogUtils;
import br.com.eadfiocruzpe.Utils.StringUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;
import br.com.eadfiocruzpe.Views.Validators.BasicValidators;
import br.com.eadfiocruzpe.Views.ViewHolders.SearchHistoryViewHolder;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryViewHolder> {

    private final String TAG = "SearchHistAdapter";

    private SearchHistoryAdapterContract.Client mCallback;
    private ArrayList<LDBSearch> mLDBSearchHistory;
    private BasicValidators mValidationUtils;
    private Resources mResources;
    private LogUtils mLogUtils;
    private StringUtils mStrUtils;

    public SearchHistoryAdapter(SearchHistoryAdapterContract.Client client,
                                Resources resources,
                                ArrayList<LDBSearch> searchHistory) {
        mCallback = client;
        mResources = resources;
        mLDBSearchHistory = searchHistory;
        mValidationUtils = new BasicValidators();
        mLogUtils = new LogUtils();
        mStrUtils = new StringUtils();

        notifyDataSetChanged();
    }

    @Override
    public SearchHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View searchHistoryView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.component_list_item_search_history, parent, false);

        return new SearchHistoryViewHolder(searchHistoryView);
    }

    @Override
    public void onBindViewHolder(final SearchHistoryViewHolder holder, final int position) {
        final LDBSearch search = mLDBSearchHistory.get(position);

        try {

            if (mValidationUtils.isValidSearch(search)) {
                holder.containerSearch.setVisibility(View.VISIBLE);

                // Load search into the view holder
                holder.lblCityName.setText(search.getCity());
                holder.lblStateName.setText(search.getState());
                holder.lblSearchedPeriod.setText(mStrUtils.getPrettyPeriodYear(search));
                ImageUtils.setFavoriteImg(mResources, holder.btnToggleFavorite, search.getFavorite());

                // Events
                holder.containerSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (mCallback != null) {
                            mCallback.onSearchHistoryItemDetails(search);
                        }
                    }
                });

                holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (mCallback != null) {
                            mCallback.onSearchHistoryItemDeleted(search);
                        }
                    }
                });

                holder.btnToggleFavorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (mCallback != null) {
                            mCallback.onClickBtnToggleFavorite(search);
                        }
                    }
                });

            } else {
                holder.containerSearch.setVisibility(View.GONE);

                mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                        TAG + "Failed onBindViewHolder");
            }

        } catch (NullPointerException ignored) {
            holder.containerSearch.setVisibility(View.GONE);

            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed onBindViewHolder");
        }
    }

    @Override
    public int getItemCount() {
        return mLDBSearchHistory.size();
    }

    public void updateList(ArrayList<LDBSearch> searchHistory) {
        try {

            if (searchHistory != null) {
                mLDBSearchHistory = searchHistory;
                notifyDataSetChanged();
            }

        } catch (Exception ignored) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to updateList");
        }
    }

    public void updateSearch(LDBSearch search) {
        try {
            BasicValidators validationUtils = new BasicValidators();

            if (search != null) {
                int pos = -1;

                for (int i = 0; i < mLDBSearchHistory.size(); i++) {
                    LDBSearch listSearch = mLDBSearchHistory.get(i);

                    if (validationUtils.isSameSearch(listSearch, search) == 1) {
                        pos = i;
                    }
                }

                if (pos != -1) {
                    mLDBSearchHistory.add(pos, search);
                    notifyItemChanged(pos);
                }
            }
        } catch (Exception ignored) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to updateSearch");
        }
    }

}