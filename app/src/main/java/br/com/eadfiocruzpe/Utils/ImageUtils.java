package br.com.eadfiocruzpe.Utils;

import java.util.HashMap;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Views.Components.CircleTransform;

public class ImageUtils {

    private static final String TAG = "ImageUtils";

    public static HashMap<String, Integer> mapStateFlagImg = new HashMap<String, Integer>(){{
        put("ac", R.drawable.flag_state_ac);
        put("al", R.drawable.flag_state_al);
        put("ap", R.drawable.flag_state_ap);
        put("am", R.drawable.flag_state_am);
        put("ba", R.drawable.flag_state_ba);
        put("ce", R.drawable.flag_state_ce);
        put("df", R.drawable.flag_state_df);
        put("es", R.drawable.flag_state_es);
        put("go", R.drawable.flag_state_go);
        put("ma", R.drawable.flag_state_ma);
        put("mt", R.drawable.flag_state_mt);
        put("ms", R.drawable.flag_state_ms);
        put("mg", R.drawable.flag_state_mg);
        put("pa", R.drawable.flag_state_pa);
        put("pb", R.drawable.flag_state_pb);
        put("pr", R.drawable.flag_state_pr);
        put("pe", R.drawable.flag_state_pe);
        put("pi", R.drawable.flag_state_pi);
        put("rj", R.drawable.flag_state_rj);
        put("rn", R.drawable.flag_state_rn);
        put("rs", R.drawable.flag_state_rs);
        put("ro", R.drawable.flag_state_ro);
        put("rr", R.drawable.flag_state_rr);
        put("sc", R.drawable.flag_state_sc);
        put("sp", R.drawable.flag_state_sp);
        put("se", R.drawable.flag_state_se);
        put("to", R.drawable.flag_state_to);

        put("acre", R.drawable.flag_state_ac);
        put("alagoas", R.drawable.flag_state_al);
        put("amapá", R.drawable.flag_state_ap);
        put("amazonas", R.drawable.flag_state_am);
        put("bahia", R.drawable.flag_state_ba);
        put("ceará", R.drawable.flag_state_ce);
        put("distrito federal", R.drawable.flag_state_df);
        put("espírito santo", R.drawable.flag_state_es);
        put("goiás", R.drawable.flag_state_go);
        put("maranhão", R.drawable.flag_state_ma);
        put("mato grosso", R.drawable.flag_state_mt);
        put("mato grosso do sul", R.drawable.flag_state_ms);
        put("minas gerais", R.drawable.flag_state_mg);
        put("pará", R.drawable.flag_state_pa);
        put("paraíba", R.drawable.flag_state_pb);
        put("paraná", R.drawable.flag_state_pr);
        put("pernambuco", R.drawable.flag_state_pe);
        put("piauí", R.drawable.flag_state_pi);
        put("rio de janeiro", R.drawable.flag_state_rj);
        put("rio grande do norte", R.drawable.flag_state_rn);
        put("rio grande do sul", R.drawable.flag_state_rs);
        put("rondônia", R.drawable.flag_state_ro);
        put("roraima", R.drawable.flag_state_rr);
        put("santa catarina", R.drawable.flag_state_sc);
        put("são paulo", R.drawable.flag_state_sp);
        put("sergipe", R.drawable.flag_state_se);
        put("tocantis", R.drawable.flag_state_to);

        put("acre", R.drawable.flag_state_ac);
        put("alagoas", R.drawable.flag_state_al);
        put("amapá", R.drawable.flag_state_ap);
        put("amazonas", R.drawable.flag_state_am);
        put("bahia", R.drawable.flag_state_ba);
        put("ceará", R.drawable.flag_state_ce);
        put("distritofederal", R.drawable.flag_state_df);
        put("espíritosanto", R.drawable.flag_state_es);
        put("goiás", R.drawable.flag_state_go);
        put("maranhão", R.drawable.flag_state_ma);
        put("matogrosso", R.drawable.flag_state_mt);
        put("matogrossodosul", R.drawable.flag_state_ms);
        put("minasgerais", R.drawable.flag_state_mg);
        put("pará", R.drawable.flag_state_pa);
        put("paraíba", R.drawable.flag_state_pb);
        put("paraná", R.drawable.flag_state_pr);
        put("pernambuco", R.drawable.flag_state_pe);
        put("piauí", R.drawable.flag_state_pi);
        put("riodejaneiro", R.drawable.flag_state_rj);
        put("riograndedonorte", R.drawable.flag_state_rn);
        put("riograndedosul", R.drawable.flag_state_rs);
        put("rondônia", R.drawable.flag_state_ro);
        put("roraima", R.drawable.flag_state_rr);
        put("santacatarina", R.drawable.flag_state_sc);
        put("sãopaulo", R.drawable.flag_state_sp);
        put("sergipe", R.drawable.flag_state_se);
        put("tocantis", R.drawable.flag_state_to);
    }};

    public static int getStateFlag(String stateId) {
        LogUtils logUtils = new LogUtils();
        int flagId = -1;

        try {
            stateId = stateId.toLowerCase();

            if (mapStateFlagImg.containsKey(stateId)) {
                flagId = mapStateFlagImg.get(stateId);
            } else {
                stateId = stateId.replaceAll("\\s","");

                if (mapStateFlagImg.containsKey(stateId)) {
                    flagId = mapStateFlagImg.get(stateId);
                }
            }

        } catch (NullPointerException ignored) {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to getStateFlag");
        }

        return flagId;
    }

    public static void loadImage(Context context, String imgUrl, ImageView targetView){
        Glide.with(context)
                .load(imgUrl)
                .fitCenter()
                .crossFade()
                .into(targetView);
    }

    public static void loadImage(Context context, Uri imgPath, ImageView targetView){
        Glide.with(context)
                .load(imgPath)
                .fitCenter()
                .crossFade()
                .into(targetView);
    }

    public static void loadImage(Context context, int imgDrawable, ImageView targetView){
        Glide.with(context)
                .load(imgDrawable)
                .fitCenter()
                .crossFade()
                .into(targetView);
    }

    public static void loadCircularImage(Context context, String imgUrl, ImageView targetView) {
        Glide.with(context)
                .load(Uri.parse(imgUrl))
                .transform(new CircleTransform(context))
                .into(targetView);
    }

    public static void loadCircularImage(Context context, int imgDrawable, ImageView targetView) {
        Glide.with(context)
                .load(imgDrawable)
                .transform(new CircleTransform(context))
                .into(targetView);
    }

    public static void setFavoriteImg(Resources resources, View targetView, boolean isFavorite) {
        LogUtils logUtils = new LogUtils();

        if (targetView != null) {

            if (isFavorite) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    targetView.setBackground(resources.getDrawable(R.drawable.background_circle_yellow));

                } else {
                    targetView.setBackgroundColor(resources.getColor(R.color.color_yellow));
                }

            } else {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    targetView.setBackground(resources.getDrawable(R.drawable.background_circle_gray));

                } else {
                    targetView.setBackgroundColor(resources.getColor(R.color.color_gray_7));
                }
            }
        } else {
            logUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to setFavoriteImg");
        }
    }

}

