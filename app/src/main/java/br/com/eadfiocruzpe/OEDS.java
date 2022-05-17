package br.com.eadfiocruzpe;

import android.app.Application;
import android.arch.persistence.room.Room;

import br.com.eadfiocruzpe.Persistence.DB;
import br.com.eadfiocruzpe.Persistence.DBManager;
import br.com.eadfiocruzpe.Persistence.PreferencesManager;
import br.com.eadfiocruzpe.Utils.ConstantUtils;
import br.com.eadfiocruzpe.Utils.LogUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;

import java.util.Locale;

public class OEDS extends Application {

    private final String TAG = "OEDS";
    private LogUtils mLogUtils = new LogUtils();

    @Override
    public void onCreate() {
        super.onCreate();

        mLogUtils.logMessage(TypeUtils.LogMsgType.REGULAR, TAG + "Initialized OEDS app.");

        initLocalDatabaseSingleton();
        configUserPreferences();
    }

    private void initLocalDatabaseSingleton() {
        DB db = Room.databaseBuilder(getApplicationContext(), DB.class, ConstantUtils.LDB_NAME).build();
        DBManager.setDBManager(db);
    }

    private void configUserPreferences() {
        PreferencesManager.initGlobalPreferences(getApplicationContext());

        try {
            PreferencesManager.setDefaultLang(ConstantUtils.LANG_PT);
        } catch (NullPointerException e) {
            PreferencesManager.setDefaultLang(Locale.getDefault().getDisplayLanguage());
        }
    }

    @Override
    public void onTerminate() {
        PreferencesManager.setSelectedHomeMenuOption(ConstantUtils.UNSELECTED_TAB_IDX);
        mLogUtils.logMessage(TypeUtils.LogMsgType.REGULAR, TAG + "Finished app.");

        super.onTerminate();
    }

}