package linhdvph25937.fpoly.appfooddelivery.ultil;

import java.util.ArrayList;

import linhdvph25937.fpoly.appfooddelivery.model.Cart;
import linhdvph25937.fpoly.appfooddelivery.model.Comment;
import linhdvph25937.fpoly.appfooddelivery.model.Food;
import linhdvph25937.fpoly.appfooddelivery.model.Order;
import linhdvph25937.fpoly.appfooddelivery.model.User;
import linhdvph25937.fpoly.appfooddelivery.model.Receiver;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MyRetrofit {
    //http://192.168.0.11:3000/api
    MyRetrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.137.1:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MyRetrofit.class);

    //Lấy danh sách người dùng
    @GET("api/user")
    Call<Receiver> getAllUser();

    //Lấy danh sách người dùng theo số điện thoại
    @FormUrlEncoded
    @POST("api/user/phone_number")
    Call<Receiver> getUserByPhoneNumber(@Field("phone_number") String phoneNumber);

    //Thêm người dùng (sign up)
    @POST("api/user/add")
    Call<User> addUser(@Body User user);

    //Đăng nhập (sign in)
    @FormUrlEncoded
    @POST("api/user/sign_in")
    Call<User> signIn(@Field("phone_number") String phoneNumber, @Field("passwd") String passwd);

    //Cập nhật mật khẩu người dùng
    @FormUrlEncoded
    @POST("api/user/up_passwd/{id}")
    Call<User> updatePasswdUser(@Path("id") String id, @Field("passwd") String passwd);

    //Cập nhật tên người dùng
    @FormUrlEncoded
    @POST("api/user/up_name/{id}")
    Call<User> updateNameUser(@Path("id") String id, @Field("name") String name);

    //Cập nhật địa chỉ người dùng
    @FormUrlEncoded
    @POST("api/user/up_address/{id}")
    Call<User> updateAddressUser(@Path("id") String id, @Field("address") String address);

    //Cập nhật số điện thoại người dùng
    @FormUrlEncoded
    @POST("api/user/up_phone_number/{id}")
    Call<User> updatePhoneNumberUser(@Path("id") String id, @Field("phone_number") String phoneNumber);

    //Lấy danh sách loại sản phẩm
    @GET("api/product_type")
    Call<Receiver> getAllProductType();

    //Lấy danh sách sản phẩm
    @GET("api/product")
    Call<Receiver> getAllProduct();

    //Lấy danh sách sản phẩm sắp xếp theo rating
    @GET("api/product/list-rating")
    Call<Receiver> getProductByRating();

    //Lấy danh sách 5 sản phẩm có lượng đánh giá cao nhất
    @GET("api/product/new")
    Call<Receiver> getProductHighetRating();

    //Lấy danh sách sản phẩm theo loại sản phẩm
    @GET("api/product/list-by-product-type/{id}")
    Call<Receiver> getProductByTypeProduct(@Path("id") String idTypeProduct);

    //Thêm sản phẩm
    @POST("api/product/add")
    Call<Food> addProduct(@Body Food obj);

    //Sửa sản phẩm
    @POST("api/product/up/{id}")
    Call<Food> updateProduct(@Path("id") String id, @Body Food obj);

    //Xóa sản phẩm
    @DELETE("api/product/del/{id}")
    Call<Food> deleteProduct(@Path("id") String id);

    //Lấy danh sách link ảnh banner
    @GET("api/image")
    Call<Receiver> getLinkImageBanner();

    //Lấy danh sách sản phẩm trong giỏ hàng bằng id user
    @GET("api/cart/list-by-id-user/{id}")
    Call<Receiver> getListCartByIdUser(@Path("id") String id);

    //Thêm sản phẩm vào giỏ hàng
    @FormUrlEncoded
    @POST("api/cart/add")
    Call<Cart> addProductToCart(@Field("id_user") String idUser, @Field("id_product") String idProduct, @Field("quantity_order") int quantityOrder);

    //Cập nhật số lượng trong giỏ hàng.
    @FormUrlEncoded
    @POST("api/cart/up-quantity-order/{id}")
    Call<Cart> updateQuantityOrderCart(@Path("id") String id,@Field("quantity_order") int quantityOrder);

    //Xóa sản phẩm trong giỏ hàng.
    @DELETE("api/cart/del/{id}")
    Call<Cart> deleteCart(@Path("id") String id);

    //Xóa sản phẩm trong giỏ hàng theo id người dùng
    @DELETE("api/cart/del-cart-by-id-user/{id}")
    Call<Cart> deleteCartByIdUser(@Path("id") String id);

    //Lấy tất cả đơn hàng
    @GET("api/order")
    Call<Receiver> getAllOrder();

    //Lấy danh sách đơn hàng theo id người dùng
    @GET("api/order/{id}")
    Call<Receiver> getAllOrderByIdUser(@Path("id") String idUser);

    //Thêm đơn hàng
    @FormUrlEncoded
    @POST("api/order/add")
    Call<Order> addOrder(@Field("id_user_add") String idUser, @Field("total_price") int price, @Field("date") String date, @Field("payment_method") String paymentMethod, @Field("state") int state);

    //Xóa đơn hàng.
    @DELETE("api/order/del/{id}")
    Call<Order> deleteOrder(@Path("id") String id);
    //Cập nhật trạng thái
    @FormUrlEncoded
    @POST("api/order/up-state/{id}")
    Call<Order> updateStateOrder(@Path("id") String id, @Field("id_user_add") String idUser, @Field("state") int state, @Field("completion_time") String completionTime);
    //Thêm comment
    @FormUrlEncoded
    @POST("api/comment/add")
    Call<Comment> addComment(@Field("id_user") String idUser, @Field("id_product") String idProduct, @Field("content") String content, @Field("date") String date);

    //Lấy danh sách commnent theo id sản phẩm
    @GET("api/comment/product/{id}")
    Call<Receiver> getCommentByIdProduct(@Path("id") String id);

}
