package cn.allen.warehouse.entry;

import java.io.File;
import java.io.Serializable;

public class ImageEntity implements Serializable {
    private String url;
    private File file;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
