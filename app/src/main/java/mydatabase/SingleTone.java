package mydatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.n1k0l4s.smartpurchaselist.MainActivity;

import java.util.ArrayList;
import java.util.List;

import models.Product;
import models.ShoppingList;

public class SingleTone {
    private static SingleTone singleTone;
    private MyDbHelper myDbHelper;
    private SQLiteDatabase db;
    private Context context;
    private List<ShoppingList> shoppingLists;
    private List<Product> productList;
    private int idOfSelectedList;

    public static SingleTone initializeSingleTone(MainActivity mainActivity){
        singleTone = new SingleTone(mainActivity);
        return singleTone;
    }

    private SingleTone(MainActivity mainActivity) {
        productList = new ArrayList<>();
        shoppingLists = new ArrayList<>();
        context = mainActivity;
        myDbHelper = new MyDbHelper(context);
    } // constructor

    public static SingleTone getSingleTone() {
        return singleTone;
    } // get single tone static method

    public void openDb(){
        db = myDbHelper.getWritableDatabase();
    }

    public void closeDb(){ myDbHelper.close(); }

    // Getters

    public MyDbHelper getMyDbHelper() {
        return myDbHelper;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public List<Product> getProductList(int listId) {
        idOfSelectedList = listId;
        productList.clear();
        productList.addAll(ProductDB.getProductList(listId));
        return productList;
    }

    public List<ShoppingList> getShoppingLists() {
        shoppingLists.clear();
        shoppingLists.addAll(ShoppingListDB.getShoppingLists());
        return shoppingLists;
    }

    public int getIdOfSelectedList() {
        return idOfSelectedList;
    }

    public List<ShoppingList> getShoppingListsWithoutRefresh() {
        return shoppingLists;
    }

}
