package com.example.user.toy.home.entity;

public class Age {
    /**
     * 1. 0-2
     * 2. 3-5
     * 3. 6-8
     * 4. 9-12
     */
    private int id;
    private String begin;
    private String end;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "Age{" +
                "id=" + id +
                ", begin='" + begin + '\'' +
                ", end='" + end + '\'' +
                '}';
    }
}
