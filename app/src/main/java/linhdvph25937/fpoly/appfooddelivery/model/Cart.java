package linhdvph25937.fpoly.appfooddelivery.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Cart implements Serializable {
    private String _id;
    @SerializedName("id_user")
    private User objUser;
    @SerializedName("id_product")
    private Food objFood;
    @SerializedName("quantity_order")
    private int quantityOrder;

    public Cart(User objUser, Food objFood, int quantityOrder) {
        this.objUser = objUser;
        this.objFood = objFood;
        this.quantityOrder = quantityOrder;
    }

    public User getObjUser() {
        return objUser;
    }


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setObjUser(User objUser) {
        this.objUser = objUser;
    }

    public Food getObjFood() {
        return objFood;
    }

    public void setObjFood(Food objFood) {
        this.objFood = objFood;
    }

    public int getQuantityOrder() {
        return quantityOrder;
    }

    public void setQuantityOrder(int quantityOrder) {
        this.quantityOrder = quantityOrder;
    }
}
