package linhdvph25937.fpoly.appfooddelivery.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable {
    //Lưu ý: state có 3 trạng thái tương ứng với 3 số: 0 - Đang xác nhận, 1 - Đang giao hàng, 2 - Giao hàng thành công.
    @SerializedName("_id")
    private String id;
    @SerializedName("id_user")
    private User idUser;

    @SerializedName("id_user_add")
    private String idAdd;
    @SerializedName("list_product")
    private ArrayList<Cart> listProductOrder;
    @SerializedName("total_price")
    private int totalPrice;
    @SerializedName("date")
    private String date;
    @SerializedName("payment_method")
    private String paymentMethod;
    @SerializedName("state")
    private int state;
    @SerializedName("date_canceled")
    private String dateCanceled;
    @SerializedName("reason_canceled")
    private String reasonCanceled;
    @SerializedName("completion_time")
    private String completionTime;

    public Order(User idUser, ArrayList<Cart> listProductOrder, int totalPrice, String date, String paymentMethod, int state, String dateCanceled, String reasonCanceled) {
        this.idUser = idUser;
        this.listProductOrder = listProductOrder;
        this.totalPrice = totalPrice;
        this.date = date;
        this.paymentMethod = paymentMethod;
        this.state = state;
        this.dateCanceled = dateCanceled;
        this.reasonCanceled = reasonCanceled;
    }

    public Order(User idUser, ArrayList<Cart> listProductOrder, int totalPrice, String date, String paymentMethod, int state) {
        this.idUser = idUser;
        this.listProductOrder = listProductOrder;
        this.totalPrice = totalPrice;
        this.date = date;
        this.paymentMethod = paymentMethod;
        this.state = state;
    }

    public Order(String id, String idAdd, int state, String completionTime) {
        this.id = id;
        this.idAdd = idAdd;
        this.state = state;
        this.completionTime = completionTime;
    }

    public String getIdAdd() {
        return idAdd;
    }

    public void setIdAdd(String idAdd) {
        this.idAdd = idAdd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getIdUser() {
        return idUser;
    }

    public void setIdUser(User idUser) {
        this.idUser = idUser;
    }

    public ArrayList<Cart> getListProductOrder() {
        return listProductOrder;
    }

    public void setListProductOrder(ArrayList<Cart> listProductOrder) {
        this.listProductOrder = listProductOrder;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getDateCanceled() {
        return dateCanceled;
    }

    public void setDateCanceled(String dateCanceled) {
        this.dateCanceled = dateCanceled;
    }

    public String getReasonCanceled() {
        return reasonCanceled;
    }

    public void setReasonCanceled(String reasonCanceled) {
        this.reasonCanceled = reasonCanceled;
    }

    public String getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(String completionTime) {
        this.completionTime = completionTime;
    }
}
