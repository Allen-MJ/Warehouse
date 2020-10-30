package cn.allen.warehouse.entry;

import java.io.File;
import java.io.Serializable;

public class ImageEntity implements Serializable {
    private File file;
    /**
     * isImg : true
     * mess : https://net-hot-factory.oss-cn-beijing.aliyuncs.com/Register/image/20200930/1113067645.jpg
     */

    private boolean isImg;
    private String mess;
    private int pan;

    public int getPan() {
        return pan;
    }

    public void setPan(int pan) {
        this.pan = pan;
    }

    public boolean isImg() {
        return isImg;
    }

    public void setImg(boolean img) {
        isImg = img;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean isIsImg() {
        return isImg;
    }

    public void setIsImg(boolean isImg) {
        this.isImg = isImg;
    }

    public String getMess() {
        return mess;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }
}
