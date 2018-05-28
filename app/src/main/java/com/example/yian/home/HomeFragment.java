package com.example.yian.home;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.yian.test.LocationActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * 文件描述
 *
 * @author Even.P
 * @since 2018/5/13 14:55
 */
public class HomeFragment extends Fragment {

    public LocationManager locationManager;
    private TextView positionText,weiZhiText;
    private MapView mapView;
    private BaiduMap baiduMap;
    private boolean isFirstLocate = true;
    private List<LatLng> points = new ArrayList<>();

    //显示心率和血氧
    private TextView heartRateTextView,spO2TextView;
    private HomeActivity homeActivity;
    private TextView ceshiyong;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //为SDK添加百度地图所需的so，jar文件
        SDKInitializer.initialize(getActivity().getApplication());
        View view=inflater.inflate(R.layout.fragment_home, container, false);

        homeActivity=(HomeActivity)getActivity();
        heartRateTextView=(TextView)view.findViewById(R.id.fragment_home_heartrate);
        spO2TextView=(TextView)view.findViewById(R.id.fragment_home_spo2);

        ceshiyong=(TextView)view.findViewById(R.id.ceshiyong);
        ceshiyong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] k=homeActivity.getData();
                heartRateTextView.setText(k[0]);
                spO2TextView.setText(k[1]);
                ceshiyong.setText(k[0]);
            }
        });
        FloatingActionButton fab=(FloatingActionButton)view.findViewById(R.id.fragment_home_fab) ;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] k=homeActivity.getData();
                heartRateTextView.setText(k[0]);
                spO2TextView.setText(k[1]);
                ceshiyong.setText(k[0]);
            }
        });
        //百度地图定位
        positionText = (TextView) view.findViewById(R.id.fragment_home_position_txt_view);
        mapView = (MapView) view.findViewById(R.id.fragment_home_bmapview);
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        String serviceName = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) getActivity().getSystemService(serviceName);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(false);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String provider = locationManager.getBestProvider(criteria, true);
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions((Activity) getContext(), permissions, 1);
        } else {

            locationManager.requestLocationUpdates(provider, 1000, 10, locationListener);
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                String res = "纬度：" + location.getLatitude() + " 经度：" + location.getLongitude();
                positionText.setText(res);
                navigateTo(location.getLatitude(), location.getLongitude());
            }
        }
        weiZhiText=(TextView)view.findViewById(R.id.fragment_home_weizhi);
        weiZhiText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentWeiZhi=new Intent(getContext(),LocationActivity.class);
                startActivity(intentWeiZhi);
            }
        });
        return view;

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
                            Toast.makeText(getContext(),"permission denied.",Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                            return;
                        }
                    }
                }else{
                    Toast.makeText(getContext(),"Unknown errors.",Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
                break;
        }
    }

    //监听定位信息
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
    //经纬度定位具体位置
    private void updateWithNewLocation(Location location)  {
        if(location!=null){
            String res="纬度："+location.getLatitude()+" 经度："+location.getLongitude();
            positionText.setText(res);
            navigateTo(location.getLatitude(),location.getLongitude());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            locationManager.removeUpdates(locationListener);
        }catch (SecurityException e){
            e.printStackTrace();
        }
        mapView.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        baiduMap.setMyLocationEnabled(false);
    }

}
