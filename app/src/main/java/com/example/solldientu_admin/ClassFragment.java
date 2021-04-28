package com.example.solldientu_admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
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

    ProgressDialog pd;
    int page = 1, pageSize = 88, totalLop = -1;
    String QueryText = "";
    int pos = -1;

    String MaGV = "";
    ArrayList<GiaoVien> arr_Gv;

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
//                arr_Gv.clear();

                Dialog dialogAddClass = new Dialog(getContext());
                dialogAddClass.setContentView(R.layout.lop_them);
                dialogAddClass.setCanceledOnTouchOutside(false);
                EditText edt_tenLop, edt_maLop;
                AutoCompleteTextView edt_tenGV;
                Button btn_addLop, btn_addHuyAdd;

                edt_tenLop = (EditText) dialogAddClass.findViewById(R.id.edt_add_TenLop);
                edt_tenGV = (AutoCompleteTextView) dialogAddClass.findViewById(R.id.edt_add_tenGvLop);
                edt_maLop = dialogAddClass.findViewById(R.id.edt_add_MaLop);

                btn_addLop = (Button) dialogAddClass.findViewById(R.id.btn_add_Lop);
                btn_addHuyAdd = (Button) dialogAddClass.findViewById(R.id.btn_HuyAddLop);
                ArrayList<GiaoVien> arrnameidGV = new ArrayList<>();
                ApiLopHoc.apiService.getMaTenGVLop().enqueue(new Callback<List<GiaoVien>>() {
                    @Override
                    public void onResponse(Call<List<GiaoVien>> call, Response<List<GiaoVien>> response) {
                        arr_Gv = (ArrayList<GiaoVien>) response.body();
                        Log.d("MangGV", "onClick: " + arr_Gv.size());

                        if (arr_Gv.size() > 0) {
                            for (int i = 0; i < arr_Gv.size(); i++) {
                                arrnameidGV.add(arr_Gv.get(i));
                            }

                            Log.d("MANG", "onClick: " + arrnameidGV.size());


                        }
                    }

                    @Override
                    public void onFailure(Call<List<GiaoVien>> call, Throwable t) {

                    }
                });
                ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.select_dialog_item, arrnameidGV);
                edt_tenGV.setAdapter(arrayAdapter);


                btn_addLop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String maLop, ten, maGV = "";
                        pd = new ProgressDialog(getActivity());
                        pd.setMessage("Đang thêm lớp.....");
                        pd.show();
                        for (int i = 0; i < arrnameidGV.size(); i++) {
                            if (arrnameidGV.get(i).getTenGv().equals(edt_tenGV.getText().toString())) {
                                maGV = arrnameidGV.get(i).getMaGv();
                            }
                        }
                        Log.d("NameGV", "onClick: " + maGV);

                        ten = edt_tenLop.getText().toString();
                        maLop = edt_maLop.getText().toString().trim();

                        LopHoc lop = new LopHoc(maLop, ten, maGV);
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
                if (userScrolledCount > 0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                                userScrolled = true;
                                userScrolledCount = 0;
                            }
                        }
                    }, 1500);
                } else {
                    if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                        userScrolled = true;
                        userScrolledCount++;
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (userScrolled && firstVisibleItem + firstVisibleItem == totalItemCount && QueryText.equals(""))
                    if (ds_Lop.size() < totalLop) {
                        userScrolled = false;
                        UpdateListView();
                    }
                if (userScrolled && firstVisibleItem + firstVisibleItem == totalItemCount && !QueryText.equals(""))
                    if (ds_Lop.size() < totalLop) {
                        userScrolled = false;
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
                Get_All(page, pageSize);
                Pagination p = new Pagination(page, pageSize, "");
                ApiLopHoc.apiService.get_All2Lop(p.getHm()).enqueue(new Callback<pLopHoc>() {
                    @Override
                    public void onResponse(Call<pLopHoc> call, Response<pLopHoc> response) {
                        ArrayList<LopHoc> ds_lop1 = response.body().getData();
                        totalLop = response.body().getTotalLop();
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
        }, 1000);

    }

    private void Get_All(int page, int pageSize) {
        pd = new ProgressDialog(getActivity());
        pd.setMessage("Đang tải dữ liệu....");
        pd.show();
        Pagination p = new Pagination(page, pageSize, "");
        ApiLopHoc.apiService.get_All2Lop(p.getHm()).enqueue(new Callback<pLopHoc>() {
            @Override
            public void onResponse(Call<pLopHoc> call, Response<pLopHoc> response) {

                ArrayList<LopHoc> ds_lop1 = response.body().getData();
//                Log.d("BBB", "onResponse: "+ds_lop1.size()+" ZZZZ");
                Toast.makeText(getActivity(), " helo :  " + ds_lop1.size(), Toast.LENGTH_LONG).show();
                totalLop = response.body().getTotalLop();
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
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.menu_contextlop, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.mn_SuaLop: {
                Update();
                break;
            }
            case R.id.mn_XoaLop: {
                Delete();
                break;
            }


        }
        return super.onContextItemSelected(item);
    }

    private void Delete() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Xóa lớp học !");
        alertDialog.setMessage("Bạn có chắc muốn xóa ?");
        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pd = new ProgressDialog(getActivity());
                pd.setMessage("Đang xóa......");
                pd.show();
                if (!ds_Lop.get(pos).equals("")) {

                    ApiLopHoc.apiService.sendDelete(ds_Lop.get(pos).getMaLop()).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            pd.dismiss();
                            Toast.makeText(getActivity(), "Đã xóa !", Toast.LENGTH_SHORT).show();
                            page = 1;
                            ds_Lop.clear();
                            Get_All(page, pageSize);
                            dialog.cancel();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private void loadIDNameGV() {

    }

    private void Update() {
        pd = new ProgressDialog(getActivity());
        pd.setMessage("Đang cập nhật.....");
        pd.show();
        Dialog dialogSuaLop = new Dialog(getActivity());
        dialogSuaLop.setContentView(R.layout.lop_sua);
        EditText edt_SuaTen;
        TextView txt_maLop;
        AutoCompleteTextView edt_suaTenGvLop;
        Button btn_Sua, btn_Huy;

        btn_Sua = (Button) dialogSuaLop.findViewById(R.id.btn_sua_Lop);
        btn_Huy = (Button) dialogSuaLop.findViewById(R.id.btn_HuySuaLop);

        txt_maLop = (TextView) dialogSuaLop.findViewById(R.id.txt_sua_MaLop);
        edt_SuaTen = (EditText) dialogSuaLop.findViewById(R.id.edt_sua_TenLop);
        edt_suaTenGvLop = (AutoCompleteTextView) dialogSuaLop.findViewById(R.id.edt_sua_TenGVLop);
        ArrayList<GiaoVien> arrtenidGV = new ArrayList<>();
        ApiLopHoc.apiService.getMaTenGVLop().enqueue(new Callback<List<GiaoVien>>() {
            @Override
            public void onResponse(Call<List<GiaoVien>> call, Response<List<GiaoVien>> response) {
                arr_Gv = (ArrayList<GiaoVien>) response.body();
                Log.d("MangGVud", "onClick: " + arr_Gv.size());

                if (arr_Gv.size() > 0) {
                    for (int i = 0; i < arr_Gv.size(); i++) {
                        arrtenidGV.add(arr_Gv.get(i));
                    }

                    Log.d("MANGud", "onClick: " + arrtenidGV.size());


                }
            }

            @Override
            public void onFailure(Call<List<GiaoVien>> call, Throwable t) {

            }
        });
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.select_dialog_item, arrtenidGV);
        edt_suaTenGvLop.setAdapter(arrayAdapter);

//        String suaTen = edt_SuaTen.getText().toString();
        LopHoc lop = ds_Lop.get(pos);
        String ten = "";
        edt_SuaTen.setText(lop.getTenLop());
        txt_maLop.setText(lop.getMaLop());
        for (int i = 0; i < arrtenidGV.size(); i++) {
            if (ds_Lop.get(pos).getMaGv().equals(arrtenidGV.get(i).getMaGv())) {
                ten = arrtenidGV.get(i).getTenGv();
            }
        }
        Toast.makeText(getActivity(), " null : " + ten, Toast.LENGTH_SHORT).show();
        edt_suaTenGvLop.setText(ten);
        btn_Huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSuaLop.cancel();
                pd.dismiss();
            }
        });
        btn_Sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten, ma = "";
                ten = edt_SuaTen.getText().toString();
                for (int i = 0; i < arrtenidGV.size(); i++) {
                    if (arrtenidGV.get(i).getTenGv().equals(edt_suaTenGvLop.getText().toString())) {
                        ma = arrtenidGV.get(i).getMaGv();
                    }
                }
                LopHoc object = new LopHoc(ten, ma);
                ApiLopHoc.apiService.UpdateLop(ds_Lop.get(pos).getMaLop(), object).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        pd.dismiss();
                        Toast.makeText(getActivity(), "Success!", Toast.LENGTH_SHORT).show();
                        page = 1;
                        ds_Lop.clear();
                        Get_All(page, pageSize);
                        dialogSuaLop.cancel();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        dialogSuaLop.show();


    }
}