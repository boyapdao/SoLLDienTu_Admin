package com.example.solldientu_admin.Api;

import com.example.solldientu_admin.Pagination.pGiaoVien;
import com.example.solldientu_admin.Pagination.pMonHoc;
import com.example.solldientu_admin.object.MonHoc;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiMonHoc {
    String url="https://solldientu.conveyor.cloud/api/MonHoc/";

    OkHttpClient client=new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    Gson gson=new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .setLenient()
            .create();

    ApiMonHoc apiService=new Retrofit.Builder().baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
            .create(ApiMonHoc.class);

    @POST("get-all2")
    Call<pMonHoc> get_All2(@Body HashMap<String, String> page);

    @POST("create-monhoc")
    Call<Void> create_MH(@Body MonHoc mh);

    @PUT("update-monhoc")
    Call<Void> update_MH(@Body MonHoc mh);

    @DELETE("delete-monhoc2/{id}")
    Call<Void> delete_MH(@Path("id")String id);

    @POST("search")
    Call<pMonHoc> search_MH(@Body HashMap<String, String> hm);
}
