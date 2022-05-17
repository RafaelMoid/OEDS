package br.com.eadfiocruzpe.Utils;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

public class UIUtils {

    public static int dpToPx(Resources resources, int dp) {
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public void toggleDescriptionVisibility(TextView name, TextView description) {

        if (description != null && name != null) {

            if (!description.getText().toString().isEmpty()) {
                description.setVisibility(description.getVisibility() == View.VISIBLE?
                        View.GONE : View.VISIBLE);

                if (description.getVisibility() == View.VISIBLE) {
                    name.setTypeface(name.getTypeface(), Typeface.BOLD);
                } else {
                    name.setTypeface(null, Typeface.NORMAL);
                }
            }
        }
    }

}