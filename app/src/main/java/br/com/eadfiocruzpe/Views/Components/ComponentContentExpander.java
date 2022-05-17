package br.com.eadfiocruzpe.Views.Components;

import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.os.Build;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Contracts.ComponentContentExpanderContract;
import br.com.eadfiocruzpe.Utils.LogUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;

public class ComponentContentExpander {

    private final String TAG = "CContExp";

    private ComponentContentExpanderContract mCallback;

    public FrameLayout rootLayout;
    private View mHeaderContainer;
    private TextView mTitle;
    private ImageView mBtnToggleContentVisibility;
    private TextView mContentTextView;
    private View mBottomLine;

    private LogUtils mLogUtils;
    private boolean mShowBottomLine = true;

    public ComponentContentExpander(FrameLayout rootLayout, ComponentContentExpanderContract callback,
                                    String title, String info) {
        mCallback = callback;

        initUI(rootLayout);
        initTools();
        initEvents();
        initData(title, info);
    }

    /**
     * Initialization
     */
    private void initUI(FrameLayout rootLayout) {
        bindViews(rootLayout);
        toggleContentVisibility();
    }

    private void bindViews(FrameLayout rootLayout) {
        try {
            this.rootLayout = rootLayout;

            mHeaderContainer = this.rootLayout.findViewById(R.id.component_content_expander_header);
            mTitle = this.rootLayout.findViewById(R.id.component_content_expander_title);
            mBtnToggleContentVisibility = this.rootLayout.findViewById(R.id.component_content_expander_btn_toggle_content);
            mContentTextView = this.rootLayout.findViewById(R.id.component_content_expander_content);
            mBottomLine = this.rootLayout.findViewById(R.id.component_content_expander_bottom_line);

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to bindViews");
        }
    }

    private void initTools() {
        mLogUtils = new LogUtils();
    }

    private void initEvents() {

        if (mOnClickBtnToggleContentVisibility == null) {
            bindOnClickListenerToggleBtn();
        }
    }

    /**
     * Events
     */
    private View.OnClickListener mOnClickBtnToggleContentVisibility = null;

    private void bindOnClickListenerToggleBtn() {
        mOnClickBtnToggleContentVisibility = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    toggleContentVisibility();

                } catch (Exception e) {
                    mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                            TAG + "Failed to toggle visibility of the content container");
                }
            }
        };

        mTitle.setOnClickListener(mOnClickBtnToggleContentVisibility);
        mBtnToggleContentVisibility.setOnClickListener(mOnClickBtnToggleContentVisibility);
    }

    private void toggleContentVisibility() {

        if (mContentTextView.getVisibility() == View.VISIBLE) {
            closeContent(true);
        } else {
            openContent();
        }
    }

    private void openContent() {
        try {

            if (mContentTextView.getVisibility() == View.GONE) {
                mContentTextView.setVisibility(View.VISIBLE);

                if (!mContentTextView.getText().toString().isEmpty()) {

                    ObjectAnimator imageViewObjectAnimator = ObjectAnimator.ofFloat(
                            mBtnToggleContentVisibility, "rotation", 0f, 180f);
                    imageViewObjectAnimator.setDuration(500);
                    imageViewObjectAnimator.start();

                    if (mCallback != null) {
                        mCallback.onContentToggled(true, getTitle());
                    }

                    hideShowBottomLine(true);
                }
            }

        } catch (Exception e) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to closeContent");
        }
    }

    public void closeContent(boolean animate) {
        try {

            if (mContentTextView.getVisibility() == View.VISIBLE) {
                mContentTextView.setVisibility(View.GONE);

                if (animate) {
                    ObjectAnimator imageViewObjectAnimator = ObjectAnimator.ofFloat(
                            mBtnToggleContentVisibility, "rotation", 180f, 0f);
                    imageViewObjectAnimator.setDuration(500);
                    imageViewObjectAnimator.start();
                } else {
                    mBtnToggleContentVisibility.setRotation(0);
                }

                hideShowBottomLine(false);
            }

        } catch (Exception e) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to closeContent");
        }
    }

    /**
     * Data
     */
    private void initData(String title, String info) {
        setTitle(title);
        setContent(info);
    }

    /**
     * Setters and Getters
     */
    public void show(boolean showComponent) {

        if (rootLayout != null) {
            rootLayout.setVisibility(showComponent ? View.VISIBLE : View.GONE);
        }
    }

    public void setTitle(String title) {

        if (mTitle != null) {
            mTitle.setText(title);
        }
    }

    public String getTitle() {

        if (mTitle != null) {
            return mTitle.getText().toString();
        } else {
            return "";
        }
    }

    public void setContent(String content) {

        if (mContentTextView != null) {
            mContentTextView.setText(content);
        }
    }

    public void setBackgroundColor(int color, Resources resources) {

        if (mHeaderContainer != null) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mHeaderContainer.setBackground(resources.getDrawable(color));

            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                mHeaderContainer.setBackground(resources.getDrawable(color));
            }
        }
    }

    public void setShowBottomLine(boolean show) {
        mShowBottomLine = show;
    }

    private boolean getShowBottomLine() {
        return mShowBottomLine;
    }

    public void hideShowBottomLine(boolean forceHide) {

        if (mBottomLine != null) {

            if (!forceHide) {
                mBottomLine.setVisibility(getShowBottomLine()? View.VISIBLE : View.GONE);
            } else {
                mBottomLine.setVisibility(View.GONE);
            }
        }
    }

}