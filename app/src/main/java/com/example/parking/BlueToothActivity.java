package com.example.parking;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.os.Messenger;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.UUID;
import android.os.Message;

public class BlueToothActivity extends AppCompatActivity {

    private ImageView img_btoothBack;
    private Switch switch_btooth;

    private BluetoothAdapter bluetoothAdapter = null;// 本地蓝牙适配器
    public static BluetoothDevice device = null;    // 远程设备
    private BluetoothSocket socket = null;    // 蓝牙设备Socket客户端
    private String uuid = "6E400001-B5A3-F393-E0A9-E50E24DCCA9E";//UUID
    private String mac = "EC:3C:BB:45:F5:F4";//MAC地址
    private String openKey = "10";//打开车位锁密钥
    private String closeKey = "11";//关闭车位锁密钥

    // 连接成功
    private static final int CONN_SUCCESS = 0x1;
    // 连接失败
    private static final int CONN_FAIL = 0x2;
    private static final int RECEIVER_INFO = 0x3;

    // 设备名称
    private static final String NAME = "LGL";
    private boolean isReceiver = true;
    // 输入输出流
    private PrintStream out;
    private BufferedReader in;
    private Dialog dialogLoading;//对话框
    private ImageButton btn_openLock, btn_closeLock;
    private ImageButton btn_connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_tooth);

        init();

    }

    private BluetoothAdapter bluetoothAdapter1 = BluetoothAdapter.getDefaultAdapter();
    private boolean blueToothLock = false;

    private void init() {


        img_btoothBack = findViewById(R.id.img_btoothback);
        switch_btooth = findViewById(R.id.switch_btooth);

        img_btoothBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BlueToothActivity.this, com.example.parking.btomNaviActivity.MainAllActivity.class);
                startActivity(intent);
            }
        });

        //检测蓝牙是否打开，如果打开switch默认是开
        if (bluetoothAdapter1.isEnabled()) {
            blueToothLock = true;
        }
        switch_btooth.setChecked(blueToothLock);
        switch_btooth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    //打开蓝牙本机的蓝牙发现功能（默认打开120秒，一个应用程序可以设定的最长持续时间为3600秒
                    enabler.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                    startActivity(enabler);
                } else {
                    //关闭蓝牙
                    bluetoothAdapter1.disable();
                }
            }
        });

       /* //蓝牙连接按钮
        btn_connect=findViewById(R.id.img_btoothconnect);
        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (bluetoothAdapter1.isEnabled()) {
                    dialogLoading= Ohuang.dialogLoadingshow(BlueToothActivity.this,"连接中");//显示对话框
                    //开始连接
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            connect();//连接

                        }
                    }).start();
                } else {
                    Toast.makeText(BlueToothActivity.this, "请打开蓝牙开关", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_openLock=findViewById(R.id.imgbtn_btoothnolock);
        btn_closeLock=findViewById(R.id.imgbtn_btoothlock);

        //开锁按钮
        btn_openLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendText(openKey);//发送信息
            }
        });
//上锁按钮
        btn_closeLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendText(closeKey);//发送信息
            }
        });
    }

    private void connect() {
        try {
            // 得到本地蓝牙适配器

            // 通过本地适配器得到地址,这个地址可以公共扫描来获取，就是getAddress()返回的地址
            device = bluetoothAdapter
                    .getRemoteDevice(mac);
            // 根据UUID返回一个socket,要与服务器的UUID一致
            socket = device.createRfcommSocketToServiceRecord(UUID
                    .fromString(uuid));
            if (socket != null) {
                // 连接
                socket.connect();
                // 处理流
                out = new PrintStream(socket.getOutputStream());

                in = new BufferedReader(new InputStreamReader(socket
                        .getInputStream()));
            }
            // 连接成功发送handler
            handler.sendEmptyMessage(CONN_SUCCESS);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Message mes = handler.obtainMessage(CONN_FAIL,
                    e.getLocalizedMessage());
            handler.sendMessage(mes);
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CONN_SUCCESS://连接成功
                    btn_closeLock.setEnabled(true);
                    btn_openLock.setEnabled(true);
                    dialogLoading.dismiss();//关闭对话框
                    Toast.makeText(BlueToothActivity.this,"连接成功",Toast.LENGTH_SHORT).show();
                    Log.i("设备的名称", device.getName());
                    Log.i("设备的UUID", device.getUuids() + "");
                    Log.i("设备的地址", device.getAddress());
                    // 开始接收信息
                    new Thread(new ReceiverInfoThread()).start();
                    break;
                case CONN_FAIL://连接失败

                    dialogLoading.dismiss();//关闭对话框

                    Toast.makeText(BlueToothActivity.this, "蓝牙连接失败", Toast.LENGTH_SHORT).show();
                    break;
                case RECEIVER_INFO://接受信息
                    System.out.println(msg.obj.toString());
                    break;


            }
        }
    };

    private void sendText(final String text) {
        // 不能为空
        if (text.isEmpty()) {
            Toast.makeText(this, "不能为空", Toast.LENGTH_SHORT).show();
        }
        new Thread(new Runnable() {

            @Override
            public void run() {
                // 输出
                out.println(text);
                out.flush();

            }
        }).start();
    }

    *//**
         * 接收消息的线程
         *//*
    class ReceiverInfoThread implements Runnable {

        @Override
        public void run() {
            String info = null;
            while (isReceiver) {
                try {
                    info = in.readLine();
                    Message msg = handler.obtainMessage(RECEIVER_INFO);
                    msg.what = RECEIVER_INFO;
                    msg.obj = info;
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    //返回按钮
    public void CloseBlueToothClick(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);//手动地把消息队列中的消息清空
    }
}
*/
    }
}