package com.yoho.myapplication;

public class ListviewPojo {
    String name;
    int images;

    public ListviewPojo(String name, int images) {

        this.name = name;
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImages() {
        return images;
    }

    public void setImages(int images) {
        this.images = images;
    }
}
