package br.com.eadfiocruzpe.Utils;

import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class SnackbarUtils {

    public static final int MAX_NUM_LINES_SNACKBAR = 6;


    public static void alignSnackbarTextCenter(Snackbar snackbar) {

        if (snackbar != null) {
            View snackView = snackbar.getView();
            TextView snackTextView = (TextView) snackView.findViewById(android.support.design.R.id.snackbar_text);

            if (snackTextView != null) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                    snackTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                }

                snackTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            }
        }
    }

    public static void setSnackbarHeight(Snackbar snackbar, int maxNumLines) {

        if (snackbar != null) {
            View snackView = snackbar.getView();
            TextView snackTextView = snackView.findViewById(android.support.design.R.id.snackbar_text);

            if (snackTextView != null && maxNumLines == 0) {
                snackTextView.setMaxLines(MAX_NUM_LINES_SNACKBAR);

            } else if (snackTextView != null) {
                snackTextView.setMaxLines(maxNumLines);
            }
        }
    }

    public static void setSnackbarBackgroundColor(Snackbar snackbar, int color) {

        if (snackbar != null) {
            View snackView = snackbar.getView();
            snackView.setBackgroundColor(color);
        }
    }

    public static void setSnackbarTextColor(Snackbar snackbar) {

        if (snackbar != null) {
            View snackView = snackbar.getView();
            TextView tv = snackView.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
        }
    }

}