package linhdvph25937.fpoly.appfooddelivery.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.ArrayList;

import linhdvph25937.fpoly.appfooddelivery.R;
import linhdvph25937.fpoly.appfooddelivery.fragment.CartFragment;
import linhdvph25937.fpoly.appfooddelivery.model.Cart;
import linhdvph25937.fpoly.appfooddelivery.ultil.MyRetrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Cart> list;
    public static final int LAYOUT_CART_FRAGMENT = 1;
    public static final int LAYOUT_CHECK_OUT_ACTIVITY = 2;
    private int mCurrentLayout = LAYOUT_CART_FRAGMENT;
    public interface ItemOnClick{
        void Addition(Cart cart, CartFragmentViewHolder cartHolder);
        void Subtraction(Cart cart, CartFragmentViewHolder cartHolder);
    }

    private ItemOnClick itemOnClick;

    public void setItemOnClick(ItemOnClick mClick){
        this.itemOnClick = mClick;
    }

    public CartAdapter(ArrayList<Cart> list, int currentLayout) {
        this.list = list;
        this.mCurrentLayout = currentLayout;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       if (mCurrentLayout == LAYOUT_CART_FRAGMENT){
           View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_cart, parent, false);
           return new CartFragmentViewHolder(view);
       }else{
           View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_check_out_order, parent, false);
           return new CheckOutActivityHolder(view);
       }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Cart obj = list.get(position);
        if (obj == null){
            return;
        }

        if (mCurrentLayout == LAYOUT_CART_FRAGMENT){
            CartFragmentViewHolder cartHolder = (CartFragmentViewHolder) holder;
            cartHolder.tvTitle.setText(obj.getObjFood().getName());
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            cartHolder.tvPrice.setText(decimalFormat.format(obj.getObjFood().getPrice())+"đ");
            Glide.with(cartHolder.itemView.getContext())
                    .load(obj.getObjFood().getAvatar())
                    .into(cartHolder.imgFood);
            cartHolder.tvNumber.setText(obj.getQuantityOrder()+"");
            cartHolder.tvTotalPrice.setText(decimalFormat.format(obj.getObjFood().getPrice() * obj.getQuantityOrder()) + "đ");
            cartHolder.tvSubtraction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemOnClick != null){
                        itemOnClick.Subtraction(obj, cartHolder);
                    }
                }
            });

            cartHolder.tvAddition.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemOnClick != null){
                        itemOnClick.Addition(obj, cartHolder);
                    }
                }
            });
        }else{
            CheckOutActivityHolder checkOutHolder = (CheckOutActivityHolder) holder;
            checkOutHolder.tvNameProduct.setText(obj.getObjFood().getName());
            checkOutHolder.tvQuantityOrder.setText(obj.getQuantityOrder() + " x");
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            checkOutHolder.tvTotalPrice.setText(decimalFormat.format(obj.getQuantityOrder() * obj.getObjFood().getPrice())+"đ");
            Glide.with(checkOutHolder.itemView.getContext())
                    .load(obj.getObjFood().getAvatar())
                    .into(checkOutHolder.imgFood);
        }
    }


    @Override
    public int getItemCount() {
        if (list != null){
            return list.size();
        }
        return 0;
    }

    public static class CartFragmentViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle, tvPrice,tvTotalPrice,tvSubtraction,tvNumber,tvAddition;
        public ImageView imgFood;
        public CartFragmentViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_title_item_cart);
            tvPrice = itemView.findViewById(R.id.tv_price_item_cart);
            tvTotalPrice = itemView.findViewById(R.id.tv_total_price_item_cart);
            tvSubtraction = itemView.findViewById(R.id.tv_subtraction_item_cart);
            tvNumber = itemView.findViewById(R.id.tv_number_item_cart);
            tvAddition = itemView.findViewById(R.id.tv_addition_item_cart);
            imgFood = itemView.findViewById(R.id.img_item_cart);
        }
    }

    static class CheckOutActivityHolder extends RecyclerView.ViewHolder {
        public TextView tvQuantityOrder, tvNameProduct, tvTotalPrice;
        public ImageView imgFood;
        public CheckOutActivityHolder(@NonNull View itemView) {
            super(itemView);

            tvQuantityOrder = itemView.findViewById(R.id.tv_quantity_order_product_check_out);
            tvNameProduct = itemView.findViewById(R.id.tv_name_product_check_out);
            tvTotalPrice = itemView.findViewById(R.id.tv_total_price_product_check_out);
            imgFood = itemView.findViewById(R.id.img_food_check_out);
        }
    }
}
