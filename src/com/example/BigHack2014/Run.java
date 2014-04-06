package com.example.BigHack2014;

import android.graphics.Bitmap;

import java.util.Date;

public class Run {
    private long id;
    private Bitmap bitmap;
    private Date date;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return date.toString();
    }
}
