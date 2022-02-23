package com.n1k0l4s.smartpurchaselist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.n1k0l4s.smartpurchaselist.MainActivity;
import com.n1k0l4s.smartpurchaselist.R;

import java.text.DecimalFormat;
import java.util.List;

import models.ShoppingList;
import mydatabase.SingleTone;

public class PdListAdapter extends BaseAdapter {
    private List<ShoppingList> list;
    private LayoutInflater layoutInflater;
    private MainActivity activity;

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    public PdListAdapter(Context context, List<ShoppingList> list) {
        activity = (MainActivity) context;
        this.list = list;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DecimalFormat myFormatter = new DecimalFormat("###,###.##");
        View view = convertView;
        if (view == null){
            view = layoutInflater.inflate(R.layout.main_item_layout, parent, false);
        }
        ShoppingList shoppingList = getList(position);
        //
        TextView listName = (TextView) view.findViewById(R.id.listName);
        listName.setText(shoppingList.getName());
        //
        TextView fullPrice = (TextView) view.findViewById(R.id.fullPrice);
        fullPrice.setText("Price of the list: " + myFormatter.format(shoppingList.getListPrice()));
        //
        TextView selectedPrice = (TextView) view.findViewById(R.id.selectedPrice);
        selectedPrice.setText("Price of selected: " + myFormatter.format(shoppingList.getSelectedPrice()));
        //
        return view;
    }

    private ShoppingList getList(int position) {
        return (ShoppingList) getItem(position);
    }

    public List<ShoppingList> getData() {
        return list;
    }

    public void refreshData() {
        list = SingleTone.getSingleTone().getShoppingLists();
        this.notifyDataSetChanged();
        //
        TextView textViewEmpty = (TextView) activity.findViewById(R.id.activityMainEmptyTextView);
        if (list.isEmpty())
            textViewEmpty.setText("No content");
        else
            textViewEmpty.setText("");
    }
}
