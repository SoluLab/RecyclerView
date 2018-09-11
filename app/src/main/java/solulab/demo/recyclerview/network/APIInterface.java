package solulab.demo.recyclerview.network;

import java.util.HashMap;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by RAJESH KUSHVAHA
 */

public interface APIInterface {
    @GET("/api")
    Call<ResponseBody> getMethod(@Query("key") String key);

    @GET("/api")
    Call<ResponseBody> getMethod( @Query("key") String key, @QueryMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST
    Call<ResponseBody> postMethod(@Url String endpoint, @HeaderMap HashMap<String, String> headerMap, @FieldMap HashMap<String, String> fields);

    @Multipart
    @POST
    Call<ResponseBody> postMethodMultipart(@Url String endpoint, @HeaderMap HashMap<String, String> headerMap, @PartMap HashMap<String, RequestBody> fields);
}
