package cn.allen.warehouse.data;

import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import allen.frame.AllenManager;
import allen.frame.tools.Logger;
import cn.allen.warehouse.entry.Order;
import cn.allen.warehouse.entry.Response;
import cn.allen.warehouse.entry.User;
import cn.allen.warehouse.utils.Constants;
import cn.allen.warehouse.utils.NullStringEmptyTypeAdapterFactory;

public class WebHelper {

    private WebService service;
    private static WebHelper helper;
    private Gson gson;

    private WebHelper(){
        service = new WebService();
        gson = new GsonBuilder().registerTypeAdapterFactory(new NullStringEmptyTypeAdapterFactory()).create();
    }
    public static WebHelper init(){
        if(helper==null){
            helper = new WebHelper();
        }
        return helper;
    }

    private void saveToLoacal(User user){
        Logger.debug("user",user.toString());
        AllenManager.getInstance().getStoragePreference().edit()
                .putInt(Constants.UserId,user.getId())
                .putString(Constants.UserAccount,user.getAccount())
                .putString(Constants.UserName,user.getName())
                .putString(Constants.UserPhone,user.getPhone())
                .putInt(Constants.UserType,user.getType())
                .putString(Constants.UserCreateTime,user.getCreatetime())
                .putString(Constants.UserWareHouse,user.getWarehouse())
                .apply();
    }

    /**
     * 登录
     * @param handler
     * @param account
     * @param psw
     */
    public void login(Handler handler, String account, String psw){
        Object[] objects = new Object[]{
                "account",account,"pwd",psw
        };
        Response response = service.getWebservice(Api.Login,objects,WebService.Get);
        Message msg = new Message();
        if(response.isSuccess("200")){
            User user = gson.fromJson(response.getData(),User.class);
            if(user!=null){
                saveToLoacal(user);
                msg.what = 0;
                msg.obj = response.getMessage();
            }else{
                msg.what = -1;
                msg.obj = "数据异常!";
            }
        }else{
            msg.what = -1;
            msg.obj = response.getMessage();
        }
        handler.sendMessage(msg);
    }

    /**
     * 获取所有订单
     * @param uid
     * @param page
     * @param pagesize
     * @return
     */
    public List<Order> getAllOrder(int uid,int page,int pagesize){
        Object[] objects = new Object[]{
                "uid",uid,"page",page,"pagesize",pagesize
        };
        List<Order> list = new ArrayList<>();
        Response response = service.getWebservice(Api.GetAllOrder,objects,WebService.Get);
        if(response.isSuccess("200")){
            list = gson.fromJson(response.getData(), new TypeToken<List<Order>>(){}.getType());
        }
        return list;
    }
}
