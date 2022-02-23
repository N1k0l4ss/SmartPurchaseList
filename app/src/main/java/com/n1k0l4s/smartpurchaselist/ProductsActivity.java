package com.n1k0l4s.smartpurchaselist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.n1k0l4s.smartpurchaselist.adapter.ProductAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import models.Product;
import models.ShoppingList;
import mydatabase.ProductDB;
import mydatabase.SingleTone;

public class ProductsActivity extends AppCompatActivity {
    private ListView productListView;
    private SingleTone singleTone;
    private ProductAdapter adapter;
    private TextView info;
    private Menu menu;
    private List<View> viewsSelected;

    @Override
    public void onBackPressed() {
        if (!getCheckedItems().isEmpty()) {
            clearChoices();
        } else
            super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if (!getCheckedItems().isEmpty()) {
                    clearChoices();
                } else finish();
                break;
            case R.id.edit:
                Product selectedProduct = getCheckedItems().get(0);
                newProductDialog(selectedProduct.getName(), String.valueOf(selectedProduct.getPrice()), true);
                break;
            case R.id.delete:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setMessage("Do you really want to delete selected items?")
                        .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                        .setPositiveButton("Yes", ((dialog, which) -> {
                            getCheckedItems().stream().forEach(x-> ProductDB.deleteProduct(x.getId()));
                            adapter.refreshData();
                            clearChoices();
                        }));
                AlertDialog dialog = alert.create();
                dialog.setTitle("Confirm");
                dialog.show();
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type here to search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.refreshData();
                List<Product> results = new ArrayList<>();
                for (Product x : adapter.getData()) {
                    if (x.getName().toLowerCase().contains(newText.toLowerCase()))
                        results.add(x);
                }
                adapter.getData().clear();
                adapter.getData().addAll(results);
                adapter.notifyDataSetChanged();
                if (newText.isEmpty())
                    adapter.refreshData();
                return false;
            }
        });
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        viewsSelected = new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("List: " + getIntent().getStringExtra("listName"));
        productListView = (ListView) findViewById(R.id.productsListView);
        info = (TextView) findViewById(R.id.productActivityInfo);
        singleTone = SingleTone.getSingleTone();
        fillInfo();
        // Adapter
        adapter = new ProductAdapter(this, singleTone.getProductList(singleTone.getIdOfSelectedList()));
        productListView.setAdapter(adapter);
        adapter.refreshData();

        productListView.setOnItemClickListener((parent, view, position, id) -> {
            if (!viewsSelected.isEmpty()){
                productListView.setItemChecked(position, !productListView.isItemChecked(position));
                choiceItem(view, position);
            }
        });
        //
        productListView.setOnItemLongClickListener((parent, view, position, id) -> {
            choiceItem(view, position);
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!getCheckedItems().isEmpty())
            clearChoices();
    }

    private void fillInfo() {
        List<ShoppingList> localShoppingLists = singleTone.getShoppingLists();
        refreshInfo(localShoppingLists, singleTone, info);
    }

    public static void refreshInfo(List<ShoppingList> localShoppingLists, SingleTone singleTone, TextView info) {
        List<ShoppingList> collect = localShoppingLists.stream().filter(x -> x.getId() == singleTone.getIdOfSelectedList()).collect(Collectors.toList());
        DecimalFormat myFormatter = new DecimalFormat("###,###.##");
        info.setText("Price of selected: " + myFormatter.format(collect.get(0).getSelectedPrice()) + "\t Price of the list: " + myFormatter.format(collect.get(0).getListPrice()));
    }

    public void newProductClicked(View view) {
        newProductDialog("", "", false);
    }

    private void newProductDialog(String pdName, String pdPrice, boolean edited) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.new_product_activity);
        Button okBtn = (Button) dialog.findViewById(R.id.addNewProductBtn);
        Button cancelBtn = (Button) dialog.findViewById(R.id.cancelNewProduct);
        EditText productNameField = (EditText) dialog.findViewById(R.id.productNameField);
        EditText priceField = (EditText) dialog.findViewById(R.id.priceNewProductField);
        productNameField.setText(pdName);
        priceField.setText(pdPrice);
        cancelBtn.setOnClickListener(v -> { dialog.dismiss(); });
        okBtn.setOnClickListener(v -> {
            if (productNameField.getText().length() == 0 || priceField.getText().length() == 0) {
                Toast.makeText(this, "Fields can't be empty!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!edited)
                ProductDB.createProduct(productNameField.getText().toString(), Double.parseDouble(priceField.getText().toString()), singleTone.getIdOfSelectedList());
            else {
                ProductDB.updateProduct(getCheckedItems().get(0).getId(), productNameField.getText().toString(), Double.valueOf(priceField.getText().toString()));
                clearChoices();
            }
            adapter.refreshData();
            fillInfo();
            dialog.dismiss();
        });
        dialog.show();
    }

    // Listview checking
    private List<Product> getCheckedItems() {
        List<Product> res = new ArrayList<>();
        for (int i = 0; i < adapter.getData().size(); i++) {
            if (productListView.isItemChecked(i))
                res.add(adapter.getData().get(i));
        }
        return res;
    }

    private void clearChoices() {
        viewsSelected.stream().forEach(x -> x.setBackgroundColor(Color.TRANSPARENT));
        productListView.clearChoices();
        viewsSelected.clear();
        if (menu != null){
            menu.clear();
            onCreateOptionsMenu(menu);
        }
    }

    private void choiceItem(View view, int position) {
        productListView.setItemChecked(position, !productListView.isItemChecked(position));
        List<Product> checkedItems = getCheckedItems();
        if (!checkedItems.isEmpty()){
            menu.clear();
            onCreateOptionsMenu(menu);
            getMenuInflater().inflate(R.menu.items_menu, menu);
            if (checkedItems.size() == 1)
                menu.findItem(R.id.edit).setVisible(true);
            else
                menu.findItem(R.id.edit).setVisible(false);
        } else {
            menu.clear();
            onCreateOptionsMenu(menu);
        }
        if (productListView.isItemChecked(position)){
            viewsSelected.add(view);
            view.setBackgroundColor(ContextCompat.getColor(this, R.color.choice_item_color));
        } else {
            view.setBackgroundColor(Color.TRANSPARENT);
            viewsSelected.remove(view);
        }
    }
}