package com.ksesoftware.htpig.sosapp.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.data.Session;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;
import com.ksesoftware.htpig.sosapp.R;
import com.ksesoftware.htpig.sosapp.action.Activity_About;
import com.ksesoftware.htpig.sosapp.action.Activity_Setting;
import com.ksesoftware.htpig.sosapp.action.CustomHttpClient;
import com.ksesoftware.htpig.sosapp.action.Directions;
import com.ksesoftware.htpig.sosapp.action.GetJsonChiTietXe;
import com.ksesoftware.htpig.sosapp.action.GetJsonDichVu;
import com.ksesoftware.htpig.sosapp.action.GetJsonLoaiXe;
import com.ksesoftware.htpig.sosapp.action.GetJsonLoaiXe2;
import com.ksesoftware.htpig.sosapp.action.GetJsonTrungTam;
import com.ksesoftware.htpig.sosapp.action.GetJsonTrungTamDichVu;
import com.ksesoftware.htpig.sosapp.action.GetJsonTrungTamXe;
import com.ksesoftware.htpig.sosapp.action.MapWrapperLayout;
import com.ksesoftware.htpig.sosapp.action.OnInfoWindowElemTouchListener;
import com.ksesoftware.htpig.sosapp.app.AppConfig;
import com.ksesoftware.htpig.sosapp.databinding.ActivitySplashBinding;
import com.ksesoftware.htpig.sosapp.helper.SQLiteHandler;
import com.ksesoftware.htpig.sosapp.lib.BottomSheetBehaviorGoogleMapsLike;
import com.ksesoftware.htpig.sosapp.lib.MergedAppBarLayoutBehavior;
import com.ksesoftware.htpig.sosapp.model.DichVu;
import com.ksesoftware.htpig.sosapp.model.Xe;
import com.ksesoftware.htpig.sosapp.sample.ItemPagerAdapter;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.ksesoftware.htpig.sosapp.app.AppConfig.URL_LOAI;
import static com.ksesoftware.htpig.sosapp.app.AppConfig.URL_SELECT_TRUNGTAM;
import static com.ksesoftware.htpig.sosapp.app.AppConfig.URL_UPDATE_DANHGIA;


public class MapsActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,
        OnMapReadyCallback, LocationListener {
    CoordinatorLayout coordinatorLayout;
    static ArrayList<HashMap<String, String>> contactList;
    private DrawerLayout drawerLayout;
    ShareDialog shareDialog;
    ShareLinkContent shareLinkContent;
    private MaterialMenuDrawable materialMenu;
    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;
    public static GoogleMap map;
    static boolean block = false;
    AlertDialog alert;
    Marker localMarker;
    MarkerOptions markerOptions;
    public static int values = 5000;
    ArrayList<LatLng> markerPoints;
    Polyline polyline;
    static double latitude;
    static double longitude;
    static LatLng vtHienTai;
    Geocoder geocoder;
    static LatLng lat1;
    static Marker mk;
    List<Address> addresses;
    RelativeLayout rela;
    Button floattingCall;
    int a1 = 0;
    GoogleApiClient mGoogleApiClient;
    static int vitri;
    static String chiso;
    Toolbar toolbar;
    ArrayList<Xe> listLoaiXe;
    ArrayList<Xe> listLoaiXe1;
    ArrayList<Xe> listLoaiXeTrungTam;
    ArrayList<Xe> listChiTiet;
    ArrayList<DichVu> listDichVu;
    ArrayList<DichVu> listDichVu1;
    static ArrayList<DichVu> listDichVuCoppy;
    ArrayList<Xe> listChiTietXe;
    public static String check2 = "";
    static int check = 0;
    SQLiteHandler sqLiteHandler;
    static float up;
    /*Khai báo breoadcast để lấy dữ liệu từ bên GETJSON... về*/
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (check != 0) {
                if (listLoaiXe != null && spnLoaiXe != null && listChiTietXe != null && spnChiTietXe != null) {
                    SpinnerLoaiXe();
                    SpinnerHangXe();
                }

            }
        }
    };
    ArrayAdapter<Xe> adapter1;
    ArrayAdapter<Xe> adapter;
    CardView cardView;
    Spinner spnLoaiXe, spnChiTietXe;
    int[] mDrawables = {
            R.drawable.sssss,
            R.drawable.sssss,
            R.drawable.sssss,
            R.drawable.sssss,
            R.drawable.sssss,
            R.drawable.sssss
    };
    TextView bottomSheetTextView;
    private static final String TAG_SUCCESS = "success";
    private ViewGroup infoWindow;
    private TextView txtTenTrungTam, txtDiaChi, txtKhoangCach, txtThoiGian;
    private Button imgThongTin, btnClose;
    private CircleImageView imgHinhAnh;
    private OnInfoWindowElemTouchListener infoButtonListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sqLiteHandler = new SQLiteHandler(this);
        /*Khai báo FacebookSdk*/
        FacebookSdk.sdkInitialize(getApplicationContext());
        rela = (RelativeLayout) findViewById(R.id.real);
        floattingCall = (Button) findViewById(R.id.floattingCall);
        /*Nguyen Quang Thu*/
        /*Khời tạo markerPoints*/
        markerPoints = new ArrayList<LatLng>();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        shareDialog = new ShareDialog(MapsActivity.this);

        /*Kiểm tra đã đăng nhập vào Gmail hay chưa*/
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        /*Kết nối tới googleAPIClient để xin cấp quyền*/
        config(10);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        googleApiClient.connect();

        /*Nút trở về vị trí ban đầu*/
        View mapView = mapFragment.getView();
        if (mapView != null && mapView.findViewById(1) != null) {
            mapView.setBackgroundResource(R.drawable.add);
            View locationButton = ((View) mapView.findViewById(1).getParent()).findViewById(2);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 30, 150);
        }
    /*Marker Option*/
        mapFragment.getMapAsync(this);
        /*Khởi tạo các đối tượng Arraylist*/
        contactList = new ArrayList<>();
        listLoaiXe = new ArrayList<>();
        listLoaiXe1 = new ArrayList<>();
        listDichVu = new ArrayList<>();
        listLoaiXeTrungTam = new ArrayList<>();
        listChiTiet = new ArrayList<>();
        listDichVuCoppy = new ArrayList<>();
        listDichVu1 = new ArrayList<>();
        listChiTietXe = new ArrayList<>();
        /*lấy kết quả từ class GETJSON... về thông quan tham số truyền vào của IntentFilter*/
        registerReceiver(broadcastReceiver, new IntentFilter("ketqua"));
        /*Lấy dữ liệu đổ lên contaclist*/
        new GetJsonTrungTam(MapsActivity.this, URL_SELECT_TRUNGTAM, contactList).execute();
        init();
        if (map != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            map.setMyLocationEnabled(true);
        }

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.htpig.MapsActivity",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {
        }
/*Láy dữ liệu về đồ lên Arraylist*/
        new GetJsonLoaiXe(MapsActivity.this, AppConfig.URL_LOAI, listLoaiXe).execute();
        new GetJsonChiTietXe(MapsActivity.this, AppConfig.URL_SELECT_CHITIETXE, listChiTiet).execute();
        new GetJsonDichVu(MapsActivity.this, AppConfig.URL_SELECT_DICHVU, listDichVu).execute();
        new GetJsonLoaiXe2(MapsActivity.this, AppConfig.URL_LOAI, listLoaiXe1).execute();
        new GetJsonTrungTamDichVu(MapsActivity.this, AppConfig.URL_SELECT_TRUNGTAM, listDichVu1).execute();
        new GetJsonTrungTamXe(MapsActivity.this, AppConfig.URL_SELECT_TRUNGTAM, listLoaiXeTrungTam).execute();
        /*Kiểm tra xem đã chọn đối tượng hiển thị trong spinner hay chưa nếu chưa chọn thì show dialog cho người dùng chọn*/
        if (check == 0) {
            check++;
            final Dialog dialog = new Dialog(MapsActivity.this);
            dialog.setContentView(R.layout.activity_dialogloaixe);
            spnLoaiXe = (Spinner) dialog.findViewById(R.id.spinnerLoaiXe);
            spnChiTietXe = (Spinner) dialog.findViewById(R.id.spinnerChiTietXe);
            Button btnXacNhan;
            final RadioButton radioTatCa, radioChonLoc;
            radioChonLoc = (RadioButton) dialog.findViewById(R.id.radioChonLoc);
            radioTatCa = (RadioButton) dialog.findViewById(R.id.radioTatCa);
            btnXacNhan = (Button) dialog.findViewById(R.id.btnXacNhan);
            SpinnerLoaiXe();
            SpinnerHangXe();
            btnXacNhan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (radioTatCa.isChecked() == true) {
                        displayTatCaTrungTam(Integer.parseInt(listLoaiXe.get(vitri).getIdXe()));
                    } else {
                        displayTrungTamChiTiet(listChiTietXe.get(vitri).getIdXe());
                    }
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
        /*giống bên trên*/
        cardView = (CardView) findViewById(R.id.btnCall);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check++;
                final Dialog dialog = new Dialog(MapsActivity.this);
                dialog.setContentView(R.layout.activity_dialogloaixe);
                spnLoaiXe = (Spinner) dialog.findViewById(R.id.spinnerLoaiXe);
                spnChiTietXe = (Spinner) dialog.findViewById(R.id.spinnerChiTietXe);
                Button btnXacNhan;
                final RadioButton radioTatCa, radioChonLoc;
                radioChonLoc = (RadioButton) dialog.findViewById(R.id.radioChonLoc);
                radioTatCa = (RadioButton) dialog.findViewById(R.id.radioTatCa);
                btnXacNhan = (Button) dialog.findViewById(R.id.btnXacNhan);
                SpinnerLoaiXe();
                SpinnerHangXe();
                btnXacNhan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (radioTatCa.isChecked() == true) {
                            displayTatCaTrungTam(Integer.parseInt(listLoaiXe.get(vitri).getIdXe()));
                        } else {
                            displayTrungTamChiTiet(listChiTietXe.get(vitri).getIdXe());
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });
        /*Khởi tạo coordinatorLayout để hiển thị thông tin chi tiết dịch vụ*/
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout);
        View bottomSheet = coordinatorLayout.findViewById(R.id.bottom_sheet);
        bottomSheet.setPadding(20, 0, 20, 0);
        final BottomSheetBehaviorGoogleMapsLike behavior = BottomSheetBehaviorGoogleMapsLike.from(bottomSheet);
        behavior.addBottomSheetCallback(new BottomSheetBehaviorGoogleMapsLike.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehaviorGoogleMapsLike.STATE_COLLAPSED:
                        cardView.setVisibility(View.VISIBLE);
                        toolbar.setVisibility(View.VISIBLE);
                        bottomSheet.setPadding(20, 0, 20, 0);
                        Log.d("bottomsheet-", "STATE_COLLAPSED");
                        break;
                    case BottomSheetBehaviorGoogleMapsLike.STATE_DRAGGING:
                        cardView.setVisibility(View.INVISIBLE);
                        toolbar.setVisibility(View.INVISIBLE);
                        bottomSheet.setPadding(0, 0, 0, 0);
                        Log.d("bottomsheet-", "STATE_DRAGGING");
                        break;
                    case BottomSheetBehaviorGoogleMapsLike.STATE_EXPANDED:
                        cardView.setVisibility(View.INVISIBLE);
                        toolbar.setVisibility(View.INVISIBLE);
                        bottomSheet.setPadding(0, 0, 0, 0);
                        Log.d("bottomsheet-", "STATE_EXPANDED");
                        break;
                    case BottomSheetBehaviorGoogleMapsLike.STATE_ANCHOR_POINT:
                        cardView.setVisibility(View.INVISIBLE);
                        toolbar.setVisibility(View.INVISIBLE);
                        bottomSheet.setPadding(0, 0, 0, 0);
                        Log.d("bottomsheet-", "STATE_ANCHOR_POINT");
                        break;
                    case BottomSheetBehaviorGoogleMapsLike.STATE_HIDDEN:
                        cardView.setVisibility(View.VISIBLE);
                        toolbar.setVisibility(View.VISIBLE);
                        bottomSheet.setPadding(0, 0, 0, 0);
                        Log.d("bottomsheet-", "STATE_HIDDEN");
                        break;
                    default:
                        bottomSheet.setPadding(20, 0, 20, 0);
                        Log.d("bottomsheet-", "STATE_SETTLING");
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_COLLAPSED);
    }

    /*Thiết đặt chiều cao phần hiển thị của coortrainLayot*/
    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.3f);
    }

    /*Lấy dữ liệu đổ lên Spinner */
    public void SpinnerLoaiXe() {
        if (listLoaiXe != null) {
            adapter = new ArrayAdapter<Xe>(this, android.R.layout.simple_list_item_1, listLoaiXe);
            adapter.notifyDataSetChanged();
            spnLoaiXe.setAdapter(adapter);
            spnLoaiXe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    displayTenXe(i);
                    vitri = i;

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

        }
    }

    /*Lấy dữ liệu đổ lên spinner hãng xe với điều kiện phụ thuộc vào spinner loại xe*/
    public void SpinnerHangXe() {

        adapter1 = new ArrayAdapter<Xe>(this, android.R.layout.simple_list_item_1, listChiTietXe);
        spnChiTietXe.setAdapter(adapter1);
        spnChiTietXe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                vitri = i;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }


    /*hiển thị thông tin xe của hãng*/
    public void displayTenXe(int k) {
        listChiTietXe.clear();
        String b[];
        if (listLoaiXe1.size() > 0) {
            b = listLoaiXe1.get(k).getTenXe().split(",");
            for (int j = 0; j < b.length; j++) {
                for (int i = 0; i < listChiTiet.size(); i++) {
                    if (listChiTiet.get(i).getIdXe().equals(b[j])) {
                        Xe xe = new Xe();
                        xe.setIdXe(listChiTiet.get(i).getIdXe());
                        xe.setTenXe(listChiTiet.get(i).getTenXe());
                        xe.setState(false);
                        if (xe != null) {
                            /*thêm vào Arraylist*/
                            listChiTietXe.add(xe);
                            /*Cập nhật lại array mỗi khi thêm*/
                            adapter1.notifyDataSetChanged();
                            /*Add dữ liệu vào spinner hãng xe*/
                            SpinnerHangXe();
                        }
                    }
                }
            }
        }
    }

    /*Xe của trung tâm*/
    public void displayTrungTamXe(int k) {
        listChiTietXe.clear();
        String b[];
        if (listLoaiXeTrungTam.size() > 0) {
            b = listLoaiXeTrungTam.get(k).getTenXe().split(",");
            for (int j = 0; j < b.length; j++) {
                for (int i = 0; i < listChiTiet.size(); i++) {
                    if (listChiTiet.get(i).getIdXe().equals(b[j])) {
                        Xe xe = new Xe();
                        xe.setIdXe(listChiTiet.get(i).getIdXe());
                        xe.setTenXe(listChiTiet.get(i).getTenXe());
                        xe.setState(false);
                        if (xe != null) {
                            listChiTietXe.add(xe);
                        }
                    }
                }
            }
        }
    }

    /*Dich vụ của trung tâm*/
    public void displayDichVuTrungTam(int k) {
        listDichVuCoppy.clear();
        String b[];
        if (listDichVu1.size() > 0 && listDichVu.size() > 0) {
            b = listDichVu1.get(k).getTenDichVu().split(",");
            for (int j = 0; j < b.length; j++) {
                for (int i = 0; i < listDichVu.size(); i++) {
                    if (listDichVu.get(i).getIdDichVu().equals(b[j])) {
                        DichVu dichvu = new DichVu();
                        dichvu.setIdDichVu(listDichVu.get(i).getIdDichVu());
                        dichvu.setTenDichVu(listDichVu.get(i).getTenDichVu());
                        dichvu.setCheckBox(false);
                        if (dichvu != null) {
                            listDichVuCoppy.add(dichvu);
                        }
                    }
                }
            }
        }
    }

    /*Hiển thị các địa điểm trung tâm thỏa mãn bán kính mà người dùng đã thiết đặt*/
    public void displayTrungTamChiTiet(String str) {
        map.clear();
        String b[];
        LatLng vtCuuHo = null;
        map.clear();
        System.out.println(contactList.size());
        for (int k = 0; k < contactList.size(); k++) {
            b = contactList.get(k).get("tenxe").split(",");
            Double a = Double.parseDouble(contactList.get(k).get("kinhdo"));
            Double c = Double.parseDouble(contactList.get(k).get("vido"));
            vtCuuHo = new LatLng(a, c);
            for (int j = 0; j < b.length; j++) {
                if (b[j].equals(str)) {
                    if (vtHienTai != null) {
                        vtCuuHo = new LatLng(a, c);
                        double distance = SphericalUtil.computeDistanceBetween(vtHienTai, vtCuuHo);
                        if (distance <= values) {
                            map.addMarker(new MarkerOptions().position(vtCuuHo).title(contactList.get(k).get("tentrungtam")).snippet(contactList.get(k).get("diachi")).rotation(k).icon(BitmapDescriptorFactory.fromResource(R.drawable.port)));
                        }
                    }
                }
            }
        }
    }

    /*hiển thị tất cả các trung tâm thỏa mãn bán kính và các yêu cầu của người dùng*/
    public void displayTatCaTrungTam(int str) {
        map.clear();
        String b[];
        LatLng vtCuuHo = null;
        map.clear();
        for (int k = 0; k < contactList.size(); k++) {
            b = contactList.get(k).get("loaixe").split(",");
            Double a = Double.parseDouble(contactList.get(k).get("kinhdo"));
            Double c = Double.parseDouble(contactList.get(k).get("vido"));
            vtCuuHo = new LatLng(a, c);
            for (int j = 0; j < b.length; j++) {
                if (Integer.parseInt(b[j]) == str) {
                    if (vtHienTai != null) {
                        vtCuuHo = new LatLng(a, c);
                        double distance = SphericalUtil.computeDistanceBetween(vtHienTai, vtCuuHo);
                        if (distance <= values) {
                            map.addMarker(new MarkerOptions().position(vtCuuHo).title(contactList.get(k).get("tentrungtam")).snippet(contactList.get(k).get("diachi")).rotation(k).icon(BitmapDescriptorFactory.fromResource(R.drawable.port)));
                        }
                    }

                }
            }
        }
    }

    /*Thanh hiển thị navigation drawble*/
    private void init() {
        drawerLayout = (DrawerLayout) findViewById(R.id.dra_layout);
        // cvICE = (Button) findViewById(R.id.cvICE);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        materialMenu = new MaterialMenuDrawable(this, Color.WHITE, MaterialMenuDrawable.Stroke.THIN);
        toolbar.setNavigationIcon(materialMenu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.itemSetting:
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        Intent intent = new Intent(MapsActivity.this, Activity_Setting.class);
                        startActivity(intent);
                        break;
                    case R.id.itemHuongDan:
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        Intent i = new Intent(MapsActivity.this, Activity_About.class);
                        startActivity(i);
                        break;
                    case R.id.itemShare:
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        if (ShareDialog.canShow(ShareLinkContent.class)) {
                            shareLinkContent = new ShareLinkContent.Builder()
                                    .setContentTitle("App SOS")
                                    .setContentDescription("App Cứu Hộ")
                                    .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.ksesoftware.htpig.sosapp&hl=vi"))
                                    .build();
                        }
                        shareDialog.show(shareLinkContent);
                    case R.id.itemThemDiaDiem:
                        drawerLayout.closeDrawer(Gravity.LEFT);

                        startActivity(new Intent(MapsActivity.this, AddDiaDiem.class));
                        break;
                    case R.id.itemLogout:
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        check2 = "check";
                        LoginManager.getInstance().logOut();
                        SharedPreferences sharedPreferences = getSharedPreferences("my_data", MODE_PRIVATE);
                        SharedPreferences.Editor editorr = sharedPreferences.edit();
                        sqLiteHandler.deleteUsers();
                        sharedPreferences.edit();
                        editorr.clear();
                        editorr.commit();
                        signOut();
                        Intent intent1 = new Intent(MapsActivity.this, LoginActivity.class);
                        startActivity(intent1);
                        break;
                }
                return true;
            }
        });
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String result = data.getStringExtra("reason");
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        final MapWrapperLayout mapWrapperLayout = (MapWrapperLayout) findViewById(R.id.map_relative_layout);
        this.map = googleMap;

        /*Sự kiện khi map đang tải */
        map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                map.setMyLocationEnabled(true);
            }
        });
        /*Ẩn thanh thông tin chi tiết*/
        coordinatorLayout.setVisibility(View.INVISIBLE);
        /*Sự kiện khi người dùng click vào map*/
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                lat1 = latLng;
                coordinatorLayout.setVisibility(View.INVISIBLE);
                toolbar.setVisibility(View.VISIBLE);
                cardView.setVisibility(View.VISIBLE);
            }
        });

        /*Bottom behevi*/
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                mk = marker;
                String url = null;
                LatLng desLatlg = marker.getPosition();
                if (polyline != null) polyline.remove();
                if (vtHienTai != null) {
                    url = getDirectionsUrl(vtHienTai, desLatlg);
                }
                DownloadTask downloadTask = new DownloadTask();
                downloadTask.execute(url);

                /*AHOLA*/
                chiso = contactList.get((int) marker.getRotation()).get("id");
                // Here we can perform some action triggered after clicking the button
                toolbar.setVisibility(View.VISIBLE);
                cardView.setVisibility(View.VISIBLE);
                coordinatorLayout.setVisibility(View.VISIBLE);

                TextView txttentrungtam, txtdiachi, txtsodienthoai, txttrangweb, txtDichVuHoTro, txtloaixehotro, txtThongtin;
                RatingBar txtDanhGia;
                Button btnDanhGia, btnGoiDien;
                btnGoiDien = (Button) findViewById(R.id.btnGOIDIEN);
                btnDanhGia = (Button) findViewById(R.id.btnDANHGIA);
                txttentrungtam = (TextView) findViewById(R.id.txtTENTRUNGTAM);
                txtdiachi = (TextView) findViewById(R.id.txtDIACHI);
                txtsodienthoai = (TextView) findViewById(R.id.txtSODIENTHOAI);
                txttrangweb = (TextView) findViewById(R.id.txtTRANGWEB);
                txtDichVuHoTro = (TextView) findViewById(R.id.txtDICHVUHOTRO);
                txtloaixehotro = (TextView) findViewById(R.id.txtLOAIXEHOTRO);
                txtThongtin = (TextView) findViewById(R.id.txtTHONGTIN);
                txtDanhGia = (RatingBar) findViewById(R.id.txtDANHGIA);
                txtDanhGia.setRating(Float.valueOf(contactList.get((int) marker.getRotation()).get("danhgia")));
                txtdiachi.setText(contactList.get((int) marker.getRotation()).get("diachi"));
                displayTrungTamXe((int) marker.getRotation());
                String str = "";
                for (int i = 0; i < listChiTietXe.size(); i++) {
                    str += listChiTietXe.get(i).getTenXe() + ",";
                }
                txtloaixehotro.setText(str);

                displayDichVuTrungTam(((int) marker.getRotation()));
                String str1 = "";
                for (int i = 0; i < listDichVuCoppy.size(); i++) {
                    str1 += listDichVuCoppy.get(i).getTenDichVu() + ",";
                }
                txtDichVuHoTro.setText(str1);
                txttrangweb.setText(contactList.get((int) marker.getRotation()).get("website"));
                txtsodienthoai.setText(contactList.get((int) marker.getRotation()).get("sdt"));
                txttentrungtam.setText(contactList.get((int) marker.getRotation()).get("tentrungtam"));
                txtThongtin.setText(contactList.get((int) marker.getRotation()).get("thongtinchitiet"));
                btnDanhGia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog dialog = new Dialog(MapsActivity.this);
                        dialog.setContentView(R.layout.dialog_danhgia);
                        RatingBar ratingBar;
                        Button btnDanhGia;
                        ratingBar = (RatingBar) dialog.findViewById(R.id.ratingBar);
                        btnDanhGia = (Button) dialog.findViewById(R.id.btnSubmit);
                        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                            public void onRatingChanged(RatingBar ratingBar, float rating,
                                                        boolean fromUser) {
                                float a = Float.valueOf(contactList.get((int) marker.getRotation()).get("danhgia"));
                                Toast.makeText(MapsActivity.this,
                                        String.valueOf((rating + a) / 2),
                                        Toast.LENGTH_SHORT).show();
                                up = (rating + a) / 2;
                            }
                        });
                        btnDanhGia.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                new CreateNewProduct().execute();
                                Toast.makeText(MapsActivity.this, "Cảm ơn bạn đã đánh giá!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                });
                btnGoiDien.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:" + Uri.encode(contactList.get((int) marker.getRotation()).get("sdt").trim())));
                        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(callIntent);
                        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        startActivity(callIntent);
                    }
                });

                   /*Marker Option*/
                return false;
            }
        });
        mapWrapperLayout.init(map, getPixelsFromDp(MapsActivity.this, 30 + 30));
        this.infoWindow = (ViewGroup) getLayoutInflater().inflate(R.layout.layout_maker, null);
        this.txtTenTrungTam = (TextView) infoWindow.findViewById(R.id.txtTenTrungTam);
        this.txtDiaChi = (TextView) infoWindow.findViewById(R.id.txtDiaChi);
        this.txtKhoangCach = (TextView) infoWindow.findViewById(R.id.txtQuangDuong);
        this.txtThoiGian = (TextView) infoWindow.findViewById(R.id.txtThoiGian);
        // Picasso.with(getApplicationContext()).load("").into(imgHinhAnh);
        this.imgThongTin = (Button) infoWindow.findViewById(R.id.imageThongTin);
        this.btnClose = (Button) infoWindow.findViewById(R.id.btnClose);

        this.infoButtonListener = new OnInfoWindowElemTouchListener(btnClose) {
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                mk.hideInfoWindow();
            }
        };
        this.btnClose.setOnTouchListener(infoButtonListener);
        this.infoButtonListener = new OnInfoWindowElemTouchListener(imgThongTin) //btn_default_pressed_holo_light
        {
            @Override
            protected void onClickConfirmed(View v, final Marker marker) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + Uri.encode(contactList.get((int) marker.getRotation()).get("sdt").trim())));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(callIntent);
                if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(callIntent);
            }
        };
        this.imgThongTin.setOnTouchListener(infoButtonListener);

        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Setting up the infoWindow with current's marker info
                txtTenTrungTam.setText(marker.getTitle());
                txtDiaChi.setText(marker.getSnippet());
                if (vtHienTai != null) {
                    LatLng vtCuuHo = null;
                    Double a = Double.parseDouble(contactList.get((int) marker.getRotation()).get("kinhdo"));
                    Double b = Double.parseDouble(contactList.get((int) marker.getRotation()).get("vido"));
                    vtCuuHo = new LatLng(a, b);
                    double distance = SphericalUtil.computeDistanceBetween(vtHienTai, vtCuuHo) / 1000;
                    DecimalFormat df = new DecimalFormat("#.0");
                    txtKhoangCach.setText(df.format(distance) + "");
                    txtThoiGian.setText((df.format((SphericalUtil.computeDistanceBetween(vtHienTai, vtCuuHo) * 60 / 1000) / 55) + ""));

                }
                infoButtonListener.setMarker(marker);

                // We must call this to set the current marker and infoWindow references
                // to the MapWrapperLayout
                mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
                return infoWindow;
            }
        });
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);

    }

    /*Update ratting*/
    class CreateNewProduct extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MapsActivity.this);
            pDialog.setMessage("Adding details..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            JSONObject json = null;
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id", chiso));
            params.add(new BasicNameValuePair("danhgia", String.valueOf(up)));
         /**/
            // getting JSON Object
            // Note that create product url accepts POST method
            int success = 0;
            json = CustomHttpClient.makeHttpRequest(URL_UPDATE_DANHGIA, "POST", params);
            // check for success tag
            try {
                success = json.getInt(TAG_SUCCESS);
                unregisterReceiver(broadcastReceiver);
            } catch (Exception e) {
                e.printStackTrace();
                success = 0;
            }
            return "" + success;
        }

        protected void onPostExecute(String result) {
            pDialog.dismiss();
            if (Integer.parseInt(result) == 1) {
            } else {
                //your task here
            }
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        vtHienTai = new LatLng(latitude, longitude);

        if (localMarker != null)
            localMarker.remove();
        if (a1 == 0) {
            localMarker = map.addMarker(new MarkerOptions().position(vtHienTai).title("Vị trí của tôi!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 13));
            a1++;
        }
        geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            String diachi = addresses.get(0).getAddressLine(0);
            String quan = addresses.get(0).getSubLocality();
            String thanhPho = addresses.get(0).getAdminArea();
            String noichon = addresses.get(0).getCountryName();

            String fullAddress = diachi + "," + quan + "," + thanhPho + "," + noichon;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onConnected(Bundle bundle) {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(5000);
        locationRequest.setInterval(10000);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                showPermissionDialog();
                break;
        }
    }

    private void showPermissionDialog() {
        if (!MapsActivity.checkPermission(this)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    10);
        }
    }

    public static boolean checkPermission(final Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public void config(int id) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)

                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 10);
            return;
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled !!!\nYou need to enable GPS to use the app")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        block = true;
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        if (alert == null) {
            alert = builder.create();
            alert.show();
        } else {
            if (!alert.isShowing()) {
                alert = builder.create();
                alert.show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        block = false;
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (!block) {
                    final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) || !manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                        buildAlertMessageNoGps();
                    }
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    /*Lấy địa chỉ cụ thể*/
    /*Chir dduwong*/
    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=false";
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        return url;
    }

    /**
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            // Connecting to url
            urlConnection.connect();
            // Reading data from url
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {
        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {
            // For storing data from web service
            String data = "";
            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            JSONObject jObject;

            List<List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                Directions parser = new Directions();
                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            String duration = "";
            MarkerOptions markerOptions = new MarkerOptions();
            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();
                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);
                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }
                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.BLUE);
            }
            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                polyline = map.addPolyline(lineOptions);
            }
        }
    }
}
