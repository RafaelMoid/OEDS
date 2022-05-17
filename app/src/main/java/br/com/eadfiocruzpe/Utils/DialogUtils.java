package br.com.eadfiocruzpe.Utils;

import android.app.Dialog;
import android.content.Context;

import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Views.Dialogs.GeneralPurposesDialog;

/**
 * This class is responsible for wrapping the creation of the different types of dialogs available
 * in the app.
 Usage example:
 DialogUtils.createInfoDialogDataIsInSync(getActivity(), new GeneralPurposesDialog.GeneralDialogCallback() {

    public void onCloseDialog() {
        DialogUtils.closeDialog();
    }

    public void onInfoDialogActionConfirmed() {
        DialogUtils.closeDialog();
        Toast.makeText(getContext().getApplicationContext(), "User confirmed the action", Toast.LENGTH_SHORT).show();
    }

    public void onConfirmationDialogActionConfirmed() {
        DialogUtils.closeDialog();
        Toast.makeText(getContext().getApplicationContext(), "User confirmed the action", Toast.LENGTH_SHORT).show();
    }
 });
 */

public class DialogUtils {

    private static Dialog sDialog;

    public static boolean isDialogVisible() {
        return sDialog != null;
    }

    public static void closeDialog() {
        if (sDialog != null) {
            sDialog.dismiss();
            sDialog = null;
        }
    }

    public static void createInfoDialogThankYou(Context context, GeneralPurposesDialog.GeneralDialogCallback callback) {
        try {

            if (!isDialogVisible()) {
                sDialog = new GeneralPurposesDialog(context,
                        callback,
                        GeneralPurposesDialog.DIALOG_TYPE_INFORMATION,
                        GeneralPurposesDialog.DIALOG_THEME_SUCCESS,
                        "...",
                        R.drawable.ico_thanks_colorful,
                        context.getString(R.string.txt_msg_thanks_for_joining_us),
                        context.getString(R.string.lbl_btn_start));

                ((GeneralPurposesDialog) sDialog).setMessageImgAlpha(1f);

                sDialog.setCanceledOnTouchOutside(false);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public static void createInfoDialogDataIsInSync(Context context, GeneralPurposesDialog.GeneralDialogCallback callback) {
        try {

            if (sDialog != null) {
                sDialog.dismiss();
                sDialog = null;
            }

            sDialog = new GeneralPurposesDialog(context,
                    callback,
                    GeneralPurposesDialog.DIALOG_TYPE_INFORMATION,
                    GeneralPurposesDialog.DIALOG_THEME_SUCCESS,
                    context.getString(R.string.lbl_title_success),
                    R.drawable.ico_sync_gray,
                    context.getString(R.string.txt_msg_your_data_in_sync),
                    "");

            ((GeneralPurposesDialog) sDialog).setMessageImgAlpha(1f);

            sDialog.setCanceledOnTouchOutside(false);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public static void createInfoDialogYouAreOffline(Context context, GeneralPurposesDialog.GeneralDialogCallback callback) {
        try {

            if (sDialog != null) {
                sDialog.dismiss();
                sDialog = null;
            }

            sDialog = new GeneralPurposesDialog(context,
                    callback,
                    GeneralPurposesDialog.DIALOG_TYPE_INFORMATION,
                    GeneralPurposesDialog.DIALOG_THEME_INFO,
                    context.getString(R.string.lbl_title_attention),
                    R.drawable.ico_connection_gray,
                    context.getString(R.string.txt_msg_seems_offline),
                    "");

            ((GeneralPurposesDialog) sDialog).setMessageImgAlpha(0.35f);

            sDialog.setCanceledOnTouchOutside(false);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public static void createErrorDialog(Context context, String message,
                                         GeneralPurposesDialog.GeneralDialogCallback callback) {
        try {

            if (sDialog != null) {
                sDialog.dismiss();
                sDialog = null;
            }

            sDialog = new GeneralPurposesDialog(context,
                    callback,
                    GeneralPurposesDialog.DIALOG_TYPE_INFORMATION,
                    GeneralPurposesDialog.DIALOG_THEME_DANGER,
                    context.getString(R.string.lbl_title_attention),
                    R.drawable.ico_face_upset_black,
                    message,
                    "");

            ((GeneralPurposesDialog) sDialog).setMessageImgAlpha(0.35f);

            sDialog.setCanceledOnTouchOutside(false);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public static void createConfirmDialogDeleteItem(Context context, GeneralPurposesDialog.GeneralDialogCallback callback) {
        try {

            if (sDialog != null) {
                sDialog.dismiss();
                sDialog = null;
            }

            sDialog = new GeneralPurposesDialog(context,
                    callback,
                    GeneralPurposesDialog.DIALOG_TYPE_CONFIRMATION,
                    GeneralPurposesDialog.DIALOG_THEME_DANGER,
                    context.getString(R.string.lbl_title_caution),
                    R.drawable.ico_delete_gray,
                    context.getString(R.string.txt_msg_deleted_items),
                    "");

            ((GeneralPurposesDialog) sDialog).setMessageImgAlpha(0.35f);

            sDialog.setCanceledOnTouchOutside(false);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

}