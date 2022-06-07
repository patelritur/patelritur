package com.demo.webservice;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.demo.R;
import com.demo.utils.Constants;
import com.demo.utils.DialogUtils;
import com.demo.utils.PrintLog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.File;
import java.security.cert.CertificateException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * we have used retorfit 2.0 to call webservice & process data
 *
 * @link APIService
 * listed all methods of api call
 */
public class RestClient {

    private static APIService apiService;
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    /**
     * Instantiates a new Rest client.
     *
     * @param context the context
     */
    public RestClient(Context context) {
        RestClient.context = context;
        setupRestClient();

    }

    /**
     * Gets api service.
     *
     * @return the api service
     */
    public static APIService getApiService() {
        return apiService;
    }




    private static Interceptor provideCacheInterceptor() {
        return chain -> {
            Request request = chain.request();
            CacheControl cacheControl = new CacheControl.Builder()
                    .maxStale(0, TimeUnit.DAYS)
                    .maxAge(0, TimeUnit.DAYS)
                    .build();

            request = request.newBuilder()
                    .addHeader("Cache-Control", cacheControl.toString())
                    .addHeader("Accept", "*/*")
                    .addHeader(/*"x-access-token"*/"ApiKey",
                            "ZGVtb2FwcDpEXmRlbW80NSEwMUA1NDo0MDAxOjAwMDA=")
                    .build();

            Response originalResponse = chain
                    .proceed(request);
            return originalResponse.newBuilder()
                    .removeHeader("Cache-Control")
                    .build();
        };
    }

    private static Cache provideCache() {
        Cache cache = null;
        try {
            cache = new Cache(new File(context.getCacheDir(), "http-cache"), 10 * 1024 * 1024);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cache;
    }

    /**
     * Show progress.
     *
     * @param context the context
     * @param toShow  to show or hide progress dialog
     */
    private static void showProgress(Context context, boolean toShow) {
        try {
            if (toShow)
                DialogUtils.showProgressDialog(context, "", "Please wait...", false);
            else DialogUtils.dismissProgressDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Showing error dialog when api response code is not 200(success)
     *
     * @param context      - context
     * @param errorMessage - error message to show
     */
    private static void showDialog(Context context, String errorMessage) {
        DialogUtils.showAlertInfo(context, errorMessage);
    }

    /**
     * Make api request.
     *
     * @param activity            the activity
     * @param call                the call
     * @param apiResponseListener the api response listener
     * @param reqCode             the req code
     * @param showProgressDialog  the show progress dialog
     */
    public static void makeApiRequest(final Context activity, Object call, final ApiResponseListener apiResponseListener, final int reqCode, final boolean showProgressDialog) {
        try {
            @SuppressWarnings("unchecked") Call<Object> objectCall = (Call<Object>) call;
            if (ConnectionUtils.isConnected(activity)) {
                if (showProgressDialog)
                    showProgress(activity, false);
                if (showProgressDialog)
                    showProgress(activity, true);

                objectCall.enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(@NonNull Call<Object> call, @NonNull retrofit2.Response<Object> response) {
                        try {
                            if (showProgressDialog)
                                showProgress(activity, false);

                            switch (response.code()) {
                                case 200:
                                    //noinspection ConstantConditions
                                    if (response.headers() != null && response.headers().names() != null) {
                                       /* if (response.headers().names().contains(*//*"x-access-token"*//*"Token")) {
                                            PreferenceUtils.getInstance(context).setValue(context.getString(R.string.pref_token),
                                                    response.headers().get(*//*"x-access-token"*//*"Token"));
                                        }*/
                                       /* if (response.headers().names().contains("userid")) {
                                            PreferenceUtils.getInstance(context).setValue(context.getString(R.string.pref_userId),
                                                    response.headers().get("userid"));
                                        }*/
                                    }

                                    if (apiResponseListener != null) {
                                        apiResponseListener.onApiResponse(call, response.body(), reqCode);
                                    }
                                    Log.d("U408Logs", "API : onResponse(200) ");
                                    break;
                                case 201:
                                    {
                                        if (apiResponseListener != null) {
                                            apiResponseListener.onApiResponse(call, response, reqCode);
                                        }
                                        Log.d("U408Logs", "API : onResponse(200) ");
                                    }
                                    break;
                                case 400:
                                    if (response.errorBody() != null) {
                                        JSONObject jObjError = new JSONObject(Objects.requireNonNull(response.errorBody()).string());
                                        CommonResponse errorResponse = new Gson()
                                                .fromJson(jObjError.toString(), CommonResponse.class);

                                        if (showProgressDialog)
                                            showDialog(activity, errorResponse.getMessage());
                                    }
                                    showProgress(activity, false);
                                    Log.e("U408Logs", "API : onResponse(400) ");
                                    break;
                                case 401:

                                    break;
                                default:
                                    if (response.errorBody() != null) {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        CommonResponse errorResponse = new Gson()
                                                .fromJson(jObjError.toString(), CommonResponse.class);
                                        if (showProgressDialog)
                                            showDialog(activity, errorResponse.getMessage());
                                    }
                                    showProgress(activity, false);

                                    break;
                            }
                        } catch (Exception e) {
                            showProgress(activity, false);
                            try {
                                apiResponseListener.onApiResponse(call, new Object(), reqCode);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            e.printStackTrace();
                            Log.e("Logs", "API : onResponse(Exception) ");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                        //There is more than just a failing request (like: no internet connection)
                        PrintLog.e("error", "" + t.toString());showProgress(activity, false);
                        DialogUtils.showAlertDialog(activity, activity.getString(R.string.ttl_connection_not_available), activity.getString(R.string.msg_connection_timeout));

                        try {
                            apiResponseListener.onApiError(call, t, 0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //Connection error when no internet available show some offline not enabled for this page error.

                    }
                });
            } else {
                DialogUtils.showAlertDialog(activity, activity.getString(R.string.ttl_connection_not_available), activity.getString(R.string.msg_connection_not_available));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Sets rest client.
     */
    private void setupRestClient() {

        Gson gson = new GsonBuilder().setLenient()
                .setDateFormat(DateUtils.getUtcTFormat()).create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getHttpClient()).build();

        apiService = retrofit.create(APIService.class);
    }








    private OkHttpClient getHttpClient() {
        try {
            /*ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                    .tlsVersions(TlsVersion.TLS_1_2)
                    .cipherSuites(
                            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                            CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                            CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
                    .build();*/

            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("TLSv1.2");


            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            OkHttpClient.Builder builderOkHttp = new OkHttpClient.Builder();
            builderOkHttp.connectTimeout(45, TimeUnit.SECONDS);
            builderOkHttp.readTimeout(30, TimeUnit.SECONDS);
          {
              HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
              logging.level(HttpLoggingInterceptor.Level.BODY);
              builderOkHttp.addInterceptor(logging);
            }
            builderOkHttp.addNetworkInterceptor(provideCacheInterceptor());
            builderOkHttp.cache(provideCache());
            /*builderOkHttp.connectionSpecs(Collections.singletonList(spec));*/
            return builderOkHttp.build();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }




}