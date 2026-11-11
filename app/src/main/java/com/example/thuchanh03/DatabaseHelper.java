package com.example.thuchanh03;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "warehouse.db";
    public static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT NOT NULL UNIQUE, password TEXT NOT NULL)");
        db.execSQL("CREATE TABLE products (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT NOT NULL, price INTEGER NOT NULL, quantity INTEGER NOT NULL, "
                + "category TEXT NOT NULL, active INTEGER NOT NULL, description TEXT)");
        insertUser(db, "admin", "123456");
        insertProduct(db, new Product("Chuột không dây", 150000, 20, "Điện tử", true, ""));
        insertProduct(db, new Product("Bàn phím cơ", 450000, 3, "Điện tử", true, ""));
        insertProduct(db, new Product("Sổ tay A5", 25000, 40, "Văn phòng phẩm", true, ""));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS products");
        onCreate(db);
    }

    private long insertUser(SQLiteDatabase db, String username, String password) {
        ContentValues v = new ContentValues();
        v.put("username", username);
        v.put("password", password);
        return db.insert("users", null, v);
    }

    public long insertUser(String username, String password) {
        return insertUser(getWritableDatabase(), username, password);
    }

    public boolean checkLogin(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT id FROM users WHERE username = ? AND password = ?",
                new String[]{username, password});
        try {
            return c.moveToFirst();
        } finally {
            c.close();
        }
    }

    private long insertProduct(SQLiteDatabase db, Product p) {
        ContentValues v = new ContentValues();
        v.put("name", p.getName());
        v.put("price", p.getPrice());
        v.put("quantity", p.getQuantity());
        v.put("category", p.getCategory());
        v.put("active", p.isActive() ? 1 : 0);
        v.put("description", p.getDescription());
        return db.insert("products", null, v);
    }

    public long insertProduct(Product p) {
        return insertProduct(getWritableDatabase(), p);
    }

    private Product fromCursor(Cursor c) {
        return new Product(c.getInt(0), c.getString(1), c.getInt(2), c.getInt(3),
                c.getString(4), c.getInt(5) == 1, c.getString(6));
    }

    private static final String COLS = "id, name, price, quantity, category, active, description";

    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery("SELECT " + COLS + " FROM products ORDER BY id DESC", null);
        try {
            while (c.moveToNext()) list.add(fromCursor(c));
        } finally {
            c.close();
        }
        return list;
    }

    public Product getProductById(int id) {
        Cursor c = getReadableDatabase().rawQuery("SELECT " + COLS + " FROM products WHERE id = ?",
                new String[]{String.valueOf(id)});
        try {
            return c.moveToFirst() ? fromCursor(c) : null;
        } finally {
            c.close();
        }
    }

    public int updateQuantity(int id, int quantity) {
        ContentValues v = new ContentValues();
        v.put("quantity", quantity);
        return getWritableDatabase().update("products", v, "id = ?", new String[]{String.valueOf(id)});
    }

    public int deleteProduct(int id) {
        return getWritableDatabase().delete("products", "id = ?", new String[]{String.valueOf(id)});
    }

    public long getTotalValue() {
        Cursor c = getReadableDatabase().rawQuery("SELECT SUM(price * quantity) FROM products", null);
        try {
            return c.moveToFirst() ? c.getLong(0) : 0;
        } finally {
            c.close();
        }
    }

    public int countLowStock() {
        Cursor c = getReadableDatabase().rawQuery("SELECT COUNT(*) FROM products WHERE quantity < ?",
                new String[]{"5"});
        try {
            return c.moveToFirst() ? c.getInt(0) : 0;
        } finally {
            c.close();
        }
    }
}
