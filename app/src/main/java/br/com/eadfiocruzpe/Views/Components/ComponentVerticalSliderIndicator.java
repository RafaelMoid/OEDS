package br.com.eadfiocruzpe.Views.Components;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Contracts.ComponentVerticalSliderIndicatorContract;
import br.com.eadfiocruzpe.Utils.LogUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;

public class ComponentVerticalSliderIndicator {

    private final String TAG = "CVertSliderInd";

    public RelativeLayout rootLayout;
    private ImageView mSliderIndicator;

    private LogUtils mLogUtils;

    private ComponentVerticalSliderIndicatorContract mCallback;

    ComponentVerticalSliderIndicator(RelativeLayout rootLayout,
                                     ComponentVerticalSliderIndicatorContract callback) {
        mCallback = callback;

        initUI(rootLayout);
        initTools();
        initEvents();
    }

    /**
     * Initialization
     */
    private void initUI(RelativeLayout rootLayout) {
        bindViews(rootLayout);
    }

    private void bindViews(RelativeLayout rootLayout) {
        try {
            this.rootLayout = rootLayout;

            mSliderIndicator = this.rootLayout.findViewById(R.id.vertical_slider_indicator_button);

            show(false);

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to bindViews");
        }
    }

    private void initTools() {
        mLogUtils = new LogUtils();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initEvents() {

        if (mTouchListenerScrollIndicator == null) {
            bindTouchListenerScrollerIndicator();
            mSliderIndicator.setOnTouchListener(mTouchListenerScrollIndicator);
        }
    }

    /**
     * Events
     */
    private View.OnTouchListener mTouchListenerScrollIndicator = null;

    private void bindTouchListenerScrollerIndicator() {

        mTouchListenerScrollIndicator = new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int action = motionEvent.getActionMasked();

                switch (action) {

                    case MotionEvent.ACTION_MOVE: {
                        // Inform the callback method about the change and update the position of the
                        // Slider indicator
                        informIndicatorMotion();

                        break;
                    }

                }

                return true;
            }

        };
    }

    private void informIndicatorMotion() {

        if (mCallback != null) {
            mCallback.onVSIFinishedSliding(getPositionSliderIndicator());
        }
    }

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

    void setPositionSliderIndicator(float yPercentageTop) {
        try {

            if (mSliderIndicator.getParent() != null && yPercentageTop < 80) {
                float parentHeight = ((View) mSliderIndicator.getParent()).getHeight();
                float newY = parentHeight * (yPercentageTop * 0.0095f);

                mSliderIndicator.setY(newY);
            }

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to setPositionSliderIndicator");
        }
    }

    private float getPositionSliderIndicator() {

        if (mCallback != null && mSliderIndicator.getParent() != null) {
            float parentHeight = ((View) mSliderIndicator.getParent()).getHeight();
            float yPosPercentFromTop = (mSliderIndicator.getY() * 100) / parentHeight;

            if (yPosPercentFromTop > 100) {
                yPosPercentFromTop = 100;

            } else if (yPosPercentFromTop < 0) {
                yPosPercentFromTop = 0;
            }

            return yPosPercentFromTop;
        }

        return 0;
    }

}