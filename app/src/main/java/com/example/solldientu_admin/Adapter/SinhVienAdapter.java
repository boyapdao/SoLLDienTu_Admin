package com.example.solldientu_admin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.solldientu_admin.Api.ApiGiaoVien;
import com.example.solldientu_admin.Api.ApiSinhVien;
import com.example.solldientu_admin.R;
import com.example.solldientu_admin.object.GiaoVien;
import com.example.solldientu_admin.object.SinhVien;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SinhVienAdapter extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<SinhVien> dsSv;

    public SinhVienAdapter(Context context, int layout, ArrayList<SinhVien> dsSv) {
        this.context = context;
        this.layout = layout;
        this.dsSv = dsSv;
    }

    @Override
    public int getCount() {
        return dsSv.size();
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
        ImageView imgSv;
        TextView tvTen, tvNgaySinh, tvGioiTinh, tvSdt;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        SinhVienAdapter.ViewHolder holder;
        if(view==null){
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(layout,null);
            holder=new SinhVienAdapter.ViewHolder();
            //Ánh xạ
            holder.imgSv=view.findViewById(R.id.image_Sv);
            holder.tvTen=view.findViewById(R.id.tv_TenSinhVien);
            holder.tvNgaySinh=view.findViewById(R.id.tv_NgaySinhSV);
            holder.tvGioiTinh=view.findViewById(R.id.tv_GioiTinhSV);
            holder.tvSdt=view.findViewById(R.id.tv_SdtSV);
            view.setTag(holder);
        }else {holder= (SinhVienAdapter.ViewHolder) view.getTag();}
        //Gán giá trị
        SinhVien a=dsSv.get(i);

        holder.tvTen.setText(a.getTenSv());
        holder.tvNgaySinh.setText(a.getNgaySinh());

        if (a.getGioiTinh()==0)
            holder.tvGioiTinh.setText("Giới tính: Nữ");
        else holder.tvGioiTinh.setText("Giới tính: Nam");
        holder.tvSdt.setText(a.getSdt());

        if (!a.getAnh().equals("")){

            String[] tenfile=a.getAnh().split("\\.");
//            Glide.with(context).load(ApiGiaoVien.url+"GetImage/"+tenfile[0]).into(holder.imgGv);
            Picasso.get().load(ApiSinhVien.url+"GetImage/"+tenfile[0]).into(holder.imgSv);
//            Log.d("IMAT", tenfile[0]);
        }else Picasso.get().load(R.drawable.add_image).into(holder.imgSv);
        return view;
    }
}
