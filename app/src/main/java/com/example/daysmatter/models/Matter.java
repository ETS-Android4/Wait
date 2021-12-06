package com.example.daysmatter.models;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.Date;

public class Matter extends LitePalSupport implements Serializable{
    // remember to increment the database version number in litepal.xml after changing annotations of the attributes

    @Column(unique = true)
    private String title;

    private Date targetDate;

    // 使用本地相册图片路径的方式存储图片
    private String imagePath;

    public Matter(String title, Date targetDate, String imagePath) {
        this.title = title;
        this.targetDate = targetDate;
        this.imagePath = imagePath;
    }

    public Matter(){}

    /**
     * Getters and Setters
     */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(Date targetDate) {
        this.targetDate = targetDate;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
