package br.com.eadfiocruzpe.Views.Components;

import android.content.res.Resources;
import android.widget.TextView;

import br.com.eadfiocruzpe.Utils.LogUtils;
import br.com.eadfiocruzpe.Utils.StringUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;

public class ComponentTextValueWithIcon {

    private static final String TAG = "CTextValIcon";

    TextView valueTextView;

    private LogUtils mLogUtils;
    private Resources mResources;

    ComponentTextValueWithIcon(Resources resources) {
        mResources = resources;
        mLogUtils = new LogUtils();
    }

    public void setPercentageOnMsg(float percentage) {
        try {
            StringUtils strUtils = new StringUtils(mResources);

            String currentVal = valueTextView.getText().toString();
            String[] partsCurrentVal = currentVal.split("%");

            if (partsCurrentVal.length > 1) {
                String cleanVal = partsCurrentVal[partsCurrentVal.length - 1]
                        .replace("\n", "");

                currentVal = strUtils.getPercentageStringFromVal(percentage) + "\n" + cleanVal;
            } else {
                String cleanVal = currentVal
                        .replace("\n", "");

                currentVal = strUtils.getPercentageStringFromVal(percentage) + "\n" + cleanVal;
            }

            valueTextView.setText(currentVal);

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to setPercentageOnMsg");
        }
    }

}