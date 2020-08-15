package com.example.parking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.parking.btomNaviActivity.ui.notifications.rechargeUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class reChargeActivity extends AppCompatActivity {
    private ImageView img_rechargequit;
    private Button btn_buy1, btn_buy2, btn_buy3, btn_buy4, btn_buy5, btn_buy6, btn_buy7;
    private Button btn_reCharge; //充值按钮

    private int flags[] = {0, 0, 0, 0, 0, 0, 0};//用来判断按钮点击之后变色恢复问题
    private String  reChargeMoney;   //充值的数额

    private EditText et_recharge;

    private TextView tv_username;
    private TextView tv_RestMoney;
    private int money;

    private int a;

    private rechargeUtil util;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_charge);


        getMoney();
        init();
        et_recharge.clearFocus();
    }

    private void init() {
        tv_username=findViewById(R.id.tv_username);
        tv_username.setText("  用户名： "+UserName.name);

        img_rechargequit = findViewById(R.id.img_rechargequit);
        btn_buy1 = findViewById(R.id.btn_buy1);
        btn_buy2 = findViewById(R.id.btn_buy2);
        btn_buy3 = findViewById(R.id.btn_buy3);
        btn_buy4 = findViewById(R.id.btn_buy4);
        btn_buy5 = findViewById(R.id.btn_buy5);
        btn_buy6 = findViewById(R.id.btn_buy6);
        btn_buy7 = findViewById(R.id.btn_buy7);

        btn_reCharge = findViewById(R.id.btn_reCharge);
        et_recharge = findViewById(R.id.et_recharge);

        et_recharge.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                btn_buy1.setActivated(false);
                btn_buy2.setActivated(false);
                btn_buy3.setActivated(false);
                btn_buy4.setActivated(false);
                btn_buy5.setActivated(false);
                btn_buy6.setActivated(false);
                btn_buy7.setActivated(false);
                flags[0] = 0;
                flags[1] = 0;
                flags[2] = 0;
                flags[3] = 0;
                flags[4] = 0;
                flags[5] = 0;
                flags[6] = 0;
            }
        });




        img_rechargequit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 充值选择的金额按钮的点击事件
        btn_buy1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_recharge.clearFocus();
                timelist(0, v);
                btn_buy2.setActivated(false);
                btn_buy3.setActivated(false);
                btn_buy4.setActivated(false);
                btn_buy5.setActivated(false);
                btn_buy6.setActivated(false);
                btn_buy7.setActivated(false);
                flags[1] = 0;
                flags[2] = 0;
                flags[3] = 0;
                flags[4] = 0;
                flags[5] = 0;
                flags[6] = 0;
                if (flags[0]==1) {
                    reChargeMoney =flags[0] * 20 + flags[1] * 50 + flags[2] * 100 + flags[3] * 200 + flags[4] * 498 + flags[5] * 598 + flags[6] * 888+"" ;
                }
            }
        });
        btn_buy2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_recharge.clearFocus();
                timelist(1, v);
                btn_buy1.setActivated(false);
                btn_buy3.setActivated(false);
                btn_buy4.setActivated(false);
                btn_buy5.setActivated(false);
                btn_buy6.setActivated(false);
                btn_buy7.setActivated(false);
                flags[0] = 0;
                flags[2] = 0;
                flags[3] = 0;
                flags[4] = 0;
                flags[5] = 0;
                flags[6] = 0;
                if (flags[1]==1) {
                    reChargeMoney = flags[0] * 20 + flags[1] * 50 + flags[2] * 100 + flags[3] * 200 + flags[4] * 498 + flags[5] * 598 + flags[6] * 888 +"";
                }
            }
        });
        btn_buy3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_recharge.clearFocus();
                timelist(2, v);
                btn_buy1.setActivated(false);
                btn_buy2.setActivated(false);
                btn_buy4.setActivated(false);
                btn_buy5.setActivated(false);
                btn_buy6.setActivated(false);
                btn_buy7.setActivated(false);
                flags[1] = 0;
                flags[0] = 0;
                flags[3] = 0;
                flags[4] = 0;
                flags[5] = 0;
                flags[6] = 0;
                if (flags[2]==1) {
                    reChargeMoney = flags[0] * 20 + flags[1] * 50 + flags[2] * 100 + flags[3] * 200 + flags[4] * 498 + flags[5] * 598 + flags[6] * 888 +"";
                }
            }
        });
        btn_buy4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_recharge.clearFocus();
                timelist(3, v);
                btn_buy2.setActivated(false);
                btn_buy3.setActivated(false);
                btn_buy1.setActivated(false);
                btn_buy5.setActivated(false);
                btn_buy6.setActivated(false);
                btn_buy7.setActivated(false);
                flags[1] = 0;
                flags[2] = 0;
                flags[0] = 0;
                flags[4] = 0;
                flags[5] = 0;
                flags[6] = 0;
                if (flags[3]==1) {
                    reChargeMoney = flags[0] * 20 + flags[1] * 50 + flags[2] * 100 + flags[3] * 200 + flags[4] * 498 + flags[5] * 598 + flags[6] * 888 +"";
                }
            }
        });
        btn_buy5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_recharge.clearFocus();
                timelist(4, v);
                btn_buy2.setActivated(false);
                btn_buy3.setActivated(false);
                btn_buy4.setActivated(false);
                btn_buy1.setActivated(false);
                btn_buy6.setActivated(false);
                btn_buy7.setActivated(false);
                flags[1] = 0;
                flags[2] = 0;
                flags[3] = 0;
                flags[0] = 0;
                flags[5] = 0;
                flags[6] = 0;
                if (flags[4]==1) {
                    reChargeMoney = flags[0] * 20 + flags[1] * 50 + flags[2] * 100 + flags[3] * 200 + flags[4] * 498 + flags[5] * 598 + flags[6] * 888 +"";
                }
            }
        });
        btn_buy6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_recharge.clearFocus();
                timelist(5, v);
                btn_buy2.setActivated(false);
                btn_buy3.setActivated(false);
                btn_buy4.setActivated(false);
                btn_buy5.setActivated(false);
                btn_buy1.setActivated(false);
                btn_buy7.setActivated(false);
                flags[1] = 0;
                flags[2] = 0;
                flags[3] = 0;
                flags[4] = 0;
                flags[0] = 0;
                flags[6] = 0;
                if (flags[5]==1) {
                    reChargeMoney = flags[0] * 20 + flags[1] * 50 + flags[2] * 100 + flags[3] * 200 + flags[4] * 498 + flags[5] * 598 + flags[6] * 888 +"";
                }
            }
        });
        btn_buy7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_recharge.clearFocus();
                timelist(6, v);
                btn_buy2.setActivated(false);
                btn_buy3.setActivated(false);
                btn_buy4.setActivated(false);
                btn_buy5.setActivated(false);
                btn_buy6.setActivated(false);
                btn_buy1.setActivated(false);
                flags[1] = 0;
                flags[2] = 0;
                flags[3] = 0;
                flags[4] = 0;
                flags[5] = 0;
                flags[0] = 0;
                if (flags[6]==1) {
                    reChargeMoney = flags[0] * 20 + flags[1] * 50 + flags[2] * 100 + flags[3] * 200 + flags[4] * 498 + flags[5] * 598 + flags[6] * 888 +"";
                }
                }
        });


        //计算充值的金额的点击事件
        btn_reCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((flags[0]+flags[1]+flags[2]+flags[3]+flags[4]+flags[5]+flags[6])==0){
                    reChargeMoney =et_recharge.getText().toString();}
                //创建一个提示对话框的构造者
                AlertDialog.Builder builder = new AlertDialog.Builder(reChargeActivity.this);
                builder.setTitle("请选择支付方式"); //设置标题
                //  builder.setMessage("您将需要支付" + s + "元"+","+"请选择支付方式");  //提示信息
                final String[] items = {"微信", "支付宝", "其他支付方式"};
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (items[which]){
                            case "微信":
                                setMoney1();
                               // setMoney2();
                                break;
                            case "支付宝":
                                setMoney1();
                              //  setMoney2();
                                break;
                            case "其他支付方式":
                                setMoney1();
                              //  setMoney2();
                                break;
                        }
                    }
                });
                builder.show();
            }
        });
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
    }

    private String status;
    //充值余额
    public void setMoney1() {
        reChargeMoney = flags[0] * 20 + flags[1] * 50 + flags[2] * 100 + flags[3] * 200 + flags[4] * 498 + flags[5] * 598 + flags[6] * 888 +"";
        String url = "https://3a142762b7.eicp.vip/user/updateBalances";
        RequestQueue queue = Volley.newRequestQueue(reChargeActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println(s);
                        status=s;
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
                map.put("balances",reChargeMoney);
                return map;
            }
        };
        queue.add(request);
    }

   /* public void setMoney2() {
        reChargeMoney = flags[0] * 20 + flags[1] * 50 + flags[2] * 100 + flags[3] * 200 + flags[4] * 498 + flags[5] * 598 + flags[6] * 888 +"";
        String url = "https://d30n971042.imdo.co/user/charges";
        RequestQueue queue = Volley.newRequestQueue(reChargeActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println(s);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(s);
                            status= jsonObject.getString("money").toString();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                map.put("username", "tom");
                map.put("charge",reChargeMoney);
                return map;
            }
        };
        queue.add(request);
    }*/

    //获取余额
    public void getMoney() {
        String url = "https://3a142762b7.eicp.vip/user/findUserBalances";
        RequestQueue queue = Volley.newRequestQueue(reChargeActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println(s);
                       /* JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(s);
                            money= jsonObject.getString("balances").toString();
                            System.out.println(money);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                        Gson gson = new Gson();
                        util = gson.fromJson(s,new TypeToken<rechargeUtil>() {
                        }.getType());
                        handler.sendEmptyMessage(1);
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
                    if ("200".equals(status)){
                        a=1;
                    }else{
                        a=0;
                    }
                    if (a==1) {
                        System.out.println(a);
                        Toast.makeText(reChargeActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(reChargeActivity.this, "余额不足", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1:
                    money=util.getBalances();
                    tv_RestMoney=findViewById(R.id.tv_RestMoney);
                    tv_RestMoney.setText("  剩余金额： "+money+"元");
                    break;
            }
        }
    };
}
