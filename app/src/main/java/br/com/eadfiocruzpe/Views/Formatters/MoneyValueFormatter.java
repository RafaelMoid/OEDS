package br.com.eadfiocruzpe.Views.Formatters;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import br.com.eadfiocruzpe.Utils.StringUtils;

public class MoneyValueFormatter implements IValueFormatter {

    private StringUtils mStrUtils = new StringUtils();

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex,
                                    ViewPortHandler viewPortHandler) {
        return "R$ " + mStrUtils.getMoneyStringFromVal(value);
    }

}