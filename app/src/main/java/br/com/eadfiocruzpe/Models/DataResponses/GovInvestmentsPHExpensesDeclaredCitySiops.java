package br.com.eadfiocruzpe.Models.DataResponses;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBGovInvestmentPH;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBRevenueTargetedPH;
import br.com.eadfiocruzpe.Utils.ConstantUtils;
import br.com.eadfiocruzpe.Utils.LogUtils;
import br.com.eadfiocruzpe.Utils.StringUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;
import br.com.eadfiocruzpe.Views.Validators.BasicValidators;

public class GovInvestmentsPHExpensesDeclaredCitySiops {

    private final String TAG = "ListGovInvestPHCityExp";

    private BaseWebScrapingResponse mRawResponse;
    private String mSelectedPeriodYear;
    private String mTypeRevenueBeingProcessed;
    private HashMap<String, LDBRevenueTargetedPH> mMapCityRevenueTargetedOnPH = new HashMap<>();
    private HashMap<String, LDBGovInvestmentPH> mMapCityExpensesOnPH = new HashMap<>();

    private LogUtils mLogUtils;
    private StringUtils mStringUtils;

    /**
     * Initialization
     */
    public GovInvestmentsPHExpensesDeclaredCitySiops(BaseWebScrapingResponse rawResponse,
                                                     String selectedPeriodYear) {
        mLogUtils = new LogUtils();
        mStringUtils = new StringUtils();

        setRawResponse(rawResponse);
        setSelectedPeriodYear(selectedPeriodYear);
        processRawResponse();
    }

    /**
     * Extracts the content of the objects managed by the Getter and Setter methods from
     * the raw HTML string.
     */
    private void processRawResponse() {
        try {
            String TABLE_REVENUE_1 = "RECEITAS PARA APURAÇÃO DA APLICAÇÃO EM AÇÕES E SERVIÇOS PÚBLICOS DE SAÚDE";
            String TABLE_REVENUE_2 = "RECEITAS ADICIONAIS PARA FINANCIAMENTO DA SAÚDE";
            String TABLE_EXPENSES = "DESPESAS COM SAÚDE (Por Subfunção)";

            if (getRawResponse() != null) {
                BasicValidators validationUtils = new BasicValidators();

                // Build a tree with the raw HTML String
                Document document = Jsoup.parseBodyFragment(getRawResponse().content);

                // Transverse the tree and find the desired content
                Element element = document.body();
                Elements listTables = element.select("table");

                // Table "RECEITAS PARA APURAÇÃO DA APLICAÇÃO EM AÇÕES E SERVIÇOS PÚBLICOS DE SAÚDE"
                Element tableRevenuesMustBeTargetedPH = null;

                // Table "RECEITAS ADICIONAIS PARA FINANCIAMENTO DA SAÚDE"
                Element tableAdditionalRevenuesMustBeTargetedPH = null;

                // Table "DESPESAS COM SAÚDE (Por Subfunção)"
                Element tableExpensesOnPHBySubFunction = null;

                // Identify and select the necessary tables
                for (Element table : listTables) {
                    Elements listElementsTableRows = table.select("tr td");

                    for (Element tableRowElement : listElementsTableRows) {

                        for (Node node : tableRowElement.childNodes()) {
                            String value = mStringUtils.getCleanString(node.outerHtml());
                            String tableRev1 = mStringUtils.getCleanString(TABLE_REVENUE_1);
                            String tableRev2 = mStringUtils.getCleanString(TABLE_REVENUE_2);
                            String tableExpenses = mStringUtils.getCleanString(TABLE_EXPENSES);

                            if (validationUtils.isNotNull(value)) {

                                if (value.equals(tableRev1)) {
                                    tableRevenuesMustBeTargetedPH = table;

                                } else if (value.equals(tableRev2)) {
                                    tableAdditionalRevenuesMustBeTargetedPH = table;

                                } else if (value.equals(tableExpenses)) {
                                    tableExpensesOnPHBySubFunction = table;
                                }
                            }
                        }
                    }
                }

                // Extract data from the revenue tables
                if (validationUtils.isNotNull(tableRevenuesMustBeTargetedPH) &&
                    validationUtils.isNotNull(tableAdditionalRevenuesMustBeTargetedPH)) {
                    assert tableRevenuesMustBeTargetedPH != null;
                    assert tableAdditionalRevenuesMustBeTargetedPH != null;

                    // Extract the values from the table 1
                    ArrayList<String> keysRevenuesTable1PH = new ArrayList<>();
                    ArrayList<LDBRevenueTargetedPH> revenuesTable1PH = new ArrayList<>();

                    Elements listElementsTable1Rows = tableRevenuesMustBeTargetedPH.select("tbody tr");
                    processRevenueTableRows(listElementsTable1Rows, keysRevenuesTable1PH,
                                            revenuesTable1PH, ConstantUtils.PREFIX_REVENUE_TABLE_1);

                    // Extract the values from the table 2
                    ArrayList<String> keysRevenuesTable2PH = new ArrayList<>();
                    ArrayList<LDBRevenueTargetedPH> revenuesTable2PH = new ArrayList<>();

                    Elements listElementsTable2Rows = tableAdditionalRevenuesMustBeTargetedPH.select("tbody tr");
                    processRevenueTableRows(listElementsTable2Rows, keysRevenuesTable2PH,
                                            revenuesTable2PH, ConstantUtils.PREFIX_REVENUE_TABLE_2);

                    // Set the attributes of the revenue tables into a HashMap of Revenue objects
                    setMapCityRevenue(keysRevenuesTable1PH, revenuesTable1PH);
                    setMapCityRevenue(keysRevenuesTable2PH, revenuesTable2PH);
                }

                // Extract data from the expenses table
                if (validationUtils.isNotNull(tableExpensesOnPHBySubFunction)) {
                    assert tableExpensesOnPHBySubFunction != null;

                    // Extract the values from the table
                    ArrayList<String> keysExpensesTargetPH = new ArrayList<>();
                    ArrayList<LDBGovInvestmentPH> expensesTargetedPH = new ArrayList<>();

                    Elements listElementsTableRows = tableExpensesOnPHBySubFunction.select("tbody tr");
                    processRowsExpensesTable(listElementsTableRows, keysExpensesTargetPH, expensesTargetedPH);

                    // Set the attributes of the revenue tables into a HashMap of Revenue objects
                    setMapCityExpenses(keysExpensesTargetPH, expensesTargetedPH);
                }

            } else {
                mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                        TAG + " Failed to processRawResponse");
            }
        } catch (NullPointerException npe) {
            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to processRawResponse");

        } catch (IndexOutOfBoundsException iobe) {
            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to processRawResponse");

        } catch (UnsupportedOperationException uoe) {
            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to processRawResponse");
        }
    }

    /**
     * Evaluate the rows of a table and try to build a valid Revenue object
     */
    private void processRevenueTableRows(Elements listElementsTableRows,
                                         ArrayList<String> keysRevenuesTargetPH,
                                         ArrayList<LDBRevenueTargetedPH> revenuesTargetedPH,
                                         String tableIdPrefix) {
        try {
            BasicValidators validationUtils = new BasicValidators();

            if (validationUtils.isValidList(listElementsTableRows)) {

                if (listElementsTableRows.size() > 3) {
                    // Remove the table header and footer
                    listElementsTableRows.remove(0);
                    listElementsTableRows.remove(0);
                    listElementsTableRows.remove(listElementsTableRows.size() - 1);

                    // Identify a valid LDBRevenueTargetedPH and add it to the data
                    for (int i = 0; i < listElementsTableRows.size(); i++) {
                        Element tableRowElement = listElementsTableRows.get(i);
                        LDBRevenueTargetedPH ldbRevenueTargetedPH = new LDBRevenueTargetedPH();

                        // Add the valid revenue object into the gathered data
                        if (evaluateRevenueInnerNode(tableRowElement, ldbRevenueTargetedPH,
                                tableIdPrefix + String.valueOf(i))) {
                            keysRevenuesTargetPH.add(ldbRevenueTargetedPH.getId());
                            revenuesTargetedPH.add(ldbRevenueTargetedPH);
                        }
                    }
                }
            }

        } catch (Exception e) {
            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to processRevenueTableRows: " + e.getMessage());
        }
    }

    /**
     * Extract the revenues made by the federal government on one of the first 5 "Bimestres"
     */
    private boolean evaluateRevenueInnerNode(Element tableRowElement,
                                             LDBRevenueTargetedPH ldbRevenueTargetedPH,
                                             String newRevenueId) {
        try {
            BasicValidators validationUtils = new BasicValidators();

            // Evaluate the structure of the list of <td> objects
            if (validationUtils.isValidList(tableRowElement.childNodes())) {

                for (Node tableRowInnerElement : tableRowElement.childNodes()) {

                    if (tableRowInnerElement.childNodes().size() > 0) {
                        String html = tableRowInnerElement.toString();
                        String value = tableRowInnerElement.childNode(0).outerHtml();

                        // Evaluate the value and possibly starting filling the federalInvestment object
                        // Identify the type of the revenue
                        if (setTypeCurrentRevenue(value)) {
                            break;

                        } else if (validationUtils.isValidString(html) &&
                                   validationUtils.isValidString(value)) {

                            // Accept only values within <td> tags
                            if (html.split("td").length > 0 ) {

                                // Start filling the object
                                if (validationUtils.isValidString(ldbRevenueTargetedPH.getName())) {

                                    if (ldbRevenueTargetedPH.getInitialAllocation() != -1) {

                                        if (ldbRevenueTargetedPH.getUpdatedAllocationPrediction() != -1) {

                                            if (ldbRevenueTargetedPH.getValueReceivedSoFar() != -1) {

                                                if (ldbRevenueTargetedPH.getPercentageReceivedValueSoFar() == -1) {
                                                    ldbRevenueTargetedPH.setPercentageReceivedValueSoFar(value);

                                                    if (validationUtils.isValidString(getTypeRevenueBeingProcessed())) {
                                                        ldbRevenueTargetedPH.setType(
                                                                getTypeRevenueBeingProcessed(),
                                                                ldbRevenueTargetedPH.getName());

                                                        ldbRevenueTargetedPH.setId(newRevenueId);

                                                        return true;
                                                    }
                                                }
                                            } else {
                                                ldbRevenueTargetedPH.setValueReceivedSoFar(value);
                                            }
                                        } else {
                                            ldbRevenueTargetedPH.setUpdatedAllocationPrediction(value);
                                        }
                                    } else {
                                        ldbRevenueTargetedPH.setInitialAllocation(value);
                                    }
                                } else {
                                    ldbRevenueTargetedPH.setName(value.replace("&nbsp;", ""));
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to evaluateRevenueInnerNode: " + e.getMessage());
        }

        return false;
    }

    /**
     * Determine the type of the current Revenue object
     */
    private boolean setTypeCurrentRevenue(String currentValue) {
        final String REVENUE_TABLE_1_IGNORED_ROW = "TOTAL DAS RECEITAS PARA APURAÇÃO DA APLICAÇÃO EM AÇÕES E SERVIÇOS PÚBLICOS DE SAÚDE (III) = I + II";
        final String REVENUE_TABLE_2_IGNORED_ROW = "TOTAL RECEITAS ADICIONAIS PARA FINANCIAMENTO DA SAÚDE";
        final String revenueTable1IgnoredRow = mStringUtils.getCleanString(REVENUE_TABLE_1_IGNORED_ROW);
        final String revenueTable2IgnoredRow = mStringUtils.getCleanString(REVENUE_TABLE_2_IGNORED_ROW);
        currentValue = mStringUtils.getCleanString(currentValue);
        boolean skipToNextRow = false;

        if (mStringUtils.getCleanString(ConstantUtils.REVENUE_TYPE_TAXES).equals(currentValue)) {
            setTypeRevenueBeingProcessed(ConstantUtils.REVENUE_TYPE_TAXES);
            skipToNextRow = true;

        } else if (mStringUtils.getCleanString(ConstantUtils.REVENUE_TYPE_FINANCIAL_COMPENSATION_TAXES_CONST_TRANSFERS).equals(currentValue)) {
            setTypeRevenueBeingProcessed(ConstantUtils.REVENUE_TYPE_FINANCIAL_COMPENSATION_TAXES_CONST_TRANSFERS);
            skipToNextRow = true;

        } else if (mStringUtils.getCleanString(ConstantUtils.REVENUE_TYPE_RESOURCES_FROM_SUS).equals(currentValue)) {
            setTypeRevenueBeingProcessed(ConstantUtils.REVENUE_TYPE_RESOURCES_FROM_SUS);
            skipToNextRow = true;

        } else if (mStringUtils.getCleanString(ConstantUtils.REVENUE_TYPE_CONSTITUTIONAL_TRANSFERS).equals(currentValue)) {
            setTypeRevenueBeingProcessed(ConstantUtils.REVENUE_TYPE_CONSTITUTIONAL_TRANSFERS);
            skipToNextRow = true;

        } else if (mStringUtils.getCleanString(ConstantUtils.REVENUE_TYPE_VOLUNTARY_TRANSFERS).equals(currentValue)) {
            setTypeRevenueBeingProcessed(ConstantUtils.REVENUE_TYPE_VOLUNTARY_TRANSFERS);

        } else if (mStringUtils.getCleanString(ConstantUtils.REVENUE_TYPE_CREDIT_OPERATIONS).equals(currentValue)) {
            setTypeRevenueBeingProcessed(ConstantUtils.REVENUE_TYPE_CREDIT_OPERATIONS);

        } else if (mStringUtils.getCleanString(ConstantUtils.REVENUE_TYPE_OTHER_REVENUES).equals(currentValue)) {
            setTypeRevenueBeingProcessed(ConstantUtils.REVENUE_TYPE_OTHER_REVENUES);

        } else if (revenueTable1IgnoredRow.equals(currentValue) || revenueTable2IgnoredRow.equals(currentValue)){
            skipToNextRow = true;
        }

        return skipToNextRow;
    }

    /**
     * Evaluate the rows of a table and try to build a valid Revenue object
     */
    private void processRowsExpensesTable(Elements listElementsTableRows,
                                          ArrayList<String> keysRevenuesTargetPH,
                                          ArrayList<LDBGovInvestmentPH> revenuesTargetedPH) {
        try {
            BasicValidators validationUtils = new BasicValidators();

            if (validationUtils.isValidList(listElementsTableRows)) {

                if (listElementsTableRows.size() > 3) {
                    // Remove the table header and footer
                    listElementsTableRows.remove(0);
                    listElementsTableRows.remove(0);
                    listElementsTableRows.remove(listElementsTableRows.size() - 1);

                    // Identify a valid LDBGovInvestmentPH and add it to the data
                    for (int i = 0; i < listElementsTableRows.size(); i++) {
                        Element tableRowElement = listElementsTableRows.get(i);
                        LDBGovInvestmentPH governmentInvestment = new LDBGovInvestmentPH();

                        // Evaluate investment objects if they belong to one of the first 5 "Bimestres"
                        if (!ConstantUtils.CODE_SEXTO_BIMESTRE
                                .equals(getSelectedPeriodYear())) {

                            // Add the valid investment object into the gathered data
                            if (evaluateInvestmentInnerNode(tableRowElement, governmentInvestment,
                                    String.valueOf(i))) {
                                keysRevenuesTargetPH.add(governmentInvestment.getId());
                                revenuesTargetedPH.add(governmentInvestment);
                            }

                        }
                        // Evaluate investment objects if they belong to the 6º "Bimestre"
                        // Add the valid investment object into the gathered data
                        else if (evaluateInvestmentInnerNodeForLastBimestre(tableRowElement,
                                governmentInvestment, String.valueOf(i))) {
                            keysRevenuesTargetPH.add(governmentInvestment.getId());
                            revenuesTargetedPH.add(governmentInvestment);

                        } else {
                            Log.d(TAG, tableRowElement.toString());
                        }
                    }
                }
            }

        } catch (Exception e) {
            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to processRowsExpensesTable: " + e.getMessage());
        }
    }

    /**
     * Extract the investments made by the federal government on one of the first 5 "Bimestres"
     */
    private boolean evaluateInvestmentInnerNode(Element tableRowElement,
                                                LDBGovInvestmentPH federalInvestment,
                                                String newFederalInvestmentId) {
        try {
            BasicValidators validationUtils = new BasicValidators();

            // Evaluate the structure of the list of <td> objects
            if (validationUtils.isValidList(tableRowElement.childNodes())) {

                for (Node tableRowInnerElement : tableRowElement.childNodes()) {

                    if (validationUtils.isNotNull(tableRowInnerElement.childNodes())) {

                        if (tableRowInnerElement.childNodes().size() > 0) {
                            Node rowNodeValue = tableRowInnerElement.childNodes().get(0);
                            String value = rowNodeValue.outerHtml();

                            // Evaluate the value and possibly starting filling the federalInvestment object
                            if (validationUtils.isValidString(value)) {

                                if (validationUtils.isValidString(federalInvestment.getName())) {

                                    if (federalInvestment.getInitialAllocation() != -1) {

                                        if (federalInvestment.getAllocationUntilSearchDate() != -1) {

                                            if (federalInvestment.getExpensesPreparedForPaymentUntilSearchDate() != -1) {

                                                if (federalInvestment.getPercentageExpensesPreparedForPayment() != -1) {

                                                    if (federalInvestment.getPaidExpensesUntilSearchDate() != -1) {

                                                        if (federalInvestment.getPercentagePaidExpensesUntilSearchDate() == -1) {
                                                            // Finishes the extraction of the Investment object
                                                            federalInvestment.setPercentagePaidExpensesUntilSearchDate(value);
                                                            federalInvestment.setId(String.valueOf(newFederalInvestmentId));
                                                            cleanInvestmentName(federalInvestment);

                                                            return true;
                                                        }
                                                    } else {
                                                        federalInvestment.setPaidExpensesUntilSearchDate(value);
                                                    }
                                                } else {
                                                    federalInvestment.setPercentageExpensesPreparedForPayment(value);
                                                }
                                            } else {
                                                federalInvestment.setExpensesPreparedForPaymentUntilSearchDate(value);
                                            }
                                        } else {
                                            federalInvestment.setAllocationUntilSearchDate(value);
                                        }
                                    } else {
                                        federalInvestment.setInitialAllocation(value);
                                    }
                                } else {
                                    federalInvestment.setName(value);
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to evaluateInnerNode: " + e.getMessage());
        }

        return false;
    }

    private void cleanInvestmentName(LDBGovInvestmentPH investmentOnPH) {

        for (String cityInvestmentName : ConstantUtils.INVESTMENT_DECLARED_CITY) {
            String cleanedInvName1 = mStringUtils.getCleanString(investmentOnPH.getName());
            String cleanedInvName2 = mStringUtils.getCleanString(cityInvestmentName);

            if (cleanedInvName1.equals(cleanedInvName2)) {
                investmentOnPH.setName(cityInvestmentName);
            }
        }
    }

    /**
     * The table that holds the value of the investments on the '6º Bimestre' has a different layout,
     * hence it's necessary to apply a different parsing process.
     */
    private boolean evaluateInvestmentInnerNodeForLastBimestre(Element tableRowElement,
                                                               LDBGovInvestmentPH federalInvestment,
                                                               String newFederalInvestmentId) {
        try {
            BasicValidators validationUtils = new BasicValidators();

            // Evaluate the structure of the list of <td> objects
            if (validationUtils.isValidList(tableRowElement.childNodes())) {
                boolean hasSkippedColumnRestsToPay = false;

                for (Node tableRowInnerElement : tableRowElement.childNodes()) {

                    if (validationUtils.isNotNull(tableRowInnerElement.childNodes())) {

                        if (tableRowInnerElement.childNodes().size() > 0) {
                            Node rowNodeValue = tableRowInnerElement.childNodes().get(0);
                            String value = rowNodeValue.outerHtml();

                            // Start filling the federalInvestment object after evaluation
                            if (validationUtils.isValidString(value)) {

                                if (validationUtils.isValidString(federalInvestment.getName())) {

                                    if (federalInvestment.getInitialAllocation() != -1) {

                                        if (federalInvestment.getAllocationUntilSearchDate() != -1) {

                                            if (federalInvestment.getPaidExpensesUntilSearchDate() != -1) {

                                                if (hasSkippedColumnRestsToPay) {

                                                    if (federalInvestment.getPercentagePaidExpensesUntilSearchDate() == -1) {
                                                        // Finishes the extraction of the Investment object
                                                        federalInvestment.setPercentagePaidExpensesUntilSearchDate(value);
                                                        federalInvestment.setId(String.valueOf(newFederalInvestmentId));
                                                        cleanInvestmentName(federalInvestment);

                                                        return true;
                                                    }
                                                } else {
                                                    hasSkippedColumnRestsToPay = true;
                                                }
                                            } else {
                                                federalInvestment.setPaidExpensesUntilSearchDate(value);
                                            }
                                        } else {
                                            federalInvestment.setAllocationUntilSearchDate(value);
                                        }
                                    } else {
                                        federalInvestment.setInitialAllocation(value);
                                    }
                                } else {
                                    federalInvestment.setName(value);
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to evaluateInnerNode: " + e.getMessage());
        }

        return false;
    }

    /**
     * Setters and Getters
     */
    private void setRawResponse(BaseWebScrapingResponse rawResponse) {
        mRawResponse = rawResponse;
    }

    private BaseWebScrapingResponse getRawResponse() {
        return mRawResponse;
    }

    private void setSelectedPeriodYear(String selectedPeriodYear) {
        mSelectedPeriodYear = selectedPeriodYear;
    }

    private String getSelectedPeriodYear() {
        return mSelectedPeriodYear;
    }

    private void setTypeRevenueBeingProcessed(String type) {
        mTypeRevenueBeingProcessed = type;
    }

    private String getTypeRevenueBeingProcessed() {
        return mTypeRevenueBeingProcessed;
    }

    private void setMapCityRevenue(ArrayList<String> keysCityRevenues,
                                   ArrayList<LDBRevenueTargetedPH> revenueSources) {
        try {

            if (keysCityRevenues.size() == revenueSources.size()) {

                for (int i = 0; i < keysCityRevenues.size(); i++) {
                    mMapCityRevenueTargetedOnPH.put(
                            keysCityRevenues.get(i),
                            revenueSources.get(i));
                }

            } else {
                mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                        TAG + " Failed to setMapCityRevenue");
            }
        } catch (Exception e) {
            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to setMapCityRevenue: " + e.getMessage());
        }
    }

    public HashMap<String, LDBRevenueTargetedPH> getMapCityRevenueSources() {
        return mMapCityRevenueTargetedOnPH;
    }

    private void setMapCityExpenses(ArrayList<String> keysCityExpenses,
                                   ArrayList<LDBGovInvestmentPH> cityExpenses) {
        try {
            mMapCityExpensesOnPH.clear();

            if (keysCityExpenses.size() == cityExpenses.size()) {

                for (int i = 0; i < keysCityExpenses.size(); i++) {
                    mMapCityExpensesOnPH.put(
                            keysCityExpenses.get(i),
                            cityExpenses.get(i));
                }
            } else {
                mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                        TAG + " Failed to setMapCityExpenses");
            }
        } catch (Exception e) {
            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to setMapCityExpenses: " + e.getMessage());
        }
    }

    public HashMap<String, LDBGovInvestmentPH> getMapCityExpenses() {
        return mMapCityExpensesOnPH;
    }

}