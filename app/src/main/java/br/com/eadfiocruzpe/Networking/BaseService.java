package br.com.eadfiocruzpe.Networking;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import android.util.Log;

import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;

import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import br.com.eadfiocruzpe.BuildConfig;
import br.com.eadfiocruzpe.Persistence.PreferencesManager;
import br.com.eadfiocruzpe.Models.DataResponses.BaseWebScrapingResponse;

public class BaseService<T> {

    private static final String TAG = "BaseService";
    private static final String ACCESS_LANG_HEADER = "lang";
    private static final int CONNECTION_TIMEOUT = 60;

    protected T mService;
    private final Class<T> mServiceClass;
    private String mBaseUrl;
    private boolean mTokenEnabled;


    public BaseService(final Class<T> serviceClass, String baseUrl, boolean tokenEnabled) {
        mServiceClass = serviceClass;
        mBaseUrl = baseUrl;
        mTokenEnabled = tokenEnabled;

        setupNetwork();
    }

    private void setupNetwork() {
        try {
            // Build a OkHttpClient
            OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

            // Add the Token interceptor if needed
            httpClientBuilder.addInterceptor(getTokenInterceptor());

            // Add the Logging interceptor
            httpClientBuilder.addInterceptor(getLoggingInterceptor());
            OkHttpClient okHttpClient = httpClientBuilder
                    .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                    .build();

            // Set the OkHttpClient on Retrofit
            if (mBaseUrl.equals(BuildConfig.SIOPS_URL)) {
                final Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(mBaseUrl)
                        .addConverterFactory(HtmlPageAdapter.FACTORY)
                        .client(okHttpClient)
                        .build();

                mService = retrofit.create(mServiceClass);
            } else {
                final Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(mBaseUrl)
                        .addConverterFactory(GsonConverterFactory.create(new Gson()))
                        .client(okHttpClient)
                        .build();

                mService = retrofit.create(mServiceClass);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Define an Interceptor to insert an AuthenticationService Header (Access-Token)
     */
    private Interceptor getTokenInterceptor() {
        return new Interceptor() {

            @Override
            public Response intercept(Chain chain) throws IOException {
                final String lang = PreferencesManager.getDefaultLang();

                try {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .header(ACCESS_LANG_HEADER, lang)
                            .method(original.method(), original.body())
                            .build();

                    return chain.proceed(request);

                } catch (NullPointerException e) {
                    return chain.proceed(chain.request());
                }
            }
        };
    }

    /**
     * Define Logging Interceptor for DEBUG mode
     */
    private HttpLoggingInterceptor getLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return loggingInterceptor;
    }

    /**
     * Create a ConverterFactory to the content of HTML files with retrofit
     */
    static final class HtmlPageAdapter implements Converter<ResponseBody, BaseWebScrapingResponse> {

        static final Converter.Factory FACTORY = new Converter.Factory() {
            @Override
            public Converter<ResponseBody, ?> responseBodyConverter(Type type,
                                                                    Annotation[] annotations,
                                                                    Retrofit retrofit) {

                if (type == BaseWebScrapingResponse.class) {
                    return new HtmlPageAdapter();
                }

                return null;
            }
        };

        @Override
        public BaseWebScrapingResponse convert(ResponseBody responseBody) throws IOException {
            try {
                Document document = Jsoup.parse(responseBody.string());
                Element element = document.body();
                String content = element.html();

                return new BaseWebScrapingResponse(content);

            } catch (Exception e) {
                Log.d(TAG, "Failed to BaseWebScrapingResponse");
            }

            return null;
        }
    }

}