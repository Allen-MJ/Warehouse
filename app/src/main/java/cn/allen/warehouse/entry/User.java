package cn.allen.warehouse.entry;

import java.io.Serializable;

import allen.frame.tools.StringUtils;

public class User implements Serializable {
    private int id;
    private String name;
    private String account;
    private String password;
    private String createtime;
    private String phone;
    private String warehouse;//所属仓库
    private int type;//0为仓库管理员  1为销售员

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", createtime='" + createtime + '\'' +
                ", phone='" + phone + '\'' +
                ", warehouse='" + warehouse + '\'' +
                ", type=" + type +
                '}';
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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = StringUtils.null2Empty(createtime).replaceAll("T"," ");
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
