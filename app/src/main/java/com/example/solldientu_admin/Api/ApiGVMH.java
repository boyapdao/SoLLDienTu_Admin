package com.example.solldientu_admin.Api;

import com.example.solldientu_admin.object.GVMH;
import com.example.solldientu_admin.object.MonHoc;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiGVMH {
    String url="https://solldientu.conveyor.cloud/api/GiaoVienMH/";

    OkHttpClient client=new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    Gson gson=new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .setLenient()
            .create();

    ApiGVMH apiService=new Retrofit.Builder().baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
            .create(ApiGVMH.class);

    @GET("get-count/{id}")
    Call<Integer> getCount(@Path("id")String id);

    //GetMH-by-id Giáo viên
    @GET("getMH-by-id/{id}")
    Call<List<MonHoc>> getMhById(@Path("id")String id);

    @GET("get-all")
    Call<List<MonHoc>> getAll_MonHoc();

    @POST("create-giaovienmh")
    Call<Void> createGVMH(@Body GVMH gvmh);

    @DELETE("delete-giaovienmh2/{idgv}/{idmh}")
    Call<Void> delete_gvmh(@Path("idgv")String idgv, @Path("idmh")String idmh);
}
