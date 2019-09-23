package com.sangbu.superchallenge;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kakao.sdk.newtoneapi.SpeechRecognizerManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

import static com.kakao.util.helper.Utility.getPackageInfo;

public class MainActivity extends AppCompatActivity {

    public static BluetoothSPP bt;

    Button tts;
    Button btnConnect;
    Button btnSend;

    int xmlcrt;

    @Override
    protected void onStart() {
        super.onStart();
        if (!bt.isBluetoothEnabled()) { //
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        } else {
            if (!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER); //DEVICE_ANDROID는 안드로이드 기기 끼리
                setup();
            }
        }
    }

    public void setup() {
        btnSend = findViewById(R.id.btnSend); //데이터 전송
        btnSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bt.send("Text", true);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bt.stopService();
}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                bt.connect(data);
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
                setup();
            } else {
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        } else if(resultCode == RESULT_OK)
        {
            switch (requestCode)
            {
                case 101:
                    Log.d("blue", "dosetdatareceived");
                    bt.send(data.getStringExtra("result"), true);
                    bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() { //데이터 수신
                        public void onDataReceived(byte[] data, String message) {
                            //Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                            btninput(message);
                        }
                    });
                    break;
            }
        }
    }

    private void permissionCheck() {
        if (Build.VERSION.SDK_INT >= 23) {

            if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                Log.v("permission", "Permission is granted");
            } else {
                Log.v("permission", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
            }
        } else {
            Toast.makeText(this, "External Storage Permission is Grant", Toast.LENGTH_SHORT).show();
            Log.d("permission", "External Storage Permission is Grant ");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SpeechRecognizerManager.getInstance().initializeLibrary(this);

        permissionCheck();

        bt = new BluetoothSPP(this); //Initializing

        if (!bt.isBluetoothAvailable()) { //블루투스 사용 불가
            Toast.makeText(getApplicationContext()
                    , "Bluetooth is not available"
                    , Toast.LENGTH_SHORT).show();
            finish();
        }

        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() { //데이터 수신
            public void onDataReceived(byte[] data, String message) {
                //Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                btninput(message);
            }
        });

        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() { //연결됐을 때
            public void onDeviceConnected(String name, String address) {
                Toast.makeText(getApplicationContext()
                        , "Connected to " + name + "\n" + address
                        , Toast.LENGTH_SHORT).show();
            }

            public void onDeviceDisconnected() { //연결해제
                Toast.makeText(getApplicationContext()
                        , "Connection lost", Toast.LENGTH_SHORT).show();
            }

            public void onDeviceConnectionFailed() { //연결실패
                Toast.makeText(getApplicationContext()
                        , "Unable to connect", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void  onClick(View v){
                switch(v.getId());
                case.R.id.
            }
        });

        btnConnect = findViewById(R.id.btnConnect); //연결시도
        btnConnect.setBackgroundDrawable(getResources().getDrawable(R.drawable.btnshape));
        btnConnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    bt.disconnect();
                } else {
                    Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
            }
        });

        tts = (Button) findViewById(R.id.gotts);
        tts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tts = new Intent(MainActivity.this, ttschk.class);
                startActivityForResult(tts, 101);
            }
        });
    }

    private void btninput(String message) {
        int chk = Integer.parseInt(message);
        switch (chk) {
            case 0:
                Toast.makeText(getApplicationContext(), "button 0", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                chgxml(false);
                break;
            case 2:
                chgxml(true);
                break;
            case 3:
                Toast.makeText(getApplicationContext(), "button 3", Toast.LENGTH_SHORT).show();
                break;
            case 4:
                pushcomfrimbtn();
                break;
        }
    }

    private void pushcomfrimbtn() {
        switch (xmlcrt)
        {
            case 0:
                btnConnect.performClick();
                break;
            case 1:
                btnSend.performClick();
                break;
            case 2:
                tts.performClick();
                break;
        }
    }

    private void chgxml(boolean b) {
        if (b) {
            switch (xmlcrt) {
                case 0:
                    btnConnect.setBackgroundDrawable(null);
                    btnSend.setBackgroundDrawable(getResources().getDrawable(R.drawable.btnshape));
                    xmlcrt = 1;
                    break;
                case 1:
                    btnSend.setBackgroundDrawable(null);
                    tts.setBackgroundDrawable(getResources().getDrawable(R.drawable.btnshape));
                    xmlcrt = 2;
                    break;
                case 2:
                    tts.setBackgroundDrawable(null);
                    btnConnect.setBackgroundDrawable(getResources().getDrawable(R.drawable.btnshape));
                    xmlcrt = 0;
                    break;
            }
        } else {
            switch (xmlcrt) {
                case 0:
                    btnConnect.setBackgroundDrawable(null);
                    tts.setBackgroundDrawable(getResources().getDrawable(R.drawable.btnshape));
                    xmlcrt = 2;
                    break;
                case 1:
                    btnSend.setBackgroundDrawable(null);
                    btnConnect.setBackgroundDrawable(getResources().getDrawable(R.drawable.btnshape));
                    xmlcrt = 0;
                    break;
                case 2:
                    tts.setBackgroundDrawable(null);
                    btnSend.setBackgroundDrawable(getResources().getDrawable(R.drawable.btnshape));
                    xmlcrt = 1;
                    break;
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (Build.VERSION.SDK_INT >= 23) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.v("permission", "Permission: " + permissions[0] + "was " + grantResults[0]);
                //resume tasks needing this permission
            }
        }
    }
}
