package com.example.yian.view;

public class MallInformation {
    private String userName;
    private String goodsName;
    public String price;
    public int imageId;
    private String address;
    public String name;
    public int viewType;


    public MallInformation(String name,String price,int imageId,int viewType){
        this.name=name;
        this.imageId=imageId;
        this.price=price;
        this.viewType=viewType;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public int getImageId() {
        return imageId;
    }

    public int getViewType() {
        return viewType;
    }
}
