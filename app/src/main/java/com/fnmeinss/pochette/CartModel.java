package com.fnmeinss.pochette;


public class CartModel {
    private String key, name, imageUrl,productType,seller;
    private int quantity;
    private float totalPrice,price;

    public CartModel(){

    }



    public CartModel(String imageUrl, String productType, String product_description2, float product_price, String seller, int quantity) {
        this.imageUrl = imageUrl;
        this.productType = productType;
        this.name = product_description2;
        this.price = product_price;
        this.seller = seller;
        this.quantity = quantity;

    }
    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

}