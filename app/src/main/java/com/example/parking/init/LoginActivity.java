package com.example.parking.init;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
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
import com.example.parking.R;
import com.example.parking.UserName;
import com.example.parking.btomNaviActivity.MainAllActivity;
import com.example.parking.btomNaviActivity.ui.home.HomeFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private ImageView img_loginquit;
    private TextView tv_signup;
    private Button btn_loginsuccess;
    private EditText et_namelogin,et_psdlogin;

    private String status = null; //服务器返回注册的状态码
    private Boolean success=false;

    private String username,psd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

    }



    private void init() {
        img_loginquit=findViewById(R.id.img_loginquit);
        tv_signup=findViewById(R.id.tv_signup);
        btn_loginsuccess=findViewById(R.id.btn_loginsuccess);
        et_namelogin=findViewById(R.id.et_namelogin);
        et_psdlogin=findViewById(R.id.et_psdlogin);

        img_loginquit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(11111);
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                System.exit(0);
            }
        });

        //登录界面注册按钮
        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });

        //登录按钮
        btn_loginsuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username=et_namelogin.getText().toString();
                psd=et_psdlogin.getText().toString();
                if (username.isEmpty() || psd.isEmpty()){
                    Toast.makeText(LoginActivity.this,"账户名或密码为空",Toast.LENGTH_SHORT).show();
                }else{
                    getStatus(); //服务器验证密码
                }

            }
        });

    }

private String zxc;
    //账号登录验证
    public void getStatus() {
        String url = "https://3a142762b7.eicp.vip/user/login";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println(s);

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(s);
                            zxc= jsonObject.getString("status").toString();
                            System.out.println(zxc);
                            if ("200".equals(zxc)) {
                                success = true;
                            }
                            if ("500".equals(zxc)) {
                                success = false;
                            }
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
                map.put("username", username);
                map.put("password", psd);
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
                    System.out.println(1233);
                    if (success) {
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    UserName.name=username;
                                    Thread.sleep(1000);
                                    Intent intent=new Intent(LoginActivity.this, MainAllActivity.class);
                                    startActivity(intent);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                    if (!success) {
                        Toast.makeText(LoginActivity.this, "账户名或者密码错误", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

}
