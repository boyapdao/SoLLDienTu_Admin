package com.example.solldientu_admin.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.solldientu_admin.Adapter.SinhVienAdapter;
import com.example.solldientu_admin.Api.ApiGiaoVien;
import com.example.solldientu_admin.Api.ApiKetQua;
import com.example.solldientu_admin.Api.ApiSinhVien;
import com.example.solldientu_admin.Pagination.Pagination;
import com.example.solldientu_admin.Pagination.pGiaoVien;
import com.example.solldientu_admin.Pagination.pSinhVien;
import com.example.solldientu_admin.R;
import com.example.solldientu_admin.ResultActivity;
import com.example.solldientu_admin.object.GiaoVien;
import com.example.solldientu_admin.object.LopHoc;
import com.example.solldientu_admin.object.SinhVien;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ResultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Result.
     */
    // TODO: Rename and change types and number of parameters
    public static ResultFragment newInstance(String param1, String param2) {
        ResultFragment fragment = new ResultFragment();
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
    ListView lv;
    ArrayList<SinhVien> ds_SV=new ArrayList<>();
    SinhVienAdapter adapter;

    ProgressDialog pd;

    ArrayAdapter adapter_sp;
    Spinner sp_Lop;
    ArrayList<LopHoc> ds_Lop=new ArrayList<>();

    String MaLop="";

    Button btn_Ok;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment__result, container, false);
        Init();
        GetAllLop();
        Events();
        return view;
    }

    private void GetAllLop() {
        pd=new ProgressDialog(getActivity());
        pd.setMessage("Đang tải dữ liệu...");
        pd.show();

        ApiKetQua.apiService.getAllLop().enqueue(new Callback<List<LopHoc>>() {
            @Override
            public void onResponse(Call<List<LopHoc>> call, Response<List<LopHoc>> response) {
                ds_Lop= (ArrayList<LopHoc>) response.body();

                adapter_sp=new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, ds_Lop);
                adapter_sp.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                sp_Lop.setAdapter(adapter_sp);

                MaLop=ds_Lop.get(0).getMaLop();

                pd.dismiss();
            }

            @Override
            public void onFailure(Call<List<LopHoc>> call, Throwable t) {

            }
        });
    }

    private void GetSvByMaLop(String maLop) {
        pd = new ProgressDialog(getActivity());
        pd.setMessage("Đang tải dữ liệu....");
        pd.show();
        ApiKetQua.apiService.getSVByMaLop(maLop).enqueue(new Callback<List<SinhVien>>() {
            @Override
            public void onResponse(Call<List<SinhVien>> call, Response<List<SinhVien>> response) {

                ds_SV= (ArrayList<SinhVien>) response.body();
                adapter=new SinhVienAdapter(getActivity(), R.layout.sinhvien_adapter, ds_SV);
                lv.setAdapter(adapter);

                pd.dismiss();
            }

            @Override
            public void onFailure(Call<List<SinhVien>> call, Throwable t) {

            }
        });
    }

    private void Events() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent it=new Intent(getActivity(), ResultActivity.class);
                it.putExtra("maSv", ds_SV.get(i).getMaSv());
                startActivity(it);
            }
        });
        sp_Lop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MaLop=ds_Lop.get(i).getMaLop();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btn_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetSvByMaLop(MaLop);
            }
        });
    }

    private void Init() {
        lv=view.findViewById(R.id.lv_resultSV);
        btn_Ok=view.findViewById(R.id.btn_Ok);
        sp_Lop=view.findViewById(R.id.sp_Lop);
    }
}