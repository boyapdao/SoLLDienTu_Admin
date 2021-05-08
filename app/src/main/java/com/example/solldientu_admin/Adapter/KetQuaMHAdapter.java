package com.example.solldientu_admin.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.solldientu_admin.R;
import com.example.solldientu_admin.object.MonHoc;

import java.util.ArrayList;

public class KetQuaMHAdapter extends BaseAdapter {
    int layout;
    Context context;
    ArrayList<MonHoc> ds;

    public KetQuaMHAdapter(int layout, Context context, ArrayList<MonHoc> ds) {
        this.layout = layout;
        this.context = context;
        this.ds = ds;
    }

    @Override
    public int getCount() {
        return ds.size();
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
        TextView tv_Ten, tv_STC, tv_CT;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
            holder = new ViewHolder();
            holder.tv_Ten=view.findViewById(R.id.tv_TenMon);
            holder.tv_STC=view.findViewById(R.id.tv_soTC);
            holder.tv_CT=view.findViewById(R.id.text_CT);
            view.setTag(holder);
        }else holder= (ViewHolder) view.getTag();

        MonHoc mh=ds.get(i);
        holder.tv_Ten.setText(mh.getTenMh());
        holder.tv_STC.setText(mh.getSoTc()+" tín chỉ");

        holder.tv_CT.setPaintFlags(holder.tv_CT.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.tv_CT.setText("Chi tiết -->");
        return view;
    }
}
