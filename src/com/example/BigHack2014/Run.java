package com.example.BigHack2014;

import java.util.Date;

public class Run {
    private long id;
    private String bitmap;
    private Date date;
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBitmap() {
        return bitmap;
    }

    public void setBitmap(String bitmap) {
        this.bitmap = bitmap;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }


    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return date.toString();
    }
}
