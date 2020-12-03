package cn.allen.warehouse.data;

import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import allen.frame.AllenManager;
import allen.frame.tools.Logger;
import cn.allen.warehouse.entry.Data;
import cn.allen.warehouse.entry.Flower;
import cn.allen.warehouse.entry.FlowerType;
import cn.allen.warehouse.entry.ImageEntity;
import cn.allen.warehouse.entry.Notice;
import cn.allen.warehouse.entry.Order;
import cn.allen.warehouse.entry.OrderInfoEntity;
import cn.allen.warehouse.entry.OrderInfoXsEntity;
import cn.allen.warehouse.entry.Response;
import cn.allen.warehouse.entry.User;
import cn.allen.warehouse.utils.Constants;
import cn.allen.warehouse.utils.NullStringEmptyTypeAdapterFactory;

public class WebHelper {

    private WebService service;
    private static WebHelper helper;
    private Gson gson;

    private WebHelper() {
        service = new WebService();
        gson = new GsonBuilder().registerTypeAdapterFactory(new NullStringEmptyTypeAdapterFactory()).create();
    }

    public static WebHelper init() {
        if (helper == null) {
            helper = new WebHelper();
        }
        return helper;
    }

    private void saveToLoacal(User user) {
        Logger.debug("user", user.toString());
        AllenManager.getInstance().getStoragePreference().edit()
                .putInt(Constants.UserId, user.getId())
                .putString(Constants.UserAccount, user.getAccount())
                .putString(Constants.UserName, user.getName())
                .putString(Constants.UserPhone, user.getPhone())
                .putInt(Constants.UserType, user.getType())
                .putString(Constants.UserCreateTime, user.getCreatetime())
                .putString(Constants.UserWareHouse, user.getWarehouse())
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
     *
     * @param handler
     * @param account
     * @param psw
     */
    public void login(Handler handler, String account, String psw) {
        Object[] objects = new Object[]{
                "account", account, "pwd", psw
        };
        Response response = service.getWebservice(Api.Login, objects, WebService.Get);
        Message msg = new Message();
        if (response.isSuccess("200")) {
            User user = gson.fromJson(response.getData(), User.class);
            if (user != null) {
                saveToLoacal(user);
                msg.what = 0;
                msg.obj = response.getMessage();
            } else {
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
     *
     * @param uid
     * @param page
     * @param pagesize
     * @return
     */
    public Data<Order> getAllOrder(int uid, String no, int page, int pagesize) {
        Object[] objects = new Object[]{
                "id", uid, "order", no,"page", page, "size", pagesize
        };
        Response response = service.getWebservice(Api.GetAllOrder, objects, WebService.Get);
        Data<Order> data = new Data<>();
        List<Order> list = new ArrayList<>();
        if (response.isSuccess("200")) {
            try {
                Object[] ob = getDataFromJson(response.getData());
                list = gson.fromJson(ob[2].toString(), new TypeToken<List<Order>>() {
                }.getType());
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
     *
     * @return
     */
    public List<FlowerType> getGrade() {
        Object[] objects = new Object[]{

        };
        List<FlowerType> list = new ArrayList<>();
        Response response = service.getWebservice(Api.GetGrade, objects, WebService.Get);
        if (response.isSuccess("200")) {
            list = gson.fromJson(response.getData(), new TypeToken<List<FlowerType>>() {
            }.getType());
        }
        return list;
    }

    /**
     * 根据类别ID查询鲜花列表
     *
     * @param id
     * @return
     */
    public List<Flower> getFlowers(int id) {
        Object[] objects = new Object[]{
                "id", id
        };
        List<Flower> list = new ArrayList<>();
        Response response = service.getWebservice(Api.GetFlowers, objects, WebService.Get);
        if (response.isSuccess("200")) {
            list = gson.fromJson(response.getData(), new TypeToken<List<Flower>>() {
            }.getType());
        }
        return list;
    }

    /**
     * 根据类别、时间、花名查询
     *
     * @param type
     * @param endtime
     * @param starttime
     * @param name
     * @return
     */
    public List<Flower> getAllType(int type, String endtime, String starttime, String name) {
        Object[] objects = new Object[]{
                "type", type, "endtime", endtime, "starttime", starttime, "name", name
        };
        List<Flower> list = new ArrayList<>();
        Response response = service.getWebservice(Api.GetAllType, objects, WebService.Get);
        if (response.isSuccess("200")) {
            list = gson.fromJson(response.getData(), new TypeToken<List<Flower>>() {
            }.getType());
        }
        return list;
    }

    /**
     * @param starttime
     * @param endtime
     * @param name
     * @return
     */
    public List<Flower> getSeachFlower(String starttime, String endtime, String name) {
        Object[] objects = new Object[]{
                "starttime", starttime, "endtime", endtime, "name", name
        };
        List<Flower> list = new ArrayList<>();
        Response response = service.getWebservice(Api.GetSeachFlower, objects, WebService.Get);
        if (response.isSuccess("200")) {
            list = gson.fromJson(response.getData(), new TypeToken<List<Flower>>() {
            }.getType());
        }
        return list;
    }

    /**
     * 获取消息通知
     *
     * @return
     */
    public List<Notice> getInformation(int id) {
        Object[] objects = new Object[]{
                "id",id
        };
        List<Notice> list = new ArrayList<>();
        Response response = service.getWebservice(Api.GetInformation, objects, WebService.Get);
        if (response.isSuccess("200")) {
            list = gson.fromJson(response.getData(), new TypeToken<List<Notice>>() {
            }.getType());
        }
        return list;
    }

    /**
     * 信息已读
     * @param handler
     * @param id
     */
    public void readMsg(Handler handler, int id){
        Object[] objects = new Object[]{
                "id", id
        };
        Response response = service.getWebservice(Api.GetRed, objects, WebService.Get);
        Message msg = new Message();
        if (response.isSuccess("200")) {
                msg.what = 10;
                msg.obj = response.getMessage();
        }else{
            msg.what = -1;
            msg.obj = response.getMessage();
        }
        handler.sendMessage(msg);
    }

    /**
     * 数量统计
     *
     * @return
     */
    public Map<String, Integer> getOrderNumber(int id) {
        Object[] objects = new Object[]{
                "id",id
        };
        Map<String, Integer> map = new HashMap<>();
        Response response = service.getWebservice(Api.GetState, objects, WebService.Get);
        if (response.isSuccess("200")) {
            try {
                JSONObject object = new JSONObject(response.getData());
                map.put("1", object.optInt("daipeihuo"));
                map.put("2", object.optInt("daichuku"));
                map.put("3", object.optInt("daihuishou"));
                map.put("4", object.optInt("yihuishou"));
                map.put("5", object.optInt("wancheng"));
            } catch (JSONException e) {
                e.printStackTrace();
                map.put("1", 0);
                map.put("2", 0);
                map.put("3", 0);
                map.put("4", 0);
                map.put("5", 0);
            }
        } else {
            map.put("1", 0);
            map.put("2", 0);
            map.put("3", 0);
            map.put("4", 0);
            map.put("5", 0);
        }
        return map;
    }

    public Map<String, Integer> getOrderNumber(int id, String starttime, String endtime) {
        Object[] objects = new Object[]{
                "id",id,"starttime",starttime,"endtime",endtime
        };
        Map<String, Integer> map = new HashMap<>();
        Response response = service.getWebservice(Api.GetStates, objects, WebService.Get);
        if (response.isSuccess("200")) {
            try {
                JSONObject object = new JSONObject(response.getData());
                map.put("1", object.optInt("daipeihuo"));
                map.put("2", object.optInt("daichuku"));
                map.put("3", object.optInt("daihuishou"));
                map.put("4", object.optInt("yihuishou"));
                map.put("5", object.optInt("wancheng"));
            } catch (JSONException e) {
                e.printStackTrace();
                map.put("1", 0);
                map.put("2", 0);
                map.put("3", 0);
                map.put("4", 0);
                map.put("5", 0);
            }
        } else {
            map.put("1", 0);
            map.put("2", 0);
            map.put("3", 0);
            map.put("4", 0);
            map.put("5", 0);
        }
        return map;
    }


    /**
     * 五、	根据订单状态查询订单
     *
     * @param state
     * @param page
     * @param pagesize
     * @return
     */
    public Data<Order> getOrderBystate(int uid, int state, int page, int pagesize) {
        Object[] objects = new Object[]{
                "id", uid, "state", state, "page", page, "size", pagesize
        };
        Response response = service.getWebservice(Api.GetOrderState, objects, WebService.Get);
        Data<Order> data = new Data<>();
        List<Order> list = new ArrayList<>();
        if (response.isSuccess("200")) {
            try {
                Object[] ob = getDataFromJson(response.getData());
                list = gson.fromJson(ob[2].toString(), new TypeToken<List<Order>>() {
                }.getType());
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
     * 根据订单号查询订单内容（销售端/仓库端：get）
     *
     * @param numberID
     * @return
     */
    public OrderInfoEntity getOrderInfo(String numberID) {
        Object[] objects = new Object[]{
                "ordernumber", numberID
        };
        Response response = service.getWebservice(Api.ReturnOrder, objects, WebService.Get);
        OrderInfoEntity data = new OrderInfoEntity();
        if (response.isSuccess("200")) {
            data = gson.fromJson(response.getData(), new TypeToken<OrderInfoEntity>() {
            }.getType());
        }
        return data;
    }

    /**
     * 十三、	已回库（销售端：get）
     * @param handler
     * @param orderNumber
     * @param rent
     * @param lossRent
     */
    public void returnOrder(Handler handler,String orderNumber,String rent,String lossRent) {
        Object[] objects = new Object[]{
                "ordernumber", orderNumber,"rent",rent,"actual_loss_rent",lossRent
        };
        Response response = service.getWebservice(Api.SubmitReturn, objects, WebService.Get);
        OrderInfoEntity data = new OrderInfoEntity();
        if (response.isSuccess("200")) {
           Message msg=new Message();
           msg.what=102;
           msg.obj=response.getMessage();
           handler.sendMessage(msg);
        }else {
            Message msg=new Message();
            msg.what=-1;
            msg.obj=response.getMessage();
            handler.sendMessage(msg);
        }
    }

    /**
     * 十六、	鲜花确认配送（仓库端：get）
     * @param handler
     * @param id
     */
    public void confirmDelivery(Handler handler,int id) {
        Object[] objects = new Object[]{
                "id", id
        };
        Response response = service.getWebservice(Api.IsFlowerTrue, objects, WebService.Get);
        if (response.isSuccess("200")) {
           Message msg=new Message();
           msg.obj=response.getMessage();
           msg.what=101;
           handler.sendMessage(msg);
        }else {
            Message msg=new Message();
            msg.obj=response.getMessage();
            msg.what=-1;
            handler.sendMessage(msg);
        }
    }

    /**
     * 十七、	待配货提交（仓库端：get）
     * @param handler
     * @param orderId
     */
    public void submitDelivery(Handler handler,String orderId,String remark) {
        Object[] objects = new Object[]{
                "order", orderId,"cremark",remark
        };
        Response response = service.getWebservice(Api.WatingSubmit, objects, WebService.Get);
        if (response.isSuccess("200")) {
            Message msg=new Message();
            msg.obj=response.getMessage();
            msg.what=102;
            handler.sendMessage(msg);
        }else {
            Message msg=new Message();
            msg.obj=response.getMessage();
            msg.what=-1;
            handler.sendMessage(msg);
        }
    }

    /**
     *十八、	确认出库 （仓库端：get）
     * @param handler
     * @param orderId
     */
    public void warehouseOut(Handler handler,String orderId,String customer_name,JSONArray imags) {
        Object[] objects = new Object[]{
                "order", orderId,"customer_name",customer_name,"list",imags
        };
        Response response = service.getWebservice(Api.WarehouseOut, objects, WebService.Post);
        if (response.isSuccess("200")) {
            Message msg=new Message();
            msg.obj=response.getMessage();
            msg.what=103;
            handler.sendMessage(msg);
        }else {
            Message msg=new Message();
            msg.obj=response.getMessage();
            msg.what=-1;
            handler.sendMessage(msg);
        }
    }

    /**
     *
     * @param handler
     * @param orderId
     * @param customer_name
     * @param list
     */
    public void tobeReturned(Handler handler,String orderId,String customer_name,JSONArray list) {
        Object[] objects = new Object[]{
                "order", orderId,"customer_name",customer_name,"list",list
        };
        Response response = service.getWebservice(Api.Recover, objects, WebService.Post);
        if (response.isSuccess("200")) {
            Message msg=new Message();
            msg.obj=response.getMessage();
            msg.what=101;
            handler.sendMessage(msg);
        }else {
            Message msg=new Message();
            msg.obj=response.getMessage();
            msg.what=-1;
            handler.sendMessage(msg);
        }
    }

    /**
     * 二十一、	填写回收损耗数据（仓库端：post）
     * @param handler
     * @param orderId
     * @param list
     */
    public void Returned(Handler handler, String orderId, JSONArray list) {
        Object[] objects = new Object[]{
                "order", orderId,"list",list
        };
        Response response = service.getWebservice(Api.Writeloss, objects, WebService.Post);
        if (response.isSuccess("200")) {
            Message msg=new Message();
            msg.obj=response.getMessage();
            msg.what=102;
            handler.sendMessage(msg);
        }else {
            Message msg=new Message();
            msg.obj=response.getMessage();
            msg.what=-1;
            handler.sendMessage(msg);
        }
    }

    public void uploadFile(Handler handler, File file) {
        Response response = service.upload(Api.ImgUpload, file);
        if (response.isSuccess("200")) {
            Message msg=new Message();
            msg.obj=response.getData();
            msg.what=102;
            handler.sendMessage(msg);
        }else {
            Message msg=new Message();
            msg.obj=response.getMessage();
            msg.what=-1;
            handler.sendMessage(msg);
        }
    }



    /**
     * 销售端下单
     * @param handler
     * @param customerName
     * @param hotelAddress
     * @param customerPhone
     * @param weddingDate
     * @param deliveryTime
     * @param recoveryDate
     * @param remark
     * @param numberId
     * @param money
     * @param list
     */
    public void placingOrder(Handler handler, String customerName, String hotelAddress, String customerPhone, String weddingDate,
                             String deliveryTime, String recoveryDate, String remark, int numberId, float money, String list){
        Object[] objects = new Object[]{
                "customer_name",customerName,"hotel_address",hotelAddress,"customer_phone",customerPhone,
                "wedding_date",weddingDate,"delivery_time",deliveryTime,"recovery_date",recoveryDate,"remark",remark,"number_id",numberId,
                "money",money,"list",list
        };
        Response response = service.getWebservice(Api.PlacingOrder,objects,WebService.Post);
        Message msg = new Message();
        if(response.isSuccess("200")){
            msg.what = 0;
        }else{
            msg.what = -1;
        }
        msg.obj = response.getMessage();
        handler.sendMessage(msg);
    }

    /**
     * 修改订单
     * @param handler
     * @param ordernumber
     * @param customerName
     * @param hotelAddress
     * @param customerPhone
     * @param weddingDate
     * @param deliveryTime
     * @param recoveryDate
     * @param remark
     * @param numberId
     * @param money
     * @param list
     */
    public void updateOrder(Handler handler, String ordernumber, String customerName, String hotelAddress, String customerPhone, String weddingDate,
                             String deliveryTime, String recoveryDate, String remark, int numberId, float money, String list){
        Object[] objects = new Object[]{
                "ordernumber",ordernumber,"customer_name",customerName,"hotel_address",hotelAddress,"customer_phone",customerPhone,
                "wedding_date",weddingDate,"delivery_time",deliveryTime,"recovery_date",recoveryDate,"remark",remark,"number_id",numberId,
                "money",money,"list",list
        };
        Response response = service.getWebservice(Api.UpdateOrder,objects,WebService.Post);
        Message msg = new Message();
        if(response.isSuccess("200")){
            msg.what = 0;
            msg.arg1=Integer.valueOf(response.getData());
        }else{
            msg.what = -1;
        }
        msg.obj = response.getMessage();
        handler.sendMessage(msg);
    }

    /**
     * 追加订单
     * @param handler
     * @param ordernumber
     * @param money
     * @param list
     */
    public void additionalOrder(Handler handler, String ordernumber, float money, String list){
        Object[] objects = new Object[]{
                "ordernumber",ordernumber,
                "money",money,"list",list
        };
        Response response = service.getWebservice(Api.AdditionalOrder,objects,WebService.Post);
        Message msg = new Message();
        if(response.isSuccess("200")){
            msg.what = 0;
        }else{
            msg.what = -1;
        }
        msg.obj = response.getMessage();
        handler.sendMessage(msg);
    }

    /**
     * 删除订单中的花
     * @param handler
     * @param ordernumber （订单号）
     * @param id 花id）
     */
    public void delete(Handler handler, String ordernumber, int id){
        Object[] objects = new Object[]{
                "Order",ordernumber,"id",id
        };
        Response response = service.getWebservice(Api.Delete,objects,WebService.Get);
        Message msg = new Message();
        if(response.isSuccess("200")){
            msg.what = 2;
        }else{
            msg.what = -2;
        }
        msg.obj = response.getMessage();
        handler.sendMessage(msg);
    }

    /**
     * 根据订单号获取订单详情
     * @param handler
     * @param no
     * @return
     */
    public Order getOrderByNo(Handler handler,String no){
        Order order = null;
        Object[] objects = new Object[]{
                "ordernumber", no
        };
        Response response = service.getWebservice(Api.ReturnOrder, objects, WebService.Get);
        if (response.isSuccess("200")) {
            order = gson.fromJson(response.getData(), new TypeToken<Order>() {}.getType());
        }
        return order;
    }

    /**
     * 销售端  根据订单号获取订单详情
     * @param handler
     * @param no
     * @return
     */
    public OrderInfoXsEntity getOrderInfoXsByNo(Handler handler, String no){
        OrderInfoXsEntity order = null;
        Object[] objects = new Object[]{
                "ordernumber", no
        };
        Response response = service.getWebservice(Api.GetDetail, objects, WebService.Get);
        if (response.isSuccess("200")) {
            order = gson.fromJson(response.getData(), new TypeToken<OrderInfoXsEntity>() {}.getType());
        }
        return order;
    }

    public void SetSign(Handler handler,int id,int sign,int number){
        Object[] objects = new Object[]{
                "id", id,"sign",sign,"count",number
        };
        Response response = service.getWebservice(Api.SetSign, objects, WebService.Get);
        Message message = new Message();
        if(response.isSuccess("200")){
            message.what = 10;
        }else{
            message.what = -1;
        }
        message.obj = response.getMessage();
        handler.sendMessage(message);

    }
}
