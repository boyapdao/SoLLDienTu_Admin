package com.example.solldientu_admin.Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.solldientu_admin.Adapter.SinhVienAdapter;
import com.example.solldientu_admin.Api.ApiGiaoVien;
import com.example.solldientu_admin.Api.ApiSinhVien;
import com.example.solldientu_admin.Pagination.Pagination;
import com.example.solldientu_admin.Pagination.pSinhVien;
import com.example.solldientu_admin.R;
import com.example.solldientu_admin.object.SinhVien;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StudentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StudentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Student.
     */
    // TODO: Rename and change types and number of parameters
    public static StudentFragment newInstance(String param1, String param2) {
        StudentFragment fragment = new StudentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    View view;
    ListView lvStudent;
    ArrayList<SinhVien> ds_SV = new ArrayList<>();
    FloatingActionButton fabAdd;
    SinhVienAdapter adapter;

    final int REQUEST_CHOOES_PHOTO = 321;
    String realpath = "";
    ImageView img_addSV, img_sv_sua;

    int pos = -1;
    boolean ThayAnh = false, ThemAnh = false;//Check thay ảnh mới

    ProgressDialog pd;
    int page = 1, pageSize = 8, totalSV = -1;

    String QueryText = "";
    boolean userScrolled = false;// Check scroll
    int userScrolledCount = 0;// count scroll
    private static RelativeLayout bottomLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment__student, container, false);
        Init();
        Events();
        Get_All(page, pageSize);
        registerForContextMenu(lvStudent);
        // Inflate the layout for this fragment
        return view;
    }

    private void Get_All(int page, int pageSize) {
        pd = new ProgressDialog(getActivity());
        pd.setMessage("Đang tải dữ liệu....");
        pd.show();
        Pagination p = new Pagination(page, pageSize, "");
        ApiSinhVien.apiService.get_AllSV2(p.getHm()).enqueue(new Callback<pSinhVien>() {
            @Override
            public void onResponse(Call<pSinhVien> call, Response<pSinhVien> response) {
                pd.dismiss();
                ArrayList<SinhVien> ds_sv1 = response.body().getData();
                totalSV = response.body().getTotal();
                if (ds_sv1.size() > 0) {
                    for (int i = 0; i < ds_sv1.size(); i++) {
                        ds_SV.add(ds_sv1.get(i));
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<pSinhVien> call, Throwable t) {

            }
        });
    }

    private void Events() {
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Add();
            }
        });
        lvStudent.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                pos=position;
                return false;
            }
        });
        lvStudent.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (userScrolledCount>0){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (scrollState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                            {
                                userScrolled=true;
                                userScrolledCount=0;
                            }
                        }
                    }, 1500);
                }else {
                    if (scrollState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                    {
                        userScrolled=true;
                        userScrolledCount++;
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (userScrolled && firstVisibleItem+visibleItemCount == totalItemCount && QueryText.equals(""))
                    if (ds_SV.size()<totalSV)
                    {
                        userScrolled=false;
                        UpdateListView();
                    }
                if (userScrolled && firstVisibleItem+visibleItemCount == totalItemCount && !QueryText.equals(""))
                    if (ds_SV.size()<totalSV)
                    {
                        userScrolled=false;
                        UpdateListView2();
                    }
            }
        });

    }

    private void Add() {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.sinhvien_them);
        dialog.setCanceledOnTouchOutside(false);

        EditText edt_TenSV, edt_ThuongTru, edt_TamTru, edt_Sdt,edt_maSV;
        TextView tv_add_NS;
        ImageView img_Calendar;

        AutoCompleteTextView edt_MaLop;
        ArrayList<String> arrMaLopSV = new ArrayList<>();
        ArrayAdapter arrayAdapter;

        RadioButton rb_Nam, rb_Nu;
        Button btn_Add, btn_Huy;

        img_Calendar=dialog.findViewById(R.id.img_Calendar);
        edt_maSV = dialog.findViewById(R.id.edt_add_MaSV);
        edt_TenSV = dialog.findViewById(R.id.edt_add_TenSV);
        tv_add_NS = dialog.findViewById(R.id.tv_add_NS);
        edt_ThuongTru = dialog.findViewById(R.id.edt_add_ThuongTruSV);
        edt_TamTru = dialog.findViewById(R.id.edt_add_TamTruSV);
        edt_Sdt = dialog.findViewById(R.id.edt_add_SdtSV);
        edt_MaLop = dialog.findViewById(R.id.edt_add_MaLopSV);


        rb_Nam = dialog.findViewById(R.id.rb_NamSV);
        rb_Nu = dialog.findViewById(R.id.rb_NuSV);

        btn_Add = dialog.findViewById(R.id.btn_add_SV);
        btn_Huy = dialog.findViewById(R.id.btn_HuyaddSV);

        img_addSV = dialog.findViewById(R.id.image_add_sinhvien);

        ApiSinhVien.apiService.getallMaLopSV().enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                ArrayList<String> list_Ma = (ArrayList<String>) response.body();
                if (list_Ma.size() > 0) {
                    for (int i = 0; i < list_Ma.size(); i++) {
                        arrMaLopSV.add(list_Ma.get(i));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }
        });
        arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.select_dialog_item,arrMaLopSV);
        edt_MaLop.setAdapter(arrayAdapter);

        img_Calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChonNgay(tv_add_NS);
            }
        });

        img_addSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Chooes_Photo();
            }
        });
        btn_Huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new ProgressDialog(getActivity());
                pd.setMessage("Đang thêm sinh viên.....");
                pd.show();

                String maSV,Ten, ThuongTru, TamTru, Anh = "", NS, MaLop, Sdt;
                int GT;
                maSV = edt_maSV.getText().toString();
                Ten = edt_TenSV.getText().toString();
                ThuongTru = edt_ThuongTru.getText().toString();
                TamTru = edt_TamTru.getText().toString();
                NS = tv_add_NS.getText().toString();
                MaLop = edt_MaLop.getText().toString();
                Sdt = edt_Sdt.getText().toString();
                if (rb_Nam.isChecked())
                    GT = 1;
                else
                    GT = 0;

                if (ThemAnh) {
                    //Add Image
                    File file = new File(realpath);
                    String file_path = file.getAbsolutePath();

                    String[] tenfile1 = file_path.split("/");
                    //        Log.d("FILE_PATH", file_path);
                    //trường hợp trùng tên file thì + thêm thời gian vào tên file
                    String[] tenfile2 = tenfile1[tenfile1.length-1].split("\\.");

                    //Gán vào ảnh
                    tenfile1[tenfile1.length-1] = tenfile2[0] + System.currentTimeMillis() + "." + tenfile2[1];
                    Anh = tenfile1[tenfile1.length-1];

                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("files", Anh, requestBody);

                    //API ThemAnh
                    ApiSinhVien.apiService.UploadPhoto(body).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
//                            Toast.makeText(getActivity(), "ok success Image! " + response.toString(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(getActivity(), "oh fail Image! " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                //new object
                SinhVien sv = new SinhVien(maSV,Ten, NS, GT, ThuongTru, TamTru, Sdt, Anh, MaLop);
                ApiSinhVien.apiService.addSinhVien(sv).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        pd.dismiss();
                        Toast.makeText(getActivity(), "Success !", Toast.LENGTH_SHORT).show();
                        ds_SV.clear();
                        page=1;
                        Get_All(page,pageSize);
                        dialog.cancel();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getActivity(), "fail!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        dialog.show();
    }

    private void UpdateListView() {
        bottomLayout.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                page++;
//                Get_All(page, pageSize);
                Pagination p=new Pagination(page, pageSize,"");
                ApiSinhVien.apiService.get_AllSV2(p.getHm()).enqueue(new Callback<pSinhVien>() {
                    @Override
                    public void onResponse(Call<pSinhVien> call, Response<pSinhVien> response) {
                        pd.dismiss();

                        ArrayList<SinhVien> ds_sv1=response.body().getData();
                        totalSV=response.body().getTotal();

                        if (ds_sv1.size()>0){
                            for (int i=0;i < ds_sv1.size(); i++)
                            {
                                ds_SV.add(ds_sv1.get(i));
                            }
                            adapter.notifyDataSetChanged();
                            bottomLayout.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<pSinhVien> call, Throwable t) {

                    }
                });
//                Toast.makeText(getActivity(), "Items Updated.", Toast.LENGTH_SHORT).show();
            }
        }, 1000);
    }

    private void UpdateListView2() {
    }

    private void ChonNgay(TextView tv_ns) {
        SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
        Date DateDefault=null;
        try {
            DateDefault = sdf.parse(tv_ns.getText().toString());
        }catch (ParseException e){
            e.printStackTrace();
        }
        SimpleDateFormat sdf_day=new SimpleDateFormat("dd");
        SimpleDateFormat sdf_month=new SimpleDateFormat("MM");
        SimpleDateFormat sdf_year=new SimpleDateFormat("yyyy");

        int ngay=Integer.parseInt(sdf_day.format(DateDefault));
        int thang=Integer.parseInt(sdf_month.format(DateDefault));
        thang-=1;//Vì tháng trong DatePicker bị nên 1 ký tự
        //Còn trong Calendar tháng từ 0-->11
        int nam=Integer.parseInt(sdf_year.format(DateDefault));

        DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month+=1;

                String d, m, y;
                d=dayOfMonth+"";
                m=month+"";
                y=year+"";

                if (dayOfMonth<10)
                    d="0"+dayOfMonth;
                if (month<10)
                    m="0"+month;

                tv_ns.setText(d+"/"+m+"/"+y);
            }
        },nam,thang,ngay);
        datePickerDialog.show();
    }

    private void Init() {
        fabAdd = view.findViewById(R.id.fab_AddStudent);
        lvStudent = view.findViewById(R.id.lv_student);
        adapter = new SinhVienAdapter(getActivity(), R.layout.sinhvien_adapter, ds_SV);
        lvStudent.setAdapter(adapter);
        bottomLayout=getActivity().findViewById(R.id.loadItemsLayout_listView);
    }

    private void Chooes_Photo() {//Chọn hình
        Intent it = new Intent(Intent.ACTION_PICK);
        it.setType("image/*");
        startActivityForResult(it, REQUEST_CHOOES_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CHOOES_PHOTO) {//Chọn hình
                try {
                    Uri imageUri = data.getData();
                    realpath = getRealPathFromURI(imageUri);

                    InputStream is = getActivity().getContentResolver().openInputStream(imageUri);
                    Bitmap b = BitmapFactory.decodeStream(is);
                    Bitmap bitmap = Bitmap.createScaledBitmap(b, 1000, 1000, false);
                    try {
                        img_addSV.setImageBitmap(bitmap);//setImageView khi chọn hình
                        ThemAnh = true;
                    } catch (Exception e) {
                        ThemAnh = false;
                    }
                    try {
                        img_sv_sua.setImageBitmap(bitmap);
                        ThayAnh = true;
                    } catch (Exception e) {
                        ThayAnh = false;
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getRealPathFromURI(Uri contentUri) {
        String path = null;
        String[] proj = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.menu_context, menu);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem mSearchMenu=menu.findItem(R.id.mn_search);
        SearchView searchView= (SearchView) mSearchMenu.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Get_AllSearch(page, pageSize, query);
                Toast.makeText(getActivity(), "abc!", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                QueryText=newText;
                return false;
            }
        });
        MenuItem mn=menu.findItem(R.id.mn_refresh);
        mn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                page=1;pageSize=8;QueryText="";
                ds_SV.clear();
                Get_All(page,pageSize);
                return false;
            }
        });
    }

    private void Get_AllSearch(int page, int pageSize, String query) {
        pd=new ProgressDialog(getActivity());
        pd.setMessage("Đang tìm kiếm dữ liệu....");
        pd.show();

        ds_SV.clear();
        Pagination p=new Pagination(page, pageSize,query);
        ApiSinhVien.apiService.searchSV(p.getHm()).enqueue(new Callback<pSinhVien>() {
            @Override
            public void onResponse(Call<pSinhVien> call, Response<pSinhVien> response) {
                pd.dismiss();

                ArrayList<SinhVien> ds_sv1=response.body().getData();
                totalSV=response.body().getTotal();

                if (ds_sv1.size()>0){
                    for (int i=0;i < ds_sv1.size(); i++)
                        ds_SV.add(ds_sv1.get(i));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<pSinhVien> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.mn_Sua:
                Update();
                break;
            case R.id.mn_Xoa:
                Delete();
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void Update() {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.sinhvien_sua);
        dialog.setCanceledOnTouchOutside(false);

        EditText edt_TenSV, edt_ThuongTru, edt_TamTru, edt_Sdt;
        TextView txt_maSV, tv_Sua_NS;
        RadioButton rb_Nam, rb_Nu;
        Button btn_Sua, btn_Huy;
        ImageView img_Calendar;

        img_sv_sua = dialog.findViewById(R.id.image_sua_sinhvien);
        img_Calendar=dialog.findViewById(R.id.img_Calendar);
        txt_maSV =(TextView) dialog.findViewById(R.id.txt_sua_MaSV);
        edt_TenSV = dialog.findViewById(R.id.edt_sua_TenSV);
        tv_Sua_NS = dialog.findViewById(R.id.tv_Sua_NS);
        edt_ThuongTru = dialog.findViewById(R.id.edt_sua_ThuongTruSV);
        edt_TamTru = dialog.findViewById(R.id.edt_sua_TamTruSV);
        edt_Sdt = dialog.findViewById(R.id.edt_sua_SdtSV);
        rb_Nam = dialog.findViewById(R.id.rb_NamSV);
        rb_Nu = dialog.findViewById(R.id.rb_NuSV);
        btn_Sua = dialog.findViewById(R.id.btn_sua_SV);
        btn_Huy = dialog.findViewById(R.id.btn_HuysuaSV);

        AutoCompleteTextView edt_MaLopSV;
        edt_MaLopSV = dialog.findViewById(R.id.edt_sua_MaLopSV);
        ArrayList<String> list_maLop = new ArrayList<>();
        ArrayAdapter arrayAdapter;
        ApiSinhVien.apiService.getallMaLopSV().enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                ArrayList<String> list_Ma = (ArrayList<String>) response.body();
                Log.d("MANG", "onResponse: "+list_Ma.size());
                if (list_Ma.size() > 0) {
                    for (int i = 0; i < list_Ma.size(); i++) {
                        list_maLop.add(list_Ma.get(i));
                        Log.d("MANGAFTER", "onResponse: "+list_maLop.get(i));
                    }
                    Log.d("MANGAFTER", "onResponse: "+list_maLop.size());
                    Toast.makeText(getActivity(), "Size "+list_Ma.size()+"Size1:  "+list_maLop.size(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }
        });
        arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,list_maLop);
        edt_MaLopSV.setAdapter(arrayAdapter);

        SinhVien sv = ds_SV.get(pos);
        if (!sv.getAnh().equals("")){
            String[] tenfile=sv.getAnh().split("\\.");
            Glide.with(getActivity()).load(ApiSinhVien.url+"GetImage/"+tenfile[0]).into(img_sv_sua);
        }
        txt_maSV.setText(sv.getMaSv());
        edt_TenSV.setText(sv.getTenSv());
        tv_Sua_NS.setText(sv.getNgaySinh());
        if(sv.getGioiTinh()==0)
            rb_Nu.setChecked(true);
        else
            rb_Nam.setChecked(true);

        edt_ThuongTru.setText(sv.getThuongTru());
        edt_TamTru.setText(sv.getTamTru());
        edt_Sdt.setText(sv.getSdt());
        edt_MaLopSV.setText(sv.getMaLop());

        img_Calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChonNgay(tv_Sua_NS);
            }
        });
        btn_Huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        img_sv_sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Chooes_Photo();
            }
        });
        btn_Sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd=new ProgressDialog(getActivity());
                pd.setMessage("Đang cập nhật.....");
                pd.show();

                String maSV,Ten, ThuongTru, TamTru, Anh = ds_SV.get(pos).getAnh(), NS, MaLop, Sdt;
                int GT;

                if (ThayAnh && !Anh.equals("")){//Nếu ảnh đã có thì xóa ròi thêm
                    //DeleteImage
                    ApiSinhVien.apiService.DeleteImage(ds_SV.get(pos).getAnh()).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {

                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {

                        }
                    });
                    //Update Image
                    //Add Image
                    File file=new File(realpath);
                    String file_path=file.getAbsolutePath();

                    Log.d("AQE", file_path+" zzzzz");

                    String[] tenfile1=file_path.split("/");
                    //        Log.d("FILE_PATH", file_path);
                    //trường hợp trùng tên file thì + thêm thời gian vào tên file
                    String[] tenfile2=tenfile1[tenfile1.length-1].split("\\.");

                    //Gán vào Anh
                    tenfile1[tenfile1.length-1]=tenfile2[0]+System.currentTimeMillis()+"."+tenfile2[1];
                    Anh=tenfile1[tenfile1.length-1];

                    RequestBody requestBody=RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part body=MultipartBody.Part.createFormData("files", Anh, requestBody);

                    //API ThemAnh
                    ApiSinhVien.apiService.UploadPhoto(body).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(getActivity(), "oh fail! "+t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {//Nếu chưa có ảnh thì chỉ thêm
                    //Add Image
                    if (ds_SV.get(pos).getAnh().equals("") && ThayAnh){
                        File file=new File(realpath);
                        String file_path=file.getAbsolutePath();

                        Log.d("AQE", file_path+" zzzzz");

                        String[] tenfile1=file_path.split("/");
                        //        Log.d("FILE_PATH", file_path);
                        //trường hợp trùng tên file thì + thêm thời gian vào tên file
                        String[] tenfile2=tenfile1[tenfile1.length-1].split("\\.");

                        //Gán vào Anh
                        tenfile1[tenfile1.length-1]=tenfile2[0]+System.currentTimeMillis()+"."+tenfile2[1];
                        Anh=tenfile1[tenfile1.length-1];

                        RequestBody requestBody=RequestBody.create(MediaType.parse("multipart/form-data"), file);
                        MultipartBody.Part body=MultipartBody.Part.createFormData("files", Anh, requestBody);

                        //API ThemAnh
                        ApiSinhVien.apiService.UploadPhoto(body).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {

                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Toast.makeText(getActivity(), "oh fail! "+t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                //Update object
                maSV = txt_maSV.getText().toString();
                Ten = edt_TenSV.getText().toString();
                ThuongTru = edt_ThuongTru.getText().toString();
                TamTru = edt_TamTru.getText().toString();
                NS = tv_Sua_NS.getText().toString();
                MaLop = edt_MaLopSV.getText().toString();
                Sdt = edt_Sdt.getText().toString();
                if (rb_Nam.isChecked()) {
                    GT = 1;
                } else {
                    GT = 0;
                }
                SinhVien object=new SinhVien(Ten, NS, GT, ThuongTru, TamTru, Sdt, Anh, MaLop);
                ApiSinhVien.apiService.updateSinhVien(maSV, object)
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                pd.dismiss();
                                Toast.makeText(getActivity(), "Success!", Toast.LENGTH_SHORT).show();
                                page=1;
                                ds_SV.clear();
                                Get_All(page, pageSize);
                                dialog.cancel();
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(getActivity(), "Fail!", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
        dialog.show();
    }

    private void Delete() {
        AlertDialog.Builder aBuilder = new AlertDialog.Builder(getActivity());
        aBuilder.setTitle("Xóa sinh viên ");
        aBuilder.setMessage("Bạn có chắc chắn muốn xóa !");
        aBuilder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pd=new ProgressDialog(getActivity());
                pd.setMessage("Đang xóa......");
                pd.show();
                ApiSinhVien.apiService.sendDeleteSV(ds_SV.get(pos).getMaSv()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        if(!ds_SV.get(pos).getAnh().equals("")){
                            ApiSinhVien.apiService.DeleteImage(ds_SV.get(pos).getAnh()).enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {

                                }
                            });

                        }
                        pd.dismiss();
                        Toast.makeText(getActivity(), "Đã xóa!", Toast.LENGTH_SHORT).show();

                        page =1;
                        ds_SV.clear();
                        Get_All(page,pageSize);
                        dialog.cancel();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
            }
        });
        aBuilder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        aBuilder.show();
    }
}