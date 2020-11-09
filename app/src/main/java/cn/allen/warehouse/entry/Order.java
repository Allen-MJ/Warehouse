package cn.allen.warehouse.entry;

import java.io.Serializable;
import java.util.List;

import allen.frame.tools.StringUtils;

public class Order implements Serializable {
    private int id;
    private String customer_name;
    private String hotel_address;
    private String customer_phone;
    private String wedding_date;
    private String delivery_time;
    private String recovery_date;
    private String remark;
    private float rent;//租金
    private String order_number;
    private String order_createtime;
    private float actual_rent;//实际收的租金
    private float actual_loss_rent;//实际损耗金额
    private float collect_ornot;
    private float collect_amount;//收款金额
    private String collect_date;//收款金额
    private int collect_status;//收款状态
    private String img;//
    private int zd1;//
    private String zd2;//
    private int order_process;//订单流程  1为待配货 2为待出库 3为待回库  4为已回库  5为完成清点
    private String order_son;//子订单号
    private String addtimes;//子订单号
    private String wedding_dates;//子订单号
    private String delivery_times;//子订单号
    private String recovery_dates;//子订单号
    private int number_id;//销售id
    private String number_name;
    private List<Flower> children;
    private List<Flower> mainchildren;
    private List<Image> Images;

    public List<Flower> getChildren() {
        return children;
    }

    public void setChildren(List<Flower> children) {
        this.children = children;
    }

    public List<Flower> getMainchildren() {
        return mainchildren;
    }

    public void setMainchildren(List<Flower> mainchildren) {
        this.mainchildren = mainchildren;
    }

    public List<Image> getImages() {
        return Images;
    }

    public void setImages(List<Image> images) {
        Images = images;
    }

    public Order() {
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customer_name='" + customer_name + '\'' +
                ", hotel_address='" + hotel_address + '\'' +
                ", customer_phone='" + customer_phone + '\'' +
                ", wedding_date='" + wedding_date + '\'' +
                ", delivery_time='" + delivery_time + '\'' +
                ", recovery_date='" + recovery_date + '\'' +
                ", remark='" + remark + '\'' +
                ", rent=" + rent +
                ", order_number='" + order_number + '\'' +
                ", order_createtime='" + order_createtime + '\'' +
                ", actual_rent=" + actual_rent +
                ", actual_loss_rent=" + actual_loss_rent +
                ", collect_ornot=" + collect_ornot +
                ", collect_amount=" + collect_amount +
                ", collect_date='" + collect_date + '\'' +
                ", collect_status=" + collect_status +
                ", img='" + img + '\'' +
                ", zd1=" + zd1 +
                ", zd2='" + zd2 + '\'' +
                ", order_process=" + order_process +
                ", order_son='" + order_son + '\'' +
                ", addtimes='" + addtimes + '\'' +
                ", wedding_dates='" + wedding_dates + '\'' +
                ", delivery_times='" + delivery_times + '\'' +
                ", recovery_dates='" + recovery_dates + '\'' +
                ", number_id=" + number_id +
                '}';
    }

    public String getNumber_name() {
        return number_name;
    }

    public void setNumber_name(String number_name) {
        this.number_name = number_name;
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

    public float getRent() {
        return rent;
    }

    public void setRent(float rent) {
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

    public float getActual_rent() {
        return actual_rent;
    }

    public void setActual_rent(int actual_rent) {
        this.actual_rent = actual_rent;
    }

    public float getActual_loss_rent() {
        return actual_loss_rent;
    }

    public void setActual_loss_rent(int actual_loss_rent) {
        this.actual_loss_rent = actual_loss_rent;
    }

    public float getCollect_ornot() {
        return collect_ornot;
    }

    public void setCollect_ornot(int collect_ornot) {
        this.collect_ornot = collect_ornot;
    }

    public float getCollect_amount() {
        return collect_amount;
    }

    public void setCollect_amount(int collect_amount) {
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
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

    public String getOrder_son() {
        return order_son;
    }

    public void setOrder_son(String order_son) {
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

    class Image implements Serializable{
        private int id;
        private String order_id;
        private String img;
        private String createtime;
        private String addtimes;
        private String customer_name;

        public Image() {
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
            this.createtime = createtime;
        }

        public String getAddtimes() {
            return addtimes;
        }

        public void setAddtimes(String addtimes) {
            this.addtimes = addtimes;
        }

        public String getCustomer_name() {
            return customer_name;
        }

        public void setCustomer_name(String customer_name) {
            this.customer_name = customer_name;
        }
    }
}
