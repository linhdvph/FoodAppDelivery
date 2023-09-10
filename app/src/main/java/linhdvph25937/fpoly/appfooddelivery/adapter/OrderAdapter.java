package linhdvph25937.fpoly.appfooddelivery.adapter;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import linhdvph25937.fpoly.appfooddelivery.R;
import linhdvph25937.fpoly.appfooddelivery.activity.DetailOrderActivity;
import linhdvph25937.fpoly.appfooddelivery.activity.MainActivity;
import linhdvph25937.fpoly.appfooddelivery.fragment.CartFragment;
import linhdvph25937.fpoly.appfooddelivery.model.Comment;
import linhdvph25937.fpoly.appfooddelivery.model.Order;
import linhdvph25937.fpoly.appfooddelivery.model.State;
import linhdvph25937.fpoly.appfooddelivery.ultil.MyRetrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public static final int LAYOUT_ADMIN = 1;
    public static final int LAYOUT_USER = 2;

    private int mCurrentLayout = LAYOUT_USER;
    private ArrayList<Order> list;

    public OrderAdapter(ArrayList<Order> list,int mCurrentLayout) {
        this.mCurrentLayout = mCurrentLayout;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       if (mCurrentLayout == LAYOUT_USER){
           View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_order, parent, false);
           return new UserViewHolder(view);
       }else{
           View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_order_admin, parent, false);
           return new AdminViewHolder(view);
       }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Order obj = list.get(position);
        if (obj == null){
            return;
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        if (mCurrentLayout == LAYOUT_USER){
            UserViewHolder userViewHolder = (UserViewHolder) holder;
            userViewHolder.tvTypeProduct.setText(obj.getListProductOrder().get(0).getObjFood().getIdTypeProduct().getName());
            userViewHolder.tvDate.setText(obj.getDate());
            userViewHolder.tvNameProduct.setText(obj.getListProductOrder().get(0).getObjFood().getName());
            userViewHolder.tvAddressProduct.setText(obj.getListProductOrder().get(0).getObjFood().getAddress());
            userViewHolder.tvTotalPrice.setText(decimalFormat.format(obj.getTotalPrice())+"đ");
            userViewHolder.tvQuantityOrderAndPaymentMethod.setText(obj.getListProductOrder().get(0).getQuantityOrder()+" món - " +obj.getPaymentMethod());
            Glide.with(userViewHolder.imgProduct.getContext())
                    .load(obj.getListProductOrder().get(0).getObjFood().getAvatar())
                    .into(userViewHolder.imgProduct);
            if (obj.getState() == 0){
                userViewHolder.tvState.setText("Chờ xác nhận");
                userViewHolder.btnEvaluate.setVisibility(View.INVISIBLE);
            }else if(obj.getState() == 1){
                userViewHolder.tvState.setText("Đang giao hàng");
                userViewHolder.btnEvaluate.setVisibility(View.INVISIBLE);
            }else{
                userViewHolder.tvState.setText("Giao hàng thành công");
                userViewHolder.btnEvaluate.setVisibility(View.VISIBLE);
            }
            userViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(userViewHolder.itemView.getContext(), DetailOrderActivity.class);
                    intent.putExtra("obj_order", obj);
                    userViewHolder.itemView.getContext().startActivity(intent);
                }
            });

            userViewHolder.btnEvaluate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialog = new Dialog(userViewHolder.itemView.getContext(), android.R.style.Theme_DeviceDefault_Light_DarkActionBar);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.layout_dialog_comment);
                    Window window = dialog.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);

                    ImageView imgArrowBack = dialog.findViewById(R.id.img_arrow_back_comment);
                    ImageView imgFood = dialog.findViewById(R.id.img_food_comment);
                    TextView tvNameFood = dialog.findViewById(R.id.tv_name_product_comment);
                    EditText edContent = dialog.findViewById(R.id.ed_content_commnet);
                    Button btnSend = dialog.findViewById(R.id.btn_send_comment);

                    Glide.with(userViewHolder.itemView.getContext())
                            .load(obj.getListProductOrder().get(0).getObjFood().getAvatar())
                            .into(imgFood);
                    tvNameFood.setText(obj.getListProductOrder().get(0).getObjFood().getName());
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
                            MyRetrofit.retrofit.addComment(obj.getIdUser().getId(), obj.getListProductOrder().get(0).getObjFood().getId(), content, getCurrentDateTime())
                                    .enqueue(new Callback<Comment>() {
                                        @Override
                                        public void onResponse(Call<Comment> call, Response<Comment> response) {
                                            if (response.body() != null){
                                                Toast.makeText(userViewHolder.itemView.getContext(), "Bạn đã đánh giá thành công.", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }else{
                                                Toast.makeText(userViewHolder.itemView.getContext(), "Không thể thêm đánh giá thành công.", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Comment> call, Throwable t) {
                                            Log.e("ErrorAddComment", "onFailure: " + t);
                                            Toast.makeText(userViewHolder.itemView.getContext(), "Lỗi server khi thêm comment.", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    });
                        }
                    });
                    dialog.show();
                }
            });
        }else{
            AdminViewHolder adminViewHolder = (AdminViewHolder) holder;
            adminViewHolder.tvUsername.setText(obj.getIdUser().getName());
            adminViewHolder.tvDate.setText(obj.getDate());
            adminViewHolder.tvAddressUser.setText(obj.getIdUser().getAddress());
            adminViewHolder.tvPhoneNumber.setText(obj.getIdUser().getPhoneNumber());
            adminViewHolder.tvTotalPrice.setText(decimalFormat.format(obj.getTotalPrice())+"đ");
            adminViewHolder.tvQuantityOrderAndPaymentMethod.setText(obj.getListProductOrder().get(0).getQuantityOrder()+" món - " +obj.getPaymentMethod());
            Glide.with(adminViewHolder.imgProduct.getContext())
                    .load(obj.getListProductOrder().get(0).getObjFood().getAvatar())
                    .into(adminViewHolder.imgProduct);
            if (obj.getState() == 0){
                adminViewHolder.tvState.setText("Chờ xác nhận");
                adminViewHolder.btnSetState.setVisibility(View.VISIBLE);
            }else if(obj.getState() == 1){
                adminViewHolder.tvState.setText("Đang giao hàng");
                adminViewHolder.btnSetState.setVisibility(View.VISIBLE);
            }else{
                adminViewHolder.tvState.setText("Giao hàng thành công");
                adminViewHolder.btnSetState.setVisibility(View.GONE);
            }
            adminViewHolder.btnSetState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialog = new Dialog(v.getContext());
                    dialog.setContentView(R.layout.layout_dialog_update_state_order);
                    Window window = dialog.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                    ArrayList<State> states = new ArrayList<>();
                    states.add(new State(0, "Chờ xác nhận"));
                    states.add(new State(1, "Đang giao hàng"));
                    states.add(new State(2, "Giao hàng thành công"));
                    ArrayAdapter<State> adapterState = new ArrayAdapter<>(v.getContext(), android.R.layout.simple_list_item_1, states);
                    adapterState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Spinner spinner = dialog.findViewById(R.id.spinner_update_state_order);
                    spinner.setAdapter(adapterState);
                    Button btnUpdate = dialog.findViewById(R.id.btn_update_state_order);
                    Button btnCancel = dialog.findViewById(R.id.btn_cancel_update_state_order);
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    btnUpdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            State objState = (State) spinner.getSelectedItem();
                            int idState = objState.getId();
                            MyRetrofit.retrofit.updateStateOrder(obj.getId(), obj.getIdUser().getId(), idState, getCurrentDateTime())
                                    .enqueue(new Callback<Order>() {
                                        @Override
                                        public void onResponse(Call<Order> call, Response<Order> response) {
                                            if (response.body() != null){
                                                Toast.makeText(adminViewHolder.itemView.getContext(), "Cập nhật trạng thái thành công.", Toast.LENGTH_SHORT).show();
                                                notifyDataSetChanged();
                                                dialog.dismiss();
                                            }else{
                                                Toast.makeText(adminViewHolder.itemView.getContext(), "Không thể Cập nhật trạng thái thành công.", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Order> call, Throwable t) {
                                            Log.e("ErrorUpdateState", "onFailure: " + t);
                                            Toast.makeText(adminViewHolder.itemView.getContext(), "Lỗi server khi cập nhật trạng thái.", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    });
                        }
                    });
                    dialog.show();
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

    private String getCurrentDateTime(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        String currentDateTime = simpleDateFormat.format(date);
        return currentDateTime;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTypeProduct, tvDate, tvNameProduct, tvAddressProduct, tvTotalPrice;
        private TextView tvQuantityOrderAndPaymentMethod, tvState;
        private ImageView imgProduct;
        private Button btnEvaluate;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTypeProduct = itemView.findViewById(R.id.tv_type_product_item_order);
            tvDate = itemView.findViewById(R.id.tv_date_item_order);
            tvNameProduct = itemView.findViewById(R.id.tv_name_product_item_order);
            tvAddressProduct = itemView.findViewById(R.id.tv_address_product_item_order);
            tvTotalPrice = itemView.findViewById(R.id.tv_total_price_item_order);
            tvQuantityOrderAndPaymentMethod = itemView.findViewById(R.id.tv_quantity_order_and_payment_method_item_order);
            tvState = itemView.findViewById(R.id.tv_state_item_order);
            imgProduct = itemView.findViewById(R.id.img_food_item_order);
            btnEvaluate = itemView.findViewById(R.id.btn_evaluate_item_order);
        }
    }

    public class AdminViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUsername, tvDate, tvAddressUser, tvPhoneNumber, tvTotalPrice;
        private TextView tvQuantityOrderAndPaymentMethod, tvState;
        private ImageView imgProduct;
        private Button btnSetState;
        public AdminViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUsername = itemView.findViewById(R.id.tv_username_item_order_admin);
            tvDate = itemView.findViewById(R.id.tv_date_item_order_admin);
            tvAddressUser = itemView.findViewById(R.id.tv_address_user_item_order_admin);
            tvPhoneNumber = itemView.findViewById(R.id.tv_phone_number_item_order_admin);
            tvTotalPrice = itemView.findViewById(R.id.tv_total_price_item_order_admin);
            tvQuantityOrderAndPaymentMethod = itemView.findViewById(R.id.tv_quantity_order_and_payment_method_item_order_admin);
            tvState = itemView.findViewById(R.id.tv_state_item_order_admin);
            imgProduct = itemView.findViewById(R.id.img_food_item_order_admin);
            btnSetState = itemView.findViewById(R.id.btn_update_item_order_admin);
        }
    }
}
