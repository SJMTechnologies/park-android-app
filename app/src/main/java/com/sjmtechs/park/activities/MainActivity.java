package com.sjmtechs.park.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.FadeExit.FadeExit;
import com.flyco.animation.FlipEnter.FlipVerticalSwingEnter;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.sjmtechs.park.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

@SuppressWarnings("deprecation")
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static final int PERMISSION_REQUEST_CODE = 0x1;
    private static final int LOCATION_REQUEST = 0x2;

    @InjectView(R.id.btnPayNow)
    Button btnPayNow;

    @InjectView(R.id.btnPayLater)
    Button btnPayLater;

    @InjectView(R.id.txtUserName)
    TextView txtUserName;

    BaseAnimatorSet bas_in;
    BaseAnimatorSet bas_out;

    Toolbar toolbar;

    private String username = "";
//    boolean isLoggedIn = false;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main2);
        ButterKnife.inject(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        pref = getSharedPreferences(Constant.PREF_NAME,0);
//        String username = pref.getString(Constant.USER_NAME,"");

        try{
            Intent intent = getIntent();
            username = intent.getExtras().getString("name");
        } catch (Exception e){
            e.printStackTrace();
        }
        txtUserName.setText(username);
        bas_in = new FlipVerticalSwingEnter();
        bas_out = new FadeExit();
        btnPayNow = (Button) findViewById(R.id.btnPayNow);
        btnPayLater = (Button) findViewById(R.id.btnPayLater);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showLocationTurnOnDialog();
        }
        if (Build.VERSION.SDK_INT >= 23) {
            int fineLocationPermission = ContextCompat.checkSelfPermission(getApplicationContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION);
            int coarseLocationPermission = ContextCompat.checkSelfPermission(getApplicationContext(),
                    android.Manifest.permission.ACCESS_COARSE_LOCATION);
            if (fineLocationPermission == PackageManager.PERMISSION_GRANTED
                    && coarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
                loadData();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
            }
        } else {
            loadData();
        }
    }

    //load data
    private void loadData() {
        btnPayNow.setOnClickListener(this);
        btnPayLater.setOnClickListener(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        MenuItem loginItem = navigationView.getMenu().getItem(1);
//        isLoggedIn = pref.getBoolean(Constant.USER_LOGIN,false);
//        if(isLoggedIn){
            loginItem.setTitle("Login");
//        } else {
//            loginItem.setTitle("Login");
//        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_register) {
            navigationView.setCheckedItem(NavigationView.NO_ID);
            startActivity(RegisterActivity.class);
        } else if (id == R.id.nav_login) {
//            if(isLoggedIn){
//                pref.edit().putBoolean(Constant.USER_LOGIN,false).apply();
//                MainActivity.this.finish();
//            } else {
                startActivity(LoginActivity.class);
//            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPayNow:
                break;
            case R.id.btnPayLater:
                startActivity(PlacesActivity.class);
                break;
        }
    }
    private void startActivity(Class cls) {
        startActivity(new Intent(MainActivity.this, cls));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                            && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                        loadData();
                    }
                } else {
                    MainActivity.this.finish();
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * This dialog will open if User Location is Off.
     * */
    private void showLocationTurnOnDialog() {

        final MaterialDialog dialog = new MaterialDialog(MainActivity.this);
        OnBtnClickL onBtnClickL1 = new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                dialog.dismiss();
            }
        };

        OnBtnClickL onBtnClickL2 = new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                dialog.superDismiss();
                Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                MainActivity.this.startActivityForResult(intent, LOCATION_REQUEST);
            }
        };

        OnBtnClickL[] clickLs = new OnBtnClickL[]{onBtnClickL1, onBtnClickL2};
        dialog.setOnBtnClickL(clickLs);
        dialog.isTitleShow(false)
                .content("Please turn on location service.")
                .btnText("Cancel", "Go To Setting")
                .showAnim(bas_in)
                .dismissAnim(bas_out)
                .show();
    }
}
