package com.example.medicinealarm;

import android.graphics.Bitmap;

public class Model {
    private String name;
    private String tag;
    private Bitmap image;

    public Model(String name, String tag, Bitmap image) {
        this.name = name;
        this.tag = tag;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public Bitmap getImage() {
        return image;
    }

    public String  getTag() {
        return tag;
    }

}