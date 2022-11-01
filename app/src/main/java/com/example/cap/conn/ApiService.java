package com.example.cap.conn;

import android.provider.ContactsContract;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    public static final String API_URL = "https://65ec86f5b4f8.ngrok.io";

    @GET("tests")
    Call<ResponseBody> get_Test(@Query("format") String json);

    @GET("tests/{id}")
    Call<ResponseBody> get_weight(@Path("id") String json);

    @FormUrlEncoded
    @POST("tests")
    Call<Json_Test> post_Test(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
//    @POST("tests/")
//    Call<ResponseBody> post_Test2(@Field("email") String test);
    @POST("tests/")
    Call<Json_Test> post_json_test_java(@Query("format") String json, @Body Json_Test json_test_java);


    @FormUrlEncoded
    @PATCH("tests/{pk}/")
    Call<ResponseBody> patch_Test(@Path("pk") int pk, @Query("format") String json, @Field("test") String test);

    @DELETE("tests/{pk}/")
    Call<ResponseBody> delete_Patch_Test(@Path("pk") int pk, @Query("format") String json);
}
