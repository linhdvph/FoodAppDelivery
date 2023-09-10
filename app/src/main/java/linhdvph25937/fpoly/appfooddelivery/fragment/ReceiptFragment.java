package linhdvph25937.fpoly.appfooddelivery.fragment;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import linhdvph25937.fpoly.appfooddelivery.R;
import linhdvph25937.fpoly.appfooddelivery.adapter.OrderAdapter;
import linhdvph25937.fpoly.appfooddelivery.model.Order;
import linhdvph25937.fpoly.appfooddelivery.model.Receiver;
import linhdvph25937.fpoly.appfooddelivery.ultil.MyRetrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReceiptFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReceiptFragment extends Fragment {
    private RecyclerView rvOrder, rvAllOrderAdmin;
    private ArrayList<Order> list;
    private OrderAdapter orderAdapter;
    public static final String TAG = ReceiptFragment.newInstance().getTag();
    private SharedPreferences sharedPreferences;
    private String idUser = "";
    private TextView tvCheckEmpty;
    private FrameLayout frameLayoutUser;
    public ReceiptFragment() {
        // Required empty public constructor
    }

    public static ReceiptFragment newInstance() {
        ReceiptFragment fragment = new ReceiptFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        initialView(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getRoleUser();
    }

    private void getRoleUser() {
        sharedPreferences = getContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String phoneNumber = sharedPreferences.getString("phone_number_sign_in", "");
        MyRetrofit.retrofit.getUserByPhoneNumber(phoneNumber)
                .enqueue(new Callback<Receiver>() {
                    @Override
                    public void onResponse(Call<Receiver> call, Response<Receiver> response) {
                        if (response.body() != null){
                            int role = response.body().getData().getRole();
                            if (role == 0){
                                rvAllOrderAdmin.setVisibility(View.VISIBLE);
                                rvOrder.setVisibility(View.GONE);
                                frameLayoutUser.setVisibility(View.GONE);
                                getAllListOrder();
                                return;
                            }
                            if (role == 1){
                                rvAllOrderAdmin.setVisibility(View.GONE);
                                rvOrder.setVisibility(View.VISIBLE);
                                frameLayoutUser.setVisibility(View.VISIBLE);
                                getListOrder();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Receiver> call, Throwable t) {
                        Log.e(TAG, "onFailure: " + t);
                        Toast.makeText(getContext(), "Lỗi server khi lấy thông tin người dùng.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getAllListOrder() {
        MyRetrofit.retrofit.getAllOrder().enqueue(new Callback<Receiver>() {
            @Override
            public void onResponse(Call<Receiver> call, Response<Receiver> response) {
                if (response.body() != null){
                    list = response.body().getListOrder();
                    orderAdapter = new OrderAdapter(list,1);
                    LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    rvAllOrderAdmin.setLayoutManager(manager);
                    rvAllOrderAdmin.setAdapter(orderAdapter);
                }else{
                    Log.e(TAG, "onResponse: Không lấy danh sách đơn hàng.");
                }
            }

            @Override
            public void onFailure(Call<Receiver> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t);
                Toast.makeText(getContext(), "Lỗi server khi lấy danh sách đơn hàng.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getListOrder() {
        MyRetrofit.retrofit.getAllOrderByIdUser(idUser).enqueue(new Callback<Receiver>() {
            @Override
            public void onResponse(Call<Receiver> call, Response<Receiver> response) {
                if (response.body() != null){
                    list = response.body().getListOrder();
                    if (list.size() == 0 || list == null){
                        tvCheckEmpty.setVisibility(View.VISIBLE);
                    }else{
                        orderAdapter = new OrderAdapter(list,2);
                        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        rvOrder.setLayoutManager(manager);
                        rvOrder.setAdapter(orderAdapter);
                        tvCheckEmpty.setVisibility(View.GONE);
                    }
                }else{
                    Log.e(TAG, "onResponse: Không lấy danh sách đơn hàng.");
                }
            }

            @Override
            public void onFailure(Call<Receiver> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t);
                Toast.makeText(getContext(), "Lỗi server khi lấy danh sách đơn hàng.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initialView(View view) {
        rvOrder = view.findViewById(R.id.rv_order);
        rvAllOrderAdmin = view.findViewById(R.id.rv_all_order_admin);
        tvCheckEmpty = view.findViewById(R.id.tv_check_empty_in_order_fragment);
        frameLayoutUser = view.findViewById(R.id.frame_layout_user);
        sharedPreferences = getContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        idUser = sharedPreferences.getString("id_user_login","");
    }
}