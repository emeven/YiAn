package com.example.yian.view;

public class HouseWifery {
    private String title;
    private int imageId;
    private String tel;
    private String likenum;
    private String address;
    public HouseWifery(String title,String tel,String address,String likenum,int imageId){
        this.title=title;
        this.tel=tel;
        this.address=address;
        this.likenum=likenum;
        this.imageId=imageId;
    }

    public String getTitle() {
        return title;
    }

    public String getAddress() {
        return address;
    }

    public String getLikenum() {
        return likenum;
    }

    public String getTel() {
        return tel;
    }

    public int getImageId() {
        return imageId;
    }
}
