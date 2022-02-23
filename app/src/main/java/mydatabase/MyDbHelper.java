package mydatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import constants.DBC;

public class MyDbHelper extends SQLiteOpenHelper {
    public MyDbHelper(@Nullable Context context){
        super(context, DBC.DB_NAME, null, DBC.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBC.LIST_TABLE_STRUCTURE);
        db.execSQL(DBC.PRODUCT_TABLE_STRUCTURE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBC.PRODUCT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBC.LIST_TABLE_NAME);
        onCreate(db);
    }
}
