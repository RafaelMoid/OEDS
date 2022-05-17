package br.com.eadfiocruzpe.Utils;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;

public class JsonUtils {

    public static final <T> T fromJson(final String json, Class<T> klass) {
        try{
            final Gson gson = new Gson();
            final T object = gson.fromJson(json, klass);
            return object;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static final String toJson(final Object object){
        final Gson gson = new Gson();
        return gson.toJson(object);
    }

    public static String loadJSONFromAsset(final Context context, final String fileName) {
        String json = null;

        try {
            final InputStream is = context.getAssets().open(fileName);
            final int size = is.available();
            final byte[] buffer = new byte[size];

            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return json;
    }

}
