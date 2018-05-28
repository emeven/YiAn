package com.example.yian.test;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.yian.R;

import java.util.ArrayList;
import java.util.List;

public class LocationActivity extends AppCompatActivity {

    public LocationManager locationManager;
    private TextView positionText;
    private MapView mapView;
    private BaiduMap baiduMap;
    private boolean isFirstLocate = true;
    private List<LatLng> points = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplication());
        setContentView(R.layout.activity_location);
        positionText = (TextView) findViewById(R.id.position_txt_view);
        mapView = (MapView) findViewById(R.id.bmapview);
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        String serviceName = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) getSystemService(serviceName);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(false);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String provider = locationManager.getBestProvider(criteria, true);
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(LocationActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(LocationActivity.this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(LocationActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(LocationActivity.this, permissions, 1);
        } else {

            locationManager.requestLocationUpdates(provider, 1000, 10, locationListener);
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                String res = "纬度：" + location.getLatitude() + " 经度：" + location.getLongitude();
                positionText.setText(res);
                navigateTo(location.getLatitude(), location.getLongitude());
            }
        }
    }

    private Handler handler=new Handler();
    public Runnable runnable=new Runnable() {
        @Override
        public void run() {
            if(points.size()>1){
                drawMyRoute(points);
            }
            handler.postDelayed(runnable, 200);
        }
    };
    protected void drawMyRoute(List<LatLng> points) {

        OverlayOptions options = new PolylineOptions().color(0xAAFF0000)
                .width(10).points(points);
        baiduMap.addOverlay(options);
    }

    private void navigateTo(double lat,double lng){
        // if(isFirstLocate){
        LatLng ll=new LatLng(lat,lng);
        points.add(ll);
        MapStatusUpdate update= MapStatusUpdateFactory.newLatLng(ll);
        baiduMap.setMapStatus(update);
        update= MapStatusUpdateFactory.zoomTo(9f);
        baiduMap.setMapStatus(update);
        MyLocationData.Builder locationBuilder=new MyLocationData.Builder();
        locationBuilder.latitude(lat);
        locationBuilder.longitude(lng);
        MyLocationData locationData=locationBuilder.build();
        baiduMap.setMyLocationData(locationData);
        // isFirstLocate=false;
        //  }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0){
                    for(int result:grantResults){
                        if(result!=PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this,"permission denied.",Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                }else{
                    Toast.makeText(this,"Unknown errors.",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }
    private final LocationListener locationListener=new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            updateWithNewLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {
            updateWithNewLocation(null);
        }
    };
    private void updateWithNewLocation(Location location)  {
        if(location!=null){
            String res="纬度："+location.getLatitude()+" 经度："+location.getLongitude();
            positionText.setText(res);
            navigateTo(location.getLatitude(),location.getLongitude());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            locationManager.removeUpdates(locationListener);
        }catch (SecurityException e){
            e.printStackTrace();
        }
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        baiduMap.setMyLocationEnabled(false);
    }
}
