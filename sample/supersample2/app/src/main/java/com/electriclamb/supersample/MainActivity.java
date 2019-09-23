package com.electriclamb.supersample;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.service.autofill.TextValueSanitizer;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.sdk.newtoneapi.SpeechRecognizeListener;
import com.kakao.sdk.newtoneapi.SpeechRecognizerClient;
import com.kakao.sdk.newtoneapi.SpeechRecognizerManager;
import com.kakao.sdk.newtoneapi.TextToSpeechClient;
import com.kakao.sdk.newtoneapi.TextToSpeechManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Permission;
import java.util.ArrayList;

import static com.kakao.util.helper.Utility.getPackageInfo;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SpeechRecognizeListener {

    private static final int REQUEST_CODE_AUDIO_AND_WRITE_EXTERNAL_STORAGE = 0;
    TextView textView;
    SpeechRecognizerClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissionCheck();

        SpeechRecognizerManager.getInstance().initializeLibrary(this);

        Button button = (Button) findViewById(R.id.button);
        Button tts = (Button) findViewById(R.id.button2);

        button.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_border));

        textView = findViewById(R.id.textView);
        button.setOnClickListener(this);
        tts.setOnClickListener(this);
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

    public void onDestroy() {
        super.onDestroy();

        SpeechRecognizerManager.getInstance().finalizeLibrary();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                // 클라이언트 생성
                SpeechRecognizerClient.Builder builder = new SpeechRecognizerClient.Builder().
                        setServiceType(SpeechRecognizerClient.SERVICE_TYPE_DICTATION);  // optional

                SpeechRecognizerClient client = builder.build();

                client.setSpeechRecognizeListener(this);
                client.startRecording(true);
                Toast.makeText(MainActivity.this, "newtone start", Toast.LENGTH_SHORT).show();
                break;

            case R.id.button2:
                Intent intent = new Intent(MainActivity.this, ttssample.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onReady() {
        Log.d("newtone", "onready");
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.d("newtone", "speech begin");
    }

    @Override
    public void onEndOfSpeech() {
        Log.d("newtone", "speech end");
    }

    @Override
    public void onError(int errorCode, String errorMsg) {
        Log.d("newtone", "speech error: " + errorMsg);
    }

    @Override
    public void onPartialResult(String partialResult) {
        Log.d("newtone", "onready");
    }

    @Override
    public void onResults(Bundle results) {
        final StringBuilder builder = new StringBuilder();

        final ArrayList<String> texts = results.getStringArrayList(SpeechRecognizerClient.KEY_RECOGNITION_RESULTS);
        ArrayList<Integer> confs = results.getIntegerArrayList(SpeechRecognizerClient.KEY_CONFIDENCE_VALUES);

        Log.d("newtone", "Result: " + texts);

        for (int i = 0; i < texts.size(); i++) {
            builder.append(texts.get(i));
            builder.append(" (");
            builder.append(confs.get(i).intValue());
            builder.append(")\n");
        }

        //모든 콜백함수들은 백그라운드에서 돌고 있기 때문에 메인 UI를 변경할려면 runOnUiThread를 사용해야 한다.
        final Activity activity = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (activity.isFinishing()) return;

                textView.setText(texts.get(0));
            }
        });


    }

    @Override
    public void onAudioLevel(float audioLevel) {
    }

    @Override
    public void onFinished() {
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
