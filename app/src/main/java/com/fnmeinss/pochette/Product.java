

package com.fnmeinss.pochette;

public class Product {


    public Product() {
    }

    public Product(String imageUrl, String productType, String product_description2, float product_price, String seller, int quantity) {
        this.imageUrl = imageUrl;
        this.productType = productType;
        this.product_description2 = product_description2;
        this.product_price = product_price;
        this.seller = seller;
        Quantity = quantity;
        this.name=name;
    }

    public Product(
                   String imageUrl,
                   String product_description2){
        this.imageUrl = imageUrl;
        this.product_description2 = product_description2;

    }

    public Product(
            String seller,
            String productType,
            String imageUrl,
            String product_description2,
            float product_price)
    {
        this.seller = seller;
        this.imageUrl = imageUrl;
        this.productType = productType;
        this.product_description2 = product_description2;
        this.product_price=product_price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public static String getKey() {
        return Key;
    }

    public static void setKey(String key) {
        Key = key;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProduct_description2() {
        return product_description2;
    }

    public void setProduct_description2(String product_description2) {
        this.product_description2 = product_description2;
    }


    public float getProduct_price() {
        return product_price;
    }

    public void setProduct_price(float product_price) {
        this.product_price = product_price;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

//    public void addProduct(String type, float price, String description,String imageUrl){
//        String productID= AddProductPage.db.push().getKey();
//        Product xx = new Product(type,imageUrl,description,price);
//        AddProductPage.db.child(type).child(productID).setValue(xx);
//
//    }

    private String imageUrl;
    private String productType;
    private String product_description2;
    private float product_price;
    private String seller;
    private int Quantity;
    private static String Key;
    private String name;


}

/*package com.fnmeinss.pochette;

public class Product {
    private String imageUrl;
    private String productType;
   // private int product_id;
    private String product_description2;
    private String product_price;
   // private String product_imageName;

    public Product() {
    }
   public Product(String imageUrl, String product_description2,String product_price) {
        this.imageUrl = imageUrl;
        this.product_description2 = product_description2;
        this.product_price=product_price;
    }
    public Product(String imageUrl,String productType, String product_description2, String product_price) {
        this.imageUrl = imageUrl;
        this.productType = productType;
       // this.product_id = product_id;
        this.product_description2 = product_description2;
        this.product_price = product_price;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public void addProduct(String type, String price, String description,String imageUrl){
        String productID= AddProductPage.db.push().getKey();
        Product xx = new Product(imageUrl,type,description,price);
        AddProductPage.db.child(type).child(productID).setValue(xx);

    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

   /* public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct_description2() {
        return product_description2;
    }

    public void setProduct_description2(String product_description2) {
        this.product_description2 = product_description2;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

   /* public String getProduct_imageName() {
        return product_imageName;
    }

    public void setProduct_imageName(String product_imageName) {
        this.product_imageName = product_imageName;
    }
}*/
