package com.example.solldientu_admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.solldientu_admin.Fragment.AssignmentFragment;
import com.example.solldientu_admin.Fragment.ClassFragment;
import com.example.solldientu_admin.Fragment.HomeFragment;
import com.example.solldientu_admin.Fragment.ResultFragment;
import com.example.solldientu_admin.Fragment.StudentFragment;
import com.example.solldientu_admin.Fragment.SubjectsFragment;
import com.example.solldientu_admin.Fragment.TeacherFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ActionBar actionBar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init();
        Events();
        setFirstItemNavigationView();
        verifyStoragePermissions(MainActivity.this);
    }

    private void Events() {
        //Click open navigation
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int i=item.getItemId();
                switch (i){
                    case R.id.mn_Home:
                        actionBar.setTitle("Contact book FIT UTEHY");
                        navigationView.setCheckedItem(i);//T?? s??ng m???c ch???n
                        drawerLayout.closeDrawer(GravityCompat.START);//????ng navigation sau khi click
                        HomeFragment fragment=new HomeFragment();
                        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content, fragment, "");
                        ft.commit();
                        break;
                    case R.id.mn_qlGiaoVien:
                        actionBar.setTitle("Qu???n l?? Gi???ng Vi??n");
                        navigationView.setCheckedItem(i);//T?? s??ng m???c ch???n
                        drawerLayout.closeDrawer(GravityCompat.START);//????ng navigation sau khi click
                        TeacherFragment fragment1=new TeacherFragment();
                        FragmentTransaction ft1=getSupportFragmentManager().beginTransaction();
                        ft1.replace(R.id.content, fragment1, "");
                        ft1.commit();
                        break;
                    case R.id.mn_qlMonHoc:
                        actionBar.setTitle("Qu???n l?? M??n H???c");
                        navigationView.setCheckedItem(i);//T?? s??ng m???c ch???n
                        drawerLayout.closeDrawer(GravityCompat.START);//????ng navigation sau khi click
                        SubjectsFragment fragment2=new SubjectsFragment();
                        FragmentTransaction ft2=getSupportFragmentManager().beginTransaction();
                        ft2.replace(R.id.content, fragment2, "");
                        ft2.commit();
                        break;
                    case R.id.mn_QLSinhVien:
                        actionBar.setTitle("Qu???n l?? Sinh Vi??n");
                        navigationView.setCheckedItem(i);//T?? s??ng m???c ch???n
                        drawerLayout.closeDrawer(GravityCompat.START);//????ng navigation sau khi click
                        StudentFragment fragment3=new StudentFragment();
                        FragmentTransaction ft3=getSupportFragmentManager().beginTransaction();
                        ft3.replace(R.id.content, fragment3, "");
                        ft3.commit();
                        break;
                    case R.id.mn_QLLop:
                        actionBar.setTitle("Qu???n l?? L???p");
                        navigationView.setCheckedItem(i);//T?? s??ng m???c ch???n
                        drawerLayout.closeDrawer(GravityCompat.START);//????ng navigation sau khi click
                        ClassFragment fragment4=new ClassFragment();
                        FragmentTransaction ft4=getSupportFragmentManager().beginTransaction();
                        ft4.replace(R.id.content, fragment4, "");
                        ft4.commit();
                        break;
                    case R.id.mn_PhanCong:
                        actionBar.setTitle("Ph??n c??ng gi???ng d???y");
                        navigationView.setCheckedItem(i);//T?? s??ng m???c ch???n
                        drawerLayout.closeDrawer(GravityCompat.START);//????ng navigation sau khi click
                        AssignmentFragment fragment5=new AssignmentFragment();
                        FragmentTransaction ft5=getSupportFragmentManager().beginTransaction();
                        ft5.replace(R.id.content, fragment5, "");
                        ft5.commit();
                        break;
                    case R.id.mn_KetQua:
                        actionBar.setTitle("K???t qu??? Sinh Vi??n");
                        navigationView.setCheckedItem(i);//T?? s??ng m???c ch???n
                        drawerLayout.closeDrawer(GravityCompat.START);//????ng navigation sau khi click

                        ResultFragment fragment6=new ResultFragment();
                        FragmentTransaction ft6=getSupportFragmentManager().beginTransaction();
                        ft6.replace(R.id.content, fragment6, "");
                        ft6.commit();
                        break;
                    case R.id.mn_LogOut:
                        finish();
                        break;
                }
                return false;
            }
        });

    }

    private void Init() {
        drawerLayout=findViewById(R.id.drawerlayout);
        toolbar=findViewById(R.id.toolbar);
        navigationView=findViewById(R.id.navigation);

        setSupportActionBar(toolbar);
        actionBar=getSupportActionBar();//Get actionbar
        actionBar.setTitle("Contact book FIT UTEHY");
        actionBar.setDisplayHomeAsUpEnabled(true);//Hi???n n??t b???m 3 g???ch (Ph???i c?? c??? ??o???n code(drawerToggle) b??n d?????i)

        //Thi???t l???p open, close cho drawer_layout (navigation)
        ActionBarDrawerToggle drawerToggle=
                new ActionBarDrawerToggle(MainActivity.this,drawerLayout,R.string.open,R.string.close);
        drawerToggle.syncState();//?????ng b???
    }

    private void setFirstItemNavigationView(){// Thi??t l???p navigation ???????c hi???n th??? khi start
        navigationView.getMenu().performIdentifierAction(R.id.mn_Home, 0);
        navigationView.setCheckedItem(R.id.mn_Home);

        HomeFragment fragment=new HomeFragment();
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, fragment, "");
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.mn_search:
                Toast.makeText(this, "Main!", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}