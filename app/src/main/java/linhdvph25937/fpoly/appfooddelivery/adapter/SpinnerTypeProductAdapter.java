package linhdvph25937.fpoly.appfooddelivery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import linhdvph25937.fpoly.appfooddelivery.R;
import linhdvph25937.fpoly.appfooddelivery.model.FoodType;

public class SpinnerTypeProductAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<FoodType> list;

    public SpinnerTypeProductAdapter(Context context, ArrayList<FoodType> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId().hashCode();
    }

    public class ViewHolder{
        TextView tvName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder bau;
        if (convertView == null){
            bau = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_item_spinner_type_product, null);

            bau.tvName = convertView.findViewById(R.id.tv_name_type_product_item);
            convertView.setTag(bau);
        }else{
            bau = (ViewHolder) convertView.getTag();
        }
        bau.tvName.setText(list.get(position).getName());
        return convertView;
    }
}
