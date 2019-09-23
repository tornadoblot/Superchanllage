package com.sangbu.superchallenge;

import android.app.Activity;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kakao.sdk.newtoneapi.TextToSpeechClient;
import com.kakao.sdk.newtoneapi.TextToSpeechListener;
import com.kakao.sdk.newtoneapi.TextToSpeechManager;

import java.util.Locale;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

import static android.speech.tts.TextToSpeech.ERROR;
import static com.sangbu.superchallenge.MainActivity.bt;

public class ttschk extends AppCompatActivity implements View.OnClickListener, TextToSpeechListener{


    private TextToSpeech tts;

    TextToSpeechClient ttsClient;


    Intent rsIntent;
    EditText editText;
    String etStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ttschk);

        TextToSpeechManager.getInstance().initializeLibrary(getApplicationContext());

        ttsClient = new TextToSpeechClient.Builder().setSpeechMode(TextToSpeechClient.NEWTONE_TALK_1)
                .setSpeechSpeed(1.0).setSpeechVoice(TextToSpeechClient.VOICE_WOMAN_DIALOG_BRIGHT)
                .setListener(this).build();

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != ERROR){
                    tts.setLanguage(Locale.KOREA);
                }
            }
        });

        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() { //데이터 수신
            public void onDataReceived(byte[] data, String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        rsIntent = new Intent();
        setResult(RESULT_OK, rsIntent);

        Button button = (Button) findViewById(R.id.button);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);
        Button button4 = (Button) findViewById(R.id.button4);
        Button button5 = (Button) findViewById(R.id.button5);
        Button button6 = (Button) findViewById(R.id.button6);
        Button button7 = (Button) findViewById(R.id.button7);
        editText = (EditText) findViewById(R.id.editText);

        button.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.button:
                //ttsClient.play("어서 오세요, 맥도날드입니다. 주문 시작하시려면 확인 버튼을 눌러주세요");
                tts.speak("어서 오세요, 맥도날드입니다. 주문 시작하시려면 확인 버튼을 눌러주세요", TextToSpeech.QUEUE_FLUSH, null);
                tts.speak("", TextToSpeech.QUEUE_FLUSH, null);
                break;
            case R.id.button2:
                ttsClient.play("음식 선택 단계입니다. 버거, 음료, 사이드, 세트 중에서 방향키를 이용하여 주문하실 음식을 선택한 후, 확인 버튼을 눌러 주세요");
                break;
            case R.id.button3:
                //ttsClient.play("선택하셨습니다.");
                tts.speak("를 선택하셨습니다", TextToSpeech.QUEUE_FLUSH, null);
                break;
            case R.id.button4:
                ttsClient.play("버거를 선택하셨습니다. 다음의 4가지 맛 중에서, 방향키를 이용하여 햄버거의 맛을 버튼 혹은 음성으로 선택하여 주세요");
                break;
            case R.id.button5:
                ttsClient.play("다음의 맛 버거 중에서, 주문하실 버거를 버튼 혹은 음성으로 선택하여 주세요");
                break;
            case R.id.button6:
                ttsClient.play("맛 버거 단품을 선택하셨습니다. 방향키를 이용하여 수량을 선택한 후 확인 버튼을 눌러 주세요");
                break;
            case R.id.button7:
                etStr = editText.getText().toString();
                rsIntent.putExtra("result", etStr);
                setResult(RESULT_OK, rsIntent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TextToSpeechManager.getInstance().finalizeLibrary();
        if(tts != null){
            tts.stop();
            tts.shutdown();
            tts = null;
        }

    }

    @Override
    public void onFinished() {

    }

    @Override
    public void onError(int code, String message) {

    }
}
