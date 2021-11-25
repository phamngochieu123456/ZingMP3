package com.example.zingmp3fake.Model;

import android.net.Uri;

import java.io.Serializable;

public class Music implements Serializable {
    private Source source;

    public Source getSource() {
        return source;
    }

    public Music(Source source) {
        this.source = source;
    }

    public void setSource(Source source) {
        this.source = source;
    }
}
