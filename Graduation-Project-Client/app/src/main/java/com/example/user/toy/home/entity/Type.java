package com.example.user.toy.home.entity;

public class Type {
    /**
     * 1. 益智玩具
     * 2. 动手玩具
     * 3. 装饰玩具
     * 4. 机械玩具
     * 5. 图纸玩具
     * 6. 声音玩具
     */

    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Type{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }


}
