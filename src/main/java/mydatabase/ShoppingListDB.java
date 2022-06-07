package mydatabase;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import constants.DBC;
import models.ShoppingList;

public class ShoppingListDB {
    // Getting list of lists
    static public List<ShoppingList> getShoppingLists(){
        SingleTone.getSingleTone().openDb();
        List<ShoppingList> shoppingLists = new ArrayList<>();
        Cursor cursor = SingleTone.getSingleTone().getDb().query(DBC.LIST_TABLE_NAME, null,null,null,null,null,null);
        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(DBC.LIST_ID));
            String name = cursor.getString(cursor.getColumnIndex(DBC.LIST_NAME));
            double selected = cursor.getDouble(cursor.getColumnIndex(DBC.LIST_SELECTED));
            double listPrice = cursor.getDouble(cursor.getColumnIndex(DBC.LIST_PRICE));
            shoppingLists.add(new ShoppingList(id, name, selected, listPrice));
        }
        cursor.close();
        SingleTone.getSingleTone().closeDb();
        return shoppingLists;
    }


    // Calculating Full price of the list
    static public void calcPrices(int listId){
        SingleTone singleTone = SingleTone.getSingleTone();
        double fullSum = 0;
        double selectedSum = 0;
        singleTone.openDb();
        // Calculating full list sum
        Cursor cursor = singleTone.getDb().query(
                DBC.PRODUCT_TABLE_NAME,
                null,
                DBC.LIST_ID_RELATION + " = ?",
                new String[]{String.valueOf(listId)},
                null,
                null,
                null);
        while (cursor.moveToNext()) {
            fullSum += cursor.getDouble(cursor.getColumnIndex(DBC.PRICE)) * cursor.getInt(cursor.getColumnIndex(DBC.AMOUNT));
        }
        cursor.close();


        // Calculating selected items sum
        singleTone.openDb();
        cursor = singleTone.getDb().query(
                DBC.PRODUCT_TABLE_NAME,
                null,
                DBC.LIST_ID_RELATION + " = ? AND " + DBC.IS_SELECTED + " = ?",
                new String[]{String.valueOf(listId), "1"},
                null,
                null,
                null);
        while (cursor.moveToNext())
            selectedSum += cursor.getDouble(cursor.getColumnIndex(DBC.PRICE)) * cursor.getInt(cursor.getColumnIndex(DBC.AMOUNT));
        cursor.close();
        singleTone.closeDb();


        // Update table
        singleTone.openDb();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBC.LIST_PRICE ,fullSum);
        contentValues.put(DBC.LIST_SELECTED ,selectedSum);
        singleTone.getDb().update(
                DBC.LIST_TABLE_NAME,
                contentValues,
                DBC.LIST_ID + " = ?",
                new String[]{String.valueOf(listId)}
        );
        singleTone.closeDb();
    }

    // Creating a new list
    static public void createNewList(String name){
        SingleTone.getSingleTone().openDb();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBC.LIST_NAME, name);
        contentValues.put(DBC.LIST_SELECTED, 0);
        contentValues.put(DBC.LIST_PRICE, 0);
        SingleTone.getSingleTone().getDb().insert(DBC.LIST_TABLE_NAME, null, contentValues);
        SingleTone.getSingleTone().closeDb();
    }

    // Removing the list
    static public void removeList(int id){
        SingleTone singleTone = SingleTone.getSingleTone();
        singleTone.openDb();
        singleTone.getDb().delete(DBC.PRODUCT_TABLE_NAME, DBC.LIST_ID_RELATION + " = ?", new String[]{String.valueOf(id)});
        singleTone.getDb().delete(DBC.LIST_TABLE_NAME, "_id = ?", new String[]{String.valueOf(id)});
        singleTone.closeDb();
    }

    public static void updateList(int id, String listName) {
        SingleTone singleTone = SingleTone.getSingleTone();
        singleTone.openDb();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBC.LIST_NAME, listName);
        singleTone.getDb().update(
                DBC.LIST_TABLE_NAME,
                contentValues,
                DBC.LIST_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
        singleTone.closeDb();
    }
}

