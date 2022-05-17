package br.com.eadfiocruzpe.Views.Pages.Splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.ImageView;

import butterknife.ButterKnife;
import butterknife.BindView;

import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Contracts.SplashContract;
import br.com.eadfiocruzpe.Presenters.SplashPresenter;
import br.com.eadfiocruzpe.Views.Pages.Global.BaseActivity;
import br.com.eadfiocruzpe.Views.Pages.Home.HomeActivity;

public class SplashActivity extends BaseActivity implements
        SplashContract.View {

    @BindView(R.id.root)
    public CoordinatorLayout rootView;

    @BindView(R.id.progress_bar_container)
    public View progressBarContainer;

    @BindView(R.id.splash_logo)
    public ImageView logoImgView;

    private SplashPresenter mSplashPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        initPresenters();
        initViews();
    }

    private void initPresenters() {
        mSplashPresenter = new SplashPresenter(getApplicationContext());
    }

    private void initViews() {
        initMainView(rootView);
        initProgressView(progressBarContainer);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        mSplashPresenter.bindView(this);
        mSplashPresenter.init();
    }

    /**
     * Events
     */
    @Override
    public void navigateToHomePage() {
        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    /**
     * Messaging
     */
    @Override
    public void onShowMessage(String message) {

        if (!isFinishing()) {

            try {
                showMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onShowProgress(boolean show) {

        if (!isFinishing()) {

            try {
                showProgress(show);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Finalization
     */
    @Override
    protected void onStop() {
        super.onStop();

        if (mSplashPresenter != null) {
            mSplashPresenter.unbindView();
        }
    }

}