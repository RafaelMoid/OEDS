package br.com.eadfiocruzpe.Utils;

import java.util.ArrayList;

import br.com.eadfiocruzpe.Models.BusinessLogicModels.User;

public class ConstantUtils {

    // Main menu
    public static final int DEFAULT_TAB_BAR_HEIGHT = 80;

    // Local Database
    public static final String LDB_NAME = "app-oeds-database";

    // Settings
    public static final int SETTINGS_DEFAULT_VAL_MAX_NUM_STORED_SEARCHES = 5000;

    // Request parameters
    public static final String REQUEST_PARAM_SHARED_CONTENT = "REQUEST_PARAM_SHARED_CONTENT";
    public static final String SCHEME_PACKAGE = "package";

    // Intents ids
    public static final int REQUEST_CODE_SHARE_NO_IMG = 0;
    public static final int REQUEST_CODE_SHARE_IMG = 1;
    public static final int REQUEST_CODE_SHARE_WHATSAPP = 2;


    // Main navigation UI components
    public static final int UNSELECTED_TAB_IDX = -1;

    // Languages
    public static final String LANG_PT = "pt";

    // Timeouts
    public static final int TIMEOUT_SHOW_KEYBOARD_AUTO_COMPLETE_SEARCH_ON_SEARCH_DIALOG = 250;
    public static final int TIMEOUT_DISMISS_KEYBOARD = 6000;
    public static final int TIMEOUT_PRESENT_DASHBOARD = 1500;
    public static final int TIMEOUT_ANIMATIONS_DASHBOARD = 3000;
    public static final int TIMEOUT_ANIMATIONS_COMPARISON = 3000;
    public static final int TIMEOUT_RESTORE_FRAGMENT = 1000;
    public static final int TIMEOUT_SHOW_EMPTY_LIST_MSG = 1000;
    public static final int TIMEOUT_LOAD_COMPONENTS = 1000;
    public static final int TIMEOUT_DISMISS_MSG = 3000;

    // Search
    public static final int MAX_NUM_CHARS_TRIGGER_SEARCH_INITIAL_PAGE = 2;
    public static final int MAX_NUM_SEARCH_SUGGESTIONS_SHOWING = 4;
    public static final String DESCRIPTION_LAST_TIME_PERIOD_DB = "6º Bimestre";
    public static final String DESCRIPTION_LAST_TIME_PERIOD_APP = "Anual";
    public static final String DESCRIPTION_MIDDLE_TIME_PERIOD_DB = "3º Bimestre";
    public static final String DESCRIPTION_MIDDLE_TIME_PERIOD_APP = "Semestral";

    // Default values
    public static final String INACTIVE = "_inactive";
    public static final String APP_IMAGE_FOLDER_NAME = "oeds_images";
    public static final int TIME_RANGE_START_YEAR = 2000;
    public static final User ANONYMOUS_USER = new User(
        "AnonymousUser",
        "AnonymousUser",
        "",
        "AnonymousUser"
    );
    public static final String DEFAULT_VALUE_SEARCH_DIALOG_STATE = "Estado";
    public static final String DEFAULT_VALUE_SEARCH_DIALOG_CITY = "Cidade";
    public static final String DEFAULT_VALUE_SEARCH_DIALOG_TIME_RANGE = "Período";
    public static final String DEFAULT_VALUE_SEARCH_DIALOG_YEAR = "Ano";
    public static final String DEFAULT_VALUE_ZERO_LIST = "zero_list_val";
    public static String CODE_SEXTO_BIMESTRE = "2";

    // List Selectors
    public static final float LIST_SELECTOR_BTN_SELECTED_TEXT_SIZE = 24;
    public static final float LIST_SELECTOR_BTN_TEXT_SIZE = 18;

    // Charts
    public static final float CHART_LEGEND_FORM_SIZE = 10f;
    public static final float CHART_LEGEND_TEXT_SIZE = 10f;
    public static final float CHART_VALUE_SM_TEXT_SIZE = 8f;
    public static final float CHART_GRANULARITY_X_AXIS = 1f;
    public static final float CHART_DATASET_SM_TEXT_SIZE = 10f;
    public static final float CHART_LABEL_ROTATION_ANGLE = 70f;
    public static final float CHART_BAR_CHART_WIDTH = 0.95f;
    public static final float CHART_LINE_CHART_WIDTH = 2.5f;
    public static final float CHART_LINE_CHART_POINT_CIRCLE_RADIUS = 5f;
    public static final String CHART_RANKING_FLAG_BEST_IN_COUNTRY = "CHART_RANKING_FLAG_BEST_IN_COUNTRY";
    public static final String CHART_RANKING_FLAG_BEST_IN_STATE = "CHART_RANKING_FLAG_BEST_IN_STATE";
    public static final String CHART_RANKING_FLAG_WORST_IN_STATE = "CHART_RANKING_FLAG_WORST_IN_STATE";
    public static final String CHART_RANKING_FLAG_WORST_IN_COUNTRY = "CHART_RANKING_FLAG_WORST_IN_COUNTRY";
    public static final String CHART_RANKING_FLAG_SEARCHED_IN_MIDDLE = "CHART_RANKING_FLAG_SEARCHED_IN_MIDDLE";

    // Buttons
    public static final float ALPHA_DISABLED_BUTTON = 0.5f;
    public static final float ALPHA_DISABLED_LIST = 0.3f;

    // Files
    public static final String FILE_PROVIDER = ".fileprovider";
    public static final String TEXT_TYPE = "text/plain";
    public static final String IMAGE_FILE_TYPE = "image/jpeg";
    public static final String TEXT_HTML = "text/html";

    // Messages
    public static final int SNACKBAR_MESSAGE_DURATION = 8000;

    // Social Networks
    public static final int SOCIAL_NET_FACEBOOK = 1;
    public static final int SOCIAL_NET_EMAIL = 2;
    public static final int SOCIAL_NET_TWITTER = 3;
    public static final int SOCIAL_NET_WHATSAPP = 4;
    public static final int SOCIAL_NET_MESSENGER = 5;

    public static final String WHATSAPP_PACKAGE_NAME = "com.whatsapp";
    public static final String GMAIL_PACKAGE_NAME = "android.gm";
    public static final String FACEBOOK_PACKAGE_NAME = "facebook";
    public static final String MESSENGER_PACKAGE_NAME = "messenger";
    public static final String TWITTER_PACKAGE_NAME = "com.twitter.composer.ComposerActivity";

    // City Indicators
    public static final String KEY_IND_VALUE_PER_CITIZEN_PER_YEAR_FOR_PH = "2.1";
    public static final String KEY_IND_PARTICIPATION_OTHER_SOURCES_TOTAL_EXPENSES_ON_PH = "3.1";
    public static final String KEY_IND_CITY_AUTONOMY_LEVEL_INVESTMENTS_ON_PH = "3.2";

    // Law constants
    public static final float LAW_THRESHOLD_MINIMUM_CITY_INVESTMENT_PH = 15f;

    // Federal Government investments on PH
    public static final String PREFIX_REVENUE_TABLE_1 = "revenue_table_1_application_action_services_ph";
    public static final String PREFIX_REVENUE_TABLE_2 = "revenue_table_2_additional_revs_finance_ph";

    public static final String REVENUE_NAME_FROM_UNION = "Provenientes da União";
    public static final String REVENUE_NAME_FROM_STATE = "Provenientes dos Estados";
    public static final String REVENUE_NAME_FROM_CITY = "Provenientes de Outros Municípios";
    public static final String REVENUE_NAME_FROM_OTHER_SUS = "Outras Receitas do SUS";
    public static final String REVENUE_NAME_IPTU = "Imposto Predial e Territorial Urbano - IPTU";
    public static final String REVENUE_NAME_ITBI = "Imposto sobre Transmissão de Bens Intervivos - ITBI";
    public static final String REVENUE_NAME_ISS = "Imposto sobre Serviços de Qualquer Natureza - ISS";
    public static final String REVENUE_NAME_ITR = "Imposto Territorial Rural - ITR";
    public static final String REVENUE_NAME_FINES_INTERESTS_OTHER = "Multas, Juros de Mora e Outros Encargos dos Impostos";
    public static final String REVENUE_NAME_ACTIVE_DEBT = "Dívida Ativa dos Impostos";
    public static final String REVENUE_NAME_ACTIVE_DEBT_OBLIGATIONS = "Multas, Juros de Mora e Outros Encargos da Dívida Ativa";
    public static final String REVENUE_NAME_IRRF = "Imposto de Renda Retido na Fonte - IRRF";
    public static final String REVENUE_NAME_FPM = "Cota-Parte FPM";
    public static final String REVENUE_NAME_ITR_PART = "Cota-Parte ITR";
    public static final String REVENUE_NAME_IPVA = "Cota-Parte IPVA";
    public static final String REVENUE_NAME_ICMS = "Cota-Parte ICMS";
    public static final String REVENUE_NAME_ICMS_DES = "Desoneração ICMS (LC 87/96)";
    public static final String REVENUE_NAME_IPI_EXP = "Cota-Parte IPI-Exportação";

    public static final String REVENUE_SOURCE_FEDERAL_GOV = "Governo Federal";
    public static final String REVENUE_SOURCE_STATE_GOV = "Governo Estadual";
    public static final String REVENUE_SOURCE_CITY_GOV = "Governo Municipal";
    public static final String REVENUE_SOURCE_OTHER = "Outras fontes";

    public static final String REVENUE_TYPE_TAXES = "RECEITA DE IMPOSTOS LÍQUIDA (I)";
    public static final String REVENUE_TYPE_CONSTITUTIONAL_TRANSFERS = "RECEITA DE TRANSFERÊNCIAS CONSTITUCIONAIS E LEGAIS (II)";
    public static final String REVENUE_TYPE_FINANCIAL_COMPENSATION_TAXES_CONST_TRANSFERS = "Compensações Financeiras Provenientes de Impostos e Transferências Constitucionais";
    public static final String REVENUE_TYPE_RESOURCES_FROM_SUS = "TRANSFERÊNCIA DE RECURSOS DO SISTEMA ÚNICO DE SAÚDE-SUS";
    public static final String REVENUE_TYPE_VOLUNTARY_TRANSFERS = "TRANSFERÊNCIAS VOLUNTÁRIAS";
    public static final String REVENUE_TYPE_CREDIT_OPERATIONS = "RECEITA DE OPERAÇÕES DE CRÉDITO VINCULADAS À SAÚDE";
    public static final String REVENUE_TYPE_OTHER_REVENUES = "OUTRAS RECEITAS PARA FINANCIAMENTO DA SAÚDE";

    public static final ArrayList<String> INVESTMENT_DECLARED_CITY = new ArrayList<String>(){{
        add("Atenção Básica");
        add("Assistência Hospitalar e Ambulatorial");
        add("Suporte Profilático e Terapêutico");
        add("Vigilância Sanitária");
        add("Vigilância Epidemiológica");
        add("Alimentação e Nutrição");
        add("Outras Subfunções");
    }};

}