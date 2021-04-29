package com.example.solldientu_admin;

import android.app.AlertDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.solldientu_admin.Adapter.GiaoVienAdapter;
import com.example.solldientu_admin.Api.ApiGiaoVien;
import com.example.solldientu_admin.Pagination.Pagination;
import com.example.solldientu_admin.Pagination.pGiaoVien;
import com.example.solldientu_admin.object.GiaoVien;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TeacherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeacherFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TeacherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Teacher.
     */
    // TODO: Rename and change types and number of parameters
    public static TeacherFragment newInstance(String param1, String param2) {
        TeacherFragment fragment = new TeacherFragment();
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
    ListView lvTeacher;
    ArrayList<GiaoVien> ds_GV=new ArrayList<>();
    FloatingActionButton fabAdd;
    GiaoVienAdapter adapter;

    final int REQUEST_CHOOES_PHOTO=321;
    String realpath="";
    ImageView img_add, img_gv_sua;
    int pos=-1;
    boolean ThayAnh=false, ThemAnh=false;//Check thay ảnh mới
    ProgressDialog pd;

    int page=1, pageSize=8, totalGV=-1;
    String QueryText="";

    boolean userScrolled = false;// Check scroll
    int userScrolledCount=0;// count scroll
    private static RelativeLayout bottomLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment__teacher, container, false);
        // Inflate the layout for this fragment
        Init();
        Events();
        Get_All(page, pageSize);
        registerForContextMenu(lvTeacher);
        return view;
    }

    private void Get_All(int page, int pageSize) {
        pd=new ProgressDialog(getActivity());
        pd.setMessage("Đang tải dữ liệu....");
        pd.show();

        Pagination p=new Pagination(page, pageSize,"");
        ApiGiaoVien.apiService.get_All2(p.getHm()).enqueue(new Callback<pGiaoVien>() {
            @Override
            public void onResponse(Call<pGiaoVien> call, Response<pGiaoVien> response) {
                pd.dismiss();

                ArrayList<GiaoVien> ds_gv1=response.body().getData();
                totalGV=response.body().getTotal();

                if (ds_gv1.size()>0){
                    for (int i=0;i < ds_gv1.size(); i++)
                        ds_GV.add(ds_gv1.get(i));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<pGiaoVien> call, Throwable t) {

            }
        });
    }

    private void Events() {
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog=new Dialog(getActivity());
                dialog.setContentView(R.layout.giaovien_them);

                EditText edt_Ten, edt_NS, edt_Que;
                RadioButton rb_Nam, rb_Nu;
                Button btn_add, btn_Huy;

                edt_Ten=dialog.findViewById(R.id.edt_add_TenGV);
                edt_NS=dialog.findViewById(R.id.edt_add_NS);
                edt_Que=dialog.findViewById(R.id.edt_add_Que);
                btn_add=dialog.findViewById(R.id.btn_add_GV);
                btn_Huy=dialog.findViewById(R.id.btn_Huy);
                rb_Nam=dialog.findViewById(R.id.rb_Nam);
                rb_Nu=dialog.findViewById(R.id.rb_Nu);
                img_add=dialog.findViewById(R.id.image_add_giaovien);

                img_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Chooes_Photo();
                    }
                });
                btn_Huy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                btn_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pd=new ProgressDialog(getActivity());
                        pd.setMessage("Đang thêm giáo viên.....");
                        pd.show();

                        String Ten, Que, Anh="", NS;
                        int GT;
                        Ten=edt_Ten.getText().toString();
                        Que=edt_Que.getText().toString();
                        NS=edt_NS.getText().toString();
                        if (rb_Nam.isChecked())
                            GT=1;
                        else GT=0;

                        if (ThemAnh){
                            //Add Image
                            File file=new File(realpath);
                            String file_path=file.getAbsolutePath();

                            String[] tenfile1=file_path.split("/");
                            //        Log.d("FILE_PATH", file_path);
                            //trường hợp trùng tên file thì + thêm thời gian vào tên file
                            String[] tenfile2=tenfile1[tenfile1.length-1].split("\\.");

                            //Gán vào ảnh
                            tenfile1[tenfile1.length-1]=tenfile2[0]+System.currentTimeMillis()+"."+tenfile2[1];
                            Anh=tenfile1[tenfile1.length-1];

                            RequestBody requestBody=RequestBody.create(MediaType.parse("multipart/form-data"), file);
                            MultipartBody.Part body=MultipartBody.Part.createFormData("files", Anh, requestBody);

                            //API ThemAnh
                            ApiGiaoVien.apiService.UploadPhoto(body).enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    Toast.makeText(getActivity(), "ok success Image! "+response.toString(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Toast.makeText(getActivity(), "oh fail Image! "+t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        //new object
                        GiaoVien gv=new GiaoVien(Ten, Que, Anh, NS, GT);
                        ApiGiaoVien.apiService.sendPosts(gv).enqueue(new Callback<GiaoVien>() {
                            @Override
                            public void onResponse(Call<GiaoVien> call, Response<GiaoVien> response) {
                                pd.dismiss();
                                Toast.makeText(getActivity(), "ok!", Toast.LENGTH_SHORT).show();
                                ds_GV.clear();
                                page=1;
                                Get_All(page, pageSize);
                                dialog.cancel();
                            }
                            @Override
                            public void onFailure(Call<GiaoVien> call, Throwable t) {
                                Toast.makeText(getActivity(), "fail!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                dialog.show();
            }
        });
        lvTeacher.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                pos=i;
                return false;
            }
        });
        lvTeacher.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (userScrolledCount>0){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (i== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                            {
                                userScrolled=true;
                                userScrolledCount=0;
                            }
                        }
                    }, 1500);
                }else {
                    if (i== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                    {
                        userScrolled=true;
                        userScrolledCount++;
                    }
                }
            }
//            firstVisibleItem(i) + visibleItemCount(i1) == totalItemCount(i2)
            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if (userScrolled && i+i1 == i2 && QueryText.equals(""))
                    if (ds_GV.size()<totalGV)
                    {
                        userScrolled=false;
                        UpdateListView();
                    }
                if (userScrolled && i+i1 == i2 && !QueryText.equals(""))
                    if (ds_GV.size()<totalGV)
                    {
                        userScrolled=false;
                        UpdateListView2();
                    }
            }
        });
    }

    private void UpdateListView2() {//Phân trang Search

    }

    private void UpdateListView() {
        bottomLayout.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                page++;
//                Get_All(page, pageSize);
                Pagination p=new Pagination(page, pageSize,"");
                ApiGiaoVien.apiService.get_All2(p.getHm()).enqueue(new Callback<pGiaoVien>() {
                    @Override
                    public void onResponse(Call<pGiaoVien> call, Response<pGiaoVien> response) {
                        pd.dismiss();

                        ArrayList<GiaoVien> ds_gv1=response.body().getData();
                        totalGV=response.body().getTotal();

                        if (ds_gv1.size()>0){
                            for (int i=0;i < ds_gv1.size(); i++)
                            {
                                ds_GV.add(ds_gv1.get(i));
                            }
                            adapter.notifyDataSetChanged();
                            bottomLayout.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<pGiaoVien> call, Throwable t) {

                    }
                });
//                Toast.makeText(getActivity(), "Items Updated.", Toast.LENGTH_SHORT).show();
            }
        }, 1000);
    }

    private void Init() {
        fabAdd=view.findViewById(R.id.fab_AddTeacher);
        lvTeacher=view.findViewById(R.id.lv_teacher);
        adapter=new GiaoVienAdapter(getActivity(), R.layout.dong_giaovien, ds_GV);
        lvTeacher.setAdapter(adapter);
        bottomLayout=getActivity().findViewById(R.id.loadItemsLayout_listView);
    }

    private void Chooes_Photo(){//Chọn hình
        Intent it=new Intent(Intent.ACTION_PICK);
        it.setType("image/*");
        startActivityForResult(it,REQUEST_CHOOES_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==RESULT_OK){
            if(requestCode==REQUEST_CHOOES_PHOTO){//Chọn hình
                try {
                    Uri imageUri=data.getData();
                    realpath=getRealPathFromURI(imageUri);

                    InputStream is=getActivity().getContentResolver().openInputStream(imageUri);
                    Bitmap b= BitmapFactory.decodeStream(is);
                    Bitmap bitmap=Bitmap.createScaledBitmap(b,1000,1000,false);
                    try {
                        img_add.setImageBitmap(bitmap);//setImageView khi chọn hình
                        ThemAnh=true;
                    }catch (Exception e){
                        ThemAnh=false;
                    }
                    try {
                        img_gv_sua.setImageBitmap(bitmap);
                        ThayAnh=true;
                    }catch (Exception e){
                        ThayAnh=false;
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getRealPathFromURI(Uri contentUri){
        String path=null;
        String[] proj={MediaStore.MediaColumns.DATA};
        Cursor cursor=getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()){
            int column_index=cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path=cursor.getString(column_index);
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
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        switch(id){
            case R.id.mn_Sua:
                Update();
                break;
            case R.id.mn_Xoa:
                Delete();
                break;
        }
        return super.onContextItemSelected(item);
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
                ds_GV.clear();
                Get_All(page,pageSize);
                return false;
            }
        });
    }

    private void Get_AllSearch(int page, int pageSize, String TenGV) {
        pd=new ProgressDialog(getActivity());
        pd.setMessage("Đang tìm kiếm dữ liệu....");
        pd.show();

        ds_GV.clear();
        Pagination p=new Pagination(page, pageSize,TenGV);
        ApiGiaoVien.apiService.search(p.getHm()).enqueue(new Callback<pGiaoVien>() {
            @Override
            public void onResponse(Call<pGiaoVien> call, Response<pGiaoVien> response) {
                pd.dismiss();

                ArrayList<GiaoVien> ds_gv1=response.body().getData();
                totalGV=response.body() .getTotal();

                if (ds_gv1.size()>0){
                    for (int i=0;i < ds_gv1.size(); i++)
                        ds_GV.add(ds_gv1.get(i));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<pGiaoVien> call, Throwable t) {

            }
        });
    }

    private void Delete() {
//        Log.d("MAZV", ds_GV.get(0).getMaGv()+" ZX ");
        AlertDialog.Builder alert=new AlertDialog.Builder(getActivity());
        alert.setTitle("Xóa giáo viên");
        alert.setMessage("Bạn có chắc chắn muốn xóa?");
        alert.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                pd=new ProgressDialog(getActivity());
                pd.setMessage("Đang xóa......");
                pd.show();

                //delete Image
                if (!ds_GV.get(pos).getAnh().equals("")){
                    ApiGiaoVien.apiService.DeleteImage(ds_GV.get(pos).getAnh()).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
//                        Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {

                        }
                    });
                }

                //delete object
                ApiGiaoVien.apiService.sendDelete(ds_GV.get(pos).getMaGv()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        pd.dismiss();
                        Toast.makeText(getActivity(), "Đã xóa!", Toast.LENGTH_SHORT).show();
                        page=1;
                        ds_GV.clear();
                        Get_All(page, pageSize);
                        dialogInterface.cancel();
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
                dialogInterface.cancel();
            }
        });
        alert.show();

    }


    private void Update() {
        Dialog dialog=new Dialog(getActivity());
        dialog.setContentView(R.layout.giaovien_sua);
        EditText edt_Ten, edt_NS, edt_Que;
        RadioButton rb_Nam, rb_Nu;
        Button btn_Sua, btn_Huy;

        img_gv_sua=dialog.findViewById(R.id.image_sua_giaovien);
        edt_Ten=dialog.findViewById(R.id.edt_sua_TenGV);
        edt_NS=dialog.findViewById(R.id.edt_sua_NS);
        edt_Que=dialog.findViewById(R.id.edt_sua_Que);
        rb_Nam=dialog.findViewById(R.id.rb_Nam);
        rb_Nu=dialog.findViewById(R.id.rb_Nu);
        btn_Sua=dialog.findViewById(R.id.btn_sua_GV);
        btn_Huy=dialog.findViewById(R.id.btn_Huy);

        GiaoVien gv=ds_GV.get(pos);
        if (!gv.getAnh().equals("")){
            img_gv_sua.setImageBitmap(null);
            String[] tenfile=gv.getAnh().split("\\.");
            Glide.with(getActivity()).load(ApiGiaoVien.url+"GetImage/"+tenfile[0]).into(img_gv_sua);
        }
        edt_Ten.setText(gv.getTenGv());
        edt_NS.setText(gv.getNgaySinh());
        edt_Que.setText(gv.getQueQuan());
        if (gv.getGioiTinh()==0)
            rb_Nu.setChecked(true);
        else rb_Nam.setChecked(true);
        btn_Huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        img_gv_sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Chooes_Photo();
            }
        });
        btn_Sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd=new ProgressDialog(getActivity());
                pd.setMessage("Đang cập nhật.....");
                pd.show();

                String Ten, Que, Anh=ds_GV.get(pos).getAnh(), NS;
                int GT;

                if (ThayAnh && !Anh.equals("")){//Nếu ảnh đã có thì xóa ròi thêm
                    //DeleteImage
                    ApiGiaoVien.apiService.DeleteImage(ds_GV.get(pos).getAnh()).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
//                        Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
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
                    ApiGiaoVien.apiService.UploadPhoto(body).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
//                        Toast.makeText(getActivity(), "ok success! "+response.toString(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(getActivity(), "oh fail! "+t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{//Nếu chưa có ảnh thì chỉ thêm
                    //Add Image
                    if (ds_GV.get(pos).getAnh().equals("") && ThayAnh){
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
                        ApiGiaoVien.apiService.UploadPhoto(body).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
//                        Toast.makeText(getActivity(), "ok success! "+response.toString(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Toast.makeText(getActivity(), "oh fail! "+t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                //Update object
                Ten=edt_Ten.getText().toString();
                Que=edt_Que.getText().toString();
                NS=edt_NS.getText().toString();
                if (rb_Nam.isChecked())
                    GT=1;
                else GT=0;

                GiaoVien object=new GiaoVien(Ten, Que, Anh, NS, GT);
                ApiGiaoVien.apiService.putPost(ds_GV.get(pos).getMaGv(), object)
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                pd.dismiss();
                                Toast.makeText(getActivity(), "Success!", Toast.LENGTH_SHORT).show();
                                page=1;
                                ds_GV.clear();
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
}