package com.example.solldientu_admin.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.solldientu_admin.Adapter.GVMH_AddMonAdapter;
import com.example.solldientu_admin.Adapter.GVMH_Apdapter;
import com.example.solldientu_admin.Adapter.GVMH_MonAdapter;
import com.example.solldientu_admin.Api.ApiGVMH;
import com.example.solldientu_admin.Api.ApiGiaoVien;
import com.example.solldientu_admin.Pagination.Pagination;
import com.example.solldientu_admin.Pagination.pGiaoVien;
import com.example.solldientu_admin.R;
import com.example.solldientu_admin.object.GVMH;
import com.example.solldientu_admin.object.GiaoVien;
import com.example.solldientu_admin.object.MonHoc;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AssignmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssignmentFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AssignmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Assignment.
     */
    // TODO: Rename and change types and number of parameters
    public static AssignmentFragment newInstance(String param1, String param2) {
        AssignmentFragment fragment = new AssignmentFragment();
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
    ListView lv_ass;
    ArrayList<GiaoVien> ds_GV=new ArrayList<>();
    GVMH_Apdapter apdapter;

    ProgressDialog pd;

    int page=1, pageSize=8;
    int pos, pos_DangKyMon;
    int totalGV;
    String QueryText="";

    ArrayList<MonHoc> arrMonHoc=new ArrayList<>();// List môn học đã đăng ký
    ArrayList<MonHoc> arrMonHoc2=new ArrayList<>();//List môn học chưa đăng ký

    GVMH_MonAdapter ar_adapter;// Adapter môn đã đăng ký
    GVMH_AddMonAdapter ar_add_adapter;//Adapter môn chưa đăng ký

    boolean userScrolled = false;// Check scroll
    int userScrolledCount=0;// count scroll
    private static RelativeLayout bottomLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment__assignment, container, false);
        Init();
        GetAll(page, pageSize);
        Events();
        return view;
    }

    private void Events() {
        lv_ass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                pos=i;
                ChiTiet();
            }
        });
        lv_ass.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                            apdapter.notifyDataSetChanged();
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

    private void ChiTiet() {
        Dialog dialog1=new Dialog(getActivity());
        dialog1.setContentView(R.layout.gvmh_chitiet);
        ImageView img, img_more;
        TextView tvTen, tvNS, tvGT, tvMonDay;
        Button btnOk, btnCancel;

        img=dialog1.findViewById(R.id.img_gvmhCT);
        img_more=dialog1.findViewById(R.id.img_more);
        tvTen=dialog1.findViewById(R.id.tv_ten);
        tvNS=dialog1.findViewById(R.id.tv_NS);
        tvGT=dialog1.findViewById(R.id.tv_GT);
        tvMonDay=dialog1.findViewById(R.id.tv_MonDay);
//        btnOk=dialog1.findViewById(R.id.btn_ok);
        btnCancel=dialog1.findViewById(R.id.btn_cancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.cancel();
            }
        });
        GiaoVien gv=ds_GV.get(pos);

        tvTen.setText(gv.getTenGv());
        tvNS.setText(gv.getNgaySinh());
        if (gv.getGioiTinh()==0)
            tvGT.setText("Nữ");
        else tvGT.setText("Nam");
        ApiGVMH.apiService.getCount(gv.getMaGv()).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                tvMonDay.setText(response.body()+"");
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
        if (!gv.getAnh().equals("")){
            String[] tenfile=gv.getAnh().split("\\.");
//            Glide.with(context).load(ApiGiaoVien.url+"GetImage/"+tenfile[0]).into(holder.imgGv);
            Picasso.get().load(ApiGiaoVien.url+"GetImage/"+tenfile[0]).into(img);
        }else Picasso.get().load(R.drawable.add_image).into(img);

        img_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadMon(gv.getMaGv(), dialog1);
            }
        });
        dialog1.show();
    }

    private void LoadMon(String maGv, Dialog dialog1) {
        pd=new ProgressDialog(getActivity());
        pd.setMessage("Đang tải môn học...");
        pd.show();
        ApiGVMH.apiService.getMhById(maGv).enqueue(new Callback<List<MonHoc>>() {
            @Override
            public void onResponse(Call<List<MonHoc>> call, Response<List<MonHoc>> response) {
                pd.dismiss();
                arrMonHoc= (ArrayList<MonHoc>) response.body();
//                if (arrMonHoc.size()>0) {
                    Dialog dialog2 = new Dialog(getActivity());
                    dialog2.setContentView(R.layout.gvmh_chitiet_mon);
                    ListView lv;
                    ImageButton imgb_add;
                    lv = dialog2.findViewById(R.id.lv_Mon);
                    imgb_add = dialog2.findViewById(R.id.imgb_add);

                    AssignmentFragment fragment = new AssignmentFragment();
                    FragmentTransaction ft5 = getActivity().getSupportFragmentManager().beginTransaction();
                    ft5.replace(R.id.content, fragment, "");

                    ar_adapter = new GVMH_MonAdapter(R.layout.dong_phancong_monhoc, arrMonHoc, maGv, getContext(), dialog1, dialog2, pd, ft5);
                    lv.setAdapter(ar_adapter);
                    imgb_add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AddMon(dialog1, dialog2);
                        }
                    });
                    dialog2.show();
//                }
//                }else Toast.makeText(getActivity(), "Giáo viên không giảng dạy môn nào!", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<List<MonHoc>> call, Throwable t) {

            }
        });
    }

    private void AddMon(Dialog dialog1, Dialog dialog2) {
        Dialog dialog3=new Dialog(getActivity());
        dialog3.setContentView(R.layout.dialog_list_monhoc);
        ListView lv_Mon;
        lv_Mon=dialog3.findViewById(R.id.lv_MH);
        ApiGVMH.apiService.getAll_MonHoc().enqueue(new Callback<List<MonHoc>>() {
            @Override
            public void onResponse(Call<List<MonHoc>> call, Response<List<MonHoc>> response) {
                ArrayList<MonHoc> ds_Test= (ArrayList<MonHoc>) response.body();
                arrMonHoc2.clear();
                //Check môn đã được đăng ký
                int is=-1;
                for (int i=0;i<ds_Test.size();i++){
                    for (int j=0;j<arrMonHoc.size();j++){
                        is=j;
                        if (ds_Test.get(i).getMaMh().equals(arrMonHoc.get(j).getMaMh()))
                        {
                            is=-1;
                            break;
                        }
                    }
                    if ((is+1)==arrMonHoc.size()){
                        is=-1;
                        arrMonHoc2.add(ds_Test.get(i));
                    }
                }
                ar_add_adapter=new GVMH_AddMonAdapter(getContext(), R.layout.dong_phancong_monhoc, arrMonHoc2);
                lv_Mon.setAdapter(ar_add_adapter);
            }

            @Override
            public void onFailure(Call<List<MonHoc>> call, Throwable t) {

            }
        });
        lv_Mon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int pos_DK=i;
                AlertDialog.Builder alert=new AlertDialog.Builder(getActivity());
                alert.setMessage("Đăng ký môn học này?");
                alert.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pd=new ProgressDialog(getActivity());
                        pd.setMessage("Đang đăng ký...");
                        pd.show();

//                        arrMonHoc.add(arrMonHoc2.get(pos_DK));
                        String Magv=ds_GV.get(pos).getMaGv();
                        String MaMH=arrMonHoc2.get(pos_DK).getMaMh();

                        GVMH gvmh=new GVMH(Magv, MaMH);
                        ApiGVMH.apiService.createGVMH(gvmh).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Toast.makeText(getActivity(), "Success!", Toast.LENGTH_SHORT).show();
                                dialog1.cancel();
                                dialog2.cancel();
                                dialog3.cancel();
                                pd.dismiss();
                                page=1;
                                GetAll(page, pageSize);
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }
                        });
                    }
                });
                alert.show();
            }
        });

        dialog3.show();
    }

    private void GetAll(int page, int pageSize) {
        pd=new ProgressDialog(getActivity());
        pd.setMessage("Đang tải dữ liệu...");
        pd.show();
        Pagination p=new Pagination(page, pageSize,"");
        ApiGiaoVien.apiService.get_All2(p.getHm()).enqueue(new Callback<pGiaoVien>() {
            @Override
            public void onResponse(Call<pGiaoVien> call, Response<pGiaoVien> response) {
                totalGV=response.body().getTotal();
                ds_GV=response.body().getData();
                apdapter=new GVMH_Apdapter(R.layout.dong_phancong, getContext() , ds_GV);
                lv_ass.setAdapter(apdapter);
//                apdapter.notifyDataSetChanged();
                Toast.makeText(getActivity(), "Thành công! "+ds_GV.size(), Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<pGiaoVien> call, Throwable t) {
                Toast.makeText(getActivity(), "Thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Init() {
        lv_ass=view.findViewById(R.id.lv_assignment);
        bottomLayout=getActivity().findViewById(R.id.loadItemsLayout_listView);
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
    }

    private void Get_AllSearch(int page, int pageSize, String query) {
        pd=new ProgressDialog(getActivity());
        pd.setMessage("Đang tìm kiếm dữ liệu....");
        pd.show();

        ds_GV.clear();
        Pagination p=new Pagination(page, pageSize,query);
        ApiGiaoVien.apiService.search(p.getHm()).enqueue(new Callback<pGiaoVien>() {
            @Override
            public void onResponse(Call<pGiaoVien> call, Response<pGiaoVien> response) {
                pd.dismiss();

                ArrayList<GiaoVien> ds_gv1=response.body().getData();
                totalGV=response.body() .getTotal();

                if (ds_gv1.size()>0){
                    for (int i=0;i < ds_gv1.size(); i++)
                        ds_GV.add(ds_gv1.get(i));
                    apdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<pGiaoVien> call, Throwable t) {

            }
        });
    }
}