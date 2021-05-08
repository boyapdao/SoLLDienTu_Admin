package com.example.solldientu_admin.Api;

import com.example.solldientu_admin.object.KetQua;
import com.example.solldientu_admin.object.LopHoc;
import com.example.solldientu_admin.object.MonHoc;
import com.example.solldientu_admin.object.SinhVien;
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
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiKetQua {
    String url="https://solldientu.conveyor.cloud/api/KetQua/";

    OkHttpClient client=new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    Gson gson=new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .setLenient()
            .create();

    ApiKetQua apiService=new Retrofit.Builder().baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
            .create(ApiKetQua.class);

    @GET("getSV-by-maLop/{id}")
    Call<List<SinhVien>> getSVByMaLop(@Path("id") String id);

    @GET("get-All-Lop")
    Call<List<LopHoc>> getAllLop();

    @GET("get-SV-ById/{id}")
    Call<SinhVien> getSvById(@Path("id")String id);

    @GET("getMH-by-KyHoc/{KyHoc}")
    Call<List<MonHoc>> getMhByKyhoc(@Path("KyHoc") int KyHoc);

    @POST("get-KetQua-ById")
    Call<KetQua> getTTSV_KetQua(@Body HashMap<String, String> p);

    @POST("confirm-ketqua")
    Call<Void> confirm_KQ(@Body KetQua kq);
}
