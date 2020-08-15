package com.example.parking.map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bunny.android.library.LoadDataLayout;
import com.example.parking.R;
import com.example.parking.UserName;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SimpleTimeZone;

public class TimeActivity extends AppCompatActivity {

    private Button btn_08, btn_10, btn_12, btn_14, btn_16, btn_18, btn_20,button;
    private int btnres[]={R.id.btn_time8,R.id.btn_time10,R.id.btn_time12,R.id.btn_time14,R.id.btn_time16,
            R.id.btn_time18,R.id.btn_time20};
    //private int flag20 = 0, flag8 = 0, flag10 = 0, flag12 = 0, flag14 = 0, flag16 = 0, flag18 = 0; //用来判断按钮是否被点击
    private TextView tv_amount; //预付金额
    private Button btn_yuding; //预定按钮
    private int s = 0; //预定的金额
    private int flags[] = {0, 0, 0, 0, 0, 0, 0};

    private String time_list[]={"06:00-08:00","08:00-10:00","10:00-12:00","12:00-14:00","14:00-16:00",
            "16:00-18:00","18:00-20:00"};

   // private LoadDataLayout loadDataLayout;

    private int carid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        initTime();
        initbtn(); //初始化按钮和监听事件
        amount();

        Intent intent=getIntent();
        carid=intent.getIntExtra("carid",0);

    }


    private void initbtn() {
        btn_20 = findViewById(R.id.btn_time20);
        btn_08 = findViewById(R.id.btn_time8);
        btn_10 = findViewById(R.id.btn_time10);
        btn_12 = findViewById(R.id.btn_time12);
        btn_14 = findViewById(R.id.btn_time14);
        btn_16 = findViewById(R.id.btn_time16);
        btn_18 = findViewById(R.id.btn_time18);

        btn_yuding = findViewById(R.id.btn_yuding);


        /*loadDataLayout =findViewById(R.id.book_ldl);

        loadDataLayout.showLoading("正在努力加载中", new LoadDataLayout.SetImgCallBack() {
            @Override
            public void setImg(ImageView img) {
                Glide.with(TimeActivity.this)
                        .load(R.mipmap.loading)
                        .into(img);
            }
        });*/
        //用系统时间来判断所有时间段的车位是否可以被购买，如果，超出时间段将不可点击
        if (nowHour >= 8) {
            btn_08.setEnabled(false);
        } else {
            btn_08.setEnabled(true);
        }

        if (nowHour >= 10) {
            btn_10.setEnabled(false);
        } else {
            btn_10.setEnabled(true);
        }

        if (nowHour >= 12) {
            btn_12.setEnabled(false);
        } else {
            btn_12.setEnabled(true);
        }

        if (nowHour >= 14) {
            btn_14.setEnabled(false);
        } else {
            btn_14.setEnabled(true);
        }

        if (nowHour >= 16) {
            btn_16.setEnabled(false);
        } else {
            btn_16.setEnabled(true);
        }

        if (nowHour >= 18) {
            btn_18.setEnabled(false);
        } else {
            btn_18.setEnabled(true);
        }

        if (nowHour >= 20) {
            btn_20.setEnabled(false);
        } else {
            btn_20.setEnabled(true);
        }


        btn_08.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timelist(0, v);
            }
        });
        btn_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timelist(1, v);

            }
        });
        btn_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timelist(2, v);
            }
        });
        btn_14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timelist(3, v);
            }
        });
        btn_16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timelist(4, v);
            }
        });
        btn_18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timelist(5, v);
            }
        });
        btn_20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timelist(6, v);
            }
        });


        //开始预定按钮的监听事件
        btn_yuding.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Boolean as = false, startyy = false;  //as用于判断时间段的连续
                int a = s / 5;   //a是被点击的数量和
                int b = 0;  //用来确定连续时间段的数量和
                if (a == 0) {
                    startyy = false;
                    Toast.makeText(TimeActivity.this, "你还未选择预定的时间段", Toast.LENGTH_SHORT);
                }
                if (a == 1) {
                    b = 1;
                }
                if (a > 1) {
                    //一个数组用于判断时间是否连续

                    for (int i = 0; i < flags.length; i++) {
                        if (i == 0) {
                            if (flags[0] == 1) {   //第一个时间段被选择之后
                                as = true;         //as=true，则连续时间段+1
                            }
                        } else {
                            if (flags[i] != flags[i - 1]) {  //如果第一个时间段不为0，则开始寻找第一个被点击的时间段flags[i]
                                as = (!as);                  //as起始值为false
                                if (flags[i] == 0) {         //当下一个时间段未被点击时，停止计数
                                    break;
                                }
                            }
                        }
                        if (as) {
                            b++;
                        }
                    }
                }
                if (b != a) {
                    startyy = false;
                }
                if (b == a && a!=0) {     //如果连续时间段总数等于点击的总数
                    startyy = true;
                }

                if (startyy) {
                    //创建一个提示对话框的构造者
                    AlertDialog.Builder builder = new AlertDialog.Builder(TimeActivity.this);
                    builder.setTitle("请选择支付方式"); //设置标题
                  //  builder.setMessage("您将需要支付" + s + "元"+","+"请选择支付方式");  //提示信息
                    final String[] items = {"微信", "支付宝", "其他支付方式"};
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (items[which]){
                                case "微信":
                                    System.out.println(11);
                                    bookPark();
                                    pay();
                                    break;
                                case "支付宝":
                                    System.out.println(22);
                                    bookPark();
                                    pay();
                                    break;
                                case "其他支付方式":
                                    System.out.println(33);
                                    bookPark();
                                    pay();
                                    break;
                            }
                        }
                    });
                    builder.show();
                    /*//builder.setIcon(R.mipmap.ic_launcher);  //设置图标


                    //正面的按钮
                    builder.setPositiveButton("去支付", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(TimeActivity.this, "即将进入支付页面", Toast.LENGTH_SHORT).show();
                        }
                    });
                    //反面的按钮
                    builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();*/
                }if (!startyy){
                    Toast.makeText(TimeActivity.this, "时间段不连续，请重新选择", Toast.LENGTH_SHORT).show();
                }
                if (a==0){
                    Toast.makeText(TimeActivity.this, "未选取时间段，请重新选择", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    private void amount() {
        tv_amount = findViewById(R.id.tv_amount);
        tv_amount.setText("预计金额：" + s + "元");
    }

    private Date date; //日历
    private int nowHour;

    private void initTime() {         //初始化时间，并判断时间段是否可购买
        date = new Date();
        nowHour = date.getHours();
    }


    //通过falg数组来判断多个按钮是否被点击, v.setActivated(true),点击之后改变颜色，直到再次点击恢复颜色
    private void timelist(int i, View v) {

        switch (flags[i]) {
            case 0:
                v.setActivated(true);
                flags[i] = 1;

                break;
            case 1:
                v.setActivated(false);
                flags[i] = 0;

                break;
        }
        s = (flags[0] + flags[1] + flags[2] + flags[3] + flags[4] + flags[5] + flags[6]) * 5;
        tv_amount.setText("预计支付金额：" + s + "元");
    }

    public void cancelMapClick(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Intent intent=new Intent(TimeActivity.this,MainActivity.class);
                    Thread.sleep(500);
                    startActivity(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //将用户选择的时间段传给服务器
    private String aa="";
    private void bookPark(){
        List<Integer> listInt=new ArrayList<>();
        for (int i=0;i<flags.length;i++){
            if (flags[i]==1){
                listInt.add(i);
            }
        }

        for (int i:listInt){
            aa=aa+time_list[i]+",";
    }
        System.out.println(aa);
        postCarList();
    }


    private int  payStatus=2;
    //付款
    public void pay() {
        String url = "https://3a142762b7.eicp.vip/user/UseBalances";
        RequestQueue queue = Volley.newRequestQueue(TimeActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println(s);
                       if ("200".equals(s)){
                           payStatus=1;
                       }else{
                           payStatus=0;
                       }
                        System.out.println(payStatus);
                        handler.sendEmptyMessage(0);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        System.out.println(volleyError);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("username", UserName.name);
                map.put("balances", s+"");
                return map;
            }
        };
        queue.add(request);
    }


    //生成付款订单
    public void postCarList() {
        String url = "https://3a142762b7.eicp.vip/carport/userReserve";
        RequestQueue queue = Volley.newRequestQueue(TimeActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        System.out.println(volleyError);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("username", "tom");
                map.put("consume", s+"");
                map.put("carId",carid+"");
                map.put("time_quantum",aa);
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
                    if (payStatus==1){
                       // loadDataLayout.showSuccess();
                        Toast.makeText(TimeActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    }else{
                      //  loadDataLayout.showSuccess();
                        Toast.makeText(TimeActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }

                    break;
            }
        }
    };
}
