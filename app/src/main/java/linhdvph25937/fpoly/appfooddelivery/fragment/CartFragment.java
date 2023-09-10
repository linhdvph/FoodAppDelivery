package linhdvph25937.fpoly.appfooddelivery.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

import linhdvph25937.fpoly.appfooddelivery.R;
import linhdvph25937.fpoly.appfooddelivery.activity.CheckOutActivity;
import linhdvph25937.fpoly.appfooddelivery.activity.MainActivity;
import linhdvph25937.fpoly.appfooddelivery.adapter.CartAdapter;
import linhdvph25937.fpoly.appfooddelivery.model.Cart;
import linhdvph25937.fpoly.appfooddelivery.model.Order;
import linhdvph25937.fpoly.appfooddelivery.model.Receiver;
import linhdvph25937.fpoly.appfooddelivery.ultil.MyRetrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {
    private LinearLayout linearLayoutCartEmpty, linearLayoutCartNoEmpty;
    private RecyclerView rvCart;
    private CartAdapter cartAdapter;
    private ArrayList<Cart> listCart;
    private String idUser;
    private SharedPreferences sharedPreferences;
    private Button btnGoToHomeFragment, btnCheckOut;
    public static final String TAG = CartFragment.newInstance().getTag();
    private static TextView tvAllPrice;
    private ConstraintLayout constraintLayout_summary;
    private Bundle bundle;

    public CartFragment() {
        // Required empty public constructor
    }

    public static CartFragment newInstance() {
        CartFragment fragment = new CartFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        bundle = getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        bundle = getArguments();
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        initialView(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDataInCartByIdUser();
        goToHome();
        setDataRvCart();
        goToCheckOutActivity();
//        getDataReOrder();
    }

    private void getDataReOrder() {
        if (bundle != null){
            Order objOrder = (Order) bundle.getSerializable("obj_re_order");
            listCart = new ArrayList<>();
            listCart = objOrder.getListProductOrder();
            Toast.makeText(getContext(), listCart.get(0).getObjFood().getName(), Toast.LENGTH_SHORT).show();
            cartAdapter = new CartAdapter(listCart,1);
            rvCart.setAdapter(cartAdapter);
        }
    }

    private void goToCheckOutActivity() {
        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CheckOutActivity.class);
                intent.putExtra("list_product_int_cart", listCart);
                startActivity(intent);
            }
        });
    }

    private void goToHome() {
        btnGoToHomeFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("fragment", "toHomeFragment");
                startActivity(intent);
            }
        });
    }

    private void getDataInCartByIdUser() {
        idUser = sharedPreferences.getString("id_user_login","");
        MyRetrofit.retrofit.getListCartByIdUser(idUser)
                .enqueue(new Callback<Receiver>() {
                    @Override
                    public void onResponse(Call<Receiver> call, Response<Receiver> response) {
                        if (response.body() != null){
                            listCart = response.body().getListCart();
                            cartAdapter = new CartAdapter(listCart, 1);
                            setOnClickEveryItem();
                            rvCart.setAdapter(cartAdapter);
                            setStateCartFragment();
                            getTotalPriceInCartFragment(listCart);
                        }
                    }

                    @Override
                    public void onFailure(Call<Receiver> call, Throwable t) {
                        Log.e(TAG, "onFailure: " + t);
                        Toast.makeText(getContext(), "Lỗi server khi lấy danh sách sản phẩm trong giỏ hàng theo id người dùng", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setOnClickEveryItem() {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        cartAdapter.setItemOnClick(new CartAdapter.ItemOnClick() {
            @Override
            public void Addition(Cart obj, CartAdapter.CartFragmentViewHolder cartHolder) {
                int numberInCart = obj.getQuantityOrder() + 1;
                obj.setQuantityOrder(numberInCart);
                MyRetrofit.retrofit.updateQuantityOrderCart(obj.get_id(), numberInCart)
                        .enqueue(new Callback<Cart>() {
                            @Override
                            public void onResponse(Call<Cart> call, Response<Cart> response) {
                                if (response.body() == null){
                                    Toast.makeText(cartHolder.itemView.getContext(), "Không cập nhật được số lượng trong giỏ hàng.", Toast.LENGTH_SHORT).show();
                                }else{
                                    CartFragment.getTotalPriceInCartFragment(listCart);
                                }
                            }

                            @Override
                            public void onFailure(Call<Cart> call, Throwable t) {
                                Toast.makeText(cartHolder.itemView.getContext(), "Lỗi server khi cập nhật số lượng trong giỏ hàng.", Toast.LENGTH_SHORT).show();
                            }
                        });
                if (numberInCart > 98){
                    cartHolder.tvAddition.setVisibility(View.INVISIBLE);
                }else{
                    cartHolder.tvAddition.setVisibility(View.VISIBLE);
                }
                cartHolder.tvNumber.setText(numberInCart+"");
                cartHolder.tvTotalPrice.setText(decimalFormat.format(obj.getObjFood().getPrice() * numberInCart) + "đ");
            }

            @Override
            public void Subtraction(Cart obj, CartAdapter.CartFragmentViewHolder cartHolder) {
                int numberInCart = obj.getQuantityOrder() - 1;
                obj.setQuantityOrder(numberInCart);
                MyRetrofit.retrofit.updateQuantityOrderCart(obj.get_id(), numberInCart)
                        .enqueue(new Callback<Cart>() {
                            @Override
                            public void onResponse(Call<Cart> call, Response<Cart> response) {
                                if (response.body() == null){
                                    Toast.makeText(getContext(), "Không cập nhật được số lượng trong giỏ hàng.", Toast.LENGTH_SHORT).show();
                                }else{
                                    CartFragment.getTotalPriceInCartFragment(listCart);
                                }
                            }

                            @Override
                            public void onFailure(Call<Cart> call, Throwable t) {
                                Toast.makeText(getContext(), "Lỗi server khi cập nhật số lượng trong giỏ hàng.", Toast.LENGTH_SHORT).show();
                            }
                        });
                if (numberInCart == 0){
                    MyRetrofit.retrofit.deleteCart(obj.get_id())
                            .enqueue(new Callback<Cart>() {
                                @Override
                                public void onResponse(Call<Cart> call, Response<Cart> response) {
                                    if (response.body() == null){
                                        Toast.makeText(getContext(), "Không xóa được sản phẩm trong giỏ hàng.", Toast.LENGTH_SHORT).show();
                                    }else {
                                        listCart.remove(cartHolder.getAdapterPosition());
                                        CartFragment.getTotalPriceInCartFragment(listCart);
                                        cartAdapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Cart> call, Throwable t) {
                                    Toast.makeText(getContext(), "Lỗi server khi xóa sản phẩm trong giỏ hàng.", Toast.LENGTH_SHORT).show();
                                }
                            });
                }else{
                    cartHolder.tvSubtraction.setVisibility(View.VISIBLE);
                }
                cartHolder.tvNumber.setText(numberInCart+"");
                cartHolder.tvTotalPrice.setText(decimalFormat.format(obj.getObjFood().getPrice() * numberInCart) + "đ");
            }
        });
    }

    private void setDataRvCart() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvCart.setLayoutManager(manager);
    }

    private void setStateCartFragment() {
        if (listCart.size() == 0){
            linearLayoutCartNoEmpty.setVisibility(View.GONE);
            linearLayoutCartEmpty.setVisibility(View.VISIBLE);
            btnCheckOut.setVisibility(View.GONE);
            constraintLayout_summary.setVisibility(View.GONE);
        }else{
            linearLayoutCartNoEmpty.setVisibility(View.VISIBLE);
            linearLayoutCartEmpty.setVisibility(View.GONE);
            btnCheckOut.setVisibility(View.VISIBLE);
            constraintLayout_summary.setVisibility(View.VISIBLE);
        }
    }

    public static void getTotalPriceInCartFragment(ArrayList<Cart> listCart){
        int totalQuantity = 0, priceDelivery = 0;
        for (int i = 0; i < listCart.size(); i++) {
            totalQuantity += listCart.get(i).getQuantityOrder() * listCart.get(i).getObjFood().getPrice();
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tvAllPrice.setText(decimalFormat.format(totalQuantity + priceDelivery) + "đ");
    }

    private void initialView(View view) {
        linearLayoutCartEmpty = view.findViewById(R.id.linearLayout_cart_empty);
        linearLayoutCartNoEmpty = view.findViewById(R.id.linearLayout_cart_no_emtpy);
        rvCart = view.findViewById(R.id.rv_cart);
        btnGoToHomeFragment = view.findViewById(R.id.btn_go_to_home_from_cart_fragment);
        btnCheckOut = view.findViewById(R.id.btn_check_out);
        tvAllPrice = view.findViewById(R.id.tv_all_price_cart);
        constraintLayout_summary = view.findViewById(R.id.constraintLayout_summary);

        listCart = new ArrayList<>();
        sharedPreferences = getContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
    }
}