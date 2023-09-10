package linhdvph25937.fpoly.appfooddelivery.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import linhdvph25937.fpoly.appfooddelivery.R;
import linhdvph25937.fpoly.appfooddelivery.adapter.CartAdapter;
import linhdvph25937.fpoly.appfooddelivery.model.Cart;
import linhdvph25937.fpoly.appfooddelivery.model.Comment;
import linhdvph25937.fpoly.appfooddelivery.model.Food;
import linhdvph25937.fpoly.appfooddelivery.model.Order;
import linhdvph25937.fpoly.appfooddelivery.ultil.MyRetrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailOrderActivity extends AppCompatActivity {
    private ImageView imgArrowBack;
    private TextView tvUsernameAndPhoneNumber, tvTotalPriceQuantityOrderPaymentMethod, tvAddress;
    private TextView tvDate, tvCompletionTime, tvTitlePrice, tvPrice, tvAllPrice;
    private RecyclerView rvProductOrder;
    private Button btnRating, btnCancelOrder;
    private Order objOrder;
    private ArrayList<Cart> listCart;
    private CartAdapter cartAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail_order);

        initialView();
        goBack();
        setValue();
        destroyOrder();
    }

    private void destroyOrder() {
        btnCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(DetailOrderActivity.this)
                        .setTitle("Hủy đơn hàng")
                        .setIcon(android.R.drawable.ic_delete)
                        .setMessage("Bạn có chắc hủy đơn hàng này?")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MyRetrofit.retrofit.deleteOrder(objOrder.getId())
                                        .enqueue(new Callback<Order>() {
                                            @Override
                                            public void onResponse(Call<Order> call, Response<Order> response) {
                                                if (response.isSuccessful()){
                                                    Toast.makeText(DetailOrderActivity.this, "Hủy đơn hàng thành công", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(DetailOrderActivity.this, MainActivity.class));
                                                    dialog.dismiss();
                                                }else{
                                                    Toast.makeText(DetailOrderActivity.this, "Hủy đơn hàng thất bại", Toast.LENGTH_SHORT).show();
                                                    dialog.dismiss();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Order> call, Throwable t) {
                                                Log.e("ErrorDeleteOrder", "onFailure: "+ t);
                                                Toast.makeText(DetailOrderActivity.this, "Lỗi server khi hủy đơn hàng", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
    }

    private void setValue() {
        objOrder = (Order) getIntent().getSerializableExtra("obj_order");
        if (objOrder.getState() == 2){
            btnRating.setVisibility(View.VISIBLE);
            btnCancelOrder.setVisibility(View.GONE);
        }else if (objOrder.getState() == 1){
            btnRating.setVisibility(View.GONE);
            btnCancelOrder.setVisibility(View.GONE);
        }else{
            btnRating.setVisibility(View.GONE);
            btnCancelOrder.setVisibility(View.VISIBLE);
        }
        listCart = new ArrayList<>();
        listCart = objOrder.getListProductOrder();
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        int quantityOrder = 0, price = 0;
        for (int i = 0; i < listCart.size(); i++) {
            quantityOrder += listCart.get(i).getQuantityOrder();
            price += listCart.get(i).getQuantityOrder() * listCart.get(i).getObjFood().getPrice();
        }
        tvUsernameAndPhoneNumber.setText(objOrder.getIdUser().getName()+" | "+objOrder.getIdUser().getPhoneNumber());
        tvTotalPriceQuantityOrderPaymentMethod.setText(decimalFormat.format(objOrder.getTotalPrice())+"đ - "+quantityOrder+" món - "+objOrder.getPaymentMethod());
        tvAddress.setText(objOrder.getIdUser().getAddress());
        tvDate.setText("Thời gian đặt hàng: "+objOrder.getDate());
//        if (!objOrder.getCompletionTime().equals("")){
//            tvCompletionTime.setText("Thời gian nhận: "+objOrder.getCompletionTime());
//        }
        tvAllPrice.setText(decimalFormat.format(objOrder.getTotalPrice())+"đ");
        tvPrice.setText(decimalFormat.format(price)+"đ");
        tvTitlePrice.setText("Tổng cộng ("+quantityOrder+" món)");
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        cartAdapter = new CartAdapter(listCart,2);
        rvProductOrder.setLayoutManager(manager);
        rvProductOrder.setAdapter(cartAdapter);
        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(DetailOrderActivity.this, android.R.style.Theme_DeviceDefault_Light_DarkActionBar);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layout_dialog_comment);
                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);

                ImageView imgArrowBack = dialog.findViewById(R.id.img_arrow_back_comment);
                ImageView imgFood = dialog.findViewById(R.id.img_food_comment);
                TextView tvNameFood = dialog.findViewById(R.id.tv_name_product_comment);
                EditText edContent = dialog.findViewById(R.id.ed_content_commnet);
                Button btnSend = dialog.findViewById(R.id.btn_send_comment);

                Glide.with(DetailOrderActivity.this)
                        .load(objOrder.getListProductOrder().get(0).getObjFood().getAvatar())
                        .into(imgFood);
                tvNameFood.setText(objOrder.getListProductOrder().get(0).getObjFood().getName());
                imgArrowBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btnSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String content = edContent.getText().toString().trim();
                        MyRetrofit.retrofit.addComment(objOrder.getIdUser().getId(), objOrder.getListProductOrder().get(0).getObjFood().getId(), content, getCurrentDateTime())
                                .enqueue(new Callback<Comment>() {
                                    @Override
                                    public void onResponse(Call<Comment> call, Response<Comment> response) {
                                        if (response.body() != null){
                                            Toast.makeText(DetailOrderActivity.this, "Bạn đã đánh giá thành công.", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }else{
                                            Toast.makeText(DetailOrderActivity.this, "Không thể thêm đánh giá thành công.", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Comment> call, Throwable t) {
                                        Log.e("ErrorAddComment", "onFailure: " + t);
                                        Toast.makeText(DetailOrderActivity.this, "Lỗi server khi đánh giá sản phẩm", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                });
                    }
                });
                dialog.show();
            }
        });
    }

    private String getCurrentDateTime(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        String currentDateTime = simpleDateFormat.format(date);
        return currentDateTime;
    }

    private void goBack() {
        imgArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initialView() {
        tvUsernameAndPhoneNumber               = findViewById(R.id.tv_username_and_phone_number_detail_order);
        tvTotalPriceQuantityOrderPaymentMethod = findViewById(R.id.tv_total_price_and_quantity_order_and_payment_method_detail_order);
        tvAddress                              = findViewById(R.id.tv_address_detail_order);
        tvDate                                 = findViewById(R.id.tv_date_detail_order);
        tvCompletionTime                       = findViewById(R.id.tv_completion_time_detail_order);
        tvTitlePrice                           = findViewById(R.id.tv_title_price_product_detail_order);
        tvPrice                                = findViewById(R.id.tv_total_price_product_detail_order);
        tvAllPrice                             = findViewById(R.id.tv_all_price_detail_order);
        rvProductOrder                         = findViewById(R.id.rv_product_detail_order);
        btnRating                              = findViewById(R.id.btn_rate_detail_order);
        btnCancelOrder                         = findViewById(R.id.btn_cancel_deltail_order);
        imgArrowBack                           = findViewById(R.id.img_arrow_back_detail_order);
    }
}