package com.example.solldientu_admin.Api;

import com.example.solldientu_admin.object.GiaoVien;
import com.example.solldientu_admin.Pagination.Pagination;
import com.example.solldientu_admin.Pagination.pGiaoVien;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiGiaoVien {

    String url="https://solldientu-yg3.conveyor.cloud/api/GiaoVien/";

    OkHttpClient client=new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    Gson gson=new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .setLenient()
            .create();

    ApiGiaoVien apiService=new Retrofit.Builder().baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
            .create(ApiGiaoVien.class);

    @Multipart
    @POST("UpImage")
    Call<String> UploadPhoto(@Part MultipartBody.Part photo);

    @DELETE("DeleteImage/{imgdel}")
    Call<Void> DeleteImage(@Path("imgdel")String imgdel);

    @GET("get-all")
    Call<List<GiaoVien>> get_All();

//    @HTTP(method = "GET", path = "get-all2", hasBody = true)
    @POST("get-all2")
    Call<pGiaoVien> get_All2(@Body HashMap<String, String> page);

    @POST("search")
    Call<pGiaoVien> search(@Body HashMap<String, String> page);

    @POST("create-giaovien")
    Call<GiaoVien> sendPosts(@Body GiaoVien Post);

    @PUT("update-giaovien/{id}")
    Call<Void> putPost(@Path("id")String id,@Body GiaoVien p);

    @DELETE("delete2-giaovien/{id}")
    Call<Void> sendDelete(@Path("id")String id);

}
