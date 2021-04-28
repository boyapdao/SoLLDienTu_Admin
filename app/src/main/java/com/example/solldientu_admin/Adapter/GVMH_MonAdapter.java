package com.example.solldientu_admin.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;

import com.example.solldientu_admin.Api.ApiGVMH;
import com.example.solldientu_admin.R;
import com.example.solldientu_admin.object.MonHoc;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GVMH_MonAdapter extends BaseAdapter {
    int layout;
    ArrayList<MonHoc> ds;
    Context context;
    ProgressDialog pd;
    FragmentTransaction ft;
    Dialog dialog1, dialog2;
    String maGv;

    public GVMH_MonAdapter(int layout, ArrayList<MonHoc> ds, String maGv, Context context,Dialog dialog1, Dialog dialog2, ProgressDialog pd, FragmentTransaction ft) {
        this.layout = layout;
        this.ds = ds;
        this.context = context;
        this.maGv=maGv;
        this.pd=pd;
        this.dialog1=dialog1;
        this.dialog2=dialog2;
        this.ft=ft;
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
        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert=new AlertDialog.Builder(context);
                alert.setTitle("Xóa!");
                alert.setMessage("Hủy đăng ký môn học này?");
                alert.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String MaMon=mh.getMaMh();
                        pd=new ProgressDialog(context);
                        pd.setMessage("Đang hủy...");
                        ApiGVMH.apiService.delete_gvmh(maGv, MaMon).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                pd.dismiss();
                                Toast.makeText(context, "Hủy thành công!", Toast.LENGTH_SHORT).show();
                                dialog1.cancel();
                                dialog2.cancel();
                                ft.commit();
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }
                        });
                    }
                });
                alert.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alert.show();
            }
        });

        return view;
    }
}
