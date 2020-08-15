package com.example.parking.map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.parking.btomNaviActivity.ui.dashboard.MyAdapter;
import com.example.parking.btomNaviActivity.ui.dashboard.reCharge_list;
import com.example.parking.map.MarkerInfoUtil;
import com.example.parking.Navi.DemoMainActivity;
import com.example.parking.Navi.Gps;
import com.example.parking.Navi.PositionUtil;
import com.example.parking.R;
import com.example.parking.map.TimeActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private MapView mapView;
    private BaiduMap baiduMap;
    private LocationClient mLocationClient = null;
    private MainActivity.MyLocationListener myLocationListener;
    private double currentLat, currentLng;//纬度 经度
    private boolean flag;

    private Marker marker;
    //private BaiduMap.OnMarkerClickListener markerClickListener;

    //覆盖物显示的布局
    private View rl_marker;

    //创建自己的箭头定位
    private BitmapDescriptor bitmapDescriptor;

    private ArrayList<initUtil> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapView = findViewById(R.id.bmapView);
        rl_marker=getLayoutInflater().inflate(R.layout.marker,null,false);



        initBaiduMap();//初始化地图
        mylocation();//显示我的位置+

       //setMarkerInfo();
    }

    //初始化地图
    private void initBaiduMap() {
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);

        mLocationClient = new LocationClient(getApplicationContext());//声明LocationClient
        myLocationListener = new MainActivity.MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);//注册监听函数

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
        option.setCoorType("gcj02");//返回的定义结果是百度经纬度,默认是gcj02
        option.setScanSpan(10000);//设置发起定位请求间隔为10000ms
        option.setIsNeedAddress(true);//返回结果包含地址信息
        option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
        mLocationClient.setLocOption(option);

        mLocationClient.start(); //开启地图定位图层
        mLocationClient.requestLocation();//发送请求


    }

     /*public void marketshow(){
         //定义Maker坐标点
         LatLng latLng1 = new LatLng(29.825500, 121.570500);//经纬度
         //构建图片描述器
         BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.park);
         //创建一个图层选项
         OverlayOptions option2 = new MarkerOptions().position(latLng1).icon(bitmapDescriptor);

         //把图层选项添加到地图
         baiduMap.addOverlay(option2);
     }*/




    //显示我的位置
    private void mylocation() {
        Toast.makeText(MainActivity.this, "正在定位...", Toast.LENGTH_LONG).show();
        flag = true;
        baiduMap.clear();
        baiduMap.setMyLocationEnabled(true);
        mLocationClient.requestLocation();//发送定位请求

    }


    //位置初始化和监听
    class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation != null & flag) {
                flag = false;
                currentLat = bdLocation.getLatitude();
                currentLng = bdLocation.getLongitude();
                MyLocationData.Builder builder = new MyLocationData.Builder();
                builder.latitude(bdLocation.getLatitude());//设置纬度
                builder.longitude(bdLocation.getLongitude());//设置经度
                builder.accuracy(bdLocation.getRadius());//设置精度(半径)
                builder.direction(bdLocation.getDirection());//设置方向
                builder.speed(bdLocation.getSpeed());//设置速度
                MyLocationData locationData = builder.build();
                baiduMap.setMyLocationData(locationData);
                LatLng latLng = new LatLng(currentLat, currentLng);
                baiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                        MyLocationConfiguration.LocationMode.FOLLOWING, true, null));
                //设置我的位置为地图的中心点
                baiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(latLng, 16));

            }
            if (bdLocation != null & !flag) {
                currentLat = bdLocation.getLatitude();
                currentLng = bdLocation.getLongitude();

                System.out.println("纬度：" + currentLat + "经度" + currentLng);
            }

        }
    }


    //添加自定义的showmarker信息
    private ArrayList<initUtil> infos;
    /*private void setMarkerInfo() {
        infos = new ArrayList<initUtil>();
        infos.add(new initUtil(1,"121.570500","29.82500",R.mipmap.park,"1号"));
        infos.add(new initUtil(2,"121.570500","29.82500",R.mipmap.park,"2号"));
        infos.add(new initUtil(3,"121.570500","29.82500",R.mipmap.park,"3号"));
    }*/

    //showmarker的显示
    private Boolean showMarker=false;
    public void showMakerClick(View view){
        if(!showMarker){
            //显示marker
            getCarInitData();
            view.setActivated(true);
            Toast.makeText(this,"显示实时车位",Toast.LENGTH_SHORT).show();
            showMarker = true;
        }else{
            //关闭显示marker
            baiduMap.clear();
            view.setActivated(false);  //单击之后按钮变色
            Toast.makeText(this,"关闭实时车位显示",Toast.LENGTH_SHORT).show();
            showMarker = false;
        }

    }

    //显示marker
    private void addOverlay(ArrayList<initUtil> mData) {
        //清空地图
        baiduMap.clear();
        //创建marker的显示图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.park);
        LatLng latLng = null;
        Marker marker;
        OverlayOptions options;
        for(initUtil info:mData){
            //获取经纬度
            latLng = new LatLng(Double.parseDouble(info.getLatitude()),Double.parseDouble(info.getLongitude()));
            //设置marker
            options = new MarkerOptions()
                    .position(latLng)//设置位置
                    .icon(bitmap);//设置图标样式
            //添加marker
            marker = (Marker) baiduMap.addOverlay(options);
            //使用marker携带info信息，当点击事件的时候可以通过marker获得info信息
            Bundle bundle = new Bundle();
            //info必须实现序列化接口
            bundle.putSerializable("info", (Serializable) info);
            marker.setExtraInfo(bundle); }


        //添加marker点击事件的监听
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //从marker中获取info信息

                Bundle bundle = marker.getExtraInfo();
                final initUtil infoUtil = (initUtil) bundle.getSerializable("info");
                //将信息显示在界面上
//                ImageView iv_img = (ImageView)rl_marker.findViewById(R.id.iv_img);
//                iv_img.setBackgroundResource(infoUtil.getImgId());
                TextView tv_name = (TextView)rl_marker.findViewById(R.id.tv_name);
                tv_name.setText(infoUtil.getId()+"号车位");

                TextView tv_description = (TextView)rl_marker.findViewById(R.id.tv_description);
                tv_description.setText(infoUtil.getCarName());

                //覆盖物中导航按钮的点击事件
                Button btn_navi=rl_marker.findViewById(R.id.btn_navigation);
                btn_navi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("onClick");
                        startNavi(Double.parseDouble(infoUtil.getLongitude()),Double.parseDouble(infoUtil.getLatitude()));
                    }
                });


                //覆盖物中详情进入预约界面
                Button btn_detail=rl_marker.findViewById(R.id.btn_detail);
                btn_detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(MainActivity.this, TimeActivity.class);
                        intent.putExtra("carid",infoUtil.getId());
                        startActivity(intent);
                    }
                });

                //覆盖物中返回的imageButton的点击事件
                ImageButton imgBtn_cancel=rl_marker.findViewById(R.id.imageBtn_markercancel);
                imgBtn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //隐藏infowindow
                        baiduMap.hideInfoWindow();
                    }
                });

                //将布局显示出来
                rl_marker.setVisibility(View.VISIBLE);




                //infowindow中的布局
//                TextView tv = new TextView(MainActivity.this);
//                tv.setBackgroundResource(R.mipmap.blank);
//               tv.setPadding(25, 10, 25, 10);
//                tv.setTextColor(Color.DKGRAY);
//                tv.setText(infoUtil.getName());
//                tv.setGravity(Gravity.CENTER);
                bitmapDescriptor = BitmapDescriptorFactory.fromView(rl_marker);
                //infowindow位置
                LatLng latLng = new LatLng(Double.parseDouble(infoUtil.getLatitude()), Double.parseDouble(infoUtil.getLongitude()));
                //infowindow点击事件
                InfoWindow.OnInfoWindowClickListener listener = new InfoWindow.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick() {
                        //隐藏infowindow
                      baiduMap.hideInfoWindow();
                    }
                };
                //显示infowindow
                InfoWindow infoWindow=new InfoWindow(rl_marker,latLng,-100);
//                InfoWindow infoWindow = new InfoWindow(bitmapDescriptor, latLng, -47, listener);
                baiduMap.showInfoWindow(infoWindow);
                return true;
            }
        });


    }

    public void startNavi(double endLongitude,double endLatitude){
        Gps endGps= PositionUtil.gcj_To_Gps84(endLatitude,endLongitude);
        Gps startGps=PositionUtil.gcj_To_Gps84(currentLat,currentLng);
        Intent intent=new Intent(MainActivity.this, DemoMainActivity.class);
        intent.putExtra("endLongitude", endGps.getWgLon());
        intent.putExtra("endLatitude", endGps.getWgLat());
        intent.putExtra("startLatitude",startGps.getWgLat());
        intent.putExtra("startLongitude",startGps.getWgLon());
        startActivity(intent);

    }

    public void cancelHomeClick(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Intent intent=new Intent(MainActivity.this,com.example.parking.btomNaviActivity.MainAllActivity.class);
                    Thread.sleep(500);
                    startActivity(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    //获取服务器车位的基础信息
    public void getCarInitData() {
        String url = "https://3a142762b7.eicp.vip/carport/findAllPark";
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Gson gson = new Gson();
                        mData = gson.fromJson(s, new TypeToken<List<initUtil>>() {
                        }.getType());
                        System.out.println(s);
                        handler.sendEmptyMessage(0);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        System.out.println(volleyError);
                       /*if ("".equals(volleyError)){
                        handler.sendEmptyMessage(1);}*/
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();

                return map;
            }
        };
        queue.add(request);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    addOverlay(mData);

                    break;
                case 1:
                    Toast.makeText(MainActivity.this, "获取车位失败", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
