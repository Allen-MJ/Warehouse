package cn.allen.warehouse.entry;

import java.io.Serializable;

public class Notice implements Serializable {
    private int id;
    private String content;
    private String order_id;
    private String createtime;
    private String addtimes;

    public Notice() {
    }

    @Override
    public String toString() {
        return "Notice{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", order_id='" + order_id + '\'' +
                ", createtime='" + createtime + '\'' +
                ", addtimes='" + addtimes + '\'' +
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
        this.createtime = createtime;
    }

    public String getAddtimes() {
        return addtimes;
    }

    public void setAddtimes(String addtimes) {
        this.addtimes = addtimes;
    }
}
