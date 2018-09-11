package solulab.demo.recyclerview.network;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.converter.gson.GsonConverterFactory;
import solulab.demo.recyclerview.utility.Logger;
import solulab.demo.recyclerview.utility.NullStringToEmptyAdapterFactory;

public class Retrofit {
    private Context context;

    private String baseURL;
    private String endPoint;
    private boolean headerEnable = true;

    private HashMap<String, String> params = new HashMap<>();
    private HashMap<String, File> fileParams = new HashMap<>();
    private HashMap<String, String> headerMap = new HashMap<>();

    public Retrofit(Context context) {
        this.context = context;
    }

    /**
     * @param context
     * @return Instance of this class
     * create instance of this class
     */
    public static Retrofit with(Context context) {
        return new Retrofit(context);
    }

    /**
     * @param baseUrl
     * @return Instance
     * set Base Url for temporary
     * optional method if you set default Base URL in API class
     */
    public Retrofit setUrl(@Nullable String baseUrl) {
        if (baseUrl != null) this.baseURL = baseUrl;
        return this;
    }

    /**
     * @param endPoint
     * @return Instance
     * set Endpoint when call every time
     */
    public Retrofit setAPI(@NonNull String endPoint) {
        this.endPoint = endPoint;
        String url = baseURL != null ? baseURL : API.BASE_URL;
        Logger.e("URL", url + endPoint);
        return this;
    }

    public Retrofit setHeaderEnable(boolean enable) {
        headerEnable = enable;
        return this;
    }

    /**
     * @return Instance
     * set Header when call every time
     */

    public Retrofit setHeader(String key, String value) {
        headerMap.put(key, value);
        Logger.e("Header", key + ":" + value);
        return this;
    }

    public Retrofit setHeader(@Nullable HashMap<String, String> headerMap) {
        if (headerMap != null) {
            this.headerMap = headerMap;
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                Logger.e("Header", entry.getKey() + ":" + entry.getValue());
            }
        }
        return this;
    }

    /**
     * @param params
     * @return Call
     * to set request parameter
     */
    public Retrofit setParameters(HashMap<String, String> params) {
        if (params != null) {
            this.params = params;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                Logger.e("params", entry.getKey() + ":" + entry.getValue());
            }
        }
        return this;
    }

    /**
     * @param files
     * @return Call
     */
    public Retrofit setFileParameters(HashMap<String, File> files) {
        if (files != null) {
            this.fileParams = files;
            for (Map.Entry<String, File> entry : fileParams.entrySet()) {
                Logger.e("params", entry.getKey() + ":" + entry.getValue());
            }
        }
        return this;
    }

    public APIInterface getAPIInterface() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory<>())
                .create();

        return new retrofit2.Retrofit.Builder()
                .baseUrl(baseURL != null ? baseURL : API.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build().create(APIInterface.class);
    }

    public void setCallBackListener(JSONCallback listener) {
        makeCall().enqueue(listener);
    }

    private Call<ResponseBody> makeCall() {
        Call<ResponseBody> call;
        HashMap<String, RequestBody> bodyParams = new HashMap<>();
        for (Map.Entry<String, File> entry : fileParams.entrySet()) {
            String fileName = entry.getKey() + "\"; filename=\"" + entry.getValue().getName();
            bodyParams.put(fileName, RequestBody.create(MediaType.parse("multipart/form-data"), entry.getValue()));
        }

        if (fileParams.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                bodyParams.put(entry.getKey(), RequestBody.create(MediaType.parse("multipart/form-data"), entry.getValue()));
            }
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory())
                .create();

        APIInterface APIInterface = new retrofit2.Retrofit.Builder()
                .baseUrl(baseURL != null ? baseURL : API.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build().create(APIInterface.class);

        if (bodyParams.size() > 0) {
            call = APIInterface.postMethodMultipart(endPoint, headerMap, bodyParams);
        } else {
            call = APIInterface.postMethod(endPoint, headerMap, params);
        }
        return call;
    }
}
