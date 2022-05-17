package br.com.eadfiocruzpe.Models.DataResponses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import com.google.gson.annotations.SerializedName;

import br.com.eadfiocruzpe.Models.BusinessLogicModels.UF;
import br.com.eadfiocruzpe.Utils.ConstantUtils;
import br.com.eadfiocruzpe.Utils.LogUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;
import br.com.eadfiocruzpe.Views.Validators.BasicValidators;

public class ListUfsResponseAPI {

    private final String TAG = "ListUfsRespAPI";

    @SerializedName("list_ufs")
    private ArrayList<UF> mUfs = new ArrayList<>();
    private LogUtils mLogUtils;
    private HashMap<String, String> mapUfsCodes = new HashMap<>();


    /**
     * Initialization
     */
    public ListUfsResponseAPI() {
        mLogUtils = new LogUtils();
    }

    public void buildMapAvailableUFs() {
        try {

            if (!this.getUFs().isEmpty()) {
                BasicValidators validationUtils = new BasicValidators();
                ArrayList<String> ufs = new ArrayList<>();
                ArrayList<String> ufsCodes = new ArrayList<>();

                for (UF uf: this.getUFs()) {

                    if (uf != null) {

                        if (validationUtils.isValidString(uf.getFullName()) &&
                                validationUtils.isValidString(uf.getUfId())) {

                            ufs.add(uf.getFullName());
                            ufsCodes.add(uf.getUfId());
                        }
                    }
                }

                setMapUfsCodes(ufs, ufsCodes);
            } else {
                setMapUfsCodesEmergencies();
            }
        } catch (NullPointerException npe) {
            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to buildListUFs");
        }
    }

    /**
     * Getters and Setters
     */
    private void setMapUfsCodes(ArrayList<String> ufs, ArrayList<String> ufsCodes) {

        try {

            if (ufs.size() > 0 && ufs.size() == ufsCodes.size()) {
                // Build the Map between the ufs and their SIOPS codes
                mapUfsCodes.clear();

                for (int i = 0; i < ufs.size(); i++) {
                    mapUfsCodes.put(ufs.get(i), ufsCodes.get(i));
                }
            } else {
                mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                        TAG + " Failed to setMapUfsCodes");
                setMapUfsCodesEmergencies();
            }

        } catch (NullPointerException npe) {
            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to setMapUfsCodes");
            setMapUfsCodesEmergencies();
        }
    }

    private void setMapUfsCodesEmergencies() {
        mapUfsCodes.clear();
        mapUfsCodes.put("AC", "12");
        mapUfsCodes.put("AL", "27");
        mapUfsCodes.put("AP", "16");
        mapUfsCodes.put("AM", "13");
        mapUfsCodes.put("BA", "29");
        mapUfsCodes.put("CE", "23");
        mapUfsCodes.put("DF", "53");
        mapUfsCodes.put("ES", "32");
        mapUfsCodes.put("GO", "52");
        mapUfsCodes.put("MA", "21");
        mapUfsCodes.put("MT", "51");
        mapUfsCodes.put("MS", "50");
        mapUfsCodes.put("MG", "31");
        mapUfsCodes.put("PA", "15");
        mapUfsCodes.put("PB", "25");
        mapUfsCodes.put("PR", "41");
        mapUfsCodes.put("PE", "26");
        mapUfsCodes.put("PI", "22");
        mapUfsCodes.put("RJ", "33");
        mapUfsCodes.put("RN", "24");
        mapUfsCodes.put("RS", "43");
        mapUfsCodes.put("RO", "11");
        mapUfsCodes.put("RR", "14");
        mapUfsCodes.put("SC", "42");
        mapUfsCodes.put("SP", "35");
        mapUfsCodes.put("SE", "28");
        mapUfsCodes.put("TO", "17");
    }

    public HashMap<String, String> getMapUfsCodes() {
        return mapUfsCodes;
    }

    public ArrayList<String> getListUfs() {
        ArrayList<String> listUfs = new ArrayList<>();

        try {
            listUfs = new ArrayList<>(mapUfsCodes.keySet());

            Collections.sort(listUfs, new Comparator<String>() {
                @Override
                public int compare(String s1, String s2) {
                    return s1.compareToIgnoreCase(s2);
                }
            });

            listUfs.add(0, ConstantUtils.DEFAULT_VALUE_SEARCH_DIALOG_STATE);

        } catch (Exception e) {
            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to getListUfs");
        }

        return listUfs;
    }

    private ArrayList<UF> getUFs() {
        return mUfs;
    }

}