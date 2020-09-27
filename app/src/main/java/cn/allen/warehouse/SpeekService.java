package cn.allen.warehouse;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import allen.frame.tools.Logger;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import cn.allen.warehouse.utils.ChineseToSpeech;

public class SpeekService extends Service {

    private ChineseToSpeech speech;
    private String content;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        content = intent.getStringExtra("content");
        Logger.e("Push_Demo","onStartCommand:"+content);
        handler.sendEmptyMessage(0);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        speech = new ChineseToSpeech(this);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    speech.speech(content);
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        speech.destroy();
    }
}
