package com.example.user.toy.home.entity;

public class Img {
    private int id;
    private String src;
    private int toyId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public int getToyId() {
        return toyId;
    }

    public void setToyId(int toyId) {
        this.toyId = toyId;
    }

    @Override
    public String toString() {
        return "Img{" +
                "id=" + id +
                ", src='" + src + '\'' +
                ", toyId=" + toyId +
                '}';
    }

}
