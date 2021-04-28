package com.example.solldientu_admin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.solldientu_admin.Api.ApiGVMH;
import com.example.solldientu_admin.Api.ApiGiaoVien;
import com.example.solldientu_admin.R;
import com.example.solldientu_admin.object.GiaoVien;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GVMH_Apdapter extends BaseAdapter {
    int layout;
    Context context;
    ArrayList<GiaoVien> dsgv;

    public GVMH_Apdapter(int layout, Context context, ArrayList<GiaoVien> dsgv) {
        this.layout = layout;
        this.context = context;
        this.dsgv = dsgv;
    }

    @Override
    public int getCount() {
        return dsgv.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    class ViewHolder{
        ImageView img;
        TextView tvTen, tvNS, tvGT, tvSoMon;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view==null){
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(layout, null);
            holder=new ViewHolder();
            //
            holder.img=view.findViewById(R.id.image_Gv);
            holder.tvTen=view.findViewById(R.id.tv_TenGiaoVien);
            holder.tvNS=view.findViewById(R.id.tv_NgaySinh);
            holder.tvGT=view.findViewById(R.id.tv_GioiTinh);
            holder.tvSoMon=view.findViewById(R.id.tv_MonDay);
            view.setTag(holder);
        }else holder= (ViewHolder) view.getTag();

        GiaoVien gv=dsgv.get(i);

        holder.tvTen.setText(gv.getTenGv());
        if (gv.getGioiTinh()==0)
            holder.tvGT.setText("Giới tính: Nữ");
        else holder.tvGT.setText("Giới tính: Nam");
        holder.tvNS.setText(gv.getNgaySinh());
        if (!gv.getAnh().equals("")){
            String[] tenfile=gv.getAnh().split("\\.");
            Picasso.get().load(ApiGiaoVien.url+"GetImage/"+tenfile[0]).into(holder.img);
        }else Picasso.get().load(R.drawable.add_image).into(holder.img);

        ApiGVMH.apiService.getCount(gv.getMaGv()).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.body()>0)
                    holder.tvSoMon.setText("Số môn giảng dạy: "+response.body());
                else holder.tvSoMon.setText("Số môn giảng dạy: "+0);
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
        return view;
    }
}
