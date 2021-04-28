package com.example.solldientu_admin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.solldientu_admin.R;
import com.example.solldientu_admin.object.MonHoc;

import java.util.ArrayList;

public class MonHocAdapter extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<MonHoc> dsMh;

    public MonHocAdapter(Context context, int layout, ArrayList<MonHoc> dsMh) {
        this.context = context;
        this.layout = layout;
        this.dsMh = dsMh;
    }

    @Override
    public int getCount() {
        return dsMh.size();
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
        ImageView imgMh;
        TextView tv_Ten, tv_SoTC, tv_KyHoc;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view==null){
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(layout, null);
            holder=new ViewHolder();
            //
            holder.imgMh=view.findViewById(R.id.image_Mh);
            holder.tv_Ten=view.findViewById(R.id.tv_TenMon);
            holder.tv_SoTC=view.findViewById(R.id.tv_soTC);
            holder.tv_KyHoc=view.findViewById(R.id.tv_kyHoc);
            view.setTag(holder);
        }else {holder= (ViewHolder) view.getTag();}

        MonHoc mh=dsMh.get(i);
        holder.tv_Ten.setText(mh.getTenMh());
        holder.tv_SoTC.setText("Số tín chỉ: "+mh.getSoTc());
        holder.tv_KyHoc.setText("Kỳ học: "+mh.getKyhoc());
        return view;
    }
}
