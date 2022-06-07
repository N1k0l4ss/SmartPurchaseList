package mydatabase;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import constants.DBC;
import models.Product;

public class ProductDB {
    static public List<Product> getProductList(int receivedId){
        List<Product> productList = new ArrayList<>();
        SingleTone singleTone = SingleTone.getSingleTone();
        singleTone.openDb();
        Cursor cursor = singleTone.getDb().query(
                DBC.PRODUCT_TABLE_NAME,
                null,
                DBC.LIST_ID_RELATION + " = ?",
                new String[]{String.valueOf(receivedId)},
                null,
                null,
                null);
        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(DBC.PRODUCT_ID));
            String listName = cursor.getString(cursor.getColumnIndex(DBC.PRODUCT_NAME));
            double price = cursor.getDouble(cursor.getColumnIndex(DBC.PRICE));
            int selected = cursor.getInt(cursor.getColumnIndex(DBC.IS_SELECTED));
            int idRelation = cursor.getInt(cursor.getColumnIndex(DBC.LIST_ID_RELATION));
            int amount = cursor.getInt(cursor.getColumnIndex(DBC.AMOUNT));
            productList.add(new Product(id, listName,price, selected == 1, idRelation, amount));
        }
        cursor.close();
        singleTone.closeDb();
        return productList;
    }

    static public void createProduct(String name, double price, int listId, int amount){ // "insert into product (name , price, listId) values (?, ? ,?)"
        SingleTone.getSingleTone().openDb();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBC.PRODUCT_NAME, name);
        contentValues.put(DBC.PRICE, price);
        contentValues.put(DBC.LIST_ID_RELATION, listId);
        contentValues.put(DBC.AMOUNT, amount);
        contentValues.put(DBC.IS_SELECTED, 0);
        SingleTone.getSingleTone().getDb().insert(DBC.PRODUCT_TABLE_NAME, null, contentValues);
        SingleTone.getSingleTone().closeDb();
        ShoppingListDB.calcPrices(SingleTone.getSingleTone().getIdOfSelectedList());
    }

    static public void deleteProduct(int id){
        SingleTone singleTone = SingleTone.getSingleTone();
        singleTone.openDb();
        singleTone.getDb().delete(DBC.PRODUCT_TABLE_NAME, "_id = ?", new String[]{String.valueOf(id)});
        singleTone.closeDb();
        ShoppingListDB.calcPrices(singleTone.getIdOfSelectedList());
    }

    static public void updateSelected(int productId, boolean selected){
        int tempBool = 0;
        if (selected)
            tempBool = 1;
        SingleTone singleTone = SingleTone.getSingleTone();
        singleTone.openDb();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBC.IS_SELECTED, tempBool);
        singleTone.getDb().update(
                DBC.PRODUCT_TABLE_NAME,
                contentValues,
                DBC.PRODUCT_ID + " = ?",
                new String[]{String.valueOf(productId)}
        );
        singleTone.closeDb();
        ShoppingListDB.calcPrices(singleTone.getIdOfSelectedList());
    }

    public static void updateProduct(int id, String name, Double price, int amount) {
        SingleTone singleTone = SingleTone.getSingleTone();
        singleTone.openDb();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBC.PRODUCT_NAME, name);
        contentValues.put(DBC.PRICE, price);
        contentValues.put(DBC.AMOUNT, amount);
        singleTone.getDb().update(
                DBC.PRODUCT_TABLE_NAME,
                contentValues,
                DBC.PRODUCT_ID + " = ?",
                new String[]{String.valueOf(id)}
                );
        singleTone.closeDb();
        ShoppingListDB.calcPrices(singleTone.getIdOfSelectedList());
    }
}

