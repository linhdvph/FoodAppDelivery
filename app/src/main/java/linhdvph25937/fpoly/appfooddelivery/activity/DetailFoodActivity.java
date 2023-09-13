package linhdvph25937.fpoly.appfooddelivery.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import linhdvph25937.fpoly.appfooddelivery.R;
import linhdvph25937.fpoly.appfooddelivery.adapter.CartAdapter;
import linhdvph25937.fpoly.appfooddelivery.adapter.CommentAdapter;
import linhdvph25937.fpoly.appfooddelivery.fragment.CartFragment;
import linhdvph25937.fpoly.appfooddelivery.model.Cart;
import linhdvph25937.fpoly.appfooddelivery.model.Comment;
import linhdvph25937.fpoly.appfooddelivery.model.Food;
import linhdvph25937.fpoly.appfooddelivery.model.Receiver;
import linhdvph25937.fpoly.appfooddelivery.ultil.MyRetrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailFoodActivity extends AppCompatActivity {
    private Button btnAddToCart;
    private TextView tvSubtraction, tvNumber, tvAddition, tvTitle, tvPrice, tvDescription, tvRating;
    private TextView tvCalo, tvTime, tvNumberInIconShoppingCart;
    private ImageView imgFood, imgArrowBack, imgToCart;
    private Food obj;
    private int numberOder = 0;
    private SharedPreferences sharedPreferences;
    public static final String TAG = DetailFoodActivity.class.toString();
    private RecyclerView rvComment;
    private ArrayList<Comment> listComment;
    private CommentAdapter commentAdapter;
    private ArrayList<Cart> listCart;
    private int quantityInCart = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail_food);
        initialView();
        onBack();
        getListInCart();
        goToCart();
        getBundle();
        addToCart();
        getListComment();
    }

    private void getListInCart() {
        String idUser = sharedPreferences.getString("id_user_login","");
        MyRetrofit.retrofit.getListCartByIdUser(idUser)
                .enqueue(new Callback<Receiver>() {
                    @Override
                    public void onResponse(Call<Receiver> call, Response<Receiver> response) {
                        if (response.body() != null){
                            int quantity = 0;
                            listCart = response.body().getListCart();
                            for (int i = 0; i < listCart.size(); i++) {
                                quantity += listCart.get(i).getQuantityOrder();
                            }
                            quantityInCart = quantity;
                            Toast.makeText(DetailFoodActivity.this, "Quantity Order: "+quantity, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Receiver> call, Throwable t) {
                        Log.e(TAG, "onFailure: " + t);
                        Toast.makeText(DetailFoodActivity.this, "Lỗi server khi lấy danh sách sản phẩm trong giỏ hàng theo id người dùng", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getListComment() {
        MyRetrofit.retrofit.getCommentByIdProduct(obj.getId())
                .enqueue(new Callback<Receiver>() {
                    @Override
                    public void onResponse(Call<Receiver> call, Response<Receiver> response) {
                        if (response.body() != null){
                           listComment = new ArrayList<>();
                           listComment = response.body().getListComment();
                           commentAdapter = new CommentAdapter(listComment);
                           LinearLayoutManager manager = new LinearLayoutManager(DetailFoodActivity.this, LinearLayoutManager.VERTICAL, false);
                           rvComment.setLayoutManager(manager);
                           rvComment.setAdapter(commentAdapter);
                        }else{
                            Log.e(TAG, "onResponse: Danh sách bình luận trống.");
                        }
                    }

                    @Override
                    public void onFailure(Call<Receiver> call, Throwable t) {
                        Log.e(TAG, "onFailure: " + t);
                        Toast.makeText(DetailFoodActivity.this, "Lỗi server khi lấy danh sách bình luận.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void goToCart() {
        imgToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailFoodActivity.this, MainActivity.class);
                intent.putExtra("fragment", "CartFragment");
                startActivity(intent);
            }
        });
    }


    private void addToCart() {
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numberOder>0){
                    String idUser = sharedPreferences.getString("id_user_login","");
                    String idProduct = obj.getId();
                    MyRetrofit.retrofit.addProductToCart(idUser, idProduct, numberOder)
                            .enqueue(new Callback<Cart>() {
                                @Override
                                public void onResponse(Call<Cart> call, Response<Cart> response) {
                                    if (response.body() != null){
                                        Toast.makeText(DetailFoodActivity.this, "Đã thêm vào giỏ hàng.", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(DetailFoodActivity.this, "Không thể thêm vào giỏ hàng.", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Cart> call, Throwable t) {
                                    Log.e(TAG, "onFailure: " + t);
                                    Toast.makeText(DetailFoodActivity.this, "Lỗi server khi thêm sản phẩm vào giỏ hàng.", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }

    private void onBack() {
        imgArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getBundle() {
        Bundle bd = getIntent().getExtras();
        obj = (Food) bd.getSerializable("object");
        Glide.with(DetailFoodActivity.this)
                .load(obj.getAvatar())
                .into(imgFood);
        tvTitle.setText(obj.getName());

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tvPrice.setText(decimalFormat.format(obj.getPrice())+"đ");

        tvDescription.setText(obj.getDescriptions());
        tvRating.setText(obj.getRating()+"");
        tvCalo.setText(obj.getEnergy()+"");
        tvTime.setText(obj.getTime()+" min");
        tvNumberInIconShoppingCart.setText(quantityInCart+"");
        tvNumberInIconShoppingCart.setVisibility(quantityInCart > 0 ? View.VISIBLE : View.GONE);
        tvAddition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberOder=numberOder+1;
                quantityInCart+=numberOder;
                if (numberOder == 0){
                    tvSubtraction.setVisibility(View.INVISIBLE);
                    tvAddition.setVisibility(View.VISIBLE);
                }else if (numberOder > 98){
                    tvAddition.setVisibility(View.INVISIBLE);
                }else{
                    tvAddition.setVisibility(View.VISIBLE);
                    tvSubtraction.setVisibility(View.VISIBLE);
                }
                tvNumber.setText(numberOder+"");
                btnAddToCart.setText("Add to cart - "+decimalFormat.format(numberOder * obj.getPrice())+"đ");
            }
        });
        tvSubtraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberOder=numberOder-1;
                quantityInCart-=numberOder;
                if (numberOder == 0){
                    tvSubtraction.setVisibility(View.INVISIBLE);
                    tvAddition.setVisibility(View.VISIBLE);
                }else if (numberOder > 98){
                    tvAddition.setVisibility(View.INVISIBLE);
                }else{
                    tvAddition.setVisibility(View.VISIBLE);
                    tvSubtraction.setVisibility(View.VISIBLE);
                }
                tvNumber.setText(numberOder+"");
                btnAddToCart.setText("Add to cart - "+decimalFormat.format(numberOder * obj.getPrice()) + "đ");
            }
        });

    }

    private void initialView() {
        btnAddToCart = findViewById(R.id.btn_add_to_cart_detail_food);
        tvSubtraction = findViewById(R.id.tv_subtraction_detail_food);
        tvNumber = findViewById(R.id.tv_number_detail_food);
        tvAddition = findViewById(R.id.tv_addition_detail_food);
        tvTitle = findViewById(R.id.tv_title_detail_food);
        tvPrice = findViewById(R.id.tv_price_detail_food);
        tvDescription = findViewById(R.id.description_detail_food);
        tvRating = findViewById(R.id.rating_detail_food);
        tvCalo = findViewById(R.id.energy_detail_food);
        tvTime = findViewById(R.id.time_detail_food);
        tvNumberInIconShoppingCart = findViewById(R.id.tv_number_in_icon_shopping_cart);
        imgFood = findViewById(R.id.img_detail_food);
        imgArrowBack = findViewById(R.id.img_arrow_back);
        imgToCart = findViewById(R.id.img_cart_detail_food);
        rvComment = findViewById(R.id.rv_comment);

        sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
    }
}