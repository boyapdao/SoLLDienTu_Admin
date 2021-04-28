package com.example.solldientu_admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.solldientu_admin.Adapter.LopAdapter;
import com.example.solldientu_admin.Api.ApiGiaoVien;
import com.example.solldientu_admin.Api.ApiLopHoc;
import com.example.solldientu_admin.Api.ApiSinhVien;
import com.example.solldientu_admin.Pagination.Pagination;
import com.example.solldientu_admin.Pagination.pGiaoVien;
import com.example.solldientu_admin.Pagination.pLopHoc;
import com.example.solldientu_admin.object.GiaoVien;
import com.example.solldientu_admin.object.LopHoc;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClassFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ClassFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Class.
     */
    // TODO: Rename and change types and number of parameters
    public static ClassFragment newInstance(String param1, String param2) {
        ClassFragment fragment = new ClassFragment();
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
    String tennGV="";
    String maGVLop="";
    String maGV="",maGV1="";
    View view;
    ListView lvClass;
    ArrayList<LopHoc> ds_Lop = new ArrayList<>();
    FloatingActionButton fabAddLop;
    LopAdapter adapter;

    ProgressDialog pd;
    int page = 1, pageSize = 8, totalLop = -1;
    String QueryText = "";

    int pos = -1;

    boolean userScrolled = false;// Check scroll
    int userScrolledCount = 0;// count scroll
    private RelativeLayout bottomLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment__class, container, false);
        Init();
        Event();
        Get_All(page, pageSize);
        registerForContextMenu(lvClass);
        return view;
    }

    private void Event() {
        fabAddLop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialogAddClass = new Dialog(getContext());
                dialogAddClass.setContentView(R.layout.lop_them);
                EditText edt_tenLop,edt_maLop;
                AutoCompleteTextView edt_TenGV;
                Button btn_addLop, btn_addHuyAdd;

                edt_maLop = (EditText) dialogAddClass.findViewById(R.id.edt_add_MaLop);
                edt_tenLop = (EditText) dialogAddClass.findViewById(R.id.edt_add_TenLop);
                edt_TenGV = (AutoCompleteTextView) dialogAddClass.findViewById(R.id.edt_add_tenGvLop);


                btn_addLop = (Button) dialogAddClass.findViewById(R.id.btn_add_Lop);
                btn_addHuyAdd = (Button) dialogAddClass.findViewById(R.id.btn_HuyAddLop);
                ArrayList<GiaoVien> arrmaGV = new ArrayList<>();
                ApiLopHoc.apiService.getMaTenGVLop().enqueue(new Callback<List<GiaoVien>>() {
                    @Override
                    public void onResponse(Call<List<GiaoVien>> call, Response<List<GiaoVien>> response) {
                        ArrayList<GiaoVien> ds_maGV = (ArrayList<GiaoVien>) response.body();
                        if (ds_maGV.size() > 0) {
                            for (int i = 0; i < ds_maGV.size(); i++) {
                                arrmaGV.add(ds_maGV.get(i));
                            }
                            ArrayAdapter<GiaoVien> arrayAdapter = new ArrayAdapter<GiaoVien>(getActivity(), android.R.layout.simple_list_item_1, arrmaGV);
                            edt_TenGV.setAdapter(arrayAdapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<GiaoVien>> call, Throwable t) {

                    }
                });



                btn_addLop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pd = new ProgressDialog(getActivity());
                        pd.setMessage("Đang thêm lớp.....");
                        pd.show();
                        String tenGV="";
                        String ten, maGVLop="",maLop;
                        ten = edt_tenLop.getText().toString();
                        maLop = edt_maLop.getText().toString();
                        tenGV=edt_TenGV.getText().toString();
                        maGVLop = tenGV.substring(tenGV.indexOf("-")+1);
                        Log.d("NameTenGV", "onClick: "+maGVLop);
                        LopHoc lop = new LopHoc(maLop,ten, maGVLop);
                        ApiLopHoc.apiService.postAddLop(lop).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                pd.dismiss();
                                Toast.makeText(getActivity(), " Success !", Toast.LENGTH_SHORT).show();
                                ds_Lop.clear();
                                page = 1;
                                Get_All(page, pageSize);
                                dialogAddClass.cancel();
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }
                        });

                    }

                });
                btn_addHuyAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogAddClass.cancel();
                    }
                });
                dialogAddClass.show();
            }
        });
        lvClass.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
                return false;
            }
        });
        lvClass.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                    if (ds_Lop.size()<totalLop)
                    {
                        userScrolled=false;
                        UpdateListView();
                    }
                if (userScrolled && firstVisibleItem+visibleItemCount == totalItemCount && !QueryText.equals(""))
                    if (ds_Lop.size()<totalLop)
                    {
                        userScrolled=false;
                        UpdateListView2();
                    }
            }
        });
    }

    private void UpdateListView2() {
    }

    private void UpdateListView() {
        bottomLayout.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                page++;
//                Get_All(page, pageSize);
                Pagination p=new Pagination(page, pageSize,"");
                ApiLopHoc.apiService.get_All2Lop(p.getHm()).enqueue(new Callback<pLopHoc>() {
                    @Override
                    public void onResponse(Call<pLopHoc> call, Response<pLopHoc> response) {
                        pd.dismiss();

                        ArrayList<LopHoc> ds_lop1=response.body().getData();
                        totalLop=response.body().getTotal();

                        if (ds_lop1.size()>0){
                            for (int i=0;i < ds_lop1.size(); i++)
                            {
                                ds_Lop.add(ds_lop1.get(i));
                            }
                            adapter.notifyDataSetChanged();
                            bottomLayout.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<pLopHoc> call, Throwable t) {

                    }
                });
//                Toast.makeText(getActivity(), "Items Updated.", Toast.LENGTH_SHORT).show();
            }
        }, 1000);
    }

    private void Get_All(int page, int pageSize) {
        pd = new ProgressDialog(getActivity());
        pd.setMessage("Đang tải dữ liệu....");
        pd.show();
        ds_Lop.clear();
        Pagination p = new Pagination(page, pageSize, "");
        ApiLopHoc.apiService.get_All2Lop(p.getHm()).enqueue(new Callback<pLopHoc>() {
            @Override
            public void onResponse(Call<pLopHoc> call, Response<pLopHoc> response) {

                ArrayList<LopHoc> ds_lop1 = response.body().getData();
//                Log.d("BBB", "onResponse: "+ds_lop1.size()+" ZZZZ");
                Toast.makeText(getActivity(), " helo :  " + ds_lop1.size(), Toast.LENGTH_LONG).show();
                totalLop = response.body().getTotal();
                if (ds_lop1.size() > 0) {
                    for (int i = 0; i < ds_lop1.size(); i++) {
                        ds_Lop.add(ds_lop1.get(i));
                    }
                    adapter.notifyDataSetChanged();
                    pd.dismiss();
                }
//                pd.dismiss();
            }

            @Override
            public void onFailure(Call<pLopHoc> call, Throwable t) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Init() {
        fabAddLop = view.findViewById(R.id.fab_AddClass);
        lvClass = view.findViewById(R.id.lv_class);
        adapter = new LopAdapter(getActivity(), R.layout.lop_adapter, ds_Lop);
        lvClass.setAdapter(adapter);
        bottomLayout = getActivity().findViewById(R.id.loadItemsLayout_listView);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem mSearchMenu = menu.findItem(R.id.mn_search);
        SearchView searchView = (SearchView) mSearchMenu.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Get_AllSearch(page, pageSize, query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                QueryText = newText;
                return false;
            }
        });
        MenuItem mn = menu.findItem(R.id.mn_refresh);
        mn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                page = 1;
                pageSize = 8;
                QueryText = "";
                ds_Lop.clear();
                Get_All(page, pageSize);
                return false;
            }
        });
    }

    private void Get_AllSearch(int page, int pageSize, String ten) {
        pd = new ProgressDialog(getActivity());
        pd.setMessage("Đang tìm kiếm dữ liệu....");
        pd.show();
        ds_Lop.clear();
        Pagination p = new Pagination(page, pageSize, ten);
        ApiLopHoc.apiService.searchTenLop(p.getHm()).enqueue(new Callback<pLopHoc>() {
            @Override
            public void onResponse(Call<pLopHoc> call, Response<pLopHoc> response) {
                pd.dismiss();
                ArrayList<LopHoc> ds_lop1 = response.body().getData();
                totalLop = response.body().getTotal();
                if (ds_lop1.size() > 0) {
                    for (int i = 0; i < ds_lop1.size(); i++) {
                        ds_Lop.add(ds_lop1.get(i));
                    }
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<pLopHoc> call, Throwable t) {

            }
        });
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.menu_context, menu);

        super.onCreateContextMenu(menu, v, menuInfo);
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

    private void Delete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Xóa lớp hoc !");
        builder.setMessage("Bạn có chắc chắn muốn xóa ?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pd = new ProgressDialog(getActivity());
                pd.setMessage("Đang xóa......");
                pd.show();

                ApiLopHoc.apiService.sendDelete(ds_Lop.get(pos).getMaLop()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        pd.dismiss();
                        Toast.makeText(getActivity(), "Đã xóa!", Toast.LENGTH_SHORT).show();
                        page = 1;
                        ds_Lop.clear();
                        Get_All(page, pageSize);
                        dialog.cancel();

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });

            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void Update() {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.lop_sua);
        dialog.setCanceledOnTouchOutside(false);
        EditText edtTen;
        TextView txtMa;
        AutoCompleteTextView edt_tenGV;

        Button btnSua,btnHuy;

        txtMa = (TextView)dialog.findViewById(R.id.txt_sua_MaLop);
        edtTen = (EditText)dialog.findViewById(R.id.edt_sua_TenLop);
        edt_tenGV = (AutoCompleteTextView)dialog.findViewById(R.id.edt_sua_TenGVLop);

        btnSua =(Button)dialog.findViewById(R.id.btn_sua_Lop);
        btnHuy = (Button)dialog.findViewById(R.id.btn_HuySuaLop);

        ArrayList<GiaoVien> list_idmaGV = new ArrayList<>();
        ArrayAdapter arrayAdapter;

        ApiLopHoc.apiService.getMaTenGVLop().enqueue(new Callback<List<GiaoVien>>() {
            @Override
            public void onResponse(Call<List<GiaoVien>> call, Response<List<GiaoVien>> response) {
                ArrayList<GiaoVien> list_idmaGV1 = (ArrayList<GiaoVien>) response.body();
                if(list_idmaGV1.size()>0){
                    for(int i=0;i<list_idmaGV1.size();i++){
                        list_idmaGV.add(list_idmaGV1.get(i));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<GiaoVien>> call, Throwable t) {
                Toast.makeText(getActivity(), "Fail!", Toast.LENGTH_SHORT).show();
            }
        });
        arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,list_idmaGV);
        edt_tenGV.setAdapter(arrayAdapter);
        LopHoc lop = ds_Lop.get(pos);
        txtMa.setText(lop.getMaLop());
        edtTen.setText(lop.getTenLop());
        String ma,ten;

        ma = txtMa.getText().toString();
        ten = edtTen.getText().toString();
//        for(int i=0;i<list_idmaGV.size();i++){
//            if(lop.getMaGv().equals(list_idmaGV.get(i).getMaGv())){
//                tennGV  = list_idmaGV.get(i).getTenGv();
//            }
//        }
//        Toast.makeText(getActivity(), "Name : "+ list_idmaGV.size(), Toast.LENGTH_SHORT).show();

        ApiGiaoVien.apiService.getById(lop.getMaGv()).enqueue(new Callback<GiaoVien>() {
            @Override
            public void onResponse(Call<GiaoVien> call, Response<GiaoVien> response) {
                tennGV=response.body().getTenGv();
                maGVLop=response.body().getMaGv();
//                Toast.makeText(getActivity(), "ok!"+tennGV, Toast.LENGTH_SHORT).show();
                edt_tenGV.setText(tennGV+"-"+maGVLop);
                maGV = edt_tenGV.getText().toString();
                maGV1 = maGV.substring(maGV.indexOf("-")+1);
//                Toast.makeText(getActivity(), "maGV : "+maGV+"\n"+maGV1, Toast.LENGTH_LONG).show();

                Toast.makeText(getActivity(), "maLop : " + txtMa.getText().toString()+"\n"+ten+"\n"+maGV1, Toast.LENGTH_LONG).show();
                btnSua.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pd=new ProgressDialog(getActivity());
                        pd.setMessage("Đang cập nhật.....");
                        pd.show();
                        String Ten=edtTen.getText().toString().trim();
                        String MaGV=edt_tenGV.getText().toString().trim().substring(edt_tenGV.getText().toString().indexOf("-")+1);

                        LopHoc object = new LopHoc(Ten, MaGV);
                        ApiLopHoc.apiService.UpdateLop(txtMa.getText().toString().trim(),object).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                pd.dismiss();
                                Toast.makeText(getActivity(), "Success!", Toast.LENGTH_SHORT).show();
                                page=1;
                                ds_Lop.clear();
                                Get_All(page, pageSize);
                                dialog.cancel();
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }
                        });
                    };
                });

            }

            @Override
            public void onFailure(Call<GiaoVien> call, Throwable t) {
                Toast.makeText(getActivity(), "fail! "+t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
    }
}