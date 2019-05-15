package com.ateb2go.paintertool;

public class PictureVO {

    String nickname;
    private String password;
    String title;
    String comment;
    String imgPath;
    String date;

    public PictureVO() {
    }

    public PictureVO(String nickname, String password, String title, String comment, String imgPath, String date) {
        this.nickname = nickname;
        this.password = password;
        this.title = title;
        this.comment = comment;
        this.imgPath = imgPath;
        this.date = date;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
