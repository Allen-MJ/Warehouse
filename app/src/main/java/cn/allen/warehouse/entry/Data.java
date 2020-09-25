package cn.allen.warehouse.entry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Data<T> implements Serializable {
    private int count;//总数
    private int total;//页数
    private List<T> list;

    public Data() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getList() {
        if(list==null){
            return new ArrayList<>();
        }
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
