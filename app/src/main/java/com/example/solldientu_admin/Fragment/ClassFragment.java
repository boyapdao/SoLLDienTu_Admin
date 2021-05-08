package com.example.solldientu_admin.Fragment;

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
import com.example.solldientu_admin.Api.ApiLopHoc;
import com.example.solldientu_admin.Pagination.Pagination;
import com.example.solldientu_admin.Pagination.pLopHoc;
import com.example.solldientu_admin.R;
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

    View view;
    ListView lvClass;
    ArrayList<LopHoc> ds_Lop = new ArrayList<>();
    FloatingActionButton fabAddLop;
    LopAdapter adapter;

    int pos = -1;
    String maGV = "";
    String TenGV ;


    ProgressDialog pd;
    int page = 1, pageSize = 8, totalLop = -1;
    String QueryText = "";

    boolean userScrolled = false;// Check scroll
    int userScrolledCount = 0;// count scroll
    private RelativeLayout bottomLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment__class, container, false);
        Init();
        Events();
        Get_All(page, pageSize);
        registerForContextMenu(lvClass);
        return view;
    }

    private void Events() {
        fabAddLop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Add();
            }
        });
        lvClass.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                pos=position;
                return false;
            }
        });
        lvClass.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(userScrolledCount>0){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(scrollState==AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                                userScrolledCount=0;
                                userScrolled=true;
                            }

                        }
                    },1500);
                }else {
                    if(scrollState==AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                        userScrolledCount++;
                        userScrolled=true;
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(userScrolled&&firstVisibleItem+visibleItemCount==totalItemCount&&QueryText.equals("")){
                    if(ds_Lop.size()<totalLop){
                        userScrolled=false;
                        UpdateListView();
                    }

                }
                if (userScrolled && firstVisibleItem+visibleItemCount==totalItemCount && !QueryText.equals("")){
                    if (ds_Lop.size()<totalLop)
                    {
                        userScrolled=false;
                        UpdateListView2();
                    }
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
                Pagination p = new Pagination(page,pageSize,"");
                ApiLopHoc.apiService.searchTenLop(p.getHm()).enqueue(new Callback<pLopHoc>() {
                    @Override
                    public void onResponse(Call<pLopHoc> call, Response<pLopHoc> response) {
                        pd.dismiss();
                        ArrayList<LopHoc> arr = response.body().getData();
                        totalLop = response.body().getTotal();
                        if(arr.size()>0){
                            for (int i=0;i<arr.size();i++){
                                ds_Lop.add(arr.get(i));
                            }
                            adapter.notifyDataSetChanged();
                            bottomLayout.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<pLopHoc> call, Throwable t) {

                    }
                });

            }
        },1000);
    }

    private void Add() {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.lop_them);
        EditText edt_maLop, edt_tenLop;
        Button btn_Add, btn_Huy;

        AutoCompleteTextView edt_tenGV;
        ArrayList<GiaoVien> arr_gv = new ArrayList<>();

        edt_tenGV = (AutoCompleteTextView) dialog.findViewById(R.id.edt_add_tenGvLop);

        ApiLopHoc.apiService.getMaTenGVLop().enqueue(new Callback<List<GiaoVien>>() {
            @Override
            public void onResponse(Call<List<GiaoVien>> call, Response<List<GiaoVien>> response) {
                ArrayList<GiaoVien> arr = (ArrayList<GiaoVien>) response.body();
                if (arr.size() > 0) {
                    for (int i = 0; i < arr.size(); i++) {
                        arr_gv.add(arr.get(i));
                    }
                    ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.select_dialog_item, arr_gv);
                    edt_tenGV.setAdapter(arrayAdapter);
                }

            }
            @Override
            public void onFailure(Call<List<GiaoVien>> call, Throwable t) {

            }
        });
        edt_maLop = (EditText) dialog.findViewById(R.id.edt_add_MaLop);
        edt_tenLop = (EditText) dialog.findViewById(R.id.edt_add_TenLop);

        btn_Add = (Button) dialog.findViewById(R.id.btn_add_Lop);
        btn_Huy = (Button) dialog.findViewById(R.id.btn_HuyAddLop);
        btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TenGV = edt_tenGV.getText().toString();
                maGV = TenGV.substring(TenGV.indexOf("-")+1);
                LopHoc objectLop = new LopHoc(edt_maLop.getText().toString(), edt_tenLop.getText().toString(), maGV);
                pd = new ProgressDialog(getActivity());
                pd.setMessage("Đang thêm lớp .....");
                pd.show();
                ApiLopHoc.apiService.postAddLop(objectLop).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        pd.dismiss();
                        Toast.makeText(getActivity(), "ok!", Toast.LENGTH_SHORT).show();

                        ds_Lop.clear();
                        page = 1;
                        Get_All(page, pageSize);
                        dialog.cancel();

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getActivity(), "fail!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        btn_Huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void Init() {
        lvClass = view.findViewById(R.id.lv_class);
        adapter = new LopAdapter(getActivity(), R.layout.lop_adapter, ds_Lop);
        lvClass.setAdapter(adapter);

        fabAddLop = view.findViewById(R.id.fab_AddClass);

        bottomLayout=getActivity().findViewById(R.id.loadItemsLayout_listView);
    }

    private void Get_All(int page, int pageSize) {
        pd = new ProgressDialog(getActivity());
        pd.setMessage("Đang tải dữ liệu....");
        pd.show();
        Pagination p = new Pagination(page, pageSize, "");
        ApiLopHoc.apiService.get_All2Lop(p.getHm()).enqueue(new Callback<pLopHoc>() {
            @Override
            public void onResponse(Call<pLopHoc> call, Response<pLopHoc> response) {
                ArrayList<LopHoc> arr = response.body().getData();
                totalLop = response.body().getTotal();
                if (arr.size() > 0) {
                    for (int i = 0; i < arr.size(); i++) {
                        ds_Lop.add(arr.get(i));
                    }
                    adapter.notifyDataSetChanged();
                    pd.dismiss();
                }
            }

            @Override
            public void onFailure(Call<pLopHoc> call, Throwable t) {

            }
        });
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.menu_context,menu);
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

    private void Delete() {
        AlertDialog.Builder aBuilder = new AlertDialog.Builder(getActivity());
        aBuilder.setTitle("Xóa lớp học !");
        aBuilder.setMessage("Bạn có chắc chắn muốn xóa ?");
        aBuilder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pd=new ProgressDialog(getActivity());
                pd.setMessage("Đang xóa......");
                pd.show();
                ApiLopHoc.apiService.sendDelete(ds_Lop.get(pos).getMaLop()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        pd.dismiss();
                        Toast.makeText(getActivity(), "Đã xóa!", Toast.LENGTH_SHORT).show();
                        page=1;
                        ds_Lop.clear();
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
                ds_Lop.clear();
                Get_All(page,pageSize);
                return false;
            }
        });
    }

    private void Get_AllSearch(int page, int pageSize, String TenLop) {
        pd=new ProgressDialog(getActivity());
        pd.setMessage("Đang tìm kiếm dữ liệu....");
        pd.show();

        ds_Lop.clear();
        Pagination p=new Pagination(page, pageSize,TenLop);
        ApiLopHoc.apiService.searchTenLop(p.getHm()).enqueue(new Callback<pLopHoc>() {
            @Override
            public void onResponse(Call<pLopHoc> call, Response<pLopHoc> response) {
                ArrayList<LopHoc> arr = response.body().getData();
                totalLop = response.body().getTotal();
                Log.d("LOG", "onResponse: "+arr.size());
                if (arr.size()>0){
                    for (int i=0;i < arr.size(); i++){
                        ds_Lop.add(arr.get(i));
                    }
                    adapter.notifyDataSetChanged();
                }
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<pLopHoc> call, Throwable t) {

            }
        });
    }

    private void Update() {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.lop_sua);
        EditText edt_tenLop;
        TextView txt_maLop;
        Button btn_Sua, btn_Huy;
        final String[] tenGv = {""};

        txt_maLop = (TextView) dialog.findViewById(R.id.txt_sua_MaLop);
        edt_tenLop = (EditText) dialog.findViewById(R.id.edt_sua_TenLop);

        btn_Sua = (Button) dialog.findViewById(R.id.btn_sua_Lop);
        btn_Huy = (Button) dialog.findViewById(R.id.btn_HuySuaLop);


        LopHoc lop = ds_Lop.get(pos);

        txt_maLop.setText(lop.getMaLop());
        edt_tenLop.setText(lop.getTenLop());
        AutoCompleteTextView edt_tenGV;
        ArrayList<GiaoVien> arr_gv = new ArrayList<>();

        edt_tenGV = (AutoCompleteTextView) dialog.findViewById(R.id.edt_sua_TenGVLop);

        ApiLopHoc.apiService.getMaTenGVLop().enqueue(new Callback<List<GiaoVien>>() {
            @Override
            public void onResponse(Call<List<GiaoVien>> call, Response<List<GiaoVien>> response) {
                ArrayList<GiaoVien> arr = (ArrayList<GiaoVien>) response.body();
                if (arr.size() > 0) {
                    for (int i = 0; i < arr.size(); i++) {
                        arr_gv.add(arr.get(i));
                    }
                    ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.select_dialog_item, arr_gv);
                    edt_tenGV.setAdapter(arrayAdapter);
                    for (int i=0;i<arr_gv.size();i++){
                        if(ds_Lop.get(pos).getMaGv().equals(arr_gv.get(i).getMaGv())){
                            tenGv[0] = arr_gv.get(i).getTenGv()+"-"+ arr_gv.get(i).getMaGv();
                        }
                        Log.d("LOG", "Update: "+ds_Lop.get(pos).getMaGv()+"\t ehihi : "+arr_gv.get(i).getMaGv());

                    }

                    edt_tenGV.setText(tenGv[0]);

                }

            }
            @Override
            public void onFailure(Call<List<GiaoVien>> call, Throwable t) {

            }
        });


        btn_Sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TenGV = edt_tenGV.getText().toString();
                maGV = TenGV.substring(TenGV.indexOf("-")+1);
                LopHoc objectLop = new LopHoc(txt_maLop.getText().toString(), edt_tenLop.getText().toString(), maGV);
                pd = new ProgressDialog(getActivity());
                pd.setMessage("Đang sửa lớp .....");
                pd.show();
                ApiLopHoc.apiService.UpdateLop(ds_Lop.get(pos).getMaLop(),objectLop).enqueue(new Callback<Void>() {
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
            }
        });
        btn_Huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();





    }
}