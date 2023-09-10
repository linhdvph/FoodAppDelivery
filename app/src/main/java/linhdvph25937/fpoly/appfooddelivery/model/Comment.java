package linhdvph25937.fpoly.appfooddelivery.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Comment implements Serializable {
    @SerializedName("_id")
    private String id;
    @SerializedName("id_user")
    private User objUser;
    @SerializedName("id_product")
    private Food objFood;
    @SerializedName("content")
    private String content;
    @SerializedName("date")
    private String date;

    public Comment(User objUser, Food objFood, String content, String date) {
        this.objUser = objUser;
        this.objFood = objFood;
        this.content = content;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getObjUser() {
        return objUser;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
