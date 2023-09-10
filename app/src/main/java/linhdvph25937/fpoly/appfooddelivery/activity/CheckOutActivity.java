package linhdvph25937.fpoly.appfooddelivery.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import linhdvph25937.fpoly.appfooddelivery.R;
import linhdvph25937.fpoly.appfooddelivery.adapter.CartAdapter;
import linhdvph25937.fpoly.appfooddelivery.model.Cart;
import linhdvph25937.fpoly.appfooddelivery.model.Order;
import linhdvph25937.fpoly.appfooddelivery.ultil.MyRetrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckOutActivity extends AppCompatActivity {
    private TextView tvUsername, tvPhoneNumber, tvAddress, tvTitlePriceProduct, tvAllPriceProduct;
    private TextView tvPriceDelivery, tvTotalPrice;
    private RecyclerView rvProductCheckOut;
    private RadioGroup radioGroup;
    private Button btnOrder;
    private ArrayList<Cart> listCart;
    private CartAdapter cartAdapter;
    private ImageView imgBack;
    private SharedPreferences sharedPreferences;
    private int allPrice = 0;
    public static final String TAG = CheckOutActivity.class.toString();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_check_out);

        initialView();
        goBack();
        setDataInRecycleView();
        setValueInTxt();
        confirmOrder();
    }
    private void confirmOrder() {
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
                int selectedId = radioGroup.getCheckedRadioButtonId();
                String payment_method = "";
                String idUser = sharedPreferences.getString("id_user_login", "");
                if (selectedId != -1){
                    RadioButton radioButton = findViewById(selectedId);
                    payment_method = radioButton.getText().toString();
                }
                MyRetrofit.retrofit.addOrder(idUser, allPrice, getCurrentDateTime(), payment_method, 0)
                        .enqueue(new Callback<Order>() {
                            @Override
                            public void onResponse(Call<Order> call, Response<Order> response) {
                                if (response.body() != null){
                                    Toast.makeText(CheckOutActivity.this, "Bạn đã đặt hàng thành công, chờ xác nhận.", Toast.LENGTH_SHORT).show();
                                    clearCart(idUser);
                                    Intent intent = new Intent(CheckOutActivity.this, MainActivity.class);
                                    intent.putExtra("fragment", "ReceiptFragment");
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Toast.makeText(CheckOutActivity.this, "Đặt hàng không thành công.", Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, "onResponse: " + response);
                                }
                            }

                            @Override
                            public void onFailure(Call<Order> call, Throwable t) {
                                Log.e(TAG, "onFailure: " + t);
                                Toast.makeText(CheckOutActivity.this, "Lỗi server khi thêm đơn hàng.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void clearCart(String id) {
        MyRetrofit.retrofit.deleteCartByIdUser(id)
                .enqueue(new Callback<Cart>() {
                    @Override
                    public void onResponse(Call<Cart> call, Response<Cart> response) {
                        if (!response.isSuccessful()){
                            Toast.makeText(CheckOutActivity.this, "Không xóa được sản phẩm trong giỏ hàng theo id người dùng.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Cart> call, Throwable t) {
                        Log.e(TAG, "onFailure: " + t);
                        Toast.makeText(CheckOutActivity.this, "Lỗi server khi xóa sản phẩm trong giỏ hàng theo id người dùng.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setValueInTxt() {
        int totalPriceProduct = 0, priceDelivery = 0, quantity = 0;
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        for (int i = 0; i < listCart.size(); i++) {
            totalPriceProduct += listCart.get(i).getQuantityOrder() * listCart.get(i).getObjFood().getPrice();
            quantity += listCart.get(i).getQuantityOrder();
        }
        allPrice = totalPriceProduct + priceDelivery;
        tvAllPriceProduct.setText(decimalFormat.format(allPrice)+"đ");
        tvTotalPrice.setText(decimalFormat.format(totalPriceProduct)+"đ");
        tvTitlePriceProduct.setText("Tổng cộng ("+quantity+" món)");
        btnOrder.setText("Đặt hàng - "+decimalFormat.format(allPrice) + "đ");
        String phoneNumber = sharedPreferences.getString("phone_number_sign_in","");
        String username = sharedPreferences.getString("name_user_login","");
        String address = sharedPreferences.getString("address_user_login","");
        tvPhoneNumber.setText(phoneNumber);
        tvUsername.setText(username);
        tvAddress.setText(address);
    }

    private void setDataInRecycleView() {
        listCart = new ArrayList<>();
        listCart = (ArrayList<Cart>) getIntent().getSerializableExtra("list_product_int_cart");
        LinearLayoutManager manager = new LinearLayoutManager(CheckOutActivity.this, LinearLayoutManager.VERTICAL, false);
        cartAdapter = new CartAdapter(listCart, 2);
        rvProductCheckOut.setLayoutManager(manager);
        rvProductCheckOut.setAdapter(cartAdapter);
    }
    private void goBack() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private String getCurrentDateTime(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        String currentDateTime = simpleDateFormat.format(date);
        return currentDateTime;
    }

    private void initialView() {
        tvUsername = findViewById(R.id.tv_username_check_out);
        tvPhoneNumber = findViewById(R.id.tv_phone_number_check_out);
        tvAddress = findViewById(R.id.tv_address_check_out);
        tvTitlePriceProduct = findViewById(R.id.tv_title_price_product_check_out);
        tvAllPriceProduct = findViewById(R.id.tv_all_price_check_out);
        tvPriceDelivery = findViewById(R.id.tv_price_delivery_check_out);
        tvTotalPrice = findViewById(R.id.tv_total_price_product_check_out);
        rvProductCheckOut = findViewById(R.id.rv_product_check_out);
        radioGroup = findViewById(R.id.radio_group_check_out);
        btnOrder = findViewById(R.id.btn_order);
        imgBack = findViewById(R.id.img_arrow_back_check_out);
        sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
    }
}