package com.example.user.toy.home.entity;

import java.util.Set;

public class HomeListItemBean {

    public String produce;
    public String age;
    public String price;
    public String type;
    //列表的展示图片
    public String showImg;
    //图片集
    public Set<Img> images;
    public int id;

    public String getProduce() {
        return produce;
    }

    public void setProduce(String produce) {
        this.produce = produce;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getShowImg() {
        return showImg;
    }

    public void setShowImg(String showImg) {
        this.showImg = showImg;
    }

    public Set<Img> getImages() {
        return images;
    }

    public void setImages(Set<Img> images) {
        this.images = images;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "HomeListItemBean{" +
                "produce='" + produce + '\'' +
                ", age='" + age + '\'' +
                ", price='" + price + '\'' +
                ", type='" + type + '\'' +
                ", showImg='" + showImg + '\'' +
                ", images=" + images +
                ", id=" + id +
                '}';
    }
}
