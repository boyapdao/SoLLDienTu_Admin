package com.example.solldientu_admin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.solldientu_admin.R;
import com.example.solldientu_admin.object.LopHoc;

import java.util.ArrayList;

public class LopAdapter extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<LopHoc> dsLop;

    public LopAdapter(Context context, int layout, ArrayList<LopHoc> dsLop) {
        this.context = context;
        this.layout = layout;
        this.dsLop = dsLop;
    }

    @Override
    public int getCount() {
        return dsLop.size();
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
        ImageView imgLop;
        TextView tvMaLop, tvtenLop, tvmaGVLop;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view==null){
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(layout,null);
            holder=new ViewHolder();
            //Ánh xạ
            holder.imgLop=view.findViewById(R.id.image_Lop);
            holder.tvMaLop=view.findViewById(R.id.tv_maLop);
            holder.tvtenLop=view.findViewById(R.id.tv_TenLop);
            holder.tvmaGVLop=view.findViewById(R.id.tv_maGVLop);
            view.setTag(holder);
        }else {holder= (ViewHolder) view.getTag();}
        //Gán giá trị
        LopHoc a=dsLop.get(i);

        holder.tvtenLop.setText(a.getTenLop());
        holder.tvmaGVLop.setText(a.getMaGv());
        holder.tvMaLop.setText(a.getMaLop());

        return view;
    }
}
