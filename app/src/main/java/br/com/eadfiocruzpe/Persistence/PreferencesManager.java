package br.com.eadfiocruzpe.Persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import br.com.eadfiocruzpe.Models.BusinessLogicModels.SharedContent;
import br.com.eadfiocruzpe.Utils.ConstantUtils;
import br.com.eadfiocruzpe.Utils.JsonUtils;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.User;

public class PreferencesManager {

    private static final String PREF_SELECTED_HOME_MENU_OPT = "PREF_SELECTED_HOME_MENU_OPT";
    private static final String PREF_CURRENT_USER = "PREF_CURRENT_USER";
    private static final String PREF_SHARED_CONTENT = "PREF_SHARED_CONTENT";
    private static final String PREF_DEFAULT_LANG = "PREF_DEFAULT_LANG";
    private static final String PREF_SETTING_MAX_NUM_STORED_SEARCHES = "PREF_SETTING_MAX_NUM_STORED_SEARCHES";
    private static final String PREF_IS_LANDING_ON_INITIAL_PAGE = "PREF_IS_LANDING_ON_INITIAL_PAGE";

    private static SharedPreferences sPreferences;


    public static void initGlobalPreferences(final Context context) {
        sPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * CRUD methods
     **/
    public static void setSelectedHomeMenuOption(int menuOptionIdx) {
        setIntPref(PREF_SELECTED_HOME_MENU_OPT, menuOptionIdx);
    }

    public static int getSelectedHomeMenuOption() {
        return getIntPref(PREF_SELECTED_HOME_MENU_OPT, ConstantUtils.UNSELECTED_TAB_IDX);
    }

    public static void setCurrentUser(User user) {

        if (user != null) {
            String userJson = JsonUtils.toJson(user);
            setStringPref(PREF_CURRENT_USER, userJson);
        } else {
            setStringPref(PREF_CURRENT_USER, "");
        }
    }

    public static User getCurrentUser() {
        User user = null;
        String userJson = getStringPref(PREF_CURRENT_USER, "");

        if (!userJson.isEmpty()) {
            user = JsonUtils.fromJson(userJson, User.class);
        }

        if (user == null) {
            user = ConstantUtils.ANONYMOUS_USER;
        }

        return user;
    }


    public static void setSharedContent(SharedContent sharedContent) {

        if (sharedContent != null) {
            String contentJson = JsonUtils.toJson(sharedContent);
            setStringPref(PREF_SHARED_CONTENT, contentJson);
        } else {
            setStringPref(PREF_SHARED_CONTENT, "");
        }
    }

    public static SharedContent getSharedContent() {
        SharedContent sharedContent = null;
        String contentJson = getStringPref(PREF_SHARED_CONTENT, "");

        if (!contentJson.isEmpty()) {
            sharedContent = JsonUtils.fromJson(contentJson, SharedContent.class);
        }

        return sharedContent;
    }


    public static void setDefaultLang(String lang) {
        setStringPref(PREF_DEFAULT_LANG, lang);
    }

    public static String getDefaultLang() {
        return getStringPref(PREF_DEFAULT_LANG, "");
    }

    public static void setSettingMaxNumStoredSearches(int maxNumStoredSearches) {
        setIntPref(PREF_SETTING_MAX_NUM_STORED_SEARCHES, maxNumStoredSearches);
    }

    public static int getSettingMaxNumStoredSearches() {
        return getIntPref(PREF_SETTING_MAX_NUM_STORED_SEARCHES,
                ConstantUtils.SETTINGS_DEFAULT_VAL_MAX_NUM_STORED_SEARCHES);
    }

    public static void setIsLandingOnInitialPage(boolean isLanding) {
        setBooleanPref(PREF_IS_LANDING_ON_INITIAL_PAGE, isLanding);
    }

    public static boolean isLandingOnInitialPage() {
        return getBooleanPref(PREF_IS_LANDING_ON_INITIAL_PAGE);
    }

    /**
     * Base methods
     **/
    private static String getStringPref(final String key, final String defaultValue) {
        return sPreferences.getString(key, defaultValue);
    }

    private static void setStringPref(final String key, final String value) {
        sPreferences
                .edit()
                .putString(key, value)
                .apply();
    }

    private static int getIntPref(final String key, final int defaultValue) {
        return sPreferences.getInt(key, defaultValue);
    }

    private static void setIntPref(final String key, final int value) {
        sPreferences
                .edit()
                .putInt(key, value)
                .apply();
    }

    private static boolean getBooleanPref(final String key) {
        return sPreferences.getBoolean(key, false);
    }

    private static void setBooleanPref(final String key, final boolean value) {
        sPreferences
                .edit()
                .putBoolean(key, value)
                .apply();
    }

}