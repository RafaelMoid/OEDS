package br.com.eadfiocruzpe.Views.Components;

import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;

import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Utils.LogUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;

public class ComponentShareableContentSelector {

    public FrameLayout rootLayout;
    private View mContentView;
    private CheckBox mContentSelector;
    private String mContentId;

    private LogUtils mLogUtils;

    private boolean mHideLockSelector;

    public ComponentShareableContentSelector(FrameLayout rootLayout, View contentView, String contentId) {
        initTools();
        initUI(rootLayout, contentView);
        initData(contentId);
    }

    /**
     * Initialization
     */
    private void initTools() {
        mLogUtils = new LogUtils();
    }

    private void initUI(FrameLayout rootLayout, View contentView) {
        mContentView = contentView;
        bindViews(rootLayout);
        lockSelectorVisibility(false);
    }

    private void bindViews(FrameLayout rootLayout) {
        try {
            this.rootLayout = rootLayout;

            mContentSelector =  this.rootLayout.findViewById(R.id.component_shareable_content_picker_checkbox);

            show(false);

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, "ComponentSCS Failed to bindViews");
        }
    }

    private void initData(String contentId) {
        setContentId(contentId);
    }

    /**
     * Setters and Getters
     */
    private void setContentId(String contentId) {
        mContentId = contentId;
    }

    String getContentId() {
        return mContentId;
    }

    View getSelectedView() {
        return mContentView;
    }

    public void show(boolean showComponent) {

        if (rootLayout != null) {
            rootLayout.setVisibility(showComponent ? View.VISIBLE : View.GONE);
        }
    }

    boolean isSelectorChecked() {
        return mContentSelector != null && mContentSelector.isChecked();
    }

    void lockSelectorVisibility(boolean lock) {
        mHideLockSelector = lock;
    }

    boolean isSelectorVisibilityLocked() {
        return mHideLockSelector;
    }

}