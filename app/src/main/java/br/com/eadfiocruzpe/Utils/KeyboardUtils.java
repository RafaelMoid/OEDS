package br.com.eadfiocruzpe.Utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyboardUtils {

    public static void dismissKeyboard(Context context, View view) {

        try {

            if (view != null) {
                InputMethodManager imm = (InputMethodManager)
                        context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                if (imm != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        } catch (Exception ignored) {}
    }

}