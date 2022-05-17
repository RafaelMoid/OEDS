package br.com.eadfiocruzpe.Utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.IllegalFormatConversionException;
import java.util.Locale;

import android.content.res.Resources;

import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBSearch;
import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Views.Validators.BasicValidators;

public class StringUtils {

    public static final String ID_JOINER = "_";
    private static String TAG = "StringUtils: ";

    private Resources mResources = null;
    private HashMap<String, String> mapUfsPrefixes = new HashMap<>();
    private HashMap<String, String> mapShortUfsPrefixes = new HashMap<>();
    private HashMap<String, String> mapUfNamesPrefixes = new HashMap<>();

    /**
     * Initialization
     */
    public StringUtils() {
        initMapUfPrefixes();
    }

    public StringUtils(Resources resources) {
        mResources = resources;
        initMapUfPrefixes();
    }

    private void initMapUfPrefixes() {
        mapUfsPrefixes.clear();
        mapUfsPrefixes.put("acre", "do ");
        mapUfsPrefixes.put("alagoas", "de ");
        mapUfsPrefixes.put("amapá", "do ");
        mapUfsPrefixes.put("amazonas", "de ");
        mapUfsPrefixes.put("bahia", "da ");
        mapUfsPrefixes.put("ceará", "do ");
        mapUfsPrefixes.put("distrito federal", "do ");
        mapUfsPrefixes.put("espírito santo", "do ");
        mapUfsPrefixes.put("goiás", "de ");
        mapUfsPrefixes.put("maranhão", "do ");
        mapUfsPrefixes.put("mato grosso", "do ");
        mapUfsPrefixes.put("mato grosso do sul", "do ");
        mapUfsPrefixes.put("minas gerais", "de ");
        mapUfsPrefixes.put("pará", "do ");
        mapUfsPrefixes.put("paraíba", "da ");
        mapUfsPrefixes.put("paraná", "do ");
        mapUfsPrefixes.put("pernambuco", "de ");
        mapUfsPrefixes.put("piauí", "do ");
        mapUfsPrefixes.put("rio de janeiro", "do ");
        mapUfsPrefixes.put("rio grande do norte", "do ");
        mapUfsPrefixes.put("rio grande do sul", "do ");
        mapUfsPrefixes.put("rondônia", "de ");
        mapUfsPrefixes.put("roraima", "de ");
        mapUfsPrefixes.put("santa catarina", "de ");
        mapUfsPrefixes.put("são paulo", "de ");
        mapUfsPrefixes.put("sergipe", "de ");
        mapUfsPrefixes.put("tocantis", "do ");

        mapShortUfsPrefixes.put("ac", "acre");
        mapShortUfsPrefixes.put("al", "alagoas");
        mapShortUfsPrefixes.put("ap", "amapá");
        mapShortUfsPrefixes.put("am", "amazonas");
        mapShortUfsPrefixes.put("ba", "bahia");
        mapShortUfsPrefixes.put("ce", "ceará");
        mapShortUfsPrefixes.put("df", "distrito federal");
        mapShortUfsPrefixes.put("es", "espírito santo");
        mapShortUfsPrefixes.put("go", "goiás");
        mapShortUfsPrefixes.put("ma", "maranhão");
        mapShortUfsPrefixes.put("mt", "mato grosso");
        mapShortUfsPrefixes.put("ms", "mato grosso do sul");
        mapShortUfsPrefixes.put("mg", "minas gerais");
        mapShortUfsPrefixes.put("pa", "pará");
        mapShortUfsPrefixes.put("pb", "paraíba");
        mapShortUfsPrefixes.put("pr", "paraná");
        mapShortUfsPrefixes.put("pe", "pernambuco");
        mapShortUfsPrefixes.put("pi", "piauí");
        mapShortUfsPrefixes.put("rj", "rio de janeiro");
        mapShortUfsPrefixes.put("rn", "rio grande do norte");
        mapShortUfsPrefixes.put("rs", "rio grande do sul");
        mapShortUfsPrefixes.put("ro", "rondônia");
        mapShortUfsPrefixes.put("rr", "roraima");
        mapShortUfsPrefixes.put("sc", "santa catarina");
        mapShortUfsPrefixes.put("sp", "são paulo");
        mapShortUfsPrefixes.put("se", "sergipe");
        mapShortUfsPrefixes.put("to", "tocantis");

        mapUfNamesPrefixes.put("acre", "ac");
        mapUfNamesPrefixes.put("alagoas", "al");
        mapUfNamesPrefixes.put("amapá", "ap");
        mapUfNamesPrefixes.put("amazonas", "am");
        mapUfNamesPrefixes.put("bahia", "ba");
        mapUfNamesPrefixes.put("ceará", "ce");
        mapUfNamesPrefixes.put("distrito federal", "df");
        mapUfNamesPrefixes.put("espírito santo", "es");
        mapUfNamesPrefixes.put("goiás", "go");
        mapUfNamesPrefixes.put("maranhão", "ma");
        mapUfNamesPrefixes.put("mato grosso", "mt");
        mapUfNamesPrefixes.put("mato grosso do sul", "ms");
        mapUfNamesPrefixes.put("minas gerais", "mg");
        mapUfNamesPrefixes.put("pará", "pa");
        mapUfNamesPrefixes.put("paraíba", "pb");
        mapUfNamesPrefixes.put("paraná", "pr");
        mapUfNamesPrefixes.put("pernambuco", "pe");
        mapUfNamesPrefixes.put("piauí", "pi");
        mapUfNamesPrefixes.put("rio de janeiro", "rj");
        mapUfNamesPrefixes.put("rio grande do norte", "rn");
        mapUfNamesPrefixes.put("rio grande do sul", "rs");
        mapUfNamesPrefixes.put("rondônia", "ro");
        mapUfNamesPrefixes.put("roraima", "rr");
        mapUfNamesPrefixes.put("santa catarina", "sc");
        mapUfNamesPrefixes.put("são paulo", "sp");
        mapUfNamesPrefixes.put("sergipe", "se");
        mapUfNamesPrefixes.put("tocantis", "to");
    }

    /**
     * Other methods
     */
    public String getUfAcronym(String stateIdentifier) {
        LogUtils logUtils = new LogUtils();

        try {

            if (stateIdentifier != null) {

                if (stateIdentifier.length() > 0) {
                    stateIdentifier = stateIdentifier.toLowerCase();
                    stateIdentifier = stateIdentifier.replace("stateof", "");
                    String stateName = mapShortUfsPrefixes.get(stateIdentifier);

                    if (stateName != null) {
                        return stateIdentifier;

                    } else {
                        String stateAcronym = mapUfNamesPrefixes.get(stateIdentifier);

                        if (stateAcronym != null) {
                            return stateAcronym;
                        }
                    }
                }
            }

        } catch (Exception e) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to getFullStateName " + e.getMessage());
        }

        return "";
    }

    public String getArticleForStateName(String stateFullName, boolean addBoldTag) {
        LogUtils logUtils = new LogUtils();

        try {

            if (stateFullName != null) {

                if (stateFullName.length() > 0) {
                    String stateName = stateFullName.toLowerCase();
                    String prefix = mapUfsPrefixes.get(stateName);

                    if (prefix == null) {
                        return "do ";

                    } else if (addBoldTag) {
                        return prefix + " <b>" + stateFullName + "</b>";

                    } else {
                        return prefix + stateFullName;
                    }
                }
            }

        } catch (Exception e) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to getNounWithPortugueseArticle " + e.getMessage());
        }

        return stateFullName;
    }

    public double getDoubleValueFromString(String strNumber) {
        try {

            // Clean the String
            if (mResources != null) {
                strNumber = strNumber.replace(mResources.getString(R.string.money_prefix), "");
            }

            strNumber = strNumber.replace(" ", "");

            // Parse the String value into a valid Float
            String[] splitByDot = strNumber.split("\\.");
            String[] splitByComma = strNumber.split(",");
            double val = -1;

            if (splitByDot.length > 1 && splitByComma.length > 1) {

                if (splitByDot[0].length() < splitByComma[0].length()) {
                    strNumber = strNumber.replace(".", "");
                    strNumber = strNumber.replace(",", ".");
                } else {
                    strNumber = strNumber.replace(",", "");
                }

                val = getDoubleFromNumberWithSeparator(strNumber);

            } else if (splitByDot.length > 1) {
                val = getDoubleFromNumberWithSeparator(strNumber);

            } else if(splitByComma.length > 1) {
                strNumber = strNumber.replace(",", ".");
                val = getDoubleFromNumberWithSeparator(strNumber);
            }

            return val;
        }
        catch (NumberFormatException ignored) {}
        catch (IllegalFormatConversionException ignored) {}
        catch (NullPointerException ignored) {}

        return -1;
    }

    private double getDoubleFromNumberWithSeparator(String strNumber) {
        try {
            return Double.parseDouble(strNumber);
        }
        catch (NumberFormatException ignored) {}
        catch (IllegalFormatConversionException ignored) {}
        catch (NullPointerException ignored) {}

        return -1f;
    }

    public String getMoneyStringFromVal(double value) {
        LogUtils logUtils = new LogUtils();
        String formattedValue = "";

        // Custom formatting
        try {
            Locale.setDefault(new Locale("pt", "BR"));
            DecimalFormat df = new java.text.DecimalFormat("###,###.##");
            formattedValue = df.format(value);

        } catch (IllegalFormatConversionException ignored){}

        // Formatting similar to the Brazilian formatting
        try {

            if (formattedValue.isEmpty()) {
                formattedValue = NumberFormat.getNumberInstance(Locale.FRANCE).format(value);
            }

        } catch (IllegalFormatConversionException ignored) {}

        // Add the R$ in front of the formatted number
        try {

            if (mResources != null) {
                return String.format(mResources.getString(R.string.money_format_str), formattedValue);
            }

        } catch (IllegalFormatConversionException ignored) {}

        return formattedValue;
    }

    public String getShortMoneyStringFromVal(double value) {
        LogUtils logUtils = new LogUtils();
        String formattedValue = getMoneyStringFromVal(value);

        try {

            if (mResources != null) {
                String[] parts = formattedValue.split("\\.");

                if (parts.length == 3) {
                    formattedValue = String.format(mResources.getString(R.string.value_format_mi),
                            formattedValue.substring(0, 7));

                } else if (parts.length == 4) {
                    formattedValue = String.format(mResources.getString(R.string.value_format_bi),
                            formattedValue.substring(0, 7));

                } else if (parts.length == 5) {
                    formattedValue = String.format(mResources.getString(R.string.value_format_tri),
                            formattedValue.substring(0, 7));
                }
            }

        } catch (Exception e) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to getShortMoneyStringFromVal");
        }

        return formattedValue;
    }

    public String getPercentageStringFromVal(float value) {
        LogUtils logUtils = new LogUtils();

        try {

            if (mResources != null) {
                return String.format(mResources.getString(R.string.percentage_format), value) + " %";

            } else {
                logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                        TAG + "Failed to getPercentageStringFromVal");
            }
        } catch (IllegalFormatConversionException e) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to getPercentageStringFromVal");
        }

        return "";
    }

    public String getPrettyPeriodYear(LDBSearch search) {
        LogUtils logUtils = new LogUtils();

        try {
            return search.getYear() + " - " + search.getPeriodYear();

        } catch (Exception npe) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to getPrettyPeriodYear");
        }

        return "";
    }

    public String getCleanString(String str) {
        LogUtils logUtils = new LogUtils();

        try {
            str = str.toLowerCase().replaceAll("[^\\p{ASCII}]", "");
            str = str.replaceAll("\uFFFD", "");
            str = str.replaceAll("\\s", "");

        } catch (NullPointerException npe) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING, "Failed to cleanString");
        }

        return str;
    }

    public String getSummarizedSearchDescription(LDBSearch search) {
        LogUtils logUtils = new LogUtils();
        String description = "";

        try {
            BasicValidators mValidationUtils = new BasicValidators();
            StringBuilder objStrRepr = new StringBuilder();

            if (mValidationUtils.isValidString(search.getCity())) {
                objStrRepr.append(search.getCity());
                objStrRepr.append(" - ");
            }

            if (mValidationUtils.isValidString(getUfAcronym(search.getState()))) {
                objStrRepr.append(getUfAcronym(search.getState()).toUpperCase());
                objStrRepr.append("\n");
            }

            if (mValidationUtils.isValidString(search.getPeriodYear())) {
                objStrRepr.append(search.getPeriodYear());
                objStrRepr.append(" - ");
            }

            if (mValidationUtils.isValidYear(search.getYear())) {
                objStrRepr.append(search.getYear());
            }

            description = objStrRepr.toString();

        } catch(Exception e) {
            logUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed getSearchDescriptionForTitle: " + e.getMessage());
        }

        return description;
    }

}