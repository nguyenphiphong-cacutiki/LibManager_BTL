package com.example.libmanager_btl.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.libmanager_btl.R;
import com.example.libmanager_btl.adapter.PhieuMuonAdapter;
import com.example.libmanager_btl.adapter.SachSpinnerAdapter;
import com.example.libmanager_btl.adapter.ThanhVienSpinnerAdapter;
import com.example.libmanager_btl.dao.PhieuMuonDAO;
import com.example.libmanager_btl.dao.SachDAO;
import com.example.libmanager_btl.dao.ThanhVienDAO;
import com.example.libmanager_btl.model.Mode;
import com.example.libmanager_btl.model.PhieuMuon;
import com.example.libmanager_btl.model.Sach;
import com.example.libmanager_btl.model.ThanhVien;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class PhieuMuonFragment extends Fragment {
    private RecyclerView rcvPhieuMuon;
    private PhieuMuonAdapter adapter;
    private PhieuMuonDAO phieuMuonDB;
    private FloatingActionButton fab;
    private SimpleDateFormat sdf;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_phieu_muon, container, false);
        mappingAndInitializeVariable(v);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcvPhieuMuon.setLayoutManager(linearLayoutManager);
        updateRecycleView();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertAndDelete(getContext(), Mode.MODE_INSERT);
            }
        });
        return v;
    }
    private void mappingAndInitializeVariable(View v){
        rcvPhieuMuon = v.findViewById(R.id.rcvPhieuMuon);
        adapter = new PhieuMuonAdapter(getContext(), this);
        phieuMuonDB = new PhieuMuonDAO(getContext());
        fab = v.findViewById(R.id.fab);
        sdf = new SimpleDateFormat("dd/MM/yyyy");
    }
    private List<PhieuMuon> getData(){
        return phieuMuonDB.getAll();
    }
    private void updateRecycleView(){
        adapter.setData(getData());
        rcvPhieuMuon.setAdapter(adapter);

    }
    public void delete(String id){

    }
    public void insertAndDelete(Context context, int type){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_phieu_muon);
        // variable
        TextView tvMaPhieuMuon, tvNgay, tvSoLuong, tvTienThue, tvGiaThue;
        Spinner spLoaiSach, spThanhVien;
        CheckBox cbTraSach;
        ImageButton imbPlus, imbMinus;
        Button btSave, btDontSave;
        final int[] tienThue = {0};
        final String[] maThanhVien = new String[1];
        final String[] maSach = new String[1];
        final int[] slSachMuon = {1};
        final int[] slSachCon = new int[1];

        // mapping
        tvMaPhieuMuon = dialog.findViewById(R.id.tvMaSach);
        tvNgay = dialog.findViewById(R.id.tvNgay);
        tvSoLuong = dialog.findViewById(R.id.tvSoLuong);
        tvTienThue = dialog.findViewById(R.id.tvTienThue);
        spLoaiSach = dialog.findViewById(R.id.spSach);
        spThanhVien = dialog.findViewById(R.id.spThanhVien);
        cbTraSach = dialog.findViewById(R.id.chkDaTraSach);
        imbPlus = dialog.findViewById(R.id.btPlusSoLuong);
        imbMinus = dialog.findViewById(R.id.btminusSoLuong);
        btSave = dialog.findViewById(R.id.btSave);
        btDontSave = dialog.findViewById(R.id.btDontSave);
        tvGiaThue = dialog.findViewById(R.id.tvGiaThue);


        //
        tvSoLuong.setText("1");
        tvNgay.setText(sdf.format(new Date()));

        // spinner thanh vien
        ThanhVienDAO thanhVienDAO = new ThanhVienDAO(context);
        ArrayList<ThanhVien> thanhVienList =(ArrayList<ThanhVien>) thanhVienDAO.getAll();
        ThanhVienSpinnerAdapter thanhVienSpinnerAdapter = new ThanhVienSpinnerAdapter(context, thanhVienList);
        spThanhVien.setAdapter(thanhVienSpinnerAdapter);
        spThanhVien.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                maThanhVien[0] = thanhVienList.get(position).getMaTV()+"";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // spinner sach
        SachDAO sachDAO = new SachDAO(context);
        ArrayList<Sach> sachList = (ArrayList<Sach>) sachDAO.getAll();
        SachSpinnerAdapter sachSpinnerAdapter = new SachSpinnerAdapter(context, sachList);
        spLoaiSach.setAdapter(sachSpinnerAdapter);
        spLoaiSach.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                maSach[0] = sachList.get(position).getMaSach()+"";
                tienThue[0] = sachList.get(position).getGiaThue() * Integer.parseInt(tvSoLuong.getText().toString().trim());
                tvTienThue.setText("Tiền thuê: "+ tienThue[0]);
                tvGiaThue.setText("Giá thuê: "+ sachList.get(position).getGiaThue());
                slSachCon[0] = sachList.get(position).getSoLuong();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // change amount
        imbPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int oldAmount = slSachMuon[0];
                if(slSachMuon[0] + 1 <= sachDAO.getId(maSach[0]+"").getSoLuong()){
                    slSachMuon[0]++;
                    tvSoLuong.setText(slSachMuon[0]+"");

                    int tienthuecu = tienThue[0];
                    int tienhthuemoi = tienthuecu/oldAmount * slSachMuon[0];
                    tienThue[0] = tienhthuemoi;
                    tvTienThue.setText("Tiền thuê: "+tienhthuemoi);
                }else{
                    Toast.makeText(context, "Không thể mươn nhiều hơn số lượng sách hiện tại", Toast.LENGTH_SHORT).show();
                }
            }
        });
        imbMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int oldAmount = slSachMuon[0];
                if(slSachMuon[0]>1){
                    slSachMuon[0]--;
                    tvSoLuong.setText(slSachMuon[0]+"");


                    int tienthuecu = tienThue[0];
                    int tienhthuemoi = tienthuecu/oldAmount * slSachMuon[0];
                    tienThue[0] = tienhthuemoi;
                    tvTienThue.setText("Tiền thuê: "+tienhthuemoi);
                }
            }
        });
        btDontSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhieuMuon item = new PhieuMuon();
                item.setMaSach(Integer.parseInt(maSach[0]));
                item.setMaTV(Integer.parseInt(maThanhVien[0]));
                item.setNgay(new Date());
                item.setTienThue(tienThue[0]);
                item.setSoLuong(slSachMuon[0]);
                SharedPreferences sharedPreferences = context.getSharedPreferences("USER_FILE", Context.MODE_PRIVATE);
                String mTT = sharedPreferences.getString("user", "");
                item.setMaTT(mTT);
                if(cbTraSach.isChecked())
                    item.setTraSach(1);
                else
                    item.setTraSach(0);
                if(type == Mode.MODE_INSERT){
                    if(phieuMuonDB.insert(item) > 0){
                        Toast.makeText(context, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, "Thêm thất bại!", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });



        if(type == Mode.MODE_UPDATE){

        }
        dialog.show();
    }

}