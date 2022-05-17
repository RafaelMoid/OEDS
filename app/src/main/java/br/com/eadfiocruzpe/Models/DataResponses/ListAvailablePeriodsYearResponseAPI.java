package br.com.eadfiocruzpe.Models.DataResponses;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

import com.google.gson.annotations.SerializedName;

import br.com.eadfiocruzpe.Models.BusinessLogicModels.PeriodYear;
import br.com.eadfiocruzpe.Utils.ConstantUtils;
import br.com.eadfiocruzpe.Utils.LogUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;
import br.com.eadfiocruzpe.Views.Validators.BasicValidators;

public class ListAvailablePeriodsYearResponseAPI {

    private final String TAG = "ListAvaPerYearResp";

    @SerializedName("list_periods_year")
    private ArrayList<PeriodYear> mPeriodsYears = new ArrayList<>();
    private HashMap<String, String> mMapPeriodsYear = new HashMap<>();
    private LogUtils mLogUtils;

    /**
     * Initialization
     */
    public ListAvailablePeriodsYearResponseAPI() {
        mLogUtils = new LogUtils();
    }

    public void buildMapPeriodsYear() {
        try {
            mMapPeriodsYear.clear();

            if (!this.getPeriodsYears().isEmpty()) {
                BasicValidators validationUtils = new BasicValidators();
                ArrayList<String> periodsYear = new ArrayList<>();
                ArrayList<String> periodsYearCodes = new ArrayList<>();

                for (PeriodYear periodYear: this.getPeriodsYears()) {

                    if (validationUtils.isValidString(periodYear.getName()) &&
                            validationUtils.isValidString(String.valueOf(periodYear.getId()))) {

                        periodsYear.add(periodYear.getName());
                        periodsYearCodes.add(String.valueOf(periodYear.getId()));
                    }
                }

                setMapPeriodsYear(periodsYear, periodsYearCodes);
            } else {
                setMapPeriodsYearEmergencies();
            }
        } catch(NullPointerException npe) {
            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to buildMapPeriodsYear");
        }
    }

    /**
     * Setters and Getters
     */
    private void setMapPeriodsYear(ArrayList<String> periodsYearDescriptions,
                                   ArrayList<String> periodsYearCodes) {
        try {

            if (periodsYearDescriptions.size() > 0 &&
                    periodsYearDescriptions.size() == periodsYearCodes.size()) {
                mMapPeriodsYear.clear();

                for (int i = 0; i < periodsYearDescriptions.size(); i++) {

                    if (periodsYearDescriptions.get(i).equals(ConstantUtils.DESCRIPTION_LAST_TIME_PERIOD_DB)) {
                        ConstantUtils.CODE_SEXTO_BIMESTRE = periodsYearCodes.get(i);
                        mMapPeriodsYear.put(ConstantUtils.DESCRIPTION_LAST_TIME_PERIOD_APP, periodsYearCodes.get(i));

                    } else if (periodsYearDescriptions.get(i).equals(ConstantUtils.DESCRIPTION_MIDDLE_TIME_PERIOD_DB)) {
                        mMapPeriodsYear.put(ConstantUtils.DESCRIPTION_MIDDLE_TIME_PERIOD_APP, periodsYearCodes.get(i));

                    } else {
                        mMapPeriodsYear.put(periodsYearDescriptions.get(i), periodsYearCodes.get(i));
                    }
                }

            } else {
                mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                        TAG + " Failed to setMapPeriodsYear");
                setMapPeriodsYearEmergencies();
            }

        } catch (NullPointerException npe) {
            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to setMapPeriodsYear");
            setMapPeriodsYearEmergencies();
        }
    }

    /**
     * If the mYear the user is searching is not the current one add all the possible periods of
     * time, otherwise consider the current period of time we are in
     */
    private void setMapPeriodsYearEmergencies() {
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);

        mMapPeriodsYear.clear();
        mMapPeriodsYear.put("1º Bimestre", "12");
        mMapPeriodsYear.put("2º Bimestre", "14");
        mMapPeriodsYear.put(ConstantUtils.DESCRIPTION_MIDDLE_TIME_PERIOD_APP, "1");
        mMapPeriodsYear.put("4º Bimestre", "18");
        mMapPeriodsYear.put("5º Bimestre", "20");
        mMapPeriodsYear.put(ConstantUtils.DESCRIPTION_LAST_TIME_PERIOD_APP, "2");

        ConstantUtils.CODE_SEXTO_BIMESTRE = "2";
    }

    public HashMap<String, String> getMapPeriodsYear() {
        return mMapPeriodsYear;
    }

    private ArrayList<PeriodYear> getPeriodsYears() {
        return mPeriodsYears;
    }

    public ArrayList<String> getListPeriodsYear() {
        ArrayList<String> listPeriodsYear = new ArrayList<>();

        try {
            listPeriodsYear = new ArrayList<>(mMapPeriodsYear.keySet());

            Collections.sort(listPeriodsYear, new Comparator<String>() {
                @Override
                public int compare(String s1, String s2) {
                    return s1.compareToIgnoreCase(s2);
                }
            });

        } catch (Exception e) {
            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to getListPeriodsYear");
        }

        return listPeriodsYear;
    }

}