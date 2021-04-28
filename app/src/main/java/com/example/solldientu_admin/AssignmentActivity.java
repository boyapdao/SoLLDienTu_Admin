package com.example.solldientu_admin;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ListView;

public class AssignmentActivity extends AppCompatActivity {

    Toolbar toolbar;
    ActionBar actionBar;
    ListView lv_mh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);
        Init();
    }

    private void Init() {
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar=getSupportActionBar();//Get actionbar
        actionBar.setTitle("Chọn môn giảng dạy");
        actionBar.setDisplayHomeAsUpEnabled(true);

        lv_mh=findViewById(R.id.lv_chonMon);
    }
}