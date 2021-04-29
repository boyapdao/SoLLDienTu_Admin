package com.example.solldientu_admin.Api;

import androidx.room.Delete;

import com.example.solldientu_admin.Pagination.pSinhVien;
import com.example.solldientu_admin.object.GiaoVien;
import com.example.solldientu_admin.object.SinhVien;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiSinhVien {
    String url="https://solldientu.conveyor.cloud/api/SinhVien/";

    Gson gson=new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .setLenient()
            .create();

    ApiSinhVien apiService=new Retrofit.Builder().baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiSinhVien.class);
    @Multipart
    @POST("UpImage")
    Call<String> UploadPhoto(@Part MultipartBody.Part photo);

    //gọi hàm delete ở API
    @DELETE("DeleteImage/{imgdel}")
    Call<Void> DeleteImage(@Path("imgdel")String imgdel);

    @POST("get-all2")
    Call<pSinhVien> get_AllSV2(@Body HashMap<String,String> page);

    @POST("create-sinhvien")
    Call<Void> addSinhVien(@Body SinhVien sinhVien);

    // lấy tất cả mã lớp
    @GET("get-allMaLopSV")
    Call<List<String>> getallMaLopSV();

    @PUT("update-sinhvien/{id}")
    Call<Void> updateSinhVien(@Path("id")String id,@Body SinhVien sv);

    @DELETE("delete-sinhvien/{id}")
    Call<Void> sendDeleteSV(@Path("id")String id);

    @POST("search")
    Call<pSinhVien> searchSV (@Body HashMap<String,String> page);


}
