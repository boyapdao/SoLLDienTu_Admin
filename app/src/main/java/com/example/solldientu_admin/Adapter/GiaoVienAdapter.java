package com.example.solldientu_admin.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.solldientu_admin.Api.ApiGiaoVien;
import com.example.solldientu_admin.R;
import com.example.solldientu_admin.object.GiaoVien;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GiaoVienAdapter extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<GiaoVien> dsGv;

    public GiaoVienAdapter(Context context, int layout, ArrayList<GiaoVien> dsGv) {
        this.context = context;
        this.layout = layout;
        this.dsGv = dsGv;
    }

    @Override
    public int getCount() {
        return dsGv.size();
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
        ImageView imgGv;
        TextView tvTen, tvNgaySinh, tvGioiTinh, tvQue;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view==null){
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(layout,null);
            holder=new ViewHolder();
            //Ánh xạ
            holder.imgGv=view.findViewById(R.id.image_Gv);
            holder.tvTen=view.findViewById(R.id.tv_TenGiaoVien);
            holder.tvNgaySinh=view.findViewById(R.id.tv_NgaySinh);
            holder.tvGioiTinh=view.findViewById(R.id.tv_GioiTinh);
            holder.tvQue=view.findViewById(R.id.tv_QueQuan);
            view.setTag(holder);
        }else {holder= (ViewHolder) view.getTag();}
        //Gán giá trị
        GiaoVien a=dsGv.get(i);

        holder.tvTen.setText(a.getTenGV());
        holder.tvNgaySinh.setText(a.getNgaySinh());

        if (a.getGioiTinh()==0)
            holder.tvGioiTinh.setText("Giới tính: Nữ");
        else holder.tvGioiTinh.setText("Giới tính: Nam");
        holder.tvQue.setText(a.getQueQuan());

        if (!a.getAnh().equals("")){

            String[] tenfile=a.getAnh().split("\\.");
//            Glide.with(context).load(ApiGiaoVien.url+"GetImage/"+tenfile[0]).into(holder.imgGv);
            Picasso.get().load(ApiGiaoVien.url+"GetImage/"+tenfile[0]).into(holder.imgGv);
//            Log.d("IMAT", tenfile[0]);
        }else Picasso.get().load(R.drawable.add_image).into(holder.imgGv);
        return view;
    }
}
