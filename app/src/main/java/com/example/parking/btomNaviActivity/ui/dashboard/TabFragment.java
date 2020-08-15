package com.example.parking.btomNaviActivity.ui.dashboard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bunny.android.library.LoadDataLayout;
import com.example.parking.UserName;
import com.example.parking.init.LoginActivity;
import com.example.parking.R;
import com.example.parking.btomNaviActivity.MainAllActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TabFragment extends Fragment {
    private TextView titleTv;
    private LoadDataLayout loadDataLayout;
    private String mTitle;
    private int titleCode;
    private ListView listView;

    private ListView listView_CarList;
    private View view;
    private ArrayList<reCharge_list> mData;
    private MyAdapter<reCharge_list> myAdapter;
    private ArrayList<reCharge_list> mData2 = new ArrayList<>();

    private ArrayList<bookCar_list> cData;
    private MyAdapter<bookCar_list> carAdapter;


    private ImageView imageView;

    //这个构造方法是便于各导航同时调用一个fragment
    public TabFragment(String title) {
        mTitle = title;
        if (title.equals("车位订单")) {
            titleCode = 1;
        }
        if (title.equals("充值订单")) {
            titleCode = 2;
        }
        if (title.equals("消费订单")) {
            titleCode = 3;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_tab, container, false);
        listView = view.findViewById(R.id.tabFragment_lists);
        loadDataLayout = view.findViewById(R.id.park_ldl);


        loadDataLayout.showLoading("正在努力加载中", new LoadDataLayout.SetImgCallBack() {
            @Override
            public void setImg(ImageView img) {
                Glide.with(getActivity())
                        .load(R.mipmap.loading)
                        .into(img);
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    if (titleCode == 1) {
                        getCarList();
                    }
                    if (titleCode == 2) {
                        getStatus();
                    }
                    if (titleCode == 3) {
                        getStatus();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return view;
    }


    //获取车位订单lists
    private void getCarList() {
        //String url2="http://192.168.31.222:8080/01/123.html";
        String url = "https://3a142762b7.eicp.vip/carport/findUserReserve";
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println(s);
                        Gson gson = new Gson();
                        cData = gson.fromJson(s, new TypeToken<List<bookCar_list>>() {
                        }.getType());
                        handler.sendEmptyMessage(1);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        System.out.println(volleyError);
                        Toast.makeText(getContext(), "网络错误，请联系服务器", Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("username", UserName.name);
                return map;
            }
        };
        queue.add(request);
    }


    //获取充值订单lists
    public void getStatus() {
        String url = "https://3a142762b7.eicp.vip/user/findChargeUser";
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println(s);
                        Gson gson = new Gson();
                        System.out.println(1333);

                        mData = gson.fromJson(s, new TypeToken<List<reCharge_list>>() {
                        }.getType());
                        System.out.println(14444);
                        int a = mData.size();

                        for (int i = a - 1; i > 0; i--) {

                            int b = Integer.parseInt(mData.get(i).getCharge());
                            if (titleCode == 2 && b > 0) {
                                reCharge_list reCharge_list = mData.get(i);
                                //   System.out.println(reCharge_list.toString());
                                mData2.add(reCharge_list);
                            }
                            if (titleCode == 3 && b < 0) {
                                reCharge_list list = mData.get(i);
                                System.out.println(list.toString());
                                mData2.add(list);
                            }
                        }

                        handler.sendEmptyMessage(0);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        System.out.println(volleyError);
                        Toast.makeText(getContext(), "网络错误，请联系服务器", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("username", UserName.name);

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
                    loadDataLayout.showSuccess();
                    myAdapter = new MyAdapter<reCharge_list>(mData2, R.layout.tabfragment_lists) {
                        @Override
                        public void bindView(ViewHolder holder, reCharge_list obj) {
                            holder.setText(R.id.tv_recharge_list_code, "订单号：" + obj.getChargeId());
                            holder.setText(R.id.tv_recharge_list_timecode, "时间：" + obj.getChargeTime());
                            if (titleCode == 2) {
                                holder.setText(R.id.tv_recharge_list_money, ""+ obj.getCharge() + "￥");
                            }
                            if (titleCode == 3) {
                                holder.setText(R.id.tv_recharge_list_money, "" + obj.getCharge() + "￥");
                            }
                        }

                    };
                    listView.setAdapter(myAdapter);
                    break;
                case 1:
                    loadDataLayout.showSuccess();
                    carAdapter = new MyAdapter<bookCar_list>(cData, R.layout.tabfragment_carlists) {
                        @Override
                        public void bindView(ViewHolder holder, bookCar_list obj) {
                            holder.setText(R.id.tv_car_list_code, "订单号：" + obj.getOrderId());
                            holder.setText(R.id.tv_carnumber, "车位号：" + obj.getCarId());
                            holder.setText(R.id.tv_booktime, "预定时间：" + obj.getTime_quantum());
                            holder.setText(R.id.tv_car_timecode, obj.getOrderTime());
                            holder.setText(R.id.tv_recharge_list_money, "-" + obj.getConsume() + "￥");
                        }

                    };
                    listView.setAdapter(carAdapter);
                    break;
            }
        }
    };


}