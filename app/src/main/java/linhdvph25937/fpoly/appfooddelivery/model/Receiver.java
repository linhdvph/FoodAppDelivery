package linhdvph25937.fpoly.appfooddelivery.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Receiver {

    private String msg;
    @SerializedName("data")
    private User data;

    @SerializedName("data_product_type")
    private ArrayList<FoodType> listFoodType;

    @SerializedName("data_product")
    private ArrayList<Food> listFood;

    @SerializedName("data_url_image")
    private ArrayList<ImageUrl> listImageUrl;

    @SerializedName("data_cart")
    private ArrayList<Cart> listCart;

    @SerializedName("data_order")
    private ArrayList<Order> listOrder;

    @SerializedName("data_comment")
    private ArrayList<Comment> listComment;
    //Getter and setter

    public ArrayList<Comment> getListComment() {
        return listComment;
    }

    public void setListComment(ArrayList<Comment> listComment) {
        this.listComment = listComment;
    }

    public ArrayList<Order> getListOrder() {
        return listOrder;
    }

    public void setListOrder(ArrayList<Order> listOrder) {
        this.listOrder = listOrder;
    }

    public ArrayList<Cart> getListCart() {
        return listCart;
    }

    public void setListCart(ArrayList<Cart> listCart) {
        this.listCart = listCart;
    }

    public ArrayList<ImageUrl> getListImageUrl() {
        return listImageUrl;
    }

    public void setListImageUrl(ArrayList<ImageUrl> listImageUrl) {
        this.listImageUrl = listImageUrl;
    }

    public ArrayList<Food> getListFood() {
        return listFood;
    }

    public void setListFood(ArrayList<Food> listFood) {
        this.listFood = listFood;
    }

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<FoodType> getListFoodType() {
        return listFoodType;
    }

    public void setListFoodType(ArrayList<FoodType> listFoodType) {
        this.listFoodType = listFoodType;
    }
}
