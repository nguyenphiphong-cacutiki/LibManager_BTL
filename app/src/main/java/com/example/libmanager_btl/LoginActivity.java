package com.example.libmanager_btl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.libmanager_btl.dao.ThuThuDAO;
import com.example.libmanager_btl.database.DbHelper;

public class LoginActivity extends AppCompatActivity {
    EditText edUserName, edPassword;
    Button btLogin, btCancel;
    CheckBox chkRememberPass;
    String strUser, strPass;
    ThuThuDAO thuThuDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("ĐĂNG NHẬP");
        createData();
        thuThuDAO = new ThuThuDAO(this);
        mapping();
//        read user pass in SharedPreferences
        SharedPreferences pref = getSharedPreferences("USER_FILE", MODE_PRIVATE);
        String user = pref.getString("USERNAME", "");
        String pass = pref.getString("PASSWORD", "");
        Boolean rem = pref.getBoolean("REMEMBER", false);


        if(rem){
            edUserName.setText(user);
            edPassword.setText(pass);
            chkRememberPass.setChecked(rem);
        }

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edUserName.setText("");
                edPassword.setText("");
            }
        });
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(LoginActivity.this, MainActivity.class);
//                i.putExtra("user", strUser);
//                startActivity(i);
                checkLogin();
            }
        });
    }
    private void rememberUser(String u, String p, Boolean status){
        SharedPreferences pref = getSharedPreferences("USER_FILE", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        if(!status){
            editor.putString("USERNAME", u);
            editor.putString("PASSWORD", p);
            editor.putBoolean("REMEMBER", false);
        }else{
            editor.putString("USERNAME", u);
            editor.putString("PASSWORD", p);
            editor.putBoolean("REMEMBER", true);
        }
        editor.apply();
    }
    public void checkLogin(){
        strUser = edUserName.getText().toString().trim();
        strPass = edPassword.getText().toString().trim();
        if(strUser.isEmpty() || strPass.isEmpty()){
            Toast.makeText(this, "Vui lòng nhập thông tin đầy đủ!", Toast.LENGTH_SHORT).show();
        }else{
            if(thuThuDAO.checkLogin(strUser, strPass) > 0){
                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                rememberUser(strUser, strPass, chkRememberPass.isChecked());
                Intent i = new Intent(this, MainActivity.class);
                i.putExtra("user", strUser);
                startActivity(i);
                finish();
            }else{
                Toast.makeText(this, "Tên đăng nhập và mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void mapping(){
        edUserName = findViewById(R.id.edUserName);
        edPassword = findViewById(R.id.edPassword);
        btLogin = findViewById(R.id.btnLogin);
        btCancel = findViewById(R.id.btCancel);
        chkRememberPass = findViewById(R.id.chkRememberPass);
    }
    private void createData(){

        SharedPreferences sharedPreferences = getSharedPreferences("CREATE_DATA", MODE_PRIVATE);
        boolean status = sharedPreferences.getBoolean("IS_CREATE_DATA", false);
        if(!status){
            Log.d("***", "chưa có dl, bắt đầu tạo");
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("IS_CREATE_DATA", true);
            editor.apply();


            // create data

            String sqlInsertThuThu = "insert into ThuThu(maTT, HoTen, MatKhau) values " +
                    "('thuthu01', 'thu thu 01', '123'), " +
                    "('thuthu02', 'thu thu 02', '1234'), " +
                    "('thuthu03', 'thu thu 03', '12345')";

            String sqlInsertThanhVien = "insert into ThanhVien(hoTen, namSinh) values " +
                    "('Trần Văn Ngọc', '2001'), " +
                    "('Trần Kiều Ân', '2002'), " +
                    "('Kiều Phong', '2003')";
            String sqlInsertLoaiSach = "insert into LoaiSach(tenLoai) values " +
                    "('Sống thực tế giữa đời thực dụng'), " +
                    "('Đừng bao giờ đi ăn một mình'), " +
                    "('Giết chết con quỷ trong bạn')";
            String sqlInsertSach = "insert into Sach(tenSach, giaThue, maLoai, soLuong) values " +
                    "('Đắc nhân tâm', '20000', '1', '3')," +
                    "('Sự im lặng của bầy cừu', '30000', '1', '5')," +
                    "('Không gia đình', '25000', '2', '8')";
            String sqlInsertPhieuMuon = "insert into PhieuMuon(maTT, maTV, maSach, tienThue, soLuong, ngay, traSach) values" +
                    "('thuthu01', '1', '1', '20000', '2', '2022-12-1', '1'), " +
                    "('thuthu01', '1', '2', '30000', '3', '2022-12-2', '-1'), " +
                    "('thuthu02', '1', '2', '40000', '4', '2022-12-3', '1')";


            DbHelper dbHelper = new DbHelper(this);
            SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
            sqLiteDatabase.execSQL(sqlInsertThuThu);
            sqLiteDatabase.execSQL(sqlInsertThanhVien);
            sqLiteDatabase.execSQL(sqlInsertLoaiSach);
            sqLiteDatabase.execSQL(sqlInsertPhieuMuon);
            sqLiteDatabase.execSQL(sqlInsertSach);

        }else{
            Log.d("***", "đã có dl");
        }


    }
}