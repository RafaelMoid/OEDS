package br.com.eadfiocruzpe.Utils;

public class TypeUtils {

    // Types
    public enum LogMsgType {
        ERROR, WARNING, SUCCESS, REGULAR, NONE, TEST
    }

    // String representations
    static String getStrReprLogMsgType(LogMsgType messageType) {

        if (messageType.equals(LogMsgType.ERROR)) {
            return "\nError: ";

        } else if (messageType.equals(LogMsgType.WARNING)) {
            return "\nWarning: ";

        } else if (messageType.equals(LogMsgType.SUCCESS)) {
            return "\nSuccess: ";

        } else if (messageType.equals(LogMsgType.REGULAR)) {
            return "\nLog: ";

        } else if (messageType.equals(LogMsgType.NONE)) {
            return "\n";

        } else if (messageType.equals(LogMsgType.TEST)) {
            return "\nTest: ";

        } else {
            return "\n" + messageType;
        }
    }

    /**
     * Return the source of a revenue: Federal Gov., State Gov., City Gov., or Other Sources
     * by its type and name.
     */
    public String getRevenueSource(String type, String name) {
        String source = "";

        switch (type) {

            case ConstantUtils.REVENUE_TYPE_TAXES: {

                switch (name) {

                    case ConstantUtils.REVENUE_NAME_IRRF: {
                        source = ConstantUtils.REVENUE_SOURCE_FEDERAL_GOV + ConstantUtils.INACTIVE;
                        break;
                    }

                    case ConstantUtils.REVENUE_NAME_IPTU: {
                        source = ConstantUtils.REVENUE_SOURCE_CITY_GOV + ConstantUtils.INACTIVE;
                        break;
                    }

                    case ConstantUtils.REVENUE_NAME_ITBI: {
                        source = ConstantUtils.REVENUE_SOURCE_CITY_GOV + ConstantUtils.INACTIVE;
                        break;
                    }

                    case ConstantUtils.REVENUE_NAME_ISS: {
                        source = ConstantUtils.REVENUE_SOURCE_CITY_GOV + ConstantUtils.INACTIVE;
                        break;
                    }

                    case ConstantUtils.REVENUE_NAME_ITR: {
                        source = ConstantUtils.REVENUE_SOURCE_CITY_GOV + ConstantUtils.INACTIVE;
                        break;
                    }

                    case ConstantUtils.REVENUE_NAME_FINES_INTERESTS_OTHER: {
                        source = ConstantUtils.REVENUE_SOURCE_CITY_GOV + ConstantUtils.INACTIVE;
                        break;
                    }

                    case ConstantUtils.REVENUE_NAME_ACTIVE_DEBT: {
                        source = ConstantUtils.REVENUE_SOURCE_FEDERAL_GOV + ConstantUtils.INACTIVE;
                        break;
                    }

                    case ConstantUtils.REVENUE_NAME_ACTIVE_DEBT_OBLIGATIONS: {
                        source = ConstantUtils.REVENUE_SOURCE_FEDERAL_GOV + ConstantUtils.INACTIVE;
                        break;
                    }

                    default: {
                        source = ConstantUtils.REVENUE_SOURCE_FEDERAL_GOV + ConstantUtils.INACTIVE;
                        break;
                    }
                }

                break;
            }

            case ConstantUtils.REVENUE_TYPE_RESOURCES_FROM_SUS: {

                switch (name) {

                    case ConstantUtils.REVENUE_NAME_FROM_UNION: {
                        source = ConstantUtils.REVENUE_SOURCE_FEDERAL_GOV;
                        break;
                    }

                    case ConstantUtils.REVENUE_NAME_FROM_STATE: {
                        source = ConstantUtils.REVENUE_SOURCE_STATE_GOV;
                        break;
                    }

                    case ConstantUtils.REVENUE_NAME_FROM_CITY: {
                        source = ConstantUtils.REVENUE_SOURCE_OTHER;
                        break;
                    }

                    case ConstantUtils.REVENUE_NAME_FROM_OTHER_SUS: {
                        source = ConstantUtils.REVENUE_SOURCE_FEDERAL_GOV;
                        break;
                    }

                    default: {
                        source = ConstantUtils.REVENUE_SOURCE_FEDERAL_GOV;
                        break;
                    }
                }

                break;
            }

            case ConstantUtils.REVENUE_TYPE_CONSTITUTIONAL_TRANSFERS: {

                switch (name) {

                    case ConstantUtils.REVENUE_NAME_IPI_EXP: {
                        source = ConstantUtils.REVENUE_SOURCE_FEDERAL_GOV + ConstantUtils.INACTIVE;
                        break;
                    }

                    case ConstantUtils.REVENUE_NAME_FPM: {
                        source = ConstantUtils.REVENUE_SOURCE_FEDERAL_GOV + ConstantUtils.INACTIVE;
                        break;
                    }

                    case ConstantUtils.REVENUE_NAME_ITR_PART: {
                        source = ConstantUtils.REVENUE_SOURCE_FEDERAL_GOV + ConstantUtils.INACTIVE;
                        break;
                    }


                    case ConstantUtils.REVENUE_NAME_ICMS: {
                        source = ConstantUtils.REVENUE_SOURCE_STATE_GOV + ConstantUtils.INACTIVE;
                        break;
                    }

                    case ConstantUtils.REVENUE_NAME_IPVA: {
                        source = ConstantUtils.REVENUE_SOURCE_STATE_GOV + ConstantUtils.INACTIVE;
                        break;
                    }

                    default: {
                        source = ConstantUtils.REVENUE_SOURCE_FEDERAL_GOV + ConstantUtils.INACTIVE;
                        break;
                    }
                }

                break;
            }

            case ConstantUtils.REVENUE_TYPE_FINANCIAL_COMPENSATION_TAXES_CONST_TRANSFERS: {

                switch (name) {

                    case ConstantUtils.REVENUE_NAME_ICMS_DES: {
                        source = ConstantUtils.REVENUE_SOURCE_STATE_GOV + ConstantUtils.INACTIVE;
                        break;
                    }

                    default: {
                        source = ConstantUtils.REVENUE_SOURCE_STATE_GOV + ConstantUtils.INACTIVE;
                        break;
                    }
                }

                break;
            }

            case ConstantUtils.REVENUE_TYPE_VOLUNTARY_TRANSFERS: {
                source = ConstantUtils.REVENUE_SOURCE_OTHER;

                break;
            }

            case ConstantUtils.REVENUE_TYPE_CREDIT_OPERATIONS: {
                source = ConstantUtils.REVENUE_SOURCE_OTHER;

                break;
            }

            case ConstantUtils.REVENUE_TYPE_OTHER_REVENUES: {
                source = ConstantUtils.REVENUE_SOURCE_OTHER;

                break;
            }
        }

        return source;
    }

}