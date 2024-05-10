package neko.stationery.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String
            DATABASE_NAME = "nekostationery",
            TABLE_EMPLOYEES = "employees",
            KEY_EMPLOYEE_ID = "id",
            KEY_EMPLOYEE_NAME = "name",
            KEY_EMPLOYEE_EMAIL = "email",
            KEY_EMPLOYEE_PASSWORD = "password",
            TABLE_PRODUCT = "product",
            KEY_PRODUCT_ID = "id",
            KEY_PRODUCT_NAME = "name",
            KEY_PRODUCT_CATEGORY ="catagory",
            KEY_PRODUCT_SUPPLIER = "supplier",
            KEY_PRODUCT_STOCK = "stock";

    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EMPLOYEES_TABLE =
            "CREATE TABLE " + TABLE_EMPLOYEES + " ( "
                + KEY_EMPLOYEE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_EMPLOYEE_NAME + " TEXT NOT NULL, "
                + KEY_EMPLOYEE_EMAIL + " TEXT NOT NULL UNIQUE, "
                + KEY_EMPLOYEE_PASSWORD + " TEXT NOT NULL "
            + " ) ";
        db.execSQL(CREATE_EMPLOYEES_TABLE);

        String CREATE_PRODUCT_TABLE =
            "CREATE TABLE " + TABLE_PRODUCT + " ( "
                + KEY_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_PRODUCT_NAME + " TEXT NOT NULL, "
                + KEY_PRODUCT_CATEGORY + " TEXT NOT NULL, "
                + KEY_PRODUCT_SUPPLIER + " TEXT NOT NULL, "
                + KEY_PRODUCT_STOCK + " INTEGER NOT NULL "
            + " ) ";
        db.execSQL(CREATE_PRODUCT_TABLE);

        String INSERT_PRODUCT =
            "INSERT INTO " + TABLE_PRODUCT + " (" + KEY_PRODUCT_NAME + ", " + KEY_PRODUCT_CATEGORY + ", " + KEY_PRODUCT_SUPPLIER + ", " + KEY_PRODUCT_STOCK + ") VALUES"
                + "('Sarasa Clip Gel Pen 0.5mm', 'Pen', 'Zebra', 120), "
                + "('Sarasa Clip Gel Pen Vintage 0.5mm', 'Pen', 'Zebra', 100), "
                + "('Sarasa Clip Gel Pen Milky 0.5mm', 'Pen', 'Zebra', 80), "
                + "('Mildliner Double-Sided Highlighter', 'Highlighter', 'Zebra', 100), "
                + "('Kokuyo Campus Paper Loose B5', 'Loose Leaf', 'Kokuyo', 90);";
        db.execSQL(INSERT_PRODUCT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        onCreate(db);
    }

    public Employee authenticate(Employee employee) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EMPLOYEES + " WHERE " + KEY_EMPLOYEE_EMAIL + " = '" + employee.getEmail() + "'", null);

        if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
            Employee emp1 = new Employee(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3)
            );

            if (employee.getPassword().equalsIgnoreCase(emp1.getPassword())) {
                return emp1;
            }
        }
        return null;
    }

    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EMPLOYEES + " WHERE " + KEY_EMPLOYEE_EMAIL + " = '" + email + "'", null);

        return cursor != null && cursor.moveToFirst() && cursor.getCount() > 0;
    }

    public void addEmployee(Employee employee) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EMPLOYEE_NAME, employee.getName());
        values.put(KEY_EMPLOYEE_EMAIL, employee.getEmail());
        values.put(KEY_EMPLOYEE_PASSWORD, employee.getPassword());

        db.insert(TABLE_EMPLOYEES, null, values);
    }

    public void addProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SqliteHelper.KEY_PRODUCT_NAME, product.getName());
        values.put(SqliteHelper.KEY_PRODUCT_CATEGORY, product.getCategory());
        values.put(SqliteHelper.KEY_PRODUCT_SUPPLIER, product.getSupplier());
        values.put(SqliteHelper.KEY_PRODUCT_STOCK, product.getStock());

        db.insert(SqliteHelper.TABLE_PRODUCT, null, values);
    }

    public Product getProductById(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PRODUCT + " WHERE " + KEY_PRODUCT_ID + " = '" + id + "'", null);

        if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
            return new Product(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4)
            );
        }
        return null;
    }

    public Employee getEmployeeById(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EMPLOYEES + " WHERE " + KEY_EMPLOYEE_ID + " = '" + id + "'", null);

        if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
            return new Employee(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3)
            );
        }
        return null;
    }

    public String getEmployeePasswordById(String id) {
        String password = null;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + KEY_EMPLOYEE_PASSWORD + " FROM " + TABLE_EMPLOYEES + " WHERE " + KEY_EMPLOYEE_ID + " = '" + id + "'", null);

        if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            password = cursor.getString(0);
        }
        return password;
    }

    public void updateProduct(Product product, String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SqliteHelper.KEY_PRODUCT_NAME, product.getName());
        values.put(SqliteHelper.KEY_PRODUCT_CATEGORY, product.getCategory());
        values.put(SqliteHelper.KEY_PRODUCT_SUPPLIER, product.getSupplier());
        values.put(SqliteHelper.KEY_PRODUCT_STOCK, product.getStock());

        db.update(SqliteHelper.TABLE_PRODUCT, values, KEY_PRODUCT_ID + " = ?", new String[]{id});
    }

    public void updateEmployee(Employee employee, String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SqliteHelper.KEY_EMPLOYEE_NAME, employee.getName());
        values.put(SqliteHelper.KEY_EMPLOYEE_EMAIL, employee.getEmail());

        db.update(SqliteHelper.TABLE_EMPLOYEES, values, KEY_EMPLOYEE_ID + " = ?", new String[]{id});
    }

    public void updateEmployeePassword(Employee employee, String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SqliteHelper.KEY_EMPLOYEE_PASSWORD, employee.getPassword());

        db.update(SqliteHelper.TABLE_EMPLOYEES, values, KEY_EMPLOYEE_ID + " = ?", new String[]{id});
    }

    public void deleteProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_PRODUCT, KEY_PRODUCT_ID + " = ?", new String[]{product.getId()});
    }
}
