package br.com.eadfiocruzpe.Views.Adapters;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Contracts.ShareableItemAdapterContract;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.ShareableItem;
import br.com.eadfiocruzpe.Utils.ConstantUtils;
import br.com.eadfiocruzpe.Utils.ImageUtils;
import br.com.eadfiocruzpe.Utils.LogUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;
import br.com.eadfiocruzpe.Views.Validators.BasicValidators;
import br.com.eadfiocruzpe.Views.ViewHolders.ShareableItemViewHolder;

public class ShareableItemsAdapter extends RecyclerView.Adapter<ShareableItemViewHolder> {

    private final String TAG = "ShareItemsAdapter";

    private ShareableItemAdapterContract mCallback;
    private ArrayList<ShareableItem> mShareableItems;
    private BasicValidators mValidationUtils;
    private Context mContext;
    private LogUtils mLogUtils;

    public ShareableItemsAdapter(ShareableItemAdapterContract client, Context context,
                                 ArrayList<ShareableItem> shareableItems) {
        mCallback = client;
        mContext = context;
        mShareableItems = shareableItems;
        mValidationUtils = new BasicValidators();
        mLogUtils = new LogUtils();

        notifyDataSetChanged();
    }

    @Override
    public ShareableItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View searchHistoryView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.component_list_shareable_item, parent, false);

        return new ShareableItemViewHolder(searchHistoryView);
    }

    @Override
    public void onBindViewHolder(final ShareableItemViewHolder holder, final int position) {
        final ShareableItem shareableItem = mShareableItems.get(position);

        try {

            if (mValidationUtils.isValidString(shareableItem.getImg())) {
                holder.container.setVisibility(View.VISIBLE);
                ImageUtils.loadImage(mContext.getApplicationContext(), shareableItem.getImg(), holder.img);

                // Events
                holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (mCallback != null) {
                            deleteItem(shareableItem);
                            mCallback.onItemDeleted(shareableItem);
                        }
                    }
                });

            } else {
                holder.container.setVisibility(View.GONE);

                mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                        TAG + "Failed onBindViewHolder");
            }

        } catch (NullPointerException ignored) {
            holder.container.setVisibility(View.GONE);

            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed onBindViewHolder");
        }
    }

    @Override
    public int getItemCount() {
        return mShareableItems.size();
    }

    private void deleteItem(ShareableItem item) {
        try {
            int idxDeletedItem = -1;

            for (int i = 0; i < mShareableItems.size(); i++) {
                ShareableItem sharedItem = mShareableItems.get(i);

                if (sharedItem.getImg().equals(item.getImg())) {
                    idxDeletedItem = i;
                }
            }

            if (idxDeletedItem != -1) {
                mShareableItems.remove(idxDeletedItem);
                notifyDataSetChanged();
            }

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to deleteItem " + npe.getMessage());
        }
    }

    public ArrayList<ShareableItem> getUpdatedList() {
        return mShareableItems;
    }

    public ArrayList<Uri> getItems(Context context, boolean addFilePrefix) {
        try {
            ArrayList<Uri> imageUris = new ArrayList<>();

            for (ShareableItem shareableItem : getUpdatedList()) {
                Uri imageUri;

                if (addFilePrefix) {
                    File file = new File(shareableItem.getImg());
                    String providerName = context.getString(R.string.settings_page_package_name) + ConstantUtils.FILE_PROVIDER;

                    imageUri = FileProvider.getUriForFile(
                            context.getApplicationContext(),
                            providerName,
                            file);
                } else {
                    imageUri = Uri.parse(shareableItem.getImg());
                }

                if (imageUri != null) {
                    imageUris.add(imageUri);
                }
            }

            return imageUris;

        } catch (Exception e) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to getItems " + e.getMessage());
        }

        return null;
    }

}