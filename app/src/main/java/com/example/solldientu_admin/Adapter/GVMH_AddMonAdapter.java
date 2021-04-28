package com.example.solldientu_admin.Adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.solldientu_admin.Api.ApiGVMH;
import com.example.solldientu_admin.R;
import com.example.solldientu_admin.object.MonHoc;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GVMH_AddMonAdapter extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<MonHoc> ds;

    public GVMH_AddMonAdapter(Context context, int layout, ArrayList<MonHoc> ds) {
        this.context = context;
        this.layout = layout;
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
        TextView tv_Ten, tv_STC;
        ImageView img_delete;
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
            holder.img_delete=view.findViewById(R.id.img_delete);
            view.setTag(holder);
        }else holder= (ViewHolder) view.getTag();

        MonHoc mh=ds.get(i);
        holder.tv_Ten.setText(mh.getTenMh());
        holder.tv_STC.setText(mh.getSoTc()+" tín chỉ");
        holder.img_delete.setVisibility(View.GONE);

        return view;
    }
}
