package com.example.zingmp3fake.Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;

public class Source implements Serializable {
    private String Url;
    private FileInputStream bytes;

    public Source() {

    }

    public String getUrl() {
        return Url;
    }

    public Source(String url, FileInputStream bytes) {
        Url = url;
        this.bytes = bytes;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public FileInputStream getBytes() {
        return bytes;
    }

    public void setBytes(FileInputStream bytes) {
        this.bytes = bytes;
    }
}
