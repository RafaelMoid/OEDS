package br.com.eadfiocruzpe.Views.Components;

import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Contracts.ComponentFloatingActionButtonsContract;
import br.com.eadfiocruzpe.Utils.ImageUtils;
import br.com.eadfiocruzpe.Utils.LogUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;

public class ComponentFloatingActionButtons {

    public LinearLayout rootLayout;
    private ImageView mBtnAddShare;
    private ImageView mBtnFavorite;
    private ImageView mBtnShare;

    private ComponentFloatingActionButtonsContract mCallback;
    private LogUtils mLogUtils;

    ComponentFloatingActionButtons(LinearLayout rootLayout, ComponentFloatingActionButtonsContract callback) {
        initUI(rootLayout);
        initTools();
        initEvents();
        mCallback = callback;
    }

    /**
     * Initialization
     */
    private void initUI(LinearLayout rootLayout) {
        try {
            this.rootLayout = rootLayout;

            mBtnAddShare = this.rootLayout.findViewById(R.id.component_fab_btn_add_share_btn);
            mBtnFavorite = this.rootLayout.findViewById(R.id.component_fab_btn_favorite_btn);
            mBtnShare = this.rootLayout.findViewById(R.id.component_fab_btn_share_btn);

            show(false);

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    "ComponentFloatingActionButtons Failed to initUI");
        }
    }

    private void initTools() {
        mLogUtils = new LogUtils();
    }

    private void initEvents() {
        mBtnAddShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mCallback != null) {
                    mCallback.onAddBtnClicked();
                }
            }
        });

        mBtnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mCallback != null) {
                    mCallback.onFavoriteBtnClicked();
                }
            }
        });

        mBtnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mCallback != null) {
                    mCallback.onShareBtnClicked();
                }
            }
        });
    }

    /**
     * Events
     */
    public void show(boolean showComponent) {

        if (rootLayout != null) {
            rootLayout.setVisibility(showComponent ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * Setters and Getters
     */
    public void setCallback(ComponentFloatingActionButtonsContract callback) {
        mCallback = callback;
    }

    void setIsFavorite(Resources resources, boolean isFavorite) {
        ImageUtils.setFavoriteImg(resources, mBtnFavorite, isFavorite);
    }

    void setShowFavoriteBtn(boolean show) {
        mBtnFavorite.setVisibility(show? View.VISIBLE : View.GONE);
    }

}