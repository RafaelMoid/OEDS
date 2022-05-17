package br.com.eadfiocruzpe.Views.Components;

import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Utils.ConstantUtils;
import br.com.eadfiocruzpe.Utils.LogUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;
import br.com.eadfiocruzpe.Contracts.ComponentSocialNetworkPickerContract;

public class ComponentSocialNetworkPicker {

    public FrameLayout rootLayout;
    private View mContentContainer;
    private View mContainerSocialBtns;
    private View mContainerInstructions;
    private ImageView mBtnFacebook;
    private ImageView mBtnEmail;
    private ImageView mBtnTwitter;
    private ImageView mBtnWhatsApp;
    private ImageView mBtnMessenger;
    private Button mBtnStart;

    private LogUtils mLogUtils;
    private ComponentSocialNetworkPickerContract mCallback;

    private int mSelectedSocialNetworkId = -1;

    public ComponentSocialNetworkPicker(FrameLayout rootLayout,
                                 ComponentSocialNetworkPickerContract callback) {
        mCallback = callback;

        initTools();
        initUI(rootLayout);
        initEvents();
    }

    /**
     * Initialization
     */
    private void initUI(FrameLayout rootLayout) {
        bindViews(rootLayout);
    }

    private void bindViews(FrameLayout rootLayout) {
        try {
            this.rootLayout = rootLayout;

            mContentContainer = this.rootLayout.findViewById(R.id.component_social_network_picker);
            mContainerSocialBtns = this.rootLayout.findViewById(R.id.component_social_network_picker_social_buttons);
            mContainerInstructions = this.rootLayout.findViewById(R.id.component_social_network_picker_sharing_instructions);

            mBtnFacebook = this.rootLayout.findViewById(R.id.component_social_network_picker_facebook);
            mBtnEmail = this.rootLayout.findViewById(R.id.component_social_network_picker_email);
            mBtnTwitter = this.rootLayout.findViewById(R.id.component_social_network_picker_twitter);
            mBtnWhatsApp = this.rootLayout.findViewById(R.id.component_social_network_picker_whatsapp);
            mBtnMessenger = this.rootLayout.findViewById(R.id.component_social_network_picker_messenger);
            mBtnStart = this.rootLayout.findViewById(R.id.component_social_network_picker_btn_start);

            show(false);
            showContainerInstructions(true);

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, "ComponentSNP Failed to bindViews");
        }
    }

    private void initTools() {
        mLogUtils = new LogUtils();
    }

    private void initEvents() {

        if (mClickListenerSocialNetwork == null) {
            bindTouchListenerScrollerIndicator();

            mContentContainer.setOnClickListener(mBackgroundClickListener);

            mBtnFacebook.setOnClickListener(mClickListenerSocialNetwork);
            mBtnEmail.setOnClickListener(mClickListenerSocialNetwork);
            mBtnTwitter.setOnClickListener(mClickListenerSocialNetwork);
            mBtnWhatsApp.setOnClickListener(mClickListenerSocialNetwork);
            mBtnMessenger.setOnClickListener(mClickListenerSocialNetwork);

            mBtnStart.setOnClickListener(mClickListenerStartSharingProcess);
        }
    }

    /**
     * Events
     */
    private View.OnClickListener mClickListenerSocialNetwork = null;

    private void bindTouchListenerScrollerIndicator() {

        mClickListenerSocialNetwork = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();

                switch (id) {

                    case R.id.component_social_network_picker_facebook:{
                        pickSocialNetwork(ConstantUtils.SOCIAL_NET_FACEBOOK);
                        break;
                    }

                    case R.id.component_social_network_picker_email:{
                        pickSocialNetwork(ConstantUtils.SOCIAL_NET_EMAIL);
                        break;
                    }

                    case R.id.component_social_network_picker_twitter:{
                        pickSocialNetwork(ConstantUtils.SOCIAL_NET_TWITTER);
                        break;
                    }

                    case R.id.component_social_network_picker_whatsapp:{
                        pickSocialNetwork(ConstantUtils.SOCIAL_NET_WHATSAPP);
                        break;
                    }

                    case R.id.component_social_network_picker_messenger:{
                        pickSocialNetwork(ConstantUtils.SOCIAL_NET_MESSENGER);
                        break;
                    }

                }
            }
        };
    }

    private void pickSocialNetwork(int socialNetId) {

        if (mCallback != null) {
            mSelectedSocialNetworkId = socialNetId;
            mCallback.onSocialNetworkPicked(socialNetId);
        }
    }

    public int getSelectedSocialNetworkId() {
        return mSelectedSocialNetworkId;
    }

    private final View.OnClickListener mClickListenerStartSharingProcess = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (mCallback != null) {
                mCallback.onClickBtnStart();
            }
        }
    };

    private final View.OnClickListener mBackgroundClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            show(false);
        }
    };

    /**
     * Setters and Getters
     */
    public void show(boolean showComponent) {

        if (rootLayout != null) {
            rootLayout.setVisibility(showComponent ? View.VISIBLE : View.GONE);
        }
    }

    public boolean isShowing() {
        return rootLayout != null && rootLayout.getVisibility() == View.VISIBLE;
    }

    public void showContainerInstructions(boolean showContainerInstructions) {

        if (mContainerInstructions != null && mContainerSocialBtns != null) {
            mContainerInstructions.setVisibility(showContainerInstructions? View.VISIBLE : View.GONE);
            mContainerSocialBtns.setVisibility(showContainerInstructions? View.GONE : View.VISIBLE);
        }
    }

}