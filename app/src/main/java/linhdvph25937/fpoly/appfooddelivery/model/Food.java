package linhdvph25937.fpoly.appfooddelivery.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Food implements Serializable {
    @SerializedName("_id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("type_product")
    private FoodType typeProduct;
    @SerializedName("type_product_id")
    private String idTypeProduct;
    @SerializedName("price")
    private int price;
    @SerializedName("descriptions")
    private String descriptions;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("address")
    private String address;
    @SerializedName("time")
    private int time;
    @SerializedName("energy")
    private double energy;
    @SerializedName("rating")
    private double rating;

    public Food(String name, String idTypeProduct, int price, String address, String avatar, int time, double energy, double rating,String descriptions) {
        this.name = name;
        this.idTypeProduct = idTypeProduct;
        this.price = price;
        this.descriptions = descriptions;
        this.avatar = avatar;
        this.address = address;
        this.time = time;
        this.energy = energy;
        this.rating = rating;
    }

    public Food(String id, String name, String idTypeProduct, int price, String descriptions, String avatar, String address, int time, double energy, double rating) {
        this.id = id;
        this.name = name;
        this.idTypeProduct = idTypeProduct;
        this.price = price;
        this.descriptions = descriptions;
        this.avatar = avatar;
        this.address = address;
        this.time = time;
        this.energy = energy;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FoodType getIdTypeProduct() {
        return typeProduct;
    }

    public void setIdTypeProduct(FoodType idTypeProduct) {
        this.typeProduct = idTypeProduct;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
