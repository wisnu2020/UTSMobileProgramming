package neko.stationery.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import neko.stationery.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyListView extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    List<Product> productList;
    ArrayList<Product> arrayList;

    public MyListView(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
        inflater = LayoutInflater.from(context);
        this.arrayList = new ArrayList<>();
        this.arrayList.addAll(productList);
    }

    public class ViewHolder {
        TextView titleText, subtitleText;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.mylist, null);

            holder.titleText = convertView.findViewById(R.id.title);
            holder.subtitleText = convertView.findViewById(R.id.subtitle);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.titleText.setText(productList.get(position).getName());
        holder.subtitleText.setText(productList.get(position).getStock());

        return convertView;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        productList.clear();
        if (charText.length() == 0) {
            productList.addAll(arrayList);
        } else {
            for (Product product : arrayList) {
                if (product.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    productList.add(product);
                }
            }
        }
        notifyDataSetChanged();
    }
}
