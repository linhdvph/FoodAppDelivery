package linhdvph25937.fpoly.appfooddelivery.model;

import com.google.gson.annotations.SerializedName;

public class ImageUrl {
    @SerializedName("_id")
    private String id;
    @SerializedName("url")
    private String url;

    public ImageUrl(String id, String url) {
        this.id = id;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
