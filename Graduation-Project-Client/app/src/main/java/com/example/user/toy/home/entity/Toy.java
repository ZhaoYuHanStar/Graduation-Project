package com.example.user.toy.home.entity;

import java.util.Set;

public class Toy {
    private int id;
    private String ageId;
    private String sex;
    private int branchId;
    private int typeId;
    private int userId;
    private int state;
    private String produce;
    private Set<Img> images;
    private String price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAgeId() {
        return ageId;
    }

    public void setAgeId(String ageId) {
        this.ageId = ageId;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getProduce() {
        return produce;
    }

    public void setProduce(String produce) {
        this.produce = produce;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Set<Img> getImages() {
        return images;
    }

    public void setImages(Set<Img> images) {
        this.images = images;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Toy{" +
                "id=" + id +
                ", ageId='" + ageId + '\'' +
                ", sex='" + sex + '\'' +
                ", branchId=" + branchId +
                ", typeId=" + typeId +
                ", userId=" + userId +
                ", state=" + state +
                ", produce='" + produce + '\'' +
                ", images=" + images +
                ", price='" + price + '\'' +
                '}';
    }
}
