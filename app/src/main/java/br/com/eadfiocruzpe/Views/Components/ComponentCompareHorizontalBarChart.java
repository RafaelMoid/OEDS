package br.com.eadfiocruzpe.Views.Components;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Utils.ConstantUtils;
import br.com.eadfiocruzpe.Utils.ImageUtils;
import br.com.eadfiocruzpe.Utils.LogUtils;
import br.com.eadfiocruzpe.Utils.StringUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;
import br.com.eadfiocruzpe.Utils.UIUtils;
import br.com.eadfiocruzpe.Views.Validators.BasicValidators;

public class ComponentCompareHorizontalBarChart {

    private final String TAG = "CCompareHorBarChart";

    public FrameLayout rootLayout;
    private LinearLayout mMainContainer;
    private ImageView mIcon;
    private TextView mMsg;
    private LinearLayout mBarsContainer;
    private FrameLayout mBarA;
    private FrameLayout mBarB;
    private TextView mBarAValue;
    private TextView mBarBValue;
    private TextView mLegendA;
    private TextView mLegendB;

    private Resources mResources;
    private LogUtils mLogUtils;

    public ComponentCompareHorizontalBarChart(FrameLayout rootLayout, Resources resources,
                                              String description) {
        initUI(rootLayout);
        initTools(resources);
        initData(description);
    }

    /**
     * Initialization
     */
    private void initUI(FrameLayout rootLayout) {
        try {
            this.rootLayout = rootLayout;

            mMainContainer = this.rootLayout.findViewById(
                    R.id.component_compare_horizontal_bar_chart_container);
            mIcon = this.rootLayout.findViewById(
                    R.id.component_compare_horizontal_bar_chart_img);
            mMsg = this.rootLayout.findViewById(
                    R.id.component_compare_horizontal_bar_chart_container_header_msg);
            mBarsContainer = this.rootLayout.findViewById(
                    R.id.component_compare_horizontal_bar_chart_a_b_container);
            mBarA = this.rootLayout.findViewById(
                    R.id.component_compare_horizontal_bar_chart_a);
            mBarB = this.rootLayout.findViewById(
                    R.id.component_compare_horizontal_bar_chart_b);
            mBarAValue = this.rootLayout.findViewById(
                    R.id.component_compare_horizontal_bar_chart_a_value);
            mBarBValue = this.rootLayout.findViewById(
                    R.id.component_compare_horizontal_bar_chart_b_value);
            mLegendA = this.rootLayout.findViewById(
                    R.id.component_compare_horizontal_bar_chart_a_legend);
            mLegendB = this.rootLayout.findViewById(
                    R.id.component_compare_horizontal_bar_chart_b_legend);

            show(false);

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to initUI");
        }
    }

    private void initTools(Resources resources) {
        mLogUtils = new LogUtils();
        mResources = resources;
    }

    private void initData(String description) {
        try {
            String msg = String.format(
                    mResources.getString(R.string.component_compare_horizontal_bar_chart_loading),
                    description);

            setMsg(msg);

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to initData");
        }
    }

    /**
     * Events
     */
    public void show(boolean showComponent) {

        if (rootLayout != null) {
            rootLayout.setVisibility(showComponent ? View.VISIBLE : View.GONE);
            mMainContainer.setVisibility(showComponent ? View.VISIBLE : View.GONE);
        }
    }

    public void loadComponent(Context context, int icoId, String msg, float aBarValue,
                              float bBarValue, String aLegend, String bLegend) {
        try {
            BasicValidators evalUtils = new BasicValidators();

            if (evalUtils.isValidString(msg) &&
                evalUtils.isValidString(aLegend) &&
                evalUtils.isValidString(bLegend) &&
                (aBarValue >= 0 && bBarValue >= 0)) {

                show(true);
                setIcon(context, icoId);
                setMsg(msg);
                setBarValues(context, aBarValue, bBarValue);
                setBarLegends(aLegend, bLegend);

            } else {
                show(false);
                setMsg(mResources.getString((R.string.component_compare_horizontal_bar_chart_failed_load)));
                mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                        TAG + "Failed to loadComponent");
            }

        } catch (Exception e) {
            show(false);
            setMsg(mResources.getString((R.string.component_compare_horizontal_bar_chart_failed_load)));
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to loadComponent");
        }
    }

    /**
     * Setters and Getters
     */
    private void setIcon(Context context, int iconId) {
        try {
            ImageUtils.loadImage(context, iconId, mIcon);

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,TAG + "Failed to setIcon");
        }
    }

    public void setMsg(String msg) {
        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mMsg.setText(Html.fromHtml(msg, Html.FROM_HTML_MODE_COMPACT));
            } else {
                mMsg.setText(Html.fromHtml(msg));
            }

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,TAG + "Failed to setMsg");
        }
    }

    private void setBarValues(Context context, double aBarValue, double bBarValue) {
        try {
            final float THRESHOLD_IN_PERCENTAGE_SWITCH_VALUE_COLOR_ = 0.5f;
            final int MIN_HORIZONTAL_BAR_WIDTH_DP = 70;

            StringUtils strUtils = new StringUtils(mResources);

            // Update bars
            double sizeFullPercentage = mBarsContainer.getMeasuredWidth();

            if (sizeFullPercentage == 0) {
                sizeFullPercentage = UIUtils.dpToPx(mResources, MIN_HORIZONTAL_BAR_WIDTH_DP);
            }

            double percentageA;
            double percentageB;

            if (aBarValue > bBarValue) {
                percentageA = 1;
                percentageB = (bBarValue * 100 / aBarValue) / 100;

                // Fix the color of the values if there is a huge gap between the percentages
                if ((percentageA - percentageB) > THRESHOLD_IN_PERCENTAGE_SWITCH_VALUE_COLOR_) {
                    mBarBValue.setTextColor(ContextCompat.getColor(context, R.color.color_blue_4));
                    mBarBValue.setGravity(Gravity.END);
                } else {
                    mBarBValue.setTextColor(ContextCompat.getColor(context, R.color.color_white));
                    mBarBValue.setGravity(Gravity.START);
                }

                mBarAValue.setTextColor(ContextCompat.getColor(context, R.color.color_white));
                mBarAValue.setGravity(Gravity.START);

            } else {
                percentageB = 1;
                percentageA = (aBarValue * 100 / bBarValue) / 100;

                // Fix the color of the values if there is a huge gap between the percentages
                if ((percentageB - percentageA) > THRESHOLD_IN_PERCENTAGE_SWITCH_VALUE_COLOR_) {
                    mBarAValue.setTextColor(ContextCompat.getColor(context, R.color.color_green_blue_4));
                    mBarAValue.setGravity(Gravity.END);
                } else {
                    mBarAValue.setTextColor(ContextCompat.getColor(context, R.color.color_white));
                    mBarAValue.setGravity(Gravity.START);
                }

                mBarBValue.setTextColor(ContextCompat.getColor(context, R.color.color_white));
                mBarBValue.setGravity(Gravity.START);
            }

            ValueAnimator animBarA = ValueAnimator.ofInt(
                    mBarA.getMeasuredWidth(),
                    (int) (percentageA * sizeFullPercentage));
            animBarA.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int val = (Integer) valueAnimator.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = mBarA.getLayoutParams();
                    layoutParams.width = val;
                    mBarA.setLayoutParams(layoutParams);
                }
            });
            animBarA.setDuration(ConstantUtils.TIMEOUT_ANIMATIONS_COMPARISON);
            animBarA.start();

            ValueAnimator animBarB = ValueAnimator.ofInt(
                    mBarB.getMeasuredWidth(),
                    (int) (percentageB * sizeFullPercentage));
            animBarB.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int val = (Integer) valueAnimator.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = mBarB.getLayoutParams();
                    layoutParams.width = val;
                    mBarB.setLayoutParams(layoutParams);
                }
            });
            animBarB.setDuration(ConstantUtils.TIMEOUT_ANIMATIONS_COMPARISON);
            animBarB.start();

            // Update values
            mBarAValue.setText(aBarValue == 0?
                    mResources.getString(R.string.lbl_non_informed) :
                    strUtils.getMoneyStringFromVal(aBarValue));

            mBarBValue.setText(bBarValue == 0?
                    mResources.getString(R.string.lbl_non_informed) :
                    strUtils.getMoneyStringFromVal(bBarValue));

        } catch (Exception e) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to setBarValues");
        }
    }

    private void setBarLegends(String legendA, String legendB) {
        try {
            mLegendA.setText(legendA);
            mLegendB.setText(legendB);

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to setBarLegends");
        }
    }

}