package linhdvph25937.fpoly.appfooddelivery.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import linhdvph25937.fpoly.appfooddelivery.R;
import linhdvph25937.fpoly.appfooddelivery.activity.SearchAndFilterActivity;
import linhdvph25937.fpoly.appfooddelivery.model.FoodType;

public class FoodTypeAdapter extends RecyclerView.Adapter<FoodTypeAdapter.ViewHolder>{
    private ArrayList<FoodType> list;

    public FoodTypeAdapter(ArrayList<FoodType> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_food_type, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodType obj = list.get(position);
        if (obj == null){
            return;
        }
        holder.tvName.setText(obj.getName());
        Glide.with(holder.itemView.getContext())
                .load(obj.getAvatar())
                .into(holder.img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), SearchAndFilterActivity.class);
                intent.putExtra("obj_food_type", obj);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list != null){
            return list.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name_food_type_item_one);
            img = itemView.findViewById(R.id.img_food_type_item_one);
        }
    }
}
