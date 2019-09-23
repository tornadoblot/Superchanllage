package com.electriclamb.supersample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kakao.sdk.newtoneapi.TextToSpeechClient;
import com.kakao.sdk.newtoneapi.TextToSpeechListener;
import com.kakao.sdk.newtoneapi.TextToSpeechManager;

public class ttssample extends AppCompatActivity implements View.OnClickListener, TextToSpeechListener {

    EditText str;
    String read;

    TextToSpeechClient ttsClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ttssample);

        TextToSpeechManager.getInstance().initializeLibrary(getApplicationContext());

        Button tts = (Button) findViewById(R.id.tts);
        tts.setOnClickListener(this);
        Button rtmain = (Button) findViewById(R.id.rtmain);
        rtmain.setOnClickListener(this);
        str = (EditText) findViewById(R.id.str);

        ttsClient = new TextToSpeechClient.Builder().setSpeechMode(TextToSpeechClient.NEWTONE_TALK_1)
                .setSpeechSpeed(1.0).setSpeechVoice(TextToSpeechClient.VOICE_MAN_READ_CALM)
                .setListener(this).build();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        TextToSpeechManager.getInstance().finalizeLibrary();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tts:
                read = str.getText().toString();
                ttsClient.play(read);
                break;

            case R.id.rtmain:
                finish();
                break;
        }

    }

    @Override
    public void onFinished() {
        int intSentSize = ttsClient.getSentDataSize();
        int intRecvSize = ttsClient.getReceivedDataSize();

        final String strText = "SentSize: " + intSentSize + "  RecvSize: " + intRecvSize;
        Log.i("tts", strText);
    }

    @Override
    public void onError(int code, String message) {
        Log.e("tts", "Error: " + message);
    }
}
