package com.example.libmanager_btl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.libmanager_btl.dao.DemoDb;
import com.example.libmanager_btl.database.DbHelper;
import com.example.libmanager_btl.fragment.ChangePassFragment;
import com.example.libmanager_btl.fragment.LoaiSachFragment;
import com.example.libmanager_btl.fragment.SachFragment;
import com.example.libmanager_btl.fragment.ThanhVienFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawer;
    Toolbar toolbar;
    View mHeaderView;
    TextView edUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        createData();
        drawer = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar1);

        // set toolbar instead actionbar
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();

        ab.setHomeAsUpIndicator(R.drawable.ic_launcher_background);
        ab.setDisplayHomeAsUpEnabled(true);
//        ab.setTitle("Quản lý thư viện");
        setTitle("Quản lý thư viện");

        NavigationView nv = findViewById(R.id.nvView);
        //show user in header
        mHeaderView = nv.getHeaderView(0);
        edUser = mHeaderView.findViewById(R.id.txtUser);
        Intent i = getIntent();
        edUser.setText("Welcome !");

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager manager = getSupportFragmentManager();
                if(item.getItemId() == R.id.nav_PhieuMuon){
                    setTitle("Quản lý phiếu mượn");
                    Toast.makeText(MainActivity.this, "Quản lý phiếu mượn", Toast.LENGTH_SHORT).show();
                }else if(item.getItemId() == R.id.nav_LoaiSach){
                    setTitle("Quản lý loại sách");
                    LoaiSachFragment loaiSachFragment = new LoaiSachFragment();
                    manager.beginTransaction()
                            .replace(R.id.flContent, loaiSachFragment)
                            .commit();
                }else if(item.getItemId() == R.id.nav_Sach){
                    setTitle("Quản lý sách");
                    SachFragment sachFragment = new SachFragment();
                    manager.beginTransaction()
                            .replace(R.id.flContent, sachFragment)
                            .commit();
                }else if(item.getItemId() == R.id.nav_ThanhVien){
                    setTitle("Quản lý thành viên");
                    ThanhVienFragment thanhVienFragment = new ThanhVienFragment();
                    manager.beginTransaction()
                            .replace(R.id.flContent, thanhVienFragment)
                            .commit();
                }else if(item.getItemId() == R.id.sub_Top){
                    setTitle("Top 10 sách cho thuê nhiều nhất");
                }else if(item.getItemId() == R.id.sub_DoanhThu){
                    setTitle("Thống kê doanh thu");
                }else if(item.getItemId() == R.id.sub_AddUser){
                    setTitle("Thêm người dùng");
                }else if(item.getItemId() == R.id.sub_Pass){
                    setTitle("Thay đổi mật khẩu");
                    ChangePassFragment changePassFragment = new ChangePassFragment();
                    manager.beginTransaction()
                            .replace(R.id.flContent, changePassFragment)
                            .commit();
                }else if(item.getItemId() == R.id.sub_logout){
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }else{
                    setTitle("unknown");
                }
//                switch (item.getItemId()){
//                    case R.id.nav_PhieuMuon:
//                        setTitle("Quản lý phiếu mượn");
//                        break;
//                    case R.id.nav_LoaiSach:
//                        setTitle("Quản lý loại sách");
//                        break;
//                    case R.id.nav_Sach:
//                        setTitle("Quản lý sách");
//                        break;
//                    case R.id.nav_ThanhVien:
//                        setTitle("Quản lý thành viên");
//                        break;
//                    case R.id.sub_Top:
//                        setTitle("Top 10 sách cho thuê nhiều nhất");
//                        break;
//                    case R.id.sub_DoanhThu:
//                        setTitle("Thống kê doanh thu");
//                        break;
//                    case R.id.sub_AddUser:
//                        setTitle("Thêm người dùng");
//                        break;
//                    case R.id.sub_Pass:
//                        setTitle("Thay đổi mật khẩu");
//                        break;
//                    case R.id.sub_logout:
//
//                        break;
//
//                }


                drawer.closeDrawers();


                return false;
            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home)
            drawer.openDrawer(GravityCompat.START);


        return super.onOptionsItemSelected(item);
    }

}