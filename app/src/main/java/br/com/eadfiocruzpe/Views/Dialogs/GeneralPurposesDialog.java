package br.com.eadfiocruzpe.Views.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Utils.ImageUtils;

public class GeneralPurposesDialog extends Dialog {

    public static final String DIALOG_TYPE_INFORMATION = "DIALOG_TYPE_INFORMATION";
    public static final String DIALOG_TYPE_CONFIRMATION = "DIALOG_TYPE_CONFIRMATION";

    public static final String DIALOG_THEME_SUCCESS = "DIALOG_THEME_SUCCESS";
    public static final String DIALOG_THEME_INFO = "DIALOG_THEME_INFO";
    private static final String DIALOG_THEME_WARNING = "DIALOG_THEME_WARNING";
    public static final String DIALOG_THEME_DANGER = "DIALOG_THEME_DANGER";

    // UI
    private LinearLayout mDialogHeader;
    private LinearLayout mDialogBody;
    private TextView mTitleView;
    private ImageView mBtnClose;
    private ImageView mMainMsgImage;
    private TextView mMessageView;
    private Button mBtnOk;
    private LinearLayout mContainerBtnsConfirmationCancelation;
    private Button mBtnCancel;
    private Button mBtnConfirm;

    // Dialog Info
    private GeneralDialogCallback mCallback;
    private String mType;
    private String mTheme;
    private String mTitle;
    private int mMsgImage;
    private String mMessage;
    private String mLblBtnConfirm;


    public GeneralPurposesDialog(@NonNull Context context, GeneralDialogCallback callback,
                                 String type, String dialogTheme, String title, int image,
                                 String message, String lblBtnConfirm) {
        super(context, R.style.DialogTheme);
        setContentView(R.layout.dialog_general_purpose);

        initData(callback, type, dialogTheme, title, image, message, lblBtnConfirm);
        initViews(this);
        initEvents();
        loadInfo();

        show();
    }

    /**
     * Initialization
     */
    private void initData(GeneralDialogCallback callback, String type, String dialogTheme,
                          String title, int image, String message, String lblBtnConfirm) {
        mCallback = callback;
        mType = type;
        mTheme = dialogTheme;
        mTitle = title;
        mMsgImage = image;
        mMessage = message;
        mLblBtnConfirm = lblBtnConfirm;
    }

    private void initViews(final Dialog dialog) {
        bindViews(dialog);
        loadTheme();
    }

    private void bindViews(final Dialog dialog) {
        mDialogHeader = dialog.findViewById(R.id.dialog_info_header);
        mDialogBody = dialog.findViewById(R.id.dialog_info_body);
        mTitleView = dialog.findViewById(R.id.dialog_info_title);
        mBtnClose = dialog.findViewById(R.id.dialog_info_btn_close);
        mMainMsgImage = dialog.findViewById(R.id.dialog_info_msg_img);
        mMessageView = dialog.findViewById(R.id.dialog_info_msg);
        mBtnOk = dialog.findViewById(R.id.dialog_info_btn_ok);
        mContainerBtnsConfirmationCancelation = dialog.findViewById(R.id.dialog_info_confirmation_buttons_container);
        mBtnCancel = dialog.findViewById(R.id.dialog_info_btn_cancel);
        mBtnConfirm = dialog.findViewById(R.id.dialog_info_btn_confirm);
    }

    private void loadTheme() {

        if (mTheme != null && mType != null) {

            switch (mTheme) {

                case DIALOG_THEME_INFO: {
                    loadInfoTheme();
                    break;
                }

                case DIALOG_THEME_WARNING: {
                    loadWarningTheme();
                    break;
                }

                case DIALOG_THEME_SUCCESS: {
                    loadSuccessTheme();
                    break;
                }

                case DIALOG_THEME_DANGER: {
                    loadErrorTheme();
                    break;
                }
            }

            switch (mType) {

                case DIALOG_TYPE_INFORMATION: {
                    setInformationDialog();
                    break;
                }

                case DIALOG_TYPE_CONFIRMATION: {
                    setConfirmationDialog();
                    break;
                }
            }

        } else {
            closeDialog();
        }
    }

    private void loadInfoTheme() {
        mDialogHeader.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        mTitleView.setTextColor(ContextCompat.getColor(getContext(), R.color.color_gray_1));

        mDialogBody.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_white));
        mMessageView.setTextColor(ContextCompat.getColor(getContext(), R.color.color_black_3));

        mBtnOk.setTextColor(ContextCompat.getColor(getContext(), R.color.color_white));
        mBtnCancel.setTextColor(ContextCompat.getColor(getContext(), R.color.color_white));
        mBtnConfirm.setTextColor(ContextCompat.getColor(getContext(), R.color.color_white));
    }

    private void loadWarningTheme() {
        mDialogHeader.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_orange_5));
        mTitleView.setTextColor(ContextCompat.getColor(getContext(), R.color.color_white));

        mDialogBody.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_white));
        mMessageView.setTextColor(ContextCompat.getColor(getContext(), R.color.color_black_3));

        mBtnOk.setTextColor(ContextCompat.getColor(getContext(), R.color.color_white));
        mBtnCancel.setTextColor(ContextCompat.getColor(getContext(), R.color.color_white));
        mBtnConfirm.setTextColor(ContextCompat.getColor(getContext(), R.color.color_white));
    }

    private void loadSuccessTheme() {
        mDialogHeader.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_green_blue_4));
        mTitleView.setTextColor(ContextCompat.getColor(getContext(), R.color.color_white));

        mDialogBody.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_white));
        mMessageView.setTextColor(ContextCompat.getColor(getContext(), R.color.color_black_3));

        mBtnOk.setTextColor(ContextCompat.getColor(getContext(), R.color.color_white));
        mBtnCancel.setTextColor(ContextCompat.getColor(getContext(), R.color.color_white));
        mBtnConfirm.setTextColor(ContextCompat.getColor(getContext(), R.color.color_white));
    }

    private void loadErrorTheme() {
        mDialogHeader.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_white));
        mTitleView.setTextColor(ContextCompat.getColor(getContext(), R.color.color_white));

        mDialogBody.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_white));
        mMessageView.setTextColor(ContextCompat.getColor(getContext(), R.color.color_black_3));

        mBtnOk.setTextColor(ContextCompat.getColor(getContext(), R.color.color_white));
        mBtnCancel.setTextColor(ContextCompat.getColor(getContext(), R.color.color_white));
        mBtnConfirm.setTextColor(ContextCompat.getColor(getContext(), R.color.color_white));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mBtnConfirm.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.background_rectangle_red_rounded_borders));
        }
    }

    private void setInformationDialog() {
        mBtnOk.setVisibility(View.VISIBLE);
        mContainerBtnsConfirmationCancelation.setVisibility(View.GONE);
        mBtnCancel.setVisibility(View.GONE);
        mBtnConfirm.setVisibility(View.GONE);
    }

    private void setConfirmationDialog() {
        mBtnOk.setVisibility(View.GONE);
        mContainerBtnsConfirmationCancelation.setVisibility(View.VISIBLE);
        mBtnCancel.setVisibility(View.VISIBLE);
        mBtnConfirm.setVisibility(View.VISIBLE);
    }

    private void loadInfo() {

        if (mTitle != null) {

            if (!mTitle.isEmpty()) {
                mTitleView.setText(mTitle);
            } else {
                mDialogHeader.setVisibility(View.GONE);
            }
        } else {
            mDialogHeader.setVisibility(View.GONE);
        }

        ImageUtils.loadImage(getContext().getApplicationContext(), mMsgImage, mMainMsgImage);

        if (mMessage != null) {
            mMessageView.setText(mMessage);
        }

        if (mLblBtnConfirm != null) {

            if (!mLblBtnConfirm.isEmpty()) {
                mBtnOk.setText(mLblBtnConfirm);
            } else {
                mBtnOk.setVisibility(View.GONE);
                mMessageView.setTextColor(ContextCompat.getColor(getContext(), R.color.color_gray_10));
            }
        } else {
            mBtnOk.setVisibility(View.GONE);
            mMessageView.setTextColor(ContextCompat.getColor(getContext(), R.color.color_gray_10));
        }
    }

    public void setMessageImgAlpha(float alpha) {
        mMainMsgImage.setAlpha(alpha);
    }

    /**
     * Events
     */
    private void initEvents() {
        mBtnClose.setOnClickListener(mBtnCloseClickListener);
        mBtnOk.setOnClickListener(mBtnOkClickListener);
        mBtnCancel.setOnClickListener(mBtnCancelClickListener);
        mBtnConfirm.setOnClickListener(mBtnConfirmClickListener);
    }

    private final View.OnClickListener mBtnCloseClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mCallback != null) {
                mCallback.onCloseDialog();
            }
        }
    };

    private final View.OnClickListener mBtnOkClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mCallback != null) {
                mCallback.onInfoDialogActionConfirmed();
            }
        }
    };

    private final View.OnClickListener mBtnCancelClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mCallback != null) {
                mCallback.onCloseDialog();
            }
        }
    };

    private final View.OnClickListener mBtnConfirmClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mCallback != null) {
                mCallback.onConfirmationDialogActionConfirmed();
            }
        }
    };

    private void closeDialog() {
        GeneralPurposesDialog.this.hide();
    }

    /**
     * Interfaces
     */
    public interface GeneralDialogCallback {
        void onCloseDialog();

        void onInfoDialogActionConfirmed();

        void onConfirmationDialogActionConfirmed();
    }

}