package cn.allen.warehouse.entry;

import java.io.Serializable;
import java.util.List;

import allen.frame.tools.StringUtils;

public class OrderInfoEntity implements Serializable {

    /**
     * id : 1
     * customer_name : 大兰
     * hotel_address : 重庆地址
     * customer_phone : 18000000000
     * wedding_date : 2020-09-06T00:00:00
     * delivery_time : 2020-09-07T00:00:00
     * recovery_date : 2020-09-08T00:00:00
     * remark : 要好
     * rent : 34.0
     * order_number : J1232
     * order_createtime : 2020-09-17T14:47:28
     * actual_rent : 0.0
     * actual_loss_rent : 45.0
     * collect_ornot : 0
     * collect_amount : 67.0
     * collect_date : 2020-09-09
     * collect_status : 1
     * img : null
     * zd1 : 0
     * zd2 : 1
     * order_process : 1
     * order_son : null
     * addtimes : 2020-09-17 02:47:28
     * wedding_dates : 2020-09-06 12:00:00
     * delivery_times : 2020-09-07 12:00:00
     * recovery_dates : 2020-09-08 12:00:00
     * number_id : 2
     * children : [{"id":2,"order_id":"J12321","flower_name":"玫瑰花","stock":100,"scheduled_quantity":12,"rent":12,"loss_quantity":10,"loss_rent":23,"createtime":"2020-09-12T12:12:12","main_ornot":1,"zd1":0,"zd2":null,"addtimes":null,"order_number":null,"id_check":0,"flower_id":1}]
     * mainchildren : [{"id":1,"order_id":"J1232","flower_name":"玫瑰花","stock":100,"scheduled_quantity":12,"rent":12,"loss_quantity":10,"loss_rent":23,"createtime":"2020-09-12T12:12:12","main_ornot":0,"zd1":0,"zd2":null,"addtimes":null,"order_number":null,"id_check":1,"flower_id":1}]
     * Images : [{"id":1,"order_id":"J1232","img":"https://net-hot-factory.oss-cn-beijing.aliyuncs.com/Register/image/20200910/1906422770.jpg","createtime":"1753-01-01T00:00:00","addtimes":null,"customer_name":"客户姓名"}]
     * number_name : 小黑黑
     */

    private int id;
    private String customer_name;
    private String hotel_address;
    private String customer_phone;
    private String wedding_date;
    private String delivery_time;
    private String recovery_date;
    private String remark;
    private String cremark;
    private double rent;
    private String order_number;
    private String order_createtime;
    private double actual_rent;
    private double actual_loss_rent;
    private int collect_ornot;
    private double collect_amount;
    private String collect_date;
    private int collect_status;
    private Object img;
    private int zd1;
    private String zd2;
    private int order_process;
    private Object order_son;
    private String addtimes;
    private String wedding_dates;
    private String delivery_times;
    private String recovery_dates;
    private int number_id;
    private int is_ornot;
    private String number_name;
    private List<ChildrenBean> children;
    private List<MainchildrenBean> mainchildren;
    private List<ImagesBean> Images;

    public int getIs_ornot() {
        return is_ornot;
    }

    public void setIs_ornot(int is_ornot) {
        this.is_ornot = is_ornot;
    }

    public String getCremark() {
        return cremark;
    }

    public void setCremark(String cremark) {
        this.cremark = cremark;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getHotel_address() {
        return hotel_address;
    }

    public void setHotel_address(String hotel_address) {
        this.hotel_address = hotel_address;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

    public String getWedding_date() {
        return wedding_date;
    }

    public void setWedding_date(String wedding_date) {
        this.wedding_date = StringUtils.null2Empty(wedding_date).replaceAll("T"," ");
    }

    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = StringUtils.null2Empty(delivery_time).replaceAll("T"," ");
    }

    public String getRecovery_date() {
        return recovery_date;
    }

    public void setRecovery_date(String recovery_date) {
        this.recovery_date = StringUtils.null2Empty(recovery_date).replaceAll("T"," ");
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public double getRent() {
        return rent;
    }

    public void setRent(double rent) {
        this.rent = rent;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getOrder_createtime() {
        return order_createtime;
    }

    public void setOrder_createtime(String order_createtime) {
        this.order_createtime = StringUtils.null2Empty(order_createtime).replaceAll("T"," ");
    }

    public double getActual_rent() {
        return actual_rent;
    }

    public void setActual_rent(double actual_rent) {
        this.actual_rent = actual_rent;
    }

    public double getActual_loss_rent() {
        return actual_loss_rent;
    }

    public void setActual_loss_rent(double actual_loss_rent) {
        this.actual_loss_rent = actual_loss_rent;
    }

    public int getCollect_ornot() {
        return collect_ornot;
    }

    public void setCollect_ornot(int collect_ornot) {
        this.collect_ornot = collect_ornot;
    }

    public double getCollect_amount() {
        return collect_amount;
    }

    public void setCollect_amount(double collect_amount) {
        this.collect_amount = collect_amount;
    }

    public String getCollect_date() {
        return collect_date;
    }

    public void setCollect_date(String collect_date) {
        this.collect_date = StringUtils.null2Empty(collect_date).replaceAll("T"," ");
    }

    public int getCollect_status() {
        return collect_status;
    }

    public void setCollect_status(int collect_status) {
        this.collect_status = collect_status;
    }

    public Object getImg() {
        return img;
    }

    public void setImg(Object img) {
        this.img = img;
    }

    public int getZd1() {
        return zd1;
    }

    public void setZd1(int zd1) {
        this.zd1 = zd1;
    }

    public String getZd2() {
        return zd2;
    }

    public void setZd2(String zd2) {
        this.zd2 = zd2;
    }

    public int getOrder_process() {
        return order_process;
    }

    public void setOrder_process(int order_process) {
        this.order_process = order_process;
    }

    public Object getOrder_son() {
        return order_son;
    }

    public void setOrder_son(Object order_son) {
        this.order_son = order_son;
    }

    public String getAddtimes() {
        return addtimes;
    }

    public void setAddtimes(String addtimes) {
        this.addtimes = StringUtils.null2Empty(addtimes).replaceAll("T"," ");
    }

    public String getWedding_dates() {
        return wedding_dates;
    }

    public void setWedding_dates(String wedding_dates) {
        this.wedding_dates = StringUtils.null2Empty(wedding_dates).replaceAll("T"," ");
    }

    public String getDelivery_times() {
        return delivery_times;
    }

    public void setDelivery_times(String delivery_times) {
        this.delivery_times = StringUtils.null2Empty(delivery_times).replaceAll("T"," ");
    }

    public String getRecovery_dates() {
        return recovery_dates;
    }

    public void setRecovery_dates(String recovery_dates) {
        this.recovery_dates = StringUtils.null2Empty(recovery_dates).replaceAll("T"," ");
    }

    public int getNumber_id() {
        return number_id;
    }

    public void setNumber_id(int number_id) {
        this.number_id = number_id;
    }

    public String getNumber_name() {
        return number_name;
    }

    public void setNumber_name(String number_name) {
        this.number_name = number_name;
    }

    public List<ChildrenBean> getChildren() {
        return children;
    }

    public void setChildren(List<ChildrenBean> children) {
        this.children = children;
    }

    public List<MainchildrenBean> getMainchildren() {
        return mainchildren;
    }

    public void setMainchildren(List<MainchildrenBean> mainchildren) {
        this.mainchildren = mainchildren;
    }

    public List<ImagesBean> getImages() {
        return Images;
    }

    public void setImages(List<ImagesBean> Images) {
        this.Images = Images;
    }

    public static class ChildrenBean implements Serializable{
        /**
         * id : 2
         * order_id : J12321
         * flower_name : 玫瑰花
         * stock : 100
         * scheduled_quantity : 12
         * rent : 12.0
         * loss_quantity : 10
         * loss_rent : 23.0
         * createtime : 2020-09-12T12:12:12
         * main_ornot : 1
         * zd1 : 0
         * zd2 : null
         * addtimes : null
         * order_number : null
         * id_check : 0
         * flower_id : 1
         */

        private int id;
        private String order_id;
        private String flower_name;
        private int stock;
        private int scheduled_quantity;
        private double rent;
        private int loss_quantity;
        private double loss_rent;
        private String createtime;
        private int main_ornot;
        private int zd1;
        private Object zd2;
        private Object addtimes;
        private Object order_number;
        private int id_check;
        private int flower_id;
        private int sign;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getFlower_name() {
            return flower_name;
        }

        public void setFlower_name(String flower_name) {
            this.flower_name = flower_name;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public int getScheduled_quantity() {
            return scheduled_quantity;
        }

        public void setScheduled_quantity(int scheduled_quantity) {
            this.scheduled_quantity = scheduled_quantity;
        }

        public double getRent() {
            return rent;
        }

        public void setRent(double rent) {
            this.rent = rent;
        }

        public int getLoss_quantity() {
            return loss_quantity;
        }

        public void setLoss_quantity(int loss_quantity) {
            this.loss_quantity = loss_quantity;
        }

        public double getLoss_rent() {
            return loss_rent;
        }

        public void setLoss_rent(double loss_rent) {
            this.loss_rent = loss_rent;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = StringUtils.null2Empty(createtime).replaceAll("T"," ");
        }

        public int getMain_ornot() {
            return main_ornot;
        }

        public void setMain_ornot(int main_ornot) {
            this.main_ornot = main_ornot;
        }

        public int getZd1() {
            return zd1;
        }

        public void setZd1(int zd1) {
            this.zd1 = zd1;
        }

        public Object getZd2() {
            return zd2;
        }

        public void setZd2(Object zd2) {
            this.zd2 = zd2;
        }

        public Object getAddtimes() {
            return addtimes;
        }

        public void setAddtimes(String addtimes) {
            this.addtimes = StringUtils.null2Empty(addtimes).replaceAll("T"," ");
        }

        public Object getOrder_number() {
            return order_number;
        }

        public void setOrder_number(Object order_number) {
            this.order_number = order_number;
        }

        public int getId_check() {
            return id_check;
        }

        public void setId_check(int id_check) {
            this.id_check = id_check;
        }

        public int getFlower_id() {
            return flower_id;
        }

        public void setFlower_id(int flower_id) {
            this.flower_id = flower_id;
        }

        public int getSign() {
            return sign;
        }

        public void setSign(int sign) {
            this.sign = sign;
        }
    }

    public static class MainchildrenBean implements Serializable{
        /**
         * id : 1
         * order_id : J1232
         * flower_name : 玫瑰花
         * stock : 100
         * scheduled_quantity : 12
         * rent : 12.0
         * loss_quantity : 10
         * loss_rent : 23.0
         * createtime : 2020-09-12T12:12:12
         * main_ornot : 0
         * zd1 : 0
         * zd2 : null
         * addtimes : null
         * order_number : null
         * id_check : 1
         * flower_id : 1
         */

        private int id;
        private String order_id;
        private String flower_name;
        private int stock;
        private int scheduled_quantity;
        private double rent;
        private int loss_quantity;
        private double loss_rent;
        private String createtime;
        private int main_ornot;
        private int zd1;
        private Object zd2;
        private String addtimes;
        private Object order_number;
        private int id_check;
        private int flower_id;
        private int sign;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getFlower_name() {
            return flower_name;
        }

        public void setFlower_name(String flower_name) {
            this.flower_name = flower_name;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public int getScheduled_quantity() {
            return scheduled_quantity;
        }

        public void setScheduled_quantity(int scheduled_quantity) {
            this.scheduled_quantity = scheduled_quantity;
        }

        public double getRent() {
            return rent;
        }

        public void setRent(double rent) {
            this.rent = rent;
        }

        public int getLoss_quantity() {
            return loss_quantity;
        }

        public void setLoss_quantity(int loss_quantity) {
            this.loss_quantity = loss_quantity;
        }

        public double getLoss_rent() {
            return loss_rent;
        }

        public void setLoss_rent(double loss_rent) {
            this.loss_rent = loss_rent;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = StringUtils.null2Empty(createtime).replaceAll("T"," ");
        }

        public int getMain_ornot() {
            return main_ornot;
        }

        public void setMain_ornot(int main_ornot) {
            this.main_ornot = main_ornot;
        }

        public int getZd1() {
            return zd1;
        }

        public void setZd1(int zd1) {
            this.zd1 = zd1;
        }

        public Object getZd2() {
            return zd2;
        }

        public void setZd2(Object zd2) {
            this.zd2 = zd2;
        }

        public String getAddtimes() {
            return addtimes;
        }

        public void setAddtimes(String addtimes) {
            this.addtimes = StringUtils.null2Empty(addtimes).replaceAll("T"," ");
        }

        public Object getOrder_number() {
            return order_number;
        }

        public void setOrder_number(Object order_number) {
            this.order_number = order_number;
        }

        public int getId_check() {
            return id_check;
        }

        public void setId_check(int id_check) {
            this.id_check = id_check;
        }

        public int getFlower_id() {
            return flower_id;
        }

        public void setFlower_id(int flower_id) {
            this.flower_id = flower_id;
        }

        public int getSign() {
            return sign;
        }

        public void setSign(int sign) {
            this.sign = sign;
        }
    }

    public static class ImagesBean implements Serializable{
        /**
         * id : 1
         * order_id : J1232
         * img : https://net-hot-factory.oss-cn-beijing.aliyuncs.com/Register/image/20200910/1906422770.jpg
         * createtime : 1753-01-01T00:00:00
         * addtimes : null
         * customer_name : 客户姓名
         */

        private int id;
        private String order_id;
        private String img;
        private String createtime;
        private String addtimes;
        private String customer_name;
        private int pan;

        public int getPan() {
            return pan;
        }

        public void setPan(int pan) {
            this.pan = pan;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = StringUtils.null2Empty(createtime).replaceAll("T"," ");
        }

        public String getAddtimes() {
            return addtimes;
        }

        public void setAddtimes(String addtimes) {
            this.addtimes = StringUtils.null2Empty(addtimes).replaceAll("T"," ");
        }

        public String getCustomer_name() {
            return customer_name;
        }

        public void setCustomer_name(String customer_name) {
            this.customer_name = customer_name;
        }
    }
}
