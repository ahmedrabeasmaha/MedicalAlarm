package com.example.medicinealarm;

import android.graphics.Bitmap;

public class Model {
    private String name;
    private Bitmap image;

    public Model(String name, Bitmap image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public Bitmap getImage() {
        return image;
    }

}