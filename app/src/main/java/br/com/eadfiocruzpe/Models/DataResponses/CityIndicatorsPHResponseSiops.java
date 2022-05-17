package br.com.eadfiocruzpe.Models.DataResponses;

import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBCityIndicator;
import br.com.eadfiocruzpe.Utils.LogUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;
import br.com.eadfiocruzpe.Views.Validators.BasicValidators;

public class CityIndicatorsPHResponseSiops {

    private final String TAG = "ListCityIndSiops";

    private BaseWebScrapingResponse mRawResponse;
    private HashMap<String, LDBCityIndicator> mMapCityIndexes = new HashMap<>();

    private LogUtils mLogUtils;

    /**
     * Initialization
     */
    public CityIndicatorsPHResponseSiops(BaseWebScrapingResponse rawResponse) {
        mLogUtils = new LogUtils();

        setRawResponse(rawResponse);
        processRawResponse();
    }

    /**
     * Extracts the content of the objects managed by the Getter and Setter methods from
     * the raw HTML string.
     */
    private void processRawResponse() {
        try {

            if (getRawResponse() != null) {
                BasicValidators validationUtils = new BasicValidators();

                // Build a tree with the raw HTML String
                Document document = Jsoup.parseBodyFragment(getRawResponse().content);

                // Transverse the tree and find the desired content
                Element element = document.body();
                Elements rowsTableIndexes = element.select(".informacao .quadro70c table tbody tr");
                rowsTableIndexes.remove(0);
                rowsTableIndexes.remove(0);
                rowsTableIndexes.remove(0);

                ArrayList<String> cityIndexesCodes = new ArrayList<>();
                ArrayList<LDBCityIndicator> ldbCityIndicators = new ArrayList<>();

                for (Element tableRow : rowsTableIndexes) {
                    LDBCityIndicator ldbCityIndicator = new LDBCityIndicator();

                    if (validationUtils.isValidList(tableRow.childNodes())) {

                        for (Node node : tableRow.childNodes()) {

                            if (validationUtils.isValidList(node.childNodes())) {
                                Node nodeElement = node.childNode(0);

                                // Evaluate if the inner-node can build a valid LDBCityIndicator object
                                // and append a valid object in the lists
                                if (evaluateInnerNode(nodeElement, ldbCityIndicator)) {
                                    cityIndexesCodes.add(ldbCityIndicator.getId());
                                    ldbCityIndicators.add(ldbCityIndicator);
                                }
                            }
                        }
                    }
                }

                // Set the attributes of the response
                setMapCityIndexes(cityIndexesCodes, ldbCityIndicators);

            } else {
                mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING, TAG + " Failed to processRawResponse");
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

    private boolean evaluateInnerNode(Node innerNode, LDBCityIndicator ldbCityIndicator) {
        try {
            BasicValidators validationUtils = new BasicValidators();

            if (!innerNode.outerHtml().equals("") &&
                    !innerNode.outerHtml().equals("\n ") &&
                    !innerNode.outerHtml().equals("\n  ") &&
                    !innerNode.outerHtml().equals("\n   ")) {
                String value = innerNode.outerHtml();

                if (validationUtils.isValidString(ldbCityIndicator.getId())) {

                    if (validationUtils.isValidString(ldbCityIndicator.getName())) {

                        if (ldbCityIndicator.getValue() == -1) {
                            String[] valueParts = value.split(" ");

                            for (String valuePart : valueParts) {
                                String cityIndexVal = valuePart.replace(",", ".");
                                ldbCityIndicator.setValue(cityIndexVal);

                                // Add only city index objects that were completely filled to the map components
                                if (ldbCityIndicator.getValue() != -1) {
                                    return true;
                                }
                            }
                        }
                    } else {
                        ldbCityIndicator.setName(value);
                    }
                } else {
                    ldbCityIndicator.setId(value);
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

    private void setMapCityIndexes(ArrayList<String> cityIndexesKeys, ArrayList<LDBCityIndicator> ldbCityIndicators) {
        try {

            if (cityIndexesKeys.size() == ldbCityIndicators.size()) {

                for (int i = 0; i < cityIndexesKeys.size(); i++) {
                    mMapCityIndexes.put(cityIndexesKeys.get(i), ldbCityIndicators.get(i));
                }
            }

        } catch (Exception e) {
            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to setCityIndicators: " + e.getMessage());
        }
    }

    public HashMap<String, LDBCityIndicator> getCityIndexes() {
        return mMapCityIndexes;
    }

}