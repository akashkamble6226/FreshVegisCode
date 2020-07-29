package com.example.freshvegis.Model;

public class Products
{
    private String productname,description,image,price,pid,quantity,discount;


    public Products()
    {

    }

    public Products(String productname, String description, String image, String price,String pid,String quantity,String discount)
    {
        this.productname = productname;
        this.description = description;
        this.image = image;
        this.price = price;
        this.pid = pid;
        this.quantity = quantity;
        this.discount = discount;



    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getProductname()
    {
        return productname;
    }

    public void setProductname(String productname)
    {
        this.productname = productname;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public String getPrice()
    {
        return price;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }

    public String getPid()
    {
        return pid;
    }

    public void setPid(String pid)
    {
        this.pid = pid;
    }

}
