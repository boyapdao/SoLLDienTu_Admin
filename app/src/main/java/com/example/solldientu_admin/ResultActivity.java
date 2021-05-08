package com.example.solldientu_admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.solldientu_admin.Adapter.KetQuaMHAdapter;
import com.example.solldientu_admin.Api.ApiGiaoVien;
import com.example.solldientu_admin.Api.ApiKetQua;
import com.example.solldientu_admin.Api.ApiSinhVien;
import com.example.solldientu_admin.Pagination.pKetQua;
import com.example.solldientu_admin.object.KetQua;
import com.example.solldientu_admin.object.MonHoc;
import com.example.solldientu_admin.object.SinhVien;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultActivity extends AppCompatActivity {

    Toolbar toolbar;
    ActionBar actionBar;

    String MaSV="";
    String MaMH="";
    String MaLop="";
    String Anh="";

    int NhapDiem=-1; // 1 là nhập điểm lần đầu, 2 là nhập điểm thi lại

    ImageView img_sv;
    TextView tvMa, tvTen, tvNS, tvGT, tvThuongTru, tvTamTru, tvSDT;

    ProgressDialog pd;

    ArrayList<Integer> ds_KyHoc=new ArrayList<>();
    Spinner sp_Kyhoc;
    ArrayAdapter adapter_sp;

    ListView lv_MH;
    ArrayList<MonHoc> ds_MH=new ArrayList<>();
    KetQuaMHAdapter adapter;

    int KyHoc=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Init();
        MaSV=getIntent().getStringExtra("maSv");
        setData();
        LoadMon();
        Events();
    }

    private void setData() {
        pd=new ProgressDialog(ResultActivity.this);
        pd.setMessage("Đang tải dữ liệu...");
        pd.show();
        ApiKetQua.apiService.getSvById(MaSV).enqueue(new Callback<SinhVien>() {
            @Override
            public void onResponse(Call<SinhVien> call, Response<SinhVien> response) {
                SinhVien s=response.body();
                MaLop=s.getMaLop();
                Anh=s.getAnh();

                tvMa.setText(MaSV);
                tvTen.setText(s.getTenSv());
                tvNS.setText(s.getNgaySinh());
                if(s.getGioiTinh()==0)
                    tvGT.setText("Nữ");
                else tvGT.setText("Nam");
                tvThuongTru.setText(s.getThuongTru());
                tvTamTru.setText(s.getTamTru());
                tvSDT.setText(s.getSdt());

                if (!s.getAnh().equals("")){
                    String[] tenfile=s.getAnh().split("\\.");
                    Picasso.get().load(ApiSinhVien.url+"GetImage/"+tenfile[0]).into(img_sv);
                }else Picasso.get().load(R.drawable.img_avt).into(img_sv);

                pd.dismiss();
            }

            @Override
            public void onFailure(Call<SinhVien> call, Throwable t) {

            }
        });
    }

    private void Events() {
        sp_Kyhoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                KyHoc=ds_KyHoc.get(i);
                LoadMon();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        lv_MH.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        lv_MH.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MaMH=ds_MH.get(i).getMaMh();
                ShowDialogChiTiet(i);
            }
        });
    }

    private void ShowDialogChiTiet(int pos_MH) {
        Dialog dialog=new Dialog(ResultActivity.this);
        dialog.setContentView(R.layout.dialog_ketqua_chitiet);

        pd=new ProgressDialog(ResultActivity.this);
        pd.setMessage("Đang tải dữ liệu...");
        pd.show();

        ImageView img_avt, img_setDiem;
        TextView tv_TenSV, tv_MaSV, tv_Lop, tv_TenMon, tv_SoTC, tv_DiemLD, tv_DiemTL;
        Button btn_XacNhan, btn_Huy;

        img_avt=dialog.findViewById(R.id.img_SV);
        img_setDiem=dialog.findViewById(R.id.img_setDiem);

        tv_TenSV=dialog.findViewById(R.id.tv_TenSV);
        tv_MaSV=dialog.findViewById(R.id.tv_MaSV);
        tv_Lop=dialog.findViewById(R.id.tv_Lop);
        tv_TenMon=dialog.findViewById(R.id.tv_TenMon);
        tv_SoTC=dialog.findViewById(R.id.tv_soTC);
        tv_DiemLD=dialog.findViewById(R.id.tv_DiemLD);
        tv_DiemTL=dialog.findViewById(R.id.tv_DiemTL);
        btn_XacNhan=dialog.findViewById(R.id.btn_Ok);
        btn_Huy=dialog.findViewById(R.id.btn_Huy);

        pKetQua p=new pKetQua(MaSV, MaMH);
        ApiKetQua.apiService.getTTSV_KetQua(p.getHm()).enqueue(new Callback<KetQua>() {
            @Override
            public void onResponse(Call<KetQua> call, Response<KetQua> response) {
                KetQua k=response.body();

                MonHoc mh=ds_MH.get(pos_MH);

                tv_TenSV.setText(tvTen.getText().toString());
                tv_MaSV.setText("Mã SV: "+tvMa.getText().toString());
                tv_Lop.setText("Học lớp: "+MaLop);
                tv_TenMon.setText(mh.getTenMh());
                tv_SoTC.setText(mh.getSoTc()+" tín chỉ");
                if(!Anh.equals("")){
                    String[] tenfile=Anh.split("\\.");
                    Picasso.get().load(ApiGiaoVien.url+"GetImage/"+tenfile[0]).into(img_avt);
                }else Picasso.get().load(R.drawable.img_avt).into(img_avt);

                if (k!=null){
                    tv_DiemLD.setText(k.getDiemLd()+"");
                    if (k.getDiemTl()<0)
                        tv_DiemTL.setText("...Empty...");
                    else tv_DiemTL.setText(k.getDiemTl()+"");

                    img_setDiem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SuaKQ(tv_DiemLD, tv_DiemTL);
                        }
                    });
                }else {
                    Toast.makeText(ResultActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                    img_setDiem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ThemKQ(tv_DiemLD);
                        }
                    });
                }
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<KetQua> call, Throwable t) {

            }
        });
        btn_Huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        btn_XacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd=new ProgressDialog(ResultActivity.this);
                pd.setMessage("Đang cập nhật dữ liệu...");
                pd.show();

                int DiemLd=-1, DiemTL=-1;
                if(NhapDiem==1)
                    DiemLd=Integer.parseInt(tv_DiemLD.getText().toString());
                if (NhapDiem==2)
                {
                    DiemLd=Integer.parseInt(tv_DiemLD.getText().toString());
                    DiemTL=Integer.parseInt(tv_DiemTL.getText().toString());
                }
                KetQua kq=new KetQua(MaSV, MaMH, DiemLd, DiemTL);
                ApiKetQua.apiService.confirm_KQ(kq).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        pd.dismiss();
                        dialog.cancel();
                        Toast.makeText(ResultActivity.this, "Cập nhật hệ thống thành công!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
            }
        });
        dialog.show();
    }

    private void SuaKQ(TextView tv_diemLD, TextView tv_diemTL) {
        Dialog dialog=new Dialog(ResultActivity.this);
        dialog.setContentView(R.layout.dialog_setdiem_all);
        EditText edt_DiemLD, edt_DiemTL;
        Button btn_Ok, btn_Cancel;

        edt_DiemLD=dialog.findViewById(R.id.edt_DiemLD);
        edt_DiemTL=dialog.findViewById(R.id.edt_DiemTL);
        btn_Ok=dialog.findViewById(R.id.btn_ok);
        btn_Cancel=dialog.findViewById(R.id.btn_Huy);

        edt_DiemLD.setEnabled(false);
        try {
            int LD=Integer.parseInt(tv_diemLD.getText().toString());
            edt_DiemLD.setText(LD+"");

            int TL=Integer.parseInt(tv_diemTL.getText().toString());
            edt_DiemTL.setText(TL+"");

        }catch (NumberFormatException ex){

        }

        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btn_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_DiemTL.getText().toString().equals("")){
                    Toast.makeText(ResultActivity.this, "Bạn chưa nhập dữ liệu!", Toast.LENGTH_SHORT).show();
                    return;
                }
                int DiemTL;
                DiemTL=Integer.parseInt(edt_DiemTL.getText().toString());

                if (DiemTL<0 || DiemTL>10)
                    Toast.makeText(ResultActivity.this, "Dữ liệu chưa chính xác!", Toast.LENGTH_SHORT).show();
                else {
                    tv_diemTL.setText(DiemTL+"");
                    NhapDiem=2;
                    dialog.cancel();
                }
            }
        });
        dialog.show();
    }

    private void ThemKQ(TextView tv_diemLD) {
        Dialog dialog=new Dialog(ResultActivity.this);
        dialog.setContentView(R.layout.dialog_setdiemld);
        EditText edt_DiemLD;
        Button btn_Ok, btn_Cancel;

        edt_DiemLD=dialog.findViewById(R.id.edt_DiemLD);
        btn_Ok=dialog.findViewById(R.id.btn_ok);
        btn_Cancel=dialog.findViewById(R.id.btn_Huy);

        try {
            int D=Integer.parseInt(tv_diemLD.getText().toString());
            edt_DiemLD.setText(D+"");
        }catch (NumberFormatException ex){

        }

        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btn_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_DiemLD.getText().toString().equals("")){
                    Toast.makeText(ResultActivity.this, "Bạn chưa nhập dữ liệu!", Toast.LENGTH_SHORT).show();
                    return;
                }
                int DiemLD;
                DiemLD=Integer.parseInt(edt_DiemLD.getText().toString());

                if (DiemLD<0 || DiemLD>10)
                    Toast.makeText(ResultActivity.this, "Dữ liệu chưa chính xác!", Toast.LENGTH_SHORT).show();
                else {
                    AlertDialog.Builder alert=new AlertDialog.Builder(ResultActivity.this);
                    alert.setTitle("Cảnh báo!");
                    alert.setMessage("Dữ liệu đã nhập không thể thay đổi! Bạn có muốn tiếp tục?");
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            tv_diemLD.setText(DiemLD+"");
                            NhapDiem=1;
                            dialog.cancel();
                        }
                    });
                    alert.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    alert.show();
                }
            }
        });
        dialog.show();
    }

    private void LoadMon() {
        if (pd.isShowing()==false){
            pd=new ProgressDialog(ResultActivity.this);
            pd.setMessage("Đang tải dữ liệu...");
            pd.show();
        }
        ApiKetQua.apiService.getMhByKyhoc(KyHoc).enqueue(new Callback<List<MonHoc>>() {
            @Override
            public void onResponse(Call<List<MonHoc>> call, Response<List<MonHoc>> response) {
                ds_MH= (ArrayList<MonHoc>) response.body();
                adapter=new KetQuaMHAdapter(R.layout.dong_ketqua_monhoc, ResultActivity.this, ds_MH);
                lv_MH.setAdapter(adapter);

                pd.dismiss();
            }

            @Override
            public void onFailure(Call<List<MonHoc>> call, Throwable t) {

            }
        });
    }

    private void Init() {
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar=getSupportActionBar();
        actionBar.setTitle("Chi tiết kết quả");
        actionBar.setDisplayHomeAsUpEnabled(true);

        tvMa=findViewById(R.id.tv_MaSinhVien);
        tvTen=findViewById(R.id.tv_TenSinhVien);
        tvNS=findViewById(R.id.tv_NgaySinh);
        tvGT=findViewById(R.id.tv_GioiTinh);
        tvThuongTru=findViewById(R.id.tv_ThuongTru);
        tvTamTru=findViewById(R.id.tv_TamTru);
        tvSDT=findViewById(R.id.tv_SDT);
        img_sv=findViewById(R.id.img_sv);

        sp_Kyhoc=findViewById(R.id.sp_KyHoc);
        lv_MH=findViewById(R.id.lv_kqMH);

        ds_KyHoc.add(1);ds_KyHoc.add(2);ds_KyHoc.add(3);ds_KyHoc.add(4);
        ds_KyHoc.add(5);ds_KyHoc.add(6);ds_KyHoc.add(7);ds_KyHoc.add(8);
        adapter_sp=new ArrayAdapter(ResultActivity.this, android.R.layout.simple_list_item_1, ds_KyHoc);
        adapter_sp.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        sp_Kyhoc.setAdapter(adapter_sp);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}