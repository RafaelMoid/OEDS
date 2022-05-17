package br.com.eadfiocruzpe.Presenters;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;

import br.com.eadfiocruzpe.Contracts.SplashContract;
import br.com.eadfiocruzpe.Persistence.PreferencesManager;
import br.com.eadfiocruzpe.Utils.ConstantUtils;
import br.com.eadfiocruzpe.Utils.LogUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;

public class SplashPresenter extends BasePresenter<SplashContract.View> implements
        SplashContract.Presenter {

    private final String TAG = "SplashPresenter";
    private LogUtils mLogUtils;


    public SplashPresenter(Context context) {
        mContext = context;
        mLogUtils = new LogUtils(context.getApplicationContext());
    }

    @Override
    public void init() {
        makeAsyncRequestWakeService();
        initInitializationProcess();
    }

    private void makeAsyncRequestWakeService() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {

                new CountDownTimer(ConstantUtils.TIMEOUT_PRESENT_DASHBOARD, 100) {

                    public void onTick(long millisUntilFinished) {}

                    public void onFinish() {
                        try {
                            SplashContract.View view = getView();

                            if (view != null) {
                                view.navigateToHomePage();
                            }

                        } catch (Exception e) {
                            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                                    TAG +" Failed to makeAsyncRequestWakeService");
                        }
                    }
                }.start();

            }
        });

    }

    private void initInitializationProcess() {
        try {
            PreferencesManager.setIsLandingOnInitialPage(true);
        } catch (Exception e) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG +" Failed to initInitializationProcess");
        }
    }

}