package br.com.eadfiocruzpe.Views.Pages.Home.Settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Contracts.SettingsContract;
import br.com.eadfiocruzpe.Presenters.SettingsPresenter;
import br.com.eadfiocruzpe.Utils.TypeUtils;
import br.com.eadfiocruzpe.Views.Adapters.HomePagerAdapter;
import br.com.eadfiocruzpe.Views.Pages.Home.BaseFragment;
import br.com.eadfiocruzpe.Views.Pages.Home.HomeActivity;

public class SettingsFragment extends BaseFragment implements
        SettingsContract.View {

    private static final String TAG = "SettingsFragment";

    @BindView(R.id.settings_page_msg_about_project)
    public TextView settingsMsgAboutProject;
    @BindView(R.id.settings_page_msg_about_siops)
    public TextView settingsMsgAboutSiops;
    @BindView(R.id.settings_page_msg_about_fiocruz)
    public TextView settingsMsgAboutFiocruz;
    @BindView(R.id.settings_page_msg_contact)
    public TextView settingsMsgContact;
    @BindView(R.id.settings_page_msg_credits)
    public TextView settingsMsgCredits;

    private SettingsPresenter mSettingsPresenter;

    /**
     * Initialization
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    public void init() {
        initViews();
        initData();
    }

    private void initViews() {
        initMainContainer();
        initTextViews();
    }

    private void initMainContainer() {
        try {
            ((HomeActivity)getActivity()).updateBackgroundRootView(
                    HomePagerAdapter.IDX_SETTINGS_OPTION,
                    false,
                    R.color.color_white,
                    R.color.color_white);

        } catch (Exception ignored) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to initMainContainer");
        }
    }

    private void initTextViews() {
        try {
            settingsMsgAboutProject.setText(Html.fromHtml(getString(R.string.settings_page_lbl_know_more)));
            settingsMsgAboutSiops.setText(Html.fromHtml(getString(R.string.settings_page_lbl_know_more)));
            settingsMsgAboutFiocruz.setText(Html.fromHtml(getString(R.string.settings_page_lbl_know_more)));
            settingsMsgContact.setText(Html.fromHtml(getString(R.string.settings_page_txt_about_contact)));
            settingsMsgCredits.setText(Html.fromHtml(getString(R.string.settings_page_txt_about_credits)));

        } catch (Exception ignored) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to initTextViews");
        }
    }

    private void initData() {
        mSettingsPresenter = new SettingsPresenter(getContext().getApplicationContext());
        mSettingsPresenter.bindView(this);
    }

    /**
     * Events
     */
    @OnClick({R.id.settings_page_msg_about_project})
    public void navigateAboutProject() {
        navigateUrl(getString(R.string.settings_page_url_project));
    }

    @OnClick({R.id.settings_page_msg_about_siops})
    public void navigateAboutSiops() {
        navigateUrl(getString(R.string.settings_page_url_siops));
    }

    @OnClick({R.id.settings_page_msg_about_fiocruz})
    public void navigateAboutFiocruz() {
        navigateUrl(getString(R.string.settings_page_url_fiocruz));
    }

    @OnClick({R.id.settings_page_msg_contact})
    public void navigateContact() {
        navigateUrl(getString(R.string.settings_page_url_contact));
    }

    @OnClick({R.id.settings_page_msg_credits})
    public void navigateCredits() {
        navigateUrl(getString(R.string.settings_page_url_credits));
    }

    @OnClick({R.id.settings_page_btn_evaluate_app})
    public void navigateEvaluateApp() {
        //Todo: Use the Google Play URL once the app get released >> openAppPageGooglePlay();
        navigateUrl(getString(R.string.settings_page_url_project));
    }

    @OnClick({R.id.settings_page_btn_recommend_app})
    public void navigateRecommendApp() {
        recommendApp();
    }

    /**
     * Navigation
     */
    private void navigateUrl(String url) {
        try {
            Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse(url));
            startActivity(viewIntent);

        } catch (Exception e) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to navigateUrl");
        }
    }

    //Todo: Use the Google Play URL once the app get released >> openAppPageGooglePlay();
    private void openAppPageGooglePlay() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getAppLinkMarket())));

        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getAppLinkGooglePlay())));
        }
    }

    private void recommendApp() {
        try {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.settings_page_url_project));
            sendIntent.setType("text/plain");
            startActivity(sendIntent);

        } catch (Exception e) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to recommendApp");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Getters and Setters
     */
    private String getAppLinkMarket() {
        return getString(R.string.settings_page_prefix_market) + getString(R.string.settings_page_package_name);
    }

    private String getAppLinkGooglePlay() {
        return getString(R.string.settings_page_prefix_google_play) + getString(R.string.settings_page_package_name);
    }

    /**
     * Messaging
     */
    @Override
    public void onShowMessage(String message) {
        try {

            if (getActivity() != null) {
                ((HomeActivity) getActivity()).showMessage(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onShowProgress(boolean show) {
        try {

            if (getActivity() != null) {
                ((HomeActivity) getActivity()).showProgress(show);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Finalization
     */
    @Override
    public void onStop() {

        if (mSettingsPresenter != null) {
            mSettingsPresenter.unbindView();
        }

        super.onStop();
    }

}