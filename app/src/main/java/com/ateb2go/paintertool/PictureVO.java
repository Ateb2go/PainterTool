package com.ateb2go.paintertool;

public class PictureVO {
    String name;
    String imgPath;
    String title;
    String date;
    String comment;

    public PictureVO() {
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public PictureVO(String name, String imgPath, String title, String date, String comment) {

        this.name = name;
        this.imgPath = imgPath;
        this.title = title;
        this.date = date;
        this.comment = comment;
    }
}
