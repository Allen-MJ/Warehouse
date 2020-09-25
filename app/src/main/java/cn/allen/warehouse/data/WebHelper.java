package cn.allen.warehouse.data;

import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import allen.frame.AllenManager;
import allen.frame.tools.Logger;
import cn.allen.warehouse.entry.Data;
import cn.allen.warehouse.entry.Flower;
import cn.allen.warehouse.entry.FlowerType;
import cn.allen.warehouse.entry.Notice;
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

    private Object[] getDataFromJson(String json) throws JSONException {
        Object[] data = new Object[3];
        JSONObject object = new JSONObject(json);
        data[0] = object.optInt("recordCount");//总条数
        data[1] = object.getString("total");//总页数
        data[2] = object.getString("list");
        return data;
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
    public Data<Order> getAllOrder(int uid,int page,int pagesize){
        Object[] objects = new Object[]{
                "id",uid,"page",page,"size",pagesize
        };
        Response response = service.getWebservice(Api.GetAllOrder,objects,WebService.Get);
        Data<Order> data = new Data<>();
        List<Order> list = new ArrayList<>();
        if(response.isSuccess("200")){
            try {
                Object[] ob = getDataFromJson(response.getData());
                list = gson.fromJson(ob[2].toString(), new TypeToken<List<Order>>(){}.getType());
                data.setCount(Integer.parseInt(ob[0].toString()));
                data.setTotal(Integer.parseInt(ob[1].toString()));
                data.setList(list);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    /**
     * 获取花类
     * @return
     */
    public List<FlowerType> getGrade(){
        Object[] objects = new Object[]{

        };
        List<FlowerType> list = new ArrayList<>();
        Response response = service.getWebservice(Api.GetGrade,objects,WebService.Get);
        if(response.isSuccess("200")){
            list = gson.fromJson(response.getData(), new TypeToken<List<FlowerType>>(){}.getType());
        }
        return list;
    }

    /**
     * 根据类别ID查询鲜花列表
     * @param id
     * @return
     */
    public List<Flower> getFlowers(int id){
        Object[] objects = new Object[]{
            "id",id
        };
        List<Flower> list = new ArrayList<>();
        Response response = service.getWebservice(Api.GetFlowers,objects,WebService.Get);
        if(response.isSuccess("200")){
            list = gson.fromJson(response.getData(), new TypeToken<List<Flower>>(){}.getType());
        }
        return list;
    }

    /**
     *
     * @param starttime
     * @param endtime
     * @param name
     * @return
     */
    public List<Flower> getSeachFlower(String starttime,String endtime,String name){
        Object[] objects = new Object[]{
            "starttime",starttime,"endtime",endtime,"name",name
        };
        List<Flower> list = new ArrayList<>();
        Response response = service.getWebservice(Api.GetSeachFlower,objects,WebService.Get);
        if(response.isSuccess("200")){
            list = gson.fromJson(response.getData(), new TypeToken<List<Flower>>(){}.getType());
        }
        return list;
    }

    /**
     * 获取消息通知
     * @return
     */
    public List<Notice> getInformation(){
        Object[] objects = new Object[]{
        };
        List<Notice> list = new ArrayList<>();
        Response response = service.getWebservice(Api.GetInformation,objects,WebService.Get);
        if(response.isSuccess("200")){
            list = gson.fromJson(response.getData(), new TypeToken<List<Notice>>(){}.getType());
        }
        return list;
    }

    /**
     * 数量统计
     * @return
     */
    public Map<String,Integer> getOrderNumber(){
        Object[] objects = new Object[]{
        };
        Map<String,Integer> map = new HashMap<>();
        Response response = service.getWebservice(Api.GetState,objects,WebService.Get);
        if(response.isSuccess("200")){
            try {
                JSONObject object = new JSONObject(response.getData());
                map.put("1",object.optInt("daipeihuo"));
                map.put("2",object.optInt("daichuku"));
                map.put("3",object.optInt("daihuishou"));
                map.put("4",object.optInt("yihuishou"));
                map.put("5",object.optInt("wancheng"));
            } catch (JSONException e) {
                e.printStackTrace();
                map.put("1",0);
                map.put("2",0);
                map.put("3",0);
                map.put("4",0);
                map.put("5",0);
            }
        }else{
            map.put("1",0);
            map.put("2",0);
            map.put("3",0);
            map.put("4",0);
            map.put("5",0);
        }
        return map;
    }

}
