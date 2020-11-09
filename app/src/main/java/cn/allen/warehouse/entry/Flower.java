package cn.allen.warehouse.entry;

import java.io.Serializable;

import allen.frame.tools.StringUtils;

public class Flower implements Serializable {
    private int id;
    private int flower_id;
    private String name;
    private String flower_name;
    private int stock;//库存
    private int scheduled_quantity;//预定数量
    private int big_id;
    private int small_id;
    private float rent;
    private float rent_loss;
    private String img;
    private int warehouse;
    private int loss_quantity;
    private String createtime;
    private int state;
    private int zd1;
    private String zd2;
    private float unit_price;
    private String addtimes;
    private String typename;
    private String warehousename;

    public Flower() {
    }

    @Override
    public String toString() {
        return "Flower{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", stock=" + stock +
                ", big_id=" + big_id +
                ", small_id=" + small_id +
                ", rent=" + rent +
                ", rent_loss=" + rent_loss +
                ", img='" + img + '\'' +
                ", warehouse=" + warehouse +
                ", loss_quantity=" + loss_quantity +
                ", createtime='" + createtime + '\'' +
                ", state=" + state +
                ", zd1=" + zd1 +
                ", zd2='" + zd2 + '\'' +
                ", unit_price=" + unit_price +
                ", addtimes='" + addtimes + '\'' +
                ", typename='" + typename + '\'' +
                ", warehousename='" + warehousename + '\'' +
                '}';
    }

    public int getScheduled_quantity() {
        return scheduled_quantity;
    }

    public void setScheduled_quantity(int scheduled_quantity) {
        this.scheduled_quantity = scheduled_quantity;
    }

    public int getFlower_id() {
        return flower_id;
    }

    public void setFlower_id(int flower_id) {
        this.flower_id = flower_id;
    }

    public String getFlower_name() {
        return flower_name;
    }

    public void setFlower_name(String flower_name) {
        this.flower_name = flower_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getBig_id() {
        return big_id;
    }

    public void setBig_id(int big_id) {
        this.big_id = big_id;
    }

    public int getSmall_id() {
        return small_id;
    }

    public void setSmall_id(int small_id) {
        this.small_id = small_id;
    }

    public float getRent() {
        return rent;
    }

    public void setRent(float rent) {
        this.rent = rent;
    }

    public float getRent_loss() {
        return rent_loss;
    }

    public void setRent_loss(float rent_loss) {
        this.rent_loss = rent_loss;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(int warehouse) {
        this.warehouse = warehouse;
    }

    public int getLoss_quantity() {
        return loss_quantity;
    }

    public void setLoss_quantity(int loss_quantity) {
        this.loss_quantity = loss_quantity;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = StringUtils.null2Empty(createtime).replaceAll("T"," ");
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
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

    public float getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(int unit_price) {
        this.unit_price = unit_price;
    }

    public String getAddtimes() {
        return addtimes;
    }

    public void setAddtimes(String addtimes) {
        this.addtimes = StringUtils.null2Empty(addtimes).replaceAll("T"," ");
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getWarehousename() {
        return warehousename;
    }

    public void setWarehousename(String warehousename) {
        this.warehousename = warehousename;
    }
}
