package constants;

public class DBC {
    public static final String LIST_TABLE_NAME = "shopping_list";
    public static final String LIST_ID = "_id";
    public static final String LIST_NAME = "list_name";
    public static final String LIST_SELECTED = "selected";
    public static final String LIST_PRICE = "list_price";

    public static final String PRODUCT_TABLE_NAME = "product";
    public static final String PRODUCT_ID = "_id";
    public static final String PRODUCT_NAME = "name";
    public static final String PRICE = "price";
    public static final String IS_SELECTED = "is_selected";
    public static final String LIST_ID_RELATION = "list_id";

    public static final String DB_NAME = "smart_purchase_list.db";
    public static final int DB_VERSION = 4;

    public static final String LIST_TABLE_STRUCTURE = "CREATE TABLE IF NOT EXISTS " +
            LIST_TABLE_NAME + " (" + LIST_ID + " INTEGER PRIMARY KEY, " +
            LIST_NAME + " TEXT, " + LIST_SELECTED + " DOUBLE, " + LIST_PRICE + " DOUBLE)";

    public static final String PRODUCT_TABLE_STRUCTURE = "CREATE TABLE IF NOT EXISTS " +
            PRODUCT_TABLE_NAME + " (" + PRODUCT_ID + " INTEGER PRIMARY KEY, " +
            PRODUCT_NAME + " TEXT, " + PRICE + " DOUBLE, " + IS_SELECTED + " BOOLEAN, " + LIST_ID_RELATION + " INTEGER)";
}
