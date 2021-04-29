package com.example.solldientu_admin.Api;

import com.example.solldientu_admin.Pagination.pLopHoc;
import com.example.solldientu_admin.object.GiaoVien;
import com.example.solldientu_admin.object.LopHoc;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
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
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiLopHoc {
    String url="https://solldientu.conveyor.cloud/api/Lop/";

    OkHttpClient client=new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    Gson gson=new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .setLenient()
            .create();

    ApiLopHoc apiService=new Retrofit.Builder().baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
            .create(ApiLopHoc.class);

    @GET("get-all")
    Call<List<GiaoVien>> get_All();

    @POST("get-all2")
    Call<pLopHoc> get_All2Lop(@Body HashMap<String, String> page);

    @POST("create-lop")
    Call<Void> postAddLop(@Body LopHoc lopHoc);

    @GET("get-all-idnameGv")
    Call<List<GiaoVien>> getMaTenGVLop();

    @PUT("update-lop/{id}")
    Call<Void> UpdateLop(@Path("id")String id,@Body LopHoc p);

    @DELETE("delete-lop/{id}")
    Call<Void> sendDelete(@Path("id")String id);

    @POST("search")
    Call<pLopHoc> searchTenLop (@Body HashMap<String,String> page);
}
