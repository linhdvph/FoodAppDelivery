package linhdvph25937.fpoly.appfooddelivery.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import linhdvph25937.fpoly.appfooddelivery.R;
import linhdvph25937.fpoly.appfooddelivery.adapter.FoodAdapter;
import linhdvph25937.fpoly.appfooddelivery.model.Food;
import linhdvph25937.fpoly.appfooddelivery.model.Receiver;
import linhdvph25937.fpoly.appfooddelivery.ultil.MyRetrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllProductFragment extends Fragment {
    private ArrayList<Food> list;
    private FoodAdapter foodAdapter;
    private RecyclerView rvAllProduct;
    public static final String TAG = AllProductFragment.newInstance().getTag();
    public AllProductFragment() {
        // Required empty public constructor
    }

    public static AllProductFragment newInstance() {
        AllProductFragment fragment = new AllProductFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_product, container, false);
        initialView(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getAllProduct();
    }

    private void getAllProduct() {
        MyRetrofit.retrofit.getAllProduct().enqueue(new Callback<Receiver>() {
            @Override
            public void onResponse(Call<Receiver> call, Response<Receiver> response) {
                if (response.body() != null){
                    list = response.body().getListFood();
                    foodAdapter = new FoodAdapter(list,1);
                    LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    rvAllProduct.setLayoutManager(manager);
                    rvAllProduct.setAdapter(foodAdapter);
                }else{
                    Log.e(TAG, "onResponse: Không lấy được danh sách sản phẩm.");
                }
            }

            @Override
            public void onFailure(Call<Receiver> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t);
                Toast.makeText(getContext(), "Lỗi server khi lấy danh sách sản phẩm.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initialView(View view) {
        rvAllProduct = view.findViewById(R.id.rv_all_product);
    }
}