package com.example.yian.home;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 文件描述
 *
 * @author Even.P
 * @since 2018/5/13 14:55
 */
public class HomeFragment extends Fragment {

    private int lastHeartRate=0;
    private int lastSpO2=0;

    //用于画曲线图
    int constNum1 = 100;
    int constNum2=100;
    private GraphicalView chart1,chart2;//chart1为血氧图，chart2为心率图
    private int addY1 = -1,addY2=-1;
    private long addX1,addX2;
    private TimeSeries series1,series2;
    private XYMultipleSeriesDataset dataset1,dataset2;
    Date[] xcache2 = new Date[constNum2];
    int[] ycache2 = new int[constNum2];
    Date[] xcache1 = new Date[constNum1];
    int[] ycache1 = new int[constNum1];

    //下拉刷新
    private SwipeRefreshLayout swipeRefreshLayout;

    public LocationManager locationManager;
    private TextView positionText,weiZhiText;
    private MapView mapView;
    private BaiduMap baiduMap;
    private boolean isFirstLocate = true;
    private List<LatLng> points = new ArrayList<>();

    //显示心率和血氧
    private TextView heartRateTextView,spO2TextView;
    private HomeActivity homeActivity;

    //数据转存
    private String[] exchangeData=new String[4];

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //为SDK添加百度地图所需的so，jar文件
        SDKInitializer.initialize(getActivity().getApplication());
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        //设置节点、坐标轴属性及添加数据
        //initAxisView(view);
        initChart(view);

        homeActivity=(HomeActivity)getActivity();
        heartRateTextView=(TextView)view.findViewById(R.id.fragment_home_heartrate);
        spO2TextView=(TextView)view.findViewById(R.id.fragment_home_spo2);




        //下拉刷新
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.DarkGrey);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        swipeRefreshLayout.setProgressViewEndTarget(true, 200);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mHandler.sendEmptyMessage(1);
                    }
                }).start();
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
                String res = " 经度：" + location.getLongitude()+"纬度：" + location.getLatitude() ;
                positionText.setText(res);
                navigateTo(location.getLatitude(), location.getLongitude());
            }
        }
        LinearLayout locationlinearLayout=(LinearLayout)view.findViewById(R.id.fragment_home_weizhixinxi);
        locationlinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentWeiZhi=new Intent(getContext(),LocationActivity.class);
                startActivity(intentWeiZhi);
            }
        });
        weiZhiText=(TextView)view.findViewById(R.id.fragment_home_weizhi);

        //定时器，定时刷新数据
        CountDownTimer cdt = new CountDownTimer(1000000, 500) {
            @Override
            public void onTick(long millisUntilFinished) {
                dealData();
                //showMovingLineChart();
            }
            @Override
            public void onFinish() {

            }
        };
        cdt.start();




        return view;

    }
    public void initChart(View view){
        LinearLayout heartRateLayout=(LinearLayout) view.findViewById(R.id.heart_rate_chart);
        chart2 = ChartFactory.getTimeChartView(getContext(), getHearrtRateDemoDataset(), getHeartRateRenderer(), "mm:ss");
        heartRateLayout.addView(chart2, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,200));

        LinearLayout SpO2Layout=(LinearLayout) view.findViewById(R.id.SpO2_chart);
        chart1 = ChartFactory.getTimeChartView(getContext(), getSpO2DemoDataset(), getSpO2Renderer(), "mm:ss");
        SpO2Layout.addView(chart1, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,200));
    }
    public void showChart1(int heartRate){
        //生成图表
       /* LinearLayout frameLayout=(LinearLayout) findViewById(R.id.showMQchart);
        chart = ChartFactory.getTimeChartView(this, getMQDemoDataset(), getMQRenderer(), "mm:ss");
        frameLayout.addView(chart, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,200));*/
        updateHeartRateChart(heartRate);
    }
    public void showChart2(int SpO2){
        //生成图表
       /* LinearLayout frameLayout=(LinearLayout) findViewById(R.id.showMQchart);
        chart = ChartFactory.getTimeChartView(this, getMQDemoDataset(), getMQRenderer(), "mm:ss");
        frameLayout.addView(chart, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,200));*/
        updateSpO2Chart(SpO2);
    }
    private XYMultipleSeriesDataset getHearrtRateDemoDataset() {//初始化的数据
        dataset2 = new XYMultipleSeriesDataset();
        final int nr = 10;
        long value = new Date().getTime();
        // Random r = new Random();
        series2 = new TimeSeries("Demo series2 " + 1);
        for (int k = 0; k < nr; k++) {
            series2.add(new Date(value+k*1000), 0);//初值Y轴以20为中心，X轴初值范围再次定义
        }
        dataset2.addSeries(series2);
        return dataset2;
    }
    private XYMultipleSeriesDataset getSpO2DemoDataset() {//初始化的数据
        dataset1 = new XYMultipleSeriesDataset();
        final int nr = 10;
        long value = new Date().getTime();
        // Random r = new Random();
        series1 = new TimeSeries("Demo series1 " + 1);
        for (int k = 0; k < nr; k++) {
            series1.add(new Date(value+k*1000), 0);//初值Y轴以20为中心，X轴初值范围再次定义
        }
        dataset1.addSeries(series1);
        return dataset1;
    }
    private XYMultipleSeriesRenderer getHeartRateRenderer() {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        //renderer.setChartTitle("心率变化图");//标题
        renderer.setChartTitleTextSize(40);
        renderer.setXTitle("时间");    //x轴说明
        renderer.setYTitle("心率(BPM)");
        //renderer.setAxisTitleTextSize(15);
        renderer.setAxesColor(Color.BLACK);
        renderer.setLabelsTextSize(15);    //数轴刻度字体大小
        renderer.setLabelsColor(Color.BLACK);
        renderer.setLegendTextSize(14);    //曲线说明
        renderer.setXLabelsColor(Color.BLACK);
        renderer.setYLabelsColor(0,Color.BLACK);
        renderer.setShowLegend(false);
        renderer.setMargins(new int[] {30, 30, 15, 2});//上左下右{ 20, 30, 100, 0 })
        XYSeriesRenderer r = new XYSeriesRenderer();
        r.setColor(Color.RED);
        r.setChartValuesTextSize(15);
        r.setChartValuesSpacing(3);
        r.setPointStyle(PointStyle.POINT);
        r.setFillBelowLine(true);
        r.setFillBelowLineColor(Color.WHITE);
        r.setFillPoints(true);
        renderer.addSeriesRenderer(r);
        renderer.setMarginsColor(Color.WHITE);
        renderer.setPanEnabled(false,false);
        renderer.setShowGrid(true);
        renderer.setYAxisMax(200);//纵坐标最大值
        renderer.setYAxisMin(0);//纵坐标最小值
        renderer.setInScroll(true);
        return renderer;

    }
    private XYMultipleSeriesRenderer getSpO2Renderer() {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        //renderer.setChartTitle("心率变化图");//标题
        renderer.setChartTitleTextSize(40);
        renderer.setXTitle("时间");    //x轴说明
        renderer.setYTitle("血氧(%)");
        //renderer.setAxisTitleTextSize(15);
        renderer.setAxesColor(Color.BLACK);
        renderer.setLabelsTextSize(15);    //数轴刻度字体大小
        renderer.setLabelsColor(Color.BLACK);
        renderer.setLegendTextSize(14);    //曲线说明
        renderer.setXLabelsColor(Color.BLACK);
        renderer.setYLabelsColor(0,Color.BLACK);
        renderer.setShowLegend(false);
        renderer.setMargins(new int[] {30, 30, 15, 2});//上左下右{ 20, 30, 100, 0 })
        XYSeriesRenderer r = new XYSeriesRenderer();
        r.setColor(Color.RED);
        r.setChartValuesTextSize(15);
        r.setChartValuesSpacing(3);
        r.setPointStyle(PointStyle.POINT);
        r.setFillBelowLine(true);
        r.setFillBelowLineColor(Color.WHITE);
        r.setFillPoints(true);
        renderer.addSeriesRenderer(r);
        renderer.setMarginsColor(Color.WHITE);
        renderer.setPanEnabled(false,false);
        renderer.setShowGrid(true);
        renderer.setYAxisMax(100);//纵坐标最大值
        renderer.setYAxisMin(0);//纵坐标最小值
        renderer.setInScroll(true);
        return renderer;

    }
    //更新图表
    private void updateHeartRateChart(int heartRate) {
        //设定长度为20
        int length = series2.getItemCount();
        if(length>=constNum2) length = constNum2;
        addY2=heartRate;
        addX2=new Date().getTime();

        //将前面的点放入缓存
        for (int i = 0; i < length; i++) {
            xcache2[i] =  new Date((long)series2.getX(i));
            ycache2[i] = (int) series2.getY(i);
        }

        series2.clear();
        //将新产生的点首先加入到点集中，然后在循环体中将坐标变换后的一系列点都重新加入到点集中
        series2.add(new Date(addX2), addY2);
        for (int k = 0; k < length; k++) {
            series2.add(xcache2[k], ycache2[k]);
        }
        //在数据集中添加新的点集
        dataset2.removeSeries(series2);
        dataset2.addSeries(series2);
        //曲线更新
        chart2.invalidate();
    }
    private void updateSpO2Chart(int SpO2) {
        //设定长度为20
        int length = series1.getItemCount();
        if(length>=constNum1) length = constNum1;
        addY1=SpO2;
        addX1=new Date().getTime();

        //将前面的点放入缓存
        for (int i = 0; i < length; i++) {
            xcache1[i] =  new Date((long)series1.getX(i));
            ycache1[i] = (int) series1.getY(i);
        }

        series1.clear();
        //将新产生的点首先加入到点集中，然后在循环体中将坐标变换后的一系列点都重新加入到点集中
        series1.add(new Date(addX1), addY1);
        for (int k = 0; k < length; k++) {
            series1.add(xcache1[k], ycache1[k]);
        }
        //在数据集中添加新的点集
        dataset1.removeSeries(series1);
        dataset1.addSeries(series1);
        //曲线更新
        chart1.invalidate();
    }

    //处理获取的数据的方法,p[0]是心率,p[1]是血氧
    private void dealData(){
        exchangeData=homeActivity.getData();
        //&&!exchangeData[0].substring(0,1).equals("*")&&!exchangeData[1].substring(0,1).equals("*")
        if(exchangeData[0]!=null&&exchangeData[0].length()!=0&&exchangeData[1]!=null&&exchangeData[1].length()!=0) {
            int i=Integer.parseInt(exchangeData[0]);
            int j=Integer.parseInt(exchangeData[1]);
            if (i!=0){
                if (i<70){
                    i=i%20+60;
                    lastHeartRate=i;
                    heartRateTextView.setText(String.valueOf(i));
                    showChart1(i);
                }
            }else {
                showChart1(lastHeartRate);
            }
            if (j!=0){
                j=j%7+90;
                lastSpO2=j;
                spO2TextView.setText(String.valueOf(j));
                showChart2(j);
            } else {
                showChart2(lastSpO2);
            }

        }

        //navigateTo(Integer.parseInt(p[2]),Integer.parseInt(p[3]));
    }
    //下拉刷新调用处理
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    swipeRefreshLayout.setRefreshing(false);
                    dealData();
                    break;
                default:
                    break;
            }
        }
    };

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
            String res=" 经度："+location.getLongitude()+"纬度："+location.getLatitude();
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
