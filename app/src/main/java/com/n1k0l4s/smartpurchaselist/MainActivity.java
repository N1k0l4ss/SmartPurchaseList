package com.n1k0l4s.smartpurchaselist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.n1k0l4s.smartpurchaselist.adapter.PdListAdapter;

import java.util.ArrayList;
import java.util.List;

import models.ShoppingList;
import mydatabase.ShoppingListDB;
import mydatabase.SingleTone;

public class MainActivity extends AppCompatActivity {
    private SingleTone singleTone;
    private ListView mainListView;
    private PdListAdapter pdListAdapter;
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
            case R.id.delete:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setMessage("Do you really want to delete selected items?")
                        .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                        .setPositiveButton("Yes", ((dialog, which) -> {
                            getCheckedItems().stream().forEach(x-> ShoppingListDB.removeList(x.getId()));
                            pdListAdapter.refreshData();
                            clearChoices();
                        }));
                AlertDialog dialog = alert.create();
                dialog.setTitle("Confirm");
                dialog.show();
                break;
            case R.id.edit:
                ShoppingList selectedList = getCheckedItems().get(0);
                newListDialog(selectedList.getName(), true);
                break;
            case android.R.id.home:
                if (!getCheckedItems().isEmpty()) {
                    clearChoices();
                } else finish();
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
                pdListAdapter.refreshData();
                List<ShoppingList> results = new ArrayList<>();
                for (ShoppingList x : pdListAdapter.getData()) {
                    if (x.getName().toLowerCase().contains(newText.toLowerCase()))
                        results.add(x);
                }
                pdListAdapter.getData().clear();
                pdListAdapter.getData().addAll(results);
                pdListAdapter.notifyDataSetChanged();
                if (newText.isEmpty())
                    pdListAdapter.refreshData();
                return false;
            }
        });
        return true;
    }

    private void initialize(){
        mainListView = (ListView) findViewById(R.id.mainListView);
        singleTone = SingleTone.initializeSingleTone(this);
        viewsSelected = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!getCheckedItems().isEmpty()){
            clearChoices();
        }
        pdListAdapter.refreshData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        pdListAdapter = new PdListAdapter(this, singleTone.getShoppingLists());
        mainListView.setAdapter(pdListAdapter);
        mainListView.setOnItemClickListener((parent, view, position, id) -> {
            if (!viewsSelected.isEmpty()){
                mainListView.setItemChecked(position, !mainListView.isItemChecked(position));
                choiceItem(view, position);
            } else {
                Intent intent = new Intent(this, ProductsActivity.class);
                intent.putExtra("listName", pdListAdapter.getData().get(position).getName());
                startActivity(intent);
                singleTone.getProductList(singleTone.getShoppingListsWithoutRefresh().get(position).getId());
            }
        });

        mainListView.setOnItemLongClickListener((parent, view, position, id) -> {
            choiceItem(view, position);
            return true;
        });
    }

    public void newListClicked(View view) {
        newListDialog("", false);
    }

    private void newListDialog(String name, Boolean edited) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.new_list_activity);
        Button okBtn = (Button) dialog.findViewById(R.id.okBtn);
        Button cancelBtn = (Button) dialog.findViewById(R.id.cancelBtn);
        EditText listNameField = (EditText) dialog.findViewById(R.id.listNameField);
        listNameField.setText(name);
        cancelBtn.setOnClickListener(v -> { dialog.dismiss(); });
        okBtn.setOnClickListener(v -> {
            if (listNameField.getText().length() == 0) {
                Toast.makeText(this, "Field can't be empty!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!edited)
                ShoppingListDB.createNewList(listNameField.getText().toString());
            else{
                ShoppingListDB.updateList(getCheckedItems().get(0).getId(), listNameField.getText().toString());
                clearChoices();
            }
            pdListAdapter.refreshData();
            dialog.dismiss();
        });
        dialog.show();
    }

    // ListView Choicer

    private void choiceItem(View view, int position) {
        mainListView.setItemChecked(position, !mainListView.isItemChecked(position));
        List<ShoppingList> checkedItems = getCheckedItems();
        if (!checkedItems.isEmpty()){
            menu.clear();
            onCreateOptionsMenu(menu);
            getMenuInflater().inflate(R.menu.items_menu, menu);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (checkedItems.size() == 1)
                menu.findItem(R.id.edit).setVisible(true);
            else
                menu.findItem(R.id.edit).setVisible(false);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            menu.clear();
            onCreateOptionsMenu(menu);
        }
        if (mainListView.isItemChecked(position)){
            viewsSelected.add(view);
            view.setBackgroundColor(ContextCompat.getColor(this, R.color.choice_item_color));
        } else {
            view.setBackgroundColor(Color.TRANSPARENT);
            viewsSelected.remove(view);
        }
    }

    private List<ShoppingList> getCheckedItems() {
        List<ShoppingList> res = new ArrayList<>();
        for (int i = 0; i < pdListAdapter.getData().size(); i++) {
            if (mainListView.isItemChecked(i))
                res.add(pdListAdapter.getData().get(i));
        }
        return res;
    }

    private void clearChoices() {
        viewsSelected.stream().forEach(x -> x.setBackgroundColor(Color.TRANSPARENT));
        mainListView.clearChoices();
        viewsSelected.clear();
        if (menu != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            menu.clear();
            onCreateOptionsMenu(menu);
        }
    }
}
