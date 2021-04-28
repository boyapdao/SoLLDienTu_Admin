package com.example.solldientu_admin;

import android.app.Activity;
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
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.solldientu_admin.Adapter.MonHocAdapter;
import com.example.solldientu_admin.Api.ApiMonHoc;
import com.example.solldientu_admin.Pagination.Pagination;
import com.example.solldientu_admin.Pagination.pMonHoc;
import com.example.solldientu_admin.object.GiaoVien;
import com.example.solldientu_admin.object.MonHoc;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubjectsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubjectsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SubjectsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Subjects.
     */
    // TODO: Rename and change types and number of parameters
    public static SubjectsFragment newInstance(String param1, String param2) {
        SubjectsFragment fragment = new SubjectsFragment();
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
    ListView lv_sb;
    FloatingActionButton fab_add;
    ArrayList<MonHoc> ds_mh=new ArrayList<>();
    MonHocAdapter adapter;

    int page=1, pageSize=8, totalMH;
    String ten="";
    ProgressDialog pd;
    int pos_sp=-1, pos=-1;

    int userScrolledCount=0;
    boolean userScrolled=false;
    String QueryText="";
    private static RelativeLayout bottomLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment__subjects, container, false);
        Init();
        Events();
        getAll(page, pageSize,"");
        registerForContextMenu(lv_sb);
        return view;
    }

    private void getAll(int page, int pageSize, String ten) {
        ds_mh.clear();
        pd=new ProgressDialog(getActivity());
        pd.setMessage("Đang tải dữ liệu....");
        pd.show();

        Pagination p=new Pagination(page, pageSize, ten);
        ApiMonHoc.apiService.get_All2(p.getHm()).enqueue(new Callback<pMonHoc>() {
            @Override
            public void onResponse(Call<pMonHoc> call, Response<pMonHoc> response) {
                pd.dismiss();
                ArrayList<MonHoc> ds2=response.body().getData();
                totalMH=response.body().getTotal();

                if (ds2.size()>0){
                    for (int i=0;i < ds2.size(); i++)
                        ds_mh.add(ds2.get(i));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<pMonHoc> call, Throwable t) {

            }
        });
    }

    private void Events() {
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddMH();
            }
        });
        lv_sb.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                pos=i;
                return false;
            }
        });
        lv_sb.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                if (userScrolled && i+i1 == i2 && QueryText.equals("")){
                    if (ds_mh.size()<totalMH)
                    {
                        userScrolled=false;
                        UpdateListView();
                    }
                }
                if (userScrolled && i+i1 == i2 && !QueryText.equals(""))
                    if (ds_mh.size()<totalMH)
                    {
                        userScrolled=false;
//                        UpdateListView2();
                    }
            }
        });
    }

    private void UpdateListView() {
        bottomLayout.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                page++;
                Pagination p=new Pagination(page, pageSize, "");
                ApiMonHoc.apiService.get_All2(p.getHm()).enqueue(new Callback<pMonHoc>() {
                    @Override
                    public void onResponse(Call<pMonHoc> call, Response<pMonHoc> response) {
                        pd.dismiss();

                        ArrayList<MonHoc> ds_mh1=response.body().getData();
                        totalMH=response.body().getTotal();

                        if (ds_mh1.size()>0){
                            for (int i=0;i < ds_mh1.size(); i++)
                            {
                                ds_mh.add(ds_mh1.get(i));
                            }
                            adapter.notifyDataSetChanged();
                            bottomLayout.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<pMonHoc> call, Throwable t) {

                    }
                });
            }
        },1500);
    }

    private void Init() {
        lv_sb=view.findViewById(R.id.lv_subject);
        fab_add=view.findViewById(R.id.fab_AddSubject);
        bottomLayout=getActivity().findViewById(R.id.loadItemsLayout_listView);

        adapter=new MonHocAdapter(getContext(), R.layout.dong_monhoc, ds_mh);
        lv_sb.setAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.menu_context, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        switch (id){
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
                getDataSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void getDataSearch(String query) {
        pd=new ProgressDialog(getActivity());
        pd.setMessage("Đang tìm kiếm môn học...");
        pd.show();

        ds_mh.clear();
        String TenMH=query;
        Pagination p=new Pagination(page, pageSize, TenMH);
        ApiMonHoc.apiService.search_MH(p.getHm()).enqueue(new Callback<pMonHoc>() {
            @Override
            public void onResponse(Call<pMonHoc> call, Response<pMonHoc> response) {
                ArrayList<MonHoc> ds_2=response.body().getData();
                if (ds_2.size()>0){
                    for (int i=0;i<ds_2.size();i++)
                        ds_mh.add(ds_2.get(i));
                    adapter.notifyDataSetChanged();
                    pd.dismiss();
                }

            }

            @Override
            public void onFailure(Call<pMonHoc> call, Throwable t) {

            }
        });
    }

    private void Delete() {
        AlertDialog.Builder alert=new AlertDialog.Builder(getActivity());
        alert.setTitle("Xóa");
        alert.setMessage("Bạn có chắc chắn muốn xóa môn học này?");
        alert.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alert.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                pd=new ProgressDialog(getActivity());
                pd.setMessage("Đang xóa...");
                pd.show();
                MonHoc mh=ds_mh.get(pos);
                String id=mh.getMaMh();
                ApiMonHoc.apiService.delete_MH(id).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        pd.dismiss();
                        Toast.makeText(getActivity(), "Đã xóa", Toast.LENGTH_SHORT).show();
                        page=1;
                        getAll(page, pageSize,"");
                        dialogInterface.cancel();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
            }
        });
        alert.show();
    }

    private void Update() {
        Dialog dialog=new Dialog(getActivity());
        dialog.setContentView(R.layout.monhoc_sua);
        EditText edt_TenMH, edt_SoTC;
        Spinner sp_Ky;
        Button btn_Sua, btn_Huy;
        String tenMH;
        int SoTC, Ky;
        ArrayList<Integer> KyHoc=new ArrayList<>();
        KyHoc.add(1);KyHoc.add(2);KyHoc.add(3);KyHoc.add(4);
        KyHoc.add(5);KyHoc.add(6);KyHoc.add(7);KyHoc.add(8);

        edt_TenMH=dialog.findViewById(R.id.edt_sua_TenMh);
        edt_SoTC=dialog.findViewById(R.id.edt_sua_STC);
        sp_Ky=dialog.findViewById(R.id.sp_Ky);
        btn_Sua=dialog.findViewById(R.id.btn_sua_MH);
        btn_Huy=dialog.findViewById(R.id.btn_Huy);

        ArrayAdapter adapter=new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, KyHoc);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        sp_Ky.setAdapter(adapter);

        MonHoc mh=ds_mh.get(pos);
        edt_TenMH.setText(mh.getTenMh());
        edt_SoTC.setText(mh.getSoTc()+"");
        for(int i=0;i<KyHoc.size();i++){
            if (mh.getKyhoc()==KyHoc.get(i))
            {
                sp_Ky.setSelection(i);
                break;
            }
        }
        btn_Huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        sp_Ky.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pos_sp=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btn_Sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Ma, Ten;
                int SoTC, Ky;
                Ma=mh.getMaMh();
                Ten=edt_TenMH.getText().toString();
                SoTC=Integer.parseInt(edt_SoTC.getText().toString());
                Ky=KyHoc.get(pos_sp);

                MonHoc mh=new MonHoc(Ma,Ten, SoTC, Ky);

                pd=new ProgressDialog(getActivity());
                pd.setMessage("Đang sửa dữ liệu...");
                pd.show();
                ApiMonHoc.apiService.update_MH(mh).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(getActivity(), "Success!", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                        dialog.cancel();
                        page=1;
                        ds_mh.clear();
                        getAll(page, pageSize,"");
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });

            }
        });
        dialog.show();
    }

    private void AddMH() {
        Dialog dialog=new Dialog(getActivity());
        dialog.setContentView(R.layout.monhoc_them);
        EditText edt_Ten, edt_SoTC;
        Spinner sp_KyHoc;
        Button btn_Add, btn_Huy;
        edt_Ten=dialog.findViewById(R.id.edt_add_TenMh);
        edt_SoTC=dialog.findViewById(R.id.edt_add_STC);
        sp_KyHoc=dialog.findViewById(R.id.sp_Ky);
        btn_Add=dialog.findViewById(R.id.btn_add_MH);
        btn_Huy=dialog.findViewById(R.id.btn_Huy);

        ArrayList<Integer> KyHoc=new ArrayList<>();
        KyHoc.add(1);KyHoc.add(2);KyHoc.add(3);KyHoc.add(4);
        KyHoc.add(5);KyHoc.add(6);KyHoc.add(7);KyHoc.add(8);

        ArrayAdapter adapter=new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, KyHoc);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        sp_KyHoc.setAdapter(adapter);
        sp_KyHoc.setSelection(0);
        sp_KyHoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pos_sp=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btn_Huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd=new ProgressDialog(getActivity());
                pd.setMessage("Đang thêm...");
                pd.show();
                String Ten;
                int SoTC, Kh;
                Ten=edt_Ten.getText().toString();
                SoTC=Integer.parseInt(edt_SoTC.getText().toString());
                Kh=KyHoc.get(pos_sp);
                MonHoc mh=new MonHoc(Ten, SoTC, Kh);
                ApiMonHoc.apiService.create_MH(mh).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(getActivity(), "Success!", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                        dialog.cancel();

                        page=1;
                        getAll(page, pageSize,"");
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
            }
        });
        dialog.show();
    }
}