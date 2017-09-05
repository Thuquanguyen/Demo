package com.ksesoftware.htpig.sosapp.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.ksesoftware.htpig.sosapp.R;
import com.ksesoftware.htpig.sosapp.action.GetJsonDichVu;
import com.ksesoftware.htpig.sosapp.action.RequestHandler;
import com.ksesoftware.htpig.sosapp.adapter.AdapterDichVu;
import com.ksesoftware.htpig.sosapp.adapter.XeAdapter;
import com.ksesoftware.htpig.sosapp.app.AppConfig;
import com.ksesoftware.htpig.sosapp.model.DichVu;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Admin on 8/6/2017.
 */

public class AddDiaDiem extends AppCompatActivity implements View.OnClickListener {
    Button btnThem, btnAdd;
    EditText edtDiaChi, edtWebsite, edtThongTinChiTiet, edtTenDV, edtSdt;
    CircleImageView imgShow;
    private int PICK_IMAGE_REQUEST = 1;
    private Uri filePath;
    private Bitmap bitmap;
    public static final String KEY_IMAGE = "hinhanh";
    public static final String KEY_TEN = "tentrungtam";
    public static final String KEY_DIACHI = "diachi";
    public static final String KEY_WEBSITE = "website";
    public static final String KEY_SDT = "sdt";
    public static final String KEY_THONGTINCHITIET = "thongtinchitiet";
    public static final String KEY_TENXE = "tenxe";
    public static final String KEY_DICHVU = "dichvu";
    public static final String KEY_KINHDO = "kinhdo";
    public static final String KEY_VIDO = "vido";

    int REQUEST_CODE = 1;
    static Double lat, log;
    int i = 0;
    static String kiemtra;
    TextView txtChonDichVu;
    TextView txtChonDoiTuongDichVu;
    ArrayList<DichVu> list;

    RelativeLayout relChonDichVu;
    RelativeLayout relChonDoiTuong;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.themtrungtam);
        list = new ArrayList<>();
        new GetJsonDichVu(AddDiaDiem.this, AppConfig.URL_SELECT_DICHVU, list).execute();
        anhsa();
        relChonDichVu = (RelativeLayout) findViewById(R.id.relChonDichVu);
        relChonDoiTuong = (RelativeLayout) findViewById(R.id.relchonDoiTuong);
        relChonDichVu.setOnClickListener(this);
        relChonDoiTuong.setOnClickListener(this);
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kiemtra = "add";
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                Intent intent;
                try {
                    intent = builder.build(AddDiaDiem.this);
                    startActivityForResult(intent, REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
        imgShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kiemtra = "image";
                showFileChooser();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check()) {
                    uploadImage();
                } else {
                    showDialog("Thông báo", "Bạn nhập thiếu thông tin");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (kiemtra.equals("add")) {
            if (requestCode == REQUEST_CODE) {
                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(data, this);
                    lat = place.getLatLng().latitude;
                    log = place.getLatLng().longitude;
                    String address = (String) place.getAddress();
                    edtDiaChi.setText(address);
                }
            }
        }
        else if (kiemtra.equals("image")) {
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                filePath = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    imgShow.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showDialog(String Title, String body) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(AddDiaDiem.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(AddDiaDiem.this);
        }
        builder.setTitle(Title)
                .setMessage(body)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void uploadImage() {
        final String tenTrungTam = edtTenDV.getText().toString().trim();
        final String diachi = edtDiaChi.getText().toString().trim();
        final String website = edtWebsite.getText().toString().trim();
        final String sdt = edtSdt.getText().toString().trim();
        final String thongtinchitiet = edtThongTinChiTiet.getText().toString().trim();
        final String tenxe = txtChonDoiTuongDichVu.getText().toString().trim();
        final String dichvu = txtChonDichVu.getText().toString().trim();
        final String kinhdo = "" + lat;
        final String vido = "" + log;
        final String image = getStringImage(bitmap);
        class UploadImage extends AsyncTask<Bitmap, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(AddDiaDiem.this, "Uploading...!", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                startActivity(new Intent(AddDiaDiem.this, MapsActivity.class));
                Toast.makeText(AddDiaDiem.this, "Upload Success!", Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(Bitmap... params) {
                RequestHandler rh = new RequestHandler();
                HashMap<String, String> param = new HashMap<String, String>();
                param.put(KEY_TEN, tenTrungTam);
                param.put(KEY_DIACHI, diachi);
                param.put(KEY_WEBSITE, website);
                param.put(KEY_SDT, sdt);
                param.put(KEY_THONGTINCHITIET, thongtinchitiet);
                param.put(KEY_TENXE, tenxe);
                param.put(KEY_DICHVU, dichvu);
                param.put(KEY_KINHDO, kinhdo);
                param.put(KEY_VIDO, vido);
                param.put(KEY_IMAGE, image);
                String result = rh.sendPostRequest(AppConfig.UPLOAD_URL_UPLOAD, param);
                return result;
            }
        }
        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private boolean check() {

        txtChonDoiTuongDichVu.setText(XeAdapter.res);
        if (edtTenDV.getText().toString().isEmpty()) {
            return false;
        } else if (edtDiaChi.getText().toString().isEmpty()) {
            return false;
        } else if (edtSdt.getText().toString().isEmpty()) {
            return false;
        } else if (edtThongTinChiTiet.getText().toString().isEmpty()) {
            return false;
        } else if (edtWebsite.getText().toString().isEmpty()) {
            return false;
        } else if (lat == null) {
            return false;
        } else if (log == null) {
            return false;
        }
        return true;
    }

    private void anhsa() {
        btnThem = (Button) findViewById(R.id.btnChonDiaDiem);
        btnAdd = (Button) findViewById(R.id.btnSend);
        edtTenDV = (EditText) findViewById(R.id.edtTenDV);
        edtDiaChi = (EditText) findViewById(R.id.edtDiaChi);
        edtWebsite = (EditText) findViewById(R.id.edtWebsite);
        edtSdt = (EditText) findViewById(R.id.edtSDT);
        edtThongTinChiTiet = (EditText) findViewById(R.id.edtThongTinCT);
        txtChonDoiTuongDichVu = (TextView) findViewById(R.id.txtDoiTuongDaChon);
        txtChonDichVu = (TextView) findViewById(R.id.txtDichVuDaChon);
        imgShow = (CircleImageView) findViewById(R.id.imgAnh);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.relChonDichVu:
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.activity_dialog_dichvu);
                ListView listView = (ListView) dialog.findViewById(R.id.listDichVu);
                Button btnHoanThanh = (Button) dialog.findViewById(R.id.btnHoanThanh);
                AdapterDichVu adapter = new AdapterDichVu(AddDiaDiem.this, R.layout.custom_dialog_dichvu, list);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        DichVu country = (DichVu) adapterView.getItemAtPosition(i);
                    }
                });
                btnHoanThanh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String res = "";
                        for (DichVu dichVu : list) {
                            if (dichVu.isCheckBox()) {
                                if (!dichVu.getIdDichVu().equals("0"))
                                    if (res.equals(""))
                                        res = dichVu.getIdDichVu();
                                    else {
                                        res = res + "," + dichVu.getIdDichVu();
                                    }
                            }
                        }
                        txtChonDichVu.setText(res);
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            case R.id.relchonDoiTuong:
                XeAdapter.res = "";
                Intent intent = new Intent(AddDiaDiem.this, DoiTuongXe.class);
                Bundle bundle = new Bundle();
                bundle.putString("tentrungtam", edtTenDV.getText().toString());
                bundle.putString("sodienthoai", edtSdt.getText().toString());
                bundle.putString("diachi", edtDiaChi.getText().toString());
                bundle.putString("website", edtWebsite.getText().toString());
                bundle.putString("chondichvu", txtChonDichVu.getText().toString());
                bundle.putString("thongtinchitiet", edtThongTinChiTiet.getText().toString());
                intent.putExtra("them", bundle);
                startActivity(intent);
                break;
        }
    }
}
