package cn.allen.warehouse;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.baidu.techain.ac.Callback;
import com.baidu.techain.ac.TH;

import allen.frame.AllenIMBaseActivity;
import allen.frame.AllenManager;
import allen.frame.tools.Logger;
import allen.frame.tools.MsgUtils;
import allen.frame.tools.StringUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.allen.warehouse.data.WebHelper;
import cn.allen.warehouse.utils.ChineseToSpeech;
import cn.allen.warehouse.utils.Constants;

import static androidx.core.content.PermissionChecker.PERMISSION_GRANTED;

public class LoginActivity extends AllenIMBaseActivity {
    @BindView(R.id.login_account)
    AppCompatEditText loginAccount;
    @BindView(R.id.login_psw)
    AppCompatEditText loginPsw;
    @BindView(R.id.login_bt)
    AppCompatButton loginBt;
    private String account,psw;
    public static final int REQUEST_CAMERA_PERMISSION = 1003;
    private SharedPreferences shared;
    private ChineseToSpeech speech;

    @Override
    protected boolean isStatusBarColorWhite() {
        return false;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_login;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speech.destroy();
    }

    @Override
    protected void initBar() {
        Logger.init().setHttp(true).setDebug(true);
        ButterKnife.bind(this);
        shared = AllenManager.getInstance().getStoragePreference();
        speech = new ChineseToSpeech(context);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION: {
                if(checkIsOk(grantResults)){

                }else{
                    finish();
                }
                break;
            }
        }
    }

    private boolean checkIsOk(int[] grantResults){
        boolean isok = true;
        for(int i:grantResults){
            isok = isok && (i == PackageManager.PERMISSION_GRANTED);
        }
        return isok;
    }

    @Override
    protected void initUI(@Nullable Bundle savedInstanceState) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=PERMISSION_GRANTED
                ||ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) !=PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
            return;
        }
        loginAccount.setText(shared.getString(Constants.UserAccount,""));
        loginPsw.setText(shared.getString(Constants.UserPsw,""));
    }

    @Override
    protected void addEvent() {

    }

    @OnClick(R.id.login_bt)
    public void onViewClicked(View v) {
        v.setEnabled(false);
        switch (v.getId()){
            case R.id.login_bt:
                if(checkIsOk()){
                    login();
                }
                break;
        }
        v.setEnabled(true);
    }

    private boolean checkIsOk(){
        account = loginAccount.getText().toString().trim();
        psw = loginPsw.getText().toString().trim();
        if(StringUtils.empty(account)){
            MsgUtils.showMDMessage(context,"请输入用户账号!");
            return false;
        }
        if(StringUtils.empty(psw)){
            MsgUtils.showMDMessage(context,"请输入用户密码!");
            return false;
        }
        return true;
    }

    private void login(){
        showProgressDialog("正在登陆，请稍等...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                WebHelper.init().login(handler,account,psw);
            }
        }).start();
    }

    private void setAlias(int id){
        TH.tinvoke(100019,"setAlias",new Callback(){
                    @Override
                    public Object onEnd(Object... objects) {
                        Logger.e("PUSH_SDK", "onEnd：" + objects[0]);
                        return super.onEnd(objects);
                    }

                    @Override
                    public Object onError(Object... objects) {
                        Logger.e("PUSH_SDK", "onError：" + objects[0]);
                        return super.onError(objects);
                    }
                },new Class[]{String.class},"ware_house_"+id);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    dismissProgressDialog();
                    MsgUtils.showShortToast(context, (String) msg.obj);
                    setAlias(shared.getInt(Constants.UserId,-1));
                    startActivity(new Intent(context,MainActivity.class));
//                    speech.speech((String) msg.obj);
                    break;
                case -1:
                    dismissProgressDialog();
                    MsgUtils.showMDMessage(context, (String) msg.obj);
                    break;
            }
        }
    };
}
