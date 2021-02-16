package com.example.user.toy.home.entity;

public class Toy {
    private int id;
    private String ageId;
    private String sex;
    private int branchId;
    private int typeId;
    private int userId;
    private int state;

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
                '}';
    }
}
