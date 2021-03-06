package cn.allen.warehouse.entry;

import java.io.Serializable;

import allen.frame.tools.StringUtils;

public class Notice implements Serializable {
    private int id;
    private String content;
    private String order_id;
    private String customer_name;
    private String createtime;
    private String addtimes;
    private int order_process;

    public Notice() {
    }

    @Override
    public String toString() {
        return "Notice{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", order_id='" + order_id + '\'' +
                ", customer_name='" + customer_name + '\'' +
                ", createtime='" + createtime + '\'' +
                ", addtimes='" + addtimes + '\'' +
                ", order_process=" + order_process +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
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

    public int getOrder_process() {
        return order_process;
    }

    public void setOrder_process(int order_process) {
        this.order_process = order_process;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }
}
