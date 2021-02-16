package com.example.user.toy.personal.entity;

public class Order {
    private int id;
    private int toyId;
    private int renterId;
    private String deadline;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getToyId() {
        return toyId;
    }

    public void setToyId(int toyId) {
        this.toyId = toyId;
    }

    public int getRenterId() {
        return renterId;
    }

    public void setRenterId(int renterId) {
        this.renterId = renterId;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", toyId=" + toyId +
                ", renterId=" + renterId +
                ", deadline='" + deadline + '\'' +
                '}';
    }
}
