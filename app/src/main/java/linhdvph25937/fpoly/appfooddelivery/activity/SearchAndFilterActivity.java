package linhdvph25937.fpoly.appfooddelivery.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import linhdvph25937.fpoly.appfooddelivery.R;
import linhdvph25937.fpoly.appfooddelivery.adapter.FoodAdapter;
import linhdvph25937.fpoly.appfooddelivery.model.Food;
import linhdvph25937.fpoly.appfooddelivery.model.FoodType;
import linhdvph25937.fpoly.appfooddelivery.model.Receiver;
import linhdvph25937.fpoly.appfooddelivery.ultil.MyRetrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchAndFilterActivity extends AppCompatActivity {
    private ImageView imgBack;
    private EditText edSearch;
    private RecyclerView rvProduct;
    private TextView tvCheckEmpty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_search_and_filter);

        initialView();
        goBack();
        getData();
    }

    private void goBack() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getData() {
        FoodType obj = (FoodType) getIntent().getSerializableExtra("obj_food_type");
        if (obj != null){
            MyRetrofit.retrofit.getProductByTypeProduct(obj.getId())
                    .enqueue(new Callback<Receiver>() {
                        @Override
                        public void onResponse(Call<Receiver> call, Response<Receiver> response) {
                            if (response.body() != null){
                                ArrayList<Food> listFood = response.body().getListFood();
                                if (listFood.size() == 0 || listFood == null){
                                    tvCheckEmpty.setVisibility(View.VISIBLE);
                                }else{
                                    LinearLayoutManager manager = new LinearLayoutManager(SearchAndFilterActivity.this, LinearLayoutManager.VERTICAL, false);
                                    FoodAdapter foodAdapter = new FoodAdapter(listFood, 1);
                                    rvProduct.setLayoutManager(manager);
                                    rvProduct.setAdapter(foodAdapter);
                                    tvCheckEmpty.setVisibility(View.GONE);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Receiver> call, Throwable t) {

                        }
                    });
        }
    }

    private void initialView() {
        imgBack = findViewById(R.id.img_arrow_back_search_and_filter);
        edSearch = findViewById(R.id.edSearchInFilterActivity);
        rvProduct = findViewById(R.id.rv_filter_product);
        tvCheckEmpty = findViewById(R.id.tv_check_empty_in_search_and_filter);
    }
}