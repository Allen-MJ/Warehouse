package cn.allen.warehouse;

import android.app.Application;

import com.baidu.techain.ac.Callback;
import com.baidu.techain.ac.TH;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TH.init(this,"700000372","d760569b53d23f3f5873be66ef169faf",100028,100019);
        TH.tinvoke(100019, "isPushEnabled",new Callback() {
            @Override
            public Object onEnd(Object...arg0) {
                return null;
            }

            @Override
            public Object onError(Object...arg0) {
                return null;
            }
        });
    }
}
