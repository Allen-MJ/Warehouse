package cn.allen.warehouse.entry;

import android.graphics.Color;

import java.io.Serializable;

import cn.allen.warehouse.R;

public class ShowOrder implements Serializable {
    private int id;
    private String name;
    private int count;
    private int color;
    private int redId;

    public ShowOrder() {
    }

    public ShowOrder(int id, String name, int count) {
        this.id = id;
        this.name = name;
        this.count = count;
    }

    @Override
    public String toString() {
        return "ShowOrder{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", count=" + count +
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getColor() {
        switch (id){
            case 0:
                color = Color.parseColor("#F57373");
                break;
            case 1:
                color = Color.parseColor("#FFD214");
                break;
            case 2:
                color = Color.parseColor("#4EE2C6");
                break;
            case 3:
                color = Color.parseColor("#67B3FF");
                break;
            case 4:
                color = Color.parseColor("#68D6A8");
                break;
            case 5:
                color = Color.parseColor("#FB3E7E");
                break;
        }
        return color;
    }

    public int getRedId() {
        switch (id){
            case 0:
                redId = R.mipmap.ic_logo_36;
                break;
            case 1:
                redId = R.mipmap.ic_logo_35;
                break;
            case 2:
                redId = R.mipmap.ic_logo_33;
                break;
            case 3:
                redId = R.mipmap.ic_logo_34;
                break;
            case 4:
                redId = R.mipmap.ic_logo_38;
                break;
            case 5:
                redId = R.mipmap.ic_logo_37;
                break;
        }
        return redId;
    }

    public String getStatus(int process) {
        String status = "";
        switch (process){
            case 1:
                status = "待配货";
                break;
            case 2:
                status = "待出库";
                break;
            case 3:
                status = "待回收";
                break;
            case 4:
                status = "已回收";
                break;
            case 5:
                status = "完成清点";
                break;
        }
        return status;
    }
}
