package com.n1k0l4s.smartpurchaselist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.n1k0l4s.smartpurchaselist.ProductsActivity;
import com.n1k0l4s.smartpurchaselist.R;

import java.text.DecimalFormat;
import java.util.List;

import models.Product;
import mydatabase.ProductDB;
import mydatabase.SingleTone;

public class ProductAdapter extends BaseAdapter {
    private List<Product> products;
    private LayoutInflater layoutInflater;
    private ProductsActivity activity;


    public ProductAdapter(Context context, List<Product> products) {
        activity = (ProductsActivity) context;
        this.products = products;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return (Product) products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DecimalFormat myFormatter = new DecimalFormat("###,###.##");
        View view = convertView;
        if (view == null){
            view = layoutInflater.inflate(R.layout.product_item_layout, parent, false);
        }
        Product product = getProduct(position);
        // CheckBox
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
        checkBox.setChecked(product.isSelected());
        checkBox.setOnClickListener(l -> {
            ProductDB.updateSelected(product.getId(), checkBox.isChecked());
            refreshData();
        });
        //
        TextView title = (TextView) view.findViewById(R.id.titleLabel);
        title.setText(((product.getAmount() > 1) ? "x" + product.getAmount() + ": " : "") + product.getName());
        checkBox.setText(myFormatter.format(product.getPrice() * product.getAmount()));
        //
        return view;
    }


    private Product getProduct(int position) {
        return (Product) getItem(position);
    }

    public void refreshData(){
        int listId = SingleTone.getSingleTone().getIdOfSelectedList();
        products = SingleTone.getSingleTone().getProductList(listId);
        this.notifyDataSetChanged();
        //
        TextView textViewEmpty = (TextView) activity.findViewById(R.id.activityProductEmptyTextView);
        if (products.isEmpty())
            textViewEmpty.setText("No content");
        else
            textViewEmpty.setText("");
        //
        ProductsActivity.refreshInfo(SingleTone.getSingleTone().getShoppingLists(), SingleTone.getSingleTone(), (TextView) activity.findViewById(R.id.productActivityInfo));
    }

    public List<Product> getData() {
        return products;
    }
}

