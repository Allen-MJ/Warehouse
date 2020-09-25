package cn.allen.warehouse.entry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FlowerType implements Serializable {
    private int id;
    private String name;
    private String createtime;
    private String addtimes;
    private List<FlowerType> children;

    public FlowerType() {
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

    public List<FlowerType> getChildren() {
        if(children==null){
            children = new ArrayList<>();
        }
        return children;
    }

    public void setChildren(List<FlowerType> children) {
        this.children = children;
    }
}
