package br.com.eadfiocruzpe.Utils;

import android.content.Context;

import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Models.DataResponses.BaseResponse;

public class ErrorUtils {

    public static String getApiError(Context context, BaseResponse baseResponse) {
        String errorMsg = "";

        if (baseResponse != null) {

            if (baseResponse.getMsg() != null) {
                errorMsg = baseResponse.getMsg();
            } else {
                errorMsg = context.getString(R.string.error_api_unknown);
            }
        } else {
            errorMsg = context.getString(R.string.error_api_unknown);
        }

        return errorMsg;
    }

    public static void verifyErrorMsg(Context context, String errorMsg) {
        LogUtils logUtils = new LogUtils(context.getApplicationContext());

        try {
            String ERROR_MSG_500_NONE_TYPE_POSSIBLE_INVALID_TOKEN = context.getResources().getString(R.string.error_msg_failed_token);
            String ERROR_MSG_500_INVALID_OBJECT_STRUCTURE = context.getResources().getString(R.string.error_msg_invalid_object);

            if (errorMsg.equals(ERROR_MSG_500_NONE_TYPE_POSSIBLE_INVALID_TOKEN) ||
                errorMsg.contains(ERROR_MSG_500_INVALID_OBJECT_STRUCTURE) ) {
            }
        } catch (NullPointerException e) {
            logUtils.logMessage(TypeUtils.LogMsgType.ERROR, "ErrorUtils: Failed to verify error message");
        }
    }

}