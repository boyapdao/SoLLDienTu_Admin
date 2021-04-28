package com.example.solldientu_admin;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.solldientu_admin.Adapter.LopAdapter;
import com.example.solldientu_admin.Api.ApiLopHoc;
import com.example.solldientu_admin.Pagination.Pagination;
import com.example.solldientu_admin.Pagination.pLopHoc;
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
    ListView lvClass ;
    ArrayList<LopHoc> ds_Lop = new ArrayList<>();
    FloatingActionButton fabAddLop;
    LopAdapter adapter;

    ProgressDialog pd;
    int page=1, pageSize=100, totalGV=-1;
    String QueryText="";

    boolean userScrolled = false;// Check scroll
    int userScrolledCount=0;// count scroll
    private  RelativeLayout bottomLayout ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment__class, container, false);
        Init();
        Event();
        Get_All(page,pageSize);
        return view;
    }

    private void Event() {
        fabAddLop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialogAddClass = new Dialog(getContext());
                dialogAddClass.setContentView(R.layout.lop_them);
                EditText edt_tenLop;
                AutoCompleteTextView edt_maGV;
                Button btn_addLop,btn_addHuyAdd;

                edt_tenLop = (EditText)dialogAddClass.findViewById(R.id.edt_add_TenLop);
                edt_maGV=(AutoCompleteTextView)dialogAddClass.findViewById(R.id.edt_add_maGvLop);

                btn_addLop= (Button)dialogAddClass.findViewById(R.id.btn_add_Lop);
                btn_addHuyAdd = (Button)dialogAddClass.findViewById(R.id.btn_HuyAddLop);
                ArrayList<String> arrmaGV = new ArrayList<>();
                ApiLopHoc.apiService.getMaGVLop().enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                        ArrayList<String> ds_maGV = (ArrayList<String>) response.body();
                        if(ds_maGV.size()>0){
                            for (int i=0;i<ds_maGV.size();i++){
                                arrmaGV.add(ds_maGV.get(i));
                            }
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,arrmaGV);
                            edt_maGV.setAdapter(arrayAdapter);
                        }

                    }

                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {

                    }
                });



                btn_addLop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pd=new ProgressDialog(getActivity());
                        pd.setMessage("Đang thêm lớp.....");
                        pd.show();
                        String ten,maGV;
                        ten = edt_tenLop.getText().toString();
                        maGV = edt_maGV.getText().toString();
                        LopHoc lop = new LopHoc(ten,maGV);
                        ApiLopHoc.apiService.postAddLop(lop).enqueue(new Callback<LopHoc>() {
                            @Override
                            public void onResponse(Call<LopHoc> call, Response<LopHoc> response) {
                                pd.dismiss();
                                Toast.makeText(getActivity(), " Success !", Toast.LENGTH_SHORT).show();
                                ds_Lop.clear();
                                page =1;
                                Get_All(page,pageSize);
                                dialogAddClass.cancel();
                            }

                            @Override
                            public void onFailure(Call<LopHoc> call, Throwable t) {
                                Toast.makeText(getActivity(), "fail!", Toast.LENGTH_SHORT).show();

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
    }

    private void Get_All(int page, int pageSize) {
        pd= new ProgressDialog(getActivity());
        pd.setMessage("Đang tải dữ liệu....");
        pd.show();
        Pagination p = new Pagination(page,pageSize,"");
        ApiLopHoc.apiService.get_All2Lop(p.getHm()).enqueue(new Callback<pLopHoc>() {
            @Override
            public void onResponse(Call<pLopHoc> call, Response<pLopHoc> response) {

                ArrayList<LopHoc> ds_lop1 = response.body().getData();
//                Log.d("BBB", "onResponse: "+ds_lop1.size()+" ZZZZ");
                Toast.makeText(getActivity(), " helo :  "+ds_lop1.size(), Toast.LENGTH_LONG).show();
                totalGV = response.body().getTotal();
                if(ds_lop1.size()>0){
                    for (int i=0;i<ds_lop1.size();i++){
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
        adapter = new LopAdapter(getActivity(),R.layout.lop_adapter,ds_Lop);
        lvClass.setAdapter(adapter);
        bottomLayout = getActivity().findViewById(R.id.loadItemsLayout_listView);
    }
}