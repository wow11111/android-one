package com.example.parking.btomNaviActivity.ui.notifications;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.example.parking.btomNaviActivity.ui.dashboard.bookCar_list;
import com.example.parking.init.LoginActivity;
import com.example.parking.UserName;
import com.example.parking.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class NotificationsFragment extends Fragment implements MyOneLineView.OnRootClickListener {

    private ImageView blurImageView;
    private ImageView avatarImageView;
    private TextView  btn_myquit;
    private TextView user_name;
    MyOneLineView oneItem, twoItem, thereItem, fourItem, fiveItem, sixItem,sevenItem;
    LinearLayout llRoot;

    private NotificationsViewModel notificationsViewModel;

    private int money=1;

    private rechargeUtil util;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Glide.get(getContext()).clearMemory();
        init();
        initData(); //对头像等数据进行初始化




        getMoney();   //获取余额
        super.onActivityCreated(savedInstanceState);
    }

    private void init() {

        user_name=getActivity().findViewById(R.id.user_name);

        user_name.setText(UserName.name);

        //在xml布局中使用MyOneLineView
        oneItem = getActivity().findViewById(R.id.one_item);
        twoItem = getActivity().findViewById(R.id.two_item);
        thereItem = getActivity().findViewById(R.id.three_item);
        fourItem = getActivity().findViewById(R.id.four_item);
        fiveItem = getActivity().findViewById(R.id.five_item);
        sixItem = getActivity().findViewById(R.id.six_item);
        sevenItem = getActivity().findViewById(R.id.seven_item);

        oneItem.initMine(R.mipmap.myusername, "基础信息", "", true);
        twoItem.initMine(R.mipmap.mymsg, "我的消息", "", true);
        thereItem.initMine(R.mipmap.myrecharge, "余额",money+"元", true);
        fourItem.initMine(R.mipmap.mypsd, "修改密码", "", true);
        fiveItem.initMine(R.mipmap.mysdk, "版本", "", true);
        sixItem.initMine(R.mipmap.mycontact, "联系我们", "", true);
        sevenItem.initMine(R.mipmap.myset, "设置", "", true);

        thereItem.setOnRootClickListener(this,1);

    }


    private void initData() {

        blurImageView = getActivity().findViewById(R.id.iv_blur);
        avatarImageView = getActivity().findViewById(R.id.iv_avatar);

        btn_myquit=getActivity().findViewById(R.id.btn_myquit);

        Glide.with(getContext()).load(R.drawable.head)
                .skipMemoryCache(true)//跳过内存缓存

                .diskCacheStrategy(DiskCacheStrategy.NONE)//不要在disk硬盘缓存

                .bitmapTransform(new BlurTransformation(getContext(), 25), new CenterCrop(getContext()))

                .into(blurImageView);

        Glide.with(getContext()).load(R.drawable.head)

                .skipMemoryCache(true)//跳过内存缓存

                .diskCacheStrategy(DiskCacheStrategy.NONE)//不要在disk硬盘缓存

                .bitmapTransform(new CropCircleTransformation(getContext()))

                .into(avatarImageView);

        btn_myquit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onRootClick(View view) {
        switch ((int) view.getTag()) {
            case 1:
                startActivity(new Intent(getContext(),com.example.parking.reChargeActivity.class));
                break;
        }
    }

    //获取余额
    public void getMoney() {
        String url = "https://3a142762b7.eicp.vip/user/findUserBalances";
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println(s);
                        /*JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(s);
                            money= jsonObject.getInt("balances");
                            System.out.println("money"+money);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                        Gson gson = new Gson();
                        util = gson.fromJson(s,new TypeToken<rechargeUtil>() {
                        }.getType());

                        handler.sendEmptyMessage(0);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        System.out.println(volleyError);
                        System.out.println("获取余额失败");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("username",UserName.name);
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
                    System.out.println(111111);
                    money=util.getBalances();
                    thereItem.initMine(R.mipmap.myrecharge, "余额",money+"元", true);
                    break;
            }
        }
    };

}
