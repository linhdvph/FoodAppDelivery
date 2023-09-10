package linhdvph25937.fpoly.appfooddelivery.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import linhdvph25937.fpoly.appfooddelivery.R;
import linhdvph25937.fpoly.appfooddelivery.model.Comment;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{
    private ArrayList<Comment> list;

    public CommentAdapter(ArrayList<Comment> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_comment_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment obj = list.get(position);
        if (obj == null){
            return;
        }
        if (obj.getObjUser().getName().equals("")){
            holder.tvUsername.setText("Ẩn danh");
        }else{
            holder.tvUsername.setText(obj.getObjUser().getName());
        }
        holder.tvContent.setText(obj.getContent());
        holder.tvDate.setText(obj.getDate());
    }

    @Override
    public int getItemCount() {
        if (list != null){
            return list.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUsername, tvContent, tvDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tv_user_name_comment);
            tvContent = itemView.findViewById(R.id.tv_content_comment);
            tvDate = itemView.findViewById(R.id.tv_date_comment);
        }
    }
}
