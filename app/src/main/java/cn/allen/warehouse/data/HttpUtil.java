package cn.allen.warehouse.data;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import allen.frame.tools.Logger;
import allen.frame.tools.StringUtils;
import cn.allen.warehouse.utils.Constants;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtil {
    private static final long TIME_OUT = 120;//单位秒S
    private String url;

    public HttpUtil() {
        url = Constants.Url;
    }

    /**
     * get请求
     *
     * @param mothed
     * @param array
     * @return
     */
    public String okhttpget(String mothed, String array) {
        String data = "";
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(TIME_OUT, TimeUnit.SECONDS).readTimeout(TIME_OUT, TimeUnit.SECONDS)
                    .build();
            Request request = new Request.Builder()
                    .url(url + mothed + (StringUtils.empty(array) ? "" : "?" + array)).build();
            Response response = null;
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                data = response.body().string();
            }
        } catch (Exception e) {
            Logger.http("erro:", e.getMessage());
            e.printStackTrace();
        }
        return data;
    }

    public String okhttppost(String mothed, String json) {
        String data = "";
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(TIME_OUT, TimeUnit.SECONDS).readTimeout(TIME_OUT, TimeUnit.SECONDS)
                    .build();// 创建OkHttpClient对象。
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");// 数据类型为json格式，
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder().url(url + mothed).post(body).build();
            Response response = null;
            response = client.newCall(request).execute();
            Logger.http("Response>>", response.toString());
            if (response.isSuccessful()) {
                data = response.body().string();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return data;
    }

    // 使用OkHttp上传文件
    public String uploadFile(String mothed, File file) {
        String data = "";
        try {
            OkHttpClient client = new OkHttpClient();
            MediaType contentType = MediaType.parse("image/jpeg; charset=utf-8"); // 上传文件的Content-Type
            // 上传文件使用MultipartBody.Builder
            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", file.getName(), RequestBody.create(contentType, file)) // 提交图片，第一个参数是键（name="第一个参数"），第二个参数是文件名，第三个是一个RequestBody
                    .build();
            Request request = new Request.Builder()
                    .url(url + mothed) // 上传地址
                    .post(body)
                    .build();
            Response response = null;
            response = client.newCall(request).execute();
            Logger.http("Response>>", response.toString());
            if (response.isSuccessful()) {
                data = response.body().string();
                Logger.http("data>>", data);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return data;
    }
}
