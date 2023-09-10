package linhdvph25937.fpoly.appfooddelivery.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;

import java.util.ArrayList;

import linhdvph25937.fpoly.appfooddelivery.R;
import linhdvph25937.fpoly.appfooddelivery.activity.DetailFoodActivity;
import linhdvph25937.fpoly.appfooddelivery.activity.SignInActivity;
import linhdvph25937.fpoly.appfooddelivery.model.Food;
import linhdvph25937.fpoly.appfooddelivery.model.FoodType;
import linhdvph25937.fpoly.appfooddelivery.model.Receiver;
import linhdvph25937.fpoly.appfooddelivery.ultil.MyRetrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public static final int LAYOUT_VERTICAL = 1;
    public static final int LAYOUT_HORIZONTAL = 2;
    private int mCurrentLayout = LAYOUT_VERTICAL;
    private ArrayList<Food> list;
    private ArrayList<FoodType> listFoodType;
    private SharedPreferences sharedPreferences;
    private boolean isSignIn;

    public interface ItemOnClick{
        void UpdateProduct(int position, Food food);
        void DeleteProduct(int position, Food food);
    }

    private ItemOnClick itemOnClick;

    public void setItemOnClick(ItemOnClick mItemOnClick){
        this.itemOnClick = mItemOnClick;
    }

    public FoodAdapter(ArrayList<Food> list, int layout) {
        this.list = list;
        this.mCurrentLayout = layout;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mCurrentLayout == LAYOUT_VERTICAL){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_food_vertical, parent, false);
            return new VerticalViewHolder(view);
        }else if (mCurrentLayout == LAYOUT_HORIZONTAL){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_food, parent, false);
            return new HorizontalViewHolder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_food_admin, parent, false);
            return new AdminViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Food objFood = list.get(position);
        getListFoodType(holder.itemView.getContext());
        if (objFood == null){
            return;
        }
        if (mCurrentLayout == LAYOUT_VERTICAL){
            VerticalViewHolder verticalView = (VerticalViewHolder) holder;
            verticalView.tvName.setText(objFood.getName());
            verticalView.tvTime.setText(objFood.getTime()+" min");
            verticalView.tvRating.setText(objFood.getRating()+"");
            Glide.with(verticalView.itemView.getContext())
                    .load(objFood.getAvatar())
                    .transform(new GranularRoundedCorners(30,30,0,0))
                    .into(verticalView.img);
            verticalView.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sharedPreferences = holder.itemView.getContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                    isSignIn = sharedPreferences.getBoolean("isSignIn", false);
                    Intent intent;
                    if (isSignIn == false){
                        intent = new Intent(verticalView.itemView.getContext(), SignInActivity.class);
                    }else {
                        intent = new Intent(verticalView.itemView.getContext(), DetailFoodActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("object", objFood);
                        intent.putExtras(bundle);
                        verticalView.itemView.getContext().startActivity(intent);
                    }
                }
            });
        }else if (mCurrentLayout == LAYOUT_HORIZONTAL){
            HorizontalViewHolder horizontalView = (HorizontalViewHolder) holder;
            horizontalView.tvName.setText(objFood.getName());
            horizontalView.tvTime.setText(objFood.getTime()+" min");
            horizontalView.tvRating.setText(objFood.getRating()+"");
            Glide.with(horizontalView.itemView.getContext())
                    .load(objFood.getAvatar())
                    .transform(new GranularRoundedCorners(30,30,0,0))
                    .into(horizontalView.img);
            horizontalView.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sharedPreferences = holder.itemView.getContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                    isSignIn = sharedPreferences.getBoolean("isSignIn", false);
                    Intent intent;
                    if (isSignIn == false){
                        intent = new Intent(horizontalView.itemView.getContext(), SignInActivity.class);
                    }else{
                        intent = new Intent(horizontalView.itemView.getContext(), DetailFoodActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("object", objFood);
                        intent.putExtras(bundle);
                    }
                    horizontalView.itemView.getContext().startActivity(intent);
                }
            });
        }else{
            AdminViewHolder adminView = (AdminViewHolder) holder;
            adminView.tvName.setText(objFood.getName());
            adminView.tvTime.setText(objFood.getTime()+" min");
            adminView.tvRating.setText(objFood.getRating()+"");
            Glide.with(adminView.itemView.getContext())
                    .load(objFood.getAvatar())
                    .transform(new GranularRoundedCorners(30,30,0,0))
                    .into(adminView.img);
            adminView.imgDel.setOnClickListener(v -> {
                if (itemOnClick != null){
                    itemOnClick.DeleteProduct(holder.getAdapterPosition(), objFood);
                }
            });

            adminView.imgEdit.setOnClickListener(v -> {
                if (itemOnClick != null){
                    itemOnClick.UpdateProduct(holder.getAdapterPosition(), objFood);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (list != null){
            return list.size();
        }
        return 0;
    }

    private void getListFoodType(Context context){
        listFoodType = new ArrayList<>();
        MyRetrofit.retrofit.getAllProductType().enqueue(new Callback<Receiver>() {
            @Override
            public void onResponse(Call<Receiver> call, Response<Receiver> response) {
                if (response.body() != null){
                    listFoodType = response.body().getListFoodType();
                }else{
                    Log.e("TAG", "onResponse: Không thể lấy danh sách loại sản phẩm");
                }
            }

            @Override
            public void onFailure(Call<Receiver> call, Throwable t) {
                Log.e("TAG", "Lỗi server khi lấy danh sách loại sản phẩm: " + t);
                Toast.makeText(context, "Lỗi server khi lấy danh sách loại sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class VerticalViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView tvTime, tvName, tvRating;
        public VerticalViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgFoodOneVertical);
            tvTime = itemView.findViewById(R.id.tvTimeFoodOneVertical);
            tvName = itemView.findViewById(R.id.tvNameFoodOneVertical);
            tvRating = itemView.findViewById(R.id.tvRatingFoodOneVertical);
        }
    }

    public class HorizontalViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView tvTime, tvName, tvRating;
        public HorizontalViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgFoodOne);
            tvTime = itemView.findViewById(R.id.tvTimeFoodOne);
            tvName = itemView.findViewById(R.id.tvNameFoodOne);
            tvRating = itemView.findViewById(R.id.tvRatingFoodOne);
        }
    }

    public class AdminViewHolder extends RecyclerView.ViewHolder {
        private ImageView img, imgEdit, imgDel;
        private TextView tvTime, tvName, tvRating;
        public AdminViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgFoodOneAdmin);
            tvTime = itemView.findViewById(R.id.tvTimeFoodOneAdmin);
            tvName = itemView.findViewById(R.id.tvNameFoodOneAdmin);
            tvRating = itemView.findViewById(R.id.tvRatingFoodOneAdmin);
            imgEdit = itemView.findViewById(R.id.img_edit_item_food_admin);
            imgDel = itemView.findViewById(R.id.img_del_item_food_admin);
        }
    }

}
